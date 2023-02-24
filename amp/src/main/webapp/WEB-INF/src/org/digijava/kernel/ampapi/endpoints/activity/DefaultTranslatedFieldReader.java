package org.digijava.kernel.ampapi.endpoints.activity;

import java.lang.reflect.Field;

import org.digijava.module.editor.exception.EditorException;

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
