package org.digijava.kernel.ampapi.endpoints.reports.designer;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.dgfoundation.amp.ar.AmpARFilter;
import org.dgfoundation.amp.ar.dbentity.AmpFilterData;
import org.dgfoundation.amp.newreports.AmountsUnits;
import org.dgfoundation.amp.newreports.AmpReportFilters;
import org.dgfoundation.amp.reports.converters.AmpARFilterConverter;
import org.dgfoundation.amp.reports.converters.AmpReportFiltersConverter;
import org.digijava.kernel.ampapi.endpoints.activity.TranslationSettings;
import org.digijava.kernel.ampapi.endpoints.common.AMPTranslatorService;
import org.digijava.kernel.ampapi.endpoints.common.JsonApiResponse;
import org.digijava.kernel.ampapi.endpoints.common.TranslationUtil;
import org.digijava.kernel.ampapi.endpoints.common.TranslatorService;
import org.digijava.kernel.ampapi.endpoints.dto.MultilingualContent;
import org.digijava.kernel.ampapi.endpoints.errors.ApiError;
import org.digijava.kernel.ampapi.endpoints.errors.ApiErrorMessage;
import org.digijava.kernel.ampapi.endpoints.errors.ApiErrorResponseService;
import org.digijava.kernel.ampapi.endpoints.errors.GenericErrors;
import org.digijava.kernel.ampapi.endpoints.reports.designer.validators.*;
import org.digijava.kernel.ampapi.endpoints.security.SecurityErrors;
import org.digijava.kernel.ampapi.endpoints.settings.SettingsConstants;
import org.digijava.kernel.ampapi.endpoints.util.FilterUtils;
import org.digijava.kernel.ampapi.endpoints.util.MaxSizeLinkedHashMap;
import org.digijava.kernel.persistence.PersistenceManager;
import org.digijava.kernel.request.TLSUtils;
import org.digijava.module.aim.dbentity.*;
import org.digijava.module.aim.helper.Constants;
import org.digijava.module.aim.helper.TeamMember;
import org.digijava.module.aim.util.*;
import org.digijava.module.categorymanager.dbentity.AmpCategoryValue;
import org.digijava.module.categorymanager.util.CategoryManagerUtil;
import org.digijava.module.translation.util.ContentTranslationUtil;
import org.digijava.module.translation.util.MultilingualInputFieldValues;
import org.jetbrains.annotations.Nullable;

import java.util.*;

import static java.lang.Boolean.TRUE;
import static org.dgfoundation.amp.ar.ColumnConstants.PROJECT_TITLE;
import static org.dgfoundation.amp.newreports.AmountsUnits.getAmountCode;
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

    public static final String PUBLIC_REPORT_GENERATOR_MODULE_NAME = "Public Report Generator";

    private static TranslatorService translatorService = AMPTranslatorService.INSTANCE;

    private static final Logger logger = Logger.getLogger(ReportManager.class);

    protected Map<Integer, ApiErrorMessage> errors = new HashMap<>();

    private ReportRequest reportRequest;

    private AmpReports report;

    private Integer reportToken;

    private final ReportColumnProvider columnProvider;

    private final ReportMeasureProvider measureProvider;

    private List<ReportValidator> fieldsValidators = new ArrayList<>();

    private List<ReportValidator> reportValidators = new ArrayList<>();

    public ReportManager(final ReportColumnProvider columnProvider, final ReportMeasureProvider measureProvider) {
        this.columnProvider = columnProvider;
        this.measureProvider = measureProvider;
    }

    public ReportManager createOrUpdateReport(ReportRequest reportRequest, final Long reportId,
                                              final Boolean isDynamic) {
        authorize(isDynamic);

        this.reportRequest = reportRequest;
        this.report = reportId == null ? new AmpReports() : getReportById(reportId);

        initValidators();
        validate();

        convertReportRequestToAmpReport();

        if (errors.isEmpty()) {
            if (isDynamic) {
                persistDynamicReport();
            } else {
                persistReport();
            }
        }

        return this;
    }

    public void initValidators() {
        fieldsValidators.add(new ReportNameValidator());
        fieldsValidators.add(new ReportUniqueNameValidator(report));
        fieldsValidators.add(new ReportTypeValidator());
        fieldsValidators.add(new ReportColumnValidator(columnProvider));
        fieldsValidators.add(new ReportMeasureValidator(measureProvider));
        fieldsValidators.add(new ReportHierarchyValidator(columnProvider));

        reportValidators.add(new ReportSummaryValidator());
        reportValidators.add(new ReportNonSummaryColumnsHierarchiesValidator());
        reportValidators.add(new ReportTabMaxMeasuresSizeValidator());
        reportValidators.add(new ReportMeasurelessHierarchiesValidator(columnProvider));
        reportValidators.add(new ReportMeasurelessHierarchiesAmountColumnsValidator(columnProvider));
    }

    private void validate() {
        validate(fieldsValidators);

        if (errors.isEmpty()) {
            validate(reportValidators);
        }
    }

    private void validate(List<ReportValidator> validators) {
        for (ReportValidator validator : validators) {
            if (!validator.isValid(reportRequest)) {
                addError(validator.getErrorMessage());
            }
        }
    }

    private void authorize(final Boolean isDynamic) {
        if (TeamUtil.getCurrentMember() == null && !isDynamic) {
            ApiErrorResponseService.reportUnauthorisedAccess(SecurityErrors.NOT_AUTHENTICATED);
        }

        if (isDynamic && !FeaturesUtil.isVisibleModule(PUBLIC_REPORT_GENERATOR_MODULE_NAME)) {
            ApiErrorResponseService.reportForbiddenAccess(GenericErrors.UNAUTHORIZED);
        }
    }

    /**
     *  Persist the current report with a generated negative hash code number in session's LinkedHashMap
     *  The list is limited to {@link#Constants.MAX_REPORTS_IN_SESSION}
     */
    private void persistDynamicReport() {
        reportToken = generateReportToken();

        MaxSizeLinkedHashMap<Integer, AmpReports> reportsList = Optional.ofNullable(TLSUtils.getReportStack())
                .orElse(new MaxSizeLinkedHashMap<>(Constants.MAX_REPORTS_IN_SESSION));
        report.setAmpReportId(Long.valueOf(reportToken));
        reportsList.put(reportToken, report);

        TLSUtils.updateReportStack(reportsList);
    }

    /**
     * Save current report in the database
     */
    private void persistReport() {
        TeamMember tm = TeamUtil.getCurrentMember();
        AdvancedReportUtil.saveReport(report, tm.getTeamId(), tm.getMemberId(), tm.getTeamHead());

        if (ContentTranslationUtil.multilingualIsEnabled()) {
            PersistenceManager.getSession().flush();
            persistContentTranslations();
        }
        logger.info("The report was saved with id = " + report.getAmpReportId());
    }

    /**
     * Generate a negative hash number used for storing dynamic reports in session
     * @return
     */
    private Integer generateReportToken() {
        Integer reportToken = UUID.randomUUID().toString().hashCode();
        if (reportToken > 0) {
            return reportToken * (-1);
        }

        return reportToken;
    }

    private void convertReportRequestToAmpReport() {
        report.setName(reportRequest.getName().getOrBuildText());

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
        boolean containsFilters = filterMap != null || reportRequest.isIncludeLocationChildren() != null
                || reportRequest.getSettings() != null;
        if (containsFilters) {
            AmpReportFilters reportFilters = FilterUtils.getFilters(filterMap, new AmpReportFilters());

            // Transform back to legacy AmpARFilters.
            AmpReportFiltersConverter converter = new AmpReportFiltersConverter(reportFilters);
            AmpARFilter filter = converter.buildFilters();
            if (filter != null) {
                if (reportRequest.isIncludeLocationChildren() != null) {
                    filter.setIncludeLocationChildren(reportRequest.isIncludeLocationChildren());
                }

                if (reportRequest.getSettings() != null) {
                    int divider = getAmountCode(AmountsUnits.getDefaultValue().divider);

                    String currency = reportRequest.getSettings().get(SettingsConstants.CURRENCY_ID).toString();
                    String calendar = reportRequest.getSettings().get(SettingsConstants.CALENDAR_TYPE_ID).toString();
                    filter.setCurrency(CurrencyUtil.getAmpcurrency(currency));
                    filter.setCalendarType(FiscalCalendarUtil.getAmpFiscalCalendar(new Long(calendar)));

                    if (reportRequest.getSettings().get(SettingsConstants.YEAR_RANGE_ID) != null) {
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

                    if (reportRequest.getSettings().get(SettingsConstants.AMOUNT_UNITS) != null) {
                        divider = getAmountCode(PersistenceManager.getInteger(
                                reportRequest.getSettings().get(SettingsConstants.AMOUNT_UNITS)));
                    }

                    filter.setAmountinthousand(divider);
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
        report.setName(getNameInMultilingualContent(ampReport));
        report.setId(ampReport.getAmpReportId());
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

    private MultilingualContent getNameInMultilingualContent(final AmpReports ampReport) {
        boolean isMultilingual = TranslationSettings.getCurrent().isMultilingual();
        Map<String, String> translations = new HashMap<>();
        if (isMultilingual) {
            MultilingualInputFieldValues mifv = new MultilingualInputFieldValues(AmpReports.class, 
                    ampReport.getAmpReportId(), "name", null, null);
            translations = mifv.getTranslations();
        }

        return MultilingualContent.build(isMultilingual, ampReport.getName(), translations);
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

    /**
     * Stores all provided translations
     *
     * @return value to be stored in the base table
     */
    protected void persistContentTranslations() {
        List<AmpContentTranslation> trnList =
                translatorService.loadFieldTranslations(AmpReports.class.getName(), report.getAmpReportId(), "name");
        // process translations
        for (Map.Entry<String, String> trn : reportRequest.getName().getOrBuildTranslations().entrySet()) {
            String langCode = trn.getKey();
            String translation = trn.getValue();
            AmpContentTranslation act = null;
            if (trn.equals(TLSUtils.getEffectiveLangCode())) {
                break;
            }
            for (AmpContentTranslation existingAct : trnList) {
                if (langCode.equalsIgnoreCase(existingAct.getLocale())) {
                    act = existingAct;
                    break;
                }
            }
            // if translation to be removed
            if (StringUtils.isBlank(trn.getValue())) {
                trnList.remove(act);
            } else if (act == null) {
                act = new AmpContentTranslation(AmpReports.class.getName(), report.getAmpReportId(), "name",
                        langCode, trn.getValue().trim());
                trnList.add(act);
            } else {
                act.setTranslation(translation.trim());
            }
        }

        TranslationUtil.serialize(report, "name", trnList);
    }
}
