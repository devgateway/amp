package org.digijava.kernel.ampapi.endpoints.gis;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.digijava.kernel.ampapi.endpoints.indicator.IndicatorEPConstants;
import org.digijava.kernel.ampapi.endpoints.indicator.SaveIndicatorRequest;

/**
 * @author Octavian Ciubotaru
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class RuntimeIndicatorGapAnalysisParameters extends PerformanceFilterParameters {

    @JsonProperty(IndicatorEPConstants.INDICATOR)
    private SaveIndicatorRequest indicator;

    public SaveIndicatorRequest getIndicator() {
        return indicator;
    }

    public void setIndicator(SaveIndicatorRequest indicator) {
        this.indicator = indicator;
    }
}
