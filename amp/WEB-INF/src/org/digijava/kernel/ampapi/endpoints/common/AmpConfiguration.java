package org.digijava.kernel.ampapi.endpoints.common;

import java.util.List;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.digijava.kernel.ampapi.endpoints.settings.SettingOptions;
import org.digijava.kernel.ampapi.endpoints.settings.SettingsUtils;
import org.digijava.kernel.ampapi.endpoints.util.ApiMethod;

/**
 * This class should have all end point related to the configuration of amp
 * @author Diego Dimunzio
 * 
 */

@Path("amp")
public class AmpConfiguration {
	@GET
	@Path("/settings")
	@Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
	@ApiMethod(ui = false, id = "Settings")
	public List<SettingOptions> getSettings() {
		return SettingsUtils.getGisSettings();
	}
}
