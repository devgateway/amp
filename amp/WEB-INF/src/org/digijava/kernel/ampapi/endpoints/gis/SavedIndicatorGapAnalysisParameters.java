package org.digijava.kernel.ampapi.endpoints.gis;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.digijava.kernel.ampapi.endpoints.indicator.IndicatorEPConstants;

/**
 * @author Octavian Ciubotaru
 */
public class SavedIndicatorGapAnalysisParameters extends PerformanceFilterParameters {

    @JsonProperty(IndicatorEPConstants.DO_GAP_ANALYSIS)
    private Boolean gapAnalysis;

    public Boolean getGapAnalysis() {
        return gapAnalysis;
    }

    public void setGapAnalysis(Boolean gapAnalysis) {
        this.gapAnalysis = gapAnalysis;
    }
}
