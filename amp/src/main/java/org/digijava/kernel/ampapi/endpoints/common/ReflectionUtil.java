package org.digijava.kernel.ampapi.endpoints.common;

import org.apache.log4j.Logger;

import java.lang.reflect.Field;

/**
 * @author Octavian Ciubotaru
 */
public final class ReflectionUtil {

    private static final Logger logger = Logger.getLogger(ReflectionUtil.class);

    private ReflectionUtil() {
    }

    public static Field getField(Object parent, String actualFieldName) {
        if (parent == null) {
            return null;
        }
        Field field = null;
        try {
            Class<?> clazz = parent.getClass();
            while (field == null && !clazz.equals(Object.class)) {
                try {
                    field = clazz.getDeclaredField(actualFieldName);
                    field.setAccessible(true);
                } catch (NoSuchFieldException ex) {
                    clazz = clazz.getSuperclass();
                }
            }
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            throw new RuntimeException(e);
        }
        return field;
    }
}
