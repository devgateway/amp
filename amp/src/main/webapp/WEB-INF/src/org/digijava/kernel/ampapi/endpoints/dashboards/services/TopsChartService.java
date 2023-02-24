package org.digijava.kernel.ampapi.endpoints.dashboards.services;

import org.dgfoundation.amp.ar.ArConstants;
import org.dgfoundation.amp.ar.ColumnConstants;
import org.dgfoundation.amp.newreports.AmountCell;
import org.dgfoundation.amp.newreports.AmountsUnits;
import org.dgfoundation.amp.newreports.AmpReportFilters;
import org.dgfoundation.amp.newreports.GeneratedReport;
import org.dgfoundation.amp.newreports.IdentifiedReportCell;
import org.dgfoundation.amp.newreports.ReportArea;
import org.dgfoundation.amp.newreports.ReportAreaImpl;
import org.dgfoundation.amp.newreports.ReportCell;
import org.dgfoundation.amp.newreports.ReportCollapsingStrategy;
import org.dgfoundation.amp.newreports.ReportColumn;
import org.dgfoundation.amp.newreports.ReportOutputColumn;
import org.dgfoundation.amp.newreports.ReportSpecificationImpl;
import org.dgfoundation.amp.newreports.SortingInfo;
import org.dgfoundation.amp.nireports.amp.OutputSettings;
import org.digijava.kernel.ampapi.endpoints.common.EndpointUtils;
import org.digijava.kernel.ampapi.endpoints.errors.ApiErrorResponseService;
import org.digijava.kernel.ampapi.endpoints.filters.FiltersConstants;
import org.digijava.kernel.ampapi.endpoints.gis.SettingsAndFiltersParameters;
import org.digijava.kernel.ampapi.endpoints.reports.ReportErrors;
import org.digijava.kernel.ampapi.endpoints.reports.ReportsUtil;
import org.digijava.kernel.ampapi.endpoints.settings.SettingsUtils;
import org.digijava.kernel.ampapi.endpoints.util.DashboardConstants;
import org.digijava.kernel.ampapi.endpoints.util.FilterUtils;
import org.digijava.kernel.translator.TranslatorWorker;
import org.digijava.kernel.util.SiteUtils;
import org.digijava.module.aim.util.FeaturesUtil;
import org.digijava.module.categorymanager.dbentity.AmpCategoryValue;
import org.digijava.module.categorymanager.util.CategoryConstants;
import org.digijava.module.categorymanager.util.CategoryManagerUtil;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import static javax.ws.rs.core.Response.Status.BAD_REQUEST;

/**
 * Tops Chart Dashboard Service
 *
 * @author Aldo Picca
 */
public class TopsChartService {

    private SettingsAndFiltersParameters config;
    private ReportSpecificationImpl spec;
    private GeneratedReport report;
    private Long id;
    private boolean isDisaggregate;
    private TopChartType type;
    private String name;
    private String title;
    private OutputSettings outSettings;
    private Integer limit;

    public TopsChartService(SettingsAndFiltersParameters config, TopChartType type, Integer limit) {
        this.config = config;
        this.type = type;
        this.limit = limit;
    }

    public TopsChartService(SettingsAndFiltersParameters config, TopChartType type, Long id) {
        this.config = config;
        this.type = type;
        this.id = id;
    }

    /**
     * Return (n) Donors sorted by amount or a project list if the chart detail was requested
     *
     * @return a JSON object with a list of objects.
     */
    public ProjectAmounts buildChartData(int offset) {
        isDisaggregate = true;
        prepare();

        return DashboardsService.buildPaginateJsonBean(report, offset);
    }

    /**
     * Return (n) Donors sorted by amount or a project list if the chart detail was requested
     *
     * @return a JSON object with a list of objects.
     */
    public TopChartData buildChartDataAggregated() {
        isDisaggregate = false;
        prepare();

        return buildJsonBeanAggregate();
    }

    private void prepare() {
        this.spec = new ReportSpecificationImpl("GetTops", ArConstants.DONOR_TYPE);

        switch (type) {
            case DO:
                if (FeaturesUtil.isVisibleField("Show Names As Acronyms")) {
                    setColumn(ColumnConstants.DONOR_ACRONYM);
                } else {
                    setColumn(ColumnConstants.DONOR_AGENCY);
                }
                applyFilter(FilterUtils.INSTANCE.idFromColumnName(ColumnConstants.DONOR_AGENCY));
                title = TranslatorWorker.translateText(DashboardConstants.TOP_DONOR_AGENCIES);
                name = DashboardConstants.TOP_DONOR_AGENCIES;
                break;
            case RO:
                setColumn(ColumnConstants.RESPONSIBLE_ORGANIZATION);
                applyFilter(FilterUtils.INSTANCE.idFromColumnName(ColumnConstants.RESPONSIBLE_ORGANIZATION));
                title = TranslatorWorker.translateText(DashboardConstants.TOP_RESPONSIBLE_ORGS);
                name = DashboardConstants.TOP_RESPONSIBLE_ORGS;
                break;
            case BA:
                setColumn(ColumnConstants.BENEFICIARY_AGENCY);
                applyFilter(FilterUtils.INSTANCE.idFromColumnName(ColumnConstants.BENEFICIARY_AGENCY));
                title = TranslatorWorker.translateText(DashboardConstants.TOP_BENEFICIARY_ORGS);
                name = DashboardConstants.TOP_BENEFICIARY_ORGS;
                break;
            case IA:
                setColumn(ColumnConstants.IMPLEMENTING_AGENCY);
                applyFilter(FilterUtils.INSTANCE.idFromColumnName(ColumnConstants.IMPLEMENTING_AGENCY));
                title = TranslatorWorker.translateText(DashboardConstants.TOP_IMPLEMENTING_ORGS);
                name = DashboardConstants.TOP_IMPLEMENTING_ORGS;
                break;
            case EA:
                setColumn(ColumnConstants.EXECUTING_AGENCY);
                applyFilter(FilterUtils.INSTANCE.idFromColumnName(ColumnConstants.EXECUTING_AGENCY));
                title = TranslatorWorker.translateText(DashboardConstants.TOP_EXECUTING_ORGS);
                name = DashboardConstants.TOP_EXECUTING_ORGS;
                break;
            case RE:
                setColumn(ColumnConstants.LOCATION_ADM_LEVEL_1);
                applyFilter(FilterUtils.INSTANCE.idFromColumnName(ColumnConstants.LOCATION_ADM_LEVEL_1));
                title = TranslatorWorker.translateText(DashboardConstants.TOP_REGIONS);
                name = DashboardConstants.TOP_REGIONS;
                spec.setReportCollapsingStrategy(ReportCollapsingStrategy.NEVER);
                break;
            case PS:
                setColumn(ColumnConstants.PRIMARY_SECTOR);
                applyFilter(FilterUtils.INSTANCE.idFromColumnName(ColumnConstants.PRIMARY_SECTOR));
                title = TranslatorWorker.translateText(DashboardConstants.TOP_SECTORS);
                name = DashboardConstants.TOP_SECTORS;
                break;
            case DG:
                setColumn(ColumnConstants.DONOR_GROUP);
                applyFilter(FilterUtils.INSTANCE.idFromColumnName(ColumnConstants.DONOR_GROUP));
                title = TranslatorWorker.translateText(DashboardConstants.TOP_DONOR_GROUPS);
                name = DashboardConstants.TOP_DONOR_GROUPS;
                break;
            case NDD:
                setColumn(ColumnConstants.SECONDARY_PROGRAM_LEVEL_1);
                applyFilter(FilterUtils.INSTANCE.idFromColumnName(ColumnConstants.SECONDARY_PROGRAM_LEVEL_1));
                name = DashboardConstants.PEACE_BUILDING_AND_STATE_BUILDING_GOALS;
                title = TranslatorWorker.translateText(DashboardConstants.PEACE_BUILDING_AND_STATE_BUILDING_GOALS);
                this.limit = 99999; // This chart has no limit of categories (no 'Others').

                // Add must-have filters for this chart.
                ArrayList<AmpCategoryValue> catList = new ArrayList<>(
                        CategoryManagerUtil.getAmpCategoryValueCollectionByKey(CategoryConstants.PEACE_MARKERS_KEY));
                List<Integer> peaceFilterOptions = new ArrayList<>();
                for (AmpCategoryValue category : catList) {
                    if (category.getValue().equals("1") || category.getValue().equals("2")
                            || category.getValue().equals("3")) {
                        peaceFilterOptions.add(category.getId().intValue());
                    }
                }
                LinkedHashMap<String, Object> peaceFilter = new LinkedHashMap<>();
                peaceFilter.put(FiltersConstants.PROCUREMENT_SYSTEM, peaceFilterOptions);
                LinkedHashMap<String, Object> filters = null;
                if (config != null) {
                    filters = (LinkedHashMap<String, Object>) config.getFilters();
                }
                if (filters == null) {
                    filters = new LinkedHashMap<>();
                }
                filters.putAll(peaceFilter);
                AmpReportFilters filterRules = FilterUtils.getFilterRules(filters, null);
                if (filterRules != null) {
                    spec.setFilters(filterRules);
                }

                break;
            default:
                throw new IllegalArgumentException("Unsupported chart: " + type);
        }

        this.outSettings = new OutputSettings(
                new HashSet<String>() {{
                    add(spec.getColumnNames().iterator().next());
                }});

        // applies settings, including funding type as a measure
        SettingsUtils.applyExtendedSettings(spec, config.getSettings());
        ReportsUtil.configureFilters(spec, config.getFilters());

        setOrder();

        // AMP-18740: For dashboards we need to use the default number formatting and leave the rest of the settings
        // configurable (calendar, currency, etc).
        DashboardsService.setCustomSettings(config, spec);

        this.report = EndpointUtils.runReport(spec, ReportAreaImpl.class, null);
        if (this.report == null) {
            ApiErrorResponseService.reportError(BAD_REQUEST, ReportErrors.REPORT_NOT_FOUND);
        }
    }

    /**
     * Set filter id to the report specification
     */
    public void applyFilter(String column) {
        Map<String, Object> filters = null;
        if (config != null) {
            filters = config.getFilters();
        }
        if (filters == null) {
            filters = new LinkedHashMap<>();
        }
        if (isDisaggregate) {
            filters.putAll(DashboardsService.setFilterId(id, column));
            AmpReportFilters filterRules = FilterUtils.getFilterRules(filters, null);
            if (filterRules != null) {
                spec.setFilters(filterRules);
            }

        }
    }

    private void setOrder() {
        if (this.isDisaggregate) {
            spec.addSorter(new SortingInfo(new ReportColumn(ColumnConstants.ACTIVITY_UPDATED_ON), false));
        } else {
            spec.addSorter(new SortingInfo(spec.getMeasures().iterator().next(), false));
        }
    }

    private void setColumn(String column) {
        if (this.isDisaggregate) {
            spec.addColumn(new ReportColumn(ColumnConstants.PROJECT_TITLE));
            spec.addColumn(new ReportColumn(ColumnConstants.ACTIVITY_UPDATED_ON));
        } else {
            spec.addColumn(new ReportColumn(column));
            spec.getHierarchies().addAll(spec.getColumns());
        }
    }

    private TopChartData buildJsonBeanAggregate() {
        TopChartData retlist = new TopChartData();
        List<TopChartAmount> values = new ArrayList<>();
        ReportOutputColumn criteriaCol = this.report.leafHeaders.get(0);
        ReportOutputColumn valueCol = this.report.leafHeaders.get(1);
        AmountsUnits unitsOption = this.spec.getSettings().getUnitsOption();
        // Format the report output return a simple list.
        // this is the report totals, which is not for the top N, but for ALL
        // results
        ReportCell totals;
        Double rawTotal;

        if (report != null && report.reportContents != null && report.reportContents.getContents() != null
                && report.reportContents.getContents().size() > 0) {
            totals = report.reportContents.getContents().get(valueCol);
            rawTotal = ((BigDecimal) totals.value).doubleValue();
            DashboardsService.postProcess(this.report, this.spec, outSettings, this.type);
        } else {
            rawTotal = new Double("0");
        }

        String currcode = spec.getSettings().getCurrencyCode();
        retlist.setCurrency(currcode);
        Integer maxLimit = report.reportContents.getChildren().size();

        BigDecimal totalPositive = BigDecimal.ZERO;
        for (ReportArea reportArea : report.reportContents.getChildren()) {
            Map<ReportOutputColumn, ReportCell> content = reportArea.getContents();
            AmountCell ac = (AmountCell) content.get(valueCol);
            BigDecimal amount = ac.extractValue();
            if (values.size() < this.limit) {
                TopChartAmount row = new TopChartAmount();
                row.setName(content.get(criteriaCol).displayedValue);
                row.setId(((IdentifiedReportCell) content.get(criteriaCol)).entityId);
                row.setAmount(amount);
                row.setFormattedAmount(ac.displayedValue);
                values.add(row);
            }
            if (amount.compareTo(BigDecimal.ZERO) > 0) {
                totalPositive = totalPositive.add(amount);
            }
        }
        if (SiteUtils.isEffectiveLangRTL()) {
            Collections.reverse(values);
        }
        retlist.setValues(values);

        retlist.setTotal(rawTotal);
        retlist.setSumarizedTotal(DashboardsService.calculateSumarizedTotals(rawTotal / unitsOption.divider, spec));
        // report the total number of tops available
        retlist.setMaxLimit(maxLimit);
        retlist.setTotalPositive(totalPositive);
        retlist.setName(name);
        retlist.setTitle(title);
        retlist.setSource(DashboardsService.getSource());
        return retlist;
    }
}
