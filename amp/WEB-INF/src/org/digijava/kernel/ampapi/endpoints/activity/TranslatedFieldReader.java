package org.digijava.kernel.ampapi.endpoints.activity;

import java.lang.reflect.Field;

/**
 * Provides a means to read a field that can be translated in many languages.
 *
 * @author Octavian Ciubotaru
 */
public interface TranslatedFieldReader {

    Object get(Field field, Class<?> clazz, Object fieldValue, Object parentObject);

    boolean isTranslatable(Field field, Class<?> clazz);
}
