package org.digijava.kernel.ampapi.endpoints.activity;

import org.digijava.module.editor.exception.EditorException;

import java.lang.reflect.Field;

/**
 * @author Octavian Ciubotaru
 */
public class DefaultTranslatedFieldReader implements TranslatedFieldReader {

    @Override
    public Object get(Field field, Class<?> clazz, Object fieldValue, Object parentObject) {
        try {
            return ActivityTranslationUtils.getTranslationValues(field, clazz, fieldValue, parentObject);
        } catch (NoSuchFieldException | IllegalAccessException | EditorException e) {
            throw new RuntimeException("Failed to read translated field.", e);
        }
    }

    public boolean isTranslatable(Field field, Class<?> clazz) {
        return ActivityTranslationUtils.isTranslatable(field, clazz);
    }
}
