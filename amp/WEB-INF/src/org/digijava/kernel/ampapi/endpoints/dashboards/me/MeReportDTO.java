package org.digijava.kernel.ampapi.endpoints.dashboards.me;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonPropertyOrder({"progress", "values"})
public class MeReportDTO {
    @JsonProperty("progress")
    private final int progress;

    @JsonProperty("values")
    private final IndicatorValues values;

    public MeReportDTO (int progress, IndicatorValues values) {
        this.progress = progress;
        this.values = values;
    }

    public int getProgress () {
        return progress;
    }

    public IndicatorValues getValues () {
        return values;
    }
}
