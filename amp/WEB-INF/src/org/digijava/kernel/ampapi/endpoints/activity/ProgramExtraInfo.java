package org.digijava.kernel.ampapi.endpoints.activity;

import org.codehaus.jackson.annotate.JsonProperty;

/**
 * @author Viorel Chihai
 */
public class ProgramExtraInfo implements ParentExtraInfo {

    @JsonProperty("parent-program-id")
    private final Long parentProgramId;

    public ProgramExtraInfo(Long parentSectorId) {
        this.parentProgramId = parentSectorId;
    }

    public Long getParentProgramId() {
        return parentProgramId;
    }

    @Override
    public Long getParentId() {
        return parentProgramId;
    }
    
}
