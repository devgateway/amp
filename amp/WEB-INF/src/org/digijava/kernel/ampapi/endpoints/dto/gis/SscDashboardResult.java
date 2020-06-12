package org.digijava.kernel.ampapi.endpoints.dto.gis;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.dgfoundation.amp.ar.ColumnConstants;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class SscDashboardResult {

    private Set<Long> activitiesId;

    private Integer mostRecentYear;

    @JsonProperty(ColumnConstants.DONOR_COUNTRY)
    private List<SscDashboardObject> children;

    public SscDashboardResult() {
        activitiesId = new HashSet<>();
        children = new ArrayList<>();
        mostRecentYear = 0;
    }

    public Set<Long> getActivitiesId() {
        return activitiesId;
    }

    public List<SscDashboardObject> getChildren() {
        return children;
    }

    public Integer getMostRecentYear() {
        return mostRecentYear;
    }

    public void setMostRecentYear(Integer mostRecentYear) {
        this.mostRecentYear = mostRecentYear;
    }
}
