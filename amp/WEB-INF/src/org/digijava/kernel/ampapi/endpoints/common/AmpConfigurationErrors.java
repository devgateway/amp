package org.digijava.kernel.ampapi.endpoints.common;

import org.digijava.kernel.ampapi.endpoints.errors.ApiErrorMessage;

/**
 * @author Octavian Ciubotaru
 */
public final class AmpConfigurationErrors {

    private AmpConfigurationErrors() {
    }

    public static final ApiErrorMessage INVALID_INPUT = new ApiErrorMessage(10, 0, "Invalid input");
}
