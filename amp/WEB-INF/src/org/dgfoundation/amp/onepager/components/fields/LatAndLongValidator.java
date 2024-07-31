package org.dgfoundation.amp.onepager.components.fields;

import org.apache.wicket.validation.IValidatable;
import org.apache.wicket.validation.ValidationError;
import org.apache.wicket.validation.validator.AbstractValidator;

public class LatAndLongValidator extends AbstractValidator<String> {
    public LatAndLongValidator() {
        super();
    }

    public LatAndLongValidator(Double min, Double max) {
        super();
    }

    @Override
    protected void onValidate(IValidatable<String> validatable) {
        String value = validatable.getValue();
        try {
            Double.parseDouble(value);
        } catch (NumberFormatException e) {
            validatable.error(new ValidationError().setMessage("The input must be a valid double."));
        }
    }
}
