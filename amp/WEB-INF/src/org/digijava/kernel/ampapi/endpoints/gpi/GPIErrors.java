package org.digijava.kernel.ampapi.endpoints.gpi;

import org.digijava.kernel.ampapi.endpoints.errors.ApiErrorMessage;

public final class GPIErrors {
    
    public static final ApiErrorMessage DATE_DONOR_COMBINATION_EXISTS = new ApiErrorMessage(11, 0,
            "Please select an unique date or donor agency.");
    
    private GPIErrors() { }
}
