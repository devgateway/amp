/**
 *
 */
package org.digijava.kernel.ampapi.endpoints.publicportal;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.digijava.kernel.ampapi.endpoints.dashboards.services.PublicServices;
import org.digijava.kernel.ampapi.endpoints.gis.SettingsAndFiltersParameters;
import org.digijava.kernel.ampapi.endpoints.publicportal.dto.PublicTotalsByMeasure;
import org.digijava.kernel.ampapi.endpoints.util.ApiMethod;

import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.OPTIONS;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
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
    @ApiResponses(@ApiResponse(code = HttpServletResponse.SC_OK, message = "Top projects",
            response = PublicTopData.class))
    public Response getTopProjects(PublicReportFormParameters config,
                                   @DefaultValue(TOP_COUNT) @QueryParam("count") Integer count,
                                   @QueryParam("months") Integer months,
                                   @QueryParam("lastUpdated") @DefaultValue("false") boolean lastUpdated) {
        return PublicServices.buildOkResponseWithOriginHeaders(
                PublicPortalService.getTopProjects(config, count, months, lastUpdated));
    }

    @OPTIONS
    @Path("/topprojects")
    @ApiOperation(
            value = "Describe options for endpoint",
            notes = "Enables Cross-Origin Resource Sharing for endpoint")
    public Response describeTopProjects() {
        return PublicServices.buildOkResponseWithOriginHeaders("");
    }

    @POST
    @Path("/donorFunding")
    @Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
    @ApiMethod(ui = false, id = "donorFunding")
    @ApiOperation(
            value = "Retrieves Donor Disbursements/Commitments List for the last X days",
            notes = "Get donor funding for the specific funding type "
                    + "with possibility to filter by number of records or age")
    @ApiResponses(@ApiResponse(code = HttpServletResponse.SC_OK, message = "Top donor funding",
            response = PublicTopData.class))
    public Response getDonorFunding(@ApiParam(required = true) PublicReportFormParameters config,
                                    @ApiParam(value = "the number of top records to show")
                                    @QueryParam("count") Integer count,
                                    @ApiParam(value = "the last number of months to consider")
                                    @QueryParam("months") Integer months,
                                    @ApiParam(value = "1 for commitment, 2 for disbursement", allowableValues =
                                            "1,2", required = true)
                                    @DefaultValue("1") @QueryParam("fundingType") Integer fundingType,
                                    @ApiParam(value = "true for Donor group, false for Donor agency", allowableValues =
                                            "true,false", required = false)
                                    @DefaultValue("false") @QueryParam("showDonorGroup") boolean showDonorGroup) {
        return PublicServices.buildOkResponseWithOriginHeaders(PublicPortalService.getDonorFunding(config, count,
                months, fundingType, showDonorGroup));
    }

    @OPTIONS
    @Path("/donorFunding")
    @ApiOperation(
            value = "Describe options for endpoint",
            notes = "Enables Cross-Origin Resource Sharing for endpoint")
    public Response describeDonorFunding() {
        return PublicServices.buildOkResponseWithOriginHeaders("");
    }

    @POST
    @Path("/activitiesPledges")
    @Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
    @ApiMethod(ui = false, id = "activitiesPledges")
    @ApiOperation("Retrieves the count for activities that have been at least linked to one pledge")
    public int getActivitiesPledgesCount(@ApiParam(required = true) PublicReportFormParameters config) {
        return PublicPortalService.getActivitiesCount(config != null ? config.getFilters() : null, true);
    }

    @POST
    @Path("/totalByMeasure")
    @Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
    @ApiMethod(ui = false, id = "getTopByMeasure")
    @ApiOperation("Total funding by measure")
    @ApiResponses(@ApiResponse(code = HttpServletResponse.SC_OK, message = "Top chart data",
            response = PublicTotalsByMeasure.class))
    public Response getTotalByMeasure(SettingsAndFiltersParameters config,
                                      String measure) {
        return PublicServices.buildOkResponseWithOriginHeaders(PublicPortalService.getTotalByMeasure(config));
    }


    @OPTIONS
    @Path("/totalByMeasure")
    @ApiOperation(
            value = "Describe options for endpoint",
            notes = "Enables Cross-Origin Resource Sharing for endpoint")
    public Response describeTotalByMeasure() {
        return PublicServices.buildOkResponseWithOriginHeaders("");
    }


    @POST
    @Path("/projectCount")
    @Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
    @ApiMethod(ui = false, id = "projectCount")
    @ApiOperation(value = "get total project count respecting filters")
    @ApiResponses(@ApiResponse(code = HttpServletResponse.SC_OK, message = "Top chart data",
            response = PublicTotalsByMeasure.class))
    public Response getTotalActivities(SettingsAndFiltersParameters config) {
        return PublicServices.buildOkResponseWithOriginHeaders(PublicPortalService.getCountByMeasure(config));
    }


    @OPTIONS
    @Path("/projectCount")
    @ApiOperation(
            value = "Describe options for endpoint",
            notes = "Enables Cross-Origin Resource Sharing for endpoint")
    public Response describeTotalActivities() {
        return PublicServices.buildOkResponseWithOriginHeaders("");
    }
}
