package org.digijava.kernel.ampapi.endpoints.gis.services;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class BoundaryRectangle {
    
    @JsonProperty("upper-left-corner")
    private BoundaryPoint upperLeftCorner;
    
    @JsonProperty("lower-right-corner")
    private BoundaryPoint lowerRightCorner;
    
    public BoundaryPoint getUpperLeftCorner() {
        return upperLeftCorner;
    }
    
    public void setUpperLeftCorner(BoundaryPoint upperLeftCorner) {
        this.upperLeftCorner = upperLeftCorner;
    }
    
    public BoundaryPoint getLowerRightCorner() {
        return lowerRightCorner;
    }
    
    public void setLowerRightCorner(BoundaryPoint lowerRightCorner) {
        this.lowerRightCorner = lowerRightCorner;
    }
}
