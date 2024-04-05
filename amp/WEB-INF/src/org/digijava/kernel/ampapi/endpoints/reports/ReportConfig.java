package org.digijava.kernel.ampapi.endpoints.reports;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModelProperty;

import java.util.Map;

/**
 * @author Octavian Ciubotaru
 */
public class ReportConfig {

    @JsonProperty("layers-view")
    private String layersView;

    @ApiModelProperty(dataType = "org.digijava.kernel.ampapi.swagger.types.SettingsPH")
    private Map<String, Object> settings;

    private ReportConfigFilters filters;

    public String getLayersView() {
        return layersView;
    }

    public void setLayersView(String layersView) {
        this.layersView = layersView;
    }

    public ReportConfigFilters getFilters() {
        return filters;
    }

    public void setFilters(ReportConfigFilters filters) {
        this.filters = filters;
    }

    public Map<String, Object> getSettings() {
        return settings;
    }

    public void setSettings(Map<String, Object> settings) {
        this.settings = settings;
    }
}
