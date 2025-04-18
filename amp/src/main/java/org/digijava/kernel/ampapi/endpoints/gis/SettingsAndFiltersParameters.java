package org.digijava.kernel.ampapi.endpoints.gis;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModelProperty;
import org.digijava.kernel.ampapi.endpoints.common.EPConstants;

import java.util.Map;

/**
 * @author Octavian Ciubotaru
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class SettingsAndFiltersParameters {

    @JsonProperty(EPConstants.SETTINGS)
    @ApiModelProperty(dataType = "org.digijava.kernel.ampapi.swagger.types.SettingsPH")
    private Map<String, Object> settings;

    @JsonProperty(EPConstants.FILTERS)
    @ApiModelProperty(dataType = "org.digijava.kernel.ampapi.swagger.types.FiltersPH")
    private Map<String, Object> filters;

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
