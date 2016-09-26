package org.digijava.kernel.ampapi.endpoints.activity;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.digijava.kernel.ampapi.endpoints.activity.utils.ActivityEndpointUtils;
import org.digijava.kernel.ampapi.endpoints.security.AuthRule;
import org.digijava.kernel.ampapi.endpoints.util.ApiMethod;
import org.digijava.kernel.ampapi.endpoints.util.JsonBean;

/**
 * 
 * @author Gabriel Inchauspe
 *
 */
@Path("cloneUtil")
public class ActivityUtilEndpoint {

	@POST
	@Path("/clone")
	@Consumes(MediaType.APPLICATION_JSON + ";charset=utf-8")
	@Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
	@ApiMethod(authTypes = {AuthRule.NONE}, ui = false, name = "clone", id = "")
	//@ApiMethod(authTypes = {AuthRule.TOKEN, AuthRule.IN_ADMIN}, ui = false, name = "clone", id = "")
	public JsonBean cloneActivities(JsonBean config) {
		return ActivityEndpointUtils.cloneActivities(config);
	}
}