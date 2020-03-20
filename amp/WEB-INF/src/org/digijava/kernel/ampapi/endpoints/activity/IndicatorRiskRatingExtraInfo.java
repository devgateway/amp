package org.digijava.kernel.ampapi.endpoints.activity;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * @author Octavian Ciubotaru
 */
public class IndicatorRiskRatingExtraInfo {

    @JsonProperty("rating-value")
    private final int ratingValue;

    public IndicatorRiskRatingExtraInfo(int ratingValue) {
        this.ratingValue = ratingValue;
    }

    public int getRatingValue() {
        return ratingValue;
    }
}
