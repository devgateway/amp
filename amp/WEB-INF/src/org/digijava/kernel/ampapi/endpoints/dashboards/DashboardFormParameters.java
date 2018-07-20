package org.digijava.kernel.ampapi.endpoints.dashboards;

import java.util.Map;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModelProperty;
import org.digijava.kernel.ampapi.endpoints.common.EPConstants;

/**
 * @author Octavian Ciubotaru
 */
public class DashboardFormParameters {

    private Integer offset;

    @JsonProperty(EPConstants.SETTINGS)
    @ApiModelProperty(dataType = "org.digijava.kernel.ampapi.swagger.types.SettingsPH")
    private Map<String, Object> settings;

    @JsonProperty(EPConstants.FILTERS)
    @ApiModelProperty(dataType = "org.digijava.kernel.ampapi.swagger.types.FiltersPH")
    private Map<String, Object> filters;

    public Integer getOffset() {
        return offset;
    }

    public void setOffset(Integer offset) {
        this.offset = offset;
    }

    public Map<String, Object> getSettings() {
        return settings;
    }

    public void setSettings(Map<String, Object> settings) {
        this.settings = settings;
    }

    public Map<String, Object> getFilters() {
        return filters;
    }

    public void setFilters(Map<String, Object> filters) {
        this.filters = filters;
    }
}
