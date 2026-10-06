package org.digijava.kernel.ampapi.endpoints.errors;

import static org.digijava.kernel.ampapi.endpoints.errors.ApiError.ERROR_CLASS_GENERIC_ID;

public final class GenericErrors {
    
    public static final ApiErrorMessage UNKNOWN_ERROR = new ApiErrorMessage(ERROR_CLASS_GENERIC_ID, 0,
            "Unknown Error");
    
    public static final ApiErrorMessage INTERNAL_ERROR = new ApiErrorMessage(ERROR_CLASS_GENERIC_ID, 1,
            "Internal Error");
    
    public static final ApiErrorMessage UNAUTHORIZED = new ApiErrorMessage(ERROR_CLASS_GENERIC_ID, 2,
            "Unauthorized operation");
    
    private GenericErrors() { }
    
}
