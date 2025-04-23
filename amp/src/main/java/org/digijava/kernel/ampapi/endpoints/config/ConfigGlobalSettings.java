package org.digijava.kernel.ampapi.endpoints.config;

import org.digijava.module.aim.dbentity.AmpGlobalSettings;

import java.util.List;

public class ConfigGlobalSettings {
    
    private List<AmpGlobalSettings> settings;
    
    public List<AmpGlobalSettings> getSettings() {
        return settings;
    }
    
    public void setSettings(List<AmpGlobalSettings> settings) {
        this.settings = settings;
    }
}
