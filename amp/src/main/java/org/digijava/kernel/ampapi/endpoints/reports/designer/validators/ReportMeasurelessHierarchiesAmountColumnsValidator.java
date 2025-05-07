package org.digijava.kernel.ampapi.endpoints.reports.designer.validators;

import org.digijava.kernel.ampapi.endpoints.errors.ApiErrorMessage;
import org.digijava.kernel.ampapi.endpoints.reports.designer.ReportColumnProvider;
import org.digijava.kernel.ampapi.endpoints.reports.designer.ReportRequest;

import java.util.List;
import java.util.stream.Collectors;

import static org.digijava.kernel.ampapi.endpoints.reports.designer.ReportDesignerErrors.REPORT_MEASURELESS_HIERARCHIES_AMOUNT_COLUMNS;

/**
 * Validates that measureless hierarchies that can be used without amount and MTEF columns
 *
 * @author Viorel Chihai
 */
public class ReportMeasurelessHierarchiesAmountColumnsValidator implements ReportValidator {

    private List<String> measurelessHierarchies;

    private final ReportColumnProvider columnProvider;

    public ReportMeasurelessHierarchiesAmountColumnsValidator(final ReportColumnProvider columnProvider) {
        this.columnProvider = columnProvider;
    }

    public boolean isValid(Object value) {
        ReportRequest reportRequest = (ReportRequest) value;

        boolean existAmountColumns = columnProvider.getAmountColumns()
                .stream()
                .filter(c -> reportRequest.getColumns().contains(c.getColumnId()))
                .findAny().isPresent();

        if (!reportRequest.getHierarchies().isEmpty() && existAmountColumns) {
            measurelessHierarchies = columnProvider.getMeasurelessColumns()
                    .stream().filter(c -> reportRequest.getHierarchies().contains(c.getColumnId()))
                    .map(c -> String.valueOf(c.getColumnId()))
                    .collect(Collectors.toList());

            return measurelessHierarchies.isEmpty();
        }

        return true;
    }

    public ApiErrorMessage getErrorMessage() {
        return REPORT_MEASURELESS_HIERARCHIES_AMOUNT_COLUMNS.withDetails(measurelessHierarchies);
    }
}