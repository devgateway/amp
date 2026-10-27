package org.digijava.kernel.ampapi.endpoints.analytics;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.digijava.kernel.ampapi.endpoints.security.AuthRule;
import org.digijava.kernel.ampapi.endpoints.util.ApiMethod;
import org.digijava.kernel.services.analytics.DataImportService;

import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.util.List;
import java.util.Map;

/**
 * Endpoints for fetching data from views for import into another database
 */
@Path("analytics")
@Api("analytics")
public class DataImportEndpoints {

    /**
     * Fetches data from the specified views
     *
     * @param viewNames list of view names to fetch data from
     * @return map of view name to list of records
     */
    @POST
    @Path("/views-data")
    @Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
    @Consumes(MediaType.APPLICATION_JSON)
    @ApiMethod(id = "importViewsData")
    @ApiOperation(
            value = "Fetch data from views for import",
            notes = "Returns data from the specified views in a format suitable for import into another database")
    @ApiResponses({
            @ApiResponse(code = HttpServletResponse.SC_OK, message = "Data from views"),
            @ApiResponse(code = HttpServletResponse.SC_BAD_REQUEST, message = "Invalid request")
    })
    public Map<String, Object> importData(
            @ApiParam(value = "List of view names to fetch data from", required = true)
            List<String> viewNames) {

        return DataImportService.fetchDataFromViews(viewNames);
    }

    /**
     * Gets all available views in the database
     *
     * @return list of view names
     */
    @GET
    @Path("/views")
    @Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
    @ApiMethod(id = "getAllViews")
    @ApiOperation(
            value = "Get all available views",
            notes = "Returns a list of all available views in the database")
    @ApiResponses({
            @ApiResponse(code = HttpServletResponse.SC_OK, message = "List of views"),
            @ApiResponse(code = HttpServletResponse.SC_INTERNAL_SERVER_ERROR, message = "Error fetching views")
    })
    public List<String> getAllViews() {
        return DataImportService.getAllViews();
    }
}
