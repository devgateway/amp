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

import clover.org.apache.commons.lang.math.NumberUtils;


/**
 * Helper methods for Activity Import
 * 
 * @author Nadejda Mandrescu
 */
public class AIHelper {
	
	private static final Logger logger = Logger.getLogger(AIHelper.class);
	/** 
	 * Stores all field paths within AmpActivityVersion tree that back reference the Activity.
	 */
	private ActivityImporter activityImporter;
	
	/**
	 * 
	 * @param activityImporter
	 */
	public AIHelper(ActivityImporter activityImporter) {
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
	
	/**
	 * Retrieves amp_activity_id (internal_id) from main JSON request
	 * @param root
	 * @return Long representation or null if invalid or missing
	 */
	public static Long getActivityIdOrNull(JsonBean root) {
		String idStr = String.valueOf(root.get(ActivityEPConstants.AMP_ACTIVITY_ID_FIELD_NAME));
		if (NumberUtils.isDigits(idStr)) 
			return Long.valueOf(idStr);
		return null;
	}
	
	
}
