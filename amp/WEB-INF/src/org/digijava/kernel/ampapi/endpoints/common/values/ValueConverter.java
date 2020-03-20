package org.digijava.kernel.ampapi.endpoints.common.values;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.beanutils.ConvertUtils;
import org.apache.log4j.Logger;
import org.digijava.kernel.ampapi.discriminators.DiscriminationConfigurer;
import org.digijava.kernel.ampapi.endpoints.activity.InterchangeUtils;
import org.digijava.kernel.ampapi.endpoints.activity.field.APIField;
import org.digijava.kernel.ampapi.endpoints.resource.ResourceType;
import org.digijava.kernel.persistence.PersistenceManager;
import org.digijava.module.aim.dbentity.ApprovalStatus;

/**
 * @author Nadejda Mandrescu
 */
public class ValueConverter {
    private static final Logger logger = Logger.getLogger(ValueConverter.class);

    private Map<Class<? extends DiscriminationConfigurer>, DiscriminationConfigurer> discriminatorConfigurerCache =
            new HashMap<>();

    /**
     * Used to restore the value of the discrimination field.
     */
    private void configureDiscriminationField(Object obj, APIField fieldDef) {
        if (fieldDef.getDiscriminationConfigurer() != null) {
            DiscriminationConfigurer configurer = discriminatorConfigurerCache.computeIfAbsent(
                    fieldDef.getDiscriminationConfigurer(), this::newConfigurer);
            configurer.configure(obj, fieldDef.getDiscriminatorField(), fieldDef.getDiscriminatorValue());
        }
    }

    private DiscriminationConfigurer newConfigurer(Class<? extends DiscriminationConfigurer> configurer) {
        try {
            return configurer.newInstance();
        } catch (InstantiationException | IllegalAccessException e) {
            throw new RuntimeException("Failed to instantiate discriminator configurer " + configurer, e);
        }
    }

    /**
     * Handles integer -> long and similar conversions, since exact type is not guaranteed during deserialisation 
     * @param type
     * @param value
     * @return
     */
    public Object toSimpleTypeValue(Class<?> type, Object value) {
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
     * Instantiate the object referred by the field. If the field is a list of objects then the element of the
     * collection is instantiated.
     *
     * @param field
     * @return
     */
    public Object instantiate(APIField field) {
        try {
            Object newInstance = field.getApiType().getType().newInstance();
            configureDiscriminationField(newInstance, field);
            return newInstance;
        } catch (InstantiationException | IllegalAccessException e) {
            logger.error(e.getMessage());
            throw new RuntimeException(e);
        }
    }

    public Object getObjectById(Class<?> entityClass, Object id) {
        if (Collection.class.isAssignableFrom(entityClass)) {
            throw new RuntimeException("Can't handle a collection of ID-linked objects yet!");
        }
        if (ApprovalStatus.class.isAssignableFrom(entityClass)) {
            return ApprovalStatus.fromId(Integer.valueOf(id.toString()));
        } else if (ResourceType.class.isAssignableFrom(entityClass)) {
            return ResourceType.fromId(Integer.valueOf(id.toString()));
        } else if (InterchangeUtils.isSimpleType(entityClass)) {
            return ConvertUtils.convert(id, entityClass);
        } else {
            return PersistenceManager.getSession().get(entityClass.getName(), Long.valueOf(id.toString()));
        }
    }

}
