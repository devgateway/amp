package org.digijava.kernel.ampapi.endpoints.dashboards.services;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonPropertyOrder({ "base", "actual", "target" })
public class ProgressReportDTO {
    @JsonProperty("base")
    private final ProgressValue base;

    @JsonProperty("actual")
    private final ProgressValue actual;

    @JsonProperty("target")
    private final ProgressValue target;

    public ProgressReportDTO (ProgressValue base, ProgressValue actual, ProgressValue target) {
        this.base = base;
        this.actual = actual;
        this.target = target;
    }

    public ProgressValue getBase () {
        return base;
    }

    public ProgressValue getActual () {
        return actual;
    }

    public ProgressValue getTarget () {
        return target;
    }
}
