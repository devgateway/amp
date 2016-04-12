package org.dgfoundation.amp.newreports;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.codehaus.jackson.annotate.JsonIgnore;

public class ReportFiltersImpl implements ReportFilters {
	protected Map<ReportElement, List<FilterRule>> filterRules = new HashMap<ReportElement, List<FilterRule>>();
	
	public ReportFiltersImpl() {}
	
	public ReportFiltersImpl(Map<ReportElement, List<FilterRule>> filterRules) {
		this.filterRules = filterRules;
	}
		
	@Override
	@JsonIgnore
	public Map<ReportElement, List<FilterRule>> getFilterRules() {
		return filterRules;
	}

	public void addFilterRule(ReportElement elem, FilterRule filterRule) {
		filterRules.computeIfAbsent(elem, z -> new ArrayList<>()).add(filterRule);
	}

}
