/**
 * 
 */
package org.digijava.kernel.ampapi.endpoints.common.fm;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.digijava.kernel.ampapi.endpoints.activity.utils.ActivityEndpointUtils;
import org.digijava.kernel.ampapi.endpoints.util.ApiMethod;
import org.digijava.kernel.ampapi.endpoints.util.JsonBean;

/**
 * Feature Manager Endpoint provides FM settings 
 * @author Nadejda Mandrescu
 */
@Path("common")
public class FMEndpoint {

	@POST
	@Path("/fm")
	@Produces(MediaType.APPLICATION_JSON)
	@ApiMethod(ui=false, name="fm", id="")
	/**
	 * Retrieves 
	 * @param config
	 * @return
	 */
	public JsonBean getFMSettings(JsonBean config) {
		return FMService.getFMSettings(config);
	}
	
	@POST
	@Path("/clone")
	@Produces(MediaType.APPLICATION_JSON)
	@ApiMethod(ui = false, name = "clone", id = "")
	/**
	 * Retrieves 
	 * @param config
	 * @return
	 */
	public JsonBean cloneActivities(JsonBean config) {
		//TODO: check user logged in.
		//TODO: basic sanitization checks.
		
		// Convert to set just in case the data contains the same AMP_ID more than once.
		Set<String> uniqueActivityIds = new HashSet<String>((List<String>) config.get("activities"));
		return ActivityEndpointUtils.cloneActivities(uniqueActivityIds);
	}
}

	