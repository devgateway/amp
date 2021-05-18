package org.digijava.kernel.ampapi.endpoints.reports.designer;

import org.digijava.kernel.ampapi.endpoints.common.AMPTranslatorService;
import org.digijava.kernel.ampapi.endpoints.common.CategoryValueLabel;
import org.digijava.kernel.ampapi.endpoints.common.CategoryValueService;
import org.digijava.kernel.ampapi.endpoints.common.JsonApiResponse;
import org.digijava.kernel.ampapi.endpoints.reports.designer.builder.ReportDesignerBuilder;
import org.digijava.module.categorymanager.util.CategoryConstants;

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
                .withReportCategories(getReportCategories())
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

    private List<CategoryValueLabel> getReportCategories() {
        return CategoryValueService.getCategoryValues(CategoryConstants.REPORT_CATEGORY_KEY);
    }

    public Report getReport(final Long reportId) {
        return new ReportManager(reportColumnProvider, reportMeasureProvider)
                .getReport(reportId);
    }

    public JsonApiResponse<Report> createReport(final ReportRequest reportRequest) {
        return new ReportManager(reportColumnProvider, reportMeasureProvider)
                .createOrUpdateReport(reportRequest, null).getResult();
    }

    public JsonApiResponse<Report> updateReport(final ReportRequest reportRequest, final Long reportId) {
        return new ReportManager(reportColumnProvider, reportMeasureProvider)
                .createOrUpdateReport(reportRequest, reportId).getResult();
    }

}
