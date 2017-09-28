package org.digijava.kernel.ampapi.endpoints.errors;

import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.digijava.kernel.ampapi.endpoints.util.JsonBean;

/**
 * Defines API Error Exception. Stores @Response.Status and JsonBean with error messages
 * 
 * @author Viorel Chihai
 */
public class ApiRuntimeException extends RuntimeException {

    private static final long serialVersionUID = 1L;
    
    private Status responseStatus;
    private JsonBean error;
    
    public ApiRuntimeException(Status status, JsonBean error) {
        this.responseStatus = status;
        this.error = error;
    }
    
    public ApiRuntimeException(JsonBean error) {
        this.responseStatus = Response.Status.BAD_REQUEST;
        this.error = error;
    }
    
    public Status getResponseStatus() {
        return responseStatus;
    }

    public JsonBean getError() {
        return error;
    }

    public Object getUnwrappedError() {
        return error.get(ApiError.JSON_ERROR_CODE);
    }
}
