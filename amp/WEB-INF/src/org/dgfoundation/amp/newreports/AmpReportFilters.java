package org.dgfoundation.amp.newreports;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

import org.codehaus.jackson.annotate.JsonIgnore;
import org.dgfoundation.amp.algo.AmpCollections;
import org.digijava.module.aim.dbentity.AmpFiscalCalendar;

public class AmpReportFilters extends ReportFiltersImpl {

	protected final Map<ReportColumn, List<FilterRule>> dateFilterRules = new HashMap<ReportColumn, List<FilterRule>>();

	protected AmpFiscalCalendar calendar;
	protected Integer computedYear;

	public AmpReportFilters(Map<ReportElement, List<FilterRule>> filterRules) {
		super(filterRules);
	}

	public AmpReportFilters(AmpFiscalCalendar calendar) {
		this.calendar = calendar;
	}
	
	public Integer getComputedYear() {
		return computedYear;
	}
	
 	public AmpFiscalCalendar getCalendar() {
 		return calendar;
 	}
		
	public AmpReportFilters(Map<ReportElement, List<FilterRule>> filterRules, AmpFiscalCalendar calendar) {
		super(filterRules);
		this.calendar = calendar;
	}

	/***
	 * the date-columns filter rules. They lie separate here instead of being merged into {@link #getFilterRules()} because  this is the way they have been implemented in the old Mondrian/API/frontend
	 * TODO: make this field disappear when reimplementing (?) the filter widget backend API
	 * @return
	 */
	@JsonIgnore
	public Map<ReportColumn, List<FilterRule>> getDateFilterRules() {
		return this.dateFilterRules;
	}

	@JsonIgnore
	@Override
	public Map<ReportElement, List<FilterRule>> getAllFilterRules() {
		if (dateFilterRules == null || dateFilterRules.isEmpty())
			return getFilterRules();
		
		Map<ReportElement, List<FilterRule>> res = new HashMap<>(getFilterRules());
		res.putAll(AmpCollections.remap(getDateFilterRules(), rc -> new ReportElement(rc), Function.identity(), false));
		return res;
	}
	
	/**
	 * These are basically the activity dates filters
	 * called through reflection during json, DO NOT DELETE
	 */
	public Map<String, List<FilterRule>> getColumnDateFilterRules() {
		return AmpCollections.remap(dateFilterRules, ReportColumn::getColumnName, Function.identity(), false);
	}
	
	protected <T> void addFilterRule(Map<T, List<FilterRule>> filterRules, T elem, FilterRule filterRule) {
		filterRules.computeIfAbsent(elem, ignored -> new ArrayList<>()).add(filterRule);
	}
}
