/**
 * 
 */
package org.digijava.kernel.ampapi.endpoints.exception;

import org.digijava.kernel.ampapi.endpoints.errors.*;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;
import java.util.Collection;

/**
 * AMP wrapper WebApplicationException
 * 
 * @author Nadejda Mandrescu
 */
public class AmpWebApplicationException extends WebApplicationException {
    private static final long serialVersionUID = 93647173994827040L;
    
    public AmpWebApplicationException(Response.Status status, ApiErrorMessage error) {
        this(status, ApiError.toError(error));
    }
    
    public AmpWebApplicationException(Response.Status status, ApiEMGroup errorGroup) {
        this(status, ApiError.toError(errorGroup));
    }
    
    public AmpWebApplicationException(Response.Status status, Collection<ApiErrorMessage> errors) {
        this(status, ApiError.toError(errors));
    }
    
    public AmpWebApplicationException(Response.Status status, ApiErrorResponse error) {
        super(ApiErrorResponseService.buildResponse(status, error));
    }

}
