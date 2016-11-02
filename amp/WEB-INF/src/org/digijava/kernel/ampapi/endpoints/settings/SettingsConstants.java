package org.digijava.kernel.ampapi.endpoints.settings;

import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import org.dgfoundation.amp.ar.MeasureConstants;

/**
 * Settings related constants
 * 
 * @author Nadejda Mandrescu
 */
public class SettingsConstants {
	public static final String FUNDING_TYPE_ID = "funding-type";
	public static final String CURRENCY_ID = "currency-code";
	public static final String CALENDAR_TYPE_ID = "calendar-id";
	public static final String CALENDAR_CURRENCIES_ID = "calendar-currencies";
	public static final String YEAR_RANGE_ID = "year-range";
	
	public static final String DEFAULT_FUNDING_TYPE_ID = MeasureConstants.ACTUAL_COMMITMENTS;
	
	public static final String YEAR_FROM = "from";
	public static final String YEAR_TO = "to";
	public static final String YEAR_ALL = "all";
	
	public static final String NO_LIMIT = "noLimit";
	public static final String CUSTOM = "custom";

	public static final String AMOUNT_UNITS = "number-divider";
	public static final String AMOUNT_UNITS_1 = "number-divider-1";
	public static final String AMOUNT_UNITS_1000 = "number-divider-1000";
	public static final String AMOUNT_UNITS_1000000 = "number-divider-1000000";
	public static final String AMOUNT_UNITS_1000000000 = "number-divider-1000000000";
	
	/** {id, value} map for settings fields ID-NAME map */
	public static final Map<String, String> ID_NAME_MAP = Collections.unmodifiableMap(new HashMap<String, String>() {{
		put(FUNDING_TYPE_ID, "Funding Type");
		put(CURRENCY_ID, "Currency");
		put(CALENDAR_TYPE_ID, "Calendar Type");
		put(CALENDAR_CURRENCIES_ID, "Calendar Currencies");
		put(YEAR_RANGE_ID, "Year Range *");
		put(CUSTOM, "Custom");
		put(NO_LIMIT, "No Limit");
		put(YEAR_FROM, "From:");
		put(YEAR_TO, "To:");
		put(YEAR_ALL, "All");
		put(AMOUNT_UNITS, "Grouping Separator");
		put(AMOUNT_UNITS_1, "Amounts in Units");
		put(AMOUNT_UNITS_1000, "Amounts in Thousands (000)");
		put(AMOUNT_UNITS_1000000, "Amounts in Millions (000 000)");
		put(AMOUNT_UNITS_1000000000, "Amounts in Billions (000 000 000)");
	}});
	
	public static final Map<String, String> YEAR_MAP = Collections.unmodifiableMap(new LinkedHashMap<String, String>() {{
		put(YEAR_ALL, "-1");
	}});

	/** {id, value} map for default amount units */
	public static final Map<String, String> AMOUNT_UNITS_MAP = Collections.unmodifiableMap(new LinkedHashMap<String, String>() {{
		put(AMOUNT_UNITS_1, "1");
		put(AMOUNT_UNITS_1000, "1000");
		put(AMOUNT_UNITS_1000000, "1000000");
		put(AMOUNT_UNITS_1000000000, "1000000000");
	}});
}
