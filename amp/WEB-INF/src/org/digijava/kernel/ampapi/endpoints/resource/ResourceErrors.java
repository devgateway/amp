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
            "Resource not found");
    public static final ApiErrorMessage RESOURCE_ERROR = new ApiErrorMessage(5, 
            "Error during processing the requested resource");
    public static final ApiErrorMessage FILE_SIZE_INVALID = new ApiErrorMessage(6, "File is too large");
    public static final ApiErrorMessage INVALID_TEAM_MEMBER = 
            new ApiErrorMessage(7, "Creator email is not member of team");
    public static final ApiErrorMessage PRIVATE_RESOURCE_SUPPORTED_ONLY = new ApiErrorMessage(7, 
            "Only private resources are supported");
    
}
