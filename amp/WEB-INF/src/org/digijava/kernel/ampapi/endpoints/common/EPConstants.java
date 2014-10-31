/**
 * 
 */
package org.digijava.kernel.ampapi.endpoints.common;

import org.dgfoundation.amp.ar.MeasureConstants;

/**
 * Endpoint related constants
 * @author Nadejda Mandrescu
 *
 */
public class EPConstants {
	public static final String LANGUAGE = "language";
	//config update 
	public static final String ADD_COLUMNS = "add_columns";
	public static final String ADD_HIERARCHIES = "add_hierarchies";
	public static final String ADD_MEASURES = "add_measures";
	public static final String SORTING = "sorting";
	
	public static final String NA = "N/A";
	
	//settings
	public static final String SETTINGS = "settings";
	
	public static final String SETTINGS_FUNDING_TYPE_ID = "0"; 
	public static final String SETTINGS_CURRENCY_ID = "1";
	public static final String SETTINGS_CALENDAR_TYPE_ID = "2";
	
	public static final String SETTINGS_FUNDING_TYPE_NAME = "Funding Type";
	public static final String SETTINGS_CURRENCY_NAME = "Currency";
	public static final String SETTINGS_CALENDAR_TYPE_NAME = "Calendar Type";
	
	public static final String SETTINGS_DEFAULT_FUNDING_TYPE_ID = MeasureConstants.ACTUAL_COMMITMENTS;
}
