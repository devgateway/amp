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
	/** columns that provide amounts and not text data */
	public final static Set<String> ACTIVITY_AMOUNTS = new HashSet<String>() {{
		add(ColumnConstants.CUMULATIVE_COMMITMENT);
		add(ColumnConstants.CUMULATIVE_DISBURSEMENT);
		add(ColumnConstants.CUMULATIVE_EXECUTION_RATE);
		// note: some measures from legacy reports are processed as columns in Mondrian 
		add(MeasureConstants.PREVIOUS_MONTH_DISBURSEMENTS);
		add(MeasureConstants.PRIOR_ACTUAL_DISBURSEMENTS);
		add(ColumnConstants.PROPOSED_PROJECT_AMOUNT);
		add(MeasureConstants.SELECTED_YEAR_PLANNED_DISBURSEMENTS);
		add(MeasureConstants.TOTAL_COMMITMENTS);
		add(MeasureConstants.TOTAL_DISBURSEMENTS);
		add(ColumnConstants.TOTAL_GRAND_ACTUAL_COMMITMENTS);
		add(ColumnConstants.TOTAL_GRAND_ACTUAL_DISBURSEMENTS);
		add(ColumnConstants.UNCOMMITTED_BALANCE);
		add(ColumnConstants.UNCOMMITTED_CUMULATIVE_BALANCE);
		add(ColumnConstants.UNDISBURSED_CUMULATIVE_BALANCE);
		add(MeasureConstants.UNDISBURSED_BALANCE);
	}};
	
	/** all columns and measures that display % amounts */
	public final static Set<String> PERCENTAGE_AMOUNTS = new HashSet<String>() {{
		add(MeasureConstants.EXECUTION_RATE);
		add(MeasureConstants.FORECAST_EXECUTION_RATE);
		add(MeasureConstants.CONSUMPTION_RATE);
		add(MeasureConstants.DISBURSMENT_RATIO);
		add(MeasureConstants.PERCENTAGE_OF_TOTAL_COMMITMENTS);
		add(MeasureConstants.PERCENTAGE_OF_TOTAL_DISBURSEMENTS);
		add(MeasureConstants.PLEDGES_PERCENTAGE_OF_DISBURSEMENT);
		add(ColumnConstants.CUMULATIVE_EXECUTION_RATE);
		add(ColumnConstants.PROJECT_AGE_RATIO);
		add(ColumnConstants.AVERAGE_DISBURSEMENT_RATE);
		
	}};
	
	/** all amount Columns and Measures for which Amount Unit setting is not applicable */ 
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
