package org.digijava.kernel.ampapi.endpoints.activity;

import org.codehaus.jackson.annotate.JsonProperty;

/**
 * @author Viorel Chihai
 */
public class CurrencyExtraInfo {

    @JsonProperty("active")
    private final Boolean active;

    public CurrencyExtraInfo(Boolean active) {
        this.active = active;
    }
}
