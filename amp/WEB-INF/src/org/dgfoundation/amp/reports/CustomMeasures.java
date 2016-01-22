/**
 * 
 */
package org.dgfoundation.amp.reports;

import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.dgfoundation.amp.ar.MeasureConstants;

/**
 * Stores different sets of measures with some custom rules
 * 
 * @author Nadejda Mandrescu
 */
public class CustomMeasures {
	/** measures whose distribution per year/quarter/months must be hidden from the output */
	public final static Set<String> NO_DISTRIBUTION = new HashSet<String>() {{
		add(MeasureConstants.EXECUTION_RATE);
		add(MeasureConstants.PRIOR_ACTUAL_DISBURSEMENTS);
		add(MeasureConstants.CUMULATED_DISBURSEMENTS);
		add(MeasureConstants.SELECTED_YEAR_PLANNED_DISBURSEMENTS);
		add(MeasureConstants.UNCOMMITTED_BALANCE);
		add(MeasureConstants.CUMULATIVE_COMMITMENT);
		add(MeasureConstants.CUMULATIVE_DISBURSEMENT);
		add(MeasureConstants.CUMULATIVE_EXECUTION_RATE);
		add(MeasureConstants.UNCOMMITTED_CUMULATIVE_BALANCE);
		add(MeasureConstants.UNDISBURSED_CUMULATIVE_BALANCE);
	}};
	
	/** measures that must not be filtered by dates filter */ 
	public final static Set<String> NO_DATE_FILTERS = new HashSet<String>() {{
		add(MeasureConstants.PRIOR_ACTUAL_DISBURSEMENTS);
		add(MeasureConstants.CUMULATED_DISBURSEMENTS);
		add(MeasureConstants.SELECTED_YEAR_PLANNED_DISBURSEMENTS);
		add(MeasureConstants.CUMULATIVE_COMMITMENT);
		add(MeasureConstants.CUMULATIVE_DISBURSEMENT);
		add(MeasureConstants.CUMULATIVE_EXECUTION_RATE);
		add(MeasureConstants.UNCOMMITTED_CUMULATIVE_BALANCE);
		add(MeasureConstants.UNDISBURSED_CUMULATIVE_BALANCE);
	}};
	
	/** measures without raw totals */
	public final static Set<String> NO_RAW_TOTALS = new HashSet<String>() {{
		add(MeasureConstants.EXECUTION_RATE);
		add(MeasureConstants.CUMULATIVE_EXECUTION_RATE);
	}};
	
	/** calculated members dependencies, that will be removed if dependent measure is not present */
	public final static Map<String, List<String>> MEASURE_DEPENDENCY = new HashMap<String, List<String>>() {{
		put(MeasureConstants.EXECUTION_RATE, Arrays.asList(MeasureConstants.ACTUAL_DISBURSEMENTS, MeasureConstants.PLANNED_DISBURSEMENTS));
		put(MeasureConstants.CUMULATIVE_COMMITMENT, Arrays.asList(MeasureConstants.ACTUAL_COMMITMENTS));
		put(MeasureConstants.CUMULATIVE_DISBURSEMENT, Arrays.asList(MeasureConstants.ACTUAL_DISBURSEMENTS));
		put(MeasureConstants.CUMULATIVE_EXECUTION_RATE, Arrays.asList(MeasureConstants.ACTUAL_COMMITMENTS, MeasureConstants.ACTUAL_DISBURSEMENTS));
		put(MeasureConstants.UNCOMMITTED_CUMULATIVE_BALANCE, Arrays.asList(MeasureConstants.ACTUAL_COMMITMENTS));
		put(MeasureConstants.UNDISBURSED_CUMULATIVE_BALANCE, Arrays.asList(MeasureConstants.ACTUAL_COMMITMENTS, MeasureConstants.ACTUAL_DISBURSEMENTS));
		put("Total Filtered Actual Disbursements", Arrays.asList(MeasureConstants.ACTUAL_DISBURSEMENTS));
		put("Total Filtered Planned Disbursements", Arrays.asList(MeasureConstants.PLANNED_DISBURSEMENTS));
	}};
	
	/** MTEF type measures that require some workarounds */
	public final static Set<String> MTEFs = new HashSet<String>() {{
		add("MTEF");
		add(MeasureConstants.MTEF_PROJECTIONS);
		add(MeasureConstants.REAL_MTEFS);
		add(MeasureConstants.PIPELINE_MTEF_PROJECTIONS);
		add(MeasureConstants.PROJECTION_MTEF_PROJECTIONS);
	}};
	
	/** Proposed Project Cost dependent Measures (not columns!) */
	public final static Set<String> PPC_DEPENDENCY = new HashSet<String>() {{
		add(MeasureConstants.UNCOMMITTED_BALANCE);
		add(MeasureConstants.UNCOMMITTED_CUMULATIVE_BALANCE);
	}};
}
