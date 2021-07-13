package org.digijava.kernel.ampapi.endpoints.reports.designer.validators;

import org.digijava.kernel.ampapi.endpoints.errors.ApiErrorMessage;
import org.digijava.kernel.ampapi.endpoints.reports.designer.ReportColumn;
import org.digijava.kernel.ampapi.endpoints.reports.designer.ReportColumnProvider;
import org.digijava.kernel.ampapi.endpoints.reports.designer.ReportProfile;
import org.digijava.kernel.ampapi.endpoints.reports.designer.ReportRequest;
import org.digijava.kernel.ampapi.endpoints.reports.designer.ReportType;

import java.util.List;
import java.util.stream.Collectors;

import static org.digijava.kernel.ampapi.endpoints.reports.designer.ReportDesignerErrors.REPORT_INVALID_HIERARCHIES;
import static org.digijava.kernel.ampapi.endpoints.reports.designer.ReportProfile.REPORT;
import static org.digijava.kernel.ampapi.endpoints.reports.designer.ReportProfile.TAB;

/**
 * Validates that hierarchy ids are valid in report
 *
 * @author Viorel Chihai
 */
public class ReportHierarchyValidator implements ReportValidator {

    public static final String HIERARCHIES = "hierarchies";

    private final ReportColumnProvider columnProvider;

    private List<String> invalidIds;

    public ReportHierarchyValidator(final ReportColumnProvider columnProvider) {
        this.columnProvider = columnProvider;
    }

    public boolean isValid(Object value) {
        ReportRequest reportRequest = (ReportRequest) value;

        if (reportRequest.getHierarchies().isEmpty()) {
            return true;
        }

        if (reportRequest.getColumns().isEmpty()) {
            return false;
        }

        ReportProfile profile = reportRequest.isTab() ? TAB : REPORT;
        ReportType type = ReportType.fromString(reportRequest.getType());

        List<Long> columnIds = columnProvider.getColumns(profile, type)
                .stream().map(ReportColumn::getId)
                .collect(Collectors.toList());

        invalidIds = reportRequest.getHierarchies().stream()
                .filter(id -> !columnIds.contains(id) || !reportRequest.getColumns().contains(id))
                .map(String::valueOf)
                .collect(Collectors.toList());

        return invalidIds.isEmpty();
    }

    public ApiErrorMessage getErrorMessage() {
        return REPORT_INVALID_HIERARCHIES.withDetails(invalidIds);
    }

}