package org.digijava.kernel.ampapi.endpoints.errors;

public final class GenericErrors {
    
    public static final ApiErrorMessage UNKNOWN_ERROR = new ApiErrorMessage(0, 0, "Unknown Error");
    
    public static final ApiErrorMessage INTERNAL_ERROR = new ApiErrorMessage(0, 1, "Internal Error");
    
    public static final ApiErrorMessage UNAUTHORIZED = new ApiErrorMessage(0, 2, "Unauthorized operation");
    
    private GenericErrors() { }
    
}
