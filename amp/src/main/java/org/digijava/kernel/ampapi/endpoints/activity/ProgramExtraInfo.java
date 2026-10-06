package org.digijava.kernel.ampapi.endpoints.activity;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Set;

/**
 * @author Viorel Chihai
 */
public class ProgramExtraInfo implements ParentExtraInfo {

    @JsonProperty("parent-program-id")
    private final Long parentProgramId;

    @JsonProperty("mapped-program-id")
    private final Set<Long> mappedPogramId;

    public ProgramExtraInfo(Long parentProgramId, Set<Long> mappedProgramId) {
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

    public Set<Long> getMappedPogramId() {
        return mappedPogramId;
    }
}
