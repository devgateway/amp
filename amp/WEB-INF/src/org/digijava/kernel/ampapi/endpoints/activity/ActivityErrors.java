/**
 * 
 */
package org.digijava.kernel.ampapi.endpoints.activity;

import org.digijava.kernel.ampapi.endpoints.errors.ApiErrorMessage;

/**
 * Defines errors used by Activity API
 * @author Nadejda Mandrescu
 */
public class ActivityErrors {
	public static final ApiErrorMessage FIELD_REQUIRED = new ApiErrorMessage(1, "Required field");
	public static final ApiErrorMessage FIELD_INVALID_TYPE = new ApiErrorMessage(2, "Invalid field type");
	public static final ApiErrorMessage FIELD_INVALID_VALUE = new ApiErrorMessage(3, "Invalid field value");
	public static final ApiErrorMessage FIELD_READ_ONLY = new ApiErrorMessage(4, "Read-only field");
	public static final ApiErrorMessage FIELD_MULTIPLE_VALUES_NOT_ALLOWED = new ApiErrorMessage(5, "Multiple values not allowed");
	public static final ApiErrorMessage FIELD_UNQUE_VALUES = new ApiErrorMessage(6, "Unique values required");
	public static final ApiErrorMessage FIELD_INVALID = new ApiErrorMessage(7, "Invalid field");
		
	public static final String GENERIC_FIELD_ERROR_CODE = "E";
}
