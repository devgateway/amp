/**
 * 
 */
package org.dgfoundation.amp.onepager.validators;

import org.apache.wicket.validation.IValidatable;
import org.apache.wicket.validation.ValidationError;

/**
 * @author mihai
 * Checks if the given value is 100 (used for validating collection percentages)
 */
public class AmpPercentageCollectionValidator extends AmpSemanticValidator<Double>{

    /**
     * @param component
     */
    public AmpPercentageCollectionValidator() {
        
    }

    /* (non-Javadoc)
     * @see org.apache.wicket.validation.IValidator#validate(org.apache.wicket.validation.IValidatable)
     */
    @Override
    public void semanticValidate(IValidatable<Double> validatable) {
        Double value = validatable.getValue();
        if(value.doubleValue()!=100)
        {
            ValidationError error = new ValidationError();
            error.addKey("AmpPercentageCollectionValidator");
            error.setVariable("label", value);
            validatable.error(error);
        }


    }
}
