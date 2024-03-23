package org.digijava.kernel.ampapi.endpoints.common;

import org.digijava.kernel.ampapi.endpoints.errors.ApiErrorMessage;

import static org.digijava.kernel.ampapi.endpoints.errors.ApiError.ERROR_CLASS_COMMON_ID;

/**
 * @author Octavian Ciubotaru
 */
public class CommonErrors {

    public static final ApiErrorMessage INVALID_TIMESTAMP = new ApiErrorMessage(ERROR_CLASS_COMMON_ID, 0,
            "Timestamp format is invalid.");
    
}
