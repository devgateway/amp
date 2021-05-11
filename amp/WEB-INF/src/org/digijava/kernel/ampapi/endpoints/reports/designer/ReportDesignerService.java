package org.digijava.kernel.ampapi.endpoints.reports.designer;

import org.dgfoundation.amp.reports.converters.AmpARFilterConverter;
import org.digijava.kernel.ampapi.endpoints.common.AMPTranslatorService;
import org.digijava.kernel.ampapi.endpoints.common.CategoryValueLabel;
import org.digijava.kernel.ampapi.endpoints.common.CategoryValueService;
import org.digijava.kernel.ampapi.endpoints.errors.ApiErrorResponseService;
import org.digijava.kernel.ampapi.endpoints.reports.designer.builder.ReportDesignerBuilder;
import org.digijava.kernel.persistence.PersistenceManager;
import org.digijava.module.aim.dbentity.AmpReports;
import org.digijava.module.categorymanager.util.CategoryConstants;

import java.util.List;

import static org.digijava.kernel.ampapi.endpoints.reports.designer.ReportDesignerErrors.REPORT_NOT_FOUND;
import static org.digijava.kernel.ampapi.endpoints.settings.SettingsUtils.getReportSettings;
import static org.digijava.module.aim.ar.util.FilterUtil.buildFilterFromSource;

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
        AmpReports ampReport = (AmpReports) PersistenceManager.getSession().get(AmpReports.class, reportId);

        if (ampReport == null) {
            ApiErrorResponseService.reportResourceNotFound(REPORT_NOT_FOUND.withDetails(String.valueOf(reportId)));
        }

        Report report = new Report();
        report.setId(ampReport.getAmpReportId());
        report.setName(ampReport.getName());
        report.setDescription(ampReport.getReportDescription());
        report.setType(ReportType.fromLong(ampReport.getType()));
        report.setGroupingOption(ampReport.getOptions());
        report.setSummary(ampReport.getHideActivities());
        report.setTab(ampReport.isTab());
        report.setPublicView(ampReport.getPublicReport());
        report.setWorkspaceLinked(ampReport.getWorkspaceLinked());
        report.setAlsoShowPledges(ampReport.getAlsoShowPledges());
        report.setShowOriginalCurrency(ampReport.getShowOriginalCurrency());
        report.setAllowEmptyFundingColumns(ampReport.getAllowEmptyFundingColumns());
        report.setSplitByFunding(ampReport.getSplitByFunding());
        report.setOwnerId(ampReport.getOwnerId());

        if (ampReport.getReportCategory() != null) {
            report.setReportCategory(ampReport.getReportCategory().getId());
        }

        report.setMeasures(ampReport.getMeasures());
        report.setColumns(ampReport.getColumns());
        report.setHierarchies(ampReport.getHierarchies());

        AmpARFilterConverter arFilterConverter = new AmpARFilterConverter(buildFilterFromSource(ampReport));
        report.setSettings(getReportSettings(arFilterConverter.buildSettings()));
        report.setFilters(arFilterConverter.buildFilters());

        return report;
    }
}
