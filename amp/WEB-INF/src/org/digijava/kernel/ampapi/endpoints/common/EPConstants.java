/**
 * 
 */
package org.digijava.kernel.ampapi.endpoints.common;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.dgfoundation.amp.ar.ArConstants;
import org.dgfoundation.amp.reports.ActivityType;


/**
 * Endpoint related constants
 * @author Nadejda Mandrescu
 *
 */
public class EPConstants {
	public static final String LANGUAGE = "language";
	
	// config update
	public static final String REPORT_NAME = "name";
	public static final String IS_CUSTOM = "custom";
	public static final String REGENERATE = "regenerate";
	public static final String ADD_COLUMNS = "add_columns";
	public static final String ADD_HIERARCHIES = "add_hierarchies";
	public static final String ADD_MEASURES = "add_measures";
	public static final String SORTING = "sorting";
	public static final String DO_ROW_TOTALS = "rowTotals";
    public static final String FORCE_HEADERS = "forceHeaders";
    public static final String PROJECT_TYPE = "projectType";
    public static final String REPORT_TYPE = "reportType";
    
    // report type & activities configurations
    public static final String DEFAULT_REPORT_TYPE = "D";
    /** maps API report type to internal report type ids */
    public static final Map<String, Integer> REPORT_TYPE_ID_MAP = new HashMap<String, Integer>() {{
    	put("D", ArConstants.DONOR_TYPE);
    	put("C", ArConstants.COMPONENT_TYPE);
    	put("P", ArConstants.PLEDGES_TYPE);
    }};
    
    public static final Map<String, List<String>> REPORT_TYPE_ACTIVITY_MAP = new HashMap<String, List<String>>() {{
    	put("D", ActivityType.STR_VALUES);
    	put("C", Arrays.asList(ActivityType.ACTIVITY.toString(), ActivityType.SSC_ACTIVITY.toString()));
    	put("P", Arrays.asList(ActivityType.PLEDGE.toString()));
    }};
	
	public static final String ERROR = "error";
	public static final String NA = "N/A";
	
	public static final String FILTERS = "filters";
	
	// settings
	public static final String SETTINGS = "settings";
	
	// map import/export
	public static final String API_STATE_TITLE = "title";
	public static final String API_STATE_DESCRIPTION = "description";
	public static final String API_STATE_BLOB = "stateBlob";
	public static final String API_STATE_LAYERS_VIEW = "layers-view";
	public static final String API_STATE_REPORT_EXPORT_DESCRIPTION = "Export to Map";
	public static final String API_STATE_REPORT_EXPORT = "reportExportId=";
}
