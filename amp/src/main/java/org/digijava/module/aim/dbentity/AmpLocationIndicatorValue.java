package org.digijava.module.aim.dbentity;

import java.io.Serializable;

public class AmpLocationIndicatorValue implements Serializable{
    
    private AmpCategoryValueLocations location;
    private AmpIndicatorLayer indicator;
    private Double value;
    private Long id;
    
    public AmpCategoryValueLocations getLocation() {
        return location;
    }
    public void setLocation(AmpCategoryValueLocations location) {
        this.location = location;
    }
    public AmpIndicatorLayer getIndicator() {
        return indicator;
    }
    public void setIndicator(AmpIndicatorLayer indicator) {
        this.indicator = indicator;
    }
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
    

}
