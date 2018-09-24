package org.digijava.kernel.ampapi.endpoints.config;

import static javax.servlet.http.HttpServletResponse.SC_OK;

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

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.digijava.kernel.ampapi.endpoints.AmpEndpoint;
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
@Api("config")
public class ConfigEndpoints implements AmpEndpoint {
    
    @Context
    private HttpServletRequest httpRequest;

    @Context
    private UriInfo uri;
    
    private static final String SAVED = "SAVED";
    private static final String INSERTED = "INSERTED";
    private static final String NOT_VALID = "NOT A VALID GLOBAL SETTING";
    private static final String NOT_VALID_VALUE = "NOT A VALID VALUE";

    @POST
    @Path("/globalSettings")
    @Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
    @ApiMethod(id = "saveGlobalSettings", authTypes = {AuthRule.IN_ADMIN}, ui = false)
    @ApiOperation(
            value = "Updates or creates global settings from a JSON representation.",
            notes = "If a settingsName is provided and a global settings exists with this settingsName, the global "
                    + "settings is updated. Otherwise, the global settings is created.\n"
                    + "\n"
                    + "The response will be an array of JSON objects having information about the global settings "
                    + "and status result (SAVED|INSERTED|NOT A VALID VALUE) \n"
                    + "\n"
                    + "### Sample Input\n"
                    + "```\n"
                    + "{\"settings\" : \n"
                    + " [\n"
                    + "  {\n"
                    + "    \"settingName\": \"Closed activity status\",\n"
                    + "    \"settingValue\": \"211\",\n"
                    + "    \"possibleValues\": \"v_g_settings_activity_statuses\",\n"
                    + "    \"description\": \"(Only valid when feature is enabled) The status corresponding to "
                    + "the \\\"Activity has ended\\\" state\",\n"
                    + "    \"section\": \"general\",\n"
                    + "    \"valueTranslatable\": null,\n"
                    + "    \"possibleValuesIds\": {\n"
                    + "      \"Cancelled / Suspended\": \"200\",\n"
                    + "      \"Completed\": \"201\",\n"
                    + "      \"Considered\": \"202\",\n"
                    + "      \"Ongoing\": \"203\",\n"
                    + "      \"Planned\": \"204\",\n"
                    + "      \"Proposed\": \"205\",\n"
                    + "      \"Closed\": \"211\"\n"
                    + "  }\n"
                    + " ]\n"
                    + "}\n"
                    + "```\n"
                    + "\n"
                    + "### Sample Output\n"
                    + "```\n"
                    + " [\n"
                    + "  {\n"
                    + "   \"Closed activity status\": \"SAVED\"\n"
                    + "  }\n"
                    + " ]\n"
                    + "```\n"
                    + "\n"
                    + "Note: Please check /rest/config/getGlobalSettings for parameters description.")
    @ApiResponses(@ApiResponse(code = SC_OK,
            message = "JSON object with all status information (SAVED|INSERTED|NOT A VALID VALUE)"))
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

    @POST
    @Path("/getGlobalSettings")
    @Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
    @ApiMethod(id = "getGlobalSettings", authTypes = {AuthRule.IN_ADMIN}, ui = false)
    @ApiOperation(
            value = "Provides global settings options and their possible values for the requested options.",
            notes = "The global settings JSON object holds information regarding:\n"
                    + "\n"
                    + "Field|Description\n"
                    + "---|---\n"
                    + "settingName|the name of the global settings\n"
                    + "settingValue|the current value of the settings\n"
                    + "possibleValues|the type of possible values. Available types are: t_Boolean, t_Integer, "
                    + "t_Double, t_year_default_start, t_year_default_end, t_static_range, t_static_year, "
                    + "t_audit_trial_clenaup, t_components_sort, t_daily_currency_update_hour, "
                    + "t_timeout_currency_update \n"
                    + "description|the description of the global settings\n"
                    + "section|the section where the global settings belongs to\n"
                    + "valueTranslatbale|if the global settings has translations\n"
                    + "possibleValuesIds|possible values of the current setting in a { \"name1\" : \"value1\", ...} "
                    + "format\n"
                    + "\n"
                    + "If the request body is an empty JSON object then all global settings will be provided.\n"
                    + "\n"
                    + "In order to retrieve information about specific settings, the request should contain a JSON "
                    + "object with the names of the desirable information.\n"
                    + "\n"
                    + "### Sample Input\n"
                    + "```\n"
                    + "{\"settings\" :\n"
                    + " [\n"
                    + "   {\"settingName\": \"Link Mode of Payment to Funding Status\"},\n"
                    + "   {\"settingName\" : \"Closed activity status\"}\n"
                    + " ]\n"
                    + "}\n"
                    + "```\n"
                    + "\n"
                    + "### Sample Output\n"
                    + "```\n"
                    + "{\n"
                    + "  \"settings\": [\n"
                    + "   {\n"
                    + "    \"settingName\": \"Link Mode of Payment to Funding Status\",\n"
                    + "    \"settingValue\": \"false\",\n"
                    + "    \"possibleValues\": \"t_Boolean\",\n"
                    + "    \"description\": \"Link Mode of Payment to Funding Status\",\n"
                    + "    \"section\": \"funding\",\n"
                    + "    \"valueTranslatable\": null,\n"
                    + "    \"possibleValuesIds\": {}\n"
                    + "   },\n"
                    + "   {\n"
                    + "    \"settingName\": \"Closed activity status\",\n"
                    + "    \"settingValue\": \"211\",\n"
                    + "    \"possibleValues\": \"v_g_settings_activity_statuses\",\n"
                    + "    \"description\": \"(Only valid when feature is enabled) The status corresponding to the "
                    + "\\\"Activity has ended\\\" state\",\n"
                    + "    \"section\": \"general\",\n"
                    + "    \"valueTranslatable\": null,\n"
                    + "    \"possibleValuesIds\": {\n"
                    + "      \"Cancelled / Suspended\": \"200\",\n"
                    + "      \"Completed\": \"201\",\n"
                    + "      \"Considered\": \"202\",\n"
                    + "      \"Ongoing\": \"203\",\n"
                    + "      \"Planned\": \"204\",\n"
                    + "      \"Proposed\": \"205\",\n"
                    + "      \"Closed\": \"211\"\n"
                    + "   }\n"
                    + " ]\n"
                    + "}\n"
                    + "```\n")
    public JsonBean getGlobalSettings(
            @ApiParam("A JSON object containing the information about the requested global settings.\n"
                    + "If the JSON is empty the endpoint will return all global settings")
            JsonBean globalSettings) {
        
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
