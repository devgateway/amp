package org.digijava.kernel.ampapi.endpoints.geocoding;

import static org.digijava.kernel.ampapi.endpoints.errors.ApiError.ERROR_CLASS_GEO_CODER_ID;

import org.digijava.kernel.ampapi.endpoints.errors.ApiErrorMessage;

/**
 * @author Octavian Ciubotaru
 */
public final class GeoCoderEndpointErrors {

    private GeoCoderEndpointErrors() {
    }

    public static final ApiErrorMessage GEO_CODING_NOT_AVAILABLE = new ApiErrorMessage(ERROR_CLASS_GEO_CODER_ID, 0,
            "Invalid URL value");
    public static final ApiErrorMessage GEO_CODING_GENERAL_ERROR = new ApiErrorMessage(ERROR_CLASS_GEO_CODER_ID, 1,
            "General error");

}
