package org.dgfoundation.amp.onepager.validators;

import org.apache.wicket.validation.IValidatable;
import org.apache.wicket.validation.ValidationError;

/**
 * @author aartimon@developmentgateway.org
 * @since 31 July 2013
 */
public class AmpMaxSizeCollectionValidator extends AmpSemanticValidator<Integer>{

    private Integer maximum;

    public AmpMaxSizeCollectionValidator(int maximum) {
        this.maximum =maximum;
    }

    /* (non-Javadoc)
     * @see org.apache.wicket.validation.IValidator#validate(org.apache.wicket.validation.IValidatable)
     */
    @Override
    public void semanticValidate(IValidatable<Integer> validatable) {
        Integer value = validatable.getValue();
        if (value.compareTo(maximum) > 0)
        {
            ValidationError error = new ValidationError();
            error.addKey("AmpMaxSizeCollectionValidator");
            error.setVariable("maximum", maximum);
            validatable.error(error);
        }

    }

}
