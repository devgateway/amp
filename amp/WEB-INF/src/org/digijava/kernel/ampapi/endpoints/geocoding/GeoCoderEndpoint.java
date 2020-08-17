package org.digijava.kernel.ampapi.endpoints.geocoding;

import java.util.Set;

import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.digijava.kernel.ampapi.endpoints.errors.ApiError;
import org.digijava.kernel.ampapi.endpoints.errors.ApiErrorResponse;
import org.digijava.kernel.ampapi.endpoints.errors.ApiRuntimeException;
import org.digijava.kernel.ampapi.endpoints.security.AuthRule;
import org.digijava.kernel.ampapi.endpoints.util.ApiMethod;
import org.digijava.kernel.entity.geocoding.GeoCodingProcess;
import org.digijava.kernel.geocoding.service.GeneralGeoCodingException;
import org.digijava.kernel.geocoding.service.GeoCodingNotAvailableException;
import org.digijava.kernel.geocoding.service.GeoCodingService;

/**
 * @author Octavian Ciubotaru
 */
@Path("geo-coder")
@Api("geo-coder")
public class GeoCoderEndpoint {

    private final GeoCodingService service = new GeoCodingService();

    @ApiOperation(value = "Add activities to current geo coding process",
            notes = "If there is no geo coding process yet, this operation will start a new one."
                    + "\n\nThis operation will fail if geo coding is in use by another team member.")
    @ApiResponses(@ApiResponse(code = HttpServletResponse.SC_NO_CONTENT, message = "success"))
    @ApiMethod(id = "process", authTypes = AuthRule.IN_WORKSPACE)
    @POST
    @Path("process")
    @Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
    public Response process(
            @ApiParam("Activity ids") Set<Long> activityIds) {
        try {
            service.processActivities(activityIds);
        } catch (GeoCodingNotAvailableException e) {
            ApiErrorResponse apiErrorResponse = ApiError.toError(
                    GeoCoderEndpointErrors.GEO_CODING_NOT_AVAILABLE.withDetails(e.getTeamMember().toString()));
            throw new ApiRuntimeException(Response.Status.BAD_REQUEST, apiErrorResponse);
        } catch (GeneralGeoCodingException e) {
            ApiErrorResponse apiErrorResponse = ApiError.toError(
                    GeoCoderEndpointErrors.GEO_CODING_GENERAL_ERROR.withDetails(e.getMessage()));
            throw new ApiRuntimeException(Response.Status.BAD_REQUEST, apiErrorResponse);
        }
        return Response.noContent().build();
    }

    @ApiOperation(value = "Returns current geo coding process",
            notes = "This operation will fail if geo coding is in use by another team member.")
    @ApiResponses({
            @ApiResponse(code = HttpServletResponse.SC_OK, message = "geo coding status", response = GeoCodingProcess.class),
            @ApiResponse(code = HttpServletResponse.SC_NOT_FOUND, message = "no geo coding in progress") })
    @ApiMethod(id = "process", authTypes = AuthRule.IN_WORKSPACE)
    @GET
    @Path("process")
    @Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
    public GeoCodingProcess getGeoCodingProcess() {
        try {
            GeoCodingProcess process = service.getGeoCodingProcess();
            if (process == null) {
                throw new WebApplicationException(
                        Response.status(Response.Status.NOT_FOUND)
                                .entity("Geo coding process not started")
                                .build());
            }
            return process;
        } catch (GeoCodingNotAvailableException e) {
            ApiErrorResponse apiErrorResponse = ApiError.toError(
                    GeoCoderEndpointErrors.GEO_CODING_NOT_AVAILABLE.withDetails(e.getTeamMember().toString()));
            throw new ApiRuntimeException(Response.Status.BAD_REQUEST, apiErrorResponse);
        }
    }

    @ApiOperation("Change location status")
    @ApiResponses(@ApiResponse(code = HttpServletResponse.SC_NO_CONTENT, message = "success"))
    @ApiMethod(id = "process", authTypes = AuthRule.IN_WORKSPACE)
    @POST
    @Path("location-status")
    @Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
    public Response changeLocationStatus(
            @ApiParam("Save location status request") ChangeLocationStatusRequest request) {
        service.changeLocationStatus(request.getAmpActivityId(), request.getAcvlId(), request.getAccepted());
        return Response.noContent().build();
    }
}
