package org.digijava.kernel.ampapi.endpoints.reports.designer.validators;

import org.digijava.kernel.ampapi.endpoints.errors.ApiErrorMessage;
import org.digijava.kernel.ampapi.endpoints.reports.designer.ReportRequest;

import static org.digijava.kernel.ampapi.endpoints.reports.designer.ReportDesignerErrors.REPORT_NON_SUMMARY_COLUMNS_HIERARCHIES;

/**
 * Validates that all columns can be hierarchies in summary reports only
 *
 * @author Viorel Chihai
 */
public class ReportNonSummaryColumnsHierarchiesValidator implements ReportValidator {

    public boolean isValid(Object value) {
        ReportRequest reportRequest = (ReportRequest) value;

        if (!reportRequest.isSummary()) {
            return reportRequest.getColumns().isEmpty()
                    || reportRequest.getColumns().size() != reportRequest.getHierarchies().size();
        }

        return true;
    }

    public ApiErrorMessage getErrorMessage() {
        return REPORT_NON_SUMMARY_COLUMNS_HIERARCHIES;
    }
}