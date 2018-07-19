package org.digijava.kernel.ampapi.endpoints.gis;

import java.util.Map;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModelProperty;
import org.digijava.kernel.ampapi.endpoints.common.EPConstants;
import org.digijava.kernel.ampapi.endpoints.indicator.IndicatorEPConstants;
import org.digijava.kernel.ampapi.endpoints.settings.SettingsConstants;

/**
 * @author Octavian Ciubotaru
 */
public class GisFormParameters {

    @JsonProperty(SettingsConstants.PERFORMANCE_ISSUES)
    private Boolean showActivitiewWithPerformanceIssues;

    @JsonProperty(IndicatorEPConstants.DO_GAP_ANALYSIS)
    private Boolean gapAnalysis;

    // FIXME should be Map, but... error reporting!
    @JsonProperty(IndicatorEPConstants.INDICATOR)
    private Object indicator;

    @JsonProperty(EPConstants.SETTINGS)
    @ApiModelProperty(dataType = "org.digijava.kernel.ampapi.swagger.types.SettingsPH")
    private Map<String, Object> settings;

    @JsonProperty(EPConstants.FILTERS)
    @ApiModelProperty(dataType = "org.digijava.kernel.ampapi.swagger.types.FiltersPH")
    private Map<String, Object> filters;

    public Boolean getShowActivitiewWithPerformanceIssues() {
        return showActivitiewWithPerformanceIssues;
    }

    public void setShowActivitiewWithPerformanceIssues(Boolean showActivitiewWithPerformanceIssues) {
        this.showActivitiewWithPerformanceIssues = showActivitiewWithPerformanceIssues;
    }

    public Boolean getGapAnalysis() {
        return gapAnalysis;
    }

    public void setGapAnalysis(Boolean gapAnalysis) {
        this.gapAnalysis = gapAnalysis;
    }

    public Object getIndicator() {
        return indicator;
    }

    public void setIndicator(Object indicator) {
        this.indicator = indicator;
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
