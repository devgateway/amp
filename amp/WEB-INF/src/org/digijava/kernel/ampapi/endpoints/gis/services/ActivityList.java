package org.digijava.kernel.ampapi.endpoints.gis.services;

import java.util.List;

import io.swagger.annotations.ApiModelProperty;
import org.digijava.kernel.ampapi.endpoints.util.JsonBean;

/**
 * @author Octavian Ciubotaru
 */
public class ActivityList {

    private Integer count;

    @ApiModelProperty("key = column name, value = column value"
            + "\n'matchesFilters' property explains why activity was included")
    private List<JsonBean> activities;

    public ActivityList(Integer count, List<JsonBean> activities) {
        this.count = count;
        this.activities = activities;
    }

    public Integer getCount() {
        return count;
    }

    public List<JsonBean> getActivities() {
        return activities;
    }
}
