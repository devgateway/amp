package org.dgfoundation.amp.newreports;

import java.util.List;
import java.util.Map;

/**
 * @author Dolghier Constantin
 *
 */
public interface ReportFilters {
	
	/**
	 * the regular filter rules, excluding dates (please see {@link AmpReportFilters#getDateFilterRules()} for an explanation) 
	 * @return
	 */
	public Map<ReportElement, List<FilterRule>> getFilterRules();
	
	/**
	 * all the filter rules. In a sane implementation this would return {@link #getFilterRules()}, but in AMP we add the Date rules manually on top of them (see {@link AmpReportFilters#getDateFilterRules()})
	 * @return
	 */
	public default Map<ReportElement, List<FilterRule>> getAllFilterRules() {
		return getFilterRules();
	}
}
