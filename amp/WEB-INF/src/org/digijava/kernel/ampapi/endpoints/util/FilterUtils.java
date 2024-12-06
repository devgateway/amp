package org.digijava.kernel.ampapi.endpoints.util;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang3.tuple.Pair;
import org.apache.log4j.Logger;
import org.dgfoundation.amp.ar.AmpARFilter;
import org.dgfoundation.amp.ar.ColumnConstants;
import org.dgfoundation.amp.newreports.*;
import org.dgfoundation.amp.newreports.ReportElement.ElementType;
import org.digijava.kernel.ampapi.endpoints.filters.FiltersConstants;
import org.digijava.kernel.ampapi.endpoints.filters.FiltersProcessor;
import org.digijava.kernel.ampapi.exception.AmpApiException;
import org.digijava.kernel.request.TLSUtils;
import org.digijava.module.aim.dbentity.AmpFiscalCalendar;
import org.digijava.module.aim.helper.TeamMember;
import org.digijava.module.aim.helper.fiscalcalendar.BaseCalendar;
import org.digijava.module.aim.util.FiscalCalendarUtil;
import org.digijava.module.aim.util.LoggerIdentifiable;
import org.digijava.module.common.util.DateTimeUtil;
import org.digijava.module.search.util.SearchUtil;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.Map.Entry;
import java.util.function.Function;

public class FilterUtils {

    protected static Logger logger = Logger.getLogger(FilterUtils.class);

    public static final FilterUtils INSTANCE = new FilterUtils();

    private Map<String, String> columnNameToId = new HashMap<>();
    private Map<String, String> idToSimpleColumn = new HashMap<>();
    private Map<String, String> idToDateColumn = new HashMap<>();

    private FilterUtils() {
        mapDateColumn(FiltersConstants.ACTUAL_APPROVAL_DATE, ColumnConstants.ACTUAL_APPROVAL_DATE);
        mapDateColumn(FiltersConstants.ACTUAL_COMPLETION_DATE, ColumnConstants.ACTUAL_COMPLETION_DATE);
        mapDateColumn(FiltersConstants.ACTUAL_START_DATE, ColumnConstants.ACTUAL_START_DATE);
        mapDateColumn(FiltersConstants.EFFECTIVE_FUNDING_DATE, ColumnConstants.EFFECTIVE_FUNDING_DATE);
        mapDateColumn(FiltersConstants.FINAL_DATE_FOR_CONTRACTING, ColumnConstants.FINAL_DATE_FOR_CONTRACTING);
        mapDateColumn(FiltersConstants.ISSUE_DATE, ColumnConstants.ISSUE_DATE);
        mapDateColumn(FiltersConstants.FUNDING_CLOSING_DATE, ColumnConstants.FUNDING_CLOSING_DATE);
        mapDateColumn(FiltersConstants.PROPOSED_APPROVAL_DATE, ColumnConstants.PROPOSED_APPROVAL_DATE);
        mapDateColumn(FiltersConstants.PROPOSED_COMPLETION_DATE, ColumnConstants.PROPOSED_COMPLETION_DATE);
        mapDateColumn(FiltersConstants.PROPOSED_START_DATE, ColumnConstants.PROPOSED_START_DATE);

        mapSimpleColumn(FiltersConstants.ACTIVITY_ID, ColumnConstants.ACTIVITY_ID);
        mapSimpleColumn(FiltersConstants.APPROVAL_STATUS, ColumnConstants.APPROVAL_STATUS);
        mapSimpleColumn(FiltersConstants.MODALITIES, ColumnConstants.MODALITIES);
        mapSimpleColumn(FiltersConstants.SSC_MODALITIES, ColumnConstants.SSC_MODALITIES);
        mapSimpleColumn(FiltersConstants.BENEFICIARY_AGENCY, ColumnConstants.BENEFICIARY_AGENCY);
        mapSimpleColumn(FiltersConstants.BENEFICIARY_AGENCY_GROUP, ColumnConstants.BENEFICIARY_AGENCY_GROUPS);
        mapSimpleColumn(FiltersConstants.BENEFICIARY_AGENCY_TYPE, ColumnConstants.BENEFICIARY_AGENCY_TYPE);
        mapSimpleColumn(FiltersConstants.BENEFICIARY_AGENCY_COUNTRY, ColumnConstants.BENEFICIARY_AGENCY_COUNTRY);
        mapSimpleColumn(FiltersConstants.ADMINISTRATIVE_LEVEL_4, ColumnConstants.LOCATION_ADM_LEVEL_4);
        mapSimpleColumn(FiltersConstants.COMPONENT_FUNDING_ORGANIZATION,
                ColumnConstants.COMPONENT_FUNDING_ORGANIZATION);
        mapSimpleColumn(FiltersConstants.COMPONENT_SECOND_RESPONSIBLE_ORGANIZATION,
                ColumnConstants.COMPONENT_SECOND_RESPONSIBLE_ORGANIZATION);
        mapSimpleColumn(FiltersConstants.COMPUTED_YEAR, ColumnConstants.COMPUTED_YEAR);
        mapSimpleColumn(FiltersConstants.CONCESSIONALITY_LEVEL, ColumnConstants.CONCESSIONALITY_LEVEL);
        mapSimpleColumn(FiltersConstants.CONTRACTING_AGENCY, ColumnConstants.CONTRACTING_AGENCY);
        mapSimpleColumn(FiltersConstants.CONTRACTING_AGENCY_GROUP, ColumnConstants.CONTRACTING_AGENCY_GROUPS);
        mapSimpleColumn(FiltersConstants.CONTRACTING_AGENCY_TYPE, ColumnConstants.CONTRACTING_AGENCY_TYPE);
        mapSimpleColumn(FiltersConstants.ADMINISTRATIVE_LEVEL_0, ColumnConstants.LOCATION_ADM_LEVEL_0);
        mapSimpleColumn(FiltersConstants.DISASTER_RESPONSE_MARKER, ColumnConstants.DISASTER_RESPONSE_MARKER);
        mapSimpleColumn(FiltersConstants.ADMINISTRATIVE_LEVEL_3, ColumnConstants.LOCATION_ADM_LEVEL_3);
        mapSimpleColumn(FiltersConstants.DONOR_AGENCY, ColumnConstants.DONOR_AGENCY);
        mapSimpleColumn(FiltersConstants.DONOR_GROUP, ColumnConstants.DONOR_GROUP);
        mapSimpleColumn(FiltersConstants.DONOR_TYPE, ColumnConstants.DONOR_TYPE);
        mapSimpleColumn(FiltersConstants.DONOR_AGENCY_COUNTRY, ColumnConstants.DONOR_COUNTRY);
        mapSimpleColumn(FiltersConstants.EXECUTING_AGENCY, ColumnConstants.EXECUTING_AGENCY);
        mapSimpleColumn(FiltersConstants.EXECUTING_AGENCY_GROUP, ColumnConstants.EXECUTING_AGENCY_GROUPS);
        mapSimpleColumn(FiltersConstants.EXECUTING_AGENCY_TYPE, ColumnConstants.EXECUTING_AGENCY_TYPE);
        mapSimpleColumn(FiltersConstants.EXECUTING_AGENCY_COUNTRY, ColumnConstants.EXECUTING_AGENCY_COUNTRY);
        mapSimpleColumn(FiltersConstants.EXPENDITURE_CLASS, ColumnConstants.EXPENDITURE_CLASS);
        mapSimpleColumn(FiltersConstants.FINANCING_INSTRUMENT, ColumnConstants.FINANCING_INSTRUMENT);
        mapSimpleColumn(FiltersConstants.FUNDING_STATUS, ColumnConstants.FUNDING_STATUS);
        mapSimpleColumn(FiltersConstants.GOVERNMENT_APPROVAL_PROCEDURES, ColumnConstants.GOVERNMENT_APPROVAL_PROCEDURES);
        mapSimpleColumn(FiltersConstants.HUMANITARIAN_AID, ColumnConstants.HUMANITARIAN_AID);
        mapSimpleColumn(FiltersConstants.IMPLEMENTING_AGENCY, ColumnConstants.IMPLEMENTING_AGENCY);
        mapSimpleColumn(FiltersConstants.IMPLEMENTING_AGENCY_GROUP, ColumnConstants.IMPLEMENTING_AGENCY_GROUPS);
        mapSimpleColumn(FiltersConstants.IMPLEMENTING_AGENCY_TYPE, ColumnConstants.IMPLEMENTING_AGENCY_TYPE);
        mapSimpleColumn(FiltersConstants.JOINT_CRITERIA, ColumnConstants.JOINT_CRITERIA);
        mapSimpleColumn(FiltersConstants.LOCATION, ColumnConstants.LOCATION);
        mapSimpleColumn(FiltersConstants.RAW_LOCATION, ColumnConstants.RAW_LOCATION);
        mapSimpleColumn(FiltersConstants.MODE_OF_PAYMENT, ColumnConstants.MODE_OF_PAYMENT);
        mapSimpleColumn(FiltersConstants.NATIONAL_PLANNING_OBJECTIVES_LEVEL_0,
                ColumnConstants.NATIONAL_PLANNING_OBJECTIVES_LEVEL_0);
        mapSimpleColumn(FiltersConstants.NATIONAL_PLANNING_OBJECTIVES_LEVEL_1, ColumnConstants.NATIONAL_PLANNING_OBJECTIVES_LEVEL_1);
        mapSimpleColumn(FiltersConstants.NATIONAL_PLANNING_OBJECTIVES_LEVEL_2, ColumnConstants.NATIONAL_PLANNING_OBJECTIVES_LEVEL_2);
        mapSimpleColumn(FiltersConstants.NATIONAL_PLANNING_OBJECTIVES_LEVEL_3, ColumnConstants.NATIONAL_PLANNING_OBJECTIVES_LEVEL_3);
        mapSimpleColumn(FiltersConstants.NATIONAL_PLANNING_OBJECTIVES_LEVEL_4, ColumnConstants.NATIONAL_PLANNING_OBJECTIVES_LEVEL_4);
        mapSimpleColumn(FiltersConstants.NATIONAL_PLANNING_OBJECTIVES_LEVEL_5, ColumnConstants.NATIONAL_PLANNING_OBJECTIVES_LEVEL_5);
        mapSimpleColumn(FiltersConstants.NATIONAL_PLANNING_OBJECTIVES_LEVEL_6, ColumnConstants.NATIONAL_PLANNING_OBJECTIVES_LEVEL_6);
        mapSimpleColumn(FiltersConstants.NATIONAL_PLANNING_OBJECTIVES_LEVEL_7, ColumnConstants.NATIONAL_PLANNING_OBJECTIVES_LEVEL_7);
        mapSimpleColumn(FiltersConstants.NATIONAL_PLANNING_OBJECTIVES_LEVEL_8, ColumnConstants.NATIONAL_PLANNING_OBJECTIVES_LEVEL_8);
        mapSimpleColumn(FiltersConstants.ACTIVITY_BUDGET, ColumnConstants.ACTIVITY_BUDGET);
        mapSimpleColumn(FiltersConstants.PERFORMANCE_ALERT_LEVEL, ColumnConstants.PERFORMANCE_ALERT_LEVEL);
        mapSimpleColumn(FiltersConstants.PERFORMANCE_ALERT_TYPE, ColumnConstants.PERFORMANCE_ALERT_TYPE);
        mapSimpleColumn(FiltersConstants.PLEDGES_AID_MODALITY, ColumnConstants.PLEDGES_AID_MODALITY);
        mapSimpleColumn(FiltersConstants.PLEDGES_ADM_LEVEL_0, ColumnConstants.PLEDGES_LOCATION_ADM_LEVEL_0);
        mapSimpleColumn(FiltersConstants.PLEDGES_ADM_LEVEL_1, ColumnConstants.PLEDGES_LOCATION_ADM_LEVEL_1);
        mapSimpleColumn(FiltersConstants.PLEDGES_ADM_LEVEL_2, ColumnConstants.PLEDGES_LOCATION_ADM_LEVEL_2);
        mapSimpleColumn(FiltersConstants.PLEDGES_ADM_LEVEL_3, ColumnConstants.PLEDGES_LOCATION_ADM_LEVEL_3);
        mapSimpleColumn(FiltersConstants.PLEDGES_ADM_LEVEL_4, ColumnConstants.PLEDGES_LOCATION_ADM_LEVEL_4);
        mapDateColumn(FiltersConstants.PLEDGES_DETAIL_START_DATE, ColumnConstants.PLEDGES_DETAIL_START_DATE);
        mapDateColumn(FiltersConstants.PLEDGES_DETAIL_END_DATE, ColumnConstants.PLEDGES_DETAIL_END_DATE);
        mapSimpleColumn(FiltersConstants.PLEDGES_DONOR_GROUP, ColumnConstants.PLEDGES_DONOR_GROUP);
        mapSimpleColumn(FiltersConstants.PLEDGES_DONOR_TYPE, ColumnConstants.PLEDGES_DONOR_TYPE);
        
        mapSimpleColumn(FiltersConstants.PLEDGES_NATIONAL_PLAN_OBJECTIVES,
                ColumnConstants.PLEDGES_NATIONAL_PLAN_OBJECTIVES);
        mapSimpleColumn(FiltersConstants.PLEDGES_NATIONAL_PLAN_OBJECTIVES_LEVEL_0,
                ColumnConstants.PLEDGES_NATIONAL_PLAN_OBJECTIVES_LEVEL_0);
        mapSimpleColumn(FiltersConstants.PLEDGES_NATIONAL_PLAN_OBJECTIVES_LEVEL_1,
                ColumnConstants.PLEDGES_NATIONAL_PLAN_OBJECTIVES_LEVEL_1);
        mapSimpleColumn(FiltersConstants.PLEDGES_NATIONAL_PLAN_OBJECTIVES_LEVEL_2,
                ColumnConstants.PLEDGES_NATIONAL_PLAN_OBJECTIVES_LEVEL_2);
        mapSimpleColumn(FiltersConstants.PLEDGES_NATIONAL_PLAN_OBJECTIVES_LEVEL_3,
                ColumnConstants.PLEDGES_NATIONAL_PLAN_OBJECTIVES_LEVEL_3);
        
        mapSimpleColumn(FiltersConstants.PLEDGES_PROGRAMS, ColumnConstants.PLEDGES_PROGRAMS);
        mapSimpleColumn(FiltersConstants.PLEDGES_PROGRAMS_LEVEL_0, ColumnConstants.PLEDGES_PROGRAMS_LEVEL_0);
        mapSimpleColumn(FiltersConstants.PLEDGES_PROGRAMS_LEVEL_1, ColumnConstants.PLEDGES_PROGRAMS_LEVEL_1);
        mapSimpleColumn(FiltersConstants.PLEDGES_PROGRAMS_LEVEL_2, ColumnConstants.PLEDGES_PROGRAMS_LEVEL_2);
        mapSimpleColumn(FiltersConstants.PLEDGES_PROGRAMS_LEVEL_3, ColumnConstants.PLEDGES_PROGRAMS_LEVEL_3);
        
        mapSimpleColumn(FiltersConstants.PLEDGES_SECONDARY_PROGRAMS, ColumnConstants.PLEDGES_SECONDARY_PROGRAMS);
        mapSimpleColumn(FiltersConstants.PLEDGES_SECONDARY_PROGRAMS_LEVEL_0,
                ColumnConstants.PLEDGES_SECONDARY_PROGRAMS_LEVEL_0);
        mapSimpleColumn(FiltersConstants.PLEDGES_SECONDARY_PROGRAMS_LEVEL_1,
                ColumnConstants.PLEDGES_SECONDARY_PROGRAMS_LEVEL_1);
        mapSimpleColumn(FiltersConstants.PLEDGES_SECONDARY_PROGRAMS_LEVEL_2,
                ColumnConstants.PLEDGES_SECONDARY_PROGRAMS_LEVEL_2);
        mapSimpleColumn(FiltersConstants.PLEDGES_SECONDARY_PROGRAMS_LEVEL_3,
                ColumnConstants.PLEDGES_SECONDARY_PROGRAMS_LEVEL_3);
        
        mapSimpleColumn(FiltersConstants.PLEDGES_TERTIARY_PROGRAMS, ColumnConstants.PLEDGES_TERTIARY_PROGRAMS);
        mapSimpleColumn(FiltersConstants.PLEDGES_TERTIARY_PROGRAMS_LEVEL_0,
                ColumnConstants.PLEDGES_TERTIARY_PROGRAMS_LEVEL_0);
        mapSimpleColumn(FiltersConstants.PLEDGES_TERTIARY_PROGRAMS_LEVEL_1,
                ColumnConstants.PLEDGES_TERTIARY_PROGRAMS_LEVEL_1);
        mapSimpleColumn(FiltersConstants.PLEDGES_TERTIARY_PROGRAMS_LEVEL_2,
                ColumnConstants.PLEDGES_TERTIARY_PROGRAMS_LEVEL_2);
        mapSimpleColumn(FiltersConstants.PLEDGES_TERTIARY_PROGRAMS_LEVEL_3,
                ColumnConstants.PLEDGES_TERTIARY_PROGRAMS_LEVEL_3);
        
        mapSimpleColumn(FiltersConstants.PLEDGES_SECTORS, ColumnConstants.PLEDGES_SECTORS);
        mapSimpleColumn(FiltersConstants.PLEDGES_SECTORS_SUB_SECTORS, ColumnConstants.PLEDGES_SECTORS_SUBSECTORS);
        mapSimpleColumn(FiltersConstants.PLEDGES_SECTORS_SUB_SUB_SECTORS,
                ColumnConstants.PLEDGES_SECTORS_SUBSUBSECTORS);
        
        mapSimpleColumn(FiltersConstants.PLEDGES_SECONDARY_SECTORS, ColumnConstants.PLEDGES_SECONDARY_SECTORS);
        mapSimpleColumn(FiltersConstants.PLEDGES_SECONDARY_SECTORS_SUB_SECTORS,
                ColumnConstants.PLEDGES_SECONDARY_SUBSECTORS);
        mapSimpleColumn(FiltersConstants.PLEDGES_SECONDARY_SECTORS_SUB_SUB_SECTORS,
                ColumnConstants.PLEDGES_SECONDARY_SUBSUBSECTORS);
        
        mapSimpleColumn(FiltersConstants.PLEDGES_TERTIARY_SECTORS, ColumnConstants.PLEDGES_TERTIARY_SECTORS);
        mapSimpleColumn(FiltersConstants.PLEDGES_TERTIARY_SECTORS_SUB_SECTORS,
                ColumnConstants.PLEDGES_TERTIARY_SUBSECTORS);
        mapSimpleColumn(FiltersConstants.PLEDGES_TERTIARY_SECTORS_SUB_SUB_SECTORS,
                ColumnConstants.PLEDGES_TERTIARY_SUBSUBSECTORS);
        
        mapSimpleColumn(FiltersConstants.PLEDGES_QUATERNARY_SECTORS, ColumnConstants.PLEDGES_QUATERNARY_SECTORS);
        mapSimpleColumn(FiltersConstants.PLEDGES_QUATERNARY_SECTORS_SUB_SECTORS,
                ColumnConstants.PLEDGES_QUATERNARY_SUBSECTORS);
        mapSimpleColumn(FiltersConstants.PLEDGES_QUATERNARY_SECTORS_SUB_SUB_SECTORS,
                ColumnConstants.PLEDGES_QUATERNARY_SUBSUBSECTORS);
        
        mapSimpleColumn(FiltersConstants.PLEDGES_QUINARY_SECTORS, ColumnConstants.PLEDGES_QUINARY_SECTORS);
        mapSimpleColumn(FiltersConstants.PLEDGES_QUINARY_SECTORS_SUB_SECTORS,
                ColumnConstants.PLEDGES_QUINARY_SUBSECTORS);
        mapSimpleColumn(FiltersConstants.PLEDGES_QUINARY_SECTORS_SUB_SUB_SECTORS,
                ColumnConstants.PLEDGES_QUINARY_SUBSUBSECTORS);
        
        mapSimpleColumn(FiltersConstants.PLEDGES_STATUS, ColumnConstants.PLEDGE_STATUS);
        mapSimpleColumn(FiltersConstants.PLEDGES_TYPE_OF_ASSISTANCE, ColumnConstants.PLEDGES_TYPE_OF_ASSISTANCE);
        mapSimpleColumn(FiltersConstants.PLEDGES_TITLES, ColumnConstants.PLEDGES_TITLES);
    
        mapSimpleColumn(FiltersConstants.PRIMARY_PROGRAM_LEVEL_0, ColumnConstants.PRIMARY_PROGRAM_LEVEL_0);
        mapSimpleColumn(FiltersConstants.PRIMARY_PROGRAM_LEVEL_1, ColumnConstants.PRIMARY_PROGRAM_LEVEL_1);
        mapSimpleColumn(FiltersConstants.PRIMARY_PROGRAM_LEVEL_2, ColumnConstants.PRIMARY_PROGRAM_LEVEL_2);
        mapSimpleColumn(FiltersConstants.PRIMARY_PROGRAM_LEVEL_3, ColumnConstants.PRIMARY_PROGRAM_LEVEL_3);
        mapSimpleColumn(FiltersConstants.PRIMARY_PROGRAM_LEVEL_4, ColumnConstants.PRIMARY_PROGRAM_LEVEL_4);
        mapSimpleColumn(FiltersConstants.PRIMARY_PROGRAM_LEVEL_5, ColumnConstants.PRIMARY_PROGRAM_LEVEL_5);
        mapSimpleColumn(FiltersConstants.PRIMARY_PROGRAM_LEVEL_6, ColumnConstants.PRIMARY_PROGRAM_LEVEL_6);
        mapSimpleColumn(FiltersConstants.PRIMARY_PROGRAM_LEVEL_7, ColumnConstants.PRIMARY_PROGRAM_LEVEL_7);
        mapSimpleColumn(FiltersConstants.PRIMARY_PROGRAM_LEVEL_8, ColumnConstants.PRIMARY_PROGRAM_LEVEL_8);

        mapSimpleColumn(FiltersConstants.INDIRECT_PRIMARY_PROGRAM_LEVEL_0,
                ColumnConstants.INDIRECT_PRIMARY_PROGRAM_LEVEL_0);
        mapSimpleColumn(FiltersConstants.INDIRECT_PRIMARY_PROGRAM_LEVEL_1,
                ColumnConstants.INDIRECT_PRIMARY_PROGRAM_LEVEL_1);
        mapSimpleColumn(FiltersConstants.INDIRECT_PRIMARY_PROGRAM_LEVEL_2,
                ColumnConstants.INDIRECT_PRIMARY_PROGRAM_LEVEL_2);
        mapSimpleColumn(FiltersConstants.INDIRECT_PRIMARY_PROGRAM_LEVEL_3,
                ColumnConstants.INDIRECT_PRIMARY_PROGRAM_LEVEL_3);
        mapSimpleColumn(FiltersConstants.INDIRECT_PRIMARY_PROGRAM_LEVEL_4,
                ColumnConstants.INDIRECT_PRIMARY_PROGRAM_LEVEL_4);
        mapSimpleColumn(FiltersConstants.INDIRECT_PRIMARY_PROGRAM_LEVEL_5,
                ColumnConstants.INDIRECT_PRIMARY_PROGRAM_LEVEL_5);
        mapSimpleColumn(FiltersConstants.INDIRECT_PRIMARY_PROGRAM_LEVEL_6,
                ColumnConstants.INDIRECT_PRIMARY_PROGRAM_LEVEL_6);
        mapSimpleColumn(FiltersConstants.INDIRECT_PRIMARY_PROGRAM_LEVEL_7,
                ColumnConstants.INDIRECT_PRIMARY_PROGRAM_LEVEL_7);
        mapSimpleColumn(FiltersConstants.INDIRECT_PRIMARY_PROGRAM_LEVEL_8,
                ColumnConstants.INDIRECT_PRIMARY_PROGRAM_LEVEL_8);

        mapSimpleColumn(FiltersConstants.PRIMARY_SECTOR, ColumnConstants.PRIMARY_SECTOR);
        mapSimpleColumn(FiltersConstants.PRIMARY_SECTOR_SUB_SECTOR, ColumnConstants.PRIMARY_SECTOR_SUB_SECTOR);
        mapSimpleColumn(FiltersConstants.PRIMARY_SECTOR_SUB_SUB_SECTOR, ColumnConstants.PRIMARY_SECTOR_SUB_SUB_SECTOR);
        mapSimpleColumn(FiltersConstants.PROCUREMENT_SYSTEM, ColumnConstants.PROCUREMENT_SYSTEM);
        mapSimpleColumn(FiltersConstants.PROJECT_IMPLEMENTING_UNIT, ColumnConstants.PROJECT_IMPLEMENTING_UNIT);
        mapSimpleColumn(FiltersConstants.ADMINISTRATIVE_LEVEL_1, ColumnConstants.LOCATION_ADM_LEVEL_1);
        mapSimpleColumn(FiltersConstants.RESPONSIBLE_ORGANIZATION, ColumnConstants.RESPONSIBLE_ORGANIZATION);
        mapSimpleColumn(FiltersConstants.RESPONSIBLE_ORGANIZATION_GROUP,
                ColumnConstants.RESPONSIBLE_ORGANIZATION_GROUPS);
        mapSimpleColumn(FiltersConstants.RESPONSIBLE_ORGANIZATION_TYPE, ColumnConstants.RESPONSIBLE_ORGANIZATION_TYPE);
        mapSimpleColumn(FiltersConstants.SECONDARY_PROGRAM_LEVEL_0, ColumnConstants.SECONDARY_PROGRAM_LEVEL_0);
        mapSimpleColumn(FiltersConstants.SECONDARY_PROGRAM_LEVEL_1, ColumnConstants.SECONDARY_PROGRAM_LEVEL_1);
        mapSimpleColumn(FiltersConstants.SECONDARY_PROGRAM_LEVEL_2, ColumnConstants.SECONDARY_PROGRAM_LEVEL_2);
        mapSimpleColumn(FiltersConstants.SECONDARY_PROGRAM_LEVEL_3, ColumnConstants.SECONDARY_PROGRAM_LEVEL_3);
        mapSimpleColumn(FiltersConstants.SECONDARY_PROGRAM_LEVEL_4, ColumnConstants.SECONDARY_PROGRAM_LEVEL_4);
        mapSimpleColumn(FiltersConstants.SECONDARY_PROGRAM_LEVEL_5, ColumnConstants.SECONDARY_PROGRAM_LEVEL_5);
        mapSimpleColumn(FiltersConstants.SECONDARY_PROGRAM_LEVEL_6, ColumnConstants.SECONDARY_PROGRAM_LEVEL_6);
        mapSimpleColumn(FiltersConstants.SECONDARY_PROGRAM_LEVEL_7, ColumnConstants.SECONDARY_PROGRAM_LEVEL_7);
        mapSimpleColumn(FiltersConstants.SECONDARY_PROGRAM_LEVEL_8, ColumnConstants.SECONDARY_PROGRAM_LEVEL_8);
        mapSimpleColumn(FiltersConstants.SECONDARY_SECTOR, ColumnConstants.SECONDARY_SECTOR);
        mapSimpleColumn(FiltersConstants.SECONDARY_SECTOR_SUB_SECTOR, ColumnConstants.SECONDARY_SECTOR_SUB_SECTOR);
        mapSimpleColumn(FiltersConstants.SECONDARY_SECTOR_SUB_SUB_SECTOR, ColumnConstants.SECONDARY_SECTOR_SUB_SUB_SECTOR);
        mapSimpleColumn(FiltersConstants.SECTOR_TAG, ColumnConstants.SECTOR_TAG);
        mapSimpleColumn(FiltersConstants.STATUS, ColumnConstants.STATUS);
        mapSimpleColumn(FiltersConstants.TEAM, ColumnConstants.TEAM);
        mapSimpleColumn(FiltersConstants.TERTIARY_PROGRAM_LEVEL_0, ColumnConstants.TERTIARY_PROGRAM_LEVEL_0);
        mapSimpleColumn(FiltersConstants.TERTIARY_PROGRAM_LEVEL_1, ColumnConstants.TERTIARY_PROGRAM_LEVEL_1);
        mapSimpleColumn(FiltersConstants.TERTIARY_PROGRAM_LEVEL_2, ColumnConstants.TERTIARY_PROGRAM_LEVEL_2);
        mapSimpleColumn(FiltersConstants.TERTIARY_PROGRAM_LEVEL_3, ColumnConstants.TERTIARY_PROGRAM_LEVEL_3);
        mapSimpleColumn(FiltersConstants.TERTIARY_PROGRAM_LEVEL_4, ColumnConstants.TERTIARY_PROGRAM_LEVEL_4);
        mapSimpleColumn(FiltersConstants.TERTIARY_PROGRAM_LEVEL_5, ColumnConstants.TERTIARY_PROGRAM_LEVEL_5);
        mapSimpleColumn(FiltersConstants.TERTIARY_PROGRAM_LEVEL_6, ColumnConstants.TERTIARY_PROGRAM_LEVEL_6);
        mapSimpleColumn(FiltersConstants.TERTIARY_PROGRAM_LEVEL_7, ColumnConstants.TERTIARY_PROGRAM_LEVEL_7);
        mapSimpleColumn(FiltersConstants.TERTIARY_PROGRAM_LEVEL_8, ColumnConstants.TERTIARY_PROGRAM_LEVEL_8);
        mapSimpleColumn(FiltersConstants.TERTIARY_SECTOR, ColumnConstants.TERTIARY_SECTOR);
        mapSimpleColumn(FiltersConstants.TERTIARY_SECTOR_SUB_SECTOR, ColumnConstants.TERTIARY_SECTOR_SUB_SECTOR);
        mapSimpleColumn(FiltersConstants.TERTIARY_SECTOR_SUB_SUB_SECTOR, ColumnConstants.TERTIARY_SECTOR_SUB_SUB_SECTOR);
        mapSimpleColumn(FiltersConstants.QUATERNARY_SECTOR, ColumnConstants.QUATERNARY_SECTOR);
        mapSimpleColumn(FiltersConstants.QUATERNARY_SECTOR_SUB_SECTOR, ColumnConstants.QUATERNARY_SECTOR_SUB_SECTOR);
        mapSimpleColumn(FiltersConstants.QUATERNARY_SECTOR_SUB_SUB_SECTOR,
                ColumnConstants.QUATERNARY_SECTOR_SUB_SUB_SECTOR);
        mapSimpleColumn(FiltersConstants.QUINARY_SECTOR, ColumnConstants.QUINARY_SECTOR);
        mapSimpleColumn(FiltersConstants.QUINARY_SECTOR_SUB_SECTOR, ColumnConstants.QUINARY_SECTOR_SUB_SECTOR);
        mapSimpleColumn(FiltersConstants.QUINARY_SECTOR_SUB_SUB_SECTOR, ColumnConstants.QUINARY_SECTOR_SUB_SUB_SECTOR);
        mapSimpleColumn(FiltersConstants.TYPE_OF_ASSISTANCE, ColumnConstants.TYPE_OF_ASSISTANCE);
        mapSimpleColumn(FiltersConstants.WORKSPACES, ColumnConstants.WORKSPACES);
        mapSimpleColumn(FiltersConstants.ADMINISTRATIVE_LEVEL_2, ColumnConstants.LOCATION_ADM_LEVEL_2);

        mapSimpleColumn(FiltersConstants.INDICATOR_NAME, ColumnConstants.INDICATOR_NAME);
    }

    private void mapSimpleColumn(String filterId, String columnName) {
        idToSimpleColumn.put(filterId, columnName);
        columnNameToId.put(columnName, filterId);
    }

    private void mapDateColumn(String filterId, String columnName) {
        idToDateColumn.put(filterId, columnName);
        columnNameToId.put(columnName, filterId);
    }

    public String idFromColumnName(String columnName) {
        return columnNameToId.get(columnName);
    }
    
    public String simpleColumnNameFromFilterId(String filterId) {
        return idToSimpleColumn.get(filterId);
    }

    private static AmpReportFilters getApiDateFilters(Map<String, Object> filter, AmpReportFilters filterRules) {
        for (Entry<String, String> entry : INSTANCE.idToDateColumn.entrySet()) {
            String filterId = entry.getKey();
            String columnName = entry.getValue();
            Map<String, Object> date = (Map<String, Object>) filter.get(filterId);
            if (date != null) {
                filterRules = addDateFilterRule(columnName, date, filterRules);
            }
        }

        Map<String, Object> date = (Map<String, Object>) filter.get(FiltersConstants.DATE);
        if (date != null) {
            filterRules = addDateFilterRule(null, date, filterRules);
        }

        filterRules = addQuarterFilterRule(filter, filterRules);
        filterRules = addYearFilterRule(filter, filterRules);

        return filterRules;
    }

    private static AmpReportFilters addDateFilterRule(String dateColumn, Map<String, Object> date,
            AmpReportFilters filterRules) {
        try {
            if (filterRules == null) {
                filterRules = new AmpReportFilters();
            }
            String start = denull(String.valueOf(date.get("start")));
            String end = denull(String.valueOf(date.get("end")));
            
            if (start != null || end != null) {
                SimpleDateFormat sdf = new SimpleDateFormat(FiltersConstants.DATE_FORMAT);
                Date startDate = start == null ? null : sdf.parse(start);
                Date endDate = end == null ? null : sdf.parse(end);
                if (dateColumn != null) {
                    filterRules.addDateRangeFilterRule(new ReportColumn(dateColumn), startDate, endDate);
                } else {
                    filterRules.addDateRangeFilterRule(startDate, endDate);
                }
            }
        } catch (AmpApiException | ParseException e) {
            logger.error("cannot process date", e);
            //throw new RuntimeException(e);
        }
        return filterRules;
    }

    private static AmpReportFilters addQuarterFilterRule(Map<String, Object> filter, AmpReportFilters filterRules) {
        Object rawFilter = filter.get(FiltersConstants.QUARTER);

        Pair<Integer, Integer> range = extractRange(rawFilter, FilterUtils::parseQuarter);
        filterRules = addQuarterFilterRule(range, filterRules);

        List<Integer> years = extractList(rawFilter, FilterUtils::parseQuarter);
        filterRules = addQuarterFilterRule(years, filterRules);

        return filterRules;
    }

    private static AmpReportFilters addYearFilterRule(Map<String, Object> filter, AmpReportFilters filterRules) {
        Object rawYearFilter = filter.get(FiltersConstants.YEAR);

        Pair<Integer, Integer> yearRange = extractRange(rawYearFilter, FilterUtils::parseYear);
        filterRules = addYearFilterRule(yearRange, filterRules);

        List<Integer> years = extractList(rawYearFilter, FilterUtils::parseYear);
        filterRules = addYearFilterRule(years, filterRules);

        return filterRules;
    }

    /**
     * Parse a quarter string to integer representation. Handles cases like Q2, 3 or q4.
     * @param quarter number or Q+number
     */
    private static Integer parseQuarter(String quarter) {
        if (quarter == null) {
            return null;
        }
        String stripped = (quarter.length() > 1 && Character.toUpperCase(quarter.charAt(0)) == 'Q')
                ? quarter.substring(1) : quarter;
        return Integer.parseInt(stripped);
    }

    /**
     * Convert a year string to an integer. Handles fiscal years like 2018/2019 by returning first part, i.e. 2018.
     * @param year regular year or FY
     */
    private static Integer parseYear(String year) {
        if (year == null) {
            return null;
        }
        int p = year.indexOf('/');
        return Integer.parseInt((p >= 0) ? year.substring(0, p) : year);
    }

    private static Pair<Integer, Integer> extractRange(Object rawFilter, Function<String, Integer> parser) {
        Integer start = null;
        Integer end = null;
        if (rawFilter instanceof Map) {
            Map map = (Map) rawFilter;
            start = extractInt(map.get("start"), parser);
            end = extractInt(map.get("end"), parser);
        }
        return Pair.of(start, end);
    }

    private static List<Integer> extractList(Object rawFilter, Function<String, Integer> parser) {
        List<Integer> values = new ArrayList<>();
        if (rawFilter instanceof List) {
            List<?> rawValues = (List<?>) rawFilter;
            for (Object rawValue : rawValues) {
                Integer value = extractInt(rawValue, parser);
                if (value != null) {
                    values.add(value);
                }
            }
        } else if (!(rawFilter instanceof Map)) {
            Integer value = extractInt(rawFilter, parser);
            if (value != null) {
                values.add(value);
            }
        }
        return values;
    }

    /**
     * Attempt to convert a value parsed by Jackson to Integer. If the value is likely a string, use the parser to
     * convert to int.
     *
     * @param rawValue as parsed by Jackson
     * @param parser parser that can convert a non-null string to an Integer
     * @return extracted Integer or null if rawValue is null
     */
    private static Integer extractInt(Object rawValue, Function<String, Integer> parser) {
        if (rawValue instanceof Integer) {
            return (Integer) rawValue;
        } else if (rawValue instanceof Long) {
            return ((Long) rawValue).intValue();
        } else if (rawValue != null) {
            return parser.apply(rawValue.toString());
        } else {
            return null;
        }
    }

    private static AmpReportFilters addQuarterFilterRule(Pair<Integer, Integer> pair, AmpReportFilters filterRules) {
        try {
            if (filterRules == null) {
                filterRules = new AmpReportFilters();
            }
            if (pair.getLeft() != null || pair.getRight() != null) {
                filterRules.addQuarterFilterRule(pair.getLeft(), pair.getRight());
            }
        } catch (AmpApiException | NumberFormatException e) {
            logger.error("cannot process quarter", e);
        }
        return filterRules;
    }

    private static AmpReportFilters addQuarterFilterRule(List<Integer> quarters, AmpReportFilters filterRules) {
        try {
            if (filterRules == null) {
                filterRules = new AmpReportFilters();
            }
            if (!quarters.isEmpty()) {
                filterRules.addQuarterFilterRule(quarters);
            }
        } catch (AmpApiException | NumberFormatException e) {
            logger.error("cannot process quarter", e);
        }
        return filterRules;
    }

    private static AmpReportFilters addYearFilterRule(Pair<Integer, Integer> range, AmpReportFilters filterRules) {
        try {
            if (filterRules == null) {
                filterRules = new AmpReportFilters();
            }
            if (range.getLeft() != null && range.getRight() != null) {
                filterRules.addYearsRangeFilterRuleNoConv(range.getLeft(), range.getRight());
            }
        } catch (AmpApiException | NumberFormatException e) {
            logger.error("cannot process year", e);
        }
        return filterRules;
    }

    private static AmpReportFilters addYearFilterRule(List<Integer> years, AmpReportFilters filterRules) {
        try {
            if (filterRules == null) {
                filterRules = new AmpReportFilters();
            }
            if (!years.isEmpty()) {
                filterRules.addYearsFilterRule(years, true);
            }
        } catch (AmpApiException | NumberFormatException e) {
            logger.error("cannot process year", e);
        }
        return filterRules;
    }

    /**
     * returns the original String instance, unless it equals "null", case in which null will be returned
     * @param s
     * @return
     */
    private static String denull(String s) {
        if (StringUtils.isNotBlank(s) && !s.equalsIgnoreCase("null")) return s;
        return null;
    }

    /**
     * returns a AmpReportFilters based on the End point parameter
     * 
     * @param filters
     * @return
     */
    private static AmpReportFilters getApiColumnFilter(Map<String, Object> filters,
            AmpReportFilters filterRules) {
        if (filters == null) {
            return filterRules;
        }
        if (filterRules == null) {
            filterRules = new AmpReportFilters();
        }
        for (Entry<String, Object> entry : filters.entrySet()) {
            String column = FilterUtils.INSTANCE.idToSimpleColumn.get(entry.getKey());
            if (column != null) {
                Object value = entry.getValue();
                if (value instanceof List) {
                    List<String> ids = getStringsFromArray((List<?>) value);
                    filterRules.addFilterRule(new ReportColumn(column), new FilterRule(ids, true));
                } else 
                if (value != null) {
                    String strValue = value.toString();
                    filterRules.addFilterRule(new ReportColumn(column), new FilterRule(strValue, true));
                }
            }
        }
        
        return filterRules;
    }
    
    private static List<String> getStringsFromArray(List<?> theArray) {
        List<String> s = new ArrayList<String>();
        for (Object obj : theArray) {
            if (obj != null) {
                s.add(obj.toString());
            }
        }
        return s;
    }
    
    public static List<String> applyKeywordSearch(Map<String, Object> filters) {
        List<String> activitIds = new ArrayList<>();

        if (filters!=null && filters.get("keyword") != null) {
            String keyword = filters.get("keyword").toString();
            Collection<LoggerIdentifiable> activitySearch = SearchUtil
                    .getActivities(keyword,
                            TLSUtils.getRequest(), (TeamMember) TLSUtils.getRequest().getSession().getAttribute("currentMember"));
            if (activitySearch != null && activitySearch.size() > 0) {
                for (LoggerIdentifiable loggerIdentifiable : activitySearch) {
                    activitIds.add(loggerIdentifiable.getIdentifier().toString());
                }
            }
        }
        return activitIds;
    }
    
    public static AmpReportFilters getFilterRules(Map<String, Object> filters, List<String> activityIds) {
        return getFilterRules(filters, activityIds, null);
    }
            
    public static AmpReportFilters getFilterRules(Map<String, Object> filters,
            List<String> activityIds, AmpReportFilters filterRules) {
        if (filters != null) {
            filterRules = FilterUtils.getApiColumnFilter(filters, filterRules);
            filterRules = FilterUtils.getApiDateFilters(filters, filterRules);
            reportIgnoredFilters(filters);
        }
        if(activityIds!=null && !activityIds.isEmpty()){
            //if we have activityIds to add to the filter comming from the search by keyworkd
            if(filterRules==null){
                filterRules = new AmpReportFilters();
            }

            filterRules.addFilterRule(new ReportColumn(ColumnConstants.ACTIVITY_ID), new FilterRule(activityIds, true)); 

        }
        return filterRules;
    }

    /**
     * Reports to log all filters that are ignored by Filters API.
     * @param filters filters to check
     */
    private static void reportIgnoredFilters(Map<String, Object> filters) {
        for (String filterId : filters.keySet()) {
            if (!isSupportedByFilterApi(filterId)) {
                logger.warn("Entry not supported by Filter API: " + filterId);
            }
        }
    }

    /**
     * Returns true if this filter id is supported by Filter API.
     * @param filterId id to check
     * @return true if this filter id is supported by Filter API
     */
    private static boolean isSupportedByFilterApi(String filterId) {
        return FilterUtils.INSTANCE.idToSimpleColumn.containsKey(filterId)
                || FilterUtils.INSTANCE.idToDateColumn.containsKey(filterId)
                || FiltersConstants.DATE.equals(filterId);
    }

    /**
     * Builds AmpReportFilters based on the json filters request
     * @param filterMap json filters config request
     * @return AmpReportFilters
     * @see #getFilters(Map, List, AmpReportFilters)
     */
    public static AmpReportFilters getFilters(Map<String, Object> filterMap, AmpReportFilters filters) {
        return getFilters(filterMap, null, filters);
    }
    
    /**
     * Builds AmpReportFilters based on the json filters request and additional options
     * @param filterMap json filters config request
     * @param activitIds    the list of activities to filter by
     * @return AmpReportFilters
     */
    public static AmpReportFilters getFilters(Map<String, Object> filterMap, List<String> activitIds,
            AmpReportFilters filters) {
        
        //we check if we have filter by keyword
        if (activitIds == null) {
            activitIds = new ArrayList<>();
        }
        activitIds.addAll(FilterUtils.applyKeywordSearch(filterMap));

        filters = FilterUtils.getFilterRules(filterMap, activitIds, filters);
        
        FiltersProcessor fProcessor = new FiltersProcessor(filterMap, filters);
        
        return fProcessor.getFilters();
    }
    
    /**
     * @param status
     * @return
     */
    public static String getApprovalStatusByNumber(Integer status){
        for (Entry<String, Integer> entry : AmpARFilter.VALIDATION_STATUS.entrySet()) {
            if (entry.getValue().equals(status)) {
                return entry.getKey();
            }
        }
        
        return "";
    }
    
    public static String getSettingbyName(Map<String, Object> settings, String value) {
        return settings == null ? null : (String) settings.get(value);
    }

    /**
     * AMP-26444 When switching to ETH-CALENDAR or to GREG-CALENDAR with offsets (m, d), the selected year for
     * date should be updated.
     * We have to update explicitly the dates. E.g.: If in GPI
     * was selected 2009 in ETH-Calendar, we have to update the date
     * filter in Gregorian CAL 2009: "01/01/2009 to 31/12/2009" in ETH Calendar
     * equals to "11/09/2016 to 10/09/2017" in GREG
     *
     * @param spec
     */
    public static void updateAllDateFilters(ReportSpecificationImpl spec) {
        AmpReportFilters filters = (AmpReportFilters) spec.getFilters();

        CalendarConverter calendarConverter = (spec.getSettings() != null && spec.getSettings().getCalendar() != null)
                ? spec.getSettings().getCalendar() : AmpARFilter.getDefaultCalendar();

        boolean shouldFilterDatesToBeConverted = false;
        if (calendarConverter instanceof AmpFiscalCalendar) {
            AmpFiscalCalendar calendar = (AmpFiscalCalendar) calendarConverter;
            shouldFilterDatesToBeConverted = !calendar.getBaseCal().equals(BaseCalendar.BASE_GREGORIAN.getValue())
                    || calendar.getStartMonthNum() != 1 || calendar.getStartDayNum() != 1;
        }

        if (shouldFilterDatesToBeConverted) {
            AmpFiscalCalendar calendar = (AmpFiscalCalendar) calendarConverter;

            // update all date filter columns
            if (filters.getDateFilterRules() != null && !filters.getDateFilterRules().isEmpty()) {
                filters.getDateFilterRules().entrySet().forEach(entry -> {
                    if (entry.getValue() != null) {
                        FilterRule gregFilterRule = entry.getValue();
                        FilterRule convertedFilterRule = convertDateFilterRule(calendar, gregFilterRule);
                        entry.setValue(convertedFilterRule);
                    }
                });
            }

            // update date filter
            Optional<Entry<ReportElement, FilterRule>> dateRuleEntry = spec.getFilters()
                    .getFilterRules().entrySet().stream()
                    .filter(entry -> entry.getKey().type.equals(ElementType.DATE))
                    .filter(entry -> entry.getKey().entity == null)
                    .findAny();

            if (dateRuleEntry.isPresent() && dateRuleEntry.get().getValue() != null) {
                FilterRule dateFilterRule = dateRuleEntry.get().getValue();
                FilterRule convertedFilterRule = convertDateFilterRule(calendar, dateFilterRule);
                dateRuleEntry.get().setValue(convertedFilterRule);
            }
        }
    }

    /**
     * Get year from date
     * @param date
     * @return
     */
    public static int getYearFromDate(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);

        return calendar.get(Calendar.YEAR);
    }

    /**
     * @param sourceCalendar
     * @param gregFilterRule
     * @return
     */
    private static FilterRule convertDateFilterRule(AmpFiscalCalendar sourceCalendar, FilterRule gregFilterRule) {
        Date gregStart = gregFilterRule.min == null ? null : DateTimeUtil.fromJulianNumberToDate(gregFilterRule.min);
        Date gregEnd = gregFilterRule.max == null ? null : DateTimeUtil.fromJulianNumberToDate(gregFilterRule.max);

        Date start = FiscalCalendarUtil.toGregorianDate(gregStart, sourceCalendar);
        Date end = FiscalCalendarUtil.toGregorianDate(gregEnd, sourceCalendar);

        try {
            return DateFilterUtils.getDatesRangeFilterRule(ElementType.DATE,
                    DateTimeUtil.toJulianDayNumber(start), DateTimeUtil.toJulianDayNumber(end),
                    DateTimeUtil.formatDateOrNull(start), DateTimeUtil.formatDateOrNull(end), false);
        } catch (AmpApiException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Apply filterRules. In case the spec already have filterRules, append them.
     *
     * @param filterMap map with all filters
     * @param spec report specification
     * @param months if not null then filter by last N months
     */
    public static void applyFilterRules(Map<String, Object> filterMap, ReportSpecificationImpl spec, Integer months) {
        AmpReportFilters filterRules = FilterUtils.getFilters(filterMap, (AmpReportFilters) spec.getFilters());
        if (months != null) {
            Calendar cal = Calendar.getInstance();
            Calendar currentCal = Calendar.getInstance();
            cal.add(Calendar.MONTH, -months);

            try {
                if (filterRules == null) {
                    filterRules = new AmpReportFilters();
                }
                filterRules.addDateRangeFilterRule(cal.getTime(), currentCal.getTime());
            } catch (AmpApiException e) {
                logger.error(e.getMessage(), e);

            }
        }
        if (filterRules != null) {
            spec.setFilters(filterRules);
        }
    }
}
