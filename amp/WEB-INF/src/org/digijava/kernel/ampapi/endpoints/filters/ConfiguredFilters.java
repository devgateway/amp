package org.digijava.kernel.ampapi.endpoints.filters;

import java.util.List;

import org.codehaus.jackson.annotate.JsonProperty;

/**
 * @author Octavian Ciubotaru
 */
public class ConfiguredFilters {

    private static final String DONOR_GROUPS = "donor-groups";

    @JsonProperty(DONOR_GROUPS)
    private List<Long> donorGroup;

    private DateRange date;

    static class DateRange {

        private String start;

        private String end;

        public String getStart() {
            return start;
        }

        public void setStart(String start) {
            this.start = start;
        }

        public String getEnd() {
            return end;
        }

        public void setEnd(String end) {
            this.end = end;
        }
    }

}
