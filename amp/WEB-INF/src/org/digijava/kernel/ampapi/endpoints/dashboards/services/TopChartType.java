package org.digijava.kernel.ampapi.endpoints.dashboards.services;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * @author Octavian Ciubotaru
 */
public enum TopChartType {
    @JsonProperty("do") DO,
    @JsonProperty("ro") RO,
    @JsonProperty("ba") BA,
    @JsonProperty("ia") IA,
    @JsonProperty("ea") EA,
    @JsonProperty("re") RE,
    @JsonProperty("ps") PS,
    @JsonProperty("dg") DG,
    @JsonProperty("ndd") NDD;

    public static TopChartType fromString(String value) {
        value = value.toUpperCase();
        for (TopChartType type : values()) {
            if (type.name().equals(value)) {
                return type;
            }
        }
        throw new IllegalArgumentException("Unknown TopChartType: " + value);
    }
}
