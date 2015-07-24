/**
 * 
 */
package org.digijava.kernel.ampapi.endpoints.common;


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
	
	public static final String ERROR = "error";
	public static final String NA = "N/A";
	
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
}
