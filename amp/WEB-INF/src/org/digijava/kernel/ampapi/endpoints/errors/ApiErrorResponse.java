/**
 * 
 */
package org.digijava.kernel.ampapi.endpoints.errors;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.ResponseBuilder;

import org.apache.log4j.Logger;
import org.digijava.kernel.ampapi.endpoints.util.JsonBean;
import org.digijava.kernel.translator.TranslatorWorker;

/**
 * Provides an API Error response 
 * 
 * @author Nadejda Mandrescu
 */
public class ApiErrorResponse {
	protected static final Logger logger = Logger.getLogger(ApiErrorResponse.class);
	
	public static final String UNKOWN_ERROR = "Unkown Error";
	public static final String INTERNAL_ERROR = "Internal Error";
	
	/**
	 * Reports HTTP 500 (Internal Server Error) with the given message
	 * @param msg the API Error
	 */
	public static void reportGenericError(ApiErrorMessage msg) {
		reportError(Response.Status.INTERNAL_SERVER_ERROR, msg);
	}

    /**
     * Builds response with HTTP 500 (Internal Server Error) with the given message
     * @param msg the API Error
     */
    public static Response buildGenericError(ApiErrorMessage msg) {
        JsonBean formattedMessage = ApiError.toError(msg);
        ResponseBuilder builder = Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(formattedMessage);
        return builder.build();
    }
	
	/**
	 * Reports that user authentication is required (HTTP 401)
	 * @param msg (optional) API error message
	 */
	public static void reportUnauthorisedAccess(ApiErrorMessage msg) {
		reportError(Response.Status.UNAUTHORIZED, msg);
	}
	
	/**
	 * Reports forbidden access with unknown reason
	 */
	public static void reportForbiddenAccess() {
		reportError(Response.Status.FORBIDDEN, ApiError.toError(TranslatorWorker.translateText(UNKOWN_ERROR)));
	}
	
	/**
	 * Reports that this action is forbidden (HTTP 403)
	 * @param msg API error message
	 */
	public static void reportForbiddenAccess(ApiErrorMessage msg) {
		reportError(Response.Status.FORBIDDEN, msg);
	}
	
	/**
	 * Reports that this action is forbidden (HTTP 403)
	 * @param msg API error message
	 */
	public static void reportForbiddenAccess(JsonBean msg) {
		reportError(Response.Status.FORBIDDEN, msg);
	}
	
	/**
	 * Reports any custom response status for the given message
	 * @param status HTTP response status 
	 * @param msg 	 API Error message
	 */
	public static void reportError(Response.Status status, ApiErrorMessage msg) {
		reportError (status, ApiError.toError(msg));
	}
	
	/**
	 * Reports any custom response status for the given json
	 * @param status HTTP response status
	 * @param error	 JSON with the error details
	 */
	public static void reportError(Response.Status status, JsonBean error) {
		logger.error(String.format("[HTTP %d] Error response = %s", status.getStatusCode(), error.toString()));
		ResponseBuilder builder = Response.status(status).entity(error);
		Response response = builder.build();
		throw new WebApplicationException(response);
	}
	
}
