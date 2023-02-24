/**
 * 
 */
package org.dgfoundation.amp.onepager.validators;

import org.apache.wicket.validation.IValidatable;
import org.apache.wicket.validation.IValidator;
import org.apache.wicket.validation.ValidationError;

/**
 *  Return a nice error when a collection is of a size smaller than the given (usually 1)
 * @author mihai
 *
 */
public class AmpMinSizeCollectionValidator extends AmpSemanticValidator<Integer>{

    private Integer minimum;
    
    public AmpMinSizeCollectionValidator(int minimum) {
        this.minimum=minimum;
    }

    /* (non-Javadoc)
     * @see org.apache.wicket.validation.IValidator#validate(org.apache.wicket.validation.IValidatable)
     */
    @Override
    public void semanticValidate(IValidatable<Integer> validatable) {
        Integer value = validatable.getValue();
        if (value.compareTo(minimum) < 0)
        {
            ValidationError error = new ValidationError();
            error.addKey("AmpMinSizeCollectionValidator");
            error.setVariable("minimum", minimum);      
            validatable.error(error);
        }

    }

}
