package org.digijava.kernel.ampapi.endpoints.activity;

import java.util.Objects;

import org.apache.commons.lang3.reflect.FieldUtils;

/**
 * @author Octavian Ciubotaru
 */
public class SimpleFieldValueReader implements FieldValueReader {

    private String fieldName;

    public SimpleFieldValueReader(String fieldName) {
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
}
