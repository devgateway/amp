package org.digijava.kernel.ampapi.endpoints.reports.designer.validators;

import org.digijava.kernel.ampapi.endpoints.errors.ApiErrorMessage;
import org.digijava.kernel.ampapi.endpoints.reports.designer.ReportRequest;

import static org.digijava.kernel.ampapi.endpoints.reports.designer.ReportDesignerErrors.REPORT_MAX_HIERARCHY_SIZE_EXCEEDED;

/**
 * Validates that number of hierarchies doesn't exceed the limit of MAX_SIZE_HIERARCHIES
 *
 * @author Viorel Chihai
 */
public class ReportMaxHierarchiesSizeValidator implements ReportValidator {

    public static final int MAX_SIZE_HIERARCHIES = 3;

    public boolean isValid(Object value) {
        ReportRequest reportRequest = (ReportRequest) value;

        return reportRequest.getHierarchies().size() <= MAX_SIZE_HIERARCHIES;
    }

    public ApiErrorMessage getErrorMessage() {
        return REPORT_MAX_HIERARCHY_SIZE_EXCEEDED.withDetails(String.valueOf(MAX_SIZE_HIERARCHIES));

    }
}