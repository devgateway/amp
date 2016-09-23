package org.digijava.kernel.ampapi.endpoints.activity;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.digijava.kernel.ampapi.endpoints.activity.utils.ActivityEndpointUtils;
import org.digijava.kernel.ampapi.endpoints.common.fm.FMService;
import org.digijava.kernel.ampapi.endpoints.util.ApiMethod;
import org.digijava.kernel.ampapi.endpoints.util.JsonBean;

/**
 * 
 * @author Gabriel Inchauspe
 *
 */
@Path("activityUtils")
public class ActivityEndpoint {

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
		// poner comentario q el q tendria q poder clonar es el usuario validador del workspace.
		
		// Convert to set just in case the data contains the same AMP_ID more than once.
		Set<String> uniqueActivityIds = new HashSet<String>((List<String>) config.get("activities"));
		return ActivityEndpointUtils.cloneActivities(uniqueActivityIds);
	}
}