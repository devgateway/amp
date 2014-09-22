/**
 * 
 */
package org.dgfoundation.amp.reports.mondrian;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.dgfoundation.amp.newreports.FilterRule;
import org.dgfoundation.amp.newreports.NamedTypedEntity;
import org.dgfoundation.amp.newreports.ReportColumn;
import org.dgfoundation.amp.newreports.ReportElement;
import org.dgfoundation.amp.newreports.ReportElement.ElementType;
import org.dgfoundation.amp.newreports.ReportFilters;
import org.dgfoundation.amp.reports.mondrian.converters.AmpARFilterConverter;
import org.digijava.kernel.ampapi.exception.AmpApiException;
import org.digijava.kernel.ampapi.mondrian.util.MondrianUtils;

/**
 * Mondrian Report Filters - can be used to populate it Manually, either via {@link AmpARFilterConverter} support
 * @author Nadejda Mandrescu
 */
public class MondrianReportFilters implements ReportFilters {
	private Map<ReportElement, List<FilterRule>> filterRules = new HashMap<ReportElement, List<FilterRule>>();
	
	/**
	 * Initialized report filters with a map of elements to filter by a list of filters each
	 * @param filterRules
	 */
	public MondrianReportFilters(Map<ReportElement, List<FilterRule>> filterRules) {
		this.filterRules = filterRules;
	}
	
	public MondrianReportFilters() {
	}
	
	@Override
	public Map<ReportElement, List<FilterRule>> getFilterRules() {
		return filterRules;
	}
	
	/**
	 * For internal use, because we need to force the use mdx properties filters for some dates 
	 * @param elem
	 * @param filterRule
	 */
	private void addFilterRule(ReportElement elem, FilterRule filterRule) {
		List<FilterRule> filtersList = filterRules.get(elem);
		if (filtersList == null) {
			filtersList = new ArrayList<FilterRule>();
			filterRules.put(elem, filtersList);
		}
		filtersList.add(filterRule);
	}
	
	/**
	 * Add a column/measure filter
	 * @param entity - column/measure to filter by
	 * @param filterRule - the filter rule to apply
	 */
	public void addFilterRule(NamedTypedEntity entity,  FilterRule filterRule) {
		addFilterRule(new ReportElement(entity), filterRule);
	}
	
	/**
	 * Adds a measures filter between [from .. to] or [from .. infinite) or (-infinite .. to] year ranges.
	 * @param from - the year to start from or null
	 * @param to - the year to end with or null
	 * @throws AmpApiException if range is invalid
	 */
	public void addYearsRangeFilterRule(Integer from, Integer to) throws AmpApiException {
		addFilterRule(new ReportElement(ElementType.YEAR), MondrianUtils.getYearsRangeFilter(from, to));
	}
	
	/**
	 * Adds a measures filter for specific  [from .. to] quarters, with no year limits
	 * @param from - from quarter limit
	 * @param to - to quarter limit
	 * @throws AmpApiException if range is invalid
	 */
	public void addQuarterRangeFilterRule(Integer from, Integer to) throws AmpApiException {
		addFilterRule(new ReportElement(ElementType.QUARTER), MondrianUtils.getQuarterRangeFilterRule(from, to));
	}
	
	/**
	 * Adds a measures filter for specific months in all years. Month numbers between [1..12] 
	 * @param from - first month number of the range
	 * @param to - last month number of the range 
	 * @throws AmpApiException if range is invalid
	 */
	public void addMonthRangeFilterRule(Integer from, Integer to) throws AmpApiException {
		addFilterRule(new ReportElement(ElementType.MONTH), MondrianUtils.getMonthRangeFilterRule(from, to));
	}
	
	/**
	 * Adds a date range filter [from .. to] or [from .. infinite ) or (infinite .. to]
	 * @param from - the date to start from or null
	 * @param to - the date to end with or null
	 * @throws AmpApiException if range is invalid
	 */
	public void addDateRangeFilterRule(Date from, Date to) throws AmpApiException {
		addFilterRule(new ReportElement(ElementType.DATE), MondrianUtils.getDateRangeFilterRule(from, to));
	}
	
	/**
	 * Adds a date range filter [from .. to] or [from .. infinite ) or (infinite .. to],
	 * over the specified report column
	 * @param column - the column to filter by (may not be present in the report)
	 * @param from - the date to start from or null
	 * @param to - the date to end with or null
	 * @throws AmpApiException if range is invalid
	 */
	public void addDateRangeFilterRule(ReportColumn column, Date from, Date to) throws AmpApiException {
		//TODO: update based on schema definition
		addFilterRule(new ReportElement(column), MondrianUtils.getDateRangeFilterRule(from, to));
	}
	
	/**
	 * Adds a measures filter for list of years 
	 * @param years - years to filter by
	 * @param valuesToInclude - true if this years to be kept, false if this years must be excluded
	 * @throws AmpApiException if list is invalid
	 */
	public void addYearsFilterRule(List<Integer> years, boolean valuesToInclude) throws AmpApiException {
		addFilterRule(new ReportElement(ElementType.YEAR), MondrianUtils.getYearsFilterRule(years, valuesToInclude));
	}
	
	/**
	 * Adds a measures filter for a list of quarters 
	 * @param quarters - the list of quarters [1..4]
	 * @param valuesToInclude - configures if this is a list of quarters to be kept (true) or to be excluded (false)
	 * @throws AmpApiException if list is invalid
	 */
	public void addQuartersFilterRule(List<Integer> quarters, boolean valuesToInclude) throws AmpApiException {
		addFilterRule(new ReportElement(ElementType.QUARTER), MondrianUtils.getQuarterFilterRule(quarters, valuesToInclude));
	}
	
	/**
	 * Adds a measures filter for specific months list in all years
	 * @param months - month numbers between [1..12]
	 * @param valuesToInclude - true if this months must be kept, false if they must be excluded
	 * @throws AmpApiException if list is invalid
	 */
	public void addMonthsFilterRule(List<Integer> months, boolean valuesToInclude) throws AmpApiException {
		addFilterRule(new ReportElement(ElementType.MONTH), MondrianUtils.getMonthsFilterRule(months, valuesToInclude));
	}
	
	/**
	 * Adds a measures date list filter 
	 * @param dates - the dates to filter by 
	 * @param valuesToInclude - true if this dates must be kept, false if they must be excluded
	 * @throws AmpApiException if list is invalid
	 */
	public void addDatesFilterRule(List<Date> dates, boolean valuesToInclude) throws AmpApiException {
		addFilterRule(new ReportElement(ElementType.DATE), MondrianUtils.getDatesFilterRule(dates, valuesToInclude));
	}
	
	/**
	 * Adds a single year filter over measures
	 * @param year
	 * @param valueToInclude
	 * @throws AmpApiException
	 */
	public void addSingleYearFilterRule(Integer year, boolean valueToInclude) throws AmpApiException {
		addFilterRule(new ReportElement(ElementType.YEAR), MondrianUtils.getSingleYearFilterRule(year, valueToInclude));
	}
	
	/**
	 * Adds a single quarter filter over measures (no years filter) 
	 * @param quarter
	 * @param valueToInclude
	 * @throws AmpApiException
	 */
	public void addSingleQuarterFilterRule(Integer quarter, boolean valueToInclude) throws AmpApiException {
		addFilterRule(new ReportElement(ElementType.QUARTER), MondrianUtils.getSingleQuarterFilterRule(quarter, valueToInclude));
	}
	
	/**
	 * Adds a single month filter over measures (no years filter) 
	 * @param quarter
	 * @param valueToInclude
	 * @throws AmpApiException
	 */
	public void addSingleMonthFilterRule(Integer month, boolean valueToInclude) throws AmpApiException {
		addFilterRule(new ReportElement(ElementType.MONTH), MondrianUtils.getSingleMonthFilterRule(month, valueToInclude));
	}
	
	/**
	 * Adds a single date filter over measures
	 * @param date
	 * @param valueToInclude
	 * @throws AmpApiException
	 */
	public void addSingleDateFilterRule(Date date, boolean valueToInclude) throws AmpApiException {
		addFilterRule(new ReportElement(ElementType.MONTH), MondrianUtils.getSingleDateFilterRule(date, valueToInclude));
	}
	
}
