package org.digijava.kernel.ampapi.endpoints.activity;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

/**
 * @author Octavian Ciubotaru
 */
public class IndicatorExtraInfo {

    private final String code;

    @JsonProperty("sector-ids")
    private final List<Long> sectorIds;



    @JsonProperty("program-id")
    private final Long programId;

    public IndicatorExtraInfo(String code, List<Long> sectorIds, Long programId) {
        this.code = code;
        this.sectorIds = sectorIds;
        this.programId = programId;
    }

    public String getCode() {
        return code;
    }
    public Long getProgramId() {
        return programId;
    }

    public List<Long> getSectorIds() {
        return sectorIds;
    }
}
