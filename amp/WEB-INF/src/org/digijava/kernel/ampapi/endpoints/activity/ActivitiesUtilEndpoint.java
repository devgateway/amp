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
	 * Clone each activity from a list, returns an object with a list of success activities and a list of failed, draft, not found activities.
	 * 
	 * 
	 * @param The list contains the ampId (String) not the id (Long) of the activity (this can be extended in the future if needed). 
	 *        {
  				"activities": ["1120098741544","112009iati23324","112009iati233242","1120093767137"]}
	 * @return {
  		"succeed":[{"112009iati23324": [67135,67138]},{ "1120098741544": [67136,67139]}],
  	    "failed": [{"112009iati23324": "Error message"}],
    	"draft": ["1120093767137"],
    	"not_found": ["112009iati233242"]
		}
	 */
	@POST
	@Path("/clone")
	@Consumes(MediaType.APPLICATION_JSON + ";charset=utf-8")
	@Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
	@ApiMethod( ui = false, name = "clone", id = "")
	public JsonBean cloneActivities(JsonBean config) {
		return CloneActivitiesUtil.cloneActivities(config);
	}
}