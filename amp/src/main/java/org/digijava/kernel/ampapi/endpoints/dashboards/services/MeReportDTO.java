package org.digijava.kernel.ampapi.endpoints.dashboards.services;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import org.digijava.kernel.ampapi.endpoints.indicator.IndicatorYearValues;

@JsonPropertyOrder({"progress", "values"})
public class MeReportDTO {
    @JsonProperty("progress")
    private final int progress;

    @JsonProperty("values")
    private final IndicatorYearValues values;

    public MeReportDTO (int progress, IndicatorYearValues values) {
        this.progress = progress;
        this.values = values;
    }

    public int getProgress () {
        return progress;
    }

    public IndicatorYearValues getValues () {
        return values;
    }
}
