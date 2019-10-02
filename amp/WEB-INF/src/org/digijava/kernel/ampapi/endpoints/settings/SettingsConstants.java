package org.digijava.kernel.ampapi.endpoints.settings;

import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import org.dgfoundation.amp.ar.MeasureConstants;
import org.digijava.module.aim.helper.GlobalSettingsConstants;

/**
 * Settings related constants
 * 
 * @author Nadejda Mandrescu
 */
public class SettingsConstants {

    public static final String ID = "id";
    public static final String CODE = "code";
    public static final String CALENDAR= "calendar";
    public static final String FUNDING_TYPE_ID = "funding-type";
    public static final String CURRENCY_ID = "currency-" + CODE;
    public static final String CALENDAR_TYPE_ID = CALENDAR + "-" + ID;
    public static final String CALENDAR_IS_FISCAL= CALENDAR + "-is-fiscal";

    public static final String CALENDAR_CURRENCIES_ID = CALENDAR + "-currencies";
    public static final String AMOUNT_FORMAT_ID = "amount-format";


    public static final String YEAR_RANGE_ID = "year-range";

    public static final String DEFAULT_FUNDING_TYPE_ID = MeasureConstants.ACTUAL_COMMITMENTS;

    public static final String DECIMAL_SYMBOL = "decimal-symbol";
    public static final String MAX_FRACT_DIGITS = "max-frac-digits";
    public static final String USE_GROUPING = "use-grouping";
    public static final String GROUP_SEPARATOR = "group-separator";
    public static final String GROUP_SIZE = "group-size";
    public static final String AMOUNT_UNITS = "number-divider";
    public static final String AMOUNT_UNITS_1 = "amount-units-1";
    public static final String AMOUNT_UNITS_1000 = "amount-units-1000";
    public static final String AMOUNT_UNITS_1000000 = "amount-units-1000000";
    public static final String AMOUNT_UNITS_1000000000 = "amount-units-1000000000";
    public static final String YEAR_FROM = "from";
    public static final String YEAR_TO = "to";
    public static final String YEAR_ALL = "all";
    
    public static final String SORT_COLUMN = "sort-column";
    public static final String MAXIMUM_FILE_SIZE = "maximum-file-size";
    public static final String LIMIT_FILE_TO_UPLOAD = "limit-file-to-upload";
    public static final String SORT_COLUMN_VIEW="v_g_settings_resource_columns";

    public static final String PERFORMANCE_ISSUES = "performanceIssues";
    
    public static final String NO_LIMIT = "noLimit";
    public static final String CUSTOM = "custom";

    public static final String REORDER_FUNDING_ITEM_ID = "reorder-funding-item";
    public static final String EFFECTIVE_CURRENCY = "effective-currency";
    
    public static final String DASHBOARD_DEFAULT_MAX_YEAR_RANGE = "dashboard-default-max-year-range";
    public static final String DASHBOARD_DEFAULT_MIN_YEAR_RANGE = "dashboard-default-min-year-range";
    public static final String DASHBOARD_DEFAULT_MAX_DATE = "dashboard-default-max-date";
    public static final String DASHBOARD_DEFAULT_MIN_DATE = "dashboard-default-min-date";
    
    public static final String GIS_DEFAULT_MAX_YEAR_RANGE = "gis-default-max-year-range";
    public static final String GIS_DEFAULT_MIN_YEAR_RANGE = "gis-default-min-year-range";
    public static final String GIS_DEFAULT_MAX_DATE = "gis-default-max-date";
    public static final String GIS_DEFAULT_MIN_DATE = "gis-default-min-date";




    /** {id, value} map for settings fields ID-NAME map */
    public static final Map<String, String> ID_NAME_MAP = Collections.unmodifiableMap(new HashMap<String, String>() {{
        put(FUNDING_TYPE_ID, "Funding Type");
        put(CURRENCY_ID, "Currency");
        put(CALENDAR_TYPE_ID, "Calendar Type");
        put(AMOUNT_FORMAT_ID, "Amount Format");
        put(CALENDAR_CURRENCIES_ID, "Calendar Currencies");
        put(YEAR_RANGE_ID, "Year Range *");
        put(CUSTOM, "Custom");
        put(NO_LIMIT, "No Limit");
        put(DECIMAL_SYMBOL, "Decimal Separator");
        put(MAX_FRACT_DIGITS, "Maximum Fraction Digits");
        put(USE_GROUPING, "Use Grouping Separator");
        put(GROUP_SEPARATOR, "Grouping Separator");
        put(GROUP_SIZE, "Group Size");
        put(AMOUNT_UNITS, "Amounts units");
        put(AMOUNT_UNITS_1, "Amounts in Units");
        put(AMOUNT_UNITS_1000, "Amounts in Thousands (000)");
        put(AMOUNT_UNITS_1000000, "Amounts in Millions (000 000)");
        put(AMOUNT_UNITS_1000000000, "Amounts in Billions (000 000 000)");
        put(YEAR_FROM, "From:");
        put(YEAR_TO, "To:");
        put(YEAR_ALL, "All");
        put(SORT_COLUMN, GlobalSettingsConstants.DEFAULT_RESOURCES_SORT_COLUMN);
        put(MAXIMUM_FILE_SIZE, GlobalSettingsConstants.CR_MAX_FILE_SIZE );
        put(LIMIT_FILE_TO_UPLOAD, GlobalSettingsConstants.LIMIT_FILE_TYPE_FOR_UPLOAD);
    }});

    public static final String DOT = "dot";
    public static final String COMMA = "comma";
    public static final String SPACE = "space";

    /** {id, value} map for default decimal separators */
    public static final Map<String, String> DECIMAL_SEPARATOR_MAP = Collections.unmodifiableMap(new LinkedHashMap<String, String>() {{
        put(DOT, ".");
        put(COMMA, ",");
        put(CUSTOM, "");
    }});

    /** {id, value} map for default maximum fractional digits */
    public static final Map<String, String> MAX_FRACT_DIGITS_MAP = Collections.unmodifiableMap(new LinkedHashMap<String, String>() {{
        put(NO_LIMIT, String.valueOf(Integer.MAX_VALUE));
        for (int id = 0; id < 6; id++) {
            final String idStr = String.valueOf(id);
            put(MAX_FRACT_DIGITS + idStr, idStr);
        }
        put(CUSTOM, "");
    }});

    /** {id, value} map for default group separators */
    public static final Map<String, String> GROUP_SEPARATOR_MAP = Collections.unmodifiableMap(new LinkedHashMap<String, String>() {{
        put(SPACE, " ");
        put(DOT, ".");
        put(COMMA, ",");
        put(CUSTOM, "");
    }});

    /** {id, value} map for default amount units */
    public static final Map<String, String> AMOUNT_UNITS_MAP = Collections.unmodifiableMap(new LinkedHashMap<String, String>() {{
        put(AMOUNT_UNITS_1, "1");
        put(AMOUNT_UNITS_1000, "1000");
        put(AMOUNT_UNITS_1000000, "1000000");
        put(AMOUNT_UNITS_1000000000, "1000000000");
    }});

    /** {id, value} map for default amount units used in reports */
    public static final Map<String, String> AMOUNT_UNITS_MAP_REPORTS = Collections.unmodifiableMap(new
                                                                                                           LinkedHashMap<String, String>() {{
        put(AMOUNT_UNITS_1, "1");
        put(AMOUNT_UNITS_1000, "1000");
        put(AMOUNT_UNITS_1000000, "1000000");
    }});
    
    public static final Map<String, String> YEAR_MAP = Collections.unmodifiableMap(new LinkedHashMap<String, String>() {{
        put(YEAR_ALL, "-1");
    }});
    
}
