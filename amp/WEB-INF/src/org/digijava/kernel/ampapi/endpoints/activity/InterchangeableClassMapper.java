package org.digijava.kernel.ampapi.endpoints.activity;

import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.digijava.module.aim.annotations.interchange.ActivityFieldsConstants;
import org.digijava.module.aim.helper.Constants;

/**
 * Stores different mapping between internal constants, classes, types and their JSON representation, etc
 */
public class InterchangeableClassMapper {
    protected static final Map<Class<?>, String> classToCustomType = new HashMap<Class<?>, String>() {{
        put(java.lang.String.class, ActivityEPConstants.FIELD_TYPE_STRING);
        put(java.lang.Integer.class, ActivityEPConstants.FIELD_TYPE_LONG);
        put(java.util.Date.class, ActivityEPConstants.FIELD_TYPE_DATE);
        put(java.lang.Double.class, ActivityEPConstants.FIELD_TYPE_FLOAT);
        put(java.lang.Boolean.class, ActivityEPConstants.FIELD_TYPE_BOOLEAN);
        put(java.lang.Long.class, ActivityEPConstants.FIELD_TYPE_LONG);
        put(java.lang.Float.class, ActivityEPConstants.FIELD_TYPE_FLOAT);
        put(java.lang.Enum.class, ActivityEPConstants.FIELD_TYPE_STRING);
    }};
    
    public static final Set<String> SIMPLE_TYPES = new HashSet<String>() {{
        addAll(classToCustomType.values());
    }};

    private static Set<Class<?>> JSON_SUPPORTED_CLASSES = new HashSet<Class<?>>() {{
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
    }};
    
    public static String getCustomMapping(Class<?> clazz) {
        return classToCustomType.get(adjust(clazz));
    }
    
    public static boolean containsSimpleClass(Class<?> clazz) {
        return classToCustomType.containsKey(adjust(clazz));
    }
    
    public static boolean containsSupportedClass(Class<?> clazz) {
        return JSON_SUPPORTED_CLASSES.contains(adjust(clazz));
    }
    
    private static Class<?> adjust(Class<?> clazz) {
        if (Enum.class.isAssignableFrom(clazz))
            clazz = Enum.class;
        return clazz;
    }

}
