/**
 * 
 */
package org.digijava.kernel.ampapi.endpoints.activity.utils;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.util.Map;
import java.util.TreeMap;

import org.apache.log4j.Logger;
import org.digijava.kernel.ampapi.endpoints.activity.ActivityEPConstants;
import org.digijava.kernel.ampapi.endpoints.activity.ActivityImporter;
import org.digijava.kernel.ampapi.endpoints.errors.ApiErrorMessage;
import org.digijava.kernel.ampapi.endpoints.util.JsonBean;


/**
 * Helper methods for Activity Import
 * 
 * @author Nadejda Mandrescu
 */
public class ActivityImporterHelper {
	
	private static final Logger logger = Logger.getLogger(ActivityImporterHelper.class);
	/** 
	 * Stores all field paths within AmpActivityVersion tree that back reference the Activity.
	 */
	private ActivityImporter activityImporter;
	
	/**
	 * 
	 * @param activityImporter
	 */
	public ActivityImporterHelper(ActivityImporter activityImporter) {
		this.activityImporter = activityImporter;
	}
	
	/**
	 * 
	 * @param field
	 * @return
	 */
	public static Class<?> getGenericsParameterClass(Field field) {
		ParameterizedType parameterizedType = (ParameterizedType) field.getGenericType();
		if (parameterizedType.getActualTypeArguments().length > 1)
			throw new RuntimeException("Unsupported field: " + field);
		String subElementTypeName = parameterizedType.getActualTypeArguments()[0].toString(); //parameterizedType.getActualTypeArguments()[0];
		String className = subElementTypeName.substring("class ".length());
		try {
			return Class.forName(className);
		} catch (ClassNotFoundException e) {
			logger.error(e.getMessage());
			throw new RuntimeException(e);
		}
	}
	
	public static void addGeneralError(JsonBean root, ApiErrorMessage error) { 
		Map<Integer, ApiErrorMessage> generalErrors = (TreeMap<Integer, ApiErrorMessage>) root.get(ActivityEPConstants.INVALID);
		if (generalErrors == null) {
			generalErrors = new TreeMap<Integer, ApiErrorMessage>();
			root.set(ActivityEPConstants.INVALID, generalErrors);
		}
		ApiErrorMessage existing = generalErrors.get(error.id);
		if (existing != null) {
			error = new ApiErrorMessage(existing, error.value);
		}
		generalErrors.put(error.id, error);
	}
	
//	public static String getIdFieldName(JsonBean fieldDescription) {
//		if (fieldDescription != null) {
//			Object children = fieldDescription.get(ActivityEPConstants.CHILDREN);
//			if (children != null && children instanceof List)
//				for (JsonBean child : (List<JsonBean>) children) {
//					if (Boolean.TRUE.equals(child.get(ActivityEPConstants.ID)))
//						return child.getString(ActivityEPConstants.FIELD_NAME);
//				}
//		}
//		return null;
//	}
	
}
