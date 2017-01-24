package org.digijava.kernel.ampapi.endpoints.permissions;

import java.security.acl.Permission;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.PathSegment;

import org.dgfoundation.amp.Util;
import org.digijava.kernel.ampapi.endpoints.security.AuthRule;
import org.digijava.kernel.ampapi.endpoints.util.ApiMethod;
import org.digijava.kernel.ampapi.endpoints.util.JsonBean;
import org.digijava.kernel.persistence.PersistenceManager;
import org.digijava.module.aim.dbentity.AmpActivity;
import org.digijava.module.aim.dbentity.AmpStructure;
import org.hibernate.Query;

/**
 * This class should have all security / permissions related methods
 * 
 * @author jdeanquin@developmentgateway.org
 * 
 */
@Path("permissions")
public class Permissions {
	
	@GET
	@Path("/activity-can-do/{activityId}")
	@Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
	@ApiMethod(id = "activity-can-do", authTypes = { AuthRule.IN_WORKSPACE }, ui = false)
	/**
	 * Retrieve a list of permissions for the given activity
	 * @param activityIds array of activities
	 * </br>
	 * <dl>
	 * As a route param we receive an array of activities to get permissions for
	 *
	 * </br>
	 * <h3>Sample Output:</h3><pre>
	 * {
	 *   "title": "Donors",
	 *   "children": [
	 *     {
	 *       "key": 1,
	 *       "title": "Government of Timor-Leste",
	 *       "folder": true,
	 *       "children": [
	 *         {
	 *           "key": 70,
	 *           "title": "RDTL Line Ministry",
	 *           "folder": true,
	 *           "children": [
	 *             {
	 *               "key": 153,
	 *               "title": "Ministry of Health",
	 *               "folder": false,
	 *               "selected": true
	 *             }
	 *           ]
	 *         },
	 *  ......
	 *     }
	 *    ]
	 *  }</pre>
	 *
	 * @return a JSON object with all donors
	 */
	public List<JsonBean> canDo(@PathParam("activityId") PathSegment activityIds) {

		return PermissionsService.getPermissionsForActivities(Arrays.asList(activityIds.getPath().split("\\s*,\\s*")));

	}
}
