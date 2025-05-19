/**
 * 
 */
package org.digijava.kernel.ampapi.endpoints.reports.designer;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Report profiles
 * 
 * @author Viorel Chihai
 */
public enum ReportProfile {
    @JsonProperty("R")
    REPORT("R"),

    @JsonProperty("T")
    TAB("T");

    private String value;

    ReportProfile(final String value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return this.value;
    }

    public static ReportProfile fromString(final String value) {
        String val = value.toUpperCase();
        for (ReportProfile profile : values()) {
            if (profile.value.equals(val)) {
                return profile;
            }
        }

        throw new IllegalArgumentException("Unknown ReportProfile: " + value);
    }

    public boolean isReport() {
        return this.equals(REPORT);
    }

    public boolean isTab() {
        return this.equals(TAB);
    }

}
