package org.digijava.kernel.ampapi.endpoints.activity.validators.mapping;

import org.apache.commons.lang3.reflect.FieldUtils;
import org.digijava.kernel.ampapi.endpoints.activity.validators.ErrorDecorator;
import org.digijava.kernel.ampapi.endpoints.errors.ApiErrorMessage;

import javax.validation.ConstraintViolation;
import java.lang.annotation.ElementType;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;

/**
 * Puts JsonConstraintViolations into json object.
 *
 * @author Octavian Ciubotaru
 */
public class JsonErrorIntegrator {

    private Function<ConstraintViolation, JsonConstraintViolation> mapper;

    public JsonErrorIntegrator(Function<ConstraintViolation, JsonConstraintViolation> mapper) {
        this.mapper = mapper;
    }

    /**
     * Bean Validation does not offer an API to partial validation for type based constraints. This method attempts
     * to map all constraint violations for types onto Json object.
     *
     * @param json json object to be modified to include errors at their location
     * @param violations all violations found for that object
     * @param errors all API errors will be added to this Map
     */
    public <T> void mapTypeErrors(Map<String, Object> json, Set<ConstraintViolation<T>> violations,
            Map<Integer, ApiErrorMessage> errors) {
        for (ConstraintViolation v : violations) {
            // TODO not sure why was needed to restrict to Type violations only, hence adding also for field
            if (isViolationForType(v) || isViolationForField(v)) {
                JsonConstraintViolation jsonConstraintViolation = mapper.apply(v);
                if (jsonConstraintViolation != null) {

                    String jsonPath = jsonConstraintViolation.getJsonPath();

                    if (jsonPath.contains(".")) {
                        throw new RuntimeException("Json path navigation not implemented yet.");
                    }

                    ErrorDecorator.addError(json, jsonPath, jsonPath, jsonConstraintViolation.getError(), errors);
                }
            }
        }
    }

    private boolean isViolationForType(ConstraintViolation v) {
        try {
            // Hibernate implementation of Bean Validation 2 API exposes getElementType() method directly.
            // For now we're reading the property directly.
            return ElementType.TYPE.equals(FieldUtils.readField(v, "elementType", true));
        } catch (IllegalAccessException e) {
            throw new RuntimeException("Failed to determine elementType of a constraint violation", e);
        }
    }

    private boolean isViolationForField(ConstraintViolation v) {
        try {
            // Hibernate implementation of Bean Validation 2 API exposes getElementType() method directly.
            // For now we're reading the property directly.
            return ElementType.FIELD.equals(FieldUtils.readField(v, "elementType", true));
        } catch (IllegalAccessException e) {
            throw new RuntimeException("Failed to determine elementType of a constraint violation", e);
        }
    }
}
