package org.digijava.kernel.ampapi.endpoints.activity;

import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

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

import org.dgfoundation.amp.ar.viewfetcher.RsInfo;
import org.dgfoundation.amp.ar.viewfetcher.SQLUtils;
import org.digijava.kernel.ampapi.endpoints.activity.visibility.FMVisibility;
import org.digijava.kernel.ampapi.endpoints.util.JsonBean;
import org.digijava.kernel.entity.Message;
import org.digijava.kernel.persistence.PersistenceManager;
import org.digijava.kernel.persistence.WorkerException;
import org.digijava.kernel.request.TLSUtils;
import org.digijava.kernel.translator.TranslatorWorker;
import org.digijava.module.aim.annotations.interchange.Interchangeable;
import org.digijava.module.aim.annotations.interchange.InterchangeableDiscriminator;
import org.digijava.module.aim.annotations.interchange.Validators;
import org.digijava.module.aim.dbentity.AmpActivityFields;
import org.digijava.module.aim.dbentity.AmpActivityVersion;
import org.digijava.module.aim.dbentity.AmpSector;
import org.digijava.module.aim.helper.Constants;
import org.digijava.module.aim.helper.TeamMember;
import org.digijava.module.aim.util.time.StopWatch;
import org.digijava.module.translation.util.ContentTranslationUtil;
import org.hibernate.jdbc.Work;
import org.hibernate.metadata.ClassMetadata;
import org.hibernate.persister.entity.AbstractEntityPersister;
import org.hibernate.persister.entity.SingleTableEntityPersister;

import com.vividsolutions.jts.operation.IsSimpleOp;

import clover.org.apache.commons.lang.StringUtils;


/**
 * AMP Activity Endpoints for Activity Import / Export
 * 
 * @author acartaleanu
 */

@Path("activity/fields")
public class FieldsEnumerator {
	
	@Context
	private HttpServletRequest httpRequest;

	
	
	static {
		fillAllFieldsLengthInformation();
	}
	
	
	@GET
	@Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
	public List<JsonBean> getAvailableFields() {
		
		return getAllAvailableFields();
	}
	/**
	 * gets fields from the type of the field
	 * 
	 * @param field
	 * @param multilingual true if multilingual enabled
	 * @return a list of JsonBeans, each a description of @Interchangeable
	 *         fields in the definition of the field's class, or field's generic
	 *         type, if it's a collection
	 */
	private static List<JsonBean> getChildrenOfField(Field field, boolean multilingual) {
		if (!InterchangeUtils.isCollection(field))
			return getAllAvailableFields(field.getType(), multilingual);
		else
			return getAllAvailableFields(InterchangeUtils.getGenericClass(field), multilingual);
	}
	
	public static boolean hasMaxSizeValidatorEnabled(Field field) {
		boolean isEnabled = false;
		Validators validators = field.getAnnotation(Validators.class);
		if (validators != null) {
			String maxSize = validators.maxSize();
			if (!maxSize.isEmpty()) {
				isEnabled = FMVisibility.isFmPathEnabled(maxSize);
			}
		}
		return isEnabled;
	}
	
	private static Map<String, Field> getInterchangeableFields(Class clazz) {
		Map<String, Field> interFields = new HashMap<String, Field>();
		Class wClass = clazz;
		while (wClass != Object.class) {
			Field[] declaredFields = wClass.getDeclaredFields();
			for (Field field : declaredFields)
				if (field.getAnnotation(Interchangeable.class) != null)
					interFields.put(field.getName(), field);
			wClass = (Class<?>) wClass.getGenericSuperclass();
		}
		return interFields;
	}
	
	
	
	private static void fillFieldsLengthInformation(Class<?> clazz) {
		final Map <String, String> dbTypes = new HashMap<String, String>();
		final Map <String, Integer> maxLengths = new HashMap<String, Integer>();
		ClassMetadata meta = PersistenceManager.getClassMetadata(clazz);
		if (meta == null)
			return;
		AbstractEntityPersister entityPersister = (AbstractEntityPersister)meta;
		String[] propertyNames = entityPersister.getPropertyNames();
		final String tableName = entityPersister.getTableName();
		Map<String, Field> interchangeableFields = getInterchangeableFields(clazz);
		PersistenceManager.getSession().doWork(new Work() {
			public void execute(Connection conn) throws SQLException {
				String allSectorsQuery = "SELECT column_name, data_type, character_maximum_length "
						+ "FROM INFORMATION_SCHEMA.COLUMNS WHERE character_maximum_length IS NOT NULL "
						+ "AND table_name = '" + tableName + "'";
				try (RsInfo rsi = SQLUtils.rawRunQuery(conn, allSectorsQuery, null)) {
					ResultSet rs = rsi.rs;
					while (rs.next()) {
						dbTypes.put(rs.getString("column_name"), rs.getString("data_type"));
						maxLengths.put(rs.getString("column_name"), rs.getInt("character_maximum_length"));
					}
					rs.close();
				}
			}
		});
		for (int i = 0; i < propertyNames.length; i++) {
			String[] columnNames = entityPersister.getPropertyColumnNames(i);
			if (columnNames.length > 0)
			{
				String colname = columnNames[0];
				String fieldName = propertyNames[i];
//				System.out.println(propertyNames[i] + "->" + colname + "(" + dbTypes.get(colname)+  "/" + maxLengths.get(colname) +")");
				if (interchangeableFields.get(fieldName) != null) {
					Field field = interchangeableFields.get(fieldName);
					fieldTypes.put(field, dbTypes.get(colname)); //maxLengths.get(colname)
					fieldMaxLengths.put(field, maxLengths.get(colname));
				}
			}
		}
		for (Field field : interchangeableFields.values()) {
			Interchangeable ant = field.getAnnotation(Interchangeable.class);
			if (ant != null && !ant.pickIdOnly() && !InterchangeUtils.isSimpleType(field.getType()))
				fillFieldsLengthInformation(InterchangeUtils.getClassOfField(field));

		}
		

	}
	/**
	 * fills the fieldTypes and fieldMaxLengths maps
	 */
	private static void fillAllFieldsLengthInformation(){
		if (fieldTypes == null) {
			fieldTypes = new HashMap<Field, String>();
			fieldMaxLengths = new HashMap<Field, Integer>();
			fillFieldsLengthInformation(AmpActivityVersion.class);
			
		}
	}
	
	public static Map<Field, String > fieldTypes;
	public static Map<Field, Integer> fieldMaxLengths;
	
	
	
	
	/**
	 * describes a field in a JSON structure of: field_type: one of the types
	 * {string, boolean, float, list} field_name: the field name, obtained from
	 * the fieldTitle attribute from the @Interchangeable annotation
	 * field_label: translations of the field in the available languages
	 * multiple_values: true if it's a collection, false otherwise importable:
	 * whether the field is to be imported, or had been exported just for the
	 * sake of matching children: if the field is not a basic type (string,
	 * boolean, or float), its class may contain other @Interchangeable fields,
	 * which are recursively added here recursive: defined by
	 * @Interchangeable.pickIdOnly; true for the purpose of avoiding loops
	 * required: specifies whether said field needs to be transmitted
	 * empty
	 * 
	 * @param field
	 * @return
	 */
	private static JsonBean describeField(Field field, Interchangeable interchangeble, boolean multilingual) {
		if (interchangeble == null)
			return null;
		JsonBean bean = new JsonBean();
		bean.set(ActivityEPConstants.FIELD_NAME, InterchangeUtils.underscorify(interchangeble.fieldTitle()));
		if (interchangeble.id()) {
			bean.set(ActivityEPConstants.ID, interchangeble.id());
		}
		bean.set(ActivityEPConstants.FIELD_TYPE, InterchangeableClassMapper.containsSimpleClass(field.getType()) ? 
				InterchangeableClassMapper.getCustomMapping(field.getType()) : "list");
		bean.set(ActivityEPConstants.FIELD_LABEL, InterchangeUtils.mapToBean(getLabelsForField(interchangeble.fieldTitle())));
		bean.set(ActivityEPConstants.REQUIRED, InterchangeUtils.getRequiredValue(field));
		
		/* list type */
		if (!InterchangeableClassMapper.containsSimpleClass(field.getType())) {
			bean.set(ActivityEPConstants.IMPORTABLE, interchangeble.importable() ? true : false);
			if (InterchangeUtils.isCollection(field) && !hasMaxSizeValidatorEnabled(field)) {
				bean.set(ActivityEPConstants.MULTIPLE_VALUES, true);
			} else {
				bean.set(ActivityEPConstants.MULTIPLE_VALUES, false);
			}
			if (!interchangeble.pickIdOnly()) {
				List<JsonBean> children = getChildrenOfField(field, multilingual);
				if (children != null && children.size() > 0) {
					bean.set(ActivityEPConstants.CHILDREN, children);
				}
			} else {
				bean.set(ActivityEPConstants.ID_ONLY, true);
			}
			bean.set(ActivityEPConstants.UNIQUE, hasUniqueValidatorEnabled(field));
		}
		
		// only String fields should clarify if they are translatable or not
		if (java.lang.String.class.equals(field.getType())) {
			bean.set(ActivityEPConstants.TRANSLATABLE, multilingual && FieldsDescriptor.isTranslatable(field));
		}
		if (ActivityEPConstants.TYPE_VARCHAR.equals(fieldTypes.get(field)) && fieldMaxLengths.get(field) != null) {
			bean.set(ActivityEPConstants.FIELD_LENGTH, fieldMaxLengths.get(field));
		}
		return bean;
	}
	
	public static List<JsonBean> getAllAvailableFields() {
		return getAllAvailableFields(AmpActivityFields.class, ContentTranslationUtil.multilingualIsEnabled());
	}

	/**
	 * Describes each @Interchangeable field of a class
	 * 
	 * @param clazz the class to be described
	 * @return
	 */
	private static List<JsonBean> getAllAvailableFields(Class<?> clazz, boolean multilingual) {
		List<JsonBean> result = new ArrayList<JsonBean>();
		StopWatch.next("Descending into", false, clazz.getName());
		Field[] fields = clazz.getDeclaredFields();
		for (Field field : fields) {
			
			if (!FMVisibility.isVisible(field)) {
				continue;
			}
			
			if (!InterchangeUtils.isCompositeField(field)) {
				Interchangeable interchangeable = field.getAnnotation(Interchangeable.class);
				JsonBean descr = describeField(field, interchangeable, multilingual);
				if (descr != null) {
					result.add(descr);
				}
			} else {
				InterchangeableDiscriminator discriminator = field.getAnnotation(InterchangeableDiscriminator.class);
				Interchangeable[] settings = discriminator.settings();
				for (int i = 0; i < settings.length; i++) {
					JsonBean descr = describeField(field, settings[i], multilingual);
					if (descr != null) {
						result.add(descr);
					}
				}
			}
		}
		return result;
	}
	
	/**
	 * Picks available translations for a string (supposedly field name)
	 * 
	 * @param fieldName the field name to be translated
	 * @return a map from the ISO2 code -> translation in said text
	 */
	private static Map<String, String> getLabelsForField(String fieldName) {
		Map<String, String> translations = new HashMap<String, String>();
		try {
			Collection<Message> messages = TranslatorWorker.getAllTranslationOfBody(fieldName, Long.valueOf(3));
			for (Message m : messages) {
				translations.put(m.getLocale(), m.getMessage());
			}
			if (translations.isEmpty()) {
				translations.put("EN", fieldName);
			}
		} catch (WorkerException e) {
			// MEANINGFUL ERROR HERE
		}
		return translations;
	}
	
	public static boolean hasUniqueValidatorEnabled(Field field) {
		boolean isEnabled = false;
		Validators validators = field.getAnnotation(Validators.class);
		if (validators != null) {
			String uniqueValidator = validators.unique();
			if (!uniqueValidator.isEmpty()) {
				isEnabled = FMVisibility.isFmPathEnabled(uniqueValidator);
			}
		}
		return isEnabled;
	}
	
}
