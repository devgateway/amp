package org.digijava.module.aim.dbentity;

import java.io.Serializable;

public class AmpIndicatorLocation implements Serializable {

    Long ampIndLocId;
    AmpIndicator indicator;

    public AmpIndicatorLocation() {
    }

    public Long getAmpIndLocId() {
        return ampIndLocId;
    }

    public void setAmpIndLocId(Long ampIndLocId) {
        this.ampIndLocId = ampIndLocId;
    }

    public AmpIndicator getIndicator() {
        return indicator;
    }

    public void setIndicator(AmpIndicator indicator) {
        this.indicator = indicator;
    }
}
