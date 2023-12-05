package org.digijava.kernel.validation;

import org.digijava.kernel.ampapi.endpoints.activity.field.APIField;
import org.digijava.kernel.ampapi.endpoints.errors.ApiErrorMessage;

import java.util.Map;

/**
 * Validates if the value satisfies one or more constraints.
 *
 * Note: Constraint validators are expected to validate one constraint at a time and the API is geared towards that.
 * However via context parameter, validator can report many constraint violations for different constraints, though
 * not advisable to do so. Ideally, context parameter must be used only when many values fail to satisfy the same
 * constraint, and a constraint violation is needed for each invalid value.
 *
 * @author Octavian Ciubotaru
 */
public interface ConstraintValidator {

    /**
     * Initialize the constraint validator.
     *
     * @param arguments arguments for the constraint validator
     */
    void initialize(Map<String, String> arguments);

    /**
     * Validate the value and return true if the object is valid. Otherwise return false. If validator has to report
     * multiple constraint violations at once, context parameter can be used to override default constraint violation
     * creation mechanism.
     *
     * Constraint validator defined at class level will always be invoked with a non-null value.
     *
     * Note: Because the meta model is represented just as list of fields, here
     * validator accepts the field in order to have access to type information.
     * Validators must not use field name because APIField will be replaced with a different class.
     *
     * @param type type information about the object
     * @param value value to be validated
     * @param context constraint validation context for the current invocation
     * @return true if the value is valid
     */
    boolean isValid(APIField type, Object value, ConstraintValidatorContext context);

    /**
     * Returns the error message to use when creating default constraint violations.
     *
     * @return error message
     */
    ApiErrorMessage getErrorMessage();
}
