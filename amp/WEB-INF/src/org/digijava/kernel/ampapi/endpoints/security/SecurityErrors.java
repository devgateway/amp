package org.digijava.kernel.ampapi.endpoints.security;

import static org.digijava.kernel.ampapi.endpoints.errors.ApiError.ERROR_CLASS_SECURITY_ID;

import org.digijava.kernel.ampapi.endpoints.errors.ApiErrorMessage;

/**
 * Defines security specific errors
 *
 * @author Nadejda Mandrescu
 */
public class SecurityErrors {

    /**
     * Action exists, but it is not allowed, e.g. to edit an activity that current user has no edit rights for
     */
    public static final ApiErrorMessage NOT_ALLOWED = new ApiErrorMessage(ERROR_CLASS_SECURITY_ID, 3, "Not allowed");

    /**
     * Invalid Request
     */
    public static final ApiErrorMessage INVALID_REQUEST = new ApiErrorMessage(ERROR_CLASS_SECURITY_ID, 5,
            "Invalid request");

    /**
     * User Banned
     */
    public static final ApiErrorMessage USER_BANNED = new ApiErrorMessage(ERROR_CLASS_SECURITY_ID, 6, "User Banned");

    /**
     * User is not part of any team
     */
    public static final ApiErrorMessage NO_TEAM = new ApiErrorMessage(ERROR_CLASS_SECURITY_ID, 7,
            "User is not part of any team");

    public static final ApiErrorMessage INVALID_TEAM = new ApiErrorMessage(ERROR_CLASS_SECURITY_ID, 8, "Invalid Team");

    public static final ApiErrorMessage INVALID_USER_PASSWORD = new ApiErrorMessage(ERROR_CLASS_SECURITY_ID, 9,
            "Username or password are invalid");

    public static final ApiErrorMessage INVALID_WORKSPACE = new ApiErrorMessage(ERROR_CLASS_SECURITY_ID, 10,
            "Workspace is invalid");

    public static final ApiErrorMessage PASSWORD_CHANGED = new ApiErrorMessage(ERROR_CLASS_SECURITY_ID, 11,
            "User password changed");

    public static final ApiErrorMessage NOT_AUTHENTICATED = new ApiErrorMessage(ERROR_CLASS_SECURITY_ID, 12,
            "Not authenticated");

    public static final ApiErrorMessage USER_SUSPENDED = new ApiErrorMessage(ERROR_CLASS_SECURITY_ID, 13,
            "User Suspsended");

    public static final ApiErrorMessage FILL_FORM_CORRECTLY = new ApiErrorMessage(ERROR_CLASS_SECURITY_ID, 14,
            "Please fill the form correctly");

    public static final ApiErrorMessage EMAIL_NOT_EQUAL = new ApiErrorMessage(ERROR_CLASS_SECURITY_ID, 15,
            "Email should be equal to repeat email.");
    public static final ApiErrorMessage NOTIFICATION_EMAIL_NOT_EQUAL = new ApiErrorMessage(ERROR_CLASS_SECURITY_ID, 16,
            "Notification email should be equal to repeat notification email.");

    public static final ApiErrorMessage NOTIFICATION_EMAIL_NOT_NULL = new ApiErrorMessage(ERROR_CLASS_SECURITY_ID, 17,
            "Notification email and repeat notification email should not be empty.");

    public static final ApiErrorMessage NOT_VALID_EMAIL = new ApiErrorMessage(ERROR_CLASS_SECURITY_ID, 18,
            "Please use valid email address.");

    public static final ApiErrorMessage USER_EMAIL_EXISTS = new ApiErrorMessage(ERROR_CLASS_SECURITY_ID, 19,
            "User with the same email exists, please use another email.");

    public static final ApiErrorMessage PASSWORD_VALIDATION = new ApiErrorMessage(ERROR_CLASS_SECURITY_ID, 20,
            "Please enter a password which meets the minimum password requirements.");

    public static final ApiErrorMessage USER_ID_INVALID = new ApiErrorMessage(ERROR_CLASS_SECURITY_ID, 21,
            "Please provide the correct user id.");

    public static final ApiErrorMessage PASSWORD_FIELD_REQUIRED = new ApiErrorMessage(ERROR_CLASS_SECURITY_ID, 22,
            "Password fields are required.");
}
