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
	public static final ApiErrorMessage DISCRIMINATOR_METHOD_ERROR = new ApiErrorMessage(12, "Error when accessing discriminator method");
	public static final ApiErrorMessage DISCRIMINATOR_CLASS_METHOD_ERROR = new ApiErrorMessage(13, "Error when accessing a method from the discriminator class");
	public static final ApiErrorMessage SAVE_AS_DRAFT_FM_DISABLED = new ApiErrorMessage(14, "Activity can only be saved as draft, but \"save as draft\" is disabled in FM!");	
	public static final ApiErrorMessage TITLE_IN_DEFAULT_LANUGAGE_REQUIRED = new ApiErrorMessage(15, "Title in default language is required");
	public static final ApiErrorMessage UPDATE_ID_MISMATCH = new ApiErrorMessage(16, "Request project ids mismatch");
	public static final ApiErrorMessage FIELD_INVALID_LENGTH = new ApiErrorMessage(17, "Invalid field length");
	public static final ApiErrorMessage UPDATE_ID_IS_OLD = new ApiErrorMessage(18, "Update request for older activity id. Please provide the latest");
	public static final ApiErrorMessage FIELD_PERCENTAGE_SUM_BAD = new ApiErrorMessage(19, "Sum of percentage fields has to be 100");
	public static final ApiErrorMessage FIELD_PARENT_CHILDREN_NOT_ALLOWED = new ApiErrorMessage(20, "Parent and child cannot be in the same collection");
	public static final ApiErrorMessage DEPENDENCY_NOT_MET = new ApiErrorMessage(21, "Dependency not met");
	public static final ApiErrorMessage UNIQUE_PRIMARY_CONTACT = new ApiErrorMessage(22, "Multiple primary contacts not allowed");
	public static final ApiErrorMessage ACTIVITY_IS_LOCKED = new ApiErrorMessage(23, "Cannot aquire lock for the activity");
	public static final ApiErrorMessage ACTIVITY_NOT_LOADED = new ApiErrorMessage(24, "Cannot load the activity");
	public static final ApiErrorMessage FIELD_INVALID_PERCENTAGE = new ApiErrorMessage(25, "Percentage fields have to be >0, <=100");
	public static final ApiErrorMessage AGREEMENT_CODE_REQUIRED = new ApiErrorMessage(26, "Agreement code is required");
	public static final ApiErrorMessage AGREEMENT_CODE_UNIQUE = new ApiErrorMessage(27, "Agreement code should be unique");
	public static final ApiErrorMessage AGREEMENT_TITLE_REQUIRED = new ApiErrorMessage(26, "Agreement title is required");
	public static final ApiErrorMessage ORGANIZATION_ROLE_PAIR_NOT_DECLARED = new ApiErrorMessage(27, "The organization and role pair is not declared");

}