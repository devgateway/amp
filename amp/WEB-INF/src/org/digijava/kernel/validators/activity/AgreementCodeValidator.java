package org.digijava.kernel.validators.activity;

import java.util.Map;
import java.util.function.Function;
import java.util.function.Supplier;

import org.apache.commons.lang.StringUtils;
import org.digijava.kernel.ampapi.endpoints.activity.field.APIField;
import org.digijava.kernel.ampapi.endpoints.activity.validators.ValidationErrors;
import org.digijava.kernel.ampapi.endpoints.errors.ApiErrorMessage;
import org.digijava.kernel.validation.ConstraintValidator;
import org.digijava.kernel.validation.ConstraintValidatorContext;

/**
 * Verify that agreement code is unique.
 *
 * @author Octavian Ciubotaru
 */
public class AgreementCodeValidator implements ConstraintValidator {

    /**
     * Input: agreement code. Output: agreement count.
     */
    private static Function<String, Integer> counter;

    public static <T> T withCounter(Function<String, Integer> counter, Supplier<T> supplier) {
        Function<String, Integer> oldCounter = AgreementCodeValidator.counter;
        try {
            AgreementCodeValidator.counter = counter;
            return supplier.get();
        } finally {
            AgreementCodeValidator.counter = oldCounter;
        }
    }

    @Override
    public void initialize(Map<String, String> arguments) {
    }

    @Override
    public boolean isValid(APIField type, Object value, ConstraintValidatorContext context) {
        if (counter == null) {
            throw new IllegalStateException("No environment specified. See AgreementCodeValidator#withCounter.");
        }

        String agreementCode = (String) value;

        if (StringUtils.isNotBlank(agreementCode)) {
            Integer count = counter.apply(agreementCode);
            return count == 0;
        } else {
            return true;
        }
    }

    @Override
    public ApiErrorMessage getErrorMessage() {
        return ValidationErrors.AGREEMENT_CODE_UNIQUE;
    }
}
