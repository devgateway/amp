package org.digijava.kernel.ampapi.endpoints.activity;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * @author Octavian Ciubotaru
 */
public class IndicatorExtraInfo {

    private final String code;

    @JsonProperty("sector-ids")
    private final List<Long> sectorIds;

    public IndicatorExtraInfo(String code, List<Long> sectorIds) {
        this.code = code;
        this.sectorIds = sectorIds;
    }

    public String getCode() {
        return code;
    }

    public List<Long> getSectorIds() {
        return sectorIds;
    }
}
