package org.digijava.kernel.ampapi.endpoints.activity.field;

import com.google.common.collect.ImmutableMap;
import java.util.Date;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * Stores different mapping between internal constants, classes, types and their JSON representation, etc
 */
public final class InterchangeableClassMapper {
    protected static final Map<Class<?>, FieldType> CLASS_TO_TYPE = new ImmutableMap.Builder<Class<?>, FieldType>()
        .put(java.lang.String.class, FieldType.STRING)
        .put(java.lang.Integer.class, FieldType.LONG)
        .put(java.util.Date.class, FieldType.DATE)
        .put(java.lang.Double.class, FieldType.FLOAT)
        .put(java.lang.Boolean.class, FieldType.BOOLEAN)
        .put(java.lang.Long.class, FieldType.LONG)
        .put(java.lang.Float.class, FieldType.FLOAT)
        .put(java.lang.Enum.class, FieldType.STRING)
        .build();

    private static final Set<Class<?>> JSON_SUPPORTED_CLASSES = new HashSet<Class<?>>() {{
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
    
    private InterchangeableClassMapper() {
    }
    
    public static FieldType getCustomMapping(Class<?> clazz) {
        return CLASS_TO_TYPE.get(adjust(clazz));
    }
    
    public static boolean containsSimpleClass(Class<?> clazz) {
        return CLASS_TO_TYPE.containsKey(adjust(clazz));
    }
    
    public static boolean containsSupportedClass(Class<?> clazz) {
        return JSON_SUPPORTED_CLASSES.contains(adjust(clazz));
    }
    
    private static Class<?> adjust(Class<?> clazz) {
        if (Enum.class.isAssignableFrom(clazz)) {
            clazz = Enum.class;
        }
        return clazz;
    }

}
