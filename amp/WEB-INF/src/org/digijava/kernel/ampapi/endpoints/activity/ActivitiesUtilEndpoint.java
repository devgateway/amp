package org.digijava.kernel.ampapi.endpoints.activity;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.digijava.kernel.ampapi.endpoints.activity.utils.CloneActivitiesUtil;
import org.digijava.kernel.ampapi.endpoints.security.AuthRule;
import org.digijava.kernel.ampapi.endpoints.util.ApiMethod;
import org.digijava.kernel.ampapi.endpoints.util.JsonBean;

/**
 * This class can have utility methods related to activities.
 * 
 * @author Gabriel Inchauspe
 *	
 */
@Path("activitiesUtil")
public class ActivitiesUtilEndpoint {

	/**
	 * Clone each activity from a list, returns an object with a list of success activities and a list of failed activities.
	 * The list contains the ampId (String) not the id (Long) of the activity (this can be extended in the future if needed).
	 * 
	 * @param config: {"activities": ["AMP_ID1", "AMP_ID2", "AMP_ID3", "AMP_ID4", "AMP_ID5"]}
	 * @return {succeed: [{OLD_ID1, NEW_ID1}, {OLD_ID2, NEW_ID2}], failed: [AMP_ID3, AMP_ID4, AMP_ID5]}
	 */
	@POST
	@Path("/clone")
	@Consumes(MediaType.APPLICATION_JSON + ";charset=utf-8")
	@Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
	@ApiMethod(authTypes = {AuthRule.TOKEN, AuthRule.IN_ADMIN}, ui = false, name = "clone", id = "")
	public JsonBean cloneActivities(JsonBean config) {
		return CloneActivitiesUtil.cloneActivities(config);
	}
}