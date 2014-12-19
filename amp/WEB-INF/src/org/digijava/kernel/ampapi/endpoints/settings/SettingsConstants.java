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
	public static final String AMOUNT_FORMAT_ID = "amountFormat";
	public static final String AMOUNT_PATTERN_ID = "ammountPattern";
	public static final String YEAR_RANGE_ID = "yearRange";
	
	public static final String DEFAULT_FUNDING_TYPE_ID = MeasureConstants.ACTUAL_COMMITMENTS;
	
	public static final String DECIMAL_SYMBOL = "decimalSymbol";
	public static final String MAX_FRACT_DIGITS = "maxFracDigits";
	public static final String USE_GROUPING = "useGrouping";
	public static final String GROUP_SEPARATOR = "groupSeparator";
	public static final String GROUP_SIZE = "groupSize";
	public static final String AMOUNT_UNITS = "amountUnits";
	public static final String AMOUNT_UNITS_1 = "amountUnits1";
	public static final String AMOUNT_UNITS_1000 = "amountUnits1000";
	public static final String AMOUNT_UNITS_1000000 = "amountUnits1000000";
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
		put(AMOUNT_PATTERN_ID, "Amount Pattern");
		put(YEAR_RANGE_ID, "Year Range *");
		put(CUSTOM, "Custom");
		put(NO_LIMIT, "No Limit");
		put(DECIMAL_SYMBOL, "Decimal Separator");
		put(MAX_FRACT_DIGITS, "Maximum Fraction Digits");
		put(USE_GROUPING, "Use Grouping Separator");
		put(GROUP_SEPARATOR, "Grouping Separator");
		put(GROUP_SIZE, "Group Size");
		put(AMOUNT_UNITS, "Grouping Separator");
		put(AMOUNT_UNITS_1, "Amounts in Units");
		put(AMOUNT_UNITS_1000, "Amounts in Thousands (000)");
		put(AMOUNT_UNITS_1000000, "Amounts in Millions (000 000)");
		put(YEAR_FROM, "From:");
		put(YEAR_TO, "To:");
		put(YEAR_ALL, "All");
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
		put(AMOUNT_UNITS_1000, "0.001");
		put(AMOUNT_UNITS_1000000, "0.000001");
	}});
	
	public static final Map<String, String> YEAR_MAP = Collections.unmodifiableMap(new LinkedHashMap<String, String>() {{
		put(YEAR_ALL, "-1");
	}});
}
