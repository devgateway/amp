package org.digijava.kernel.ampapi.endpoints.activity;

import org.codehaus.jackson.annotate.JsonProperty;

/**
 * @author Viorel Chihai
 */
public class SectorExtraInfo implements ParentExtraInfo {

    @JsonProperty("parent-sector-id")
    private final Long parentSectorId;

    public SectorExtraInfo(Long parentSectorId) {
        this.parentSectorId = parentSectorId;
    }

    public Long getParentSectorId() {
        return parentSectorId;
    }

    @Override
    public Long getParentId() {
        return parentSectorId;
    }
    
}
