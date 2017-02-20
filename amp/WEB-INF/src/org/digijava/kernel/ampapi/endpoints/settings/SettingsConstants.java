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
	public static final String MAXIMUN_FILE_SIZE = "maximun-file-size";
	public static final String LIMIT_FILE_TO_UPLOAD = "limit-file-to-upload";
	
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
		put(AMOUNT_UNITS, "Amount units");
		put(AMOUNT_UNITS_1, "Amounts in Units");
		put(AMOUNT_UNITS_1000, "Amounts in Thousands (000)");
		put(AMOUNT_UNITS_1000000, "Amounts in Millions (000 000)");
		put(AMOUNT_UNITS_1000000000, "Amounts in Billions (000 000 000)");
		put(YEAR_FROM, "From:");
		put(YEAR_TO, "To:");
		put(YEAR_ALL, "All");
		put(SORT_COLUMN,"Resource List Sort Column");
		put(MAXIMUN_FILE_SIZE,"Maximum File Size");
		put(LIMIT_FILE_TO_UPLOAD,"Limit file type for upload");
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
	/** {id,value} map for Resource List Sort Column**/
	public static final Map<String, String> SORT_COLUMN_MAP = Collections.unmodifiableMap(new LinkedHashMap<String, String>() {{
		put("resource_title_ASC","Title ASCENDING");
		put("resource_title_DESC","Title DESCENDING");
		put("type_ASC","Type ASCENDING");
		put("type_DESC","Type DESCENDING");
		put("file_name_ASC","File name ASCENDING");
		put("file_name_DESC","File name DESCENDING");
		put("date_ASC","Date ASCENDING");
		put("date_DESC","Date DESCENDING");
		put("yearOfPublication_ASC","Publ. Year ASCENDING");
		put("yearOfPublication_DESC","Publ. Year DESCENDING");
		put("size_ASC","Size ASCENDING");
		put("size_DESC","Size DESCENDING");
		put("cm_doc_type_ASC","Document type ASCENDING");
		put("cm_doc_type_DESC","Document type DESCENDING");
	}});
	public static final Map<String, String> YEAR_MAP = Collections.unmodifiableMap(new LinkedHashMap<String, String>() {{
		put(YEAR_ALL, "-1");
	}});
}
