package org.digijava.kernel.ampapi.endpoints.config;

import java.util.ArrayList;
import java.util.LinkedHashMap;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.UriInfo;

import org.digijava.kernel.ampapi.endpoints.config.utils.ConfigHelper;
import org.digijava.kernel.ampapi.endpoints.util.ApiMethod;
import org.digijava.kernel.ampapi.endpoints.util.JsonBean;
import org.digijava.module.aim.dbentity.AmpGlobalSettings;
import org.digijava.module.aim.util.FeaturesUtil;


/**
 * AMP Config Endpoints for Config save
 * 
 * @author apicca
 */
@Path("config")
public class ConfigEndpoints {
	
	@Context
	private HttpServletRequest httpRequest;

    @Context
    private UriInfo uri;
    
    private static final String SAVED = "SAVED";
    private static final String INSERTED = "INSERTED";
    private static final String NOT_VALID = "NOT A VALID GLOBAL SETTING";
	/**
	 * Save global settings
	 * @param globalSettings jsonBean with a settings to save
	 * @return save's status jsonBean 
	 */
	@POST
	@Path("/globalSettings")
	@Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
	@ApiMethod(id = "saveGlobalSettings", ui = false)
	public JsonBean saveGlobalSettings(JsonBean globalSettings) {
		
		ArrayList<String> list = ConfigHelper.getValidSettings();
		JsonBean result = new JsonBean();

		if (globalSettings.get("settings") != null) {
			ArrayList<LinkedHashMap<String, Object>> settings = (ArrayList<LinkedHashMap<String, Object>>) globalSettings
					.get("settings");

			for (LinkedHashMap<String, Object> setting : settings) {

					String globalSettingName = ConfigHelper.getGlobalSettingName(setting);
					if (list.contains(globalSettingName)) {
					boolean isNew = false;
					AmpGlobalSettings ampGlobalSetting = FeaturesUtil.getGlobalSetting(globalSettingName);

					if (ampGlobalSetting==null) {
						ampGlobalSetting = new AmpGlobalSettings();
						isNew = true;
					}
					
					ConfigHelper.getGlobalSetting(ampGlobalSetting, setting);
					
					FeaturesUtil.updateGlobalSetting(ampGlobalSetting);

					result.set(globalSettingName, (isNew? INSERTED : SAVED ));
					
					} else {
						result.set(globalSettingName, NOT_VALID);
					}
					
			}

		}

		return result;
	}
	
}
