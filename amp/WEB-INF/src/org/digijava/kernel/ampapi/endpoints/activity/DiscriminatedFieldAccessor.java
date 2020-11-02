package org.digijava.kernel.ampapi.endpoints.activity;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.TreeSet;

import org.apache.commons.beanutils.PropertyUtils;
import org.digijava.kernel.request.TLSUtils;

import static org.digijava.kernel.ampapi.endpoints.activity.ActivityInterchangeUtils.WORKSPACE_PREFIX;

/**
 * On read will return a list of objects that satisfy discrimination condition. Changes to this list are not propagated
 * to underlying collection.
 *
 * On write will replace all items that satisfy discrimination condition from underlying collection with provided
 * items. The other items will stay untouched.
 *
 * If the discriminated field is known to have at most one value then instead of a collection the actual object
 * is returned. Setting value to null in such case will mean that this element will be removed from the underlying
 * collection.
 *
 * @author Octavian Ciubotaru
 */
public class DiscriminatedFieldAccessor implements FieldAccessor {

    private final String discriminatorField;
    private final String discriminatorValue;

    private final FieldAccessor targetField;

    private final boolean multipleValues;

    public DiscriminatedFieldAccessor(FieldAccessor targetField, String discriminatorField, String discriminatorValue,
            boolean multipleValues) {
        this.targetField = targetField;
        this.discriminatorField = discriminatorField;
        this.discriminatorValue = discriminatorValue;
        this.multipleValues = multipleValues;
    }

    @Override
    public <T> T get(Object targetObject) {
        String prefix = "" + TLSUtils.getRequest().getAttribute(WORKSPACE_PREFIX);
        Collection<?> collection = getWrappedCollection(targetObject);
        if (multipleValues) {
            return (T) getList(collection, prefix);
        } else {
            return (T) getObject(collection, prefix);
        }
    }

    private Object getObject(Collection<?> collection, String prefix) {
        Object filteredItem = null;
        for (Object item : collection) {
            if (getDiscriminationValue(item).equals(discriminatorValue)
                    || getDiscriminationValue(item).equals(prefix + discriminatorValue)) {
                if (filteredItem != null) {
                    throw new RuntimeException(
                            "MultipleValues is false but the underlying collection contains two items.");
                }
                filteredItem = item;
            }
        }
        return filteredItem;
    }

    private Object getList(Collection<?> collection, String prefix) {
        List<Object> filteredItems = new ArrayList<>();
        for (Object item : collection) {
            if (getDiscriminationValue(item).equals(discriminatorValue)
                    || getDiscriminationValue(item).equals(prefix + discriminatorValue)) {
                filteredItems.add(item);
            }
        }
        return filteredItems;
    }

    @Override
    public void set(Object targetObject, Object value) {
        Collection targetCollection = getWrappedCollection(targetObject);
        String prefix = "" + TLSUtils.getRequest().getAttribute(WORKSPACE_PREFIX);
        if (multipleValues) {
            setList(targetCollection, (Collection) value, prefix);
        } else {
            setObject(targetCollection, value, prefix);
        }

        targetField.set(targetObject, targetCollection);
    }

    private void setObject(Collection targetCollection, Object value, String prefix) {
        Iterator<?> it = targetCollection.iterator();
        boolean refPresent = false;
        boolean found = false;
        while (it.hasNext()) {
            Object item = it.next();
            if (getDiscriminationValue(item).equals(discriminatorValue)
                    || getDiscriminationValue(item).equals(prefix + discriminatorValue)) {
                if (found) {
                    throw newMultipleValuesException();
                }
                found = true;
                if (item == value) {
                    refPresent = true;
                } else {
                    it.remove();
                }
            }
        }
        if (value != null && !refPresent) {
            targetCollection.add(value);
        }
    }

    private void setList(Collection targetCollection, Collection values, String prefix) {
        TreeSet<Object> newItems = new TreeSet<>(Comparator.comparingInt(System::identityHashCode));
        newItems.addAll(values);

        Iterator<?> it = targetCollection.iterator();
        while (it.hasNext()) {
            Object item = it.next();
            if (getDiscriminationValue(item).equals(discriminatorValue)
                    || getDiscriminationValue(item).equals(prefix + discriminatorValue)) {
                boolean removed = newItems.remove(item);
                if (!removed) {
                    it.remove();
                }
            }
        }
        targetCollection.addAll(newItems);
    }

    private IllegalStateException newMultipleValuesException() {
        return new IllegalStateException("Field is marked as single value but there are multiple values"
                + " in the underlying collection. Accessor: " + this.toString());
    }

    private Collection getWrappedCollection(Object targetObject) {
        Object obj = targetField.get(targetObject);
        if (!(obj instanceof Collection)) {
            throw new IllegalStateException("Value is either null or does not implement java.util.Collection for "
                    + this.toString());
        }
        return (Collection) obj;
    }

    private String getDiscriminationValue(Object obj) {

        Object discriminatorObj;
        try {
            discriminatorObj = PropertyUtils.getProperty(obj, discriminatorField);
        } catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
            throw new RuntimeException(String.format(
                    "Failed to read discriminator value. Object %s, accessor %s.",
                    obj, this), e);
        }

        if (discriminatorObj == null) {
            throw new RuntimeException(String.format(
                    "Discriminator value must be non-null. Object %s, accessor %s.",
                    obj, this));
        }

        return discriminatorObj.toString();
    }

    @Override
    public String toString() {
        return "Discriminated accessor for " + targetField.toString() + " where "
                + discriminatorField + "=" + discriminatorValue;
    }

}
