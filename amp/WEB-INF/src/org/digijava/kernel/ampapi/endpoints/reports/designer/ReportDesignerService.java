package org.digijava.kernel.ampapi.endpoints.reports.designer;

import org.digijava.kernel.ampapi.endpoints.common.AMPTranslatorService;
import org.digijava.kernel.ampapi.endpoints.reports.designer.builder.ReportDesignerBuilder;

import java.util.List;

/**
 * Service used by report designer
 *
 * @author Viorel Chihai
 */
public class ReportDesignerService {

    private final ReportOptionProvider reportOptionProvider;

    private final ReportColumnProvider reportColumnProvider;

    private final ReportMeasureProvider reportMeasureProvider;

    public ReportDesignerService() {
        this.reportOptionProvider = new ReportOptionProvider(AMPTranslatorService.INSTANCE);
        this.reportColumnProvider = new ReportColumnProvider(AMPTranslatorService.INSTANCE);
        this.reportMeasureProvider = new ReportMeasureProvider(AMPTranslatorService.INSTANCE);
    }

    public ReportDesigner getReportDesigner(final ReportProfile profile, final ReportType type) {
        return new ReportDesignerBuilder()
                .withProfile(profile)
                .withType(type)
                .withColumns(getColumns(profile, type))
                .withMeasures(getMeasures(type))
                .withOptions(getOptions(profile, type))
                .build();
    }

    private List<ReportColumn> getColumns(final ReportProfile profile, final ReportType type) {
        return reportColumnProvider.getColumns(profile, type);
    }

    private List<ReportMeasure> getMeasures(final ReportType type) {
        return reportMeasureProvider.getMeasures(type);
    }

    private List<ReportOption> getOptions(final ReportProfile profile, final ReportType type) {
        return reportOptionProvider.getOptions(profile, type);
    }

}
