package org.digijava.kernel.ampapi.endpoints.config;

import io.swagger.annotations.ApiModelProperty;

public class GlobalSettingsRequest {
    
    @ApiModelProperty(example = "Link Mode of Payment to Funding Status")
    private String settingName;
    
    public String getSettingName() {
        return settingName;
    }
    
    public void setSettingName(String settingName) {
        this.settingName = settingName;
    }
}
