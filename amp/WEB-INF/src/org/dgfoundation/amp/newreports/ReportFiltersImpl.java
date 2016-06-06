package org.dgfoundation.amp.newreports;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.codehaus.jackson.annotate.JsonIgnore;

/**
 * a concrete implementation of {@link ReportFilters}
 * @author Dolghier Constantin
 *
 */
public class ReportFiltersImpl implements ReportFilters {
	protected final Map<ReportElement, List<FilterRule>> filterRules = new HashMap<ReportElement, List<FilterRule>>();
	
	public ReportFiltersImpl() {}
	
	public ReportFiltersImpl(Map<ReportElement, List<FilterRule>> filterRules) {
		this.filterRules.putAll(filterRules);
	}
		
	@Override
	@JsonIgnore
	public Map<ReportElement, List<FilterRule>> getFilterRules() {
		return filterRules;
	}
	
	/**
	 * Adds Report Entity Filter
	 * @param elem
	 * @param filterRule
	 */
	public void addFilterRule(ReportElement elem, FilterRule filterRule) {
		filterRules.computeIfAbsent(elem, z -> new ArrayList<>()).add(filterRule);
	}
	
	/**
     * Adds a column/measure filter
     * @param entity - column/measure to filter by
     * @param filterRule - the filter rule to apply
     */
    public void addFilterRule(NamedTypedEntity entity,  FilterRule filterRule) {
        addFilterRule(new ReportElement(entity), filterRule);
    }

}
