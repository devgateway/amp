/**
 * 
 */
package org.digijava.kernel.ampapi.endpoints.publicportal;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.digijava.kernel.ampapi.endpoints.util.ApiMethod;
import org.digijava.kernel.ampapi.endpoints.util.JsonBean;

/**
 * Publicly available endpoints
 * @author Nadejda Mandrescu
 */
@Path("public")
public class PublicEndpoint {

	/**
	 * 
	 */
	@GET
	@Path("/topprojects")
	@Produces(MediaType.APPLICATION_JSON)
	@ApiMethod(ui=false,name="topprojects")
	/**
	 * Retrieves top 20 projects based on fixed requirements.
	 * @return JsonBean object with results
	 */
	public JsonBean getTopProjects() {
		return PublicPortalService.getTopProjects();
	}
}
