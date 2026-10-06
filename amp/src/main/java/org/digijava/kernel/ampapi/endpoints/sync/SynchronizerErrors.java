package org.digijava.kernel.ampapi.endpoints.sync;

import org.digijava.kernel.ampapi.endpoints.errors.ApiErrorMessage;

import static org.digijava.kernel.ampapi.endpoints.errors.ApiError.ERROR_CLASS_SYNCHRONYZER_ID;

/**
 * @author Octavian Ciubotaru
 */
public class SynchronizerErrors {
    
    public static final ApiErrorMessage NO_USERS_ARE_SPECIFIED = new ApiErrorMessage(ERROR_CLASS_SYNCHRONYZER_ID, 0,
            "No users are specified");
    
    public static final ApiErrorMessage LAST_SYNC_TIME_REQUIRED = new ApiErrorMessage(ERROR_CLASS_SYNCHRONYZER_ID, 1,
            "Last sync time is required");
    
}
