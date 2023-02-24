package org.digijava.kernel.ampapi.endpoints.gpi;

import static org.digijava.kernel.ampapi.endpoints.errors.ApiError.ERROR_CLASS_GPI_ID;

import org.digijava.kernel.ampapi.endpoints.errors.ApiErrorMessage;

public final class GPIErrors {
    
    public static final ApiErrorMessage DATE_DONOR_COMBINATION_EXISTS = new ApiErrorMessage(ERROR_CLASS_GPI_ID, 0,
            "Please select an unique date or donor agency.");
    
    private GPIErrors() { }
}
