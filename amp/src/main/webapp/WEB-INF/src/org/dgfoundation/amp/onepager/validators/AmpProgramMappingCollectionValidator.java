/**
 * 
 */
package org.dgfoundation.amp.onepager.validators;

import org.apache.wicket.validation.IValidatable;
import org.apache.wicket.validation.ValidationError;

/**
 * @author Viorel Chihai
 *
 */
public class AmpProgramMappingCollectionValidator extends AmpSemanticValidator<String> {

    /* (non-Javadoc)
     * @see org.apache.wicket.validation.IValidator#validate(org.apache.wicket.validation.IValidatable)
     */
    @Override
    public void semanticValidate(IValidatable<String> validatable) {
        String value = validatable.getValue();
        if (value.length() > 0) {
            ValidationError error = new ValidationError();
            error.addKey("AmpProgramMappingCollectionValidator");
            error.setVariable("label", value);
            validatable.error(error);
        }
    }

}