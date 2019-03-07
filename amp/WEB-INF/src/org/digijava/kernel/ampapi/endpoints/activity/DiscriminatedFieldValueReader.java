package org.digijava.kernel.ampapi.endpoints.activity;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.apache.commons.beanutils.PropertyUtils;

/**
 * @author Octavian Ciubotaru
 */
public class DiscriminatedFieldValueReader extends SimpleFieldValueReader {

    private String discriminatorField;
    private String discriminatorValue;

    public DiscriminatedFieldValueReader(String fieldName, String discriminatorField, String discriminatorValue) {
        super(fieldName);
        this.discriminatorField = discriminatorField;
        this.discriminatorValue = discriminatorValue;
    }

    @Override
    public Object get(Object targetObject) {
        Object obj = super.get(targetObject);
        if (!(obj instanceof Collection)) {
            throw new IllegalStateException(String.format(
                    "Value read from %s field of %s is either null or does not implement java.util.Collection",
                    getFieldName(), targetObject.getClass().getSimpleName()));
        }
        Collection collection = (Collection) obj;
        List<Object> filteredItems = new ArrayList<>();
        for (Object item : collection) {
            if (getDiscriminationValue(item).equals(discriminatorValue)) {
                filteredItems.add(item);
            }
        }
        return filteredItems;
    }

    private String getDiscriminationValue(Object obj) {

        Object discriminatorObj;
        try {
            discriminatorObj = PropertyUtils.getProperty(obj, discriminatorField);
        } catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
            throw new RuntimeException(String.format(
                    "Failed to read discriminator value. Object %s, discriminator field %s.",
                    obj, discriminatorField));
        }

        if (discriminatorObj == null) {
            throw new RuntimeException(String.format(
                    "Discriminator value must be non-null. Object %s, discriminator field %s.",
                    obj, discriminatorField));
        }

        return discriminatorObj.toString();
    }
}
