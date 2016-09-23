package org.digijava.kernel.ampapi.endpoints.activityUtil;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.digijava.kernel.ampapi.endpoints.activityUtil.ActivityEndpointUtils;
import org.digijava.kernel.ampapi.endpoints.common.fm.FMService;
import org.digijava.kernel.ampapi.endpoints.security.AuthRule;
import org.digijava.kernel.ampapi.endpoints.util.ApiMethod;
import org.digijava.kernel.ampapi.endpoints.util.JsonBean;

/**
 * 
 * @author Gabriel Inchauspe
 *
 */
@Path("activityUtil")
public class ActivityEndpoint {

	@POST
	@Path("/clone")
	@Consumes(MediaType.APPLICATION_JSON + ";charset=utf-8")
	@Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
	@ApiMethod(authTypes = {AuthRule.NONE}, ui = false, name = "clone2", id = "")
	public JsonBean cloneActivities(JsonBean config) {
		return ActivityEndpointUtils.cloneActivities(config);
	}
}