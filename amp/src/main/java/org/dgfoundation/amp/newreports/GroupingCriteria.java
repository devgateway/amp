package org.dgfoundation.amp.newreports;

/**
 * class describing the timeframe by which to group funding data in the report
 * @author Dolghier Constantin
 *
 */
public enum GroupingCriteria {
    
    GROUPING_YEARLY,
    
    GROUPING_QUARTERLY,
    
    GROUPING_MONTHLY,
    
    /**
     * only show totals
     */
    GROUPING_TOTALS_ONLY
}
