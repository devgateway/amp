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

    public static final ApiErrorMessage REPORT_NAME_REQUIRED = new ApiErrorMessage(ERROR_CLASS_REPORT_DESIGNER_ID, 3,
            "Report name required");

    public static final ApiErrorMessage REPORT_TYPE_REQUIRED = new ApiErrorMessage(ERROR_CLASS_REPORT_DESIGNER_ID, 4,
            "Report type required");

    public static final ApiErrorMessage REPORT_NAME_DUPLICATED = new ApiErrorMessage(ERROR_CLASS_REPORT_DESIGNER_ID, 5,
            "Report name is not unique");

    public static final ApiErrorMessage REPORT_SUMMARY_WITHOUT_HIERS_OR_MEASURES = new ApiErrorMessage(
            ERROR_CLASS_REPORT_DESIGNER_ID, 6, "Summary reports should have at least one measure or hierarchy");

    public static final ApiErrorMessage REPORT_MAX_HIERARCHY_SIZE_EXCEEDED = new ApiErrorMessage(
            ERROR_CLASS_REPORT_DESIGNER_ID, 7, "The maximum hierarchies size exceeded");

    public static final ApiErrorMessage REPORT_NON_SUMMARY_COLUMNS_HIERARCHIES = new ApiErrorMessage(
            ERROR_CLASS_REPORT_DESIGNER_ID, 8, "Only in summary reports all columns can be hierarchies");

    public static final ApiErrorMessage REPORT_MAX_MEASURES_SIZE_EXCEEDED = new ApiErrorMessage(
            ERROR_CLASS_REPORT_DESIGNER_ID, 9, "The maximum measures size exceeded for a tab");

    public static final ApiErrorMessage REPORT_MEASURELESS_HIERARCHIES = new ApiErrorMessage(
            ERROR_CLASS_REPORT_DESIGNER_ID, 10,
            "There are hierarchies that can be used only in reports without measures");

    public static final ApiErrorMessage REPORT_MEASURELESS_HIERARCHIES_AMOUNT_COLUMNS = new ApiErrorMessage(
            ERROR_CLASS_REPORT_DESIGNER_ID, 11,
            "There are hierarchies that can be used only in reports without amount and MTEF columns");

    public static final ApiErrorMessage REPORT_INVALID_COLUMNS = new ApiErrorMessage(
            ERROR_CLASS_REPORT_DESIGNER_ID, 12, "Invalid columns");

    public static final ApiErrorMessage REPORT_INVALID_MEASURES = new ApiErrorMessage(
            ERROR_CLASS_REPORT_DESIGNER_ID, 13, "Invalid measures");

    public static final ApiErrorMessage REPORT_INVALID_HIERARCHIES = new ApiErrorMessage(
            ERROR_CLASS_REPORT_DESIGNER_ID, 14, "Invalid hierarchies");

    public static final ApiErrorMessage REPORT_FIELD_REQUIRED = new ApiErrorMessage(ERROR_CLASS_REPORT_DESIGNER_ID, 15,
            "Field required");

}
