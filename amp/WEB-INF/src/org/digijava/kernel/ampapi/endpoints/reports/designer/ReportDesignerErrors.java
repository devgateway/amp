package org.digijava.kernel.ampapi.endpoints.reports.designer;

import org.digijava.kernel.ampapi.endpoints.errors.ApiErrorMessage;

import static org.digijava.kernel.ampapi.endpoints.errors.ApiError.ERROR_CLASS_REPORT_DESIGNER_ID;

/**
 * Defines errors used by Reports Designer API.
 * Please define concrete errors, normally is an invalid request input. <br>
 * Anything that is out of control, should be re-thrown and it will be handled by API Exception Filter.
 *
 * @author Viorel Chihai
 */
public final class ReportDesignerErrors {

    private ReportDesignerErrors() {
    }

    public static final ApiErrorMessage REPORT_PROFILE_INVALID =
            new ApiErrorMessage(ERROR_CLASS_REPORT_DESIGNER_ID, 0, "Invalid report profile");
    
    public static final ApiErrorMessage REPORT_TYPE_INVALID =
            new ApiErrorMessage(ERROR_CLASS_REPORT_DESIGNER_ID, 1, "Invalid report type");

    public static final ApiErrorMessage REPORT_NOT_FOUND = new ApiErrorMessage(ERROR_CLASS_REPORT_DESIGNER_ID, 2,
            "Report not found");

}
