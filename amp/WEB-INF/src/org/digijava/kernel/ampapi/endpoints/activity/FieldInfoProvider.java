package org.digijava.kernel.ampapi.endpoints.activity;

import java.lang.reflect.Field;

/**
 * This class provides some basic info about fields from db entities like: type, max lenght, is translatable?
 *
 * @author Octavian Ciubotaru
 */
public interface FieldInfoProvider {

    /**
     * Returns database type for field.
     * @param field field from db entity
     * @return database type
     */
    String getType(Field field);

    /**
     * Returns maximum allowed length for field.
     * @param field field from db entity
     * @return maximum length or null if maximum length is not defined
     */
    Integer getMaxLength(Field field);

    /**
     * Returns true if field value can be translated.
     * @param field field from db entity
     * @return true if field value can be translated
     */
    boolean isTranslatable(Field field);
}
