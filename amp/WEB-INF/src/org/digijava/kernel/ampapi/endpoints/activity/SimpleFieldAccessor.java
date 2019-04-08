package org.digijava.kernel.ampapi.endpoints.activity;

import java.util.Objects;

import org.apache.commons.lang3.reflect.FieldUtils;

/**
 * TODO rename to Reflective
 *
 * @author Octavian Ciubotaru
 */
public class SimpleFieldAccessor implements FieldAccessor {

    private String fieldName;

    public SimpleFieldAccessor(String fieldName) {
        this.fieldName = fieldName;
    }

    @Override
    public Object get(Object targetObject) {
        Objects.requireNonNull(targetObject);
        try {
            return FieldUtils.readField(targetObject, fieldName, true);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(
                    String.format("Failed to read %s field value from %s.", fieldName, targetObject));
        }
    }

    @Override
    public void set(Object targetObject, Object value) {
        Objects.requireNonNull(targetObject);
        try {
            FieldUtils.writeField(targetObject, fieldName, value, true);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(
                    String.format("Failed to write %s field value to %s.", fieldName, targetObject));
        }
    }
}
