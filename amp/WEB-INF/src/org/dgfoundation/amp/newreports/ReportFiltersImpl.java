/**
 * 
 */
package org.dgfoundation.amp.newreports;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.dgfoundation.amp.reports.mondrian.AmpARFilterTranslator;

/**
 * Generic Report Filter - can be used to populate it Manually, either via {@link AmpARFilterTranslator} support
 * @author Nadejda Mandrescu
 */
public class ReportFiltersImpl implements ReportFilters {
	private Map<ReportElement, List<FilterRule>> filterRules = new HashMap<ReportElement, List<FilterRule>>();
	
	/**
	 * Initialized report filters with a map of elements to filter by a list of filters each
	 * @param filterRules
	 */
	public ReportFiltersImpl(Map<ReportElement, List<FilterRule>> filterRules) {
		this.filterRules = filterRules;
	}
	
	public ReportFiltersImpl() {
	}
	
	@Override
	public Map<ReportElement, List<FilterRule>> getFilterRules() {
		return filterRules;
	}
	
	public void addFilterRule(ReportElement elem, FilterRule filterRule) {
		List<FilterRule> filtersList = filterRules.get(elem);
		if (filtersList == null) {
			filtersList = new ArrayList<FilterRule>();
			filterRules.put(elem, filtersList);
		}
		filtersList.add(filterRule);
	}
}
