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
	public static final String TRANSLATIONS = "translations";

    // the request attribute name to set response status
    public static final String RESPONSE_STATUS = "response_status";
	
	// config update
	public static final String REPORT_NAME = "name";
	public static final String IS_CUSTOM = "custom";
	public static final String REGENERATE = "regenerate";
	public static final String ADD_COLUMNS = "add_columns";
	public static final String ADD_HIERARCHIES = "add_hierarchies";
	public static final String ADD_MEASURES = "add_measures";
	public static final String SORTING = "sorting";
	public static final String SHOW_EMPTY_ROWS = "show_empty_rows";
	public static final String SHOW_EMPTY_COLUMNS = "show_empty_cols"; // this is for column groups (quarter, year)
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
	
	// fm constants
	public static final String REPORTING_FIELDS = "reporting-fields";
	public static final String ENABLED_MODULES = "enabled-modules";
	public static final String DETAIL_MODULES = "detail-modules";
	public static final String DETAIL_ALL_ENABLED_MODULES = "all-enabled";
	
	// menu constants
	public static final String MENU_NAME = "name"; 
	public static final String MENU_TOOLTIP = "tooltip";
	public static final String MENU_URL = "url";
	public static final String MENU_CHILDREN = "children";
	public static final String MENU_OPEN_POPUP = "popup";
	public static final String MENU_OPEN_TAB = "tab";
	public static final String MENU_POST = "post";
	public static final String MENU_LANUGAGE = "language";
	
	//footer constants
	public static final String BUILD_DATE = "buildDate";
	public static final String AMP_VERSION = "ampVersion";
	public static final String SITE_ID = "siteId";
	public static final String TRACKING_URL = "trackingUrl";
	public static final String TRACKING_ENABLED = "trackingEnabled";
	public static final String ADMIN_LINKS = "adminLinks";
	public static final String ADMIN_LINK_NAME = "admin";
	public static final String USERDEV_LINK_NAME = "User/Dev Mode";
	public static final String FOOTER_TEXT = "footerText";
	public static final String LINK_NAME = "name";
	public static final String LINK_URL = "url";
	

	
}
