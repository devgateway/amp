/**
 * 
 */
package org.digijava.kernel.ampapi.endpoints.activity;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.digijava.kernel.ampapi.endpoints.errors.ApiErrorMessage;

/**
 * Defines errors used by Activity API
 * @author Nadejda Mandrescu
 */
public class ActivityErrors {
	public static final ApiErrorMessage REQUIRED = new ApiErrorMessage(1, "Required field", "Invalid field type: %s");
	public static final ApiErrorMessage INVALID_TYPE = new ApiErrorMessage(2, "Invalid field type", "Invalid field type: %s");
	/** Maps existing error messages to the field error code*/
	public static final Map<ApiErrorMessage, String> FIELD_ERROR_CODE_MAP = Collections.unmodifiableMap(
			new HashMap<ApiErrorMessage, String>(){{
		put(REQUIRED, "R");
		put(INVALID_TYPE, "T");
	}});
	
	public static final String GENERIC_FIELD_ERROR_CODE = "E";
}
