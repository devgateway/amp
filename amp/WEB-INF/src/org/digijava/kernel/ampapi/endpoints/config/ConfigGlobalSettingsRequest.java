package org.digijava.kernel.ampapi.endpoints.config;

import java.util.List;

public class ConfigGlobalSettingsRequest {
    
    private List<GlobalSettingsRequest> settings;
    
    public List<GlobalSettingsRequest> getSettings() {
        return settings;
    }
    
    public void setSettings(List<GlobalSettingsRequest> settings) {
        this.settings = settings;
    }
}
