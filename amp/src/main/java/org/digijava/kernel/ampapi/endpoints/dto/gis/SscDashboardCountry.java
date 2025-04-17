package org.digijava.kernel.ampapi.endpoints.dto.gis;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.dgfoundation.amp.ar.ColumnConstants;

import java.util.ArrayList;
import java.util.List;

public class SscDashboardCountry implements SscDashboardObject {
    @JsonProperty(SSCDashboardConstants.ID)
    private Long countryId;

    @JsonProperty(ColumnConstants.PRIMARY_SECTOR)
    private List<SscDashboardObject> children;

    public SscDashboardCountry(Long countryId) {
        this.countryId = countryId;
        this.children = new ArrayList<>();
    }

    public Long getCountryId() {
        return countryId;
    }

    public List<SscDashboardObject> getChildren() {
        return children;
    }
}
