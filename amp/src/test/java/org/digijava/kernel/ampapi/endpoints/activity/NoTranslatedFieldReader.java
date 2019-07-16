package org.digijava.kernel.ampapi.endpoints.activity;

import java.lang.reflect.Field;

/**
 * @author Octavian Ciubotaru
 */
public class NoTranslatedFieldReader implements TranslatedFieldReader {

    @Override
    public Object get(Field field, Class<?> clazz, Object fieldValue, Object parentObject) {
        throw new UnsupportedOperationException("Not supported!");
    }

    @Override
    public boolean isTranslatable(Field field, Class<?> clazz) {
        return false;
    }
}
