package org.digijava.kernel.ampapi.endpoints.resource;

import org.digijava.kernel.ampapi.endpoints.errors.ApiErrorMessage;

/**
 * @author Viorel Chihai
 */
public final class ResourceErrors {
    
    private ResourceErrors() {
    }
    
    public static final ApiErrorMessage FIELD_INVALID_URL_VALUE = new ApiErrorMessage(4, 0, "Invalid URL value");
    
    public static final ApiErrorMessage RESOURCE_NOT_FOUND = new ApiErrorMessage(4, 1,
            "Resource not found");
    
    public static final ApiErrorMessage RESOURCE_ERROR = new ApiErrorMessage(4, 2,
            "Error in content repository during processing the requested resource");
    
    public static final ApiErrorMessage FILE_SIZE_INVALID = new ApiErrorMessage(4, 3, "File is too large");
    
    public static final ApiErrorMessage INVALID_TEAM_MEMBER =
            new ApiErrorMessage(4, 4, "Creator email is not member of team");
    
    public static final ApiErrorMessage PRIVATE_RESOURCE_SUPPORTED_ONLY = new ApiErrorMessage(4, 5,
            "Only private resources are supported");
    
    public static final ApiErrorMessage FILE_NOT_FOUND = new ApiErrorMessage(4, 6, "File not found");
    
    public static final ApiErrorMessage RESOURCE_TYPE_INVALID = new ApiErrorMessage(4, 7, "Resource type is not valid");
    
}
