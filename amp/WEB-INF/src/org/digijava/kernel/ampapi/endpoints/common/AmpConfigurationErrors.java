package org.digijava.kernel.ampapi.endpoints.common;

import static org.digijava.kernel.ampapi.endpoints.errors.ApiError.ERROR_CLASS_CONFIGURATION_ID;

import org.digijava.kernel.ampapi.endpoints.errors.ApiErrorMessage;

/**
 * @author Octavian Ciubotaru
 */
public final class AmpConfigurationErrors {

    private AmpConfigurationErrors() {
    }

    public static final ApiErrorMessage INVALID_INPUT = new ApiErrorMessage(ERROR_CLASS_CONFIGURATION_ID, 0,
            "Invalid input");
}
