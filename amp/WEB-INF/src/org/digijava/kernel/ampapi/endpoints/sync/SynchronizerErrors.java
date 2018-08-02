package org.digijava.kernel.ampapi.endpoints.sync;

import org.digijava.kernel.ampapi.endpoints.errors.ApiErrorMessage;

/**
 * @author Octavian Ciubotaru
 */
public class SynchronizerErrors {

    public static final ApiErrorMessage NO_USERS_ARE_SPECIFIED = new ApiErrorMessage(1, "No users are specified");
    public static final ApiErrorMessage LAST_SYNC_TIME_REQUIRED = new ApiErrorMessage(2, "Last sync time is required");
    
}
