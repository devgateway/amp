/**
 * 
 */
package org.dgfoundation.amp.reports;

import java.util.HashSet;
import java.util.Set;

import org.dgfoundation.amp.ar.ColumnConstants;
import org.dgfoundation.amp.ar.MeasureConstants;

/**
 * Stores the set of columns that provide amounts and not text data
 * @author Nadejda Mandrescu
 */
public class AmountColumns {
	public final static Set<String> ACTIVITY_AMOUNTS = new HashSet<String>() {{
		add(ColumnConstants.CUMULATIVE_COMMITMENT);
		add(ColumnConstants.CUMULATIVE_DISBURSEMENT);
		add(ColumnConstants.CUMULATIVE_EXECUTION_RATE);
		add(MeasureConstants.PREVIOUS_MONTH_DISBURSEMENTS);
		add(MeasureConstants.PRIOR_ACTUAL_DISBURSEMENTS);
		add(ColumnConstants.PROPOSED_PROJECT_AMOUNT);
		add(MeasureConstants.SELECTED_YEAR_PLANNED_DISBURSEMENTS);
		add(MeasureConstants.TOTAL_COMMITMENTS);
		add(MeasureConstants.TOTAL_DISBURSEMENTS);
		add(ColumnConstants.UNCOMMITTED_BALANCE);
		add(ColumnConstants.UNCOMMITTED_CUMULATIVE_BALANCE);
		add(ColumnConstants.UNDISBURSED_CUMULATIVE_BALANCE);
		add(MeasureConstants.UNDISBURSED_BALANCE);
	}};
}
