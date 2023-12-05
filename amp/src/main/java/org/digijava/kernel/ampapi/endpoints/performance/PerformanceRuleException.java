package org.digijava.kernel.ampapi.endpoints.performance;

import org.digijava.kernel.ampapi.endpoints.errors.ApiError;
import org.digijava.kernel.ampapi.endpoints.errors.ApiErrorMessage;
import org.digijava.kernel.ampapi.endpoints.errors.ApiRuntimeException;

public class PerformanceRuleException extends ApiRuntimeException {

    public PerformanceRuleException(String message) {
        super(ApiError.toError(message));
    }

    public PerformanceRuleException(ApiErrorMessage errorMessage) {
        super(ApiError.toError(errorMessage));
    }

    public PerformanceRuleException(ApiErrorMessage errorMessage, String details) {
        super(ApiError.toError(errorMessage.withDetails(details)));
    }
}
