package org.digijava.kernel.ampapi.endpoints.dto.gis;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;
import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class SscDashboardModality implements SscDashboardObject {

    @JsonProperty(SSCDashboardConstants.ID)
    private Long modalityId;

    @JsonProperty(SSCDashboardConstants.ACTIVITIES)
    private List<SscDashboardObject> children;

    public SscDashboardModality(Long modalityId) {
        this.modalityId = modalityId;
        children = new ArrayList<>();
    }

    @Override
    public List<SscDashboardObject> getChildren() {
        return children;
    }
}
