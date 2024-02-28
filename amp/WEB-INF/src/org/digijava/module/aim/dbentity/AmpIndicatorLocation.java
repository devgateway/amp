package org.digijava.module.aim.dbentity;

import java.io.Serializable;

public class AmpIndicatorLocation implements Serializable {

    Long ampIndLocId;
    AmpIndicator indicator;

    AmpCategoryValueLocations location;

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

    public AmpCategoryValueLocations getLocation() {
        return location;
    }

    public void setLocation(AmpCategoryValueLocations location) {
        this.location = location;
    }
}
