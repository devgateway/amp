package org.dgfoundation.amp.onepager.validators;

import org.apache.wicket.validation.IValidatable;
import org.apache.wicket.validation.ValidationError;

/**
 * Checks if the indicator responses are populated (used for validating indicator survey)
 * 
 * @author Viorel Chihai 
 * 
 */
public class AmpGPINiIndicatorValidator extends AmpSemanticValidator<String> {

    private static final long serialVersionUID = -4876421729351708085L;

    public AmpGPINiIndicatorValidator() {

    }

    @Override
    public void semanticValidate(IValidatable<String> validatable) {
        String value = validatable.getValue();
        if (value.length() > 0) {
            ValidationError error = new ValidationError();
            error.addKey("AmpGPINiIndicatorValidator");
            error.setVariable("label", value);
            validatable.error(error);
        }
    }
}
