package org.digijava.kernel.ampapi.endpoints.reports.designer.validators;

import org.apache.commons.lang3.StringUtils;
import org.digijava.kernel.ampapi.endpoints.errors.ApiErrorMessage;
import org.digijava.kernel.ampapi.endpoints.reports.designer.ReportRequest;

import static org.digijava.kernel.ampapi.endpoints.reports.designer.ReportDesignerErrors.REPORT_TYPE_REQUIRED;

/**
 * Validates that report type is valid
 *
 * @author Viorel Chihai
 */
public class ReportTypeValidator implements ReportValidator {

    private static final String REPORT_TYPE = "type";

    public boolean isValid(Object value) {
        ReportRequest reportRequest = (ReportRequest) value;

        return StringUtils.isNotBlank(reportRequest.getType());
    }

    public ApiErrorMessage getErrorMessage() {
        return REPORT_TYPE_REQUIRED.withDetails(REPORT_TYPE);
    }
}