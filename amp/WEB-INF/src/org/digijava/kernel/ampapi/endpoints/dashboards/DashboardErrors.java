package org.digijava.kernel.ampapi.endpoints.dashboards;

import static org.digijava.kernel.ampapi.endpoints.errors.ApiError.ERROR_CLASS_DASHBOARD_ID;

import org.digijava.kernel.ampapi.endpoints.errors.ApiErrorMessage;

/**
 * Dashboard Errors
 *
 * @author Nadejda Mandrescu
 */
public class DashboardErrors {
    
    public static final ApiErrorMessage INVALID_THRESHOLD = new ApiErrorMessage(ERROR_CLASS_DASHBOARD_ID, 0,
            "Invalid Threshold");
    
    public static final ApiErrorMessage DUPLICATE_THRESHOLDS = new ApiErrorMessage(ERROR_CLASS_DASHBOARD_ID, 1,
            "Duplicate Thresholds");
    
    public static final ApiErrorMessage INVALID_COLUMN = new ApiErrorMessage(ERROR_CLASS_DASHBOARD_ID, 2,
            "Invalid Column");
}