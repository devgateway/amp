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
	
	/** User token not found on the session */
	public static final ApiErrorMessage NO_SESSION_TOKEN = new ApiErrorMessage(1, "User Token not found or expired");
	/** No token provided within the HTTP request */
	public static final ApiErrorMessage NO_REQUEST_TOKEN = new ApiErrorMessage(2, "Request Token not provided");
	/** Invalid token is provided, do not match the one on session */
	public static final ApiErrorMessage INVALID_TOKEN = new ApiErrorMessage(3, "Invalid Token");
	/** Action exists, but it is not allowed, e.g. to edit an activity that current user has no edit rights for */
	public static final ApiErrorMessage NOT_ALLOWED = new ApiErrorMessage(4, "Not allowed");
	/** Invalid API Method authorization definition */
	public static final ApiErrorMessage INVALID_API_METHOD = new ApiErrorMessage(5, "Invalid API Method");
	
}
