package org.digijava.kernel.ampapi.endpoints.indicator;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiParam;

public class LocationIndicatorValueResult {
    
    @JsonProperty(IndicatorEPConstants.VALUE)
    private Double value;
    
    @JsonProperty(IndicatorEPConstants.ID)
    private Long id;
    
    @JsonProperty(IndicatorEPConstants.GEO_CODE_ID)
    private String geoCodeId;

    @JsonProperty(IndicatorEPConstants.NAME)
    @ApiParam(example = "Timor-Leste")
    private String name;
    
    public Double getValue() {
        return value;
    }
    
    public void setValue(Double value) {
        this.value = value;
    }
    
    public Long getId() {
        return id;
    }
    
    public void setId(Long id) {
        this.id = id;
    }
    
    public String getGeoCodeId() {
        return geoCodeId;
    }
    
    public void setGeoCodeId(String geoCodeId) {
        this.geoCodeId = geoCodeId;
    }
    
    public String getName() {
        return name;
    }
    
    public void setName(String name) {
        this.name = name;
    }
}
