/**
 * 
 */
package org.digijava.kernel.ampapi.endpoints.util;

import java.util.LinkedHashMap;
import java.util.Map;

import org.dgfoundation.amp.ar.MeasureConstants;

/**
 * GIS related constants
 * @author Nadejda Mandrescu
 */
public class GisConstants {
	
	public static final String COMMITMENTS = "Commitments";  
	public static final String DISBURSEMENTS = "Disbursements";
	public static final String EXPENDITURES = "Expenditures";
	
	@SuppressWarnings("serial")
	/**
	 * The order is important - this is the priority order between measures, 
	 * that is used to convert the report config to gis funding type option   
	 */
	public static final Map<String, String> MEASURE_TO_NAME_MAP = new LinkedHashMap<String, String>() {{
		put(MeasureConstants.ACTUAL_COMMITMENTS, COMMITMENTS);
		put(MeasureConstants.ACTUAL_DISBURSEMENTS, DISBURSEMENTS);
		put(MeasureConstants.ACTUAL_EXPENDITURES, EXPENDITURES);
	}};
}
