package org.digijava.kernel.ampapi.endpoints.config;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashMap;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.UriInfo;

import org.digijava.kernel.ampapi.endpoints.config.utils.ConfigHelper;
import org.digijava.kernel.ampapi.endpoints.security.AuthRule;
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
	@ApiMethod(id = "saveGlobalSettings", authTypes = {AuthRule.IN_ADMIN}, ui = false)
	public Collection<JsonBean> saveGlobalSettings(JsonBean globalSettings) {

		ArrayList<String> list = ConfigHelper.getValidSettings();
		Collection<JsonBean> resultList = new ArrayList<JsonBean>();

		if (globalSettings.get("settings") != null) {
			ArrayList<LinkedHashMap<String, Object>> settings = (ArrayList<LinkedHashMap<String, Object>>) globalSettings
					.get("settings");

			for (LinkedHashMap<String, Object> setting : settings) {
				JsonBean result = new JsonBean();
				String globalSettingName = ConfigHelper.getGlobalSettingName(setting);
				if (list.contains(globalSettingName)) {
					boolean isNew = false;
					AmpGlobalSettings ampGlobalSetting = FeaturesUtil.getGlobalSetting(globalSettingName);

					if (ampGlobalSetting == null) {
						ampGlobalSetting = new AmpGlobalSettings();
						isNew = true;
					}

					ConfigHelper.getGlobalSetting(ampGlobalSetting, setting);

					FeaturesUtil.updateGlobalSetting(ampGlobalSetting);

					result.set(globalSettingName, (isNew ? INSERTED : SAVED));

				} else {
					result.set(globalSettingName, NOT_VALID);
				}
				resultList.add(result);
			}

		}

		return resultList;
	}
	
	
	/**
	 * get global settings
	 * @param globalSettings jsonBean with a settings to save
	 * @return save's status jsonBean 
	 */
	@POST
	@Path("/getGlobalSettings")
	@Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
	@ApiMethod(id = "getGlobalSettings", authTypes = {AuthRule.IN_ADMIN}, ui = false)
	public Collection<JsonBean> getGlobalSettings(JsonBean globalSettings) {
		
		Collection<JsonBean> resultList = new ArrayList<JsonBean>();
		ArrayList<LinkedHashMap<String, Object>> settings = null;
		ArrayList<String> list = null;
		
		if (globalSettings.get("settings") != null) {
			list = ConfigHelper.getValidSettings();
			settings = (ArrayList<LinkedHashMap<String, Object>>) globalSettings.get("settings");
		}
		
		if (settings != null && settings.size()>0) {
			for (LinkedHashMap<String, Object> setting : settings) {
				JsonBean result = new JsonBean();
				String globalSettingName = ConfigHelper.getGlobalSettingName(setting);
				if (list.contains(globalSettingName)) {
					boolean isNew = false;
					AmpGlobalSettings ampGlobalSetting = FeaturesUtil.getGlobalSetting(globalSettingName);

					if (ampGlobalSetting != null) {
						result = ConfigHelper.getGlobalSettingJson(ampGlobalSetting);
					}

				} else {
					result.set(globalSettingName, NOT_VALID);
				}
				resultList.add(result);
			}
		}else{
			Collection<AmpGlobalSettings> ampGlobalSettings = FeaturesUtil.getGlobalSettings();
			for (AmpGlobalSettings sett : ampGlobalSettings) {
				JsonBean result = new JsonBean();
	            result = ConfigHelper.getGlobalSettingJson(sett);
	            resultList.add(result);
	        }
		}

		return resultList;
	}
	
	
}
