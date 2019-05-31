package org.digijava.kernel.ampapi.endpoints.errors;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import io.swagger.annotations.ApiOperation;
import org.digijava.kernel.ampapi.endpoints.AmpEndpoint;
import org.digijava.kernel.ampapi.endpoints.util.ApiMethod;

/**
 * Allows any endpoint to report all its possible errors. Implementations must provide a class that holds all errors
 * specific to this endpoint/component.
 *
 * @author Octavian Ciubotaru
 */
public interface ErrorReportingEndpoint extends AmpEndpoint {

    ApiErrorCollector errorCollector = new ApiErrorCollector();

    @GET
    @Path("errors")
    @Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
    @ApiMethod(id = "errors", ui = false)
    @ApiOperation("Errors reported by this endpoint")
    default ApiErrorResponse getErrors() {
        return ApiError.format(errorCollector.collect(getErrorsClass()));
    }

    /**
     * For endpoints to override and specify the class which holds all errors.
     * For more details please check: {@link ApiErrorCollector#collect(Class)}.
     *
     * @return the class which holds the errors (for no errors use Void.class)
     */
    Class getErrorsClass();
}
