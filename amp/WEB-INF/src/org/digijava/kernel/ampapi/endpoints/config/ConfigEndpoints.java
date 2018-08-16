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
 * @implicitParam X-Auth-Token|string|header
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
    private static final String NOT_VALID_VALUE = "NOT A VALID VALUE";
    
    
    /**
     * Updates or creates global settings from a JSON representation.
     * </br>
     * If a settingsName is provided and a global settings exists with this settingsName, the global settings is updated. Otherwise, the global settings is created.<br>
     * The response will be an array of JSON objects having information about the global settings and status result (SAVED|INSERTED|NOT A VALID VALUE) 
     * 
     * <h3>Sample Input:</h3><pre>
     * {"settings" : 
     *  [
     *   {
     *     "settingName": "Closed activity status",
     *     "settingValue": "211",
     *     "possibleValues": "v_g_settings_activity_statuses",
     *     "description": "(Only valid when feature is enabled) The status corresponding to the \"Activity has ended\" state",
     *     "section": "general",
     *     "valueTranslatable": null,
     *     "possibleValuesIds": {
     *       "Cancelled / Suspended": "200",
     *       "Completed": "201",
     *       "Considered": "202",
     *       "Ongoing": "203",
     *       "Planned": "204",
     *       "Proposed": "205",
     *       "Closed": "211"
     *   }
     *  ]
     * }</pre>
     * </br>
     * <h3>Sample Output:</h3><pre>
     *  [
     *   {
     *    "Closed activity status": "SAVED"
     *   }
     *  ]</pre>
     *  
     *  
     *  </br>
     *  Note: Please check /rest/config/getGlobalSettings for parameters description.
     * 
     * @param globalSettings JSON representation of the global settings
     * @return JSON object with all status information (SAVED|INSERTED|NOT A VALID VALUE) 
     */
    @POST
    @Path("/globalSettings")
    @Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
    @ApiMethod(id = "saveGlobalSettings", authTypes = {AuthRule.IN_ADMIN}, ui = false)
    public Collection<JsonBean> saveGlobalSettings(JsonBean globalSettings) {

        //ArrayList<String> list = ConfigHelper.getValidSettings();
        Collection<JsonBean> resultList = new ArrayList<JsonBean>();
        if (globalSettings.get("settings") != null) {
            ArrayList<LinkedHashMap<String, Object>> settings = (ArrayList<LinkedHashMap<String, Object>>) globalSettings
                    .get("settings");

            for (LinkedHashMap<String, Object> setting : settings) {
                JsonBean result = new JsonBean();
                String globalSettingName = ConfigHelper.getGlobalSettingName(setting);
                //if (list.contains(globalSettingName)) {
                    boolean isNew = false;
                    AmpGlobalSettings ampGlobalSetting = FeaturesUtil.getGlobalSetting(globalSettingName);

                    if (ampGlobalSetting == null) {
                        ampGlobalSetting = new AmpGlobalSettings();
                        isNew = true;
                    }

                    ConfigHelper.getGlobalSetting(ampGlobalSetting, setting);
                    
                    if (ConfigHelper.validateGlobalSetting(ampGlobalSetting, ampGlobalSetting.getGlobalSettingsValue())) {
                        FeaturesUtil.updateGlobalSetting(ampGlobalSetting);
    
                        result.set(globalSettingName, (isNew ? INSERTED : SAVED));
                    } else {
                        result.set(globalSettingName, NOT_VALID_VALUE);
                    }
                    
                //} else {
                    //  result.set(globalSettingName, NOT_VALID);
                    //}
                resultList.add(result);
            }

        }
        
        return resultList;
    }
    
    /**
     * Provides global settings options and their possible values for the requested options.
     * </br>
     * <dl>
     * The global settings JSON object holds information regarding:
     * <dt><b>settingName</b><dd> - the name of the global settings
     * <dt><b>settingValue</b><dd> - the current value of the settings
     * <dt><b>possibleValues</b><dd> - the type of possible values. Available types are: t_Boolean, t_Integer,
     * t_Double, t_year_default_start, t_year_default_end, t_static_range, t_static_year, t_audit_trial_clenaup,
     * t_components_sort, t_daily_currency_update_hour, t_timeout_currency_update
     * <dt><b>description</b><dd> - the description of the global settings
     * <dt><b>section</b><dd> - the section where the global settings belongs to
     * <dt><b>valueTranslatbale</b><dd> - if the global settings has translations
     * <dt><b>possibleValuesIds</b><dd> - possible values of the current setting in a { "name1" : "value1", ...} format
     * </dl></br></br>
     * If the request contains an empty JSON, all global settings will be provided.
     * Empty Request: <code>{}</code></br></br>
     * 
     * In order to retrieve information about specific settings, the request should contain a JSON object with the names of the desirable information.
     * 
     * <h3>Sample Input:</h3><pre>
     * {"settings" : 
     *  [
     *    {"settingName": "Link Mode of Payment to Funding Status"},
     *    {"settingName" : "Closed activity status"}
     *  ]
     * }</pre>
     * </br>
     * <h3>Sample Output:</h3><pre>
     * {
     *   "settings": [
     *    {
     *     "settingName": "Link Mode of Payment to Funding Status",
     *     "settingValue": "false",
     *     "possibleValues": "t_Boolean",
     *     "description": "Link Mode of Payment to Funding Status",
     *     "section": "funding",
     *     "valueTranslatable": null,
     *     "possibleValuesIds": {}
     *    },
     *    {
     *     "settingName": "Closed activity status",
     *     "settingValue": "211",
     *     "possibleValues": "v_g_settings_activity_statuses",
     *     "description": "(Only valid when feature is enabled) The status corresponding to the \"Activity has ended\" state",
     *     "section": "general",
     *     "valueTranslatable": null,
     *     "possibleValuesIds": {
     *       "Cancelled / Suspended": "200",
     *       "Completed": "201",
     *       "Considered": "202",
     *       "Ongoing": "203",
     *       "Planned": "204",
     *       "Proposed": "205",
     *       "Closed": "211"
     *    }
     *  ]
     * }</pre>
     * 
     * 
     * @param globalSettings a JSON object containing the information about the requested global settings. 
     * If the JSON is empty the endpoint will return all global settings
     * 
     * @return a JSON object with the available global settings 
     */
    @POST
    @Path("/getGlobalSettings")
    @Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
    @ApiMethod(id = "getGlobalSettings", authTypes = {AuthRule.IN_ADMIN}, ui = false)
    public JsonBean getGlobalSettings(JsonBean globalSettings) {
        
        Collection<JsonBean> resultList = new ArrayList<JsonBean>();
        JsonBean finalFormatResult = new JsonBean();
        ArrayList<LinkedHashMap<String, Object>> settings = null;
        //ArrayList<String> list = null;
        
        if (globalSettings.get("settings") != null) {
            //list = ConfigHelper.getValidSettings();
            settings = (ArrayList<LinkedHashMap<String, Object>>) globalSettings.get("settings");
        }
        
        if (settings != null && settings.size()>0) {
            for (LinkedHashMap<String, Object> setting : settings) {
                JsonBean result = new JsonBean();
                String globalSettingName = ConfigHelper.getGlobalSettingName(setting);
                //if (list.contains(globalSettingName)) {
                    boolean isNew = false;
                    AmpGlobalSettings ampGlobalSetting = FeaturesUtil.getGlobalSetting(globalSettingName);

                    if (ampGlobalSetting != null) {
                        result = ConfigHelper.getGlobalSettingJson(ampGlobalSetting);
                    }

                    //} else {
                    //result.set(globalSettingName, NOT_VALID);
                    //}
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
        
        finalFormatResult.set("settings", resultList);
         
        return finalFormatResult;
    }
    
    
}
