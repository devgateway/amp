package org.dgfoundation.amp.gpi.reports;

import org.apache.commons.lang3.StringUtils;
import org.dgfoundation.amp.ar.ArConstants;
import org.dgfoundation.amp.ar.ColumnConstants;
import org.dgfoundation.amp.ar.MeasureConstants;
import org.dgfoundation.amp.newreports.*;
import org.dgfoundation.amp.newreports.ReportElement.ElementType;
import org.dgfoundation.amp.reports.ReportUtils;
import org.digijava.kernel.ampapi.endpoints.common.EndpointUtils;
import org.digijava.kernel.ampapi.endpoints.filters.FiltersConstants;
import org.digijava.kernel.ampapi.endpoints.gpi.GPIDataService;
import org.digijava.kernel.ampapi.endpoints.gpi.GpiFormParameters;
import org.digijava.kernel.ampapi.endpoints.settings.SettingsUtils;
import org.digijava.kernel.ampapi.endpoints.util.DateFilterUtils;
import org.digijava.kernel.ampapi.endpoints.util.FilterUtils;
import org.digijava.kernel.ampapi.exception.AmpApiException;
import org.digijava.module.aim.dbentity.AmpFiscalCalendar;
import org.digijava.module.aim.helper.GlobalSettingsConstants;
import org.digijava.module.aim.helper.fiscalcalendar.BaseCalendar;
import org.digijava.module.aim.util.FeaturesUtil;
import org.digijava.module.aim.util.FiscalCalendarUtil;
import org.digijava.module.common.util.DateTimeUtil;
import org.joda.time.DateTime;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.*;
import java.util.Map.Entry;
import java.util.stream.Collectors;

public class GPIReportUtils {
    
    private static final int MTEF_YEARS_SIZE = 3;

    /**
     * 
     * @param indicatorCode
     * @param formParams
     * @return generatedRerport {@link GeneratedReport}
     */
    public static GeneratedReport getGeneratedReportForIndicator(String indicatorCode,
            GpiFormParameters formParams) {

        switch (indicatorCode) {
            case GPIReportConstants.REPORT_1:
                return getGeneratedReportForIndicator1(formParams);
            case GPIReportConstants.REPORT_5a:
                return getGeneratedReportForIndicator5a(formParams);
            case GPIReportConstants.REPORT_5b:
                return getGeneratedReportForIndicator5b(formParams);
            case GPIReportConstants.REPORT_6:
                return getGeneratedReportForIndicator6(formParams);
            case GPIReportConstants.REPORT_9b:
                return getGeneratedReportForIndicator9b(formParams);
            default:
                throw new RuntimeException("Wrong indicator code:" + indicatorCode);
        }
    }
    
    /**
     * Create the template for the GPI report 1
     * 
     * @param formParams
     * @return generatedReport 
     */
    public static GeneratedReport getGeneratedReportForIndicator1(GpiFormParameters formParams) {
        if (EndpointUtils.getSingleValue(formParams.getOutput(), 1) == 2) {
            return getGeneratedReportForIndicator1Output2(formParams);
        }
        
        return getGeneratedReportForIndicator1Output1(formParams);
    }
    
    /**
     * Create the template for the GPI report 1 Output 1
     * 
     * @param formParams
     * @return generatedReport 
     */
    public static GeneratedReport getGeneratedReportForIndicator1Output1(GpiFormParameters formParams) {

        ReportSpecificationImpl spec = new ReportSpecificationImpl(GPIReportConstants.REPORT_1, ArConstants.DONOR_TYPE);

        spec.addColumn(new ReportColumn(ColumnConstants.PROJECT_TITLE));
        spec.addColumn(new ReportColumn(ColumnConstants.DONOR_AGENCY));
        spec.addColumn(new ReportColumn(ColumnConstants.ACTUAL_APPROVAL_DATE));
        spec.addColumn(new ReportColumn(ColumnConstants.IMPLEMENTING_AGENCY));
        spec.addColumn(new ReportColumn(ColumnConstants.IMPLEMENTING_AGENCY_GROUPS));
        spec.addColumn(new ReportColumn(ColumnConstants.PRIMARY_SECTOR));
        spec.addColumn(new ReportColumn(ColumnConstants.PRIMARY_PROGRAM));
        spec.addColumn(new ReportColumn(ColumnConstants.FINANCING_INSTRUMENT));
        spec.addColumn(new ReportColumn(ColumnConstants.GPI_1_Q6));
        spec.addColumn(new ReportColumn(ColumnConstants.GPI_1_Q6_DESCRIPTION));
        spec.addColumn(new ReportColumn(ColumnConstants.GPI_1_Q7));
        spec.addColumn(new ReportColumn(ColumnConstants.GPI_1_Q8));
        spec.addColumn(new ReportColumn(ColumnConstants.GPI_1_Q9));
        spec.addColumn(new ReportColumn(ColumnConstants.GPI_1_Q10));
        spec.addColumn(new ReportColumn(ColumnConstants.GPI_1_Q10_DESCRIPTION));
        
        spec.addMeasure(new ReportMeasure(MeasureConstants.ACTUAL_COMMITMENTS));
        spec.addMeasure(new ReportMeasure(MeasureConstants.PLANNED_COMMITMENTS));
        spec.addMeasure(new ReportMeasure(MeasureConstants.PIPELINE_COMMITMENTS));
        spec.addMeasure(new ReportMeasure(MeasureConstants.ACTUAL_DISBURSEMENTS));
        spec.addMeasure(new ReportMeasure(MeasureConstants.PLANNED_DISBURSEMENTS));
        spec.addMeasure(new ReportMeasure(MeasureConstants.ACTUAL_EXPENDITURES));
        spec.addMeasure(new ReportMeasure(MeasureConstants.PLANNED_EXPENDITURES));
        spec.addMeasure(new ReportMeasure(MeasureConstants.ACTUAL_DISBURSEMENT_ORDERS));
        spec.addMeasure(new ReportMeasure(MeasureConstants.PLANNED_DISBURSEMENT_ORDERS));
        spec.addMeasure(new ReportMeasure(MeasureConstants.ACTUAL_ESTIMATED_DISBURSEMENTS));
        spec.addMeasure(new ReportMeasure(MeasureConstants.PLANNED_ESTIMATED_DISBURSEMENTS));
        spec.addMeasure(new ReportMeasure(MeasureConstants.ANNUAL_PROPOSED_PROJECT_COST));
        
        spec.getHierarchies().add(new ReportColumn(ColumnConstants.PROJECT_TITLE));
        spec.getHierarchies().add(new ReportColumn(ColumnConstants.DONOR_AGENCY));
        
        applyAppovalStatusFilter(formParams, spec);
        removeDonorAgencyFilterRule(spec);
        applySettings(formParams, spec);
        FilterUtils.updateAllDateFilters(spec);
        clearYearRangeSettings(spec);

        GeneratedReport generatedReport = EndpointUtils.runReport(spec, ReportAreaImpl.class, null);

        return generatedReport;
    }

    /**
     * Create the template for the GPI report 1 Output 2
     * 
     * @param formParams
     * @return generatedReport 
     */
    public static GeneratedReport getGeneratedReportForIndicator1Output2(GpiFormParameters formParams) {

        ReportSpecificationImpl spec = new ReportSpecificationImpl(GPIReportConstants.REPORT_1, ArConstants.DONOR_TYPE);

        spec.addColumn(new ReportColumn(ColumnConstants.PROJECT_TITLE));
        spec.addColumn(new ReportColumn(ColumnConstants.DONOR_AGENCY));
        spec.addColumn(new ReportColumn(ColumnConstants.ACTUAL_APPROVAL_DATE));
        spec.addColumn(new ReportColumn(ColumnConstants.GPI_1_Q6));
        spec.addColumn(new ReportColumn(ColumnConstants.GPI_1_Q7));
        spec.addColumn(new ReportColumn(ColumnConstants.GPI_1_Q8));
        spec.addColumn(new ReportColumn(ColumnConstants.GPI_1_Q9));
        spec.addColumn(new ReportColumn(ColumnConstants.GPI_1_Q10));
        
        spec.addMeasure(new ReportMeasure(MeasureConstants.ACTUAL_COMMITMENTS));
        spec.addMeasure(new ReportMeasure(MeasureConstants.PLANNED_COMMITMENTS));
        spec.addMeasure(new ReportMeasure(MeasureConstants.PIPELINE_COMMITMENTS));
        spec.addMeasure(new ReportMeasure(MeasureConstants.ACTUAL_DISBURSEMENTS));
        spec.addMeasure(new ReportMeasure(MeasureConstants.PLANNED_DISBURSEMENTS));
        spec.addMeasure(new ReportMeasure(MeasureConstants.ACTUAL_EXPENDITURES));
        spec.addMeasure(new ReportMeasure(MeasureConstants.PLANNED_EXPENDITURES));
        spec.addMeasure(new ReportMeasure(MeasureConstants.ACTUAL_DISBURSEMENT_ORDERS));
        spec.addMeasure(new ReportMeasure(MeasureConstants.PLANNED_DISBURSEMENT_ORDERS));
        spec.addMeasure(new ReportMeasure(MeasureConstants.ACTUAL_ESTIMATED_DISBURSEMENTS));
        spec.addMeasure(new ReportMeasure(MeasureConstants.PLANNED_ESTIMATED_DISBURSEMENTS));
        spec.addMeasure(new ReportMeasure(MeasureConstants.ANNUAL_PROPOSED_PROJECT_COST));
        
        spec.getHierarchies().add(new ReportColumn(ColumnConstants.PROJECT_TITLE));
        spec.getHierarchies().add(new ReportColumn(ColumnConstants.DONOR_AGENCY));
        
        applyAppovalStatusFilter(formParams, spec);
        applySettings(formParams, spec);
        FilterUtils.updateAllDateFilters(spec);
        clearYearRangeSettings(spec);

        GeneratedReport generatedReport = EndpointUtils.runReport(spec, ReportAreaImpl.class, null);

        return generatedReport;
    }

    /**
     * Create the template for the GPI report 5a
     * 
     * @param formParams
     * @return generatedReport 
     */
    public static GeneratedReport getGeneratedReportForIndicator5a(GpiFormParameters formParams) {

        ReportSpecificationImpl spec = new ReportSpecificationImpl(GPIReportConstants.REPORT_5a,
                ArConstants.DONOR_TYPE);

        if (isDonorAgency(formParams)) {
            spec.addColumn(new ReportColumn(ColumnConstants.DONOR_AGENCY));
            spec.getHierarchies().add(new ReportColumn(ColumnConstants.DONOR_AGENCY));
        } else {
            spec.addColumn(new ReportColumn(ColumnConstants.DONOR_GROUP));
            spec.getHierarchies().add(new ReportColumn(ColumnConstants.DONOR_GROUP));
        }
        
        spec.addColumn(new ReportColumn(ColumnConstants.ACTIVITY_BUDGET));
        spec.addColumn(new ReportColumn(ColumnConstants.HAS_EXECUTING_AGENCY));
        
        spec.getHierarchies().add(new ReportColumn(ColumnConstants.ACTIVITY_BUDGET));
        spec.getHierarchies().add(new ReportColumn(ColumnConstants.HAS_EXECUTING_AGENCY));

        spec.addMeasure(new ReportMeasure(MeasureConstants.ACTUAL_DISBURSEMENTS));
        spec.addMeasure(new ReportMeasure(MeasureConstants.PLANNED_DISBURSEMENTS));
        spec.addMeasure(new ReportMeasure(MeasureConstants.DISBURSED_AS_SCHEDULED));
        spec.addMeasure(new ReportMeasure(MeasureConstants.OVER_DISBURSED));
        spec.setGroupingCriteria(GroupingCriteria.GROUPING_YEARLY);
        spec.setSummaryReport(true);

        applyAppovalStatusFilter(formParams, spec);
        applySettings(formParams, spec);
        FilterUtils.updateAllDateFilters(spec);
        clearYearRangeSettings(spec);

        GeneratedReport generatedReport = EndpointUtils.runReport(spec, ReportAreaImpl.class, null);

        return generatedReport;
    }

    /**
     * Create the template for the GPI report 5b
     * 
     * @param formParams
     * @return generatedReport 
     */
    public static GeneratedReport getGeneratedReportForIndicator5b(GpiFormParameters formParams) {

        ReportSpecificationImpl spec = new ReportSpecificationImpl(GPIReportConstants.REPORT_5b,
                ArConstants.DONOR_TYPE);

        if (isDonorAgency(formParams)) {
            spec.addColumn(new ReportColumn(ColumnConstants.DONOR_AGENCY));
            spec.getHierarchies().add(new ReportColumn(ColumnConstants.DONOR_AGENCY));
        } else {
            spec.addColumn(new ReportColumn(ColumnConstants.DONOR_GROUP));
            spec.getHierarchies().add(new ReportColumn(ColumnConstants.DONOR_GROUP));
        }

        spec.setSummaryReport(true);

        applyAppovalStatusFilter(formParams, spec);
        applySettings(formParams, spec);
        clearYearRangeSettings(spec);
        
        if (FeaturesUtil.getGlobalSettingValueBoolean(GlobalSettingsConstants.MTEF_ANNUAL_DATE_FORMAT)) {
            for (String mtefColumn : getMTEFColumnsForIndicator5b(spec)) {
                spec.addColumn(new ReportColumn(mtefColumn));
            }
        } else {
            spec.addMeasure(new ReportMeasure(MeasureConstants.MTEF));
            spec.addMeasure(new ReportMeasure(MeasureConstants.ACTUAL_COMMITMENTS));
            spec.addMeasure(new ReportMeasure(MeasureConstants.PLANNED_COMMITMENTS));
            spec.addMeasure(new ReportMeasure(MeasureConstants.PIPELINE_COMMITMENTS));
            spec.addMeasure(new ReportMeasure(MeasureConstants.ACTUAL_DISBURSEMENTS));
            spec.addMeasure(new ReportMeasure(MeasureConstants.PLANNED_DISBURSEMENTS));
            spec.addMeasure(new ReportMeasure(MeasureConstants.ACTUAL_EXPENDITURES));
            spec.addMeasure(new ReportMeasure(MeasureConstants.PLANNED_EXPENDITURES));
            spec.addMeasure(new ReportMeasure(MeasureConstants.ACTUAL_DISBURSEMENT_ORDERS));
            spec.addMeasure(new ReportMeasure(MeasureConstants.PLANNED_DISBURSEMENT_ORDERS));
            spec.addMeasure(new ReportMeasure(MeasureConstants.ACTUAL_ESTIMATED_DISBURSEMENTS));
            spec.addMeasure(new ReportMeasure(MeasureConstants.PLANNED_ESTIMATED_DISBURSEMENTS));
            spec.addMeasure(new ReportMeasure(MeasureConstants.ANNUAL_PROPOSED_PROJECT_COST));
            spec.setGroupingCriteria(GroupingCriteria.GROUPING_YEARLY);
            updateDateFilterForMTEFYears(spec);
            FilterUtils.updateAllDateFilters(spec);
        }

        GeneratedReport generatedReport = EndpointUtils.runReport(spec, ReportAreaImpl.class, null);

        return generatedReport;
    }
    
    /**
     * Create the template for the GPI report 5b
     * 
     * @param formParams
     * @return generatedReport 
     */
    public static GeneratedReport getGeneratedReportForIndicator5bActDisb(GpiFormParameters formParams) {

        ReportSpecificationImpl spec = new ReportSpecificationImpl(GPIReportConstants.REPORT_5b + " measures",
                ArConstants.DONOR_TYPE);

        if (isDonorAgency(formParams)) {
            spec.addColumn(new ReportColumn(ColumnConstants.DONOR_AGENCY));
            spec.getHierarchies().add(new ReportColumn(ColumnConstants.DONOR_AGENCY));
        } else {
            spec.addColumn(new ReportColumn(ColumnConstants.DONOR_GROUP));
            spec.getHierarchies().add(new ReportColumn(ColumnConstants.DONOR_GROUP));
        }
        
        spec.addMeasure(new ReportMeasure(MeasureConstants.ACTUAL_COMMITMENTS));
        spec.addMeasure(new ReportMeasure(MeasureConstants.PLANNED_COMMITMENTS));
        spec.addMeasure(new ReportMeasure(MeasureConstants.PIPELINE_COMMITMENTS));
        spec.addMeasure(new ReportMeasure(MeasureConstants.ACTUAL_DISBURSEMENTS));
        spec.addMeasure(new ReportMeasure(MeasureConstants.PLANNED_DISBURSEMENTS));
        spec.addMeasure(new ReportMeasure(MeasureConstants.ACTUAL_EXPENDITURES));
        spec.addMeasure(new ReportMeasure(MeasureConstants.PLANNED_EXPENDITURES));
        spec.addMeasure(new ReportMeasure(MeasureConstants.ACTUAL_DISBURSEMENT_ORDERS));
        spec.addMeasure(new ReportMeasure(MeasureConstants.PLANNED_DISBURSEMENT_ORDERS));
        spec.addMeasure(new ReportMeasure(MeasureConstants.ACTUAL_ESTIMATED_DISBURSEMENTS));
        spec.addMeasure(new ReportMeasure(MeasureConstants.PLANNED_ESTIMATED_DISBURSEMENTS));
        spec.addMeasure(new ReportMeasure(MeasureConstants.ANNUAL_PROPOSED_PROJECT_COST));

        spec.setSummaryReport(true);

        applyAppovalStatusFilter(formParams, spec);
        applySettings(formParams, spec);
        clearYearRangeSettings(spec);
        
        if (!FeaturesUtil.getGlobalSettingValueBoolean(GlobalSettingsConstants.MTEF_ANNUAL_DATE_FORMAT)) {
            FilterUtils.updateAllDateFilters(spec);
        }

        GeneratedReport generatedReport = EndpointUtils.runReport(spec, ReportAreaImpl.class, null);

        return generatedReport;
    }

    /**
     * Create the template for the GPI report 6
     * 
     * @param formParams
     * @return generatedReport 
     */
    public static GeneratedReport getGeneratedReportForIndicator6(GpiFormParameters formParams) {

        ReportSpecificationImpl spec = new ReportSpecificationImpl(GPIReportConstants.REPORT_6, ArConstants.DONOR_TYPE);

        if (isDonorAgency(formParams)) {
            spec.addColumn(new ReportColumn(ColumnConstants.DONOR_AGENCY));
            spec.getHierarchies().add(new ReportColumn(ColumnConstants.DONOR_AGENCY));
        } else {
            spec.addColumn(new ReportColumn(ColumnConstants.DONOR_GROUP));
            spec.getHierarchies().add(new ReportColumn(ColumnConstants.DONOR_GROUP));
        }
        
        spec.setGroupingCriteria(GroupingCriteria.GROUPING_YEARLY);
        spec.addMeasure(new ReportMeasure(MeasureConstants.PLANNED_DISBURSEMENTS));
        spec.setSummaryReport(true);

        applyAppovalStatusFilter(formParams, spec);
        applySettings(formParams, spec);
        FilterUtils.updateAllDateFilters(spec);
        clearYearRangeSettings(spec);
        
        GeneratedReport generatedReport = EndpointUtils.runReport(spec, ReportAreaImpl.class, null);

        return generatedReport;
    }

    /**
     * Create the template for the GPI report 9b.
     * 
     * @param formParams
     * @return generatedReport 
     */
    public static GeneratedReport getGeneratedReportForIndicator9b(GpiFormParameters formParams) {

        ReportSpecificationImpl spec = new ReportSpecificationImpl(GPIReportConstants.REPORT_9b, ArConstants.GPI_TYPE);

        if (isDonorAgency(formParams)) {
            spec.addColumn(new ReportColumn(ColumnConstants.DONOR_AGENCY));
            spec.getHierarchies().add(new ReportColumn(ColumnConstants.DONOR_AGENCY));
        } else {
            spec.addColumn(new ReportColumn(ColumnConstants.DONOR_GROUP));
            spec.getHierarchies().add(new ReportColumn(ColumnConstants.DONOR_GROUP));
        }
        
        spec.setGroupingCriteria(GroupingCriteria.GROUPING_YEARLY);
        spec.addMeasure(new ReportMeasure(MeasureConstants.NATIONAL_BUDGET_EXECUTION_PROCEDURES));
        spec.addMeasure(new ReportMeasure(MeasureConstants.NATIONAL_FINANCIAL_REPORTING_PROCEDURES));
        spec.addMeasure(new ReportMeasure(MeasureConstants.NATIONAL_AUDITING_PROCEDURES));
        spec.addMeasure(new ReportMeasure(MeasureConstants.NATIONAL_PROCUREMENT_EXECUTION_PROCEDURES));
        spec.setSummaryReport(true);

        applyAppovalStatusFilter(formParams, spec);
        applySettings(formParams, spec);
        FilterUtils.updateAllDateFilters(spec);
        clearYearRangeSettings(spec);

        GeneratedReport generatedReport = EndpointUtils.runReport(spec, ReportAreaImpl.class, null);

        return generatedReport;
    }

    public static String getHierarchyColumn(GpiFormParameters formParams) {
        if (formParams.getHierarchy() != null) {
            return formParams.getHierarchy();
        }

        return GPIReportConstants.HIERARCHY_DONOR_AGENCY;
    }
    
    public static boolean isDonorAgency(GpiFormParameters formParams) {
        String donorHierarchy = getHierarchyColumn(formParams);
        
        return !GPIReportConstants.HIERARCHY_DONOR_GROUP.equals(donorHierarchy);
    }
    
    /**
     * @param formParams
     * @param spec
     */
    public static void applyAppovalStatusFilter(GpiFormParameters formParams, ReportSpecificationImpl spec) {
        if (formParams != null) {
            Map<String, Object> filters = formParams.getFilters();
            
            AmpReportFilters filterRules = FilterUtils.getFilterRules(filters, null);

            if (filterRules == null) {
                filterRules = new AmpReportFilters();
            }

            ReportElement elem = new ReportElement(new ReportColumn(ColumnConstants.APPROVAL_STATUS));

            // Validated Activities - 4
            FilterRule filterRule = new FilterRule(Arrays.asList("4"), true);
            filterRules.addFilterRule(elem, filterRule);

            spec.setFilters(filterRules);
        }
    }

    /**
     * Update date filter in order to retrieve data from the other MTEF years
     * @param spec
     */
    private static void updateDateFilterForMTEFYears(ReportSpecificationImpl spec) {
            // update date filter
            Optional<Entry<ReportElement, FilterRule>> dateRuleEntry = spec.getFilters()
                    .getFilterRules().entrySet().stream()
                    .filter(entry -> entry.getKey().type.equals(ElementType.DATE))
                    .filter(entry -> entry.getKey().entity == null)
                    .findAny();
            
            if (dateRuleEntry.isPresent() && dateRuleEntry.get().getValue() != null) {
                FilterRule dateFilterRule = dateRuleEntry.get().getValue();
                FilterRule mtefDateFilterRule = addYearsToDateFilterRule(dateFilterRule, MTEF_YEARS_SIZE);
                dateRuleEntry.get().setValue(mtefDateFilterRule);
            }
    }

    /**
     * 
     * @param gregFilterRule
     * @param years
     * @return
     */
    private static FilterRule addYearsToDateFilterRule(FilterRule gregFilterRule, int years) {
        FilterRule mtefYearRule = null;

        Date gregStart = gregFilterRule.min == null ? null : DateTimeUtil.fromJulianNumberToDate(gregFilterRule.min);
        Date gregEnd = gregFilterRule.max == null ? null : DateTimeUtil.fromJulianNumberToDate(gregFilterRule.max);
        
        Calendar c = Calendar.getInstance();
        c.setTime(gregEnd);
        c.add(Calendar.YEAR, years);
        
        try {
            mtefYearRule = DateFilterUtils.getDatesRangeFilterRule(ElementType.DATE,
                    DateTimeUtil.toJulianDayNumber(gregStart), DateTimeUtil.toJulianDayNumber(c.getTime()),
                    DateTimeUtil.formatDateOrNull(gregStart), DateTimeUtil.formatDateOrNull(c.getTime()), false);
        } catch (AmpApiException e) {
            throw new RuntimeException(e);
        }

        return mtefYearRule;
    }

    /**
     * Apply settings on report specifications
     * 
     * @param formParams
     * @param spec
     */
    public static void applySettings(GpiFormParameters formParams, ReportSpecificationImpl spec) {
        spec.setSettings(ReportUtils.getCurrentUserDefaultSettings());
        spec.getSettings().getCurrencyFormat().setMinimumFractionDigits(0);
        spec.getSettings().getCurrencyFormat().setMinimumIntegerDigits(1);
        
        SettingsUtils.applySettings(spec, formParams.getSettings(), true);
    }

    /**
     * @param spec
     */
    public static void clearYearRangeSettings(ReportSpecificationImpl spec) {
        ReportSettingsImpl reportSettings = (ReportSettingsImpl) spec.getSettings();
        reportSettings.setYearRangeFilter(null);
    }

    public static List<String> getMTEFColumnsForIndicator5b(ReportSpecification spec) {
        
        FilterRule dateFilterRule = getDateFilterRule(spec);
        if (dateFilterRule == null || StringUtils.isBlank(dateFilterRule.min)) {
            throw new RuntimeException("No year selected. Please specify the date filter");
        }
        
        Date fromJulianNumberToDate = DateTimeUtil.fromJulianNumberToDate(dateFilterRule.min);
        int year = FilterUtils.getYearFromDate(fromJulianNumberToDate);

        List<String> mtefColumns = new ArrayList<>();
        for (int i = 1; i <= 3; i++) {
            mtefColumns.add("MTEF " + (year + i));
        }

        return mtefColumns;
    }

    /**
     * we get the pivot year from filters until AMP-27540 is fixed
     * @param formParams
     * @return
     */
    public static Integer getPivotYearFromFormParams(GpiFormParameters formParams) {
        Integer pivotYear = null;
        Map<String, Object> filters = formParams.getFilters();
        if (filters != null) {
            Map<String, Object> date = (Map<String, Object>) filters.get(FiltersConstants.DATE);
            if (date != null) {
                String start = String.valueOf(date.get("start"));
                SimpleDateFormat sdf = new SimpleDateFormat(FiltersConstants.DATE_FORMAT);
                try {
                    Date startDate = start == null ? null : sdf.parse(start);
                    pivotYear = FilterUtils.getYearFromDate(startDate);
                } catch (java.text.ParseException ex) {
                    throw new RuntimeException("No year selected. Please specify the date filter");
                }
            }
        }
        return pivotYear;
    }

    public static int getPivoteYear(ReportSpecification spec) {
        
        FilterRule dateFilterRule = getDateFilterRule(spec);
        Date fromJulianNumberToDate;
        
        if (dateFilterRule == null || StringUtils.isBlank(dateFilterRule.min)) {
            throw new RuntimeException("No year selected. Please specify the date filter");
        }  else {
            fromJulianNumberToDate = DateTimeUtil.fromJulianNumberToDate(dateFilterRule.min);
        }
        return getYearOfCustomCalendar(spec, fromJulianNumberToDate);
    }

    public static FilterRule getDateFilterRule(ReportSpecification spec) {
        Optional<Entry<ReportElement, FilterRule>> dateRuleEntry = spec.getFilters()
                .getFilterRules().entrySet().stream()
                .filter(entry -> entry.getKey().type.equals(ElementType.DATE))
                .filter(entry -> entry.getKey().entity == null)
                .findAny();
        
        FilterRule dateRule = dateRuleEntry.isPresent() ? dateRuleEntry.get().getValue() : null;
        
        return dateRule;
    }
    
    /**
     * @param spec
     */
    private static void removeDonorAgencyFilterRule(ReportSpecificationImpl spec) {
        ReportElement donorAgencyRuleElement = getFilterRuleElement(spec.getFilters().getAllFilterRules(), 
                ColumnConstants.DONOR_AGENCY);
        
        if (donorAgencyRuleElement != null) {
            spec.getFilters().getAllFilterRules().remove(donorAgencyRuleElement);
        }
    }
    
    public static FilterRule getFilterRule(GpiFormParameters formParams, String columnName) {
        FilterRule filterRule = null;
        if (formParams != null) {
            Map<String, Object> filters = formParams.getFilters();

            AmpReportFilters filterRules = FilterUtils.getFilterRules(filters, null);
            if (filterRules != null) {
                ReportElement ruleElement = getFilterRuleElement(filterRules.getAllFilterRules(), columnName);
                filterRule = filterRules.getAllFilterRules().get(ruleElement);
            }
        }

        return filterRule;
    }
    
    public static ReportElement getFilterRuleElement(Map<ReportElement, FilterRule> filterRules, String column) {

        Optional<Entry<ReportElement, FilterRule>> dateRuleEntry = filterRules.entrySet().stream()
                .filter(entry -> entry.getKey() != null && entry.getKey().entity != null)
                .filter(entry -> column.equals(entry.getKey().entity.getEntityName()))
                .findAny();

        ReportElement reportElement = dateRuleEntry.isPresent() ? dateRuleEntry.get().getKey() : null;

        return reportElement;
    }
    
    /**
     * Get the converted year of the julidanDateNumber using the calendar from the report spec
     *
     * @param spec
     * @param julianDateNumber
     * @return
     */
    public static int getYearOfCustomCalendar(ReportSpecification spec, long julianDateNumber) {
        Date date = DateTimeUtil.fromJulianNumberToDate(Long.toString(julianDateNumber));

        return getYearOfCustomCalendar(spec, date);
    }
    public static int getYearOfCustomCalendar(ReportSpecification spec, Date date) {

        CalendarConverter calendarConverter = spec.getSettings().getCalendar();
        if (calendarConverter != null && calendarConverter instanceof AmpFiscalCalendar) {
            AmpFiscalCalendar calendar = (AmpFiscalCalendar) calendarConverter;
            DateTime convDate = FiscalCalendarUtil.convertFromGregorianDate(date, calendar);
            
            return convDate.getYear();
        }
        return FilterUtils.getYearFromDate(date);

    }

    /**
     * Get GPI Remarks for indicator 5a exports (pdf and xlsx)
     * 
     * @param rowData
     * @return remarks as string (joined by '\n')
     */ 
    public static String getRemarksForIndicator5a(Map<GPIReportOutputColumn, String> rowData) {
        String year = "";
        boolean isDonorAgency = true;
        List<Long> donorIds = new ArrayList<Long>();
        
        for (Map.Entry<GPIReportOutputColumn, String> e : rowData.entrySet()) {
            GPIReportOutputColumn column = e.getKey();
            if (column.originalColumnName.equals(ColumnConstants.DONOR_GROUP)) {
                isDonorAgency = false;
            }
            if (column.originalColumnName.equals(GPIReportConstants.COLUMN_YEAR)) {
                year = rowData.get(column);
            }
            if (column.originalColumnName.equals(ColumnConstants.DONOR_ID)) {
                donorIds.add(Long.valueOf(rowData.get(column)));
            }
        };
        
        String donorType = isDonorAgency ? GPIReportConstants.HIERARCHY_DONOR_AGENCY
                : GPIReportConstants.HIERARCHY_DONOR_GROUP;

        int y = Integer.parseInt(year);
        Long from = Long.valueOf(DateTimeUtil.toJulianDayNumber(LocalDate.ofYearDay(y, 1)));
        Long to = Long.valueOf(DateTimeUtil.toJulianDayNumber(LocalDate.ofYearDay(y + 1, 1)));

        List<GPIRemark> remarks = GPIDataService.getGPIRemarks(GPIReportConstants.REPORT_5a, 
                            donorIds, donorType, from, to);
        
        List<String> remarksAsStringList = remarks.stream()
                .map(remark -> String.format("%s : %s", remark.getDate(), remark.getRemark()))
                .collect(Collectors.toList());
        
        
        return String.join("\n", remarksAsStringList);
    }
    
    /**
     * Get GPI Remarks for indicator 1 exports (pdf and xlsx)
     * 
     * @param report
     * @return remarks
     */
    public static List<GPIRemark> getRemarksForIndicator1(GPIReport report) {
        String donorType = GPIReportConstants.HIERARCHY_DONOR_AGENCY;

        FilterRule donorAgencyRule = GPIReportUtils.getFilterRule(report.getOriginalFormParams(), 
                ColumnConstants.DONOR_AGENCY);
        List<Long> ids = donorAgencyRule == null ? new ArrayList<>()
                : donorAgencyRule.values.stream().map(s -> Long.parseLong(s)).collect(Collectors.toList());
        
        FilterRule aprDateRule = GPIReportUtils.getFilterRule(report.getOriginalFormParams(), 
                ColumnConstants.ACTUAL_APPROVAL_DATE);
        
        Long min = aprDateRule == null ? 0L : aprDateRule.min != null ? Long.parseLong(aprDateRule.min) : 0L;
        Long max = aprDateRule == null ? 0L : aprDateRule.max != null ? Long.parseLong(aprDateRule.max) : 0L;
        
        List<GPIRemark> remarks = GPIDataService.getGPIRemarks(GPIReportConstants.REPORT_1, ids, donorType, min, max);
        
        return remarks;
    }
    
    /**
     * Get the approval date in format MM/yyyy. If the date is in ethiopian calendar, 
     * it is in the format dd/MM/yyyy (see @NiReportDateFormatter.getEthiopianFormattedDate())
     * 
     * @param dateAsString
     * @param calendarConverter
     * @return approvalDate in format MM/yyyy
     */
    public static String getApprovalDateForExports(String dateAsString, CalendarConverter calendarConverter) {
        String approvalDate = dateAsString;
        
        if (!StringUtils.isBlank(dateAsString)) {
            if (calendarConverter != null && calendarConverter instanceof AmpFiscalCalendar) {
                AmpFiscalCalendar calendar = (AmpFiscalCalendar) calendarConverter;
                if (calendar.getBaseCal().equalsIgnoreCase(BaseCalendar.BASE_ETHIOPIAN.getValue())) {
                    approvalDate = dateAsString.substring(GPIReportConstants.ETHIOPIAN_FORMATTED_DATE_DAYS_OFFSET);
                } else {
                    try {
                        String dateFormat = DateTimeUtil.getGlobalPattern();
                        approvalDate = new SimpleDateFormat(GPIReportConstants.INDICATOR1_EXPORT_APPROVAL_DATE_FORMAT)
                                .format(new SimpleDateFormat(dateFormat).parse(dateAsString));
                    } catch (ParseException e) {
                        throw new RuntimeException("Error in parsing the approval date", e);
                    }
                }
            }
        }
        
        return approvalDate;
    }
}
