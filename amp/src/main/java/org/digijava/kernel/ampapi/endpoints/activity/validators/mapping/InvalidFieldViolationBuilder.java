package org.digijava.kernel.ampapi.endpoints.activity.validators.mapping;

import org.digijava.kernel.ampapi.endpoints.activity.validators.ValidationErrors;

import javax.validation.ConstraintViolation;

/**
 * @author Nadejda Mandrescu
 */
public class InvalidFieldViolationBuilder implements ConstraintViolationBuilder {

    private String fieldPath;

    public InvalidFieldViolationBuilder(String fieldPath) {
        this.fieldPath = fieldPath;
    }

    @Override
    public JsonConstraintViolation build(ConstraintViolation v) {
        return new JsonConstraintViolation(fieldPath, ValidationErrors.FIELD_INVALID_VALUE);
    }

}
