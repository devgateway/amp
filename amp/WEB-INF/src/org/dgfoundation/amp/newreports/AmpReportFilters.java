package org.dgfoundation.amp.newreports;

import java.util.List;
import java.util.Map;

import org.digijava.module.aim.dbentity.AmpFiscalCalendar;

public class AmpReportFilters extends ReportFiltersImpl {

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

}
