package org.digijava.kernel.ampapi.endpoints.activity;

import org.apache.commons.lang3.reflect.FieldUtils;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Objects;

/**
 * Treats collections as values. In order to edit a collection one has to retrieve the collection, edit it, then write
 * the collection back. Collections as values are necessary because in {@link DiscriminatedFieldAccessor} we would need
 * to implement a collection wrapper that can iterate/manipulate the wrapped collection on the fly.
 *
 * @author Octavian Ciubotaru
 */
public class SimpleFieldAccessor implements FieldAccessor {

    private Field targetField;
    private boolean isCollection;

    public SimpleFieldAccessor(Field field) {
        this.targetField = field;
        isCollection = Collection.class.isAssignableFrom(targetField.getType());
    }

    @Override
    public Object get(Object targetObject) {
        Objects.requireNonNull(targetObject);
        try {
            Object objectValue = FieldUtils.readField(targetField, targetObject, true);
            if (isCollection) {
                return new ArrayList<>((Collection) objectValue);
            } else {
                return objectValue;
           }
        } catch (IllegalAccessException e) {
            throw new RuntimeException(
                    String.format("Failed to read %s from %s.", targetField, targetObject));
        }
    }

    @Override
    public void set(Object targetObject, Object value) {
        Objects.requireNonNull(targetObject);
        try {
            if (isCollection) {
                Object currentValue = FieldUtils.readField(targetField, targetObject, true);
                ((Collection) currentValue).clear();
                ((Collection) currentValue).addAll((Collection) value);
            } else {
                FieldUtils.writeField(targetField, targetObject, value, true);
            }
        } catch (IllegalAccessException e) {
            throw new RuntimeException(
                    String.format("Failed to write %s to %s.", targetField, targetObject));
        }
    }

    @Override
    public String toString() {
        return "Reflective accessor for " + targetField;
    }
}
