package org.digijava.kernel.ampapi.endpoints.resource;

import java.util.List;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.digijava.kernel.ampapi.endpoints.activity.APIField;
import org.digijava.kernel.ampapi.endpoints.activity.AmpFieldsEnumerator;
import org.digijava.kernel.ampapi.endpoints.errors.ErrorReportingEndpoint;
import org.digijava.kernel.ampapi.endpoints.security.AuthRule;
import org.digijava.kernel.ampapi.endpoints.util.ApiMethod;

/**
 * @author Viorel Chihai
 */
@Path("resource")
public class ResourceEndpoint implements ErrorReportingEndpoint {

    /**
     * Provides full set of available fields and their settings/rules in a hierarchical structure.
     * @return JSON with fields information
     * @see <a href="https://wiki.dgfoundation.org/display/AMPDOC/Fields+enumeration">Fields Enumeration Wiki<a/>
     */
    @GET
    @Path("fields")
    @Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
    @ApiMethod(authTypes = AuthRule.AUTHENTICATED, id = "getFields", ui = false)
    public List<APIField> getAvailableFields() {
        return AmpFieldsEnumerator.PUBLIC_ENUMERATOR.getResourceFields();
    }

    @Override
    public Class<?> getErrorsClass() {
        return ResourceErrors.class;
    }
    
}
