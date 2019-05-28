package org.digijava.kernel.ampapi.endpoints.activity.field;

/**
 * @author Octavian Ciubotaru
 */
public final class APIFieldUtil {

    private APIFieldUtil() {
    }

    /**
     * Read the field value if field is present. Otherwise return default value.
     *
     * @param field field to read
     * @param object target object
     * @param defaultValue default value if field is null
     * @param <T> value type
     * @return return field value or the specified default value
     */
    public static <T> T readFieldValueOrDefault(APIField field, Object object, T defaultValue) {
        if (field == null) {
            return defaultValue;
        } else {
            return field.getFieldAccessor().get(object);
        }
    }
}
