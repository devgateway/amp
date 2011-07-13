package org.dgfoundation.amp.onepager.validators;

import java.util.regex.Pattern;

import org.apache.wicket.validation.IValidatable;
import org.apache.wicket.validation.validator.EmailAddressValidator;
import org.apache.wicket.validation.validator.StringValidator;
import org.digijava.module.aim.util.ContactInfoUtil;

public class ContactEmailValidator extends EmailAddressValidator {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private Long contcatId;

	public ContactEmailValidator(Long contactId) {
		super();
		this.contcatId=contactId;
	}

	@Override
	protected void onValidate(IValidatable<String> validatable) {
		super.onValidate(validatable);
		final String email = validatable.getValue();
			int contactWithSameEmail=0;
			try {
				contactWithSameEmail = ContactInfoUtil.getContactsCount(email,contcatId);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				error(validatable);
			}
			if(contactWithSameEmail>0){
				error(validatable, "not_unique");
			}	
		}	
}
