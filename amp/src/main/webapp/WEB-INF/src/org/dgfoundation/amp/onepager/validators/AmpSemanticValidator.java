/**
 * 
 */
package org.dgfoundation.amp.onepager.validators;

import org.apache.log4j.Logger;
import org.apache.wicket.behavior.Behavior;
import org.apache.wicket.validation.IValidatable;
import org.apache.wicket.validation.IValidator;
import org.dgfoundation.amp.onepager.components.fields.AmpCollectionValidatorField;

/**
 * @author mihai
 *  Validator that can be turned off. This means this validator is OPTIONAL. Form can save without it, but we use 
 *  it to validate semantically the form data. Example: sum of all percentages is 100%. 
 *  @see AmpPercentageCollectionValidator
 *  @see AmpCollectionValidatorField
 */
public abstract class AmpSemanticValidator<T> extends Behavior implements IValidator<T> {
    private static final Logger logger = Logger.getLogger(AmpSemanticValidator.class);
    
    protected boolean enabled=true;
    
    public boolean isEnabled() {
        return enabled;
    }
    public void setEnabled(boolean enabled) {
        this.enabled=enabled;
    }
    
    @Override
    public void validate(IValidatable<T> validatable) {
        if(!enabled) return;
        semanticValidate(validatable);
    }
    
    
    /**
     * This is identical with the {@link #validate(IValidatable)} method but it is used as a wrapper.
     * Use this one for {@link AmpSemanticValidator}S. The real {@link #validate(IValidatable)} method
     * checks if {@link #isEnabled()} is true, and if so it invokes {@link #semanticValidate(IValidatable)} 
     * @see #validate(IValidatable)
     * @param validatable
     */
    public abstract void semanticValidate(IValidatable<T> validatable);
}
