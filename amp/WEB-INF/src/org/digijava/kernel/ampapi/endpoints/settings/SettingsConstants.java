/**
 * 
 */
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
	// TODO: legacy numeric ids defined in GIS API proposal - better to change them to more meaning-full ids
	public static final String FUNDING_TYPE_ID = "0";
	public static final String CURRENCY_ID = "1";
	public static final String CALENDAR_TYPE_ID = "2";
	// TODO: legacy numeric ids - END
	public static final String CALENDAR_CURRENCIES_ID = "calendarCurrencies";
	public static final String AMOUNT_FORMAT_ID = "amountFormat";
	public static final String YEAR_RANGE_ID = "yearRange";
	
	public static final String DEFAULT_FUNDING_TYPE_ID = MeasureConstants.ACTUAL_COMMITMENTS;
	
	public static final String DECIMAL_SYMBOL = "decimalSymbol";
	public static final String MAX_FRACT_DIGITS = "maxFracDigits";
	public static final String USE_GROUPING = "useGrouping";
	public static final String GROUP_SEPARATOR = "groupSeparator";
	public static final String GROUP_SIZE = "groupSize";
	public static final String AMOUNT_UNITS = "amountUnits";
	public static final String YEAR_FROM = "yearFrom";
	public static final String YEAR_TO = "yearTo";
	public static final String YEAR_ALL = "all";
	
	public static final String NO_LIMIT = "noLimit";
	public static final String CUSTOM = "custom";
	
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
		put(AMOUNT_UNITS, "Grouping Separator");
		put(YEAR_FROM, "From:");
		put(YEAR_TO, "To:");
		put(YEAR_ALL, "All");
	}});
	
	public static final Map<String, String> YEAR_MAP = Collections.unmodifiableMap(new LinkedHashMap<String, String>() {{
		put(YEAR_ALL, "-1");
	}});
}
