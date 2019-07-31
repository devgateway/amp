/**
 *
 */
package org.digijava.kernel.ampapi.endpoints.dashboards;

import org.digijava.kernel.ampapi.endpoints.errors.ApiErrorMessage;

/**
 * Dashboard Errors
 *
 * @author Nadejda Mandrescu
 */
public class DashboardErrors {
    
    public static final ApiErrorMessage INVALID_THRESHOLD = new ApiErrorMessage(8, 0, "Invalid Threshold");
    
    public static final ApiErrorMessage DUPLICATE_THRESHOLDS = new ApiErrorMessage(8, 1, "Duplicate Thresholds");
    
    public static final ApiErrorMessage INVALID_COLUMN = new ApiErrorMessage(8, 2, "Invalid Column");
}