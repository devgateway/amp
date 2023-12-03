package org.digijava.kernel.ampapi.endpoints.reports.designer.validators;

import org.digijava.kernel.ampapi.endpoints.errors.ApiErrorMessage;
import org.digijava.kernel.ampapi.endpoints.reports.designer.ReportRequest;

import static org.digijava.kernel.ampapi.endpoints.reports.designer.ReportDesignerErrors.REPORT_SUMMARY_WITHOUT_HIERS_OR_MEASURES;

/**
 * Validates that summary reports should contain at least one hierarchy or measure
 *
 * @author Viorel Chihai
 */
public class ReportSummaryValidator implements ReportValidator {

    public boolean isValid(Object value) {
        ReportRequest reportRequest = (ReportRequest) value;

        if (reportRequest.isSummary()) {
            return !(reportRequest.getHierarchies().isEmpty() && reportRequest.getMeasures().isEmpty());
        }

        return true;
    }

    public ApiErrorMessage getErrorMessage() {
        return REPORT_SUMMARY_WITHOUT_HIERS_OR_MEASURES;
    }
}