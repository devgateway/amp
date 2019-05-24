package org.digijava.kernel.ampapi.endpoints.gis.services;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class BoundaryPoint {
    
    @JsonProperty("lat")
    private Double latCoord;
    
    @JsonProperty("long")
    private Double longCoord;
    
    public Double getLatCoord() {
        return latCoord;
    }
    
    public void setLatCoord(Double latCoord) {
        this.latCoord = latCoord;
    }
    
    public Double getLongCoord() {
        return longCoord;
    }
    
    public void setLongCoord(Double longCoord) {
        this.longCoord = longCoord;
    }
}
