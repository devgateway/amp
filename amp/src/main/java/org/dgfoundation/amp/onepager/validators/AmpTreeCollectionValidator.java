/**
 * 
 */
package org.dgfoundation.amp.onepager.validators;

import org.apache.wicket.validation.IValidatable;
import org.apache.wicket.validation.ValidationError;

/**
 * @author mihai
 *
 */
public class AmpTreeCollectionValidator extends AmpSemanticValidator<String> {

    /**
     * 
     */
    public AmpTreeCollectionValidator() {
        // TODO Auto-generated constructor stub
    }

    /* (non-Javadoc)
     * @see org.apache.wicket.validation.IValidator#validate(org.apache.wicket.validation.IValidatable)
     */
    @Override
    public void semanticValidate(IValidatable<String> validatable) {
        String value = validatable.getValue();
        if(value.length()>0)
        {
            ValidationError error = new ValidationError();
            error.addKey("AmpTreeCollectionValidator");
            error.setVariable("label", value);
            validatable.error(error);
        }
    }

}
