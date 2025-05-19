package org.digijava.kernel.ampapi.endpoints.common;

import org.digijava.kernel.ampapi.endpoints.errors.ApiErrorMessage;

import static org.digijava.kernel.ampapi.endpoints.errors.ApiError.ERROR_CLASS_CONFIGURATION_ID;

/**
 * @author Octavian Ciubotaru
 */
public final class AmpConfigurationErrors {

    private AmpConfigurationErrors() {
    }

    public static final ApiErrorMessage INVALID_INPUT = new ApiErrorMessage(ERROR_CLASS_CONFIGURATION_ID, 0,
            "Invalid input");
}
