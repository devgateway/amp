package org.digijava.kernel.ampapi.endpoints.common;

import org.digijava.kernel.ampapi.endpoints.errors.ApiErrorMessage;

/**
 * @author Octavian Ciubotaru
 */
public class CommonErrors {

    public static final ApiErrorMessage INVALID_TIMESTAMP = new ApiErrorMessage(2, "Timestamp format is invalid.");
    
}
