package org.digijava.kernel.ampapi.endpoints.common.values;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

import org.apache.commons.beanutils.ConvertUtils;
import org.apache.log4j.Logger;
import org.digijava.kernel.ampapi.endpoints.activity.InterchangeUtils;
import org.digijava.kernel.ampapi.endpoints.resource.ResourceType;
import org.digijava.kernel.persistence.PersistenceManager;
import org.digijava.module.aim.dbentity.ApprovalStatus;

/**
 * @author Nadejda Mandrescu
 */
public class ValueConverter {
    private static final Logger logger = Logger.getLogger(ValueConverter.class);

    /**
     * Handles integer -> long and similar conversions, since exact type is not guaranteed during deserialisation 
     * @param value
     * @param type
     * @return
     */
    public Object toSimpleTypeValue(Object value, Class<?> type) {
        if (value == null || type.isAssignableFrom(value.getClass())) {
            return value;
        }
        try {
            Method valueOf = type.getMethod("valueOf", String.class);
            return valueOf.invoke(type, value.toString());
        } catch (NoSuchMethodException | SecurityException | IllegalAccessException | IllegalArgumentException
                | InvocationTargetException e) {
            logger.error("Could not automatically convert the value. The deserializer configuration may be missing.");
            throw new RuntimeException(e);
        }
    }

    /**
     * Generates an instance of the type of the field
     * @param parent
     * @param field
     * @return
     */
    public Object getNewInstance(Object parent, Field field) {
        Object fieldValue;
        try {
            if (SortedSet.class.isAssignableFrom(field.getType())) {
                fieldValue = new TreeSet<>();
            } else if (Set.class.isAssignableFrom(field.getType())) {
                fieldValue = new HashSet<>();
            } else if (List.class.isAssignableFrom(field.getType())) {
                fieldValue = new ArrayList<>();
            } else if (Collection.class.isAssignableFrom(field.getType())) {
                fieldValue = new ArrayList<>();
            } else {
                fieldValue = field.getType().newInstance();
            }
            field.set(parent, fieldValue);
        } catch (InstantiationException | IllegalAccessException e) {
            logger.error(e.getMessage());
            throw new RuntimeException(e);
        }
        return fieldValue;
    }

    public Object getObjectById(Class<?> entityClass, Object id) {
        if (Collection.class.isAssignableFrom(entityClass)) {
            throw new RuntimeException("Can't handle a collection of ID-linked objects yet!");
        }
        if (ApprovalStatus.class.isAssignableFrom(entityClass)) {
            return ApprovalStatus.fromId((Integer) id);
        } else if (ResourceType.class.isAssignableFrom(entityClass)) {
            return ResourceType.fromId((Integer) id);
        } else if (InterchangeUtils.isSimpleType(entityClass)) {
            return ConvertUtils.convert(id, entityClass);
        } else {
            return PersistenceManager.getSession().get(entityClass.getName(), Long.valueOf(id.toString()));
        }
    }

}
