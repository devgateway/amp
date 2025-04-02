/**
 * 
 */
package org.dgfoundation.amp.reports;

import java.util.HashSet;
import java.util.Set;

import org.dgfoundation.amp.ar.ColumnConstants;
import org.dgfoundation.amp.ar.MeasureConstants;

/**
 * Stores various groupings of Columns and Measures that require a special treatment
 * @author Nadejda Mandrescu
 */
public class CustomAmounts {
    /** columns that must be summed up by activity level (not automatically done by Mondrian / Saiku)*/
    @SuppressWarnings("serial")
    public final static Set<String> ACTIVITY_SUM_AMOUNTS = new HashSet<String>() {{
    }};
    
    /** columns that provide amounts and not text data */
    @SuppressWarnings("serial")
    public final static Set<String> ACTIVITY_AMOUNTS = new HashSet<String>() {{
        addAll(ACTIVITY_SUM_AMOUNTS);

        add(ColumnConstants.CUMULATIVE_EXECUTION_RATE);
        // note: some measures from legacy reports are processed as columns in Mondrian 
        add(MeasureConstants.PREVIOUS_MONTH_DISBURSEMENTS);
        add(ColumnConstants.TOTAL_GRAND_ACTUAL_COMMITMENTS);
        add(ColumnConstants.TOTAL_GRAND_ACTUAL_DISBURSEMENTS);
        add(ColumnConstants.UNCOMMITTED_BALANCE);
        add(MeasureConstants.FORECAST_EXECUTION_RATE);
        add(MeasureConstants.UNDISBURSED_BALANCE);
    }};
    
    /** all columns and measures that display % amounts */
    @SuppressWarnings("serial")
    public final static Set<String> PERCENTAGE_AMOUNTS = new HashSet<String>() {{
        add(MeasureConstants.EXECUTION_RATE);
        add(MeasureConstants.PERCENTAGE_OF_TOTAL_COMMITMENTS);
        add(MeasureConstants.PERCENTAGE_OF_TOTAL_DISBURSEMENTS);
        add(MeasureConstants.PLEDGES_PERCENTAGE_OF_DISBURSEMENT);
        add(MeasureConstants.AVERAGE_DISBURSEMENT_RATE);
        add(ColumnConstants.CUMULATIVE_EXECUTION_RATE);
        add(ColumnConstants.PROJECT_AGE_RATIO);
        add(MeasureConstants.FORECAST_EXECUTION_RATE);
        
    }};
    
    /** all amount Columns and Measures for which Amount Unit setting is not applicable */ 
    @SuppressWarnings("serial")
    public final static Set<String> UNIT_MULTIPLIER_NOT_APPLICABLE = new HashSet<String>() {{
        addAll(PERCENTAGE_AMOUNTS);
        add(ColumnConstants.ACTIVITY_COUNT);
        add(ColumnConstants.AGE_OF_PROJECT_MONTHS);
        add(ColumnConstants.AVERAGE_SIZE_OF_DISBURSEMENTS);
        add(ColumnConstants.AVERAGE_SIZE_OF_PROJECTS);
        add(ColumnConstants.CALCULATED_PROJECT_LIFE);
        add(ColumnConstants.PROPOSED_PROJECT_LIFE);
    }};
}
