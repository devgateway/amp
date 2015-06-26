/**
 * 
 */
package org.digijava.kernel.ampapi.endpoints.activity;

import org.digijava.kernel.ampapi.endpoints.errors.ApiErrorMessage;

/**
 * Defines errors used by Activity API. Please define concrete errors, normally is an invalid request input. <br>
 * Anything that is out of control, should be re-thrown and it will be handled by API Exception Filter.
 * 
 * @author Nadejda Mandrescu
 */
public class ActivityErrors {

	// Validation errors
	public static final ApiErrorMessage FIELD_REQUIRED = new ApiErrorMessage(1, "Required field");
	public static final ApiErrorMessage FIELD_INVALID_TYPE = new ApiErrorMessage(2, "Invalid field type");
	public static final ApiErrorMessage FIELD_INVALID_VALUE = new ApiErrorMessage(3, "Invalid field value");
	public static final ApiErrorMessage FIELD_READ_ONLY = new ApiErrorMessage(4, "Read-only field");
	public static final ApiErrorMessage FIELD_MULTIPLE_VALUES_NOT_ALLOWED = new ApiErrorMessage(5, "Multiple values not allowed");
	public static final ApiErrorMessage FIELD_UNQUE_VALUES = new ApiErrorMessage(6, "Unique values required");
	public static final ApiErrorMessage FIELD_INVALID = new ApiErrorMessage(7, "Invalid field");
	/* NO "other" errors are allowed, only concrete errors related to the current logic, so please define them explicitly
	 * for any uncontrolled error, exception must be logged   
	public static final ApiErrorMessage SOME_OTHER_ERROR = new ApiErrorMessage(8, "Other non-specific error");
	 */
	public static final ApiErrorMessage UNIQUE_ACTIVITY_TITLE = new ApiErrorMessage(9, "Activity title should be unique");
	
	
}