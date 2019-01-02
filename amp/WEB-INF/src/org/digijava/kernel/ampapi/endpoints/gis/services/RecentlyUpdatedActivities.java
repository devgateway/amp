package org.digijava.kernel.ampapi.endpoints.gis.services;

import java.util.List;
import java.util.Map;

import io.swagger.annotations.ApiModelProperty;

/**
 * @author Octavian Ciubotaru
 */
public class RecentlyUpdatedActivities {

    @ApiModelProperty("key = column name, value = localized column name")
    private List<Map<String, String>> headers;

    @ApiModelProperty("key = column name, value = column value")
    private List<Map<String, String>> activities;

    public RecentlyUpdatedActivities(List<Map<String, String>> headers, List<Map<String, String>> activities) {
        this.headers = headers;
        this.activities = activities;
    }

    public List<Map<String, String>> getHeaders() {
        return headers;
    }

    public List<Map<String, String>> getActivities() {
        return activities;
    }
}
