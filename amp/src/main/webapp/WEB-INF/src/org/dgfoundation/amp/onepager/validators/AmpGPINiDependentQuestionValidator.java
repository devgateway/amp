package org.dgfoundation.amp.onepager.validators;

import org.apache.wicket.validation.IValidatable;
import org.apache.wicket.validation.ValidationError;

/**
 * Validates survey responses for depending questions(used for validating indicator survey)
 * E.g.: Q8 and Q9 should have values less than or equal to Q7
 * The validation error should be "[Q7] should only have values less than or equal to [Q8, Q9]"
 * 
 * @author Viorel Chihai 
 * 
 */
public class AmpGPINiDependentQuestionValidator extends AmpSemanticValidator<String> {

    private static final long serialVersionUID = -4876421729351708085L;
    private String dependentQuestionLabel; 

    public AmpGPINiDependentQuestionValidator(String dependentQuestionCode) {
        this.dependentQuestionLabel = "[Q" + dependentQuestionCode + "]";
    }

    @Override
    public void semanticValidate(IValidatable<String> validatable) {
        String value = validatable.getValue();
        if (value.length() > 0) {
            ValidationError error = new ValidationError();
            error.addKey("AmpGPINiDependentQuestionValidator");
            error.setVariable("label", value);
            error.setVariable("dest", dependentQuestionLabel);
            validatable.error(error);
        }
    }
}
