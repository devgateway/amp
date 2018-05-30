package org.digijava.kernel.ampapi.endpoints.contact;

import org.digijava.kernel.ampapi.endpoints.errors.ApiErrorMessage;

/**
 * @author Octavian Ciubotaru
 */
public final class ContactErrors {

    private ContactErrors() {
    }

    public static final ApiErrorMessage FIELD_REQUIRED = new ApiErrorMessage(1, "Required field");
    public static final ApiErrorMessage FIELD_INVALID_VALUE = new ApiErrorMessage(3, "Invalid field value");
    public static final ApiErrorMessage FIELD_READ_ONLY = new ApiErrorMessage(4, "Read-only field");
    public static final ApiErrorMessage UNIQUE_PRIMARY_ORG_CONTACT =
            new ApiErrorMessage(22, "Multiple primary organisation contacts are not allowed");
}
