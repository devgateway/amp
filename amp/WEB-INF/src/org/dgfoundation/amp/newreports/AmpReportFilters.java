package org.dgfoundation.amp.newreports;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.dgfoundation.amp.algo.AmpCollections;
import org.dgfoundation.amp.ar.AmpARFilter;
import org.dgfoundation.amp.newreports.ReportElement.ElementType;
import org.digijava.kernel.ampapi.endpoints.filters.FiltersConstants;
import org.digijava.kernel.ampapi.endpoints.util.DateFilterUtils;
import org.digijava.kernel.ampapi.endpoints.util.FilterUtils;
import org.digijava.kernel.ampapi.exception.AmpApiException;
import org.digijava.module.aim.dbentity.AmpFiscalCalendar;

/**
 * an AMP-specific extension of {@link ReportFilters}
 * @author Dolghier Constantin
 *
 */
public class AmpReportFilters extends ReportFiltersImpl {

    private final Map<ReportColumn, FilterRule> dateFilterRules = new HashMap<>();

    @JsonIgnore
    protected AmpFiscalCalendar calendar;

    /**
     * also known as "selected year"
     */
    @JsonProperty(FiltersConstants.COMPUTED_YEAR)
    protected Integer computedYear;

    public AmpReportFilters(Map<ReportElement, FilterRule> filterRules) {
        super(filterRules);
    }
    
    public AmpReportFilters() {
        this(AmpARFilter.getDefaultCalendar());
    }

    public AmpReportFilters(AmpFiscalCalendar calendar) {
        this.calendar = calendar;
    }
    
    public Integer getComputedYear() {
        return computedYear;
    }

    public void setComputedYear(Integer computedYear) {
        this.computedYear = computedYear;
    }
    
    public AmpFiscalCalendar getCalendar() {
        return calendar;
    }
        
    public AmpReportFilters(Map<ReportElement, FilterRule> filterRules, AmpFiscalCalendar calendar) {
        super(filterRules);
        this.calendar = calendar;
    }

    /***
     * the date-columns filter rules. They lie separate here instead of being merged into {@link #getFilterRules()} because  this is the way they have been implemented in the old Mondrian/API/frontend
     * TODO: make this field disappear when reimplementing (?) the filter widget backend API
     * @return
     */
    @JsonIgnore
    public Map<ReportColumn, FilterRule> getDateFilterRules() {
        return this.dateFilterRules;
    }

    @JsonAnyGetter
    public Map<String, FilterRule> getAllFilterRulesForJackson() {
        return AmpCollections.remap(getAllFilterRules(), this::idForReportElement, Function.identity(), false);
    }

    private String idForReportElement(ReportElement re) {
        String id;
        if (ElementType.ENTITY.equals(re.type)) {
            String entityName = re.entity.getEntityName();
            id = FilterUtils.INSTANCE.idFromColumnName(entityName);
            if (id == null) {
                throw new RuntimeException("No matching filter for column name: " + entityName);
            }
        } else {
            id = re.type.toString().toLowerCase();
        }
        return id;
    }

    /**
     * concatenates {@link #getFilterRules()} with {@link #getDateFilterRules()}
     */
    @JsonIgnore
    @Override
    public Map<ReportElement, FilterRule> getAllFilterRules() {
        if (dateFilterRules == null || dateFilterRules.isEmpty())
            return getFilterRules();
        
        Map<ReportElement, FilterRule> res = new HashMap<>(getFilterRules());
        res.putAll(AmpCollections.remap(getDateFilterRules(), rc -> new ReportElement(rc), Function.identity(), false));
        return res;
    }

    protected <T> void addFilterRule(Map<T, FilterRule> filterRules, T elem, FilterRule filterRule) {
        filterRules.put(elem, filterRule);
    }
    
    public static int getReportSelectedYear(ReportSpecification spec, int defaultYear) {
        AmpReportFilters filters = (AmpReportFilters) spec.getFilters();
        Integer year = filters == null ? null : filters.getComputedYear();
        // if not set, then use default year
        if (year == null) {
            year = defaultYear;
        }
        return year;
    }
    
    /**
     * Adds a date range filter [from .. to] or [from .. infinite ) or (infinite .. to]
     * @param from - the date to start from or null
     * @param to - the date to end with or null
     * @throws AmpApiException if range is invalid
     */
    public void addDateRangeFilterRule(Date from, Date to) throws AmpApiException {
        addFilterRule(new ReportElement(ElementType.DATE), DateFilterUtils.getDateRangeFilterRule(from, to, calendar, null));
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
        //validate
        addFilterRule(dateFilterRules, column, DateFilterUtils.getDateRangeFilterRule(from, to, calendar, null));
    }
    
    /**
     * Adds a single year filter over measures
     * @param year
     * @param valueToInclude
     * @throws Exception 
     */
    public void addSingleYearFilterRule(Integer year, boolean valueToInclude) throws Exception {
        addFilterRule(new ReportElement(ElementType.YEAR), DateFilterUtils.getSingleYearFilterRule(year, calendar, valueToInclude));
    }
    
    /**
     * Adds a measures filter between [from .. to] or [from .. infinite) or (-infinite .. to] year ranges.
     * @param from - the year to start from or null
     * @param to - the year to end with or null
     * @throws Exception 
     */
    public void addYearsRangeFilterRule(Integer from, Integer to) throws Exception {
        addFilterRule(new ReportElement(ElementType.YEAR), 
                DateFilterUtils.getYearsRangeFilter(from, to, null, calendar));
    }
    
    /**
     * Adds a single date filter over measures
     * @param date
     * @param valueToInclude
     * @throws AmpApiException
     */
    public void addSingleDateFilterRule(Date date, boolean valueToInclude) throws AmpApiException {
        addFilterRule(new ReportElement(ElementType.DATE), DateFilterUtils.getSingleDateFilterRule(date, valueToInclude));
    }
}
