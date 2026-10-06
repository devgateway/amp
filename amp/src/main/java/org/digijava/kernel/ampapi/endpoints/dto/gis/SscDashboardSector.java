package org.digijava.kernel.ampapi.endpoints.dto.gis;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.dgfoundation.amp.ar.ColumnConstants;

import java.util.ArrayList;
import java.util.List;

public class SscDashboardSector implements SscDashboardObject {

    @JsonProperty(SSCDashboardConstants.ID)
    private Long sectorId;

    @JsonProperty(ColumnConstants.MODALITIES)
    private List<SscDashboardObject> children;

    public SscDashboardSector(Long sectorId) {
        this.sectorId = sectorId;
        children = new ArrayList<>();
    }

    public Long getSectorId() {
        return sectorId;
    }


    @Override
    public List<SscDashboardObject> getChildren() {
        return children;
    }

}
