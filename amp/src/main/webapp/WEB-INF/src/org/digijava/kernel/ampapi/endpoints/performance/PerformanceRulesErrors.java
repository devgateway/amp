package org.digijava.kernel.ampapi.endpoints.performance;

import static org.digijava.kernel.ampapi.endpoints.errors.ApiError.ERROR_CLASS_PERFORMANCERULE_ID;

import org.digijava.kernel.ampapi.endpoints.errors.ApiErrorMessage;

/**
 * Defines errors used by Performance Rules API
 *
 * @author Viorel Chihai
 */
public final class PerformanceRulesErrors {
    
    public static final ApiErrorMessage RULE_INVALID = new ApiErrorMessage(ERROR_CLASS_PERFORMANCERULE_ID, 0,
            "Invalid Rule");
    
    public static final ApiErrorMessage CATEGORY_VALUE_INVALID = new ApiErrorMessage(ERROR_CLASS_PERFORMANCERULE_ID, 1,
            "Invalid Category Value");
    
    public static final ApiErrorMessage REQUIRED_ATTRIBUTE = new ApiErrorMessage(ERROR_CLASS_PERFORMANCERULE_ID, 2,
            "Required Attribute");
    
    public static final ApiErrorMessage RULE_TYPE_INVALID = new ApiErrorMessage(ERROR_CLASS_PERFORMANCERULE_ID, 3,
            "Invalid Rule Type");
    
    
    private PerformanceRulesErrors() {
    }
    
}
