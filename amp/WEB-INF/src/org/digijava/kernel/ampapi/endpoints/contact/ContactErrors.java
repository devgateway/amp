package org.digijava.kernel.ampapi.endpoints.contact;

import org.digijava.kernel.ampapi.endpoints.errors.ApiErrorMessage;

/**
 * @author Octavian Ciubotaru
 */
public final class ContactErrors {

    private ContactErrors() {
    }

    public static final ApiErrorMessage UNIQUE_PRIMARY_ORG_CONTACT =
            new ApiErrorMessage(22, "Multiple primary organisation contacts are not allowed");
}
