package org.digijava.kernel.ampapi.endpoints.common;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.digijava.kernel.ampapi.endpoints.settings.SettingsConstants;

public class CurrencySettings {
    
    @JsonProperty(SettingsConstants.ID)
    private Long id;
    
    @JsonProperty(SettingsConstants.CODE)
    private String code;
    
    public CurrencySettings(Long id, String code) {
        this.id = id;
        this.code = code;
    }
    
    public Long getId() {
        return id;
    }
    
    public void setId(Long id) {
        this.id = id;
    }
    
    public String getCode() {
        return code;
    }
    
    public void setCode(String code) {
        this.code = code;
    }
}
