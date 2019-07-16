package org.digijava.kernel.ampapi.endpoints.dashboards.services;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.dgfoundation.amp.ar.ArConstants;
import org.dgfoundation.amp.ar.ColumnConstants;
import org.dgfoundation.amp.ar.MeasureConstants;
import org.dgfoundation.amp.newreports.AmountCell;
import org.dgfoundation.amp.newreports.AmountsUnits;
import org.dgfoundation.amp.newreports.AmpReportFilters;
import org.dgfoundation.amp.newreports.DateCell;
import org.dgfoundation.amp.newreports.GeneratedReport;
import org.dgfoundation.amp.newreports.GroupingCriteria;
import org.dgfoundation.amp.newreports.IdentifiedReportCell;
import org.dgfoundation.amp.newreports.ReportArea;
import org.dgfoundation.amp.newreports.ReportAreaImpl;
import org.dgfoundation.amp.newreports.ReportCell;
import org.dgfoundation.amp.newreports.ReportCollapsingStrategy;
import org.dgfoundation.amp.newreports.ReportColumn;
import org.dgfoundation.amp.newreports.ReportMeasure;
import org.dgfoundation.amp.newreports.ReportOutputColumn;
import org.dgfoundation.amp.newreports.ReportSettingsImpl;
import org.dgfoundation.amp.newreports.ReportSpecificationImpl;
import org.dgfoundation.amp.newreports.SortingInfo;
import org.dgfoundation.amp.newreports.TextCell;
import org.dgfoundation.amp.nireports.NiReportsEngine;
import org.dgfoundation.amp.nireports.amp.OutputSettings;
import org.dgfoundation.amp.reports.mondrian.MondrianReportUtils;
import org.digijava.kernel.ampapi.endpoints.common.EPConstants;
import org.digijava.kernel.ampapi.endpoints.common.EndpointUtils;
import org.digijava.kernel.ampapi.endpoints.filters.FiltersConstants;
import org.digijava.kernel.ampapi.endpoints.reports.ReportsUtil;
import org.digijava.kernel.ampapi.endpoints.settings.SettingsConstants;
import org.digijava.kernel.ampapi.endpoints.settings.SettingsUtils;
import org.digijava.kernel.ampapi.endpoints.util.DashboardConstants;
import org.digijava.kernel.ampapi.endpoints.util.FilterUtils;
import org.digijava.kernel.ampapi.endpoints.util.JsonBean;
import org.digijava.kernel.ampapi.mondrian.util.MoConstants;
import org.digijava.kernel.translator.TranslatorWorker;
import org.digijava.kernel.util.SiteUtils;
import org.digijava.module.aim.dbentity.AmpCategoryValueLocations;
import org.digijava.module.aim.util.DynLocationManagerUtil;
import org.digijava.module.aim.util.FeaturesUtil;
import org.digijava.module.aim.util.FiscalCalendarUtil;
import org.digijava.module.categorymanager.dbentity.AmpCategoryValue;
import org.digijava.module.categorymanager.util.CategoryConstants;
import org.digijava.module.categorymanager.util.CategoryManagerUtil;

import clover.com.google.common.base.Strings;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

/**
 * 
 * @author Diego Dimunzio
 * 
 */

public class DashboardsService {

    private static Logger logger = Logger.getLogger(DashboardsService.class);
    public static final int RECORDS_PER_PAGE = 50;

    /**
     * Utility method for creating the small objects for the list of tops Note
     * -- I (Phil) hacked this in... it could probably be done better
     * 
     * @param pathId
     *            the id to use in the path for the actual data
     * @param name
     *            the human-readable name for this top
     * @return
     */
    private static JsonBean getTopsListBean(String pathId, String name) {
        JsonBean obj = new JsonBean();
        obj.set("id", pathId);
        obj.set("name", name);
        return obj;
    }

    /**
     * Return a list of the available top __ for the dashboard charts Note -- I
     * (Phil) hacked this in, so it probably could use a review Also, I
     * hard-coded the names ("Donor Agency" etc.) but they should be translated
     * 
     * @return
     */
    public static List<JsonBean> getTopsList() {
        List<JsonBean> tops = new ArrayList<JsonBean>();
        tops.add(getTopsListBean("do", "Donor Agency"));
        tops.add(getTopsListBean("re", "Region"));
        tops.add(getTopsListBean("ps", "Primary Sector"));
        return tops;
    }
    
    /**
     * Return (n) Donors sorted by amount
     * 
     * @param type
     *            (Donor, Regions, Primary Sector)
     * @param n
     * @param config request configuration that stores filters, settings and any other options
     * @return
     */
    public static JsonBean getTops(String type, Integer n, JsonBean config) {
        String err = null;
        JsonBean retlist = new JsonBean();
        String name = "";
        String title = "";
        List<JsonBean> values = new ArrayList<JsonBean>();
        ReportSpecificationImpl spec = new ReportSpecificationImpl("GetTops", ArConstants.DONOR_TYPE);

        Map<String, Object> filters = null;
        if (config != null) {
            filters = (Map<String, Object>) config.get(EPConstants.FILTERS);
        }
        if (filters == null) {
            filters = new LinkedHashMap<>();
        }

        switch (type.toUpperCase()) {
        case "DO":
            if (FeaturesUtil.isVisibleField("Show Names As Acronyms")) {
                spec.addColumn(new ReportColumn(MoConstants.ATTR_ORG_ACRONYM));
            } else {
                spec.addColumn(new ReportColumn(MoConstants.DONOR_AGENCY));
            }
            title = TranslatorWorker.translateText(DashboardConstants.TOP_DONOR_AGENCIES);
            name = DashboardConstants.TOP_DONOR_AGENCIES;
            break;
        case "RO":
            spec.addColumn(new ReportColumn(MoConstants.RESPONSIBLE_AGENCY));
            title = TranslatorWorker.translateText(DashboardConstants.TOP_RESPONSIBLE_ORGS);
            name = DashboardConstants.TOP_RESPONSIBLE_ORGS;
            break;
        case "BA":
            spec.addColumn(new ReportColumn(MoConstants.BENEFICIARY_AGENCY));
            title = TranslatorWorker.translateText(DashboardConstants.TOP_BENEFICIARY_ORGS);
            name = DashboardConstants.TOP_BENEFICIARY_ORGS;
            break;
        case "IA":
            spec.addColumn(new ReportColumn(MoConstants.IMPLEMENTING_AGENCY));
            title = TranslatorWorker.translateText(DashboardConstants.TOP_IMPLEMENTING_ORGS);
            name = DashboardConstants.TOP_IMPLEMENTING_ORGS;
            break;
        case "EA":
            spec.addColumn(new ReportColumn(MoConstants.EXECUTING_AGENCY));
            title = TranslatorWorker.translateText(DashboardConstants.TOP_EXECUTING_ORGS);
            name = DashboardConstants.TOP_EXECUTING_ORGS;
            break;
        case "RE":
            spec.addColumn(new ReportColumn(ColumnConstants.REGION));
            title = TranslatorWorker.translateText(DashboardConstants.TOP_REGIONS);
            name = DashboardConstants.TOP_REGIONS;
            spec.setReportCollapsingStrategy(ReportCollapsingStrategy.NEVER);
            break;
        case "PS":
            spec.addColumn(new ReportColumn(MoConstants.PRIMARY_SECTOR));
            title = TranslatorWorker.translateText(DashboardConstants.TOP_SECTORS);
            name = DashboardConstants.TOP_SECTORS;
            break;
        case "DG":
            spec.addColumn(new ReportColumn(ColumnConstants.DONOR_GROUP));
            title = TranslatorWorker.translateText(DashboardConstants.TOP_DONOR_GROUPS);
            name = DashboardConstants.TOP_DONOR_GROUPS;
            break;
        case "NDD":
            spec.addColumn(new ReportColumn(ColumnConstants.SECONDARY_PROGRAM));
            name = DashboardConstants.PEACE_BUILDING_AND_STATE_BUILDING_GOALS;
            title = TranslatorWorker.translateText(DashboardConstants.PEACE_BUILDING_AND_STATE_BUILDING_GOALS);
            n = 99999; // This chart has no limit of categories (no 'Others').

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
            filters.putAll(peaceFilter);

            break;
        default:
            spec.addColumn(new ReportColumn(MoConstants.DONOR_AGENCY));
            title = TranslatorWorker.translateText(DashboardConstants.TOP_DONOR_AGENCIES);
            name = DashboardConstants.TOP_DONOR_AGENCIES;
            break;
        }
        
        OutputSettings outSettings = new OutputSettings(
                new HashSet<String>() {{add(spec.getColumnNames().iterator().next());}});

        spec.getHierarchies().addAll(spec.getColumns());
        // applies settings, including funding type as a measure
        SettingsUtils.applyExtendedSettings(spec, config);
        spec.addSorter(new SortingInfo(spec.getMeasures().iterator().next(), Boolean.FALSE));

        AmpReportFilters filterRules = FilterUtils.getFilterRules(filters, null);
        if (filterRules != null) {
            spec.setFilters(filterRules);
        }

        // AMP-18740: For dashboards we need to use the default number formatting and leave the rest of the settings
        // configurable (calendar, currency, etc).
        setCustomSettings(config, spec);

        // TODO: add error reporting
        GeneratedReport report = EndpointUtils.runReport(spec, ReportAreaImpl.class, outSettings);
        if (report == null) {
            retlist.set(EPConstants.ERROR, "Could not generate report");
        }
        ReportOutputColumn criteriaCol = report.leafHeaders.get(0);
        ReportOutputColumn valueCol = report.leafHeaders.get(1);

        AmountsUnits unitsOption = spec.getSettings().getUnitsOption();
        
        // Format the report output return a simple list. 
        // this is the report totals, which is not for the top N, but for ALL
        // results
        ReportCell totals = null;
        Double rawTotal = null;
        
        if (report != null && report.reportContents != null && report.reportContents.getContents() != null
                && report.reportContents.getContents().size() > 0) {
            totals = (ReportCell) report.reportContents.getContents().get(valueCol);
            rawTotal = ((BigDecimal) totals.value).doubleValue() * unitsOption.divider; // Save total in units.
            postProcess(report, spec, outSettings, type);
        } else {
            rawTotal = new Double("0");
        }

        String currcode = spec.getSettings().getCurrencyCode();
        retlist.set("currency", currcode);
        Integer maxLimit = report.reportContents.getChildren().size();

        double totalPositive = 0;
        for (ReportArea reportArea: report.reportContents.getChildren()) {
            Map<ReportOutputColumn, ReportCell> content = reportArea.getContents();
            AmountCell ac = (AmountCell) content.get(valueCol);
            double amount = ((BigDecimal) ac.value).doubleValue() * unitsOption.divider;
            if (values.size() < n) {
                JsonBean row = new JsonBean();
                row.set("name", content.get(criteriaCol).displayedValue);
                row.set("id", ((IdentifiedReportCell) content.get(criteriaCol)).entityId);
                row.set("amount", amount);
                row.set("formattedAmount", ac.displayedValue);
                values.add(row);
            }
            if(amount > 0) {
                totalPositive += amount;
            }
        }

        if (SiteUtils.isEffectiveLangRTL()) {
            Collections.reverse(values);
        }

        retlist.set("values", values);

        retlist.set("total", rawTotal);
        retlist.set("sumarizedTotal",
                calculateSumarizedTotals(rawTotal / unitsOption.divider, spec));
        // report the total number of tops available
        retlist.set("maxLimit", maxLimit);
        retlist.set("totalPositive", totalPositive);
        retlist.set("name", name);
        retlist.set("title", title);
        return retlist;
    }
    

    public static int getOffset(JsonBean config) {
        return (Integer) EndpointUtils.getSingleValue(config, "offset", 0);
    }

    public static LinkedHashMap<String, Object> setFilterId(Long id, String column) {
        LinkedHashMap<String, Object> filterObject = new LinkedHashMap<String, Object>();
        List<Long> filterIds = new ArrayList<Long>();
        if (id >0) {
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
            String type) {
        switch (type.toUpperCase()) {
        case "RE": postProcessRE(report, spec, outSettings); break;
        }
    }
    
    /**
     * Replace "Undefined" region with "International", "National" and actual "Undefined" region
     * (this is one of the workaround solutions)   
     * @param report
     * @param spec
     * @param outSettings
     */
    protected static void postProcessRE(GeneratedReport report, ReportSpecificationImpl spec, OutputSettings outSettings) {
        final DecimalFormat formatter = ReportsUtil.getDecimalFormatOrDefault(spec);
        final AmountsUnits amountsUnits = ReportsUtil.getAmountsUnitsOrDefault(spec);
        
        List<ReportArea> undefinedAreas = new ArrayList<>();
        for (ReportArea ra : report.reportContents.getChildren()) {
            // detect those undefined for countries, but skip those that are really undefined for regions
            if (ra.getOwner() != null && ra.getOwner().id < 0 && (ra.getOwner().id != -MoConstants.UNDEFINED_KEY))
                undefinedAreas.add(ra);
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
            String name, long id, Map<Long, String> entitiesIdsValues, List<ReportArea> children) {
        // recreate the cell to have a correct name for the undefined area
        TextCell uRegionCell = new TextCell(TranslatorWorker.translateText(name), id, entitiesIdsValues);
        undefined.getContents().put(regionCol, uRegionCell);
        
        for (ReportArea child : undefined.getChildren()) {
            child.getContents().put(regionCol, uRegionCell);
        }
    }
    
    protected static JSONObject buildEmptyJSon(String...keys) {
        JSONObject obj = new JSONObject();
        for(String key:keys)
            obj.put(key, 0d);
        return obj;
    }


    public static JsonBean getAidPredictability(JsonBean config) throws Exception {
        return getAidPredictability(config, null, null);
    }


    /**
     * 
     * @param config
     * @return
     */
    public static JsonBean getAidPredictability(JsonBean config, String measure, String yearString) {
        JsonBean retlist = new JsonBean();
        ReportSpecificationImpl spec = new ReportSpecificationImpl("GetAidPredictability", ArConstants.DONOR_TYPE);
        LinkedHashMap<String, Object> filters = null;
        if (config != null) {
            filters = (LinkedHashMap<String, Object>) config.get(EPConstants.FILTERS);
        }
        if (filters == null) {
            filters = new LinkedHashMap<>();
        }
        
        SettingsUtils.applySettings(spec, config, true);
        int year = 0;
        if (spec.getSettings() != null && StringUtils.isNotBlank(yearString)) {
            year = spec.getSettings().getCalendar().parseYear(yearString);
        }
        if (year > 0 && !Strings.isNullOrEmpty(measure)) {
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
        } else {
            spec.addColumn(new ReportColumn(ColumnConstants.COUNTRY));
            spec.getHierarchies().add(new ReportColumn(ColumnConstants.COUNTRY));
            spec.addMeasure(new ReportMeasure(MeasureConstants.PLANNED_DISBURSEMENTS));
            spec.addMeasure(new ReportMeasure(MeasureConstants.ACTUAL_DISBURSEMENTS));
            spec.setGroupingCriteria(GroupingCriteria.GROUPING_YEARLY);
        }
        
        if (filters != null) {
            AmpReportFilters filterRules = FilterUtils.getFilterRules(filters, null);
            if (filterRules != null) {
                spec.setFilters(filterRules);
            }
            FilterUtils.updateAllDateFilters(spec);
        }
        
        // AMP-18740: For dashboards we need to use the default number formatting and leave the rest of the settings
        // configurable (calendar, currency, etc).
        setCustomSettings(config, spec);
        
        GeneratedReport report = EndpointUtils.runReport(spec, ReportAreaImpl.class, null);

        if (year > 0 && !Strings.isNullOrEmpty(measure)) {
            return buildPaginateJsonBean(report, getOffset(config));
        }

        //Not only years, we can have values like 'Fiscal calendar 2010-2011', so the Map should be <String,JSONObject>
        Map<String, JSONObject> results = new TreeMap<>(); // accumulator of per-year results
                
        if (report.reportContents.getContents()!=null) {
             for (ReportOutputColumn outputColumn:report.reportContents.getContents().keySet()) {
                // ignore non-funding contents
                if (outputColumn.parentColumn == null) {
                    continue;
                }
                    
                boolean isPlannedColumn = outputColumn.originalColumnName.equals(MeasureConstants.PLANNED_DISBURSEMENTS);
                boolean isTotalColumn = outputColumn.parentColumn != null && outputColumn.parentColumn.originalColumnName.equals(NiReportsEngine.TOTALS_COLUMN_NAME);
                String destination = isPlannedColumn ? "planned disbursements" : "actual disbursements";
                
                String yearValue = isTotalColumn ? "totals" : outputColumn.parentColumn.columnName;
                if (!results.containsKey(yearValue))
                    results.put(yearValue, buildEmptyJSon("planned disbursements", "actual disbursements"));
                JSONObject amountObj = results.get(yearValue);
    
                JSONObject amounts = new JSONObject();
                amounts.put("amount", report.reportContents.getContents().get(outputColumn).value);
                amounts.put("formattedAmount", report.reportContents.getContents().get(outputColumn).displayedValue);
                amountObj.put(destination, amounts);
            }
        }
        JSONArray yearsArray = new JSONArray ();
        for(String yearValue:results.keySet())
            if (!yearValue.equals("totals")) {
                results.get(yearValue).put("year", yearValue);
                yearsArray.add(results.get(yearValue));
            }
        retlist.set("years", yearsArray);
        retlist.set("totals", results.get("totals"));
    
        String currcode = null;
        currcode = spec.getSettings().getCurrencyCode();
        retlist.set("currency", currcode);
        
        retlist.set("name", DashboardConstants.AID_PREDICTABILITY);
        retlist.set("title", TranslatorWorker.translateText(DashboardConstants.AID_PREDICTABILITY));
        retlist.set("measure", "disbursements");
        return retlist;
    }


    public static JsonBean getFundingType(String adjtype, JsonBean config) {
        return getFundingType(adjtype, config, null, null);
    }

    /**
     * Get a list of funding types by year.
     * @param adjtype - Measure Actual commitment, Actual Disbursement, Etc.
     * @param config
     * @return
     */
    public static JsonBean getFundingType(String adjtype, JsonBean config, String yearString, Integer id) {
        JsonBean retlist = new JsonBean();
        
        ReportSpecificationImpl spec = new ReportSpecificationImpl("fundingtype", ArConstants.DONOR_TYPE);
        LinkedHashMap<String, Object> filters = null;
        if (config != null) {
            filters = (LinkedHashMap<String, Object>) config.get(EPConstants.FILTERS);
        }
        if (filters == null) {
            filters = new LinkedHashMap<>();
        }
        
        SettingsUtils.applyExtendedSettings(spec, config);
        
        int year = 0;
        if (spec.getSettings() != null && StringUtils.isNotBlank(yearString)) {
            year = spec.getSettings().getCalendar().parseYear(yearString);
        }
        
        if (year > 0 && id != null && id.intValue() > 0) {
            spec.addColumn(new ReportColumn(ColumnConstants.PROJECT_TITLE));
            spec.addColumn(new ReportColumn(ColumnConstants.ACTIVITY_UPDATED_ON));
            spec.setGroupingCriteria(GroupingCriteria.GROUPING_YEARLY);
            filters.putAll(DashboardsService.setFilterId(id.longValue(), FilterUtils.INSTANCE.idFromColumnName(MoConstants
                    .TYPE_OF_ASSISTANCE)));
            filters.putAll(setFilterYear(year));
            AmpReportFilters filterRules = FilterUtils.getFilterRules(filters, null);
            if (filterRules != null) {
                spec.setFilters(filterRules);
            }
            spec.addSorter(new SortingInfo(new ReportColumn(ColumnConstants.ACTIVITY_UPDATED_ON), false));
        } else {
            spec.setGroupingCriteria(GroupingCriteria.GROUPING_YEARLY);
            spec.addColumn(new ReportColumn(MoConstants.TYPE_OF_ASSISTANCE));
            spec.getHierarchies().addAll(spec.getColumns());
            spec.setSummaryReport(true);

        }
       
        spec.addSorter(new SortingInfo(spec.getMeasures().iterator().next(), false));
        
        if (filters != null) {
            AmpReportFilters filterRules = FilterUtils.getFilterRules(filters, null);
            if (filterRules != null) {
                spec.setFilters(filterRules);
            }
            
            FilterUtils.updateAllDateFilters(spec);
        }
        
        // AMP-18740: For dashboards we need to use the default number formatting and leave the rest of the settings
        // configurable (calendar, currency, etc).
        setCustomSettings(config, spec);
        
        GeneratedReport report = EndpointUtils.runReport(spec, ReportAreaImpl.class, null);

        if (year > 0 && id != null && id.intValue() > 0) {
            return buildPaginateJsonBean(report, getOffset(config));
        } else {
            spec.addSorter(new SortingInfo(spec.getMeasures().iterator().next(), false));
            //Get total
            AmountCell totals = (AmountCell) report.reportContents.getContents().get(report.leafHeaders.get(report.leafHeaders.size() - 1));

            retlist.set("total", totals.value);
            retlist.set("sumarizedTotal", calculateSumarizedTotals(totals.extractValue(), spec));

            String currcode = null;
            currcode = spec.getSettings().getCurrencyCode();
            retlist.set("currency", currcode);

            Map<String, List<JsonBean>> values = new TreeMap<>(); // Map<year, List<type, amount, formattedAmount>>
            ReportOutputColumn toaCol = report.leafHeaders.get(0);
            for (ReportArea toaArea : report.reportContents.getChildren()) {
                String toa = toaArea.getContents().get(toaCol).displayedValue;
                long toaId = toaArea.getOwner().id;
                for (int i = 1; i < report.leafHeaders.size() - 1; i++) {
                    JsonBean toaBean = new JsonBean();
                    toaBean.set("type", toa);
                    toaBean.set("id", toaId);
                    ReportOutputColumn hdr = report.leafHeaders.get(i);
                    //long year = Integer.valueOf(hdr.parentColumn.originalColumnName);
                    String toaYear = hdr.parentColumn.columnName;
                    AmountCell cell = (AmountCell) toaArea.getContents().get(hdr);
                    toaBean.set("amount", cell.extractValue());
                    toaBean.set("formattedAmount", cell.displayedValue);
                    if (!values.containsKey(toaYear))
                        values.put(toaYear, new ArrayList<>());
                    values.get(toaYear).add(toaBean);
                    //values.computeIfAbsent(year, yr -> new ArrayList<>()).add(toaBean);
                }
            }
            List<JsonBean> outValues = new ArrayList<>();
            for (String yearValue : values.keySet()) {
                JsonBean yearBean = new JsonBean();
                yearBean.set("Year", yearValue);
                yearBean.set("values", values.get(yearValue));
                outValues.add(yearBean);
            }
            retlist.set("values", outValues);

            retlist.set("name", DashboardConstants.FUNDING_TYPE);
            retlist.set("title", TranslatorWorker.translateText(DashboardConstants.FUNDING_TYPE));

            return retlist;
        }
    }

    public static JsonBean getPeaceMarkerProjectsByCategory(JsonBean config, Integer id) {
        String err = null;
        JsonBean retlist = new JsonBean();
        List<JsonBean> values = new ArrayList<JsonBean>();

        ReportSpecificationImpl spec = new ReportSpecificationImpl("GetNDD", ArConstants.DONOR_TYPE);
        spec.addColumn(new ReportColumn(ColumnConstants.PROJECT_TITLE));
        spec.addColumn(new ReportColumn(ColumnConstants.ACTIVITY_UPDATED_ON));

        OutputSettings outSettings = new OutputSettings(new HashSet<String>(){{add(ColumnConstants.PROJECT_TITLE);}});
        // applies settings, including funding type as a measure
        SettingsUtils.applyExtendedSettings(spec, config);
        spec.addSorter(new SortingInfo(spec.getMeasures().iterator().next(), false));
        
        LinkedHashMap<String, Object> filters = null;
        if (config != null) {
            filters = (LinkedHashMap<String, Object>) config.get(EPConstants.FILTERS);
        }
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

    public static JsonBean buildPaginateJsonBean(GeneratedReport report, int offset) {
        JsonBean retlist = new JsonBean();
        List<JsonBean> values = new ArrayList<JsonBean>();
        ReportOutputColumn titleCol = report.leafHeaders.get(0);
        ReportOutputColumn dateCol = report.leafHeaders.get(1);
        ReportOutputColumn amountCol = report.leafHeaders.get(2);

        report.reportContents.getChildren().stream().limit(offset + RECORDS_PER_PAGE).forEach(
                n -> {
                    JsonBean row = new JsonBean();
                    IdentifiedReportCell title = (IdentifiedReportCell) n.getContents().get(titleCol);
                    AmountCell amount = (AmountCell) n.getContents().get(amountCol);
                    DateCell date = (DateCell) n.getContents().get(dateCol);
                    
                    if (title != null) {
                        row.set("name", title.displayedValue);
                        row.set("id", title.entityId);
                    }
                    
                    if (amount != null) {
                        row.set("amount", amount.value);
                        row.set("formattedAmount", amount.displayedValue);
                    }
                    
                    if (date != null) {
                        row.set("date", date.displayedValue);
                    }
                    values.add(row);
                }
        );

        retlist.set("totalRecords", report.reportContents.getChildren().size());
        retlist.set("values", values);

        return retlist;
    }


    /**
     * Use this method to set the default settings from GS and then customize them with the values from the UI.
     * @param config Is the JsonBean object from UI.
     * @param spec Is the current Mondrian Report specification.
     */
    public static void setCustomSettings(JsonBean config, ReportSpecificationImpl spec) {
        LinkedHashMap<String, Object> userSettings = (LinkedHashMap<String, Object>) config.get("settings");
        ReportSettingsImpl defaultSettings = MondrianReportUtils.getCurrentUserDefaultSettings();
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
            formatted = formatted + "kMBTPE".charAt(exp - 1);
        }
        return formatted;
    }
}
