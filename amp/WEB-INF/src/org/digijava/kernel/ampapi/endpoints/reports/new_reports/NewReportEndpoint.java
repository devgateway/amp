package org.digijava.kernel.ampapi.endpoints.reports.new_reports;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.digijava.kernel.ampapi.endpoints.reports.designer.ReportDesigner;
import org.digijava.kernel.ampapi.endpoints.util.ApiMethod;

import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.List;

@Path("new_report")
@Api("new_report")
public class NewReportEndpoint {
    @ApiOperation("Retrieve the options required for a particular filter type")
@ApiResponses(@ApiResponse(code = HttpServletResponse.SC_OK, message = "filter options",
        response = ReportDesigner.class))
@ApiMethod(id = "options")
@GET
@Path("/options")
@Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
    public List<String> getReportFilterOptions(@QueryParam("type") @DefaultValue("activity_name") String type)
    {
        return NewreportService.getFilterOptions(type);
    }

}
