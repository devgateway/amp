package org.digijava.kernel.ampapi.endpoints.errors;

import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

/**
 * Defines API Error Exception. Stores @Response.Status and JsonBean with error messages
 *
 * @author Viorel Chihai
 */
public class ApiRuntimeException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    private Status responseStatus;
    private ApiErrorResponse error;

    public ApiRuntimeException(ApiErrorResponse error) {
        this(Response.Status.BAD_REQUEST, error);
    }

    public ApiRuntimeException(Status status, ApiErrorResponse error) {
        this(status, error, null);
    }

    public ApiRuntimeException(Status status, ApiErrorResponse error, Throwable cause) {
        super(error.toString(), cause);
        this.responseStatus = status;
        this.error = error;
    }

    public Status getResponseStatus() {
        return responseStatus;
    }

    public ApiErrorResponse getError() {
        return error;
    }

    public Object getUnwrappedError() {
        return error.getErrors();
    }
}
