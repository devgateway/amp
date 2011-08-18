/**
 * 
 */
package org.dgfoundation.amp.onepager.validators;

import org.apache.wicket.Component;
import org.apache.wicket.validation.IValidatable;
import org.apache.wicket.validation.ValidationError;

/**
 * @author mihai
 *
 */
public class AmpRequiredFieldValidator<T> extends AbstractTrnValidator<T> {

	/**
	 * @param component
	 */
	public AmpRequiredFieldValidator(Component component) {
		super(component);
		// TODO Auto-generated constructor stub
	}

	/* (non-Javadoc)
	 * @see org.apache.wicket.validation.IValidator#validate(org.apache.wicket.validation.IValidatable)
	 */
	@Override
	public void validate(IValidatable<T> validatable) {
		T value = validatable.getValue();
		if(value==null)
		{
			ValidationError error = getTrnError("Field is required!");
			validatable.error(error);
		}



	}

}
