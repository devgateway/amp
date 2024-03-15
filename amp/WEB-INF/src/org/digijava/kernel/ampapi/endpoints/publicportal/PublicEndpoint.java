/**
 *
 */
package org.digijava.kernel.ampapi.endpoints.publicportal;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.dgfoundation.amp.ar.ArConstants;
import org.dgfoundation.amp.newreports.GeneratedReport;
import org.dgfoundation.amp.newreports.ReportSpecificationImpl;
import org.digijava.kernel.ampapi.endpoints.common.EndpointUtils;
import org.digijava.kernel.ampapi.endpoints.dashboards.services.PublicServices;
import org.digijava.kernel.ampapi.endpoints.errors.ApiErrorResponse;
import org.digijava.kernel.ampapi.endpoints.gis.SettingsAndFiltersParameters;
import org.digijava.kernel.ampapi.endpoints.publicportal.dto.PublicTotalsByMeasure;
import org.digijava.kernel.ampapi.endpoints.reports.ReportFormParameters;
import org.digijava.kernel.ampapi.endpoints.reports.ReportsUtil;
import org.digijava.kernel.ampapi.endpoints.settings.SettingsUtils;
import org.digijava.kernel.ampapi.endpoints.util.ApiMethod;
import org.digijava.kernel.ampapi.endpoints.util.FilterUtils;

import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.OPTIONS;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import static org.digijava.kernel.ampapi.endpoints.common.EPConstants.REPORT_TYPE_ID_MAP;

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
    public Response getTotalByMeasure(SettingsAndFiltersParameters config) {
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

    /**
     * @see ReportsUtil#getReportResultByPage
     */
    @POST
    @Path("/searchprojects")
    @Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
    @ApiOperation("Generates a custom report.")
    @ApiResponses(@ApiResponse(code = HttpServletResponse.SC_OK, message = "successful operation",
            response = PublicTopData.class))
    public final Response searchProjects(ReportFormParameters formParams) {
        ApiErrorResponse result = ReportsUtil.validateReportConfig(formParams, true);
        if (result != null) {
            return Response.ok(result).build(); // FIXME return bad request
        }
        // we need reportId only to store the report result in cache
        Long reportId = (long) formParams.getReportName().hashCode();
        formParams.setCustom(true);
        return PublicServices.buildOkResponseWithOriginHeaders(PublicPortalService.searchProjects(formParams,
                reportId));
    }

    @OPTIONS
    @Path("/searchprojects")
    @ApiOperation(
            value = "Describe options for endpoint",
            notes = "Enables Cross-Origin Resource Sharing for endpoint")
    public Response describeSearchProjects() {
        return PublicServices.buildOkResponseWithOriginHeaders("");
    }

    /**
     * @see org.digijava.kernel.ampapi.endpoints.reports.Reports#getReportResult(ReportFormParameters)
     * @return a JSON with the report
     */
    @POST
    @Path("/generateReport")
    @Produces({MediaType.APPLICATION_JSON + ";charset=utf-8"})
    @ApiOperation("Render a report preview in JSON format.")
    @ApiResponses(@ApiResponse(code = HttpServletResponse.SC_OK, message = "successful operation",
            response = GeneratedReport.class))
    public final Response getReportResultInJson(
            @ApiParam("a JSON object with the report's parameters") ReportFormParameters formParams) {
        int reportType = ArConstants.DONOR_TYPE;
        if (formParams.getReportType() != null) {
            reportType = REPORT_TYPE_ID_MAP.get(formParams.getReportType());
        }
        ReportSpecificationImpl
                spec = new ReportSpecificationImpl("preview report", reportType);
        spec.setSummaryReport(Boolean.TRUE.equals(formParams.getSummary()));
        String groupingOption = formParams.getGroupingOption();
        ReportsUtil.setGroupingCriteria(spec, groupingOption);
        ReportsUtil.update(spec, formParams);
        SettingsUtils.applySettings(spec, formParams.getSettings(), true);
        FilterUtils.applyFilterRules(formParams.getFilters(), spec, null);
        return PublicServices.buildOkResponseWithOriginHeaders(EndpointUtils.runReport(spec));
    }

    @OPTIONS
    @Path("/generateReport")
    @ApiOperation(
            value = "Describe options for endpoint",
            notes = "Enables Cross-Origin Resource Sharing for endpoint")
    public Response describeReportResultInJson() {
        return PublicServices.buildOkResponseWithOriginHeaders("");
    }
}
