package org.digijava.kernel.ampapi.endpoints.geocoding;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModelProperty;

/**
 * @author Octavian Ciubotaru
 */
public class ChangeLocationStatusRequest {

    @ApiModelProperty("Activity Id")
    @JsonProperty("amp_activity_id")
    private Long ampActivityId;

    @ApiModelProperty("Location Id")
    @JsonProperty("amp_category_value_location_id")
    private Long acvlId;

    @ApiModelProperty("Location status. True if location was accepted, false if rejected, null if unknown.")
    private Boolean accepted;

    public Long getAmpActivityId() {
        return ampActivityId;
    }

    public void setAmpActivityId(Long ampActivityId) {
        this.ampActivityId = ampActivityId;
    }

    public Long getAcvlId() {
        return acvlId;
    }

    public void setAcvlId(Long acvlId) {
        this.acvlId = acvlId;
    }

    public Boolean getAccepted() {
        return accepted;
    }

    public void setAccepted(Boolean accepted) {
        this.accepted = accepted;
    }
}
