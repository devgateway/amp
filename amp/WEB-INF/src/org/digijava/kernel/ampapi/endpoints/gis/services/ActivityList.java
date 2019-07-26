package org.digijava.kernel.ampapi.endpoints.gis.services;

import java.util.List;

import io.swagger.annotations.ApiModelProperty;
import org.digijava.kernel.ampapi.endpoints.dto.GisActivity;

/**
 * @author Octavian Ciubotaru
 */
public class ActivityList {

    private Integer count;

    @ApiModelProperty("List of activities")
    private List<GisActivity> activities;

    public ActivityList(Integer count, List<GisActivity> activities) {
        this.count = count;
        this.activities = activities;
    }

    public Integer getCount() {
        return count;
    }

    public List<GisActivity> getActivities() {
        return activities;
    }
}
