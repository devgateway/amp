package org.digijava.kernel.ampapi.endpoints.dashboards;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModelProperty;
import org.digijava.kernel.ampapi.endpoints.gis.SettingsAndFiltersParameters;
import org.digijava.kernel.ampapi.endpoints.util.DashboardConstants;

/**
 * @author Octavian Ciubotaru
 */
public class HeatMapParameters extends SettingsAndFiltersParameters {

    @JsonProperty(DashboardConstants.X_COLUMN)
    @ApiModelProperty(value = "must be OrigName", example = "Primary Sector")
    private String columnXAxis;

    @JsonProperty("xCount")
    @ApiModelProperty(
            value = "Default 25, set -1 to no limit. +1 (\"Others\") will be added if more than that available.",
            example = "25")
    private Integer countXAxis;

    @JsonProperty(DashboardConstants.Y_COLUMN)
    @ApiModelProperty(value = "must be OrigName", example = "Donor Group")
    private String columnYAxis;

    @JsonProperty("yCount")
    @ApiModelProperty(
            value = "Default 10, set -1 to no limit. +1 (\"Others\") will be added if more than that available.",
            example = "10")
    private Integer countYAxis;

    public String getColumnXAxis() {
        return columnXAxis;
    }

    public void setColumnXAxis(String columnXAxis) {
        this.columnXAxis = columnXAxis;
    }

    public Integer getCountXAxis() {
        return countXAxis;
    }

    public void setCountXAxis(Integer countXAxis) {
        this.countXAxis = countXAxis;
    }

    public String getColumnYAxis() {
        return columnYAxis;
    }

    public void setColumnYAxis(String columnYAxis) {
        this.columnYAxis = columnYAxis;
    }

    public Integer getCountYAxis() {
        return countYAxis;
    }

    public void setCountYAxis(Integer countYAxis) {
        this.countYAxis = countYAxis;
    }
}
