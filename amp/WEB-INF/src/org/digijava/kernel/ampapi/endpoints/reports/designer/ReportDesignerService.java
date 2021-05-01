package org.digijava.kernel.ampapi.endpoints.reports.designer;

import org.digijava.kernel.ampapi.endpoints.reports.designer.builder.ReportDesignerBuilder;

import java.util.ArrayList;
import java.util.List;

/**
 * Reports service to
 *
 * @author Viorel Chihai
 */
public class ReportDesignerService {

    public ReportDesigner getReportDesigner(final ReportProfile profile, final ReportType type) {
        return new ReportDesignerBuilder()
                .withProfile(profile)
                .withType(type)
                .withColumns(getColumns(profile, type))
                .withMeasures(getMeasures(profile, type))
                .withOptions(getOptions(profile, type))
                .build();
    }

    private List<ReportColumn> getColumns(final ReportProfile profile, final ReportType type) {
        return new ArrayList<>();
    }

    private List<ReportMeasure> getMeasures(final ReportProfile profile, final ReportType type) {
        return new ArrayList<>();
    }

    private List<ReportOption> getOptions(final ReportProfile profile, final ReportType type) {
        return new ArrayList<>();
    }

}
