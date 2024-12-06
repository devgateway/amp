package org.digijava.kernel.ampapi.endpoints.indicator.AmpDashboard;

import com.fasterxml.jackson.annotation.JsonProperty;

public class CoreIndicatorTypeDTO {
    private String name;
    @JsonProperty("core-type")
    private String coreType;
    private String unit;

    public String getCoreType() {
        return coreType;
    }

    public void setCoreType(String coreType) {
        this.coreType = coreType;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
