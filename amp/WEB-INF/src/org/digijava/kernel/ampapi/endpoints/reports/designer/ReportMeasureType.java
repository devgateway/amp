/**
 * 
 */
package org.digijava.kernel.ampapi.endpoints.reports.designer;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Report measure type
 * 
 * @author Viorel Chihai
 */
public enum ReportMeasureType {
    @JsonProperty("A")
    ALL("A"),

    @JsonProperty("D")
    DERIVED("D"),

    @JsonProperty("P")
    PLEDGE("P");

    private String value;

    ReportMeasureType(final String value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return this.value;
    }

    public static ReportMeasureType fromString(final String value) {
        String val = value.toUpperCase();
        for (ReportMeasureType profile : values()) {
            if (profile.value.equals(val)) {
                return profile;
            }
        }

        throw new IllegalArgumentException("Unknown ReportMeasureType: " + value);
    }

}
