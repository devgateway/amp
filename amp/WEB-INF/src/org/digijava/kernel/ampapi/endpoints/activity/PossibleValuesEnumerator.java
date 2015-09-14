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
import java.util.Map;

import org.apache.log4j.Logger;
import org.dgfoundation.amp.ar.viewfetcher.RsInfo;
import org.dgfoundation.amp.ar.viewfetcher.SQLUtils;
import org.digijava.kernel.ampapi.endpoints.errors.ApiError;
import org.digijava.kernel.ampapi.endpoints.errors.ApiErrorMessage;
import org.digijava.kernel.ampapi.endpoints.util.JsonBean;
import org.digijava.kernel.persistence.PersistenceManager;
import org.digijava.kernel.translator.TranslatorWorker;
import org.digijava.module.aim.annotations.interchange.Interchangeable;
import org.digijava.module.aim.dbentity.AmpCategoryValueLocations;
import org.digijava.module.aim.dbentity.AmpLocation;
import org.digijava.module.aim.dbentity.AmpSector;
import org.digijava.module.aim.dbentity.AmpTheme;
import org.digijava.module.categorymanager.dbentity.AmpCategoryValue;
import org.hibernate.jdbc.Work;
import org.hibernate.proxy.HibernateProxyHelper;

import clover.org.apache.commons.lang.StringUtils;


/**
 * AMP Activity Endpoint for Possible Values -- /activity/fields/:fieldName
 * and all the methods only it might have use of
 * 
 * @author acartaleanu
 */
public class PossibleValuesEnumerator {
	
	public static final Logger LOGGER = Logger.getLogger(PossibleValuesEnumerator.class);
	
	/**
	 * recursive method that gets possible values that can be held by a field
	 * @param longFieldName underscorified field name 
	 * @param clazz the class on which the method operates
	 * @param discriminatorOption recursive option to be passed down if there was a discriminator option higher up
	 * @return JSON object containing the possible values that can be held by the field
	 */
	public static List<JsonBean> getPossibleValuesForField(String longFieldName, Class<?> clazz, String discriminatorOption) {

		String fieldName = "";
		if (longFieldName.contains("~")) {
			/*
			 * we might have a field containing a discriminator description,
			 * but what we actually need is underneath -> obtain name of the field underneath
			 * */
			fieldName = longFieldName.substring(0, longFieldName.indexOf('~') );
			Field field = InterchangeUtils.getPotentiallyDiscriminatedField(clazz, fieldName);
			if (field == null) {
				List<JsonBean> result = new ArrayList<JsonBean>();
				result.add(ApiError.toError(new ApiErrorMessage(ActivityErrors.FIELD_INVALID, fieldName)));
				return result;
			}
			
			String configString = discriminatorOption == null? null : discriminatorOption;
			if (InterchangeUtils.isCompositeField(field)) {
				configString =  InterchangeUtils.getConfigValue(fieldName, field);
			}
			
			return getPossibleValuesForField(longFieldName.substring(longFieldName.indexOf('~') + 1), 
					InterchangeUtils.getClassOfField(field), configString);
		} else {
			/*
			 * the last field might contain discriminated values
			 * if it is such a field, it's a special case for each class
			 * 
			 * */
			Field finalField =  InterchangeUtils.getPotentiallyDiscriminatedField(clazz, longFieldName);
			if (finalField == null) {
				List<JsonBean> result = new ArrayList<JsonBean>();
				result.add(ApiError.toError(new ApiErrorMessage(ActivityErrors.FIELD_INVALID, longFieldName)));
				return result;
			} else {
				String configString = discriminatorOption == null? null : discriminatorOption;
				if (InterchangeUtils.isCompositeField(finalField)) {
					configString =  InterchangeUtils.getConfigValue(longFieldName, finalField);	
				}

				try {
					Class<? extends FieldsDiscriminator> discClass = InterchangeUtils.getDiscriminatorClass(finalField);
					if (discClass != null)
						return getPossibleValuesDirectly(discClass);
				} catch(ClassNotFoundException exc) {
					List<JsonBean> result = new ArrayList<JsonBean>();
					result.add(ApiError.toError(new ApiErrorMessage(ActivityErrors.DISCRIMINATOR_CLASS_NOT_FOUND, exc.getMessage())));
					return result;
				} catch (InvocationTargetException | IllegalArgumentException | IllegalAccessException 
						| SecurityException  | NoSuchMethodException | InstantiationException e) {
					List<JsonBean> result = new ArrayList<JsonBean>();
					result.add(ApiError.toError(new ApiErrorMessage(ActivityErrors.DISCRIMINATOR_CLASS_METHOD_ERROR, e.getMessage())));
					return result;
				}				
				if (InterchangeUtils.isCompositeField(finalField) || configString != null) {
					return getPossibleValuesForComplexField(finalField, configString);
				}

				return getPossibleValuesForField(finalField);
			}
		}
	}
	
	/**
	 * method employed for the scenario that possible values are to be obtained from
	 * a FieldsDiscriminator-derived class, instead of the usual database queries
	 * @param discClass
	 * @return
	 * @throws NoSuchMethodException
	 * @throws SecurityException
	 * @throws IllegalAccessException
	 * @throws IllegalArgumentException
	 * @throws InvocationTargetException
	 * @throws InstantiationException
	 */
	private static List<JsonBean> getPossibleValuesDirectly(Class<? extends FieldsDiscriminator> discClass) 
			throws NoSuchMethodException, SecurityException, IllegalAccessException, IllegalArgumentException, 
			InvocationTargetException, InstantiationException {
		Method m = discClass.getMethod("getPossibleValues");
		FieldsDiscriminator discObj = discClass.newInstance();
		Map<String, Object> vals = (Map<String, Object>) m.invoke(discObj);
		List<JsonBean> result = new ArrayList<JsonBean>();
		for (Map.Entry<String, Object> entry : vals.entrySet()) {
			JsonBean bean = new JsonBean();
			bean.set("id", entry.getKey());
			bean.set("value", entry.getValue());
			result.add(bean);
		}
		return result;
		
	}
	
	/**
	 * Complex fields are discriminated fields -- when several underscorified paths 
	 * lead to the same Java field. This method gets possible values for such fields
	 * @param field
	 * @param configValue
	 * @return
	 */
	private static List<JsonBean> getPossibleValuesForComplexField(Field field, String configValue) {
		List<JsonBean> result = new ArrayList<JsonBean>();
		if (configValue == null) {
			return getPossibleValuesForField(field);
		}
		/*AmpActivitySector || AmpComponentFunding || AmpActivityProgram*/
		List<Object[]> items;
		if (InterchangeUtils.getClassOfField(field).equals(AmpSector.class)) {
			items = getSpecialCaseObjectList(configValue, "all_sectors_with_levels",
					 "ampSectorId", "name", "sector_config_name", "amp_sector_id", AmpSector.class);
		} else if  (InterchangeUtils.getClassOfField(field).equals(AmpTheme.class)) {
			items = getSpecialCaseObjectList(configValue, "all_programs_with_levels",
					 "ampThemeId", "name", "program_setting_name", "amp_theme_id", AmpTheme.class);
		} else if (InterchangeUtils.getClassOfField(field).equals(AmpCategoryValue.class)){
			return getPossibleCategoryValues(field, configValue);
		} else {
			//not a complex field, after all
			return getPossibleValuesForField(field);
		}
		result = setProperties(items, result, false);
		return result;
	}
	
	/**
	 * Method that wraps generic approaches for the programs, sector and org. role entities
	 * @param configType
	 * @param configTableName
	 * @param entityIdColumnName
	 * @param conditionColumnName
	 * @param idColumnName
	 * @param clazz
	 * @return
	 */
	private static List<Object[]> getSpecialCaseObjectList(final String configType, final String configTableName, 
					 String entityIdColumnName, String entityValueColumnName, final String conditionColumnName, 
					 final String idColumnName,  Class<?> clazz) {
		final List<Long> itemIds = new ArrayList<Long>();
		PersistenceManager.getSession().doWork(new Work() {
			public void execute(Connection conn) throws SQLException {
				String allSectorsQuery = "SELECT "+ idColumnName +" FROM "+ configTableName+" WHERE "+ conditionColumnName + 
										 "='" + configType +"'" +
										 " ORDER BY " + idColumnName;
				try (RsInfo rsi = SQLUtils.rawRunQuery(conn, allSectorsQuery, null)) {
					ResultSet rs = rsi.rs;
					while (rs.next()) 
						itemIds.add(rs.getLong(idColumnName));
					rs.close();
				}
			}
		});		
		
		if (itemIds.size() == 0) {
			return new ArrayList<Object[]>();
		}
		
		String ids = StringUtils.join(itemIds, ",");
		String queryString = "select cls." + entityIdColumnName + ", " +
				"cls." + entityValueColumnName + " " +
				" from " + clazz.getName() + " cls where cls."+ entityIdColumnName + " in (" + ids + ")";
		
		List<Object[]> objectList = InterchangeUtils.getSessionWithPendingChanges().createQuery(queryString).list();
		return objectList;
	}
	
	/**
	 * Generic method for obtaining possible values for most cases (without any fancy special cases)
	 * @param field
	 * @return
	 */
	
	@SuppressWarnings("unchecked")
	private static List<JsonBean> getPossibleValuesForField(Field field) {
		if (!InterchangeUtils.isFieldEnumerable(field))
			return new ArrayList<JsonBean>();
		List<JsonBean> result = new ArrayList<JsonBean>();
		Class<?> clazz = InterchangeUtils.getClassOfField(field);
		
		if (clazz.isAssignableFrom(AmpCategoryValue.class))
			return getPossibleCategoryValues(field, null);
		if (clazz.isAssignableFrom(AmpLocation.class))
			return getPossibleLocations(field);
		Field[] fields = InterchangeUtils.getClassOfField(field).getDeclaredFields();
		String idFieldName = null;
		String valueFieldName = null;
		for (Field passField : fields) {
			Interchangeable ant = passField.getAnnotation(Interchangeable.class);
			if (ant != null) {
				if (ant.id())
					idFieldName = passField.getName();
				if (ant.value())
					valueFieldName = passField.getName();
			}
		}
		if (idFieldName == null || valueFieldName == null)
			return result;
		String queryString = "SELECT cls."+idFieldName +
							", cls." + valueFieldName +
							" FROM " + clazz.getName() + " cls "+ 
							"ORDER BY " + idFieldName;
		List<Object[]> objectList = InterchangeUtils.getSessionWithPendingChanges().createQuery(queryString).list();
		result = setProperties(objectList, result, false);
//		for (Object obj : objectList) {
//			JsonBean item = null;
//			try {
//				item = setProperties(obj);
//			} catch (Exception exc) {
//				LOGGER.error(exc.getMessage());
//				throw new RuntimeException(exc);
//			}
//			if (item != null)
//				result.add(item);
//		}
		return result;
	}
	
	private static List<JsonBean> getPossibleLocations(Field field) {
		List <JsonBean> result = new ArrayList<JsonBean>();
		Interchangeable ant = field.getAnnotation(Interchangeable.class);
		String queryString = "SELECT loc.id, acvl.name, parentLoc.id, parentLoc.name" +
				" ,parentCat.id, parentCat.value" +
					" from "+ AmpLocation.class.getName() + " loc " +
					" LEFT JOIN loc.location as acvl" +
					" LEFT JOIN acvl.parentLocation as parentLoc" +
					" LEFT JOIN acvl.parentCategoryValue as parentCat" +
					" ORDER BY loc.id";
		@SuppressWarnings("unchecked")
		List<Object[]> objColList = (List<Object[]>) InterchangeUtils
				.getSessionWithPendingChanges().createQuery(queryString).list();

		for (Object[] item : objColList){
			Long id = ((Number)(item[0])).longValue();
			String value = ((String)(item[1]));
			Long parentLocationId = item[2] == null? null : ((Number)(item[2])).longValue();
			String parentLocationName = item[3] == null? null : ((String)(item[3]));
			Long categoryValueId = item[4] == null? null : ((Number)(item[4])).longValue();
			String categoryValueName = item[5] == null? null : ((String)(item[5]));
			
			JsonBean bean = new JsonBean();
			bean.set("id", id);
			bean.set("value", value);
			JsonBean extraInfo = new JsonBean();
			extraInfo.set("parent_location_id", parentLocationId);
			extraInfo.set("parent_location_name", parentLocationName);
			extraInfo.set("implementation_level_id", categoryValueId);
			extraInfo.set("implementation_location_name", categoryValueName);
			bean.set("extra_info", extraInfo);
			result.add(bean);
		}
		return result;		
	}

	/**
	 * Gets possible values for the AmpCategoryValue class
	 * @param field
	 * @param discriminatorOption 
	 * @return
	 */
	@SuppressWarnings("unchecked")
	private static List<JsonBean> getPossibleCategoryValues(Field field, String discriminatorOption) {
		List <JsonBean> result = new ArrayList<JsonBean>();
		Interchangeable ant = field.getAnnotation(Interchangeable.class);
		if (StringUtils.isBlank(discriminatorOption)) {
			discriminatorOption = ant.discriminatorOption();
		}
		if (StringUtils.isNotBlank(discriminatorOption)) {
			String queryString = "SELECT acv.id, acv.value, acv.deleted from " + AmpCategoryValue.class.getName() + " acv "
					+ "WHERE acv.ampCategoryClass.keyName ='" + discriminatorOption + "' ORDER BY acv.id";
			@SuppressWarnings("unchecked")
			List<Object[]> objColList = (List<Object[]>) InterchangeUtils
					.getSessionWithPendingChanges().createQuery(queryString).list();

			result = setProperties(objColList, result, true);
			return result;
		} else {
			LOGGER.error("discriminatorOption is not configured for CategoryValue [" + field.getName() + "]");
		}
		
		return result; 
	}
	
	private static List<JsonBean> setProperties(List<Object[]> objColList, List<JsonBean> result, boolean checkDeleted) {
		
		for (Object[] item : objColList){
			Long id = ((Number)(item[0])).longValue();
			String value = ((String)(item[1]));
			boolean itemGood = !checkDeleted || Boolean.FALSE.equals((Boolean)(item[2])); 
//			Boolean deleted = ((Boolean)(item[2]));
			if (itemGood) {
				JsonBean bean = new JsonBean();
				bean.set("id", id);
				bean.set("value", value);
				result.add(bean);
			}
			
		}
		return result;
	}


}
