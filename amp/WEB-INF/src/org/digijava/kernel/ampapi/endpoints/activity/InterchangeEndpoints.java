package org.digijava.kernel.ampapi.endpoints.activity;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;

import org.digijava.kernel.ampapi.endpoints.util.JsonBean;
import org.digijava.kernel.request.TLSUtils;
import org.digijava.module.aim.dbentity.AmpActivityFields;
import org.digijava.module.aim.helper.Constants;
import org.digijava.module.aim.helper.TeamMember;


/**
 * AMP Activity Endpoints for Activity Import / Export
 * 
 * @author acartaleanu
 */

@Path("activity")
public class InterchangeEndpoints {
	
	@Context
	private HttpServletRequest httpRequest;

	/**
	 * Returns a list of JSON objects, each describing a possible value that might be specified 
	 * in an activity field 
	 * 
	 * 
	 * 
	 * @param fieldName, the Activity field title, underscorified (see <InterchangeUtils.underscorify for details>
	 * @return list of JsonBean objects, each representing a possible value
	 */
	@GET
	@Path("fields/{fieldName}")
	@Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
	public List<JsonBean> getValues(@PathParam("fieldName") String fieldName) {
		return PossibleValuesEnumerator.getPossibleValuesForField(fieldName, AmpActivityFields.class, null);
	}

	@GET
	@Path("fields")
	@Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
	public List<JsonBean> getAvailableFields() {
		
		return FieldsEnumerator.getAllAvailableFields();
	}
	
	/**
	 * Returns a JSON object with the list of all projects on the system, including its view and edit status for the current logged user.
	 * If the user can view the project, the 'view' property of the project is set to true. False otherwise.
	 * If the user can edit the project, the 'edit' property of the project on the JSON is set to true. False otherwise.
	 * Pagination can be used if the parameters are sent on the request. If not parameters are sent, the full list
	 * of projects is returned.
	 * 
	 * @param pid  current pagination request reference (random id). It acts as a key for a LRU caching mechanism that holds the 
	 * full list of projects for the current user. If it is not provided no caching is used
	 * @param offset, Integer used for pagination. It represents which is the first project to return. It helps to skip the unnecessary 
	 * records.
	 * @param size, Integer used for pagination. It tells how many projects to return
	 * @return list of JsonBean with all the projects on the system
	 */
	@GET
	@Path("/projects")
	@Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
	public Collection<JsonBean> getProjects(@QueryParam ("pid") String pid,@QueryParam("offset") Integer offset, @QueryParam("count") Integer count) {
		TeamMember tm = (TeamMember) TLSUtils.getRequest().getSession().getAttribute(Constants.CURRENT_MEMBER);
		Collection<JsonBean> activityCollection = ProjectList.getActivityList(pid, tm);
		int start = 0;
		int end = activityCollection.size() - 1;
		if (offset != null && count != null && offset < activityCollection.size()) {
			start = offset.intValue();
			if (activityCollection.size() > (offset + count)) {
				end = offset + count;
			}
		}
		return new ArrayList(activityCollection).subList(start, end);
	}
	

	/**
	 * Provides full project information 
	 * @param projectId project id
	 * @return project with full set of configured fields and their values 
	 */
	@GET
	@Path("/projects/{projectId}")
	@Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
	public JsonBean getProject(@PathParam("projectId") Long projectId) {
		return InterchangeUtils.getActivity(projectId);
	}
	
	/**
	 * Provides full project information 
	 * @param projectId project id
	 * @param filter jsonBean with a list of fields that will be displayed
	 * @return project with full set of configured fields and their values 
	 */
	@POST
	@Path("/projects/{projectId}")
	@Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
	public JsonBean getProject(@PathParam("projectId") Long projectId, JsonBean filter) {
		return InterchangeUtils.getActivity(projectId, filter);
	}
	
	/**
	 * Imports an activity
	 * @param newJson activity configuration
	 * @return latest project overview or an error if invalid configuration is received
	 */
	@POST
	@Path("/")
	@Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
	public JsonBean addProject(JsonBean newJson) {
		return InterchangeUtils.importActivity(newJson, false);
	}
	
	/**
	 * Updates an activity
	 * @param newJson activity configuration
	 * @return latest project overview or an error if invalid configuration is received
	 */
	@PUT
	@Path("/")
	@Produces(MediaType.APPLICATION_JSON + ";charset=utf-8") 
	public JsonBean updateProject(JsonBean newJson) {
		return InterchangeUtils.importActivity(newJson, true);
	}
}
