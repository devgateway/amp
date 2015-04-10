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
 * @author Nadejda Mandrescu
 */
public class CustomMeasures {
	/** measures whose distribution per year/quarter/months must be hidden from the output */
	public final static Set<String> NO_DISTRIBUTION = new HashSet<String>() {{
		add(MeasureConstants.EXECUTION_RATE);
	}};
	
	/** measures without raw totals */
	public final static Set<String> NO_RAW_TOTALS = new HashSet<String>() {{
		add(MeasureConstants.EXECUTION_RATE);
	}};
	
	public final static Map<String, List<String>> MEASURE_DEPENDENCY = new HashMap<String, List<String>>() {{
		put(MeasureConstants.EXECUTION_RATE, Arrays.asList(MeasureConstants.ACTUAL_DISBURSEMENTS, MeasureConstants.PLANNED_DISBURSEMENTS));
		put("Total Filtered Actual Disbursements", Arrays.asList(MeasureConstants.ACTUAL_DISBURSEMENTS));
		put("Total Filtered Planned Disbursements", Arrays.asList(MeasureConstants.PLANNED_DISBURSEMENTS));
	}};
}
