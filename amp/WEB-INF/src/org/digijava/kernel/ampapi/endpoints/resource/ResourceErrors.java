package org.digijava.kernel.ampapi.endpoints.resource;

import static org.digijava.kernel.ampapi.endpoints.errors.ApiError.ERROR_CLASS_RESOURCE_ID;

import org.digijava.kernel.ampapi.endpoints.errors.ApiErrorMessage;

/**
 * @author Viorel Chihai
 */
public final class ResourceErrors {
    
    private ResourceErrors() {
    }
    
    public static final ApiErrorMessage FIELD_INVALID_URL_VALUE = new ApiErrorMessage(ERROR_CLASS_RESOURCE_ID, 0,
            "Invalid URL value");
    
    public static final ApiErrorMessage RESOURCE_NOT_FOUND = new ApiErrorMessage(ERROR_CLASS_RESOURCE_ID, 1,
            "Resource not found");
    
    public static final ApiErrorMessage RESOURCE_ERROR = new ApiErrorMessage(ERROR_CLASS_RESOURCE_ID, 2,
            "Error in content repository during processing the requested resource");
    
    public static final ApiErrorMessage FILE_SIZE_INVALID = new ApiErrorMessage(ERROR_CLASS_RESOURCE_ID, 3,
            "File is too large");
    
    public static final ApiErrorMessage INVALID_TEAM_MEMBER =
            new ApiErrorMessage(ERROR_CLASS_RESOURCE_ID, 4, "Creator email is not member of team");
    
    public static final ApiErrorMessage PRIVATE_RESOURCE_SUPPORTED_ONLY =
            new ApiErrorMessage(ERROR_CLASS_RESOURCE_ID, 5, "Only private resources are supported");
    
    public static final ApiErrorMessage FILE_NOT_FOUND = new ApiErrorMessage(ERROR_CLASS_RESOURCE_ID, 6,
            "File not found");
    
    public static final ApiErrorMessage RESOURCE_TYPE_INVALID = new ApiErrorMessage(ERROR_CLASS_RESOURCE_ID, 7,
            "Resource type is not valid");

    public static final ApiErrorMessage INVALID_YEAR_OF_PUBLICATION =
            new ApiErrorMessage(ERROR_CLASS_RESOURCE_ID, 8, "Invalid year of publication");
    
}
