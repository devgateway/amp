package org.digijava.kernel.ampapi.endpoints.gis.services;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.digijava.kernel.ampapi.endpoints.util.GisConstants;

/**
 * @author Octavian Ciubotaru
 */
public enum AdmLevel {
    @JsonProperty(GisConstants.ADM0) COUNTRY(GisConstants.ADM0),
    @JsonProperty(GisConstants.ADM1) REGION(GisConstants.ADM1),
    @JsonProperty(GisConstants.ADM2) ZONE(GisConstants.ADM2),
    @JsonProperty(GisConstants.ADM3) DISTRICT(GisConstants.ADM3),
    @JsonProperty(GisConstants.ADM4) ADM4(GisConstants.ADM4);

    private String label;

    AdmLevel(String label) {
        this.label = label;
    }

    public String getLabel() {
        return label;
    }

    public static AdmLevel fromString(String label) {
        for (AdmLevel level : values()) {
            if (level.label.equals(label)) {
                return level;
            }
        }
        throw new IllegalArgumentException("Unsupported adm level: " + label);
    }
}
