package org.digijava.kernel.ampapi.endpoints.gis;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.digijava.kernel.ampapi.endpoints.indicator.Indicator;
import org.digijava.kernel.ampapi.endpoints.indicator.IndicatorEPConstants;

/**
 * @author Octavian Ciubotaru
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class RuntimeIndicatorGapAnalysisParameters extends PerformanceFilterParameters {

    @JsonProperty(IndicatorEPConstants.INDICATOR)
    private Indicator indicator;

    public Indicator getIndicator() {
        return indicator;
    }

    public void setIndicator(Indicator indicator) {
        this.indicator = indicator;
    }
}
