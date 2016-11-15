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
	
	/**
	 * Provides available settings and their possible values.
	 * <br>
	 * These settings should be supported by almost all API calls.<br>
	 * Note: most API endpoints will also need to accept a 'settings' object on queries.<br>
	 * <br>
	 * Example of 'settings' json object used in API endpoints: 
	 * <pre>
	 * settings : { "1": "CAD", 
	 *              "2" : "6", 
	 *              "yearRange": {"yearFrom":"2005", "yearTo":"2020"}
	 *            }
	 * </pre>
	 * 
	 * @return a list of setting options
	 * @see SettingOptions
	 */
	@GET
	@Path("/settings")
	@Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
	@ApiMethod(ui = false, id = "Settings")
	public List<SettingOptions> getSettings() throws NumberFormatException, Exception {
		return SettingsUtils.getGisSettings();
	}
}
