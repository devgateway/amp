/**
 *
 */
package org.digijava.kernel.ampapi.endpoints.security;

import org.digijava.kernel.ampapi.endpoints.errors.ApiErrorMessage;

/**
 * Defines security specific errors
 *
 * @author Nadejda Mandrescu
 */
public class SecurityErrors {
    
    private static final Integer ID = 5;
    
    /** User token not found on the session */
    public static final ApiErrorMessage NO_SESSION_TOKEN = new ApiErrorMessage(5, 0, "User Token not found or expired");
    /** No token provided within the HTTP request */
    public static final ApiErrorMessage NO_REQUEST_TOKEN = new ApiErrorMessage(5, 1, "Request Token not provided");
    /** Invalid token is provided, do not match the one on session */
    public static final ApiErrorMessage INVALID_TOKEN = new ApiErrorMessage(5, 2, "Invalid Token");
    /** Action exists, but it is not allowed, e.g. to edit an activity that current user has no edit rights for */
    public static final ApiErrorMessage NOT_ALLOWED = new ApiErrorMessage(5, 3, "Not allowed");
    /** Token expired */
    public static final ApiErrorMessage TOKEN_EXPIRED = new ApiErrorMessage(5, 4, "Token expired");
    /** Invalid Request */
    public static final ApiErrorMessage INVALID_REQUEST = new ApiErrorMessage(5, 5, "Invalid request");
    /** User Banned */
    public static final ApiErrorMessage USER_BANNED = new ApiErrorMessage(5, 6, "User Banned");
    /** User is not part of any team */
    public static final ApiErrorMessage NO_TEAM = new ApiErrorMessage(5, 7, "User is not part of any team");
    
    public static final ApiErrorMessage INVALID_TEAM = new ApiErrorMessage(5, 8, "Invalid Team");
    
    public static final ApiErrorMessage INVALID_USER_PASSWORD = new ApiErrorMessage(5, 9,
            "Username or password are invalid");
    
    public static final ApiErrorMessage INVALID_WORKSPACE = new ApiErrorMessage(5, 10, "Workspace is invalid");
    
    public static final ApiErrorMessage PASSWORD_CHANGED = new ApiErrorMessage(5, 11, "User password changed");
    
    public static final ApiErrorMessage NOT_AUTHENTICATED = new ApiErrorMessage(5, 12, "Not authenticated");
    
}
