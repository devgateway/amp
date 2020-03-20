package org.digijava.kernel.ampapi.endpoints.activity.field;

import java.util.Map;

import com.google.common.collect.ImmutableMap;

import org.digijava.kernel.ampapi.endpoints.dto.MultilingualContent;

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
        .put(MultilingualContent.class, FieldType.STRING)
        .build();

    private InterchangeableClassMapper() {
    }

    public static FieldType getCustomMapping(Class<?> clazz) {
        return CLASS_TO_TYPE.get(adjust(clazz));
    }

    public static boolean containsSimpleClass(Class<?> clazz) {
        return CLASS_TO_TYPE.containsKey(adjust(clazz));
    }

    private static Class<?> adjust(Class<?> clazz) {
        if (Enum.class.isAssignableFrom(clazz)) {
            clazz = Enum.class;
        }
        return clazz;
    }

}
