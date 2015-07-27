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
	public static final ApiErrorMessage WRONG_PROGRAM_TYPE = new ApiErrorMessage(8, "Wrong config value for programs");
	public static final ApiErrorMessage UNIQUE_ACTIVITY_TITLE = new ApiErrorMessage(9, "Activity title should be unique");
	public static final ApiErrorMessage CANNOT_GET_PROPERTIES = new ApiErrorMessage(10, "Cannot get properties for type");
	public static final ApiErrorMessage DISCRIMINATOR_CLASS_NOT_FOUND = new ApiErrorMessage(11, "Cannot find discriminator class");
	public static final ApiErrorMessage DISCRIMINATOR_METHOD_ERROR = new ApiErrorMessage(11, "Error when accessing discriminator method");
	public static final ApiErrorMessage DISCRIMINATOR_CLASS_METHOD_ERROR = new ApiErrorMessage(12, "Error when accessing a method from the discriminator class");
	public static final ApiErrorMessage SAVE_AS_DRAFT_FM_DISABLED = new ApiErrorMessage(13, "Activity can only be saved as draft, but \"save as draft\" is disabled in FM!");	
	public static final ApiErrorMessage TITLE_IN_DEFAULT_LANUGAGE_REQUIRED = new ApiErrorMessage(14, "Title in default language is required");
	public static final ApiErrorMessage UPDATE_ID_MISMATCH = new ApiErrorMessage(15, "Request project ids mismatch");
	public static final ApiErrorMessage FIELD_INVALID_LENGTH = new ApiErrorMessage(16, "Invalid field length");
	public static final ApiErrorMessage UPDATE_ID_IS_OLD = new ApiErrorMessage(17, "Update request for older activty id. Please provide the latest");
	
}