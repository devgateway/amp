package org.digijava.kernel.ampapi.endpoints.reports.saiku;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.swagger.annotations.ApiModelProperty;

import java.util.List;

/**
 * @author Octavian Ciubotaru
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class QuerySettings {

    @ApiModelProperty("optional, list of additional information to include "
            + "[\"stats\", \"warnings\", \"generatedHeaders\"], default is [] (none)")
    private List<String> info;

    public List<String> getInfo() {
        return info;
    }

    public void setInfo(List<String> info) {
        this.info = info;
    }
}
