package org.digijava.kernel.ampapi.endpoints.reports.designer.validators;

import org.digijava.kernel.ampapi.endpoints.errors.ApiErrorMessage;
import org.digijava.kernel.ampapi.endpoints.reports.designer.ReportColumn;
import org.digijava.kernel.ampapi.endpoints.reports.designer.ReportColumnProvider;
import org.digijava.kernel.ampapi.endpoints.reports.designer.ReportProfile;
import org.digijava.kernel.ampapi.endpoints.reports.designer.ReportRequest;
import org.digijava.kernel.ampapi.endpoints.reports.designer.ReportType;

import java.util.List;
import java.util.stream.Collectors;

import static org.digijava.kernel.ampapi.endpoints.reports.designer.ReportDesignerErrors.REPORT_FIELD_REQUIRED;
import static org.digijava.kernel.ampapi.endpoints.reports.designer.ReportDesignerErrors.REPORT_INVALID_COLUMNS;
import static org.digijava.kernel.ampapi.endpoints.reports.designer.ReportProfile.REPORT;
import static org.digijava.kernel.ampapi.endpoints.reports.designer.ReportProfile.TAB;

/**
 * Validates that column ids are valid in report
 *
 * @author Viorel Chihai
 */
public class ReportColumnValidator implements ReportValidator {

    public static final String COLUMNS = "columns";

    private final ReportColumnProvider columnProvider;

    private boolean isEmpty;

    private List<String> invalidIds;

    public ReportColumnValidator(final ReportColumnProvider columnProvider) {
        this.columnProvider = columnProvider;
    }

    public boolean isValid(Object value) {
        ReportRequest reportRequest = (ReportRequest) value;

        if (reportRequest.getColumns().isEmpty()) {
            isEmpty = true;
            return false;
        }

        ReportProfile profile = reportRequest.isTab() ? TAB : REPORT;
        ReportType type = ReportType.fromString(reportRequest.getType());

        List<Long> columnIds = columnProvider.getColumns(profile, type)
                .stream().map(ReportColumn::getId)
                .collect(Collectors.toList());

        invalidIds = reportRequest.getColumns().stream().filter(id -> !columnIds.contains(id))
                .map(String::valueOf)
                .collect(Collectors.toList());

        return invalidIds.isEmpty();
    }

    public ApiErrorMessage getErrorMessage() {
        if (isEmpty) {
            return REPORT_FIELD_REQUIRED.withDetails(COLUMNS);
        }

        return REPORT_INVALID_COLUMNS.withDetails(invalidIds);
    }

}
