package org.digijava.kernel.ampapi.endpoints.scorecard.model;

import io.swagger.annotations.ApiModelProperty;

/**
 * @author Viorel Chihai
 */
public class DonorScoreCardStats {

    @ApiModelProperty(value = "the percentage of donors which are on time", example = "60")
    private final int onTime;

    @ApiModelProperty(value = "the percentage of donors which are late", example = "25")
    private final int late;

    @ApiModelProperty(value = "the percentage of donors without updates", example = "15")
    private final int noUpdates;

    public DonorScoreCardStats(int onTime, int late, int noUpdates) {
        this.onTime = onTime;
        this.late = late;
        this.noUpdates = noUpdates;
    }

    public int getOnTime() {
        return onTime;
    }

    public int getLate() {
        return late;
    }

    public int getNoUpdates() {
        return noUpdates;
    }
}
