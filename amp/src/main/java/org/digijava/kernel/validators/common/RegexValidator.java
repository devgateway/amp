package org.digijava.kernel.validators.common;

import org.digijava.kernel.ampapi.endpoints.activity.field.APIField;
import org.digijava.kernel.ampapi.endpoints.activity.validators.ValidationErrors;
import org.digijava.kernel.ampapi.endpoints.errors.ApiErrorMessage;
import org.digijava.kernel.validation.ConstraintValidator;
import org.digijava.kernel.validation.ConstraintValidatorContext;

import java.util.Map;
import java.util.Objects;
import java.util.regex.Pattern;

/**
 * Verifies that the value matches the regex pattern.
 *
 * @author Octavian Ciubotaru
 */
public class RegexValidator implements ConstraintValidator {

    private Pattern pattern;

    @Override
    public void initialize(Map<String, String> arguments) {
        String regex = Objects.requireNonNull(arguments.get("regex"));
        pattern = Pattern.compile(regex);
    }

    @Override
    public boolean isValid(APIField type, Object value, ConstraintValidatorContext context) {
        if (value == null) {
            return true;
        }

        String stringValue = (String) value;
        return pattern.matcher(stringValue).matches();
    }

    @Override
    public ApiErrorMessage getErrorMessage() {
        return ValidationErrors.FIELD_INVALID_VALUE;
    }
}
