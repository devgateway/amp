package org.digijava.kernel.ampapi.endpoints.auditlogger;

import org.digijava.kernel.ampapi.endpoints.common.EndpointUtils;
import org.digijava.kernel.ampapi.endpoints.errors.ErrorReportingEndpoint;
import org.digijava.kernel.ampapi.endpoints.util.ApiMethod;
import org.digijava.kernel.ampapi.endpoints.util.AvailableMethod;
import org.digijava.kernel.ampapi.endpoints.util.JsonBean;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.util.List;

@Path("audit-logger")
public class AuditLogger implements ErrorReportingEndpoint {
    @Override
    public JsonBean getErrors() {
        return null;
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
    public List<AvailableMethod> getAvailableFilters() {
        return EndpointUtils.getAvailableMethods(AuditLogger.class.getName());
    }

    @Override
    public Class getErrorsClass() {
        return null;
    }

    @GET
    @Path("/changes")
    @Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
    @ApiMethod(ui = false, id = "auditLoggerChanges")
    public AuditLoggerChanges getChanges() {

        return AuditLoggerService.getIinstance().getAuditLogger();
    }
}
