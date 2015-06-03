package org.digijava.kernel.ampapi.endpoints.interchange;

import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.digijava.kernel.ampapi.endpoints.common.fm.FMService;
import org.digijava.kernel.ampapi.endpoints.util.ApiMethod;
import org.digijava.kernel.ampapi.endpoints.util.JsonBean;


/**
 * 
 * @author acartaleanu
 * Endpoints for the Interchange module of AMP -- activity export / import 
 */

@Path("interchange")
public class InterchangeEndpoints {
	
	@GET
	@Path("/fields")
//	@Produces(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
	public JsonBean getAvailableFields() {
		return InterchangeUtils.getAllAvailableFields();
	}
}
