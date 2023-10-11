package org.digijava.kernel.ampapi.endpoints.reports.designer.validators;

import org.digijava.kernel.ampapi.endpoints.errors.ApiErrorMessage;
import org.digijava.kernel.ampapi.endpoints.reports.designer.ReportColumn;
import org.digijava.kernel.ampapi.endpoints.reports.designer.ReportColumnProvider;
import org.digijava.kernel.ampapi.endpoints.reports.designer.ReportProfile;
import org.digijava.kernel.ampapi.endpoints.reports.designer.ReportRequest;
import org.digijava.kernel.ampapi.endpoints.reports.designer.ReportType;
import org.digijava.module.aim.dbentity.AmpColumns;

import java.util.ArrayList;
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
    private List<String> invalidColumnsNames;

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

        if (!invalidIds.isEmpty()) {
            invalidColumnsNames = new ArrayList<>();
            for (String id : invalidIds) {
                final int idNumber = Integer.parseInt(id);
                AmpColumns ampColumn = columnProvider.fetchAmpColumns().stream().filter(column -> column.getColumnId() == idNumber).collect(Collectors.toList()).get(0);
                String name = columnProvider.translatorService.translateText( ampColumn.getColumnName() );
                String errorText = " -> [" + id + "] " + name;
                invalidColumnsNames.add(errorText);
            }
        }

        return invalidIds.isEmpty();
    }

    public ApiErrorMessage getErrorMessage() {
        if (isEmpty) {
            return REPORT_FIELD_REQUIRED.withDetails(COLUMNS);
        }

        //return REPORT_INVALID_COLUMNS.withDetails(invalidIds);
        return REPORT_INVALID_COLUMNS.withDetails(invalidColumnsNames);
    }

}
