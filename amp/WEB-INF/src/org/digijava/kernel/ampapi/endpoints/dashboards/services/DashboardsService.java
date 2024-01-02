package org.digijava.kernel.ampapi.endpoints.dashboards.services;

import com.google.common.base.Preconditions;
import com.google.common.base.Strings;
import org.apache.commons.lang3.StringUtils;
import org.dgfoundation.amp.ar.ArConstants;
import org.dgfoundation.amp.ar.ColumnConstants;
import org.dgfoundation.amp.ar.MeasureConstants;
import org.dgfoundation.amp.newreports.*;
import org.dgfoundation.amp.nireports.NiReportsEngine;
import org.dgfoundation.amp.nireports.amp.OutputSettings;
import org.dgfoundation.amp.reports.ReportUtils;
import org.digijava.kernel.ampapi.endpoints.common.EndpointUtils;
import org.digijava.kernel.ampapi.endpoints.dashboards.DashboardFormParameters;
import org.digijava.kernel.ampapi.endpoints.filters.FiltersConstants;
import org.digijava.kernel.ampapi.endpoints.gis.SettingsAndFiltersParameters;
import org.digijava.kernel.ampapi.endpoints.reports.ReportsUtil;
import org.digijava.kernel.ampapi.endpoints.settings.SettingsConstants;
import org.digijava.kernel.ampapi.endpoints.settings.SettingsUtils;
import org.digijava.kernel.ampapi.endpoints.util.DashboardConstants;
import org.digijava.kernel.ampapi.endpoints.util.FilterUtils;
import org.digijava.kernel.request.TLSUtils;
import org.digijava.kernel.translator.TranslatorWorker;
import org.digijava.kernel.util.RequestUtils;
import org.digijava.module.aim.dbentity.AmpCategoryValueLocations;
import org.digijava.module.aim.util.DynLocationManagerUtil;
import org.digijava.module.aim.util.FeaturesUtil;
import org.digijava.module.aim.util.FiscalCalendarUtil;
import org.digijava.module.categorymanager.dbentity.AmpCategoryValue;
import org.digijava.module.categorymanager.util.CategoryConstants;
import org.digijava.module.categorymanager.util.CategoryManagerUtil;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.TreeMap;

import static org.dgfoundation.amp.nireports.runtime.ColumnReportData.UNALLOCATED_ID;

/**
 * @author Diego Dimunzio
 */

public final class DashboardsService {

    private static final int RECORDS_PER_PAGE = 50;
    private static final int EXP_1 = 1;
    private static final int EXP_2 = 2;
    private static final int EXP_3 = 3;
    private static final int EXP_4 = 4;
    private static final int EXP_5 = 5;
    private static final int EXP_6 = 6;

    private DashboardsService() {

    }

    /**
     * Return a list of the available top __ for the dashboard charts Note -- I
     * (Phil) hacked this in, so it probably could use a review Also, I
     * hard-coded the names ("Donor Agency" etc.) but they should be translated
     *
     * @return
     */
    public static List<TopDescription> getTopsList() {
        List<TopDescription> tops = new ArrayList<>();
        tops.add(new TopDescription("do", "Donor Agency"));
        tops.add(new TopDescription("re", "Region"));
        tops.add(new TopDescription("ps", "Primary Sector"));
        return tops;
    }

    public static int getOffset(DashboardFormParameters config) {
        return EndpointUtils.getSingleValue(config.getOffset(), 0);
    }

    public static LinkedHashMap<String, Object> setFilterId(Long id, String column) {
        LinkedHashMap<String, Object> filterObject = new LinkedHashMap<String, Object>();
        List<Long> filterIds = new ArrayList<Long>();
        if (id > 0 || id.equals(UNALLOCATED_ID)) {
            filterIds.add(id);
            filterObject.put(column, filterIds);
        }
        return filterObject;
    }

    public static LinkedHashMap<String, Object> setFilterYear(Integer year) {
        LinkedHashMap<String, Object> filterObject = new LinkedHashMap<String, Object>();
        LinkedHashMap<String, Object> filterYear = new LinkedHashMap<String, Object>();
        filterYear.put("start", year.intValue() + "-01-01");
        filterYear.put("end", year.intValue() + "-12-31");
        filterObject.put(FiltersConstants.DATE, filterYear);
        return filterObject;
    }

    protected static void postProcess(GeneratedReport report, ReportSpecificationImpl spec, OutputSettings outSettings,
                                      TopChartType type) {
        if (type == TopChartType.RE) {
            postProcessRE(report, spec, outSettings);
        }
    }

    /**
     * Replace "Undefined" region with "International", "National" and actual "Undefined" region
     * (this is one of the workaround solutions)
     *
     * @param report
     * @param spec
     * @param outSettings
     */
    protected static void postProcessRE(GeneratedReport report, ReportSpecificationImpl spec,
                                        OutputSettings outSettings) {
        final DecimalFormat formatter = ReportsUtil.getDecimalFormatOrDefault(spec);
        final AmountsUnits amountsUnits = ReportsUtil.getAmountsUnitsOrDefault(spec);

        List<ReportArea> undefinedAreas = new ArrayList<>();
        for (ReportArea ra : report.reportContents.getChildren()) {
            // detect those undefined for countries, but skip those that are really undefined for regions
            if (ra.getOwner() != null && ra.getOwner().id < 0
                    && (ra.getOwner().id != UNALLOCATED_ID)) {
                undefinedAreas.add(ra);
            }
        }
        // if no countries are found, then nothing to update
        if (undefinedAreas.isEmpty()) {
            return;
        }

        ReportOutputColumn regionCol = report.leafHeaders.get(0);
        ReportOutputColumn amountCol = report.leafHeaders.get(1);
        AmpCategoryValueLocations currentCountry = DynLocationManagerUtil.getDefaultCountry();

        // collect other countries under International
        final Map<Long, String> internationalEntitiesIdsValues = new TreeMap<>();
        List<ReportArea> intlChildren = new ArrayList<>();
        long intlAnyCountryId = 0;
        ReportArea intlUndefined = null;
        BigDecimal intlAmount = null;

        for (ReportArea undefined : undefinedAreas) {
            IdentifiedReportCell uRegionCell = (IdentifiedReportCell) undefined.getContents().get(regionCol);

            if (uRegionCell.entityId == -currentCountry.getId()) {
                // national
                updateUndefinedEntry(undefined, regionCol, DashboardConstants.NATIONAL, uRegionCell.entityId,
                        uRegionCell.entitiesIdsValues, undefined.getChildren());
            } else {
                BigDecimal otherCountryAmount = (BigDecimal) undefined.getContents().get(amountCol).value;
                // international
                if (intlUndefined == null) {
                    // reuse only the 1st country that we'll transformed to international later
                    intlUndefined = undefined;
                    intlAnyCountryId = uRegionCell.entityId; // not relevant for this EP
                    intlAmount = otherCountryAmount;
                } else {
                    report.reportContents.getChildren().remove(undefined);
                    intlAmount = intlAmount.add(otherCountryAmount);
                }
                //internationId = uRegion.entityId; // remember any id, not relevant
                if (uRegionCell.entitiesIdsValues != null) {
                    internationalEntitiesIdsValues.putAll(uRegionCell.entitiesIdsValues);
                }
                intlChildren.addAll(undefined.getChildren());
            }
        }

        if (intlUndefined != null) {
            // repeating Reports behavior: divide only formatted amount
            BigDecimal scaledAmount = intlAmount.divide(BigDecimal.valueOf(amountsUnits.divider));
            intlUndefined.getContents().put(amountCol, new AmountCell(intlAmount, formatter.format(scaledAmount)));
            updateUndefinedEntry(intlUndefined, regionCol, DashboardConstants.INTERNATIONAL, intlAnyCountryId,
                    internationalEntitiesIdsValues, intlChildren);
        }
    }

    private static void updateUndefinedEntry(ReportArea undefined, ReportOutputColumn regionCol,
                                             String name, long id, Map<Long, String> entitiesIdsValues,
                                             List<ReportArea> children) {
        // recreate the cell to have a correct name for the undefined area
        TextCell uRegionCell = new TextCell(TranslatorWorker.translateText(name), id, entitiesIdsValues);
        undefined.getContents().put(regionCol, uRegionCell);

        for (ReportArea child : undefined.getChildren()) {
            child.getContents().put(regionCol, uRegionCell);
        }
    }

    public static AidPredictabilityChartData getAidPredictability(SettingsAndFiltersParameters filter) {
        ReportSpecificationImpl spec = getAidPredictabilityReportSpec(filter);

        GeneratedReport report = EndpointUtils.runReport(spec, ReportAreaImpl.class, null);

        AidPredictabilityChartData retlist = new AidPredictabilityChartData();

        //Not only years, we can have values like 'Fiscal calendar 2010-2011', so the Map should be <String,JSONObject>
        Map<String, AidPredictabilityAmounts> results = new TreeMap<>(); // accumulator of per-year results

        if (report.reportContents.getContents() != null) {
            for (ReportOutputColumn outputColumn : report.reportContents.getContents().keySet()) {
                // ignore non-funding contents
                if (outputColumn.parentColumn == null) {
                    continue;
                }

                boolean isPlannedColumn =
                        outputColumn.originalColumnName.equals(MeasureConstants.PLANNED_DISBURSEMENTS);
                boolean isTotalColumn = outputColumn.parentColumn.originalColumnName.equals(
                        NiReportsEngine.TOTALS_COLUMN_NAME);

                String yearValue = isTotalColumn ? "totals" : outputColumn.parentColumn.columnName;
                if (!results.containsKey(yearValue)) {
                    results.put(yearValue, new AidPredictabilityAmounts());
                }
                AidPredictabilityAmounts amountObj = results.get(yearValue);

                AidPredictabilityAmount amounts = new AidPredictabilityAmount();
                AmountCell reportCell = (AmountCell) report.reportContents.getContents().get(outputColumn);
                amounts.setAmount(reportCell.extractValue());
                amounts.setFormattedAmount(reportCell.displayedValue);
                if (isPlannedColumn) {
                    amountObj.setPlanned(amounts);
                } else {
                    amountObj.setActual(amounts);
                }
            }
        }
        List<AidPredictabilityAmounts> yearsArray = new ArrayList<>();
        for (String yearValue : results.keySet()) {
            if (!yearValue.equals("totals")) {
                results.get(yearValue).setYear(yearValue);
                yearsArray.add(results.get(yearValue));
            }
        }
        retlist.setYears(yearsArray);
        retlist.setTotals(results.get("totals"));

        retlist.setCurrency(spec.getSettings().getCurrencyCode());

        retlist.setName(DashboardConstants.AID_PREDICTABILITY);
        retlist.setTitle(TranslatorWorker.translateText(DashboardConstants.AID_PREDICTABILITY));
        retlist.setMeasure("disbursements");
        retlist.setSource(getSource());
        return retlist;
    }

    public static String getSource() {
        return FeaturesUtil.isVisibleFeature("/Dashboards", "Source")
                ? buildSource()
                : null;
    }

    private static String buildSource() {
        return String.format("%s: %s - %s",
                TranslatorWorker.translateText("Source"),
                TranslatorWorker.translateText("AMP"),
                TranslatorWorker.translateText(FeaturesUtil.getCurrentCountryName()));
    }

    public static ProjectAmounts getAidPredictabilityProjects(DashboardFormParameters filter, String year,
                                                              String measure) {
        Objects.requireNonNull(year);
        Preconditions.checkArgument(!Strings.isNullOrEmpty(measure));

        ReportSpecificationImpl spec = getAidPredictabilityProjectsReportSpec(filter, year, measure);

        GeneratedReport report = EndpointUtils.runReport(spec, ReportAreaImpl.class, null);

        return buildPaginateJsonBean(report, getOffset(filter));
    }

    private static ReportSpecificationImpl getAidPredictabilityReportSpec(SettingsAndFiltersParameters params) {
        ReportSpecificationImpl spec = new ReportSpecificationImpl("GetAidPredictability", ArConstants.DONOR_TYPE);
        LinkedHashMap<String, Object> filters = null;
        if (params != null) {
            filters = (LinkedHashMap<String, Object>) params.getFilters();
        }
        if (filters == null) {
            filters = new LinkedHashMap<>();
        }

        spec.addColumn(new ReportColumn(ColumnConstants.LOCATION_ADM_LEVEL_0));
        spec.getHierarchies().add(new ReportColumn(ColumnConstants.LOCATION_ADM_LEVEL_0));
        spec.addMeasure(new ReportMeasure(MeasureConstants.PLANNED_DISBURSEMENTS));
        spec.addMeasure(new ReportMeasure(MeasureConstants.ACTUAL_DISBURSEMENTS));
        spec.setGroupingCriteria(GroupingCriteria.GROUPING_YEARLY);

        AmpReportFilters filterRules = FilterUtils.getFilterRules(filters, null);
        if (filterRules != null) {
            spec.setFilters(filterRules);
        }
        SettingsUtils.applySettings(spec, params.getSettings(), true);

        // AMP-18740: For dashboards we need to use the default number formatting and leave the rest of the settings
        // configurable (calendar, currency, etc).
        setCustomSettings(params, spec);
        return spec;
    }

    private static ReportSpecificationImpl getAidPredictabilityProjectsReportSpec(
            SettingsAndFiltersParameters params, String yearString, String measure) {
        ReportSpecificationImpl spec = new ReportSpecificationImpl("GetAidPredictability", ArConstants.DONOR_TYPE);
        LinkedHashMap<String, Object> filters = null;
        if (params != null) {
            filters = (LinkedHashMap<String, Object>) params.getFilters();
        }
        if (filters == null) {
            filters = new LinkedHashMap<>();
        }

        SettingsUtils.applySettings(spec, params.getSettings(), true);
        int year = 0;
        if (spec.getSettings() != null && StringUtils.isNotBlank(yearString)) {
            year = spec.getSettings().getCalendar().parseYear(yearString);
        }
        spec.addColumn(new ReportColumn(ColumnConstants.PROJECT_TITLE));
        spec.addColumn(new ReportColumn(ColumnConstants.ACTIVITY_UPDATED_ON));
        if (measure.equalsIgnoreCase(MeasureConstants.PLANNED_DISBURSEMENTS)) {
            spec.addMeasure(new ReportMeasure(MeasureConstants.PLANNED_DISBURSEMENTS));
        } else {
            spec.addMeasure(new ReportMeasure(MeasureConstants.ACTUAL_DISBURSEMENTS));
        }
        spec.setGroupingCriteria(GroupingCriteria.GROUPING_YEARLY);

        filters.putAll(setFilterYear(year));

        AmpReportFilters filterRules = FilterUtils.getFilterRules(filters, null);
        if (filterRules != null) {
            spec.setFilters(filterRules);
        }

        FilterUtils.updateAllDateFilters(spec);

        // AMP-18740: For dashboards we need to use the default number formatting and leave the rest of the settings
        // configurable (calendar, currency, etc).
        setCustomSettings(params, spec);
        return spec;
    }

    public static FundingTypeChartData getFundingTypeChartData(SettingsAndFiltersParameters filter) {
        return getFundingChartData(filter, 1);
    }

    public static FundingTypeChartData getFinancingInstrumentChartData(SettingsAndFiltersParameters filter) {
        return getFundingChartData(filter, 2);
    }

    public static FundingTypeChartData getFundingChartData(SettingsAndFiltersParameters filter, Integer reportType) {
        ReportSpecificationImpl spec = getFundingTypeChartReportSpec(filter, reportType);

        GeneratedReport report = EndpointUtils.runReport(spec, ReportAreaImpl.class, null);

        spec.addSorter(new SortingInfo(spec.getMeasures().iterator().next(), false));
        //Get total
        AmountCell totals = (AmountCell) report.reportContents.getContents().get(
                report.leafHeaders.get(report.leafHeaders.size() - 1));

        FundingTypeChartData retlist = new FundingTypeChartData();

        retlist.setTotal(totals.extractValue());
        retlist.setSumarizedTotal(calculateSumarizedTotals(totals.extractValue().doubleValue(), spec));

        String currcode = null;
        currcode = spec.getSettings().getCurrencyCode();
        retlist.setCurrency(currcode);

        Map<String, List<FundingTypeAmount>> values = new TreeMap<>(); // Map<year, List<type, amount, formattedAmount>>
        ReportOutputColumn toaCol = report.leafHeaders.get(0);

        String fundingTypeConstant = DashboardConstants.FUNDING_TYPE;
        String title = TranslatorWorker.translateText(fundingTypeConstant);
        List<FundingTypeAmountsForYear> outValues = new ArrayList<>();


        // check if report has report contents
        if (report.isEmpty) {
            retlist.setValues(outValues);
            retlist.setName(fundingTypeConstant);
            retlist.setTitle(title);

            return retlist;
        }

        for (ReportArea toaArea : report.reportContents.getChildren()) {
            String toa = toaArea.getContents().get(toaCol).displayedValue;
            long toaId = toaArea.getOwner().id;
            for (int i = 1; i < report.leafHeaders.size() - 1; i++) {
                FundingTypeAmount toaBean = new FundingTypeAmount();
                toaBean.setType(toa);
                toaBean.setId(toaId);
                ReportOutputColumn hdr = report.leafHeaders.get(i);
                //long year = Integer.valueOf(hdr.parentColumn.originalColumnName);
                String toaYear = hdr.parentColumn.columnName;
                AmountCell cell = (AmountCell) toaArea.getContents().get(hdr);
                toaBean.setAmount(cell.extractValue());
                toaBean.setFormattedAmount(cell.displayedValue);
                if (!values.containsKey(toaYear)) {
                    values.put(toaYear, new ArrayList<>());
                }
                values.get(toaYear).add(toaBean);
                //values.computeIfAbsent(year, yr -> new ArrayList<>()).add(toaBean);
            }
        }

        for (String yearValue : values.keySet()) {
            FundingTypeAmountsForYear yearBean = new FundingTypeAmountsForYear();
            yearBean.setYear(yearValue);
            yearBean.setValues(values.get(yearValue));
            outValues.add(yearBean);
        }
        retlist.setValues(outValues);
        if (reportType == 1) {
            retlist.setName(DashboardConstants.FUNDING_TYPE);
            retlist.setTitle(TranslatorWorker.translateText(DashboardConstants.FUNDING_TYPE));
        } else {
            retlist.setName(DashboardConstants.FINANCING_INSTRUMENT);
            retlist.setTitle(TranslatorWorker.translateText(DashboardConstants.FINANCING_INSTRUMENT));
        }
        for (ReportMeasure m : spec.getMeasures()) {
            retlist.addMeasure(m.getMeasureName(), TranslatorWorker.translateText(m.getMeasureName()));

        }
        retlist.setSource(getSource());
        return retlist;
    }

    public static ProjectAmounts getProjectsByFundingTypeAndYear(DashboardFormParameters filter, String year,
                                                                 Integer id) {
        Objects.requireNonNull(year);
        Objects.requireNonNull(id);

        ReportSpecificationImpl spec = getFundingTypeProjectsReportSpec(filter, year, id);

        GeneratedReport report = EndpointUtils.runReport(spec, ReportAreaImpl.class, null);

        return buildPaginateJsonBean(report, getOffset(filter));
    }

    /**
     * @param filter
     * @param reportType 1- TYPE_OF_ASSISTANCE 2- FINANCING_INSTRUMENT
     * @return
     */

    private static ReportSpecificationImpl getFundingTypeChartReportSpec(SettingsAndFiltersParameters filter,
                                                                         Integer reportType) {
        ReportSpecificationImpl spec = new ReportSpecificationImpl("fundingtype", ArConstants.DONOR_TYPE);
        LinkedHashMap<String, Object> filters = null;
        if (filter != null) {
            filters = (LinkedHashMap<String, Object>) filter.getFilters();
        }
        if (filters == null) {
            filters = new LinkedHashMap<>();
        }

        SettingsUtils.applyExtendedSettings(spec, filter.getSettings());

        spec.setGroupingCriteria(GroupingCriteria.GROUPING_YEARLY);
        if (reportType.equals(1)) {
            spec.addColumn(new ReportColumn(ColumnConstants.TYPE_OF_ASSISTANCE));
        } else {
            spec.addColumn(new ReportColumn(ColumnConstants.FINANCING_INSTRUMENT));
        }

        spec.getHierarchies().addAll(spec.getColumns());
        spec.setSummaryReport(true);

        spec.addSorter(new SortingInfo(spec.getMeasures().iterator().next(), false));
        AmpReportFilters filterRules = FilterUtils.getFilterRules(filters, null);
        if (filterRules != null) {
            spec.setFilters(filterRules);
        }

        // AMP-18740: For dashboards we need to use the default number formatting and leave the rest of the settings
        // configurable (calendar, currency, etc).
        setCustomSettings(filter, spec);
        return spec;
    }

    private static ReportSpecificationImpl getFundingTypeProjectsReportSpec(SettingsAndFiltersParameters params,
                                                                            String yearString, Integer id) {
        ReportSpecificationImpl spec = new ReportSpecificationImpl("fundingtype", ArConstants.DONOR_TYPE);
        LinkedHashMap<String, Object> filters = null;
        if (params != null) {
            filters = (LinkedHashMap<String, Object>) params.getFilters();
        }
        if (filters == null) {
            filters = new LinkedHashMap<>();
        }

        SettingsUtils.applyExtendedSettings(spec, params.getSettings());
        int year = 0;
        if (spec.getSettings() != null && StringUtils.isNotBlank(yearString)) {
            year = spec.getSettings().getCalendar().parseYear(yearString);
        }

        spec.addColumn(new ReportColumn(ColumnConstants.PROJECT_TITLE));
        spec.addColumn(new ReportColumn(ColumnConstants.ACTIVITY_UPDATED_ON));
        spec.setGroupingCriteria(GroupingCriteria.GROUPING_YEARLY);
        filters.putAll(DashboardsService.setFilterId(id.longValue(),
                FilterUtils.INSTANCE.idFromColumnName(ColumnConstants.TYPE_OF_ASSISTANCE)));
        filters.putAll(setFilterYear(year));

        AmpReportFilters filterRules = FilterUtils.getFilterRules(filters, null);
        if (filterRules != null) {
            spec.setFilters(filterRules);
        }
        FilterUtils.updateAllDateFilters(spec);

        spec.addSorter(new SortingInfo(new ReportColumn(ColumnConstants.ACTIVITY_UPDATED_ON), false));
        spec.addSorter(new SortingInfo(spec.getMeasures().iterator().next(), false));

        // AMP-18740: For dashboards we need to use the default number formatting and leave the rest of the settings
        // configurable (calendar, currency, etc).
        setCustomSettings(params, spec);
        return spec;
    }

    public static ProjectAmounts getPeaceMarkerProjectsByCategory(DashboardFormParameters config, Integer id) {

        ReportSpecificationImpl spec = new ReportSpecificationImpl("GetNDD", ArConstants.DONOR_TYPE);
        spec.addColumn(new ReportColumn(ColumnConstants.PROJECT_TITLE));
        spec.addColumn(new ReportColumn(ColumnConstants.ACTIVITY_UPDATED_ON));

        // applies settings, including funding type as a measure
        SettingsUtils.applyExtendedSettings(spec, config.getSettings());
        spec.addSorter(new SortingInfo(spec.getMeasures().iterator().next(), false));

        LinkedHashMap<String, Object> filters;
        filters = (LinkedHashMap<String, Object>) config.getFilters();
        if (filters == null) {
            filters = new LinkedHashMap<>();
        }
        // Add must-have filters for this chart.
        ArrayList<AmpCategoryValue> catList = new ArrayList<AmpCategoryValue>(
                CategoryManagerUtil.getAmpCategoryValueCollectionByKey(CategoryConstants.PEACE_MARKERS_KEY));
        List<Integer> peaceFilterOptions = new ArrayList<Integer>();
        for (AmpCategoryValue category : catList) {
            if (category.getValue().equals("1") || category.getValue().equals("2") || category.getValue().equals("3")) {
                peaceFilterOptions.add(category.getId().intValue());
            }
        }
        LinkedHashMap<String, Object> peaceFilter = new LinkedHashMap<String, Object>();
        peaceFilter.put(FiltersConstants.PROCUREMENT_SYSTEM, peaceFilterOptions);
        filters.putAll(peaceFilter);
        // Add filter for secondary program.
        LinkedHashMap<String, Object> secondaryProgramFilter = new LinkedHashMap<String, Object>();
        List<Integer> secondaryProgramIds = new ArrayList<Integer>();
        secondaryProgramIds.add(id);
        secondaryProgramFilter.put(FiltersConstants.SECONDARY_PROGRAM_LEVEL_1, secondaryProgramIds);
        filters.putAll(secondaryProgramFilter);
        AmpReportFilters filterRules = FilterUtils.getFilterRules(filters, null);
        if (filterRules != null) {
            spec.setFilters(filterRules);
        }

        // AMP-18740: For dashboards we need to use the default number formatting and leave the rest of the settings
        // configurable (calendar, currency, etc).
        setCustomSettings(config, spec);

        GeneratedReport report = EndpointUtils.runReport(spec, ReportAreaImpl.class, null);
        return buildPaginateJsonBean(report, getOffset(config));
    }

    public static ProjectAmounts buildPaginateJsonBean(GeneratedReport report, int offset) {
        ProjectAmounts retlist = new ProjectAmounts();
        List<ProjectAmount> values = new ArrayList<>();
        ReportOutputColumn titleCol = report.leafHeaders.get(0);
        ReportOutputColumn dateCol = report.leafHeaders.get(1);
        ReportOutputColumn amountCol = report.leafHeaders.get(2);

        report.reportContents.getChildren().stream().limit(offset + RECORDS_PER_PAGE).forEach(
                n -> {
                    ProjectAmount row = new ProjectAmount();
                    IdentifiedReportCell title = (IdentifiedReportCell) n.getContents().get(titleCol);
                    AmountCell amount = (AmountCell) n.getContents().get(amountCol);
                    DateCell date = (DateCell) n.getContents().get(dateCol);
                    if (title != null) {
                        row.setName(title.displayedValue);
                        row.setId(title.entityId);
                    }
                    if (amount != null) {
                        row.setAmount(amount.extractValue());
                        row.setFormattedAmount(amount.displayedValue);
                    }
                    if (date != null) {
                        row.setDate(date.displayedValue);
                    }
                    values.add(row);
                }
        );

        retlist.setTotalRecords(report.reportContents.getChildren().size());
        retlist.setValues(values);

        return retlist;
    }


    /**
     * Use this method to set the default settings from GS and then customize them with the values from the UI.
     *
     * @param config Is the JsonBean object from UI.
     * @param spec   Is the current Mondrian Report specification.
     */
    public static void setCustomSettings(SettingsAndFiltersParameters config, ReportSpecificationImpl spec) {
        LinkedHashMap<String, Object> userSettings = (LinkedHashMap<String, Object>) config.getSettings();
        ReportSettingsImpl defaultSettings = ReportUtils.getCurrentUserDefaultSettings();
        defaultSettings.setUnitsOption(AmountsUnits.getDefaultValue());
        if (userSettings.get(SettingsConstants.CURRENCY_ID) != null) {
            defaultSettings.setCurrencyCode(userSettings.get(SettingsConstants.CURRENCY_ID).toString());
        }

        if (userSettings.get(SettingsConstants.CALENDAR_TYPE_ID) != null) {
            defaultSettings.setCalendar(FiscalCalendarUtil.getAmpFiscalCalendar(
                    Long.valueOf(userSettings.get(SettingsConstants.CALENDAR_TYPE_ID).toString())));
        }
        spec.setSettings(defaultSettings);
    }

    /**
     * Generate a smaller version of any number (big or small) by adding a suffix kMBT.
     *
     * @param total
     * @param spec
     * @return
     */
    public static String calculateSumarizedTotals(double total, ReportSpecificationImpl spec) {
        // Convert the number back to units (depending of GS total could be in millions or thousands).
        total = total * spec.getSettings().getUnitsOption().divider;
        String formatted;
        boolean addSufix = false;
        int exp = (int) (Math.log(total) / Math.log(1000));
        if (total < 1000) {
            formatted = String.format("%.1f", total);
        } else {
            addSufix = true;
            // total = Math.round(total / Math.pow(1000, exp));
            total = total / Math.pow(1000, exp);
            formatted = String.format("%.1f", total);
        }
        formatted = formatted.replace('.', spec.getSettings().getCurrencyFormat().getDecimalFormatSymbols()
                .getDecimalSeparator());
        if (formatted.endsWith(spec.getSettings().getCurrencyFormat().getDecimalFormatSymbols().getDecimalSeparator()
                + "0")) {
            formatted = formatted.substring(0, formatted.length() - 2);
        }
        if (addSufix) {
            String currentLang = RequestUtils.getNavigationLanguage(TLSUtils.getRequest()).getCode();
            formatted = formatted + getKMB(currentLang, exp);
        }
        return formatted;
    }

    private static String getKMB(String lang, int exp) {
        String ret = "";
        if (lang.equals("en") || lang.equals("sp")) {
            ret = "kMBTPE".charAt(exp - 1) + "";
        } else if (lang.equals("fr")) {
            switch (exp) {
                case EXP_1:
                    ret = "m";
                    break;
                case EXP_2:
                    ret = "M";
                    break;
                case EXP_3:
                    ret = "MM";
                    break;
                case EXP_4:
                    ret = "T";
                    break;
                case EXP_5:
                    ret = "P";
                    break;
                case EXP_6:
                    ret = "E";
                    break;
                default:
                    ret = "";
                    break;
            }
        }
        return ret;
    }
}
