package org.digijava.kernel.ampapi.endpoints.activity;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;

import org.apache.log4j.Logger;
import org.dgfoundation.amp.ar.viewfetcher.RsInfo;
import org.dgfoundation.amp.ar.viewfetcher.SQLUtils;
import org.digijava.kernel.ampapi.endpoints.errors.ApiError;
import org.digijava.kernel.ampapi.endpoints.errors.ApiErrorMessage;
import org.digijava.kernel.ampapi.endpoints.util.JsonBean;
import org.digijava.kernel.persistence.PersistenceManager;
import org.digijava.kernel.request.TLSUtils;
import org.digijava.module.aim.annotations.interchange.Interchangeable;
import org.digijava.module.aim.annotations.interchange.InterchangeableDiscriminator;
import org.digijava.module.aim.dbentity.AmpActivityFields;
import org.digijava.module.aim.dbentity.AmpActivityProgram;
import org.digijava.module.aim.dbentity.AmpActivitySector;
import org.digijava.module.aim.dbentity.AmpComponentFunding;
import org.digijava.module.aim.dbentity.AmpSector;
import org.digijava.module.aim.dbentity.AmpTheme;
import org.digijava.module.aim.helper.Constants;
import org.digijava.module.aim.helper.TeamMember;
import org.digijava.module.categorymanager.dbentity.AmpCategoryValue;
import org.hibernate.jdbc.Work;

import clover.org.apache.commons.lang.StringUtils;


/**
 * AMP Activity Endpoint for Possible Values -- /activity/fields/:fieldName
 * and all the methods only it might have use of
 * 
 * @author acartaleanu
 */





public class PossibleValuesEnumerator {
	
	public static final Logger LOGGER = Logger.getLogger(PossibleValuesEnumerator.class);
	
	
	public static List<JsonBean> getPossibleValuesForField(String longFieldName, Class<?> clazz) {

		String fieldName = "";
		if (longFieldName.contains("~")) {
			/*
			 * we might have a field containing a discriminator description,
			 * but what we actually need is underneath -> obtain name of the field underneath
			 * */
			fieldName = longFieldName.substring(0, longFieldName.indexOf('~') );
			Field field = getPotentiallyDiscriminatedField(clazz, fieldName);
			if (field == null) {
				List<JsonBean> result = new ArrayList<JsonBean>();
				result.add(ApiError.toError(new ApiErrorMessage(ActivityErrors.FIELD_INVALID, fieldName)));
				return result;
			} else {
				return getPossibleValuesForField(longFieldName.substring(longFieldName.indexOf('~') + 1), InterchangeUtils.getClassOfField(field));
			}
		} else {
			/*
			 * the last field might contain discriminated values
			 * if it is such a field, it's a special case for each class
			 * 
			 * */
			Field finalField = getPotentiallyDiscriminatedField(clazz, longFieldName);
			if (finalField == null) {
				List<JsonBean> result = new ArrayList<JsonBean>();
				result.add(ApiError.toError(new ApiErrorMessage(ActivityErrors.FIELD_INVALID, fieldName)));
				return result;
			} else {
				if (InterchangeUtils.isCompositeField(finalField)) {
					return getPossibleValuesForComplexField(finalField, longFieldName);
				}

				return getPossibleValuesForField(finalField);
			}
		}
	}
	private static List<JsonBean> getPossibleValuesForComplexField(Field field, String originalName) {
		List<JsonBean> result = new ArrayList<JsonBean>();
		/*AmpActivitySector || AmpComponentFunding || AmpActivityProgram*/
		if (InterchangeUtils.getClassOfField(field).equals(AmpSector.class) || InterchangeUtils.getClassOfField(field).equals(AmpActivitySector.class)) {
			String configValue = getConfigValue(originalName, field);
			if (configValue == null) {
				/* FIXME: 
				result.add(ApiError.toError(new ApiErrorMessage(ActivityErrors.SOME_OTHER_ERROR, field.getName())));
				*/
				return result;
			}
			List<Object> sectors = InterchangeUtils.getClassOfField(field).equals(AmpSector.class) ? getSectorList(configValue) : getActivitySectorList(configValue);
			for (Object sector : sectors) {
				JsonBean item = null;
				try {
					item = setProperties(sector);
				} catch (Exception exc) {
					LOGGER.error(exc.getMessage());
					throw new RuntimeException(exc);
				}
				result.add(item);
				
			}
			
		} else if (InterchangeUtils.getClassOfField(field).equals(AmpTheme.class) || InterchangeUtils.getClassOfField(field).equals(AmpActivityProgram.class)) {
			String configValue = getConfigValue(originalName, field);
			if (configValue == null) {
				result.add(ApiError.toError(new ApiErrorMessage(ActivityErrors.WRONG_PROGRAM_TYPE, field.getName())));
				return result;
			}
			List<Object> themes = InterchangeUtils.getClassOfField(field).equals(AmpTheme.class) ? getThemeList(configValue) : getActivityProgramList(configValue);
			for (Object theme : themes) {
				JsonBean item = null;
				try {
					item = setProperties(theme);
				} catch (Exception exc) {
					result.add(ApiError.toError(new ApiErrorMessage(ActivityErrors.CANNOT_GET_PROPERTIES, field.getName())));
				}
				result.add(item);
				
			}

		} else {
			
//			result.add(ApiError.toError(new ApiErrorMessage(ActivityErrors.FIELD_INVALID, field.getName() + "/config:" + originalName)));
//			return result;
		}
		return result;
		
	}
	private static String getConfigValue(String longFieldName, Field field) {
		InterchangeableDiscriminator ant = field.getAnnotation(InterchangeableDiscriminator.class);
		for (Interchangeable inter : ant.settings()) {
			if (inter.fieldTitle().equals(InterchangeUtils.deunderscorify(longFieldName))) {
				return inter.discriminatorOption();
			}
		}
		return null;
	}
	
	private static List<Object> getActivitySectorList(final String configType) {
//		List<AmpSector> result = new ArrayList<AmpSector>();
		final List<Long> sectorIds = new ArrayList<Long>();
		PersistenceManager.getSession().doWork(new Work() {
			public void execute(Connection conn) throws SQLException {
				String allSectorsQuery = "SELECT amp_sector_id FROM all_sectors_with_levels WHERE sector_config_name=" + 
										 "'" + configType +"'" ;
				try (RsInfo rsi = SQLUtils.rawRunQuery(conn, allSectorsQuery, null)) {
					ResultSet rs = rsi.rs;
					while (rs.next()) 
						sectorIds.add(rs.getLong("amp_sector_id"));
					rs.close();
				}
			}
		});		
		
		String ids = StringUtils.join(sectorIds, ",");
		String queryString = "select ast from " + AmpActivitySector.class.getName() + " ast where ast.sectorId.ampSectorId in (" + ids + ")";
		List<Object> objectList = PersistenceManager.getSession().createQuery(queryString).list();
		return objectList;		
	}
	
	
	private static List<Object> getSectorList(final String configType) {
//		List<AmpSector> result = new ArrayList<AmpSector>();
		final List<Long> sectorIds = new ArrayList<Long>();
		PersistenceManager.getSession().doWork(new Work() {
			public void execute(Connection conn) throws SQLException {
				String allSectorsQuery = "SELECT amp_sector_id FROM all_sectors_with_levels WHERE sector_config_name=" + 
										 "'" + configType +"'" ;
				try (RsInfo rsi = SQLUtils.rawRunQuery(conn, allSectorsQuery, null)) {
					ResultSet rs = rsi.rs;
					while (rs.next()) 
						sectorIds.add(rs.getLong("amp_sector_id"));
					rs.close();
				}
			}
		});		
		
		String ids = StringUtils.join(sectorIds, ",");
		String queryString = "select ast from " + AmpSector.class.getName() + " ast where ast.ampSectorId in (" + ids + ")";
		List<Object> objectList = PersistenceManager.getSession().createQuery(queryString).list();
		return objectList;
	}
	private static List<Object> getActivityProgramList(final String configType) {
//		List<AmpSector> result = new ArrayList<AmpSector>();
		final List<Long> programIds = new ArrayList<Long>();
		PersistenceManager.getSession().doWork(new Work() {
			public void execute(Connection conn) throws SQLException {
				String allSectorsQuery = "SELECT amp_theme_id FROM all_programs_with_levels WHERE program_setting_name=" + 
										 "'" + configType +"'" ;
				try (RsInfo rsi = SQLUtils.rawRunQuery(conn, allSectorsQuery, null)) {
					ResultSet rs = rsi.rs;
					while (rs.next()) 
						programIds.add(rs.getLong("amp_theme_id"));
					rs.close();
				}
			}
		});		
		
		String ids = StringUtils.join(programIds, ",");
		String queryString = "select ast from " + AmpActivityProgram.class.getName() + " ast where ast.program.ampThemeId in (" + ids + ")";
		List<Object> objectList = PersistenceManager.getSession().createQuery(queryString).list();
		return objectList;
	}
	private static List<Object> getThemeList(final String configType) {
//		List<AmpSector> result = new ArrayList<AmpSector>();
		final List<Long> programIds = new ArrayList<Long>();
		PersistenceManager.getSession().doWork(new Work() {
			public void execute(Connection conn) throws SQLException {
				String allSectorsQuery = "SELECT amp_theme_id FROM all_programs_with_levels WHERE program_setting_name=" + 
										 "'" + configType +"'" ;
				try (RsInfo rsi = SQLUtils.rawRunQuery(conn, allSectorsQuery, null)) {
					ResultSet rs = rsi.rs;
					while (rs.next()) 
						programIds.add(rs.getLong("amp_theme_id"));
					rs.close();
				}
			}
		});		
		
		String ids = StringUtils.join(programIds, ",");
		String queryString = "select ast from " + AmpTheme.class.getName() + " ast where ast.ampThemeId in (" + ids + ")";
		List<Object> objectList = PersistenceManager.getSession().createQuery(queryString).list();
		return objectList;
	}
	private static Field getPotentiallyDiscriminatedField(Class<?> clazz, String fieldName){ 
		Field field = getField(clazz, InterchangeUtils.deunderscorify(fieldName));
		if (field == null) {
			//attempt to check if it's a composite field
			String discriminatedFieldName = InterchangeUtils.getDiscriminatedFieldTitle(fieldName);
			if (discriminatedFieldName != null)
				return getField(clazz, discriminatedFieldName);
		}
		return field;
	}
	private static Field getField(Class<?> clazz, String fieldname) {
		for (Field field: clazz.getDeclaredFields()) {
			Interchangeable ant = field.getAnnotation(Interchangeable.class);
			if (ant != null) {
//				if (ant.descend()) {
//					return getField(InterchangeUtils.getClassOfField(field), fieldname);
//				}
				if (fieldname.equals(ant.fieldTitle()))
					return field;
			}
		}
		
		return null;
	}
	private static List<JsonBean> getPossibleValuesForField(Field field) {
		List<JsonBean> result = new ArrayList<JsonBean>();
		Class<?> clazz = InterchangeUtils.getClassOfField(field);

		
		
		if (clazz.isAssignableFrom(AmpCategoryValue.class))
			return getPossibleCategoryValues(field);
		
//		Field potentialDescend = getDescendField(clazz);
//		if (potentialDescend != null) {
//			return getPossibleValuesForField(potentialDescend);
//		}
		
		String queryString = "select cls from " + clazz.getName() + " cls ";
		List<Object> objectList= PersistenceManager.getSession().createQuery(queryString).list();
		for (Object obj : objectList) {
			JsonBean item = null;
			try {
				item = setProperties(obj);
			} catch (Exception exc) {
				LOGGER.error(exc.getMessage());
				throw new RuntimeException(exc);
			}
			result.add(item);
		}
		return result;
	}
	private static List<JsonBean> getPossibleCategoryValues(Field field) {
		List <JsonBean> result = new ArrayList<JsonBean>();
		Interchangeable ant = field.getAnnotation(Interchangeable.class);
		String whereClause =  "WHERE acv.ampCategoryClass.name=" + "'" + ant.fieldTitle() +"'";
		
		String queryString = "SELECT acv from " + AmpCategoryValue.class.getName() + " acv ";
		/* This hack is done for the case when the field analyzed is
		 * AmpActivityFields.categories. 
		 * The alternative to this would have been to set one more attribute for the annotation
		 * (something like "listAllValues") or making a shortcut from upper (like getPossibleValuesForField(String)) 
		 * 
		 * */ 
		if (! (field.getName().equals("categories") && (field.getDeclaringClass().equals(AmpActivityFields.class)))) {
			queryString += whereClause;
		}
		List<AmpCategoryValue> acvList = (List<AmpCategoryValue>) PersistenceManager.getSession().createQuery(queryString).list();

		for (AmpCategoryValue acv : acvList) {
			if (acv.isVisible()) {
				JsonBean item = null;
				try {
					item = setProperties(acv);
					result.add(item);
				} catch (Exception exc) {
					LOGGER.error(exc.getMessage());
					throw new RuntimeException(exc);
				}
			}
		}
		
		
		return result; 
	}
	/*private static Field getDescendField(Class clazz) {
		for (Field field : clazz.getDeclaredFields()) {
			Interchangeable ant = field.getAnnotation(Interchangeable.class);
			if (ant != null && ant.descend())
				return field;
		}
		return null;
	}
	*/
	private static JsonBean setProperties(Object obj) throws NoSuchMethodException, 
	SecurityException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
		if (InterchangeUtils.isSimpleType(obj.getClass())) {
			return null;
		} else {
			JsonBean result = new JsonBean();
			for (Field field : obj.getClass().getDeclaredFields()) {
				Interchangeable ant = field.getAnnotation(Interchangeable.class);
				if (ant != null) {
					Method meth = obj.getClass().getMethod(InterchangeUtils.getGetterMethodName(field.getName()));
					Object property = meth.invoke(obj);
					if (ant.id()) {
						if (Long.class.isAssignableFrom(field.getType()) || String.class.isAssignableFrom(field.getType())) {
							result.set("id", property);
						} else {
							/*we need to go deeper*/
							result.set("id", InterchangeUtils.getId(property));
						}
					} 
					if (ant.value()) {
						if (String.class.isAssignableFrom(field.getType()) || Long.class.isAssignableFrom(field.getType())) {
							result.set("value", property);
						} else {
							/*we need to go deeper*/
							result.set("value", getValue(property));
						}
					}
					if (ant.pickIdOnly() && property != null) {
						result.set(InterchangeUtils.underscorify(ant.fieldTitle()), InterchangeUtils.getId(property));
					}
					if (!(ant.value() || ant.id())) {
						//additional property

						if (InterchangeUtils.isSimpleType(meth.getReturnType()) && property != null)
							result.set(InterchangeUtils.underscorify(ant.fieldTitle()), property);
					}
					
				}
			}
			return result;
		}
	}
	
	private static String getValue(Object obj) throws NoSuchMethodException, 
	SecurityException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
		if (InterchangeUtils.isSimpleType(obj.getClass())) {
			return null;
		} else {
			for (Field field : obj.getClass().getDeclaredFields()) {
				Interchangeable ant = field.getAnnotation(Interchangeable.class);
				if (ant != null) {
					if (ant.value()) {
						Method meth = obj.getClass().getMethod(InterchangeUtils.getGetterMethodName(field.getName()));
						if (String.class.isAssignableFrom(field.getType())) {
							return (String) meth.invoke(obj);
						} else {
							/*we need to go deeper*/
							return getValue(meth.invoke(obj));
						}
					}
				}
			}
		}
		return null;
	}	
	
	

	
}
