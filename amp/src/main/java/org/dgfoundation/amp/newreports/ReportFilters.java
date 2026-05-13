package org.dgfoundation.amp.newreports;

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
    Map<ReportElement, FilterRule> getFilterRules();
    
    /**
     * all the filter rules. In a sane implementation this would return {@link #getFilterRules()}, but in AMP we add the Date rules manually on top of them (see {@link AmpReportFilters#getDateFilterRules()})
     * @return
     */
    default Map<ReportElement, FilterRule> getAllFilterRules() {
        return getFilterRules();
    }
}
