package org.digijava.kernel.ampapi.endpoints.activity;

import static org.digijava.kernel.ampapi.endpoints.errors.ApiError.ERROR_CLASS_ACTIVITY_ID;

import org.digijava.kernel.ampapi.endpoints.errors.ApiErrorMessage;

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
    
}
