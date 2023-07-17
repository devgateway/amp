package org.digijava.module.aim.dbentity;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import javax.persistence.*;

@Entity
@Table(name = "AMP_INDICATOR_COLOR")
public class AmpIndicatorColor implements Serializable{
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "AMP_INDICATOR_COLOR_seq")
    @SequenceGenerator(name = "AMP_INDICATOR_COLOR_seq", sequenceName = "AMP_INDICATOR_COLOR_seq", allocationSize = 1)
    @Column(name = "amp_indicator_color_id")
    @JsonIgnore

    private Long indicatorColorId;

    @Column(name = "color")
    private String color;

    @Column(name = "payload")
    @JsonProperty("order")
    private Long payload;

    @ManyToOne
    @JsonIgnore

    @JoinColumn(name = "amp_indicator_layer_id")
    private AmpIndicatorLayer indicatorLayer;


    
    
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
