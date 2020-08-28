package org.digijava.kernel.ampapi.endpoints.activity;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * @author Octavian Ciubotaru
 */
public class IndicatorRiskRatingExtraInfo {

    @JsonProperty("rating-value")
    private final int ratingValue;

    private final int index;

    public IndicatorRiskRatingExtraInfo(int ratingValue, int index) {
        this.ratingValue = ratingValue;
        this.index = index;
    }

    public int getRatingValue() {
        return ratingValue;
    }

    public int getIndex() {
        return index;
    }
}
