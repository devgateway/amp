package org.digijava.kernel.ampapi.endpoints.reports.designer.validators;

import org.apache.commons.lang3.StringUtils;
import org.digijava.kernel.ampapi.endpoints.errors.ApiErrorMessage;
import org.digijava.kernel.ampapi.endpoints.reports.designer.ReportRequest;

import static org.digijava.kernel.ampapi.endpoints.reports.designer.ReportDesignerErrors.REPORT_NAME_REQUIRED;

/**
 * Validates that report name is not blank
 *
 * @author Viorel Chihai
 */
public class ReportNameValidator implements ReportValidator {

    public static final String REPORT_NAME = "name";

    public boolean isValid(Object value) {
        ReportRequest reportRequest = (ReportRequest) value;
        return StringUtils.isNotBlank(reportRequest.getName().getOrBuildText());
    }

    public ApiErrorMessage getErrorMessage() {
        return REPORT_NAME_REQUIRED.withDetails(REPORT_NAME);
    }
}