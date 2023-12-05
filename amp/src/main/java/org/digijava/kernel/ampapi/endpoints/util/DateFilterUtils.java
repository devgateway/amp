/**
 * 
 */
package org.digijava.kernel.ampapi.endpoints.util;

import org.dgfoundation.amp.ar.ArConstants;
import org.dgfoundation.amp.newreports.FilterRule;
import org.dgfoundation.amp.newreports.ReportElement.ElementType;
import org.digijava.kernel.ampapi.exception.AmpApiException;
import org.digijava.module.aim.dbentity.AmpFiscalCalendar;
import org.digijava.module.aim.util.FiscalCalendarUtil;
import org.digijava.module.common.util.DateTimeUtil;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Utility methods to build dates / years filters
 * 
 * @author Nadejda Mandrescu
 */
public class DateFilterUtils {

    public static final int MIN_QUARTER = 1;
    public static final int MAX_QUARTER = 4;

    /**
     * Builds a date range filter [from .. to] or [from .. infinite ) or (infinite .. to]
     * @param from - the date to start from or null
     * @param to - the date to end with or null
     * @throws AmpApiException if range is invalid
     */
    public static FilterRule getDateRangeFilterRule(Date start, Date end, AmpFiscalCalendar toCalendar, 
            AmpFiscalCalendar fromCalendar) 
            throws AmpApiException {
        /*
         * once UI will be fully in sync with current Calendar (filter picker + keeping track for calendar of the dates)
         * you can uncomment this part
        Date gregStart = start == null ? null : FiscalCalendarUtil.toGregorianDate(start, 
                fromCalendar != null ? fromCalendar : toCalendar);
        Date gregEnd = end == null ? null : FiscalCalendarUtil.toGregorianDate(end, 
                fromCalendar != null ? fromCalendar : toCalendar);
        start = FiscalCalendarUtil.convertDate(fromCalendar, start, toCalendar);
        end = FiscalCalendarUtil.convertDate(fromCalendar, end, toCalendar);
        return getDatesRangeFilterRule(ElementType.DATE,
                DateTimeUtil.toJulianDayNumber(gregStart), DateTimeUtil.toJulianDayNumber(gregEnd), 
                DateTimeUtil.formatDateOrNull(start), DateTimeUtil.formatDateOrNull(end), false);
        */
        return getDatesRangeFilterRule(ElementType.DATE,
                DateTimeUtil.toJulianDayNumber(start), DateTimeUtil.toJulianDayNumber(end),
                DateTimeUtil.formatDateOrNull(start, ArConstants.DATE_FORMAT), 
                DateTimeUtil.formatDateOrNull(end, ArConstants.DATE_FORMAT), false);

    }
    
    private static FilterRule getDatesRangeFilterRule(ElementType elemType, Integer from, Integer to, boolean bothLimits) 
            throws AmpApiException {
        return getDatesRangeFilterRule(elemType, from, to, null, null, bothLimits); 
    }
    
    public static FilterRule getDatesRangeFilterRule(ElementType elemType, Integer from, Integer to, 
            String fromName, String toName, boolean bothLimits) throws AmpApiException {
        validate (elemType, from);
        validate (elemType, to);
        if (from == null && to == null)
            throw new AmpApiException(elemType + ": at least 'from' or 'to' range limit must be specified. Do not use the range filter if no filter is needed.");
        if (from != null && to != null && from > to)
            throw new AmpApiException("The lower limit 'from' must be smaller or equal to the upper limit 'to'. Failed request for from = " + from + ", to = " + to);
        if (to == null)
            to = ArConstants.UNDEFINED_KEY - 1; //to skip undefined dates
        return new FilterRule(toStringOrNull(from), toStringOrNull(to), fromName, toName, true, true);
    }
    
    /**
     * Builds a filter for list of years 
     * @param years - years to filter by
     * @param valuesToInclude - true if this years to be kept, false if this years must be excluded
     * @throws AmpApiException if range is invalid
     */
    public static FilterRule getYearsFilterRule(List<Integer> years, boolean valuesToInclude) throws AmpApiException {
        return getDatesListFilterRule(ElementType.YEAR, years, valuesToInclude);
    }

    /**
     * Builds a filter for range of years
     * @param fromYear - first year to include
     * @param untilYear - last year to include
     * @throws AmpApiException if range is invalid
     */
    public static FilterRule getYearsFilterRule(Integer fromYear, Integer untilYear) throws AmpApiException {
        return getDatesRangeFilterRule(ElementType.YEAR, fromYear, untilYear, true);
    }

    public static FilterRule getQuarterFilterRule(List<Integer> quarters) throws AmpApiException {
        return getDatesListFilterRule(ElementType.QUARTER, quarters, true);
    }

    public static FilterRule getQuarterFilterRule(Integer start, Integer end) throws AmpApiException {
        return getDatesRangeFilterRule(ElementType.QUARTER, start, end, true);
    }

    /**
     * Builds a date list filter 
     * @param dates - the dates to filter by 
     * @param valuesToInclude - true if this dates must be kept, false if they must be excluded
     * @throws AmpApiException if list is invalid
     */
    public static FilterRule getDatesFilterRule(List<Date> dates, boolean valuesToInclude) throws AmpApiException {
        List<Integer> julianDateNumbers = new ArrayList<Integer>(dates.size());
        //List<String> dateStrList = new ArrayList<String>(dates.size());
        for (Date date : dates) {
            julianDateNumbers.add(DateTimeUtil.toJulianDayNumber(date));
            //dateStrList.add(DateTimeUtil.formatDateOrNull(date));
        }
        return getDatesListFilterRule(ElementType.DATE, julianDateNumbers, valuesToInclude);
    }
    
    private static FilterRule getDatesListFilterRule(ElementType elemType, List<Integer> values, boolean valuesToInclude) throws AmpApiException {
        List<String> strValues = new ArrayList<String>(values.size());
        for (Integer value : values) {
            validate(elemType, value);
            strValues.add(value == null ? null : value.toString());
        }
        return new FilterRule(strValues, valuesToInclude);
    }
    
    /**
     * Builds a filter between [from .. to] or [from .. infinite) or (-infinite .. to] year ranges.
     * @param from - the year to start from or null
     * @param to - the year to end with or null
     * @param calendar - (optional) the calendar to use to store actual names
     * @throws Exception if range is invalid
     */
    public static FilterRule getYearsRangeFilter(Integer start, Integer end, AmpFiscalCalendar fromCalendar,
            AmpFiscalCalendar toCalendar) throws AmpApiException {
        start = FiscalCalendarUtil.getActualYear(fromCalendar, start, 0, toCalendar);
        end = FiscalCalendarUtil.getActualYear(fromCalendar, end + 1, -1, toCalendar);
        return getDatesRangeFilterRule(ElementType.YEAR, start, end, false);
    }
    
    /**
     * Builds a single year filter 
     * @param year
     * @param calendar - (optional) the calendar to use to store actual names
     * @param valueToInclude
     * @throws Exception 
     */
    public static FilterRule getSingleYearFilterRule(Integer year, AmpFiscalCalendar calendar, 
            boolean valueToInclude) throws Exception {
        return getSingleDateFilterRule(ElementType.YEAR, year, valueToInclude);
    }
    
    /**
     * Builds a single date filter
     * @param month
     * @param valueToInclude
     * @throws AmpApiException
     */
    public static FilterRule getSingleDateFilterRule(Date date, boolean valueToInclude) throws AmpApiException {
        return getSingleDateFilterRule(ElementType.DATE, DateTimeUtil.toJulianDayNumber(date), 
                DateTimeUtil.formatDateOrNull(date, ArConstants.DATE_FORMAT), valueToInclude);
    }
    
    private static FilterRule getSingleDateFilterRule(ElementType elemType, Integer value, boolean valueToInclude) throws AmpApiException {
        return getSingleDateFilterRule(elemType, value, null, valueToInclude);
    }
    
    private static FilterRule getSingleDateFilterRule(ElementType elemType, Integer value, 
                String name, boolean valueToInclude) throws AmpApiException {
        validate (elemType, value);
        if (value == null)
            throw new AmpApiException("Single value filter must have a value specified. value = " + value);
        return new FilterRule(value.toString(), valueToInclude);
    }
    
    private static void validate(ElementType elemType, Integer value) throws AmpApiException {
        Integer lowerLimit = null;
        Integer upperLimit = null;
        Integer undefined = null;
        boolean mustBeNotNull = false;
        switch (elemType) {
            case YEAR:
            case DATE:
                lowerLimit = 0;
                upperLimit = Integer.MAX_VALUE;
                break;
            case QUARTER:
                lowerLimit = MIN_QUARTER;
                upperLimit = MAX_QUARTER;
                break;
            default:
                break;
        }
        if (mustBeNotNull && value == null || lowerLimit != null && value != null && (value < lowerLimit || value > upperLimit ) 
                && (undefined != null && undefined != value))
            throw new AmpApiException(elemType + " range limits must be within [" + lowerLimit + ", " + upperLimit + "]. Value not in the range = " + value);
    }
    
    private static String toStringOrNull(Object o) {
        return o == null ? null : o.toString();
    }
}
