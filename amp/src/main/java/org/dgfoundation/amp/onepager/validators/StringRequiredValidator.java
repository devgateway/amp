package org.dgfoundation.amp.onepager.validators;

import org.apache.wicket.validation.IValidatable;
import org.apache.wicket.validation.IValidator;
import org.apache.wicket.validation.ValidationError;

/**
 * @author aartimon
 * @since 06/12/13
 */
public class StringRequiredValidator implements IValidator<String> {

    @Override
    public void validate(IValidatable<String> validatable) {
        String value = validatable.getValue();
        if(value == null || value.trim().length() == 0)
        {
            ValidationError error = new ValidationError();
            error.addKey("Required");
            validatable.error(error);
        }
    }
}
