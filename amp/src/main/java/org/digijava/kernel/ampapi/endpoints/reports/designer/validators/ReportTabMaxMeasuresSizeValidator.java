package org.digijava.kernel.ampapi.endpoints.reports.designer.validators;

import org.digijava.kernel.ampapi.endpoints.errors.ApiErrorMessage;
import org.digijava.kernel.ampapi.endpoints.reports.designer.ReportRequest;

import static org.digijava.kernel.ampapi.endpoints.reports.designer.ReportDesignerErrors.REPORT_MAX_MEASURES_SIZE_EXCEEDED;

/**
 * Validates that number of measures in a TAB doesn't exceed the limit of MAX_SIZE_MEASURES
 *
 * @author Viorel Chihai
 */
public class ReportTabMaxMeasuresSizeValidator implements ReportValidator {

    public static final int MAX_SIZE_MEASURES = 2;

    public boolean isValid(Object value) {
        ReportRequest reportRequest = (ReportRequest) value;

        if (reportRequest.isTab()) {
            return reportRequest.getMeasures().size() <= MAX_SIZE_MEASURES;
        }

        return true;
    }

    public ApiErrorMessage getErrorMessage() {
        return REPORT_MAX_MEASURES_SIZE_EXCEEDED.withDetails(String.valueOf(MAX_SIZE_MEASURES));

    }
}