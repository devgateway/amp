package org.digijava.kernel.ampapi.endpoints.gis;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModelProperty;
import org.digijava.kernel.ampapi.endpoints.settings.SettingsConstants;

/**
 * @author Octavian Ciubotaru
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class PerformanceFilterParameters extends SettingsAndFiltersParameters {

    /**
     * This flag is implemented separately to allow filtering all activities without
     * performance issues until standard filters allow for it.
     */
    @JsonProperty(SettingsConstants.PERFORMANCE_ISSUES)
    @ApiModelProperty("When null all activities included. When true, will include only activities with "
            + "performance issues, otherwise only activities without performance issue.")
    private Boolean showActivitiewWithPerformanceIssues;

    public Boolean getShowActivitiewWithPerformanceIssues() {
        return showActivitiewWithPerformanceIssues;
    }

    public void setShowActivitiewWithPerformanceIssues(Boolean showActivitiewWithPerformanceIssues) {
        this.showActivitiewWithPerformanceIssues = showActivitiewWithPerformanceIssues;
    }

    @Override
    public String toString() {
        return "PerformanceFilterParameters{" +
                "showActivitiewWithPerformanceIssues=" + showActivitiewWithPerformanceIssues +
                '}';
    }
}
