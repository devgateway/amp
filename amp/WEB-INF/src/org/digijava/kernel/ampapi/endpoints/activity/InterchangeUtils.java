package org.digijava.kernel.ampapi.endpoints.activity;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.digijava.kernel.ampapi.endpoints.activity.visibility.FMVisibility;
import org.digijava.kernel.ampapi.endpoints.errors.ApiError;
import org.digijava.kernel.ampapi.endpoints.errors.ApiErrorMessage;
import org.digijava.kernel.ampapi.endpoints.util.JsonBean;
import org.digijava.kernel.exception.DgException;
import org.digijava.module.aim.annotations.interchange.Interchangeable;
import org.digijava.module.aim.annotations.interchange.InterchangeableDiscriminator;
import org.digijava.module.aim.annotations.interchange.Validators;
import org.digijava.module.aim.dbentity.AmpActivityFields;
import org.digijava.module.aim.dbentity.AmpActivityVersion;
import org.digijava.module.aim.util.ActivityUtil;

/**
 * Activity Import/Export Utility methods 
 * 
 */
public class InterchangeUtils {

	public static final Logger LOGGER = Logger.getLogger(InterchangeUtils.class);
	private static Map<String, String> underscoreToTitleMap = new HashMap<String, String>();
	private static Map<String, String> titleToUnderscoreMap = new HashMap<String, String>();
	
	/**map from discriminator title (i.e. "Primary Sectors") to actual field name (i.e. "Sectors")
	 */
	private static Map<String, String> discriminatorMap = new HashMap<String, String> ();
	static {
		addUnderscoredTitlesToMap(AmpActivityFields.class);
	}
	
	private static final String NOT_REQUIRED = "_NONE_";
	private static final String ALWAYS_REQUIRED = "_ALWAYS_";
	
	

	
	public static String getDiscriminatedFieldTitle(String fieldName) {
		return discriminatorMap.get(deunderscorify(fieldName));
	}
	
	private static void addUnderscoredTitlesToMap(Class clazz) {
		for (Field field : clazz.getDeclaredFields()) {
			Interchangeable ant = field.getAnnotation(Interchangeable.class);
			if (ant != null) {
				if (!isCompositeField(field))
				{
					underscoreToTitleMap.put(underscorify(ant.fieldTitle()), ant.fieldTitle());
					titleToUnderscoreMap.put(ant.fieldTitle(), underscorify(ant.fieldTitle()));
				} else {
					InterchangeableDiscriminator antd = field.getAnnotation(InterchangeableDiscriminator.class);
					Interchangeable[] settings = antd.settings();
					for (Interchangeable ants : settings) {
						underscoreToTitleMap.put(underscorify(ants.fieldTitle()), ants.fieldTitle());
						titleToUnderscoreMap.put(ants.fieldTitle(), underscorify(ants.fieldTitle()));
						discriminatorMap.put(ants.fieldTitle(), ant.fieldTitle());
					}
				}
				if (!isSimpleType(getClassOfField(field)) && !ant.pickIdOnly())
					addUnderscoredTitlesToMap(getClassOfField(field));
			}
		}
	}
	

	/**
	 * transforms a Map<String,String> to a JsonBean with equal structure
	 * 
	 * @param map the map to be transformed
	 * @return a JsonBean of the structure {"\<code1\>":"\<translation1\>", "\<code2\>":"\<translation2\>", ...}
	 */
	public static JsonBean mapToBean(Map<String, String> map) {
		if (map.isEmpty())
			return null;
		JsonBean bean = new JsonBean();
		for (Map.Entry<String, String> entry : map.entrySet()) {
			bean.set(entry.getKey(), entry.getValue());
		}
		return bean;
	}
	
	/**
	 * checks whether a Field is assignable from a Collection
	 * 
	 * @param field a Field
	 * @return true/false
	 */
	public static boolean isCollection(Field field) {
		return Collection.class.isAssignableFrom(field.getType());
	}

	/**
	 * returns the generic class defined within a Collection, e.g.
	 * Collection<Class_returned>
	 * 
	 * @param field
	 * @return the generic class
	 */
	public static Class<?> getGenericClass(Field field) {
		if (!isCollection(field))
			throw new RuntimeException("Not a collection: " + field.toString());
		ParameterizedType collectionType = null;
		collectionType = (ParameterizedType) field.getGenericType();
		Type[] genericTypes = collectionType.getActualTypeArguments();
		if (genericTypes.length > 1) {
			// dealing with a map or anything else having > 1 parameterized
			// types
			// throw an exception, this is a very unexpected case
			throw new RuntimeException("Only collections with one generic type expected!");
		}
		if (genericTypes.length == 0) {
			// return null;
			// dealing with a raw type
			// throw an exception, it won't be complete with no parameterization
			throw new RuntimeException("Raw types are not allowed!");
		}
		return ((Class<?>) genericTypes[0]);
	}




	/**
	 * converts the uppercase letters of a string to underscore + lowercase (except for first one)
	 * 
	 * @param input String to be converted
	 * @return converted string
	 */
	public static String underscorify(String input) {
		if (titleToUnderscoreMap.containsKey(input))
			return titleToUnderscoreMap.get(input);
		StringBuilder bld = new StringBuilder();
		for (int i = 0; i < input.length(); i++) {
			if (input.charAt(i) == ' ')
				bld.append('_');
			else
				bld.append(Character.toLowerCase(input.charAt(i)));
		}
		return bld.toString();
	}

	public static String deunderscorify(String input) {
		if (underscoreToTitleMap.containsKey(input)) 
			return underscoreToTitleMap.get(input);
		StringBuilder bld = new StringBuilder();
		boolean upcaseMarker = false;
		for (int i = 0; i < input.length(); i++) {
			if (upcaseMarker) {
				bld.append(Character.toUpperCase(input.charAt(i)));
				upcaseMarker = false;
			} else
				if (input.charAt(i) == '_') {
					upcaseMarker = true;
				} else {
					bld.append(input.charAt(i));
				}
			
		}
		return bld.toString();
	}
	


	public static boolean isCompositeField(Field field) {
		return field.getAnnotation(InterchangeableDiscriminator.class) != null;
	}
	
	/**
	 * Activity Export as JSON
	 * 
	 * @param projectId is amp_activity_id
	 * @return
	 */
	public static JsonBean getActivity(Long projectId) {
		return getActivity(projectId, null);
	}
	
	/**
	 * Activity Export as JSON 
	 * 
	 * @param projectId is amp_activity_id
	 * @param filter is the JSON with a list of fields
	 * @return
	 */
	public static JsonBean getActivity(Long projectId, JsonBean filter) {
		try {
			AmpActivityVersion activity = ActivityUtil.loadActivity(projectId);
			
			return getActivity(activity, filter);
		} catch (DgException e) {
			LOGGER.error("Coudn't load activity with id: " + projectId + ". " + e.getMessage());
			throw new RuntimeException(e);
		}
	}
	
	/**
	 * Activity Export as JSON 
	 * 
	 * @param AmpActivityVersion is the activity
	 * @param filter is the JSON with a list of fields
	 * @return
	 */
	public static JsonBean getActivity(AmpActivityVersion activity, JsonBean filter) {
		try {
			ActivityExporter exporter = new ActivityExporter();
			
			return exporter.getActivity(activity, filter);
		} catch (Exception e) {
			LOGGER.error("Error in loading activity. " + e.getMessage());
			throw new RuntimeException(e);
		}
	}
	
	public static String getGetterMethodName(String fieldName) {

		if (fieldName.length() == 1)
			return "get" + Character.toUpperCase(fieldName.charAt(0));
		return "get" + Character.toUpperCase(fieldName.charAt(0)) + 
				((fieldName.length() > 1) ? fieldName.substring(1) : "");
	}	
	
	public static Long  getId(Object obj) throws NoSuchMethodException,	SecurityException, 
		IllegalAccessException, IllegalArgumentException, InvocationTargetException {
		
		if (InterchangeUtils.isSimpleType(obj.getClass())) {
			return null;
		} else {
			for (Field field : obj.getClass().getDeclaredFields()) {
				Interchangeable ant = field.getAnnotation(Interchangeable.class);
				if (ant != null) {
					if (ant.id()) {
						Method meth = obj.getClass().getMethod(getGetterMethodName(field.getName()));
						if (Long.class.isAssignableFrom(field.getType())) {
							return (Long) meth.invoke(obj);
						} else {
							/*we need to go deeper*/
							return getId(meth.invoke(obj));
						}
					}
				}
			}
		}
		
		return null;
	}	

	/**
	 * Gets the field required value. 
	 * 
	 * @param Field the field to get its required value
	 * @return String with Y|ND|N, where Y (yes) = always required, ND=for draft status=false, 
	 * N (no) = not required. .
	 */
	public static String getRequiredValue(Field field) {
		String requiredValue = ActivityEPConstants.FIELD_NOT_REQUIRED;
		String minSize = "";
		Validators validators = field.getAnnotation(Validators.class);
		Interchangeable interchangeable = field.getAnnotation(Interchangeable.class);
		if (validators != null) {
			minSize = validators.minSize();
		}
		String required = interchangeable.required();
		if (required.equals(ALWAYS_REQUIRED)) {
			requiredValue = ActivityEPConstants.FIELD_ALWAYS_REQUIRED;
		}
		else if ((!required.equals(NOT_REQUIRED) && FMVisibility.isFmPathEnabled(required))
				|| (!minSize.isEmpty() && FMVisibility.isFmPathEnabled(minSize))) {
			requiredValue = ActivityEPConstants.NON_DRAFT_REQUIRED;
		}
		return requiredValue;
	}

	
	

	public static boolean isSimpleType(Class<?> clazz) {
		return InterchangeableClassMapper.containsSimpleClass(clazz);
	}

	@SuppressWarnings("rawtypes")
	public static Class getClassOfField(Field field) {
		if (!isCollection(field))
			return field.getType();
		else
			return getGenericClass(field);
		
	}
		
	/**
	 * Imports or Updates an activity
	 * @param json new activity configuration
	 * @param update flags whether this is an import or an update request 
	 * @return latest project overview or an error if invalid configuration is received 
	 */
	public static JsonBean importActivity(JsonBean newJson, boolean update) {
		ActivityImporter importer = new ActivityImporter();
		List<ApiErrorMessage> errors = importer.importOrUpdate(newJson, update);
		
		return getImportResult(importer.getNewActivity(), importer.getOldJson(), errors);
		
	}
	
	protected static JsonBean getImportResult(AmpActivityVersion newActivity, JsonBean oldJson, 
			List<ApiErrorMessage> errors) {
		JsonBean result = null;
		if (errors.size() == 0 && newActivity == null) {
			result = ApiError.toError(ApiError.UNKOWN_ERROR); 
		} else if (errors.size() > 0) {
			result = ApiError.toError(errors);
			result.set(ActivityEPConstants.ACTIVITY, oldJson);
		} else {
			List<JsonBean> activities = ProjectList.getActivitiesByIds(Arrays.asList(newActivity.getAmpActivityId()), true, true, true);
			if (activities == null || activities.size() == 0) {
				result = ApiError.toError(ApiError.UNKOWN_ERROR);
			} else {
				result = activities.get(0);
			}
		}
		return result;
	}
	

}