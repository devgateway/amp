package org.digijava.kernel.ampapi.endpoints.interchange;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;

import org.digijava.kernel.ampapi.endpoints.util.JsonBean;
import org.digijava.kernel.request.TLSUtils;
import org.digijava.module.aim.helper.Constants;
import org.digijava.module.aim.helper.TeamMember;


/**
 * 
 * @author acartaleanu
 * Endpoints for the Interchange module of AMP -- activity export / import 
 */

@Path("interchange")
public class InterchangeEndpoints {
	
	@Context
	private HttpServletRequest httpRequest;

	
	@GET
	@Path("/fields")
//	@Produces(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
	public List<JsonBean> getAvailableFields() {
		return InterchangeUtils.getAllAvailableFields();
	}
	
	
	@GET
	@Path("/projects")
	@Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
	public Collection<JsonBean> getProjects() {
		TeamMember tm = (TeamMember) TLSUtils.getRequest().getSession().getAttribute(Constants.CURRENT_MEMBER);
		return InterchangeUtils.getActivityList(tm);
	}
}
