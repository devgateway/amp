/**
 * 
 */
package org.digijava.kernel.ampapi.endpoints.reports.designer;

import com.fasterxml.jackson.annotation.JsonProperty;

import static org.dgfoundation.amp.ar.ArConstants.COMPONENT_TYPE;
import static org.dgfoundation.amp.ar.ArConstants.DONOR_TYPE;
import static org.dgfoundation.amp.ar.ArConstants.GPI_TYPE;
import static org.dgfoundation.amp.ar.ArConstants.PLEDGES_TYPE;
import static org.dgfoundation.amp.ar.ArConstants.REGIONAL_TYPE;

/**
 * Report types
 * 
 * @author Viorel Chihai
 */
public enum ReportType {
    @JsonProperty("C")
    COMPONENT((long) COMPONENT_TYPE, "C"),

    @JsonProperty("D")
    DONOR((long) DONOR_TYPE, "D"),

    @JsonProperty("G")
    GPI((long) GPI_TYPE, "G"),

    @JsonProperty("R")
    REGIONAL((long) REGIONAL_TYPE, "R"),

    @JsonProperty("P")
    PLEDGE((long) PLEDGES_TYPE, "P");

    private Long id;

    private String value;

    ReportType(final Long id, final String value) {
        this.id = id;
        this.value = value;
    }

    public Long getId() {
        return id;
    }

    public String getValue() {
        return value;
    }

    public static ReportType fromString(final String value) {
        String val = value.toUpperCase();
        for (ReportType type : values()) {
            if (type.value.equals(val)) {
                return type;
            }
        }

        throw new IllegalArgumentException("Unknown ReportType: " + value);
    }

    public static ReportType fromLong(final Long id) {
        for (ReportType type : values()) {
            if (type.id.equals(id)) {
                return type;
            }
        }

        throw new IllegalArgumentException("Unknown ReportType with id : " + id);
    }

    public boolean isPledge() {
        return this.equals(PLEDGE);
    }

    public boolean isDonor() {
        return this.equals(DONOR);
    }
}
