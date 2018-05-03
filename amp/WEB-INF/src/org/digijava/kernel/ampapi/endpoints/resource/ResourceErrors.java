package org.digijava.kernel.ampapi.endpoints.resource;

import org.digijava.kernel.ampapi.endpoints.errors.ApiErrorMessage;

/**
 * @author Viorel Chihai
 */
public final class ResourceErrors {

    private ResourceErrors() {
    }

    public static final ApiErrorMessage FIELD_REQUIRED = new ApiErrorMessage(1, "Required field");
    public static final ApiErrorMessage FIELD_INVALID_VALUE = new ApiErrorMessage(2, "Invalid field value");
    public static final ApiErrorMessage FIELD_INVALID_URL_VALUE = new ApiErrorMessage(3, "Invalid URL value");
    public static final ApiErrorMessage RESOURCE_NOT_FOUND = new ApiErrorMessage(4, 
            "The requested resource does not exist");
    public static final ApiErrorMessage RESOURCE_ERROR = new ApiErrorMessage(5, 
            "Error durin processing the requested resource");
    
}
