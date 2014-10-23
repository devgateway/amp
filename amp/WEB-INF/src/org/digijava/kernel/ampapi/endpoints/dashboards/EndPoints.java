package org.digijava.kernel.ampapi.endpoints.dashboards;

import java.util.List;

import javax.ws.rs.DefaultValue;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import net.sf.json.JSONObject;

import org.digijava.kernel.ampapi.endpoints.gis.services.DashboarsService;
import org.digijava.kernel.ampapi.endpoints.util.ApiMethod;
import org.digijava.kernel.ampapi.endpoints.util.DashboardFilters;
import org.digijava.kernel.ampapi.endpoints.util.JsonBean;
import org.digijava.module.aim.helper.GlobalSettingsConstants;
import org.digijava.module.aim.util.FeaturesUtil;

/**
 * 
 * @author Diego Dimunzio
 * - All dash boards end points 
 */

@Path("dashboard")
public class EndPoints {

	@GET
	@Path("/tops")
	@Produces(MediaType.APPLICATION_JSON)
	@ApiMethod(ui=false,name="topsList")
	/**
	 * Show a list of available top ___ things, with their names
	 * @return
	 */
	public List<JsonBean> getAdminLevelsTotalslist() {
		return DashboarsService.getTopsList();
	}

	@GET
	@Path("/tops/{type}")
	@Produces(MediaType.APPLICATION_JSON)
	@ApiMethod(ui=false,name="tops")
	/**
	 * Get top donors values for dash boards chart
	 * @param type (Chart Type)
	 * @param adjtype (Adjustment Type)
	 * @param limit (Result Limit)
	 * @return
	 */
	public JsonBean getAdminLevelsTotals(
			@PathParam("type") String type,
			@DefaultValue("ac") @QueryParam("adjtype") String adjtype,
			@DefaultValue("5") @QueryParam("limit") Integer limit) {
		return DashboarsService.getTops(type,adjtype,limit);
	}
	
	@GET
	@Path("/aidPredictability")
	@Produces(MediaType.APPLICATION_JSON)
	@ApiMethod(ui=false,name="aidPredictability")
	/**
	 * Get aid predictability values for dash boards chart
	 * @param years (number of years to include)
	 * @return
	 */
	public JSONObject getAidPredictability() {
		return DashboarsService.getAidPredictability(null);
	}
}
