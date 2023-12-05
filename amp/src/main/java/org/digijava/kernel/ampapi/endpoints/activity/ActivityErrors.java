package org.digijava.kernel.ampapi.endpoints.activity;

import org.digijava.kernel.ampapi.endpoints.errors.ApiErrorMessage;

import static org.digijava.kernel.ampapi.endpoints.errors.ApiError.ERROR_CLASS_ACTIVITY_ID;

/**
 * Defines errors used by Activity API. Please define concrete errors, normally is an invalid request input. <br>
 * Anything that is out of control, should be re-thrown and it will be handled by API Exception Filter.
 *
 * @author Nadejda Mandrescu
 */
public class ActivityErrors {
    
    public static final ApiErrorMessage UNIQUE_ACTIVITY_TITLE = new ApiErrorMessage(ERROR_CLASS_ACTIVITY_ID, 0,
            "Activity title should be unique");
    
    public static final ApiErrorMessage SAVE_AS_DRAFT_FM_DISABLED = new ApiErrorMessage(ERROR_CLASS_ACTIVITY_ID, 1,
            "Activity cannot be saved as draft, \"save as draft\" is disabled in FM");
    
    public static final ApiErrorMessage UPDATE_ID_MISMATCH = new ApiErrorMessage(ERROR_CLASS_ACTIVITY_ID, 2,
            "Request project ids mismatch");
    
    public static final ApiErrorMessage UPDATE_ID_IS_OLD = new ApiErrorMessage(ERROR_CLASS_ACTIVITY_ID, 3,
            "Update request for older activity id. Please provide the latest");
    
    public static final ApiErrorMessage ACTIVITY_IS_BEING_EDITED =
            new ApiErrorMessage(ERROR_CLASS_ACTIVITY_ID, 4, "Current activity is being edited by:");
    
    public static final ApiErrorMessage ACTIVITY_NOT_LOADED = new ApiErrorMessage(ERROR_CLASS_ACTIVITY_ID, 5,
            "Cannot load the activity");
    
    public static final ApiErrorMessage ACTIVITY_IS_STALE = new ApiErrorMessage(ERROR_CLASS_ACTIVITY_ID, 6,
            "The activity is stale");
    
    public static final ApiErrorMessage FUNDING_PLEDGE_ORG_GROUP_MISMATCH =
            new ApiErrorMessage(ERROR_CLASS_ACTIVITY_ID, 7,
            "The organization group of the pledge doesn't match with funding donor organization group");
    
    public static final ApiErrorMessage ACTIVITY_NOT_FOUND = new ApiErrorMessage(ERROR_CLASS_ACTIVITY_ID, 8,
            "Activity not found");
    
    public static final ApiErrorMessage LOCALE_INVALID =
            new ApiErrorMessage(ERROR_CLASS_ACTIVITY_ID, 9, "Invalid translation language specified");
    
    public static final ApiErrorMessage FIELD_ACTIVITY_ID_NULL =
            new ApiErrorMessage(ERROR_CLASS_ACTIVITY_ID, 10, "Activity id cannot be null");
    
    public static final String ADD_ACTIVITY_NOT_ALLOWED = "Adding activity is not allowed";
    public static final String EDIT_ACTIVITY_NOT_ALLOWED = "No right to edit this activity";
    public static final String ACTIVITY_NOT_LAST_VERSION = "Activity is not the latest version.";
    public static final String INVALID_MODIFY_BY_FIELD = "Invalid team member in modified_by field.";
    
    public static final ApiErrorMessage BULK_TO_BIG =
            new ApiErrorMessage(ERROR_CLASS_ACTIVITY_ID, 11, "Too many activities found in the request");

    public static final ApiErrorMessage PROCESS_ALREADY_RUNNING =
            new ApiErrorMessage(ERROR_CLASS_ACTIVITY_ID, 12, "PROCESS_ALREADY_RUNNING");
    public static final ApiErrorMessage ONLY_SYNC =
            new ApiErrorMessage(ERROR_CLASS_ACTIVITY_ID, 13, "Only sync is allowed.");

}
