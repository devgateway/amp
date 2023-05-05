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
 * 
 * @author Nadejda Mandrescu
 */
public class EPConstants {
    public static final String LANGUAGE = "language";
    public static final String TRANSLATIONS = "translations";

    // the request attribute name to set response status
    public static final String RESPONSE_STATUS = "response_status_marker";
    public static final String RESPONSE_HEADERS_MAP = "response_headers_map";
    
    // JSON Filters
    public static final String JSON_FILTERS = "jsonFilters";
    public static final String JSON_FILTER_AMOUNT_CELL = "jsonFilter.AmountCell";
    
    // config update
    public static final String ID = "id";
    public static final String LABEL = "label";
    public static final String VALUE = "value";
    public static final String TYPE = "type";
    public static final String REPORT_NAME = "name";
    public static final String IS_CUSTOM = "custom";
    public static final String ADD_COLUMNS = "add_columns";
    public static final String COLUMNS_WITH_IDS = "columns_with_ids";
    public static final String ADD_HIERARCHIES = "add_hierarchies";
    public static final String ADD_MEASURES = "add_measures";
    public static final String RAW_VALUES = "raw_values";
    public static final String SORTING = "sorting";
    public static final String SHOW_EMPTY_ROWS = "show_empty_rows";
    public static final String SHOW_EMPTY_COLUMNS = "show_empty_cols"; // this is for column groups (quarter, year)
    public static final String SHOW_ORIGINAL_CURRENCY = "show_original_currency";
    public static final String FORCE_HEADERS = "forceHeaders";
    public static final String PROJECT_TYPE = "projectType";
    public static final String REPORT_TYPE = "reportType";
    public static final String INFO = "info";
    public static final String GENERATED_HEADERS = "generatedHeaders";
    public static final String STATS = "stats";
    public static final String WARNINGS = "warnings";
    public static final String MD5_TOKEN = "MD5";
    public static final String COUNT = "count";
    public static final String SUMMARY = "summary";
    public static final String GROUPING_OPTION = "groupingOption";
    public static final String INCLUDE_LOCATION_CHILDREN = "include-location-children";
    
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
    public static final String API_STATE_REPORT_EXPORT_DESCRIPTION = "Export to Map";
    public static final String API_STATE_REPORT_EXPORT = "reportExportId=";
    
    // fm constants
    public static final String REPORTING_FIELDS = "reporting-fields";
    public static final String ENABLED_MODULES = "enabled-modules";
    public static final String DETAIL_MODULES = "detail-modules";
    public static final String DETAIL_ALL_ENABLED_MODULES = "all-enabled";
    public static final String DETAILS_FLAT = "detail-flat";
    public static final String FULL_ENABLED_PATHS = "full-enabled-paths";
    public static final String FM_ENABLED = "__enabled";
    public static final String FM_PATHS_FILTER = "fm-paths";
    public static final String WS_MEMBER_IDS = "ws-member-ids";
    public static final String FM_TREE = "fm-tree";

    //tab dependency of each filter
    /*
     * var filterInstancesNames = {donors: 'Funding Organizations', sectors : 'Sector', programs: 'Programs', 
      activity: 'Activity', allAgencies: 'All Agencies', financials: 'Financial',
          locations: 'Location', others: 'Other'};
     */
    public static final String TAB_ORGANIZATIONS = "Funding Organizations";
    public static final String TAB_SECTORS = "Sector";
    public static final String TAB_PROGRAMS = "Programs";
    public static final String TAB_ACTIVITY = "Activity";
    public static final String TAB_PLEDGE = "Pledge";
    public static final String TAB_ALL_AGENCIES = "All Agencies";
    public static final String TAB_FINANCIALS = "Financial";
    public static final String TAB_LOCATIONS = "Location";
    public static final String TAB_OTHER= "Other";
    public static final String TAB_UNASSIGNED= "Unassigned";

    public static final String ISO8601_DATE_AND_TIME_FORMAT = "yyyy-MM-dd'T'HH:mm:ss.SSSZ";
    public static final String ISO8601_DATE_FORMAT = "yyyy-MM-dd";
    public static final String ACTIVITY_DOCUMENTS = "activity_documents";
    /**
     * Map containing the length of date strings used for validation
     * "2019-02-26T11:08:56.235-0200" - 28 characters
     * "2019-02-26" - 10 characters
     */
    public static final Map<String, Integer> DATE_FORMAT_STRICT_LENGTH = new HashMap<String, Integer>() {{
        put(ISO8601_DATE_AND_TIME_FORMAT, 28);
        put(ISO8601_DATE_FORMAT, 10);
    }};
    
}
