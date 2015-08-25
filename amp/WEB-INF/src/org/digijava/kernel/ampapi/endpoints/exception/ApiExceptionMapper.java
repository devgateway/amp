package org.digijava.kernel.ampapi.endpoints.exception;

import org.apache.log4j.Logger;
import org.digijava.kernel.ampapi.endpoints.errors.ApiError;
import org.digijava.kernel.ampapi.endpoints.errors.ApiErrorMessage;
import org.digijava.kernel.ampapi.endpoints.errors.ApiErrorResponse;

import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

/**
 * Builds the generic response with error code 500 for all unhandled exceptions
 */
@Provider
public class ApiExceptionMapper implements ExceptionMapper<Exception> {

    private static final Logger logger = Logger.getLogger(ApiExceptionMapper.class);
    private static final int MAX_EXCEPTION_NESTED = 2;

    @Override
    public Response toResponse(Exception e) {
        logger.error("ApiExceptionMapper: ", e);

        ApiErrorMessage apiErrorMessage = getApiErrorMessageFromException(e);

        return ApiErrorResponse.buildGenericError(apiErrorMessage);
    }
    
    public ApiErrorMessage getApiErrorMessageFromException(Throwable e) {
    	String message = extractMessageFromException(e);

        return new ApiErrorMessage(ApiError.GENERIC_UNHANDLED_ERROR_CODE, message);
    }


    private String extractMessageFromException(Throwable e) {
        StringBuilder accumulatedMessage = new StringBuilder(e.getMessage() == null ? "" : e.getMessage());
        String message = extractMessageFromException(e, 0, accumulatedMessage);
        message = (message == null || "".equals(message)) ? ApiErrorResponse.UNKOWN_ERROR : message;
        return message;
    }


    private String extractMessageFromException(Throwable e, int rootCauseNest, StringBuilder accumulatedMessage) {
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
