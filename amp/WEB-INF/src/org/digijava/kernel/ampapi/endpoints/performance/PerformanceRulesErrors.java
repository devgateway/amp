/**
 * 
 */
package org.digijava.kernel.ampapi.endpoints.performance;

import org.digijava.kernel.ampapi.endpoints.errors.ApiErrorMessage;

/**
 * Defines errors used by Performance Rules API
 * 
 * @author Viorel Chihai
 */
public final class PerformanceRulesErrors {

    public static final ApiErrorMessage RULE_INVALID = new ApiErrorMessage(1, "Invalid Rule");
    public static final ApiErrorMessage CATEGORY_VALUE_INVALID = new ApiErrorMessage(2, "Invalid Category Value");
    public static final ApiErrorMessage REQUIRED_ATTRIBUTE = new ApiErrorMessage(3, "Required Attribute");
    public static final ApiErrorMessage RULE_TYPE_INVALID = new ApiErrorMessage(4, "Invalid Rule Type");

    private PerformanceRulesErrors() {
    }

}
