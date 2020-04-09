package org.digijava.kernel.ampapi.endpoints.common.fm;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModelProperty;
import org.digijava.kernel.ampapi.endpoints.common.EPConstants;

import java.util.List;

/**
 * @author Viorel Chihai
 */
public class FMMemberSettingsResult {

    @JsonProperty(EPConstants.WS_MEMBER_IDS)
    @ApiModelProperty(value = "list of members", example = "[1, 2, 5]")
    private List<Long> wsMemberIds;

    @JsonProperty(EPConstants.FM_TREE)
    @ApiModelProperty(value = "fm tree")
    private FMSettingsResult fmTree;

    public List<Long> getWsMemberIds() {
        return wsMemberIds;
    }

    public void setWsMemberIds(List<Long> wsMemberIds) {
        this.wsMemberIds = wsMemberIds;
    }

    public FMSettingsResult getFmTree() {
        return fmTree;
    }

    public void setFmTree(FMSettingsResult fmTree) {
        this.fmTree = fmTree;
    }
}
