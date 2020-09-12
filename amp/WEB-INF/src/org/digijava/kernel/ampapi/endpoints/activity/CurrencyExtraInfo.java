package org.digijava.kernel.ampapi.endpoints.activity;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * @author Viorel Chihai
 */
public class CurrencyExtraInfo {

    @JsonProperty("active")
    private final Boolean active;

    public CurrencyExtraInfo(Boolean active) {
        this.active = active;
    }
    
    public Boolean getActive() {
        return active;
    }
}
