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
public class AmpTreeCollectionValidator implements IValidator<String> {

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
	public void validate(IValidatable<String> validatable) {
		String value = validatable.getValue();
		if(value.length()>0)
		{
			ValidationError error = new ValidationError();
			error.addMessageKey("AmpTreeCollectionValidator");
			validatable.error(error);
		}
	}

}
