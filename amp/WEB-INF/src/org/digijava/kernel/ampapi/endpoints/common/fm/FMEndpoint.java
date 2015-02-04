/**
 * 
 */
package org.digijava.kernel.ampapi.endpoints.common.fm;

import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

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
}

	