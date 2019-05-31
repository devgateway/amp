package org.digijava.kernel.ampapi.endpoints.activity;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.TreeSet;

import org.apache.commons.beanutils.PropertyUtils;

/**
 * On read will return a list of objects that satisfy discrimination condition. Changes to this list are not propagated
 * to underlying collection.
 *
 * On write will replace all items that satisfy discrimination condition from underlying collection with provided
 * items. The other items will stay untouched.
 *
 * TODO this accessor works with lists even when field does not accept multiple values,
 *      refactor to return actual value instead of a list with one single item
 *
 * @author Octavian Ciubotaru
 */
public class DiscriminatedFieldAccessor implements FieldAccessor {

    private String discriminatorField;
    private String discriminatorValue;

    private FieldAccessor target;

    public DiscriminatedFieldAccessor(FieldAccessor target, String discriminatorField, String discriminatorValue) {
        this.target = target;
        this.discriminatorField = discriminatorField;
        this.discriminatorValue = discriminatorValue;
    }

    @Override
    public Object get(Object targetObject) {
        Collection collection = getWrappedCollection(targetObject);
        List<Object> filteredItems = new ArrayList<>();
        for (Object item : collection) {
            if (getDiscriminationValue(item).equals(discriminatorValue)) {
                filteredItems.add(item);
            }
        }
        return filteredItems;
    }

    private Collection getWrappedCollection(Object targetObject) {
        Object obj = target.get(targetObject);
        if (!(obj instanceof Collection)) {
            throw new IllegalStateException("Value is either null or does not implement java.util.Collection");
        }
        return (Collection) obj;
    }

    @Override
    public void set(Object targetObject, Object value) {
        Collection collection = getWrappedCollection(targetObject);
        TreeSet<Object> newItems = new TreeSet<>(Comparator.comparingInt(System::identityHashCode));
        newItems.addAll((Collection) value);

        Iterator it = collection.iterator();
        while (it.hasNext()) {
            Object item = it.next();
            if (getDiscriminationValue(item).equals(discriminatorValue)) {
                boolean removed = newItems.remove(item);
                if (!removed) {
                    it.remove();
                }
            }
        }
        collection.addAll(newItems);

        target.set(targetObject, collection);
    }

    private String getDiscriminationValue(Object obj) {

        Object discriminatorObj;
        try {
            discriminatorObj = PropertyUtils.getProperty(obj, discriminatorField);
        } catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
            throw new RuntimeException(String.format(
                    "Failed to read discriminator value. Object %s, discriminator field %s.",
                    obj, discriminatorField), e);
        }

        if (discriminatorObj == null) {
            throw new RuntimeException(String.format(
                    "Discriminator value must be non-null. Object %s, discriminator field %s.",
                    obj, discriminatorField));
        }

        return discriminatorObj.toString();
    }

    public static <T> T unwrapSingleElement(Collection collection) {
        Iterator iterator = collection.iterator();
        if (iterator.hasNext()) {
            return (T) iterator.next();
        } else {
            return null;
        }
    }
}
