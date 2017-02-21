package org.digijava.kernel.ampapi.endpoints.exception;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.digijava.kernel.ampapi.endpoints.errors.ApiError;
import org.digijava.kernel.ampapi.endpoints.errors.ApiRuntimeException;
import org.digijava.kernel.ampapi.endpoints.errors.ApiErrorMessage;
import org.digijava.kernel.ampapi.endpoints.errors.ApiErrorResponse;

/**
 * Builds the generic response with error code 500 for all unhandled exceptions
 */
@Provider
public class ApiExceptionMapper implements ExceptionMapper<Exception> {

    private static final Logger logger = Logger.getLogger(ApiExceptionMapper.class);
    private static final int MAX_EXCEPTION_NESTED = 2;
    public static final ApiErrorMessage INTERNAL_ERROR = new ApiErrorMessage(ApiError.GENERIC_UNHANDLED_ERROR_CODE, 
    		ApiErrorResponse.INTERNAL_ERROR);
    
    @Context
	private HttpServletRequest httpRequest;
    
    @Override
    public Response toResponse(Exception e) {
        logger.error("ApiExceptionMapper: ", e);
        
        if (e instanceof ApiRuntimeException) {
    		ApiRuntimeException apiException = (ApiRuntimeException) e;
    		return ApiErrorResponse.buildGenericError(apiException.getResponseStatus(), apiException.getError(), httpRequest.getContentType());
    	}

        ApiErrorMessage apiErrorMessage = getApiErrorMessageFromException(e);
       
        return ApiErrorResponse.buildGenericError(Response.Status.INTERNAL_SERVER_ERROR, apiErrorMessage,  httpRequest.getContentType());
    }
    
    /**
     * Builds up custom internal error for current exception
     * @param e error detected
     * @return custom API error message
     */
    public ApiErrorMessage getApiErrorMessageFromException(Throwable e) {
    	String message = extractMessageFromException(e);
    	return INTERNAL_ERROR.withDetails(message);
    }

    private String extractMessageFromException(Throwable e) {
        StringBuilder accumulatedMessage = new StringBuilder(e.getMessage() == null ? "" : e.getMessage());
        String message = extractMessageFromException(e, 0, accumulatedMessage);
        message = StringUtils.isBlank(message) ? ApiErrorResponse.UNKOWN_ERROR : message;
        
        return message;
    }
    
    private String extractMessageFromException(Throwable e, int rootCauseNest, StringBuilder accumulatedMessage) {
    	// collect deeper cause
        if (e.getCause() != null
                && e.getCause().getMessage() != null
                && rootCauseNest < MAX_EXCEPTION_NESTED) {
            accumulatedMessage.append("; ").append(e.getCause().getMessage());
            return extractMessageFromException(e.getCause(), ++ rootCauseNest, accumulatedMessage);
        } else {
            return accumulatedMessage.toString();
        }
    }
}
