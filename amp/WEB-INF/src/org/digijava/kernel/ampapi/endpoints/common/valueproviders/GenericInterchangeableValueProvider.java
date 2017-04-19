package org.digijava.kernel.ampapi.endpoints.common.valueproviders;

import java.lang.reflect.InvocationTargetException;

import org.apache.commons.beanutils.PropertyUtils;
import org.digijava.module.aim.annotations.interchange.InterchangeableValueProvider;

/**
 * @author Octavian Ciubotaru
 */
public class GenericInterchangeableValueProvider<T> implements InterchangeableValueProvider<T> {

    private String valueFieldName;

    public GenericInterchangeableValueProvider(String valueFieldName) {
        this.valueFieldName = valueFieldName;
    }

    @Override
    public String getValue(T object) {
        try {
            return (String) PropertyUtils.getProperty(object, valueFieldName);
        } catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
            throw new RuntimeException("Failed to extract possible value object from " + object, e);
        }
    }
}
