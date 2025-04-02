package org.digijava.kernel.ampapi.endpoints.dashboards.services;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModelProperty;

/**
 * @author Octavian Ciubotaru
 */
public class AidPredictabilityAmounts {

    @JsonProperty("planned disbursements")
    private AidPredictabilityAmount planned;

    @JsonProperty("actual disbursements")
    private AidPredictabilityAmount actual;

    @ApiModelProperty("not available for totals")
    private String year;

    public AidPredictabilityAmount getPlanned() {
        return planned;
    }

    public void setPlanned(AidPredictabilityAmount planned) {
        this.planned = planned;
    }

    public AidPredictabilityAmount getActual() {
        return actual;
    }

    public void setActual(AidPredictabilityAmount actual) {
        this.actual = actual;
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }
}
