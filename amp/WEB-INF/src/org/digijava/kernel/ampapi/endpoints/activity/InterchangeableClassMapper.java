package org.digijava.kernel.ampapi.endpoints.activity;

import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class InterchangeableClassMapper {
	protected static final Map<Class<?>, String> classToCustomType = new HashMap<Class<?>, String>() {
		{
			put(java.lang.String.class, "string");
			put(java.lang.Integer.class, "string");
			put(java.util.Date.class, "date");
			put(java.lang.Double.class, "float");
			put(java.lang.Boolean.class, "boolean");
			put(java.lang.Long.class, "string");
			// put(java.lang.Long.class, "int");
			put(java.lang.Float.class, "float");
		}
	};
	
	

	private static Set<Class<?>> JSON_SUPPORTED_CLASSES = new HashSet<Class<?>>() {
		{
			add(Boolean.class);
			add(Character.class);
			add(Byte.class);
			add(Short.class);
			add(Integer.class);
			add(Long.class);
			add(Float.class);
			add(Double.class);
			add(String.class);
			add(Date.class);
		}
	};
	
	public static String getCustomMapping(Class<?> clazz) {
		return classToCustomType.get(clazz);
	}
	
	public static boolean containsSimpleClass(Class<?> clazz) {
		return classToCustomType.containsKey(clazz);
	}
	
	public static boolean containsSupportedClass(Class<?> clazz) {
		return JSON_SUPPORTED_CLASSES.contains(clazz);
	}
	
//	private static final Map<Class<?>, String> classToCustomType = new HashMap<Class<?>, String>(){
//		put(java.lang.String.class, "string");
//		
//	};
}
