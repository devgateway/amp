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
    public static final ApiErrorMessage SAVE_AS_DRAFT_FM_DISABLED = new ApiErrorMessage(14, "Activity cannot be saved as draft, \"save as draft\" is disabled in FM!");
    public static final ApiErrorMessage TITLE_IN_DEFAULT_LANUGAGE_REQUIRED = new ApiErrorMessage(15, "Title in default language is required");
    public static final ApiErrorMessage UPDATE_ID_MISMATCH = new ApiErrorMessage(16, "Request project ids mismatch");
    public static final ApiErrorMessage FIELD_INVALID_LENGTH = new ApiErrorMessage(17, "Invalid field length");
    public static final ApiErrorMessage UPDATE_ID_IS_OLD = new ApiErrorMessage(18, "Update request for older activity id. Please provide the latest");
    public static final ApiErrorMessage FIELD_PERCENTAGE_SUM_BAD = new ApiErrorMessage(19, "Sum of percentage fields has to be 100");
    public static final ApiErrorMessage FIELD_PARENT_CHILDREN_NOT_ALLOWED = new ApiErrorMessage(20, "Parent and child cannot be in the same collection");
    public static final ApiErrorMessage UNIQUE_PRIMARY_CONTACT = new ApiErrorMessage(22, "Multiple primary contacts not allowed");
    public static final ApiErrorMessage ACTIVITY_IS_BEING_EDITED =
            new ApiErrorMessage(23, "Current activity is being edited by:");
    public static final ApiErrorMessage ACTIVITY_NOT_LOADED = new ApiErrorMessage(24, "Cannot load the activity");
    public static final ApiErrorMessage FIELD_INVALID_PERCENTAGE = new ApiErrorMessage(25, "Percentage fields have to be >0, <=100");
    public static final ApiErrorMessage AGREEMENT_CODE_REQUIRED = new ApiErrorMessage(26, "Agreement code is required");
    public static final ApiErrorMessage AGREEMENT_CODE_UNIQUE = new ApiErrorMessage(27, "Agreement code should be unique");
    public static final ApiErrorMessage AGREEMENT_TITLE_REQUIRED = new ApiErrorMessage(26, "Agreement title is required");
    public static final ApiErrorMessage ORGANIZATION_ROLE_PAIR_NOT_DECLARED = new ApiErrorMessage(27, "The organization and role pair is not declared");
    public static final ApiErrorMessage ORGANIZATION_NOT_DECLARED = new ApiErrorMessage(28, "The organization is not declared");
    public static final ApiErrorMessage ACTIVITY_IS_STALE = new ApiErrorMessage(29, "The activity is stale");
    public static final ApiErrorMessage FUNDING_PLEDGE_ORG_GROUP_MISMATCH = new ApiErrorMessage(30, 
            "The organization group of the pledge doesn't match with funding donor organization group");
    public static final ApiErrorMessage FIELD_TOO_MANY_VALUES_NOT_ALLOWED = new ApiErrorMessage(31, "Too many values");
    public static final ApiErrorMessage ACTIVITY_NOT_FOUND = new ApiErrorMessage(32,
            "Activity not found");
    public static final ApiErrorMessage IMPLEMENTATION_LEVEL_NOT_SPECIFIED = new ApiErrorMessage(33,
            "Implementation level must be specified");
    public static final ApiErrorMessage DOESNT_MATCH_IMPLEMENTATION_LEVEL = new ApiErrorMessage(34,
            "Doesn't match with implementation level");
    public static final ApiErrorMessage LOCALE_INVALID =
            new ApiErrorMessage(35, "Invalid translation language specified");


}
