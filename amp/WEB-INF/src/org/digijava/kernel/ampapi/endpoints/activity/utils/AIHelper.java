/**
 * 
 */
package org.digijava.kernel.ampapi.endpoints.activity.utils;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;

import org.apache.log4j.Logger;
import org.digijava.kernel.ampapi.endpoints.activity.ActivityEPConstants;
import org.digijava.kernel.ampapi.endpoints.activity.ActivityImporter;
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
	 * Retrieves the class specified as type for Generics
	 * @param field
	 * @return
	 */
	public static Class<?> getGenericsParameterClass(Field field) {
		ParameterizedType parameterizedType = (ParameterizedType) field.getGenericType();
		if (parameterizedType.getActualTypeArguments().length > 1)
			throw new RuntimeException("Unsupported field: " + field);
		String subElementTypeName = parameterizedType.getActualTypeArguments()[0].toString();
		String className = subElementTypeName.substring("class ".length());
		try {
			return Class.forName(className);
		} catch (ClassNotFoundException e) {
			logger.error(e.getMessage());
			throw new RuntimeException(e);
		}
	}

	/**
	 * Retrieves amp_activity_id (internal_id) from main JSON request
	 * @param root
	 * @return Long representation or null if invalid or missing
	 */
	public static Long getActivityIdOrNull(JsonBean root) {
		return longOrNull(root.get(ActivityEPConstants.AMP_ACTIVITY_ID_FIELD_NAME));
	}

	/**
	 * Retrieves team id from JSON activity.
	 * @param root
	 * @return Long representation or null if invalid or missing
	 */
	public static Long getTeamIdOrNull(JsonBean root) {
		return longOrNull(root.get(ActivityEPConstants.TEAM_FIELD_NAME));
	}

	private static Long longOrNull(Object obj) {
		try {
			return Long.valueOf(String.valueOf(obj));
		} catch (NumberFormatException e) {
			return null;
		}
	}
}
