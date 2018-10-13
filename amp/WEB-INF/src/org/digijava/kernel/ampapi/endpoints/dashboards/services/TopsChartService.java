package org.digijava.kernel.ampapi.endpoints.dashboards.services;


import org.apache.log4j.Logger;
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
import org.digijava.kernel.ampapi.endpoints.common.EPConstants;
import org.digijava.kernel.ampapi.endpoints.common.EndpointUtils;
import org.digijava.kernel.ampapi.endpoints.errors.ApiErrorResponse;
import org.digijava.kernel.ampapi.endpoints.filters.FiltersConstants;
import org.digijava.kernel.ampapi.endpoints.reports.ReportErrors;
import org.digijava.kernel.ampapi.endpoints.reports.ReportsUtil;
import org.digijava.kernel.ampapi.endpoints.settings.SettingsUtils;
import org.digijava.kernel.ampapi.endpoints.util.DashboardConstants;
import org.digijava.kernel.ampapi.endpoints.util.FilterUtils;
import org.digijava.kernel.ampapi.endpoints.util.JsonBean;
import org.digijava.kernel.ampapi.mondrian.util.MoConstants;
import org.digijava.kernel.translator.TranslatorWorker;
import org.digijava.module.aim.util.FeaturesUtil;
import org.digijava.module.categorymanager.dbentity.AmpCategoryValue;
import org.digijava.module.categorymanager.util.CategoryConstants;
import org.digijava.module.categorymanager.util.CategoryManagerUtil;

import java.math.BigDecimal;
import java.util.ArrayList;
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
    private static final Logger LOGGER = Logger.getLogger(TopsChartService.class);

    private JsonBean config;
    private ReportSpecificationImpl spec;
    private GeneratedReport report;
    private Long id;
    private boolean isDisaggregate;
    private String type;
    private String name;
    private String title;
    private OutputSettings outSettings;
    private Integer limit;

    public TopsChartService(JsonBean config, String type, Integer limit) {
        this.config = config;
        this.type = type;
        this.limit = limit;
        this.isDisaggregate = false;
    }

    public TopsChartService(JsonBean config, String type, Long id) {
        this.config = config;
        this.type = type;
        this.id = id;
        this.isDisaggregate = true;
    }

    /**
     * Return (n) Donors sorted by amount or a project list if the chart detail was requested
     *
     * @return a JSON object with a list of objects.
     */
    public JsonBean buildChartData() {
        this.spec = new ReportSpecificationImpl("GetTops", ArConstants.DONOR_TYPE);

        switch (this.type.toUpperCase()) {
            case "DO":
                if (FeaturesUtil.isVisibleField("Show Names As Acronyms")) {
                    setColumn(MoConstants.ATTR_ORG_ACRONYM);
                } else {
                    setColumn(MoConstants.DONOR_AGENCY);
                }
                applyFilter(FilterUtils.INSTANCE.idFromColumnName(MoConstants.DONOR_AGENCY));
                title = TranslatorWorker.translateText(DashboardConstants.TOP_DONOR_AGENCIES);
                name = DashboardConstants.TOP_DONOR_AGENCIES;
                break;
            case "RO":
                setColumn(MoConstants.RESPONSIBLE_AGENCY);
                applyFilter(FilterUtils.INSTANCE.idFromColumnName(MoConstants.RESPONSIBLE_AGENCY));
                title = TranslatorWorker.translateText(DashboardConstants.TOP_RESPONSIBLE_ORGS);
                name = DashboardConstants.TOP_RESPONSIBLE_ORGS;
                break;
            case "BA":
                setColumn(MoConstants.BENEFICIARY_AGENCY);
                applyFilter(FilterUtils.INSTANCE.idFromColumnName(MoConstants.BENEFICIARY_AGENCY));
                title = TranslatorWorker.translateText(DashboardConstants.TOP_BENEFICIARY_ORGS);
                name = DashboardConstants.TOP_BENEFICIARY_ORGS;
                break;
            case "IA":
                setColumn(MoConstants.IMPLEMENTING_AGENCY);
                applyFilter(FilterUtils.INSTANCE.idFromColumnName(MoConstants.IMPLEMENTING_AGENCY));
                title = TranslatorWorker.translateText(DashboardConstants.TOP_IMPLEMENTING_ORGS);
                name = DashboardConstants.TOP_IMPLEMENTING_ORGS;
                break;
            case "EA":
                setColumn(MoConstants.EXECUTING_AGENCY);
                applyFilter(FilterUtils.INSTANCE.idFromColumnName(MoConstants.EXECUTING_AGENCY));
                title = TranslatorWorker.translateText(DashboardConstants.TOP_EXECUTING_ORGS);
                name = DashboardConstants.TOP_EXECUTING_ORGS;
                break;
            case "RE":
                setColumn(ColumnConstants.REGION);
                applyFilter(FilterUtils.INSTANCE.idFromColumnName(ColumnConstants.REGION));
                title = TranslatorWorker.translateText(DashboardConstants.TOP_REGIONS);
                name = DashboardConstants.TOP_REGIONS;
                spec.setReportCollapsingStrategy(ReportCollapsingStrategy.NEVER);
                break;
            case "PS":
                setColumn(MoConstants.PRIMARY_SECTOR);
                applyFilter(FilterUtils.INSTANCE.idFromColumnName(MoConstants.PRIMARY_SECTOR));
                title = TranslatorWorker.translateText(DashboardConstants.TOP_SECTORS);
                name = DashboardConstants.TOP_SECTORS;
                break;
            case "DG":
                setColumn(ColumnConstants.DONOR_GROUP);
                applyFilter(FilterUtils.INSTANCE.idFromColumnName(ColumnConstants.DONOR_GROUP));
                title = TranslatorWorker.translateText(DashboardConstants.TOP_DONOR_GROUPS);
                name = DashboardConstants.TOP_DONOR_GROUPS;
                break;
            case "NDD":
                setColumn(ColumnConstants.SECONDARY_PROGRAM_LEVEL_1);
                applyFilter(FilterUtils.INSTANCE.idFromColumnName(ColumnConstants.SECONDARY_PROGRAM_LEVEL_1));
                name = DashboardConstants.PEACE_BUILDING_AND_STATE_BUILDING_GOALS;
                title = TranslatorWorker.translateText(DashboardConstants.PEACE_BUILDING_AND_STATE_BUILDING_GOALS);
                this.limit = 99999; // This chart has no limit of categories (no 'Others').

                // Add must-have filters for this chart.
                ArrayList<AmpCategoryValue> catList = new ArrayList<AmpCategoryValue>(
                        CategoryManagerUtil.getAmpCategoryValueCollectionByKey(CategoryConstants.PEACE_MARKERS_KEY));
                List<Integer> peaceFilterOptions = new ArrayList<Integer>();
                for (AmpCategoryValue category : catList) {
                    if (category.getValue().equals("1") || category.getValue().equals("2")
                            || category.getValue().equals("3")) {
                        peaceFilterOptions.add(category.getId().intValue());
                    }
                }
                LinkedHashMap<String, Object> peaceFilter = new LinkedHashMap<String, Object>();
                peaceFilter.put(FiltersConstants.PROCUREMENT_SYSTEM, peaceFilterOptions);
                LinkedHashMap<String, Object> filters = null;
                if (config != null) {
                    filters = (LinkedHashMap<String, Object>) config.get(EPConstants.FILTERS);
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
                setColumn(MoConstants.DONOR_AGENCY);
                applyFilter(FilterUtils.INSTANCE.idFromColumnName(MoConstants.DONOR_AGENCY));
                title = TranslatorWorker.translateText(DashboardConstants.TOP_DONOR_AGENCIES);
                name = DashboardConstants.TOP_DONOR_AGENCIES;
                break;
        }

        this.outSettings = new OutputSettings(
                new HashSet<String>() {{
                    add(spec.getColumnNames().iterator().next());
                }});

        // applies settings, including funding type as a measure
        SettingsUtils.applyExtendedSettings(spec, config);
        ReportsUtil.configureFilters(spec, config);

        setOrder();

        // AMP-18740: For dashboards we need to use the default number formatting and leave the rest of the settings
        // configurable (calendar, currency, etc).
        DashboardsService.setCustomSettings(config, spec);

        this.report = EndpointUtils.runReport(spec, ReportAreaImpl.class, null);
        if (this.report == null) {
            ApiErrorResponse.reportError(BAD_REQUEST, ReportErrors.REPORT_NOT_FOUND);
        }

        if (isDisaggregate) {
            return DashboardsService.buildPaginateJsonBean(report, DashboardsService.getOffset(config));
        } else {
            return buildJsonBeanAggregate();
        }
    }

    /**
     * Set filter id to the report specification
     */
    public void applyFilter(String column) {
        Map<String, Object> filters = null;
        if (config != null) {
            filters = (Map<String, Object>) config.get(EPConstants.FILTERS);
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

    public void setOrder() {
        if (this.isDisaggregate) {
            spec.addSorter(new SortingInfo(new ReportColumn(ColumnConstants.ACTIVITY_UPDATED_ON), false));
        } else {
            spec.addSorter(new SortingInfo(spec.getMeasures().iterator().next(), false));
        }
    }

    public void setColumn(String column) {
        if (this.isDisaggregate) {
            spec.addColumn(new ReportColumn(ColumnConstants.PROJECT_TITLE));
            spec.addColumn(new ReportColumn(ColumnConstants.ACTIVITY_UPDATED_ON));
        } else {
            spec.addColumn(new ReportColumn(column));
            spec.getHierarchies().addAll(spec.getColumns());
        }
    }

    private JsonBean buildJsonBeanAggregate() {
        JsonBean retlist = new JsonBean();
        List<JsonBean> values = new ArrayList<JsonBean>();
        ReportOutputColumn criteriaCol = this.report.leafHeaders.get(0);
        ReportOutputColumn valueCol = this.report.leafHeaders.get(1);
        AmountsUnits unitsOption = this.spec.getSettings().getUnitsOption();
        // Format the report output return a simple list.
        // this is the report totals, which is not for the top N, but for ALL
        // results
        ReportCell totals = null;
        Double rawTotal = null;

        if (report != null && report.reportContents != null && report.reportContents.getContents() != null
                && report.reportContents.getContents().size() > 0) {
            totals = (ReportCell) report.reportContents.getContents().get(valueCol);
            rawTotal = ((BigDecimal) totals.value).doubleValue();
            DashboardsService.postProcess(this.report, this.spec, outSettings, this.type);
        } else {
            rawTotal = new Double("0");
        }

        String currcode = spec.getSettings().getCurrencyCode();
        retlist.set("currency", currcode);
        Integer maxLimit = report.reportContents.getChildren().size();

        double totalPositive = 0;
        for (ReportArea reportArea : report.reportContents.getChildren()) {
            Map<ReportOutputColumn, ReportCell> content = reportArea.getContents();
            AmountCell ac = (AmountCell) content.get(valueCol);
            double amount = ((BigDecimal) ac.value).doubleValue();
            if (values.size() < this.limit) {
                JsonBean row = new JsonBean();
                row.set("name", content.get(criteriaCol).displayedValue);
                row.set("id", ((IdentifiedReportCell) content.get(criteriaCol)).entityId);
                row.set("amount", amount);
                row.set("formattedAmount", ac.displayedValue);
                values.add(row);
            }
            if (amount > 0) {
                totalPositive += amount;
            }
        }
        retlist.set("values", values);

        retlist.set("total", rawTotal);
        retlist.set("sumarizedTotal",
                DashboardsService.calculateSumarizedTotals(rawTotal / unitsOption.divider, spec));
        // report the total number of tops available
        retlist.set("maxLimit", maxLimit);
        retlist.set("totalPositive", totalPositive);
        retlist.set("name", name);
        retlist.set("title", title);
        return retlist;
    }
}
