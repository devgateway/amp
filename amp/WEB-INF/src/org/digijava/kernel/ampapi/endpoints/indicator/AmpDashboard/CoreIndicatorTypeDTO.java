package org.digijava.kernel.ampapi.endpoints.indicator.AmpDashboard;

import com.fasterxml.jackson.annotation.JsonProperty;

public class CoreIndicatorTypeDTO {
    private String name;
    @JsonProperty("core-type")
    private String coreType;
    private String unit;
    @JsonProperty("number-divider")
    private double numberDivider;

    public CoreIndicatorTypeDTO() {
    }

    public CoreIndicatorTypeDTO(String unit, String coreType, String name, double numberDivider) {
        this.unit = unit;
        this.coreType = coreType;
        this.name = name;
        this.numberDivider = numberDivider;

    }

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

    public double getNumberDivider() {
        return numberDivider;
    }

    public void setNumberDivider(double numberDivider) {
        this.numberDivider = numberDivider;
    }
}
