package org.digijava.kernel.ampapi.endpoints.common;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import org.digijava.kernel.ampapi.endpoints.settings.SettingsUtils;
import org.digijava.kernel.ampapi.endpoints.util.ApiMethod;
import org.digijava.kernel.ampapi.endpoints.util.JsonBean;
import org.digijava.kernel.ampapi.endpoints.util.SpringUtil;
import org.digijava.kernel.services.AmpVersionService;

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
	 * Example of 'settings' json object provided as POST input in API endpoints: 
	 * <pre>
	 * settings : { "1": "CAD", 
	 *              "2" : "6", 
	 *              "yearRange": {"yearFrom":"2005", "yearTo":"2020"}
	 *            }
	 * </pre>
	 * </br>
	 * <h3>Sample Output:</h3><pre>
     * [
     *   ...,
     *   {
     *    "id": "use-icons-for-sectors-in-project-list",
     *    "multi": false,
     *    "name": "Use icons for Sectors in Project List",
     *    "defaultId": "true",
     *    "options": [
     *      {
     *        "id": "true",
     *        "name": "true",
     *        "value": "true"
     *      }
     *    ]
     *   },
     *   {
     *    "id": "project-sites",
     *    "multi": false,
     *    "name": "Project Sites",
     *    "defaultId": "true",
     *    "options": [
     *    {
     *      "id": "true",
     *      "name": "true",
     *      "value": "true"
     *    }
     *   ]
     *  },
     *  ...
     * ]</pre>
	 * 
	 * @return a list of setting options
	 * @see SettingOptions
	 */
	@GET
	@Path("/settings")
	@Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
	@ApiMethod(ui = false, id = "Settings")
	public JsonBean getSettings() {
		return SettingsUtils.getGeneralSettings();
	}

	/**
	 * Check if AMP Offline App is compatible with AMP.
	 * <p>This method will check if AMP Offline App is compatible with AMP. The only input it takes is version of
	 * AMP Offline. Also returns AMP version and whenever AMP Offline is enabled or not.</p>
	 */
	@GET
	@Path("/amp-offline-version-check")
	@Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
	@ApiMethod(ui = false, id = "version-check")
	public VersionCheckResponse ampOfflineVersionCheck(
			@QueryParam("amp-offline-version") String ampOfflineVersion) {

		AmpVersionService ampVersionService = SpringUtil.getBean(AmpVersionService.class);

		VersionCheckResponse response = new VersionCheckResponse();
		response.setAmpOfflineCompatible(ampVersionService.isAmpOfflineCompatible(ampOfflineVersion));
		response.setAmpOfflineEnabled(true);
		response.setAmpVersion(ampVersionService.getVersionInfo().getAmpVersion());
		return response;
	}
}
