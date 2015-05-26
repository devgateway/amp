/**
 * 
 */
package org.digijava.kernel.ampapi.endpoints.util;

import java.util.LinkedHashSet;
import java.util.Set;

import org.dgfoundation.amp.ar.MeasureConstants;

/**
 * GIS related constants
 * @author Nadejda Mandrescu
 */
public class GisConstants {
	
	/*
	public static final String COMMITMENTS = "Commitments";  
	public static final String DISBURSEMENTS = "Disbursements";
	public static final String EXPENDITURES = "Expenditures";
	*/
	public static final String USE_ICONS_FOR_SECTORS_IN_PROJECT_LIST =  "Use icons for Sectors in Project List";
	
	/**
	 * The order is important - this is the priority order between measures, 
	 * that is used to convert the report config to gis funding type option   
	 */
	/* 
	public static final Map<String, String> MEASURE_TO_NAME_MAP = new LinkedHashMap<String, String>() {{
		put(MeasureConstants.ACTUAL_COMMITMENTS, COMMITMENTS);
		put(MeasureConstants.ACTUAL_DISBURSEMENTS, DISBURSEMENTS);
		put(MeasureConstants.ACTUAL_EXPENDITURES, EXPENDITURES);
	}};
	*/
	
	/**
	 * Set of measures that can be used in GIS module as funding type options.
	 * The priority doesn't matter anymore, but still adding some order via LinkedHashSet just for display.
	 */
	public static final Set<String> FUNDING_TYPES = new LinkedHashSet<String>() {{
		add(MeasureConstants.ACTUAL_COMMITMENTS);
		add(MeasureConstants.ACTUAL_DISBURSEMENTS);
		add(MeasureConstants.ACTUAL_EXPENDITURES);
		add(MeasureConstants.PLANNED_COMMITMENTS);
		add(MeasureConstants.PLANNED_DISBURSEMENTS);
		add(MeasureConstants.PLANNED_EXPENDITURES);
		add(MeasureConstants.BILATERAL_SSC_COMMITMENTS);
		add(MeasureConstants.TRIANGULAR_SSC_COMMITMENTS);
		add(MeasureConstants.UNDISBURSED_BALANCE);
		add(MeasureConstants.OFFICIAL_DEVELOPMENT_AID_COMMITMENTS);
	}};
}
