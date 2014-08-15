/**
 * 
 */
package org.dgfoundation.amp.newreports;

import java.util.Map;

import org.dgfoundation.amp.reports.mondrian.AmpARFilterTranslator;

/**
 * Generic Report Filter - can be used to populate it Manually, either via {@link AmpARFilterTranslator} support
 * @author Nadejda Mandrescu
 */
public class ReportFiltersImpl implements ReportFilters {
	private Map<ReportElement, FilterRule> filterRules;
	
	public ReportFiltersImpl(Map<ReportElement, FilterRule> filterRules) {
		this.filterRules = filterRules;
	}
	
	@Override
	public Map<ReportElement, FilterRule> getFilterRules() {
		return filterRules;
	}
}
