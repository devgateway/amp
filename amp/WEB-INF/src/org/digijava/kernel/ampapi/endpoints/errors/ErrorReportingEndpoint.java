package org.digijava.kernel.ampapi.endpoints.errors;

import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.digijava.kernel.ampapi.endpoints.common.EndpointUtils;
import org.digijava.kernel.ampapi.endpoints.security.AuthRule;
import org.digijava.kernel.ampapi.endpoints.util.ApiMethod;
import org.digijava.kernel.ampapi.endpoints.util.JsonBean;

/**
 * Allows any endpoint to report all its possible errors. Implementations must provide a class that holds all errors
 * specific to this endpoint/component.
 *
 * @author Octavian Ciubotaru
 */
public interface ErrorReportingEndpoint {

    ApiErrorCollector errorCollector = new ApiErrorCollector();

    @GET
    @Path("errors")
    @Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
    @ApiMethod(id = "errors", ui = false)
    default JsonBean getErrors() {
        JsonBean jsonBean = ApiError.toError(errorCollector.collect(getErrorsClass()));
        EndpointUtils.setResponseStatusMarker(HttpServletResponse.SC_OK);
        return jsonBean;
    }

    /**
     * For endpoints to override and specify the class which holds all errors.
     * For more details please check: {@link ApiErrorCollector#collect(Class)}.
     *
     * @return the class which holds the errors (for no errors use Void.class)
     */
    Class getErrorsClass();
}
