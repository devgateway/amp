package org.digijava.kernel.ampapi.endpoints.config;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.digijava.kernel.ampapi.endpoints.AmpEndpoint;
import org.digijava.kernel.ampapi.endpoints.config.utils.ConfigHelper;
import org.digijava.kernel.ampapi.endpoints.security.AuthRule;
import org.digijava.kernel.ampapi.endpoints.util.ApiMethod;
import org.digijava.module.aim.dbentity.AmpGlobalSettings;
import org.digijava.module.aim.util.FeaturesUtil;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.UriInfo;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


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
    
    public enum SaveResult {
        SAVED, INSERTED,
        @JsonProperty("NOT A VALID VALUE") NOT_VALID_VALUE
    };

    @POST
    @Path("/globalSettings")
    @Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
    @ApiMethod(id = "saveGlobalSettings", authTypes = {AuthRule.IN_ADMIN}, ui = false)
    @ApiOperation(
            value = "Updates or creates global settings.",
            notes = "If a settingsName is provided and a global settings exists with this settingsName, the global "
                    + "settings is updated. Otherwise, the global settings is created.\n")
    public List<Map<String, SaveResult>> saveGlobalSettings(ConfigGlobalSettings settingsRequest) {

        List<Map<String, SaveResult>> resultList = new ArrayList<>();
        List<AmpGlobalSettings> configSettings = settingsRequest.getSettings();
        if (configSettings != null && configSettings.size() > 0) {
            for (AmpGlobalSettings setting : configSettings) {
                Map<String, SaveResult> result = new HashMap<>();
                String globalSettingName = setting.getGlobalSettingsName();
                
                AmpGlobalSettings ampGlobalSetting = FeaturesUtil.getGlobalSetting(globalSettingName);
    
                boolean isNew = false;
                if (ampGlobalSetting == null) {
                    ampGlobalSetting = new AmpGlobalSettings();
                    isNew = true;
                }
                
                ampGlobalSetting.updateValuesFromSetting(setting);

                if (ConfigHelper.validateGlobalSetting(ampGlobalSetting, ampGlobalSetting.getGlobalSettingsValue())) {
                    FeaturesUtil.updateGlobalSetting(ampGlobalSetting);

                    result.put(globalSettingName, (isNew ? SaveResult.INSERTED : SaveResult.SAVED));
                } else {
                    result.put(globalSettingName, SaveResult.NOT_VALID_VALUE);
                }
                resultList.add(result);
            }

        }
        
        return resultList;
    }

    @POST
    @Path("/getGlobalSettings")
    @Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
    @ApiMethod(id = "getGlobalSettings", authTypes = {AuthRule.IN_ADMIN}, ui = false)
    @ApiOperation("Provides global settings options and their possible values for the requested options.")
    public ConfigGlobalSettings getGlobalSettings(
            @ApiParam("Retrieves information about the requested global settings.\n"
                    + "If the requested list is empty the endpoint will return all global settings.")
                    ConfigGlobalSettingsRequest configGlobalSettingsRequest) {
    
        ConfigGlobalSettings configGlobalSettingsResponse = new ConfigGlobalSettings();
        List<GlobalSettingsRequest> requestSettings = configGlobalSettingsRequest.getSettings();
    
        if (requestSettings != null && requestSettings.size() > 0) {
            List<AmpGlobalSettings> globalSettings = new ArrayList<>();
            for (GlobalSettingsRequest requestSetting : requestSettings) {
                AmpGlobalSettings ampGlobalSetting = FeaturesUtil.getGlobalSetting(requestSetting.getSettingName());
    
                if (ampGlobalSetting != null) {
                    globalSettings.add(ampGlobalSetting);
                }
            }
            if (globalSettings.size() > 0) {
                configGlobalSettingsResponse.setSettings(globalSettings);
            }
        } else {
            configGlobalSettingsResponse.setSettings(FeaturesUtil.getGlobalSettings());
        }
        
        return configGlobalSettingsResponse;
    }
    
}
