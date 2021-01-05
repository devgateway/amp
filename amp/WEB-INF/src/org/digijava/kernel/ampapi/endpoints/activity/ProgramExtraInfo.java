package org.digijava.kernel.ampapi.endpoints.activity;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * @author Viorel Chihai
 */
public class ProgramExtraInfo implements ParentExtraInfo {

    @JsonProperty("parent-program-id")
    private final Long parentProgramId;

    @JsonProperty("mapped-program-id")
    private final Long mappedPogramId;

    public ProgramExtraInfo(Long parentProgramId, Long mappedProgramId) {
        this.parentProgramId = parentProgramId;
        this.mappedPogramId = mappedProgramId;
    }

    public Long getParentProgramId() {
        return parentProgramId;
    }

    @Override
    public Long getParentId() {
        return parentProgramId;
    }

    public Long getMappedPogramId() {
        return mappedPogramId;
    }
}
