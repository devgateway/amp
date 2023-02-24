package org.dgfoundation.amp.newreports;

import java.util.HashMap;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 * a concrete implementation of {@link ReportFilters}
 * @author Dolghier Constantin
 *
 */
public class ReportFiltersImpl implements ReportFilters {
    protected final Map<ReportElement, FilterRule> filterRules = new HashMap<>();
    
    public ReportFiltersImpl() {}
    
    public ReportFiltersImpl(Map<ReportElement, FilterRule> filterRules) {
        this.filterRules.putAll(filterRules);
    }
        
    @Override
    @JsonIgnore
    public Map<ReportElement, FilterRule> getFilterRules() {
        return filterRules;
    }
    
    /**
     * Adds Report Entity Filter
     * @param elem
     * @param filterRule
     */
    public void addFilterRule(ReportElement elem, FilterRule filterRule) {
        filterRules.put(elem, filterRule);
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
