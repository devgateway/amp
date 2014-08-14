package org.dgfoundation.amp.newreports;

import java.util.Map;

/**
 * TODO: specify a generic filters. Should we reuse AmpARFilter?
 * @author Dolghier Constantin
 *
 */
public interface ReportFilters {
	
	public Map<ReportElement, FilterRule> getFilterRules();
}
