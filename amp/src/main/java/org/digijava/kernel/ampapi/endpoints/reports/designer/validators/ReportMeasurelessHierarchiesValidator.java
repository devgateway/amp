package org.digijava.kernel.ampapi.endpoints.reports.designer.validators;

import org.digijava.kernel.ampapi.endpoints.errors.ApiErrorMessage;
import org.digijava.kernel.ampapi.endpoints.reports.designer.ReportColumnProvider;
import org.digijava.kernel.ampapi.endpoints.reports.designer.ReportRequest;

import java.util.List;
import java.util.stream.Collectors;

import static org.digijava.kernel.ampapi.endpoints.reports.designer.ReportDesignerErrors.REPORT_MEASURELESS_HIERARCHIES;

/**
 * Validates that the report doesn't contain measureless hierarchies and amount/MTEF columns
 *
 * @author Viorel Chihai
 */
public class ReportMeasurelessHierarchiesValidator implements ReportValidator {

    private List<String> measurelessHierarchies;

    private final ReportColumnProvider columnProvider;

    public ReportMeasurelessHierarchiesValidator(final ReportColumnProvider columnProvider) {
        this.columnProvider = columnProvider;
    }

    public boolean isValid(Object value) {
        ReportRequest reportRequest = (ReportRequest) value;

        if (!reportRequest.getMeasures().isEmpty() && !reportRequest.getHierarchies().isEmpty()) {

            measurelessHierarchies = columnProvider.getMeasurelessColumns()
                    .stream().filter(c -> reportRequest.getHierarchies().contains(c.getColumnId()))
                    .map(c -> String.valueOf(c.getColumnId()))
                    .collect(Collectors.toList());

            return measurelessHierarchies.isEmpty();
        }

        return true;
    }

    public ApiErrorMessage getErrorMessage() {
        return REPORT_MEASURELESS_HIERARCHIES.withDetails(measurelessHierarchies);
    }
}