package org.digijava.kernel.ampapi.endpoints.common;

import com.fasterxml.jackson.annotation.JsonProperty;

public class AmpGisSettings {
    @JsonProperty("multi_country_gis_enabled")
    private Boolean multiCountryEnabled;
    @JsonProperty("gis_sectors_enabled")
    private Boolean sectorsEnabled;


    public Boolean getMultiCountryEnabled() {
        return multiCountryEnabled;
    }

    public void setMultiCountryEnabled(Boolean multiCountryEnabled) {
        this.multiCountryEnabled = multiCountryEnabled;
    }

    public Boolean getSectorsEnabled() {
        return sectorsEnabled;
    }

    public void setSectorsEnabled(Boolean sectorsEnabled) {
        this.sectorsEnabled = sectorsEnabled;
    }

}
