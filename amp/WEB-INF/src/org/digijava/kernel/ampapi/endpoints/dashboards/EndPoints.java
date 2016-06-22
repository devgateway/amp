package org.digijava.kernel.ampapi.endpoints.dashboards;

import java.util.List;

import javax.ws.rs.DefaultValue;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import org.digijava.kernel.ampapi.endpoints.common.EndpointUtils;
import org.digijava.kernel.ampapi.endpoints.dashboards.services.DashboardsService;
import org.digijava.kernel.ampapi.endpoints.dashboards.services.HeatMapConfigs;
import org.digijava.kernel.ampapi.endpoints.dashboards.services.HeatMapService;
import org.digijava.kernel.ampapi.endpoints.util.ApiMethod;
import org.digijava.kernel.ampapi.endpoints.util.JsonBean;

import net.sf.json.JSONObject;


/**
 * 
 * @author Diego Dimunzio
 * - All dash boards end points 
 */

@Path("dashboard")
public class EndPoints {
	/**
	 * Show a list of available top ___ things, with their names
	 * @return
	 */
	
	@GET
	@Path("/tops")
	@Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
	@ApiMethod(ui = false, id = "topsList")
	public List<JsonBean> getAdminLevelsTotalslist() {
		return DashboardsService.getTopsList();
	}

	/**
	 * Get top donors values for dash boards chart
	 * @param type (Chart Type)
	 * @param adjtype (Adjustment Type)
	 * @param limit (Result Limit)
	 * @return
	 */
	
	@POST 
	@Path("/tops/{type}")
	@Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
	@ApiMethod(ui = false, id = "tops")
	//TODO: Implement Filters
	public JsonBean getAdminLevelsTotals(JsonBean config,
			@PathParam("type") String type,
			@DefaultValue("5") @QueryParam("limit") Integer limit) {
		return DashboardsService.getTops(type, limit, config);
	}
	
	/**
	 * Get aid predictability values for dash boards chart
	 * @param years (number of years to include)
	 * @return
	 */
	
	@POST 
	@Path("/aid-predictability")
	@Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
	@ApiMethod(ui = false, id = "aidPredictability")
	public JSONObject getAidPredictability(JsonBean filter) throws Exception {
		return DashboardsService.getAidPredictability(filter);
	}
	
	/**
	 * Get funding types by year
	 * @param adjtype
	 * @param limit
	 * @return
	 */
	
	@POST 
	@Path("/ftype")
	@Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
	@ApiMethod(ui = false, id = "ftype")
	//TODO: Implement Filters
	public JsonBean getfundingtype(JsonBean config,
			@DefaultValue("ac") @QueryParam("adjtype") String adjtype) {
		return DashboardsService.fundingtype(adjtype,config);
	}
	
	@POST
	@Path("/saved-charts")
	@Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
	@ApiMethod(ui = false, id = "SaveChart")
	public JsonBean savedMaps(final JsonBean pChrat) {
		return EndpointUtils.saveApiState(pChrat,"C");
	}

	@GET
	@Path("/saved-charts/{chartId}")
	@Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
	@ApiMethod(ui = false, id = "ChartById")
	public JsonBean savedCharts(@PathParam("chartId") Long chartId) {
		return EndpointUtils.getApiState(chartId);

	}

	@GET
	@Path("/saved-charts")
	@Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
	@ApiMethod(ui = false, id = "ChartList")
	public List<JsonBean> savedCharts() {
		String type="C";
		return EndpointUtils.getApiStateList(type);
	}
	
	@POST
	@Path("/tops/ndd/{id}")
	@Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
	@ApiMethod(ui = false, id = "ndd_projects")
	public JsonBean getAdminLevelsTotals(JsonBean config, @PathParam("id") Integer id) {
		//TODO: Once we implement details for all top charts we can change the path to '/tops/details/' 
		// and send the type of chart and category id as params. 
		return DashboardsService.getPeaceMarkerProjectsByCategory(config, id);
	}
	
	/**
     * <pre>
     * Build Heat Map based on the expected input format:
     * INPUT:
     * {
     *  “count” : 25, // omit for no limit
     *  “xColumn” : “Primary Sector”, // must be OrigName
     *  “yColumn” : “Donor Group”, // must be origName
     *  “filters”: { ... }, // usual filters input
     *  “settings” : { ... } // usual settings input, and Dashboard specific with Measure selection
     * }
     * OUTPUT:
     * {
     *  “summary” : [“Primary Sector”, “Donor Group”, “Actual Commitments”],
     *  “xDataSet” : [“Education”, “Health”, ...],
     *  “yDataSet” : [“World Bank Group”, “ADB”, ...],
     *  “xPTotals” : [100, ...], // percentage, 100 for each X per current rules
     *  “xTotals” : [“5 000”, …], // formatted abmounts
     *  “yPTotals” : [17, ...],
     *  “yTotals”: [“800”, …],
     *  “matrix” : [[100, ...], null, [...], ...],
     * } 
     * </pre> 
     * @param config exp
     * @return
     */
	@POST
    @Path("/heat-map")
    @Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
    @ApiMethod(ui = false, id = "heatMap")
    public JsonBean getHeatMap(JsonBean config) {
        return new HeatMapService(config).buildHeatMap();
    }
	
	/**
	 * OUTPUT:
	 * {
	 *     “columns” : [{“name” : “Donor Group”, “origName”: “Donor Group”},
	 *                  {“name” : “Primary Sector”, “origName”: “...”},
	 *                  {“name” : “Primary Sector Sub-Sector”, ...},
	 *                  …
	 *                  {“name” : “Secondary Program Level 8”, ...}
	 *                  ],
	 *     “charts” : [{
	 *                 “type” : “S”, // other options: “P”, “L”
	 *                 “name” : “Fragmentation by Donor and Sector”,
	 *                 “yColumns” : [0], xColumns : [1, 2, 3] // indexes ref of all used columns
	 *                 }, ....],
	 *     “colors” :  [ {“#C20C0C” : 0}, {“#FF7F7F” : 1}, ...] // i.e. use #FF7F7F color for values >= 1
	 * }
	 * @return existing HeatMap configurations
	 */
	@GET
    @Path("/heat-map/configs")
    @Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
    @ApiMethod(ui = false, id = "heatMapConfigs")
    public JsonBean getHeatMapConfigs() {
        return new HeatMapConfigs().getHeatMapConfigs();
    }
}
