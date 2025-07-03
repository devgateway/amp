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
@Api(value = "Reports Designer API", description = "Endpoints for creating, retrieving, and updating reports in the AMP Report Designer")
public class ReportsDesignerEndpoint {

    private final ReportDesignerService reportDesignerService = new ReportDesignerService();

    @ApiOperation(
                value = "Get report designer configuration",
                notes = "This endpoint provides all configuration settings needed for the Report Designer.\n\n" +
                        "### What you'll get\n" +
                        "- Available columns you can add to reports\n" +
                        "- Measures (like actual disbursements, actual commitments e.t.c)\n" +
                        "- Hierarchies for organizing data\n" +
                        "- Filter options\n" +
                        "- Other settings for building reports\n\n" +
                        "### Parameters\n" +
                        "- **profile**: The report profile type\n" +
                        "  - 'R' = Regular reports (default)\n" +
                        "  - 'T' = Tabs\n\n" +
                        "- **type**: The report data focus\n" +
                        "  - 'D' = Donor perspective (default)\n" +
                        "  - 'R' = Regional Funding Reports\n" +
                        "  - 'C' = Component Funding Reports\n\n" +
                        "### Example request\n" +
                        "```\n" +
                        "GET /rest/reports/designer?profile=R&type=D\n" +
                        "```")
    @ApiResponses({
        @ApiResponse(code = HttpServletResponse.SC_OK, message = "Configuration retrieved successfully",
                    response = ReportDesigner.class),
        @ApiResponse(code = HttpServletResponse.SC_BAD_REQUEST, message = "Invalid profile or type parameter")
    })
    @ApiMethod(id = "designer")
    @GET
    @Path("/designer")
    @Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
    public final ReportDesigner getReport(
            @ApiParam(value = "Report profile type (R=Regular)", defaultValue = "R")
            @QueryParam("profile") @DefaultValue("R") String profile,
            @ApiParam(value = "Report data focus (D=Donor, R=Regional, C=Component)", defaultValue = "D")
            @QueryParam("type") @DefaultValue("D") String type) {
        return reportDesignerService.getReportDesigner(getReportProfile(profile), getReportType(type));
    }

    @ApiOperation(
                value = "Get a specific report by ID",
                notes = "This endpoint retrieves a single report using its unique ID.\n\n" +
                        "### What you'll get\n" +
                        "- Complete report configuration\n" +
                        "- All columns included in the report\n" +
                        "- Measures and calculations used\n" +
                        "- Applied filters\n" +
                        "- Sorting and grouping settings\n\n" +
                        "### Common uses\n" +
                        "- View an existing report's structure\n" +
                        "- Edit a report's configuration\n" +
                        "- Create a new report based on an existing one\n\n" +
                        "### Requirements\n" +
                        "- You must be a member of a workspace\n" +
                        "- You must have access to the requested report\n\n" +
                        "### Example request\n" +
                        "```\n" +
                        "GET /rest/reports/123\n" +
                        "```")
    @ApiResponses({
        @ApiResponse(code = HttpServletResponse.SC_OK, message = "Report retrieved successfully",
                    response = Report.class),
        @ApiResponse(code = HttpServletResponse.SC_NOT_FOUND, message = "Report not found"),
        @ApiResponse(code = HttpServletResponse.SC_FORBIDDEN, message = "Not authorized to access this report")
    })
    @ApiMethod(id = "getReport", authTypes = AuthRule.IN_WORKSPACE)
    @GET
    @Path("/{reportId}")
    @Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
    public final Report getReport(
            @ApiParam(value = "Unique ID of the report to retrieve", required = true)
            @PathParam("reportId") Long reportId) {
        return reportDesignerService.getReport(reportId);
    }

    @ApiOperation(
                value = "Create a new report",
                notes = "This endpoint creates a new custom report based on your specifications.\n\n" +
                        "### Required information\n" +
                        "- Report name\n" +
                        "- Columns to include\n" +
                        "- Measures (like actual disbursements, actual commitments e.t.c)\n" +
                        "- Filters to apply\n" +
                        "- Hierachies to apply\n" +
                        "- Sorting preferences\n\n" +
                        "### Parameters\n" +
                        "- **reportRequest**: Your complete report configuration (JSON)\n" +
                        "  - Must include name, columns, and other settings\n" +
                        "  - See example below for format\n" +
                        "- **isDynamic**: Create as a dynamic report? (default: false)\n" +
                        "  - true = Report data updates automatically\n" +
                        "  - false = Report data is static\n\n" +
                        "### What happens\n" +
                        "- System validates your configuration\n" +
                        "- Creates the report if valid\n" +
                        "- Returns the new report with its ID\n" +
                        "- Returns error details if something is wrong\n\n" +
                        "### Example request\n" +
                        "```\n" +
                        "POST /rest/reports/?isDynamic=false\n" +
                        "Content-Type: application/json\n\n" +
                        "{\n" +
                        "  \"name\": \"My New Report\",\n" +
                        "  \"columns\": [1,2,3],\n" +
                        "  \"measures\": [1,2,3],\n" +
                        "  \"hierarchies\": [1,2,3],\n" +
                        "  \"filters\": [...],\n" +
                        "  \"sorting\": [...]\n" +
                        "}\n" +
                        "```")
    @ApiMethod(id = "createReport")
    @ApiResponses({
            @ApiResponse(code = HttpServletResponse.SC_OK, reference = "JsonApiResponse",
                    message = "Report created successfully"),
            @ApiResponse(code = HttpServletResponse.SC_BAD_REQUEST, reference = "JsonApiResponse",
                    message = "Invalid report configuration"),
            @ApiResponse(code = HttpServletResponse.SC_UNAUTHORIZED, message = "Authentication required")
    })
    @POST
    @Path("/")
    @Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
    public final JsonApiResponse<Report> createReport(
            @ApiParam(value = "Complete report configuration with name, columns, measures, etc.", required = true)
            ReportRequest reportRequest,
            @ApiParam(value = "Set to true for dynamic reports that update automatically", defaultValue = "false")
            @QueryParam("isDynamic") @DefaultValue("false") Boolean isDynamic) {
        return reportDesignerService.createReport(reportRequest, isDynamic);
    }

    @ApiOperation(
                value = "Update an existing report",
                notes = "This endpoint modifies an existing report with your updated configuration.\n\n" +
                        "### Required information\n" +
                        "- Report ID (in the URL path)\n" +
                        "- Updated report configuration (in request body)\n\n" +
                        "### Parameters\n" +
                        "- **reportId**: ID of the report to update (in URL path)\n" +
                        "- **reportRequest**: Your updated report configuration (JSON)\n" +
                        "  - Include all fields you want to change\n" +
                        "  - Fields not included will keep their current values\n\n" +
                        "### Requirements\n" +
                        "- You must be a member of a workspace\n" +
                        "- You must have permission to edit this report\n\n" +
                        "### What happens\n" +
                        "- System validates your configuration\n" +
                        "- Updates the report if valid\n" +
                        "- Returns the updated report\n" +
                        "- Returns error details if something is wrong\n\n" +
                        "### Example request\n" +
                        "```\n" +
                        "POST /rest/reports/123\n" +
                        "Content-Type: application/json\n\n" +
                        "{\n" +
                        "  \"name\": \"Updated Report Name\",\n" +
                        "  \"columns\": [1,2,3],\n" +
                        "  \"measures\": [1,2,3],\n" +
                        "  \"hierarchies\": [1,2,3],\n" +
                        "  \"filters\": [...],\n" +
                        "  \"sorting\": [...]\n" +
                        "}\n" +
                        "```")
    @ApiMethod(id = "updateReport", authTypes = AuthRule.IN_WORKSPACE)
    @ApiResponses({
            @ApiResponse(code = HttpServletResponse.SC_OK, reference = "JsonApiResponse",
                    message = "Report updated successfully"),
            @ApiResponse(code = HttpServletResponse.SC_BAD_REQUEST, reference = "JsonApiResponse",
                    message = "Invalid report configuration"),
            @ApiResponse(code = HttpServletResponse.SC_NOT_FOUND, message = "Report not found"),
            @ApiResponse(code = HttpServletResponse.SC_FORBIDDEN, message = "Not authorized to update this report")
    })
    @POST
    @Path("/{reportId}")
    @Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
    public final JsonApiResponse<Report> createReport(
            @ApiParam(value = "Updated report configuration with name, columns, measures, etc.", required = true)
            ReportRequest reportRequest,
            @ApiParam(value = "ID of the report to update", required = true)
            @PathParam("reportId") Long reportId) {
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
