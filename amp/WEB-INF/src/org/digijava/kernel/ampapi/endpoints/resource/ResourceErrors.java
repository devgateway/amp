package org.digijava.kernel.ampapi.endpoints.resource;

import org.digijava.kernel.ampapi.endpoints.errors.ApiErrorMessage;

/**
 * @author Viorel Chihai
 */
public final class ResourceErrors {

    private ResourceErrors() {
    }

    public static final ApiErrorMessage FIELD_REQUIRED = new ApiErrorMessage(1, "Required field");
    public static final ApiErrorMessage FIELD_INVALID_VALUE = new ApiErrorMessage(3, "Invalid field value");

}
