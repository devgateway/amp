package org.digijava.kernel.ampapi.endpoints.reports.designer;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.dgfoundation.amp.ar.AmpARFilter;
import org.dgfoundation.amp.ar.dbentity.AmpFilterData;
import org.dgfoundation.amp.newreports.AmpReportFilters;
import org.dgfoundation.amp.reports.converters.AmpARFilterConverter;
import org.dgfoundation.amp.reports.converters.AmpReportFiltersConverter;
import org.digijava.kernel.ampapi.endpoints.common.JsonApiResponse;
import org.digijava.kernel.ampapi.endpoints.errors.ApiError;
import org.digijava.kernel.ampapi.endpoints.errors.ApiErrorMessage;
import org.digijava.kernel.ampapi.endpoints.errors.ApiErrorResponseService;
import org.digijava.kernel.ampapi.endpoints.errors.GenericErrors;
import org.digijava.kernel.ampapi.endpoints.settings.SettingsConstants;
import org.digijava.kernel.ampapi.endpoints.util.FilterUtils;
import org.digijava.kernel.persistence.PersistenceManager;
import org.digijava.module.aim.dbentity.AmpColumns;
import org.digijava.module.aim.dbentity.AmpMeasures;
import org.digijava.module.aim.dbentity.AmpReportColumn;
import org.digijava.module.aim.dbentity.AmpReportHierarchy;
import org.digijava.module.aim.dbentity.AmpReportMeasures;
import org.digijava.module.aim.dbentity.AmpReports;
import org.digijava.module.aim.helper.TeamMember;
import org.digijava.module.aim.util.AdvancedReportUtil;
import org.digijava.module.aim.util.CurrencyUtil;
import org.digijava.module.aim.util.FeaturesUtil;
import org.digijava.module.aim.util.FiscalCalendarUtil;
import org.digijava.module.aim.util.TeamUtil;
import org.digijava.module.categorymanager.dbentity.AmpCategoryValue;
import org.digijava.module.categorymanager.util.CategoryManagerUtil;
import org.jetbrains.annotations.Nullable;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import static java.lang.Boolean.TRUE;
import static org.dgfoundation.amp.ar.ColumnConstants.PROJECT_TITLE;
import static org.digijava.kernel.ampapi.endpoints.reports.designer.ReportDesignerErrors.REPORT_NOT_FOUND;
import static org.digijava.kernel.ampapi.endpoints.settings.SettingsUtils.getReportSettings;
import static org.digijava.module.aim.ar.util.FilterUtil.buildFilterFromSource;
import static org.digijava.module.aim.helper.GlobalSettingsConstants.PROJECT_TITLE_HIRARCHY;
import static org.digijava.module.categorymanager.util.CategoryConstants.ACTIVITY_LEVEL_KEY;

/**
 * @author Viorel Chihai
 */
public class ReportManager {

    public static final String REPORT = "report";

    public static final String REPORT_NAME = "name";
    public static final String REPORT_TYPE = "type";

    private static final Logger logger = Logger.getLogger(ReportManager.class);

    protected Map<Integer, ApiErrorMessage> errors = new HashMap<>();

    private ReportRequest reportRequest;

    private AmpReports report;

    private final ReportColumnProvider columnProvider;
    private final ReportMeasureProvider measureProvider;

    public ReportManager(final ReportColumnProvider columnProvider, final ReportMeasureProvider measureProvider) {
        this.columnProvider = columnProvider;
        this.measureProvider = measureProvider;
    }

    public ReportManager createOrUpdateReport(ReportRequest reportRequest, final Long reportId) {
        this.reportRequest = reportRequest;
        this.report = reportId == null ? new AmpReports() : getReportById(reportId);

        validateReportRequestFields();

        if (errors.isEmpty()) {
            convertReportRequestToAmpReport();
            persistReport();
        }

        return this;
    }

    private void persistReport() {
        TeamMember tm = TeamUtil.getCurrentMember();
        AdvancedReportUtil.saveReport(report, tm.getTeamId(), tm.getMemberId(), tm.getTeamHead());
        logger.info("The report was saved with id = " + report.getAmpReportId());
    }

    private void validateReportRequestFields() {
        if (StringUtils.isBlank(reportRequest.getName())) {
            addError(ReportDesignerErrors.REPORT_NAME_REQUIRED.withDetails(REPORT_NAME));
        } else if (isReportNameDuplicated()) {
            addError(ReportDesignerErrors.REPORT_NAME_DUPLICATED.withDetails(reportRequest.getName()));
        }

        if (StringUtils.isBlank(reportRequest.getType())) {
            addError(ReportDesignerErrors.REPORT_TYPE_REQUIRED.withDetails(REPORT_TYPE));
        }
    }

    private boolean isReportNameDuplicated() {
        boolean nameIsUpdated = report.getAmpReportId() == null ? true
                : !StringUtils.equalsIgnoreCase(report.getName(), reportRequest.getName());

        if (nameIsUpdated) {
            String queryStr = "select r FROM " + AmpReports.class.getName() + " r where "
                    + AmpReports.hqlStringForName("r") + "=:reportName";
            List<AmpReports> conflicts = PersistenceManager.getSession()
                    .createQuery(queryStr)
                    .setString("reportName", reportRequest.getName())
                    .list();

            return conflicts.stream()
                    .filter(r -> report.getAmpReportId() == null || !report.getAmpReportId().equals(r.getAmpReportId()))
                    .findAny().isPresent();
        }

        return false;
    }


    private void convertReportRequestToAmpReport() {
        report.setName(reportRequest.getName());

        // Set the report type and profile only for the new reports
        if (report.getId() == null) {
            report.setType(ReportType.fromString(reportRequest.getType()).getId());
            report.setDrilldownTab(reportRequest.getTab());
        }

        if (reportRequest.getPublicView()) {
            if (report.getPublicReport() == null || (!report.getPublicReport())) {
                report.setPublishedDate(new Date(System.currentTimeMillis()));
            }
        }

        report.setUpdatedDate(new Date(System.currentTimeMillis()));
        report.setWorkspaceLinked(reportRequest.getWorkspaceLinked());
        report.setAlsoShowPledges(reportRequest.getAlsoShowPledges());
        report.setShowOriginalCurrency(reportRequest.getShowOriginalCurrency());
        report.setOwnerId(reportRequest.getOwnerId() == null ? TeamUtil.getCurrentAmpTeamMember()
                : reportRequest.getOwnerId());
        report.setHideActivities(reportRequest.getSummary());
        report.setOptions(reportRequest.getGroupingOption());
        report.setReportDescription(reportRequest.getDescription());
        report.setPublicReport(reportRequest.getPublicView());
        report.setAllowEmptyFundingColumns(reportRequest.getAllowEmptyFundingColumns());
        report.setSplitByFunding(reportRequest.getSplitByFunding());

        if (reportRequest.getReportCategory() != null && reportRequest.getReportCategory() != 0) {
            report.setReportCategory(CategoryManagerUtil.getAmpCategoryValueFromDb(reportRequest.getReportCategory()));
        }

        report.setColumns(getReportColumns(reportRequest.getColumns()));
        report.setHierarchies(getReportHierarchies(reportRequest.getHierarchies()));
        report.setMeasures(getReportMeasures(reportRequest.getMeasures()));

        /* If all columns are set as hierarchies we add the Project Title column */
        addOrRemoveProjectTitleAsHierarchy();

        if (report.getAmpReportId() != null) {
            AmpFilterData.deleteOldFilterData(report.getAmpReportId());
        }

        Map<String, Object> filterMap = reportRequest.getFilters();
        if (filterMap != null) {
            AmpReportFilters reportFilters = FilterUtils.getFilters(filterMap, new AmpReportFilters());

            // Transform back to legacy AmpARFilters.
            AmpReportFiltersConverter converter = new AmpReportFiltersConverter(reportFilters);
            AmpARFilter filter = converter.buildFilters();
            if (filter != null) {
                if (reportRequest.isIncludeLocationChildren() != null) {
                    filter.setIncludeLocationChildren(reportRequest.isIncludeLocationChildren());
                }

                if (reportRequest.getSettings() != null) {
                    String currency = reportRequest.getSettings().get(SettingsConstants.CURRENCY_ID).toString();
                    String calendar = reportRequest.getSettings().get(SettingsConstants.CALENDAR_TYPE_ID).toString();
                    filter.setCurrency(CurrencyUtil.getAmpcurrency(currency));
                    filter.setCalendarType(FiscalCalendarUtil.getAmpFiscalCalendar(new Long(calendar)));
                    if (reportRequest.getSettings() != null
                            && reportRequest.getSettings().get(SettingsConstants.YEAR_RANGE_ID) != null) {
                        Map<String, Object> yearRange = (Map<String, Object>)
                                reportRequest.getSettings().get(SettingsConstants.YEAR_RANGE_ID);
                        if (yearRange.get(SettingsConstants.YEAR_FROM) != null) {
                            filter.setRenderStartYear(
                                    Integer.valueOf((String) yearRange.get(SettingsConstants.YEAR_FROM)));
                        }
                        if (yearRange.get(SettingsConstants.YEAR_TO) != null) {
                            filter.setRenderEndYear(
                                    Integer.valueOf((String) yearRange.get(SettingsConstants.YEAR_TO)));
                        }
                    }
                }
                Set<AmpFilterData> fdSet = AmpFilterData.createFilterDataSet(report, filter);

                if (report.getFilterDataSet() == null) {
                    report.setFilterDataSet(fdSet);
                } else {
                    report.getFilterDataSet().clear();
                    report.getFilterDataSet().addAll(fdSet);
                }
            }
        }
    }

    private void addOrRemoveProjectTitleAsHierarchy() {
        if (!report.getHierarchies().isEmpty() && !report.getColumns().isEmpty()) {
            if (report.getColumnNames().equals(report.getHierarchyNames())
                    && !TRUE.equals(report.getHideActivities())) {
                if (report.getColumnNames().contains(PROJECT_TITLE)) {
                    if (!FeaturesUtil.getGlobalSettingValueBoolean(PROJECT_TITLE_HIRARCHY)) {
                            report.getHierarchies().removeIf(h -> h.getColumn().getColumnName().equals(PROJECT_TITLE));
                    }
                } else {
                    ReportColumn titleAmpColumn = getAvailableColumns().stream()
                            .filter(h -> h.getName().equals(PROJECT_TITLE)).findAny()
                            .get();

                    AmpReportColumn projectTitleColumn = new AmpReportColumn();
                    projectTitleColumn.setColumn(getAmpColumnById(titleAmpColumn.getId()));
                    projectTitleColumn.setLevel(getDefaultActivityLevel());
                    projectTitleColumn.setOrderId((long) report.getColumns().size() + 1);
                    report.getColumns().add(projectTitleColumn);
                }
            }
        }
    }

    private Set<AmpReportColumn> getReportColumns(final List<Long> columns) {
        Long orderId = 0L;
        TreeSet<AmpReportColumn> reportColumns = new TreeSet<>();
        for (Long columnId : columns) {
            AmpReportColumn reportColumn = new AmpReportColumn();
            reportColumn.setColumn(getAmpColumnById(columnId));
            reportColumn.setOrderId(++orderId);
            reportColumn.setLevel(getDefaultActivityLevel());
            reportColumns.add(reportColumn);
        }

        return reportColumns;
    }

    private Set<AmpReportHierarchy> getReportHierarchies(final List<Long> hierarchies) {
        TreeSet<AmpReportHierarchy> reportHierarchies = new TreeSet<>();
        if (hierarchies != null && !hierarchies.isEmpty()) {
            Long levelId = 0L;
            for (Long columnId : hierarchies) {
                AmpReportHierarchy reportHierarchy = new AmpReportHierarchy();
                reportHierarchy.setColumn(getAmpColumnById(columnId));
                reportHierarchy.setLevelId(++levelId);
                reportHierarchy.setLevel(getDefaultActivityLevel());
                reportHierarchies.add(reportHierarchy);
            }
        }

        return reportHierarchies;
    }

    private Set<AmpReportMeasures> getReportMeasures(final List<Long> measures) {
        Long orderId = 0L;
        TreeSet<AmpReportMeasures> reportMeasures = new TreeSet<>();
        for (Long measureId : measures) {
            AmpReportMeasures reportMeasure = new AmpReportMeasures();
            reportMeasure.setMeasure(getAmpMeasureById(measureId));
            reportMeasure.setOrderId(++orderId);
            reportMeasure.setLevel(getDefaultActivityLevel());
            reportMeasures.add(reportMeasure);
        }

        return reportMeasures;
    }

    private AmpColumns getAmpColumnById(final Long columnId) {
        return (AmpColumns) PersistenceManager.getSession().get(AmpColumns.class, columnId);
    }

    private AmpMeasures getAmpMeasureById(final Long columnId) {
        return (AmpMeasures) PersistenceManager.getSession().get(AmpMeasures.class, columnId);
    }

    @Nullable
    private AmpCategoryValue getDefaultActivityLevel() {
        return CategoryManagerUtil.getAmpCategoryValueFromDb(ACTIVITY_LEVEL_KEY, 0L);
    }

    public void addError(ApiErrorMessage error) {
        errors.put(error.id, error);
    }

    public Map<Integer, ApiErrorMessage> getErrors() {
        return errors;
    }

    /**
     * Provides import/update result
     *
     * @return JsonApiResponse the result of the import or update action
     */
    public JsonApiResponse<Report> getResult() {
        Map<String, Object> details = null;
        Report report = errors.isEmpty() ? getImportResult() : null;

        if (report == null) {
                details = new HashMap<>();
                details.put(REPORT, reportRequest);
            if (errors.isEmpty()) {
                addError(GenericErrors.UNKNOWN_ERROR);
            }
        }

        return buildResponse(details, report);
    }

    public Report getImportResult() {
        return convertAmpReportsToReport(this.report);
    }

    protected JsonApiResponse<Report> buildResponse(Map<String, Object> details, Report report) {
        return new JsonApiResponse<>(
                ApiError.formatNoWrap(errors.values()),
                null,
                details, report);
    }

    public Report getReport(final Long reportId) {
        return convertAmpReportsToReport(getReportById(reportId));
    }

    private Report convertAmpReportsToReport(final AmpReports ampReport) {
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
        report.setIncludeLocationChildren(arFilterConverter.getArFilter().isIncludeLocationChildren());

        return report;
    }

    private AmpReports getReportById(final Long reportId) {
        AmpReports ampReport = (AmpReports) PersistenceManager.getSession().get(AmpReports.class, reportId);
        if (ampReport == null) {
            ApiErrorResponseService.reportResourceNotFound(REPORT_NOT_FOUND.withDetails(String.valueOf(reportId)));
        }

        return ampReport;
    }

    private List<ReportColumn> getAvailableColumns() {
        ReportProfile reportProfile = report.isTab() ? ReportProfile.TAB : ReportProfile.REPORT;
        return columnProvider.getColumns(reportProfile, ReportType.fromLong(report.getType()));
    }

}
