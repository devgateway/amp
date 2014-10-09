package org.digijava.kernel.ampapi.endpoints.dashboards;

import javax.ws.rs.DefaultValue;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.digijava.kernel.ampapi.endpoints.gis.services.DashboarsService;
import org.digijava.kernel.ampapi.endpoints.util.ApiMethod;
import org.digijava.kernel.ampapi.endpoints.util.JsonBean;

/**
 * 
 * @author Diego Dimunzio
 * - All dash boards end points 
 */

@Path("dashboard")
public class EndPoints {

	@GET
	@Path("/topdonors/{limit}")
	@Produces(MediaType.APPLICATION_JSON)
	@ApiMethod(ui=false,name="topdonors")
	/**
	 * Get top donors values for dash boards chart
	 * @param limit
	 * @return
	 */
	public JsonBean getAdminLevelsTotals(@DefaultValue("5") @PathParam ("limit") Integer limit ){
		return  DashboarsService.getTopDonors(limit);
	}
}
