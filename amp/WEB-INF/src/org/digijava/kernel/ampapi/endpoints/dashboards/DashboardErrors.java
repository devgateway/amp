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
    public static final ApiErrorMessage INVALID_ID = new ApiErrorMessage(1, "Invalid ID");
    public static final ApiErrorMessage INVALID_THRESHOLD = new ApiErrorMessage(2, "Invalid Threshold");
    public static final ApiErrorMessage DUPLICATE_THRESHOLDS = new ApiErrorMessage(3, "Duplicate Thresholds");
    public static final ApiErrorMessage INVALID_COLUMN = new ApiErrorMessage(5, "Invalid Column");
}
