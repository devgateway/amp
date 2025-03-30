package org.digijava.kernel.validators.common;

import org.digijava.kernel.ampapi.endpoints.activity.field.APIField;
import org.digijava.kernel.ampapi.endpoints.activity.validators.ValidationErrors;
import org.digijava.kernel.ampapi.endpoints.errors.ApiErrorMessage;
import org.digijava.kernel.validation.ConstraintValidator;
import org.digijava.kernel.validation.ConstraintValidatorContext;

import java.math.BigDecimal;
import java.util.Collection;
import java.util.Map;

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

    private static final BigDecimal ONE_HUNDRED = BigDecimal.valueOf(100.0);
    private static final BigDecimal ERROR = BigDecimal.valueOf(0.0001);
    @Override
    public void initialize(Map<String, String> arguments) {
    }

    @Override
    public boolean isValid(APIField type, Object value, ConstraintValidatorContext context) {
        Collection<?> items = (Collection<?>) value;

        APIField percentageField = type.getPercentageField();
        if (percentageField == null || items == null || items.isEmpty()) {
            return true;
        }

        BigDecimal total = BigDecimal.ZERO;

        for (Object item : items) {
            Float percentage = percentageField.getFieldAccessor().get(item);
            if (percentage != null) {
                total = total.add(BigDecimal.valueOf(percentage.doubleValue())); // Convert Float to BigDecimal
            }
        }

        return total.subtract(ONE_HUNDRED).abs().compareTo(ERROR) < 0;
    }

    @Override
    public ApiErrorMessage getErrorMessage() {
        return ValidationErrors.FIELD_PERCENTAGE_SUM_BAD;
    }
}
