/**
 * 
 */
package org.dgfoundation.amp.reports.mondrian;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.dgfoundation.amp.ar.AmpARFilter;
import org.dgfoundation.amp.ar.ColumnConstants;
import org.dgfoundation.amp.newreports.AmpReportFilters;
import org.dgfoundation.amp.newreports.FilterRule;
import org.dgfoundation.amp.newreports.NamedTypedEntity;
import org.dgfoundation.amp.newreports.ReportColumn;
import org.dgfoundation.amp.newreports.ReportElement;
import org.dgfoundation.amp.newreports.ReportElement.ElementType;
import org.dgfoundation.amp.reports.converters.AmpARFilterConverter;
import org.digijava.kernel.ampapi.endpoints.util.DateFilterUtils;
import org.digijava.kernel.ampapi.exception.AmpApiException;
import org.digijava.kernel.ampapi.mondrian.util.MondrianUtils;
import org.digijava.module.aim.dbentity.AmpFiscalCalendar;

/**
 * 
 * TODO: Scheduled to be deleted while removing Mondrian from AMP and refactoring the Filters API backend to use {@link AmpReportFilters} instead
 * Mondrian Report Filters - can be used to populate it Manually, either via {@link AmpARFilterConverter} support
 * 
 * @author Nadejda Mandrescu
 */
public class MondrianReportFilters extends AmpReportFilters {
		
//	/** stores  MondrianReportFilter that should be applied as same hierarchy filters */  
//	private Map<String, MondrianReportFilters> groupFilterRules = null; 
	
	/**
	 * holds filter rules for the filtering which is done through SQL (in the schema processor)
	 * <strong>elements which land in this map are not duplicated in the main {@link #filterRules} map</strong>
	 * Map<[Column Group Name]>, List<[Rules related to it]>
	 */
	private Map<String, List<FilterRule>> sqlFilterRules = new HashMap<>();
		
	protected AmpFiscalCalendar oldCalendar = null;
		
	/**
	 * Initialized report filters with a map of elements to filter by a list of filters each
	 * @param filterRules
	 */
	public MondrianReportFilters(Map<ReportElement, List<FilterRule>> filterRules) {
		this(filterRules, AmpARFilter.getDefaultCalendar());
	}
	
	public MondrianReportFilters() {
		this(AmpARFilter.getDefaultCalendar());
	}
	
	public MondrianReportFilters(Map<ReportElement, List<FilterRule>> filterRules, AmpFiscalCalendar calendar) {
		super(filterRules, calendar);
	}
	
	public MondrianReportFilters(AmpFiscalCalendar calendar) {
		super(calendar);
	}
			
	/**
	 * For internal use, because we need to force the use mdx properties filters for some dates 
	 * @param elem
	 * @param filterRule
	 */
	public void addFilterRule(ReportElement elem, FilterRule filterRule) {
		Set<String> RAW_COLUMNS_WITH_NAMES_ENDING_IN_ID = new HashSet<>(Arrays.asList(ColumnConstants.ACTIVITY_ID, ColumnConstants.INTERNAL_USE_ID, ColumnConstants.AMP_ID));
		// Check if this is a filter that must be in a group
		if (ElementType.ENTITY.equals(elem.type) 
				&& FiltersGroup.FILTER_GROUP.containsKey(elem.entity.getEntityName())) {
			
			String filterGroup = FiltersGroup.FILTER_GROUP.get(elem.entity.getEntityName());
			if (filterGroup.endsWith(" Id") && !RAW_COLUMNS_WITH_NAMES_ENDING_IN_ID.contains(filterGroup)) {
				// hack: do we really need to be able to filter by id fields instead of filtering by id the natural value field?
				// idea of hack: filtering by "Primary Sector Id" (irrespective of whether by id or value) is the exact same thing as filtering by "Primary Sector" by id
				filterGroup = filterGroup.substring(0, filterGroup.length() - 3); // Delete " Id"
			}
			if (!this.sqlFilterRules.containsKey(filterGroup))
				this.sqlFilterRules.put(filterGroup, new ArrayList<FilterRule>());
			this.sqlFilterRules.get(filterGroup).add(filterRule);
		}
		
		addFilterRule(filterRules, elem, filterRule);
	}
			
	/**
	 * Add a column/measure filter
	 * @param entity - column/measure to filter by
	 * @param filterRule - the filter rule to apply
	 */
	public void addFilterRule(NamedTypedEntity entity,  FilterRule filterRule) {
		if (entity.getEntityName().equals(ColumnConstants.GEOCODE)) {
			addFilterRule(new ReportColumn(ColumnConstants.LOCATION), MondrianReportUtils.postprocessGeocodeRule(filterRule));
			return;
		}
		addFilterRule(new ReportElement(entity), filterRule);
	}
	
	/**
	 * Adds a date range filter [from .. to] or [from .. infinite ) or (infinite .. to]
	 * @param from - the date to start from or null
	 * @param to - the date to end with or null
	 * @throws AmpApiException if range is invalid
	 */
	public void addDateRangeFilterRule(Date from, Date to) throws AmpApiException {
		addFilterRule(new ReportElement(ElementType.DATE), DateFilterUtils.getDateRangeFilterRule(from, to, calendar, oldCalendar));
	}
	
	/**
	 * adds a rule relating to a date column
	 * @param dateColumnName
	 * @param rule
	 */
	public void addDateFilterRule(String dateColumnName, FilterRule rule) {
		ReportColumn col = new ReportColumn(dateColumnName);
		addFilterRule(dateFilterRules, col, rule);
	}
	
	/**
	 * Adds a measures filter for list of years 
	 * @param years - years to filter by
	 * @param valuesToInclude - true if this years to be kept, false if this years must be excluded
	 * @throws Exception 
	 */
	public void addYearsFilterRule(List<Integer> years, boolean valuesToInclude) throws Exception {
		addFilterRule(new ReportElement(ElementType.YEAR), DateFilterUtils.getYearsFilterRule(years, calendar, valuesToInclude));
	}
	
	/**
	 * Adds a measures date list filter 
	 * @param dates - the dates to filter by 
	 * @param valuesToInclude - true if this dates must be kept, false if they must be excluded
	 * @throws AmpApiException if list is invalid
	 */
	public void addDatesFilterRule(List<Date> dates, boolean valuesToInclude) throws AmpApiException {
		addFilterRule(new ReportElement(ElementType.DATE), DateFilterUtils.getDatesFilterRule(dates, valuesToInclude));
	}
	
	@Deprecated
	/**
	 * Adds a single quarter filter over measures (no years filter) 
	 * @param quarter
	 * @param valueToInclude
	 * @throws Exception 
	 */
	public void addSingleQuarterFilterRule(Integer quarter, boolean valueToInclude) throws Exception {
		addFilterRule(new ReportElement(ElementType.QUARTER), MondrianUtils.getSingleQuarterFilterRule(quarter, calendar, valueToInclude));
	}
	
	@Deprecated
	/**
	 * Adds a single month filter over measures (no years filter) 
	 * @param month
	 * @param valueToInclude
	 * @throws Exception 
	 */
	public void addSingleMonthFilterRule(Integer month, boolean valueToInclude) throws Exception {
		addFilterRule(new ReportElement(ElementType.MONTH), MondrianUtils.getSingleMonthFilterRule(month, calendar, valueToInclude));
	}
	
//	/**
//	 * Adds a single date filter over a date column
//	 * @param column - the date column to filter 
//	 * @param date - date to be considered
//	 * @throws AmpApiException
//	 */
//	public void addSingleDateFilterRule(ReportColumn column, Date date) throws AmpApiException {
//		if (date == null)
//			throw new AmpApiException("Cannot add a filter to a null date");
//		addFilterRule(dateFilterRules, column, new FilterRule(DateTimeUtil.formatDate(date), true, false));
//	}

	public Map<String, List<FilterRule>> getSqlFilterRules() {
		return sqlFilterRules;
	}

	public void setSqlFilterRules(Map<String, List<FilterRule>> sqlFilterRules) {
		this.sqlFilterRules = sqlFilterRules;
	}
	
	public AmpFiscalCalendar getCalendar() {
		return calendar;
	}
	
	/**
	 * Configures the calendar type
	 * @param calendar
	 */
	public void setCalendar(AmpFiscalCalendar calendar) {
		this.calendar = calendar;
	}
	
	/**
	 * @return the oldCalendar
	 */
	public AmpFiscalCalendar getOldCalendar() {
		return oldCalendar;
	}

	/**
	 * @param oldCalendar the oldCalendar to set
	 */
	public void setOldCalendar(AmpFiscalCalendar oldCalendar) {
		this.oldCalendar = oldCalendar;
	}

	@Override public String toString() {
		return String.format("{filterRules: %s, dateFilterRules: %s, sqlFilterRules: %s}", filterRules, dateFilterRules, sqlFilterRules);
	}
}
