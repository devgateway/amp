/**
 * 
 */
package org.digijava.kernel.ampapi.endpoints.activity.utils;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;

import org.apache.log4j.Logger;
import org.digijava.kernel.ampapi.endpoints.activity.ActivityImporter;


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
	
	public ActivityImporterHelper(ActivityImporter activityImporter) {
		this.activityImporter = activityImporter;
	}
	
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
	
}
