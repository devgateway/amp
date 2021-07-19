/**
 *
 */
package org.digijava.kernel.ampapi.endpoints.publicportal;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.digijava.kernel.ampapi.endpoints.dashboards.services.PublicServices;
import org.digijava.kernel.ampapi.endpoints.util.ApiMethod;

import javax.ws.rs.DefaultValue;
import javax.ws.rs.GET;
import javax.ws.rs.OPTIONS;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/**
 * Publicly available endpoints
 * @author Nadejda Mandrescu
 */
@Path("public")
@Api("public")
public class PublicEndpoint {
    /** the number of top projects to be provided */
    //shouldn't it be configurable?
    private static final String TOP_COUNT = "20";

    @POST
    @Path("/topprojects")
    @Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
    @ApiMethod(ui = false, id = "topprojects")
    @ApiOperation("Retrieves top 'count' projects based on fixed requirements.")
    public PublicTopData getTopProjects(PublicReportFormParameters config,
            @DefaultValue(TOP_COUNT) @QueryParam("count") Integer count,
            @QueryParam("months") Integer months) {
        return PublicPortalService.getTopProjects(config, count, months);
    }

    @POST
    @Path("/donorFunding")
    @Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
    @ApiMethod(ui = false, id = "donorFunding")
    @ApiOperation(
            value = "Retrieves Donor Disbursements/Commitments List for the last X days",
            notes = "Get donor funding for the specific funding type "
                    + "with possibility to filter by number of records or age")
    public PublicTopData getDonorFunding(@ApiParam(required = true) PublicReportFormParameters config,
            @ApiParam(value = "the number of top records to show")
            @QueryParam("count") Integer count,
            @ApiParam(value = "the last number of months to consider")
            @QueryParam("months") Integer months,
            @ApiParam(value = "1 for commitment, 2 for disbursement", allowableValues = "1,2", required = true)
            @DefaultValue("1") @QueryParam("fundingType") Integer fundingType) {
        return PublicPortalService.getDonorFunding(config, count, months,fundingType);
    }

    @POST
    @Path("/activitiesPledges")
    @Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
    @ApiMethod(ui = false, id = "activitiesPledges")
    @ApiOperation("Retrieves the count for activities that have been at least linked to one pledge")
    public int getActivitiesPledgesCount(@ApiParam(required = true) PublicReportFormParameters config) {
        return PublicPortalService.getActivitiesPledgesCount(config);
    }

    @GET
    @Path("/top/{measure}")
    @Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
    @ApiMethod(ui = false, id = "activitiesPledges")
    @ApiOperation("Retrieves the count for activities that have been at least linked to one pledge")
    public Response getTopByMeasure(
            @PathParam("measure")
            @ApiParam(value = "Measure", example = "Actual Commitments")
                    String measure) {
            return PublicServices.buildOkResponseWithOriginHeaders(PublicPortalService.getTotalByMeasure(measure));
    }


    @OPTIONS
    @Path("/top/{measure}")
    @ApiOperation(
            value = "Describe options for endpoint",
            notes = "Enables Cross-Origin Resource Sharing for endpoint")
    public Response describeTopByMeasure() {
        return PublicServices.buildOkResponseWithOriginHeaders("");
    }
}
