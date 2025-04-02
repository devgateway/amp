package org.digijava.kernel.ampapi.endpoints.contact;

import static org.digijava.kernel.ampapi.endpoints.errors.ApiError.ERROR_CLASS_CONTACT_ID;

import org.digijava.kernel.ampapi.endpoints.errors.ApiErrorMessage;

/**
 * @author Octavian Ciubotaru
 */
public final class ContactErrors {
    
    private ContactErrors() { }
    
    public static final ApiErrorMessage UNIQUE_PRIMARY_ORG_CONTACT =
            new ApiErrorMessage(ERROR_CLASS_CONTACT_ID, 0, "Multiple primary organisation contacts are not allowed");
    
    public static final ApiErrorMessage CONTACT_NOT_FOUND = new ApiErrorMessage(ERROR_CLASS_CONTACT_ID, 1,
            "Contact not found");
}
