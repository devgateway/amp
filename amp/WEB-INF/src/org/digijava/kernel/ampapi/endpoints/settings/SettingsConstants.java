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
		put(AMOUNT_UNITS, "Amount units");
	}});
	
	public static final Map<String, String> YEAR_MAP = Collections.unmodifiableMap(new LinkedHashMap<String, String>() {{
		put(YEAR_ALL, "-1");
	}});
}
