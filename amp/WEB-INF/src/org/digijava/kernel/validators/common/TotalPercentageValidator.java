package org.digijava.kernel.validators.common;

import java.util.Collection;
import java.util.Map;

import org.digijava.kernel.ampapi.endpoints.activity.ActivityErrors;
import org.digijava.kernel.ampapi.endpoints.activity.field.APIField;
import org.digijava.kernel.ampapi.endpoints.errors.ApiErrorMessage;
import org.digijava.kernel.validation.ConstraintValidator;
import org.digijava.kernel.validation.ConstraintValidatorContext;

/**
 * Validates that the percentages from a collection sum up to 100. To read the percentage for each element
 * {@link APIField#getPercentageField()} is used.
 *
 * <p>Corner cases:
 * <ul><li>Null is a valid group.</li>
 * <li>Null percentage is treated as a zero.</li></ul></p>
 *
 * @author Octavian Ciubotaru
 */
public class TotalPercentageValidator implements ConstraintValidator {

    private static final float ERROR = 0.0001f;
    private static final float ONE_HUNDRED = 100f;

    @Override
    public void initialize(Map<String, String> arguments) {
    }

    @Override
    public boolean isValid(APIField type, Object value, ConstraintValidatorContext context) {
        Collection items = (Collection) value;

        APIField percentageField = type.getPercentageField();
        if (percentageField == null || items == null || items.isEmpty()) {
            return true;
        }

        float total = 0f;

        for (Object item : items) {
            Float percentage = percentageField.getFieldAccessor().get(item);
            if (percentage != null) {
                total += percentage;
            }
        }

        return Math.abs(ONE_HUNDRED - total) < ERROR;
    }

    @Override
    public ApiErrorMessage getErrorMessage() {
        return ActivityErrors.FIELD_PERCENTAGE_SUM_BAD;
    }
}
