package org.digijava.module.aim.dbentity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;

public class AmpIndicatorColor implements Serializable{

    private String color;

    @JsonIgnore
    private AmpIndicatorLayer indicatorLayer;

    @JsonIgnore
    private Long indicatorColorId;

    @JsonProperty("order")
    private Long payload;
    
    
    public String getColor() {
        return color;
    }
    public void setColor(String color) {
        this.color = color;
    }
    public AmpIndicatorLayer getIndicatorLayer() {
        return indicatorLayer;
    }
    public void setIndicatorLayer(AmpIndicatorLayer indicatorLayer) {
        this.indicatorLayer = indicatorLayer;
    }
    public Long getIndicatorColorId() {
        return indicatorColorId;
    }
    public void setIndicatorColorId(Long indicatorColorId) {
        this.indicatorColorId = indicatorColorId;
    }
    public Long getPayload() {
        return payload;
    }
    public void setPayload(Long payload) {
        this.payload = payload;
    }
}
