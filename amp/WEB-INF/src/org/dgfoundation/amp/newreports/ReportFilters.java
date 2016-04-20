package org.dgfoundation.amp.newreports;

import java.util.List;
import java.util.Map;

/**
 * @author Dolghier Constantin
 *
 */
public interface ReportFilters {
	
	/**
	 * the regular filter rules, excluding dates (please see {@link #getDateFilterRules()} for an explanation) 
	 * @return
	 */
	public Map<ReportElement, List<FilterRule>> getFilterRules();
	
	public default Map<ReportElement, List<FilterRule>> getAllFilterRules() {
		return getFilterRules();
	}
}
