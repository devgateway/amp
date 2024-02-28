package org.digijava.kernel.ampapi.endpoints.gpi;

import java.util.*;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.ws.rs.core.Response;

import org.digijava.kernel.ampapi.endpoints.errors.ApiError;
import org.digijava.kernel.ampapi.endpoints.errors.ApiRuntimeException;

/**
 * @author Octavian Ciubotaru
 */
public final class ValidationUtils {

    private ValidationUtils() {
    }

    // TODO after upgrade to jersey 2 replace calls to this method with @Valid annotation on resource method parameters
    // TODO how to represent the errors in a more human readable way? Should it also be machine readable?
    /**
     * Use Bean Validation Framework to check if object is valid.
     * <p>If object is invalid, then {@link ApiRuntimeException} with status code 400 is raised.
     */
    public static void requireValid(Object obj) {
        List<String> violations = validate(obj);
        if (!violations.isEmpty()) {
            throw new ApiRuntimeException(Response.Status.BAD_REQUEST, ApiError.toError(violations));
        }
    }

    public static void valuesValid(Collection possibleValues, Object value) {
        if (possibleValues == null) { throw new IllegalArgumentException("possibleValues cannot be null"); }
        for (Object possibleValue : possibleValues) {
            if (possibleValue.equals(value)) {
                return;
            }
        }
        List<String> violations = new ArrayList<>();
        violations.add("Invalid value: " + value);
        throw new ApiRuntimeException(Response.Status.BAD_REQUEST, ApiError.toError(violations));
    }

    private static <T> List<String> validate(T obj) {
        List<String> errorMessages = new ArrayList<String>();
        Validator validator = Validation.buildDefaultValidatorFactory().getValidator();

        if (obj instanceof Iterable) {
            Iterator iterator = ((Iterable) obj).iterator();
            int idx = 0;
            while (iterator.hasNext()) {
                Object currItem = iterator.next();

                Set<ConstraintViolation<Object>> violations = validator.validate(currItem);
                if (!violations.isEmpty()) {
                    for (ConstraintViolation<Object> violation : violations) {
                        errorMessages.add("[" + idx + "]." + violation.getPropertyPath() + ": "
                                + violation.getMessage());
                    }
                }

                idx++;
            }
        } else {
            Set<ConstraintViolation<T>> violations = validator.validate(obj);
            if (!violations.isEmpty()) {
                for (ConstraintViolation<T> violation : violations) {
                    errorMessages.add(violation.getPropertyPath() + ": " + violation.getMessage());
                }
            }
        }
        return errorMessages;
    }
}
