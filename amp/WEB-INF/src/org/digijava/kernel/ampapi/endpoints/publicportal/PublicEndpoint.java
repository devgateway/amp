/**
 * 
 */
package org.digijava.kernel.ampapi.endpoints.publicportal;

import javax.ws.rs.DefaultValue;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import org.digijava.kernel.ampapi.endpoints.util.ApiMethod;
import org.digijava.kernel.ampapi.endpoints.util.JsonBean;

/**
 * Publicly available endpoints
 * @author Nadejda Mandrescu
 */
@Path("public")
public class PublicEndpoint {
	/** the number of top projects to be provided */
	//shouldn't it be configurable?
	private static final String TOP_COUNT = "20";
	
	/**
	 * 
	 */
	@POST
	@Path("/topprojects")
	@Produces(MediaType.APPLICATION_JSON)
	@ApiMethod(ui=false,name="topprojects")
	/**
	 * Retrieves top 'count' projects based on fixed requirements.
	 * @return JsonBean object with results
	 */
	public JsonBean getTopProjects(JsonBean config, 
			@DefaultValue(TOP_COUNT) @QueryParam("count") Integer count, 
			@QueryParam("months") Integer months) {
		return PublicPortalService.getTopProjects(config, count, months);
	}
}
