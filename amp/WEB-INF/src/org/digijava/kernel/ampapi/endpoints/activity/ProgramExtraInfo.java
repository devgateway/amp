package org.digijava.kernel.ampapi.endpoints.activity;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * @author Viorel Chihai
 */
public class ProgramExtraInfo implements ParentExtraInfo {

    @JsonProperty("parent-program-id")
    private final Long parentProgramId;

    public ProgramExtraInfo(Long parentProgramId) {
        this.parentProgramId = parentProgramId;
    }

    public Long getParentProgramId() {
        return parentProgramId;
    }

    @Override
    public Long getParentId() {
        return parentProgramId;
    }
    
}
