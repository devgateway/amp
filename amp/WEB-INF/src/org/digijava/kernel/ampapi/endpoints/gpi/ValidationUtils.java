package org.digijava.kernel.ampapi.endpoints.gpi;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.ws.rs.core.Response;

import org.digijava.kernel.ampapi.endpoints.errors.ApiRuntimeException;
import org.digijava.kernel.ampapi.endpoints.util.JsonBean;

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
            JsonBean jsBean = new JsonBean();
            jsBean.set("errors", violations);
            throw new ApiRuntimeException(Response.Status.BAD_REQUEST, jsBean);
        }
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
