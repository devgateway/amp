package org.digijava.kernel.validators.common;

import static java.util.Objects.requireNonNull;

import java.util.Collection;
import java.util.Map;

import org.digijava.kernel.ampapi.endpoints.activity.ActivityErrors;
import org.digijava.kernel.ampapi.endpoints.activity.field.APIField;
import org.digijava.kernel.ampapi.endpoints.activity.validators.ValidationErrors;
import org.digijava.kernel.ampapi.endpoints.errors.ApiErrorMessage;
import org.digijava.kernel.validation.ConstraintValidator;
import org.digijava.kernel.validation.ConstraintValidatorContext;

/**
 * Verifies that the values is within specified boundaries. Only max is currently supported.
 *
 * Applies only to collections.
 *
 * If a collection must have at least one element specified see the required validator.
 *
 * @author Octavian Ciubotaru
 */
public class SizeValidator implements ConstraintValidator {

    private Integer max;

    @Override
    public void initialize(Map<String, String> arguments) {
        max = Integer.parseInt(requireNonNull(arguments.get("max")));
    }

    @Override
    public boolean isValid(APIField type, Object value, ConstraintValidatorContext context) {
        if (value == null) {
            return true;
        }

        Collection col = (Collection) value;
        int size = col.size();
        return size <= max;
    }

    @Override
    public ApiErrorMessage getErrorMessage() {
        return ValidationErrors.FIELD_TOO_MANY_VALUES_NOT_ALLOWED;
    }
}
