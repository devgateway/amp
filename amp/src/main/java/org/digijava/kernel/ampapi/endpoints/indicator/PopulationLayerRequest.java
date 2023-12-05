package org.digijava.kernel.ampapi.endpoints.indicator;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModelProperty;

import java.util.List;

public class PopulationLayerRequest {
    
    @JsonProperty("layersIds")
    @ApiModelProperty("The list of indicators ids to be designated as population layers")
    private List<Long> layersIds;
    
    public List<Long> getLayersIds() {
        return layersIds;
    }
    
    public void setLayersIds(List<Long> layersIds) {
        this.layersIds = layersIds;
    }
}
