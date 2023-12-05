package org.digijava.module.aim.helper;

import org.digijava.module.aim.dbentity.AmpIndicator;

import java.util.List;

public class IndicatorThemeBean {
    
    private Long indicatorThemeId;
    private AmpIndicator indicator;
    private List<AmpPrgIndicatorValue> programIndicatorValues;
    
    
    public List<AmpPrgIndicatorValue> getProgramIndicatorValues() {
        return programIndicatorValues;
    }
    public void setProgramIndicatorValues(
            List<AmpPrgIndicatorValue> programIndicatorValues) {
        this.programIndicatorValues = programIndicatorValues;
    }
    public Long getIndicatorThemeId() {
        return indicatorThemeId;
    }
    public void setIndicatorThemeId(Long indicatorThemeId) {
        this.indicatorThemeId = indicatorThemeId;
    }
    public AmpIndicator getIndicator() {
        return indicator;
    }
    public void setIndicator(AmpIndicator indicator) {
        this.indicator = indicator;
    }
    
    
}
