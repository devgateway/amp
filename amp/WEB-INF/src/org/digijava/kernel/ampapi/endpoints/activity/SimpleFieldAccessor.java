package org.digijava.kernel.ampapi.endpoints.activity;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
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
            Object objectValue = FieldUtils.readField(targetObject, fieldName, true);
            
            if (objectValue instanceof Collection) {
                List<Object> items = new ArrayList<>();
                items.addAll((Collection) objectValue);
                return items;
            }
            
            return objectValue;
        } catch (IllegalAccessException e) {
            throw new RuntimeException(
                    String.format("Failed to read %s field value from %s.", fieldName, targetObject));
        }
    }

    @Override
    public void set(Object targetObject, Object value) {
        Objects.requireNonNull(targetObject);
        try {
            Object currentValue = FieldUtils.readField(targetObject, fieldName, true);
            if (currentValue instanceof Collection) {
                ((Collection) currentValue).clear();
                ((Collection) currentValue).addAll((Collection) value);
                FieldUtils.writeField(targetObject, fieldName, currentValue, true);
            } else {
                FieldUtils.writeField(targetObject, fieldName, value, true);
            }
            
        } catch (IllegalAccessException e) {
            throw new RuntimeException(
                    String.format("Failed to write %s field value to %s.", fieldName, targetObject));
        }
    }

}
