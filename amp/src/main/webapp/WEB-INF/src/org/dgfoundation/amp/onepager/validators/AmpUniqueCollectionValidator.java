/**
 * 
 */
package org.dgfoundation.amp.onepager.validators;

import org.apache.wicket.validation.IValidatable;
import org.apache.wicket.validation.IValidator;
import org.apache.wicket.validation.ValidationError;

/**
 * @author mihai
 *
 */
public class AmpUniqueCollectionValidator  extends AmpSemanticValidator<String>{

    /**
     * 
     */
    public AmpUniqueCollectionValidator() {
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
            error.addKey("AmpUniqueCollectionValidator");
            error.setVariable("label", value);
            validatable.error(error);
        }
    }

}
