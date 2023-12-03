package org.digijava.kernel.ampapi.endpoints.reports.designer.validators;

import org.digijava.kernel.ampapi.endpoints.errors.ApiErrorMessage;
import org.digijava.kernel.ampapi.endpoints.reports.designer.ReportMeasure;
import org.digijava.kernel.ampapi.endpoints.reports.designer.ReportMeasureProvider;
import org.digijava.kernel.ampapi.endpoints.reports.designer.ReportRequest;
import org.digijava.kernel.ampapi.endpoints.reports.designer.ReportType;

import java.util.List;
import java.util.stream.Collectors;

import static org.digijava.kernel.ampapi.endpoints.reports.designer.ReportDesignerErrors.REPORT_INVALID_MEASURES;

/**
 * Validates that measure ids are valid in report
 *
 * @author Viorel Chihai
 */
public class ReportMeasureValidator implements ReportValidator {

    public static final String MEASURES = "measures";

    private final ReportMeasureProvider measureProvider;

    private boolean isEmpty;

    private List<String> invalidIds;

    public ReportMeasureValidator(final ReportMeasureProvider measureProvider) {
        this.measureProvider = measureProvider;
    }

    public boolean isValid(Object value) {
        ReportRequest reportRequest = (ReportRequest) value;

        if (reportRequest.getMeasures().isEmpty()) {
            return true;
        }

        ReportType type = ReportType.fromString(reportRequest.getType());
        List<Long> columnIds = measureProvider.getMeasures(type)
                .stream().map(ReportMeasure::getId)
                .collect(Collectors.toList());

        invalidIds = reportRequest.getMeasures().stream().filter(id -> !columnIds.contains(id))
                .map(String::valueOf)
                .collect(Collectors.toList());

        return invalidIds.isEmpty();
    }

    public ApiErrorMessage getErrorMessage() {
        return REPORT_INVALID_MEASURES.withDetails(invalidIds);
    }

}