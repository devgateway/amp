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

    @ApiModelProperty(value = "the percentage of donors without updates", example = "5")
    private final int noUpdates;

    @ApiModelProperty(value = "the percentage of donors that provided update within the validation period",
            example = "10")
    private final int validationPeriod;

    public DonorScoreCardStats(int onTime, int late, int noUpdates, int validationPeriod) {
        this.onTime = onTime;
        this.late = late;
        this.noUpdates = noUpdates;
        this.validationPeriod = validationPeriod;
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

    public int getValidationPeriod() {
        return validationPeriod;
    }
}
