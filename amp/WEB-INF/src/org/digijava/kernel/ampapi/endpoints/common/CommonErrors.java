package org.digijava.kernel.ampapi.endpoints.common;

import org.digijava.kernel.ampapi.endpoints.errors.ApiError;
import org.digijava.kernel.ampapi.endpoints.errors.ApiErrorMessage;

/**
 * @author Octavian Ciubotaru
 */
public class CommonErrors {

    public static final ApiErrorMessage UNKOWN_ERROR = new ApiErrorMessage(
            ApiError.GENERIC_UNHANDLED_ERROR_CODE, "Unknown Error", true);
    public static final ApiErrorMessage INVALID_TIMESTAMP = new ApiErrorMessage(2, "Timestamp format is invalid.");

}
