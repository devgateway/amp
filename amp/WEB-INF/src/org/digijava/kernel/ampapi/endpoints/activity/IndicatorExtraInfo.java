package org.digijava.kernel.ampapi.endpoints.activity;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;
import java.util.Set;

/**
 * @author Octavian Ciubotaru
 */
public class IndicatorExtraInfo {

    private final String code;

    @JsonProperty("sector-ids")
    private final List<Long> sectorIds;


    public Set<Long> getProgramIds() {
        return programIds;
    }

    @JsonProperty("program-ids")
    private final Set<Long> programIds;

    public IndicatorExtraInfo(String code, List<Long> sectorIds, Set<Long> programIds) {
        this.code = code;
        this.sectorIds = sectorIds;
        this.programIds = programIds;
    }

    public String getCode() {
        return code;
    }


    public List<Long> getSectorIds() {
        return sectorIds;
    }
}
