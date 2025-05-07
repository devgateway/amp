package org.digijava.kernel.ampapi.endpoints.common.valueproviders;

import org.apache.commons.beanutils.PropertyUtils;
import org.digijava.kernel.ampapi.endpoints.activity.TranslationSettings;
import org.digijava.kernel.ampapi.endpoints.common.TranslationUtil;
import org.digijava.module.aim.annotations.interchange.InterchangeableValueProvider;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;

/**
 * @author Octavian Ciubotaru
 */
public class GenericInterchangeableValueProvider<T> implements InterchangeableValueProvider<T> {

    private String fieldName;
    private boolean translatable;

    public GenericInterchangeableValueProvider(Class<T> targetClass, String fieldName) {
        this.fieldName = fieldName;
        try {
            Field field = targetClass.getDeclaredField(fieldName);
            translatable = TranslationUtil.getTranslatableType(field) == TranslationSettings.TranslationType.STRING;
        } catch (NoSuchFieldException | NullPointerException | SecurityException e) {
            String msg = String.format("Failed to create value provider for class=%s field=%s", targetClass, fieldName);
            throw new RuntimeException(msg, e);
        }
    }

    @Override
    public String getValue(T object) {
        try {
            return (String) PropertyUtils.getProperty(object, fieldName);
        } catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
            throw new RuntimeException("Failed to extract possible value object from " + object, e);
        }
    }

    @Override
    public boolean isTranslatable() {
        return translatable;
    }
}
