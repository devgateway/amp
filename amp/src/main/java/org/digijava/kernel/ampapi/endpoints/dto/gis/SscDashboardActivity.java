package org.digijava.kernel.ampapi.endpoints.dto.gis;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class SscDashboardActivity implements SscDashboardObject {

    @JsonProperty(SSCDashboardConstants.ID)
    private Long activityId;

    private Integer year;

    public SscDashboardActivity(Long activityId) {
        this.activityId = activityId;
    }

    @Override
    public List<SscDashboardObject> getChildren() {
        return null;
    }

    public Integer getYear() {
        return year;
    }

    public void setYear(Integer year) {
        this.year = year;
    }
}
