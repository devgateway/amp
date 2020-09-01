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

    private String discriminatorField;
    private String discriminatorValue;

    private boolean multipleValues;

    private FieldAccessor target;

    public DiscriminatedFieldAccessor(FieldAccessor target, String discriminatorField, String discriminatorValue,
            boolean multipleValues) {
        this.target = target;
        this.discriminatorField = discriminatorField;
        this.discriminatorValue = discriminatorValue;
        this.multipleValues = multipleValues;
    }

    @Override
    public Object get(Object targetObject) {
        Collection collection = getWrappedCollection(targetObject);
        String prefix = "" + TLSUtils.getRequest().getAttribute(WORKSPACE_PREFIX);
        if (multipleValues) {
            List<Object> filteredItems = new ArrayList<>();
            for (Object item : collection) {
                // AMPOFFLINE-1528
                if (getDiscriminationValue(item).equals(discriminatorValue)
                        || getDiscriminationValue(item).equals(prefix + discriminatorValue)) {
                    filteredItems.add(item);
                }
            }
            return filteredItems;
        } else {
            Object singleItem = null;
            for (Object item : collection) {
                // AMPOFFLINE-1528
                if (getDiscriminationValue(item).equals(discriminatorValue)
                        || getDiscriminationValue(item).equals(prefix + discriminatorValue)) {
                    if (singleItem == null) {
                        singleItem = item;
                    } else {
                        throw newMultipleValuesException();
                    }
                }
            }
            return singleItem;
        }
    }

    private Collection getWrappedCollection(Object targetObject) {
        Object obj = target.get(targetObject);
        if (!(obj instanceof Collection)) {
            throw new IllegalStateException("Value is either null or does not implement java.util.Collection for "
                    + this.toString());
        }
        return (Collection) obj;
    }

    @Override
    public void set(Object targetObject, Object value) {
        Collection collection = getWrappedCollection(targetObject);
        String prefix = "" + TLSUtils.getRequest().getAttribute(WORKSPACE_PREFIX);
        if (multipleValues) {
            TreeSet<Object> newItems = new TreeSet<>(Comparator.comparingInt(System::identityHashCode));
            newItems.addAll((Collection) value);

            Iterator it = collection.iterator();
            while (it.hasNext()) {
                Object item = it.next();
                // AMPOFFLINE-1528
                if (getDiscriminationValue(item).equals(discriminatorValue)
                        || getDiscriminationValue(item).equals(prefix + discriminatorValue)) {
                    boolean removed = newItems.remove(item);
                    if (!removed) {
                        it.remove();
                    }
                }
            }
            collection.addAll(newItems);
        } else {
            boolean removed = false;
            Iterator it = collection.iterator();
            while (it.hasNext()) {
                Object item = it.next();
                // AMPOFFLINE-1528
                if (getDiscriminationValue(item).equals(discriminatorValue)
                        || getDiscriminationValue(item).equals(prefix + discriminatorValue)) {
                    it.remove();
                    if (removed) {
                        throw newMultipleValuesException();
                    }
                    removed = true;
                }
            }
            if (value != null) {
                collection.add(value);
            }
        }

        target.set(targetObject, collection);
    }

    private IllegalStateException newMultipleValuesException() {
        return new IllegalStateException("Field is marked as single value but there are multiple values"
                + " in the underlying collection. Accessor: " + this.toString());
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
        return "Discriminated accessor for " + target.toString() + " where "
                + discriminatorField + "=" + discriminatorValue;
    }
}
