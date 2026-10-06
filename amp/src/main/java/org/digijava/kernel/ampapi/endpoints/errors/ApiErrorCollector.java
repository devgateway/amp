package org.digijava.kernel.ampapi.endpoints.errors;

import org.apache.commons.lang3.reflect.FieldUtils;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.List;

/**
 * This class collects all errors for an API component.
 *
 * @author Octavian Ciubotaru
 */
public class ApiErrorCollector {

    /**
     * Collect all errors for a specific API component. As input it takes a class in which all error are defined
     * and generated a list of all errors for this component. It collects only public static ApiErrorMessage fields.
     * It will also search for errors in super classes.
     *
     * @param errorsClass class that contains the errors
     * @return list of all found errors
     */
    public List<ApiErrorMessage> collect(Class errorsClass) {
        try {
            List<ApiErrorMessage> messages = new ArrayList<>();
            Class<?> currentClass = errorsClass;
            while (currentClass != null) {
                final Field[] declaredFields = currentClass.getDeclaredFields();
                for (Field field : declaredFields) {
                    if (Modifier.isStatic(field.getModifiers())
                            && Modifier.isPublic(field.getModifiers())
                            && ApiErrorMessage.class.isAssignableFrom(field.getType())) {
                        Object value = FieldUtils.readStaticField(field);
                        if (value != null) {
                            messages.add((ApiErrorMessage) value);
                        }
                    }
                }
                currentClass = currentClass.getSuperclass();
            }
            return messages;
        } catch (IllegalAccessException e) {
            // we can get here if errorsClass has a modifier that does not allow accessing it from this class
            // most of the time error class is a public one, so we should not worry much about this
            throw new RuntimeException(e);
        }
    }
}
