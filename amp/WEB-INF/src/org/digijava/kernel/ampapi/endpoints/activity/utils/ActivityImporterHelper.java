/**
 * 
 */
package org.digijava.kernel.ampapi.endpoints.activity.utils;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Collection;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import org.apache.log4j.Logger;
import org.digijava.kernel.ampapi.endpoints.activity.ActivityImporter;
import org.digijava.kernel.ampapi.endpoints.activity.InterchangeUtils;
import org.digijava.module.aim.dbentity.AmpActivityVersion;


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
	
//	protected static Set<Class<?>> toSkip = new HashSet<Class<?>>() {{
//		add(null);
//		add(Object.class);
//		add(Comparator.class);
//		add(java.util.Map.class);
//		add(org.dgfoundation.amp.ar.MetaInfo.class);
//	}};
//	
//	public static ActivityRefPath getActivityRefPathsSet() {
//		toSkip.add(java.util.Map.class);
//		toSkip.add(org.dgfoundation.amp.ar.MetaInfo.class);
//		toSkip.add(java.security.Principal.class);
//		
//		Map<Class<?>, ActivityRefPath> scanned = new HashMap<Class<?>, ActivityRefPath>();
//		buildPaths(AmpActivityVersion.class, scanned);
//		cleanupInvalid(scanned.get(AmpActivityVersion.class).getRefPaths());
//		return scanned.get(AmpActivityVersion.class);
//	}
//	
//	protected static void buildPaths(Class<?> origClazz, Map<Class<?>, ActivityRefPath> scanned) {
//		if (scanned.containsKey(origClazz)) {
//			return;
//		}
//		if (!(origClazz.getName().contains("org.digijava") || origClazz.getName().contains("org.dgfoundation"))) {
//			scanned.put(origClazz, null);
//			return;
//		}
//		
//		ActivityRefPath currentRefPath = new ActivityRefPath();
//		
//		Map<Field, String> toScan = new HashMap<Field, String>();
//		String activityFieldName = null;
//		Class<?> clazz = origClazz;
//		while (!toSkip.contains(clazz)) {
//			for (Field field : clazz.getDeclaredFields()) {
//				if (!field.getType().isPrimitive() && !field.getType().getName().startsWith("java.lang")) {
//					if (field.getType().isAssignableFrom(AmpActivityVersion.class)) {
//						activityFieldName = field.getName();
//						currentRefPath.setActivityField(activityFieldName);
//					} else { 
//						if (scanned.containsKey(field.getType())) {
//							ActivityRefPath refPath = scanned.get(field.getType());
//							if (refPath != null) {
//								currentRefPath.addActivityRefPath(field.getName(), refPath); 
//							}
//						} else {
//							toScan.put(field, field.getName());
//						}
//					}
//				}
//			}
//			clazz = clazz.getSuperclass(); 
//		}
//		
//		scanned.put(origClazz, currentRefPath);
//		
//		for (Map.Entry<Field, String> classField : toScan.entrySet()) {
//			Field field = classField.getKey();
//			Class<?> childClass = null;
//			if (InterchangeUtils.isCollection(field)) {
//				childClass =  getGenericsParameterClass(field);
//			} else {
//				childClass = field.getType();
//			}
//			buildPaths(childClass, scanned);
//			ActivityRefPath refPath = scanned.get(childClass);
//			if (refPath != null) {
//				currentRefPath.addActivityRefPath(classField.getValue(), refPath); 
//			}
//		}
//	}
//	
//	protected static void cleanupInvalid(Map<String, ActivityRefPath> scanned) {
//		for (Iterator<Map.Entry<String, ActivityRefPath>> iter = scanned.entrySet().iterator(); iter.hasNext(); ) {
//			Map.Entry<String, ActivityRefPath> entry = iter.next();
//			ActivityRefPath ref = entry.getValue();
//			cleanupInvalid(ref.getRefPaths());
//			if (ref.getRefPaths().isEmpty() && !ref.hasActivityRef()) {
//				// no actual ref paths
//				iter.remove();
//			}
//		}
//	}
	
	public static Class<?> getGenericsParameterClass(Field field) {
//		if (!ParameterizedType.class.isAssignableFrom(field.getGenericType().getClass())
//				|| java.util.Map.class.isAssignableFrom(field.getType())) {
//			return null;
//		}
		ParameterizedType parameterizedType = (ParameterizedType) field.getGenericType();
//		if (toSkip.contains(parameterizedType))
//			return null;
		// so far we process only 1 parameter only collections
		if (parameterizedType.getActualTypeArguments().length > 1)
			throw new RuntimeException("Unsupported field: " + field);
		String subElementTypeName = parameterizedType.getActualTypeArguments()[0].toString(); //parameterizedType.getActualTypeArguments()[0];
//		if (subElementTypeName.equals("K") || subElementTypeName.contains("<"))
//			return null;
		String className = subElementTypeName.substring("class ".length());
		try {
			return Class.forName(className);
		} catch (ClassNotFoundException e) {
			logger.error(e.getMessage());
			throw new RuntimeException(e);
		}
	}
	
	
	
}
