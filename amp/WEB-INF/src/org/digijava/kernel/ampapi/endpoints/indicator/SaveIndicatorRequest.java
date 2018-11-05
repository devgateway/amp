package org.digijava.kernel.ampapi.endpoints.indicator;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModelProperty;

/**
 * FIXME This class has these responsibilities:
 * - to link indicator to workspaces
 * - update indicator and/or indicator values
 * - used to return number of modified/inserted values
 *
 * @author Octavian Ciubotaru
 */
public class SaveIndicatorRequest extends Indicator {

    public enum Option {
        @JsonProperty("overwrite") OVERWRITE,
        @JsonProperty("new") NEW
    }

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @ApiModelProperty("Color ramp id to use. See `GET /indicator/amp-color`. "
            + "During update this property will be updated only if not null.")
    private Integer colorRampId;

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @ApiModelProperty("The workspaces this indicator is shared with. "
            + "During update this property will be updated only if not null.")
    private List<String> sharedWorkspaces;

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @ApiModelProperty("Controls how indicator values are updated.")
    private Option option = Option.OVERWRITE;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    @ApiModelProperty("Number indicator values imported.")
    private Integer numberOfImportedRecords;

    public Integer getColorRampId() {
        return colorRampId;
    }

    public void setColorRampId(Integer colorRampId) {
        this.colorRampId = colorRampId;
    }

    public List<String> getSharedWorkspaces() {
        return sharedWorkspaces;
    }

    public void setSharedWorkspaces(List<String> sharedWorkspaces) {
        this.sharedWorkspaces = sharedWorkspaces;
    }

    public Option getOption() {
        return option;
    }

    public void setOption(Option option) {
        this.option = option;
    }

    public Integer getNumberOfImportedRecords() {
        return numberOfImportedRecords;
    }

    public void setNumberOfImportedRecords(Integer numberOfImportedRecords) {
        this.numberOfImportedRecords = numberOfImportedRecords;
    }
}
