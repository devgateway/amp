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
import org.digijava.kernel.ampapi.endpoints.security.AuthRule;
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
     *  “xCount” : 25, // default 25, set -1 to no limit. +1 ("Others") will be added if more than that available
     *  “yCount” : 10, // default 10, set -1 to no limit. +1 ("Others") will be added if more than that available
     *  “xColumn” : “Primary Sector”, // must be OrigName
     *  “yColumn” : “Donor Group”, // must be origName
     *  “filters”: { ... }, // usual filters input
     *  “settings” : { ... } // usual settings input, and Dashboard specific with Measure selection
     * }
     * OUTPUT:
     * {
     *  “summary” : [“Primary Sector”, “Donor Group”, “Actual Commitments”],
     *  “xDataSet” : [“Education”, “Health”, ...], // may end with "Others" (translated) for anything cut off 
     *  “yDataSet” : [“World Bank Group”, “ADB”, ...], // may end with "Others" (translated) for anything cut off
     *  “xPTotals” : [100, ...], // percentage, 100 for each X per current rules
     *  “xTotals” : [“5 000”, …], // formatted abmounts
     *  “yPTotals” : [17, ...],
     *  “yTotals”: [“800”, …],
     *  “matrix” : [[{“p”: 100, “dv” : “12 000”}, ...], null, [...], ...], // p = % amount, dv = display value
     *  "xTotalCount" : 30,// the actual total count of entries for X. Can be used to detect if "Other" is present on X
     *  "yTotalCount" : 20 // the actual total count of entries for Y. Can be used to detect if "Other" is present on Y
     * } 
     * </pre> 
     * @param config exp
     * @return
     * 
     * IMPORTANT NOTE ABOUT /{type} PARAMETER: This extra parameter is needed here because the UI differentiates each heatmap in the dashboard by its url, 
     * 	so we need for each heatmap (by Sector, Location or Program) an extra parameter that isnt actually used on the backend.
     */
	@POST
    @Path("/heat-map/{type}")
    @Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
    @ApiMethod(ui = false, id = "heatMap")
    public JsonBean getHeatMap(JsonBean config) {
	    return new HeatMapService(config).buildHeatMap();
    }
	
	/**
	 * Provides possible HeatMap Configurations
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
	 *     “amountColors” :  [ {0 : “#d05151”}, {1 : #e68787}, ...] // i.e. for values >= 1, use #e68787 color
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
	
	/**
	 * Provides HeatMap Admin Settings
	 * OUTPUT:
	 * {
	 *     “amountColors” :[ {       // i.e. for values >= 0, use #d05151 color
	 *     “id” : 1,
	 *     “amountFrom” : 0, // a floating point number
	 *     “color” : “#d05151”,
	 *     “name” : “Dark Red” // translated name
	 *     }, …
	 *     ]
	 * }
	 * @return JSON structure of HeatMap Administrative Settings
	 */
	@GET
    @Path("/heat-map/settings")
    @Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
    @ApiMethod(ui = false, id = "readHeatMapSettings", authTypes = {AuthRule.IN_ADMIN})
    public JsonBean getHeatMapSettings() {
        return new HeatMapConfigs().getHeatMapAdminSettings();
    }
	
	/**
	 * Updates HeatMapSettings with new configuration
	 * INPUT:
	 * {
	 *     “amountColors” : [{ “id”: 1, “color” : “#d05151”, “amountFrom” : 0}, ...] 
	 * }
	 * Note: for now we have a fixed set, but in future we may want to allow different number of colors and nuances
	 * OUTPUT: empty {} on success, or result with errors
	 * {
	 *     “error” : {
	 *         “1234” : [“Invalid color threshold”]
	 *         ...
	 *     }
	 * }
	 * @param config
	 * @return
	 * @throws Exception 
	 */
	@POST
    @Path("/heat-map/settings")
    @Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
    @ApiMethod(ui = false, id = "readHeatMapSettings", authTypes = {AuthRule.IN_ADMIN})
    public JsonBean setHeatMapSettings(JsonBean config) throws Exception {
        return new HeatMapConfigs().saveHeatMapAdminSettings(config);
    }
}
