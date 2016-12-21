/**
 * 
 */
package org.digijava.kernel.ampapi.endpoints.reports;

import org.digijava.kernel.ampapi.endpoints.errors.ApiErrorMessage;

/**
 * Defines errors used by Reports API. Please define concrete errors, normally is an invalid request input. <br>
 * Anything that is out of control, should be re-thrown and it will be handled by API Exception Filter.
 * 
 * @author vlimansky
 */
public class ReportErrors {

	// Validation errors
	public static final ApiErrorMessage REPORT_NAME_REQUIRED = new ApiErrorMessage(1, "Report name not specified");
    public static final ApiErrorMessage LIST_NAME_REQUIRED = new ApiErrorMessage(2, "Not specified: ");
    public static final ApiErrorMessage LIST_INVALID = new ApiErrorMessage(3, "Not enabled / invalid: ");
    public static final ApiErrorMessage REPORT_TYPE_INVALID = new ApiErrorMessage(4, "Invalid report type: ");
    public static final ApiErrorMessage REPORT_TYPE_REQUIRED = new ApiErrorMessage(5, "Project type is required");
    public static final ApiErrorMessage ACTIVITY_TYPE_LIST_INVALID = new ApiErrorMessage(6, "Invalid list of activity types: ");
    public static final ApiErrorMessage REPORT_NOT_FOUND = new ApiErrorMessage(7, "Report not found");
}