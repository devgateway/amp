package org.digijava.module.aim.dbentity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.io.Serializable;
import java.util.Date;

@JsonIgnoreProperties(ignoreUnknown = true)
public class AmpIndicatorGlobalValue implements Serializable {

    @JsonIgnore
    private Long id;


    @JsonIgnore
    /**
     * The type of indicator, see {@link AmpIndicatorValue constants}
     * TARGET - 0
     * BASE - 2
     */
    private int type;

    private Double originalValue;

    private Date originalValueDate;

    private Double revisedValue;

    private Double revisedValueDate;

    @JsonIgnore
    private AmpIndicator indicator;

    public AmpIndicatorGlobalValue(final int type) {
        this.type = type;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public int getType() {
        return type;
    }

    public void setType(final int type) {
        this.type = type;
    }

    public AmpIndicator getIndicator() {
        return indicator;
    }

    public void setIndicator(AmpIndicator indicator) {
        this.indicator = indicator;
    }

    public Double getOriginalValue() {
        return originalValue;
    }

    public void setOriginalValue(final Double originalValue) {
        this.originalValue = originalValue;
    }

    public Date getOriginalValueDate() {
        return originalValueDate;
    }

    public void setOriginalValueDate(final Date originalValueDate) {
        this.originalValueDate = originalValueDate;
    }

    public Double getRevisedValue() {
        return revisedValue;
    }

    public void setRevisedValue(final Double revisedValue) {
        this.revisedValue = revisedValue;
    }

    public Double getRevisedValueDate() {
        return revisedValueDate;
    }

    public void setRevisedValueDate(final Double revisedValueDate) {
        this.revisedValueDate = revisedValueDate;
    }

    @JsonIgnore
    public boolean isBaseValue() {
        return type == AmpIndicatorValue.BASE;
    }

    @JsonIgnore
    public boolean isTargetValue() {
        return type == AmpIndicatorValue.TARGET;
    }
}
