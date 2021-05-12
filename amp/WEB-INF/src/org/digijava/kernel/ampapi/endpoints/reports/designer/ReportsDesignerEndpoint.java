package org.digijava.kernel.ampapi.endpoints.reports.designer;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.digijava.kernel.ampapi.endpoints.errors.ApiError;
import org.digijava.kernel.ampapi.endpoints.errors.ApiRuntimeException;
import org.digijava.kernel.ampapi.endpoints.security.AuthRule;
import org.digijava.kernel.ampapi.endpoints.util.ApiMethod;

import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

/**
 * @author Viorel Chihai
 */
@Path("reports")
@Api("reports")
public class ReportsDesignerEndpoint {

    private final ReportDesignerService reportDesignerService = new ReportDesignerService();

    @ApiOperation("Retrieve the information needed for the report designer")
    @ApiResponses(@ApiResponse(code = HttpServletResponse.SC_OK, message = "report designer info",
                    response = ReportDesigner.class))
    @ApiMethod(id = "designer", authTypes = AuthRule.IN_WORKSPACE)
    @GET
    @Path("/designer")
    @Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
    public final ReportDesigner getReport(@QueryParam("profile") @DefaultValue("R") String profile,
                                          @QueryParam("type") @DefaultValue("D") String type) {
        return reportDesignerService.getReportDesigner(getReportProfile(profile), getReportType(type));
    }

    @ApiOperation("Retrieve the report by specifying the id")
    @ApiResponses(@ApiResponse(code = HttpServletResponse.SC_OK, message = "report",
            response = ReportDesigner.class))
    @ApiMethod(id = "getReport", authTypes = AuthRule.IN_WORKSPACE)
    @GET
    @Path("/{reportId}")
    @Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
    public final Report getReport(@PathParam("reportId") Long reportId) {
        return reportDesignerService.getReport(reportId);
    }

    private ReportProfile getReportProfile(final String profile) {
        try {
            return ReportProfile.fromString(profile);
        } catch (IllegalArgumentException e) {
            throw new ApiRuntimeException(
                    ApiError.toError(ReportDesignerErrors.REPORT_PROFILE_INVALID.withDetails(profile)));
        }
    }

    private ReportType getReportType(final String type) {
        try {
            return ReportType.fromString(type);
        } catch (IllegalArgumentException e) {
            throw new ApiRuntimeException(
                    ApiError.toError(ReportDesignerErrors.REPORT_TYPE_INVALID.withDetails(type)));
        }
    }

}
