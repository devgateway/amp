package org.digijava.kernel.ampapi.endpoints.reports.designer;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.digijava.kernel.ampapi.endpoints.common.JsonApiResponse;
import org.digijava.kernel.ampapi.endpoints.errors.ApiError;
import org.digijava.kernel.ampapi.endpoints.errors.ApiRuntimeException;
import org.digijava.kernel.ampapi.endpoints.security.AuthRule;
import org.digijava.kernel.ampapi.endpoints.util.ApiMethod;

import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
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

    @ApiOperation(
                value = "Retrieve configuration information for the report designer",
                notes = "This endpoint returns all the necessary configuration information needed to initialize " +
                        "and populate the AMP Report Designer interface.\n\n" +
                        "The response includes available report columns, measures, hierarchies, filters, and other " +
                        "metadata required to build and customize reports. This information is structured according " +
                        "to the specified report profile and type.\n\n" +
                        "**Parameters:**\n" +
                        "- **profile**: Report profile code (default: 'R' for Reports)\n" +
                        "- **type**: Report type code (default: 'D' for Donor)")
    @ApiResponses(@ApiResponse(code = HttpServletResponse.SC_OK, message = "report designer info",
                    response = ReportDesigner.class))
    @ApiMethod(id = "designer")
    @GET
    @Path("/designer")
    @Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
    public final ReportDesigner getReport(@QueryParam("profile") @DefaultValue("R") String profile,
                                          @QueryParam("type") @DefaultValue("D") String type) {
        return reportDesignerService.getReportDesigner(getReportProfile(profile), getReportType(type));
    }

    @ApiOperation(
                value = "Retrieve a specific report by ID",
                notes = "This endpoint returns detailed information about a single report identified by its ID.\n\n" +
                        "The response includes the report's configuration, columns, measures, filters, and other " +
                        "settings that define how the report is structured and what data it displays.\n\n" +
                        "This information can be used to view, edit, or duplicate an existing report. The endpoint " +
                        "requires that the user has access to the specified report and is a member of a workspace.")
    @ApiResponses(@ApiResponse(code = HttpServletResponse.SC_OK, message = "report",
            response = Report.class))
    @ApiMethod(id = "getReport", authTypes = AuthRule.IN_WORKSPACE)
    @GET
    @Path("/{reportId}")
    @Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
    public final Report getReport(@PathParam("reportId") Long reportId) {
        return reportDesignerService.getReport(reportId);
    }

    @ApiOperation(
                value = "Create a new report",
                notes = "This endpoint allows users to create a new report with custom configuration.\n\n" +
                        "The request must include the report's name, columns, measures, filters, and other " +
                        "settings that define how the report is structured and what data it displays.\n\n" +
                        "**Parameters:**\n" +
                        "- **reportRequest**: The complete report configuration in JSON format\n" +
                        "- **isDynamic**: Whether the report should be created as a dynamic report (default: false)\n\n" +
                        "On success, the response includes the newly created report with its assigned ID and " +
                        "other metadata. If there are validation errors or other issues, appropriate error " +
                        "messages will be returned.")
    @ApiMethod(id = "createReport")
    @ApiResponses({
            @ApiResponse(code = HttpServletResponse.SC_OK, reference = "JsonApiResponse",
                    message = "latest project overview"),
            @ApiResponse(code = HttpServletResponse.SC_BAD_REQUEST, reference = "JsonApiResponse",
                    message = "error if invalid configuration is received")})
    @POST
    @Path("/")
    @Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
    public final JsonApiResponse<Report> createReport(@ApiParam("reportId") ReportRequest reportRequest,
                                                  @QueryParam("isDynamic") @DefaultValue("false") Boolean isDynamic) {
        return reportDesignerService.createReport(reportRequest, isDynamic);
    }

    @ApiOperation(
                value = "Update an existing report",
                notes = "This endpoint allows users to modify an existing report identified by its ID.\n\n" +
                        "The request must include the updated report configuration including name, columns, " +
                        "measures, filters, and other settings that define how the report is structured and " +
                        "what data it displays.\n\n" +
                        "**Parameters:**\n" +
                        "- **reportRequest**: The complete updated report configuration in JSON format\n" +
                        "- **reportId**: The ID of the report to update\n\n" +
                        "On success, the response includes the updated report with its metadata. If there are " +
                        "validation errors or other issues, appropriate error messages will be returned.\n\n" +
                        "This endpoint requires that the user is a member of a workspace and has permission " +
                        "to modify the specified report.")
    @ApiMethod(id = "updateReport", authTypes = AuthRule.IN_WORKSPACE)
    @ApiResponses({
            @ApiResponse(code = HttpServletResponse.SC_OK, reference = "JsonApiResponse",
                    message = "latest project overview"),
            @ApiResponse(code = HttpServletResponse.SC_BAD_REQUEST, reference = "JsonApiResponse",
                    message = "error if invalid configuration is received")})
    @POST
    @Path("/{reportId}")
    @Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
    public final JsonApiResponse<Report> createReport(
            @ApiParam("reportId") ReportRequest reportRequest,
            @ApiParam("the id of the report which should be updated") @PathParam("reportId") Long reportId) {
        return reportDesignerService.updateReport(reportRequest, reportId);
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
