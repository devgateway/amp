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
import org.digijava.kernel.ampapi.endpoints.gis.services.DashboarsService;
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
	@Produces(MediaType.APPLICATION_JSON)
	@ApiMethod(ui = false, id = "topsList")
	public List<JsonBean> getAdminLevelsTotalslist() {
		return DashboarsService.getTopsList();
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
	@Produces(MediaType.APPLICATION_JSON)
	@ApiMethod(ui = false, id = "tops")
	//TODO: Implement Filters
	public JsonBean getAdminLevelsTotals(JsonBean config,
			@PathParam("type") String type,
			@DefaultValue("ac") @QueryParam("adjtype") String adjtype,
			@DefaultValue("5") @QueryParam("limit") Integer limit) {
		return DashboarsService.getTops(type,adjtype,limit,config);
	}
	
	/**
	 * Get aid predictability values for dash boards chart
	 * @param years (number of years to include)
	 * @return
	 */
	
	@POST 
	@Path("/aidPredictability")
	@Produces(MediaType.APPLICATION_JSON)
	@ApiMethod(ui = false, id = "aidPredictability")
	//TODO: Implement Filters
	public JSONObject getAidPredictability(JsonBean config) {
		return DashboarsService.getAidPredictability(config);
	}
	
	/**
	 * Get funding types by year
	 * @param adjtype
	 * @param limit
	 * @return
	 */
	
	@POST 
	@Path("/ftype")
	@Produces(MediaType.APPLICATION_JSON)
	@ApiMethod(ui = false, id = "ftype")
	//TODO: Implement Filters
	public JsonBean getfundingtype(JsonBean config,
			@DefaultValue("ac") @QueryParam("adjtype") String adjtype) {
		return DashboarsService.fundingtype(adjtype,config);
	}
}
