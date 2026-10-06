package org.dgfoundation.amp.onepager.validators;

import org.apache.wicket.validation.validator.EmailAddressValidator;

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
}
