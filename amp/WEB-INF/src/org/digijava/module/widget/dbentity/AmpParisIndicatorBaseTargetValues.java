

package org.digijava.module.widget.dbentity;

import org.digijava.module.aim.dbentity.AmpAhsurveyIndicator;


public class AmpParisIndicatorBaseTargetValues {
    private Long id;
    private AmpParisIndicatorTableWidget widget;
    private AmpAhsurveyIndicator parisIndicator;
    private String baseValue;
    private String targetValue;
    private Integer index;

    public String getBaseValue() {
        return baseValue;
    }

    public void setBaseValue(String baseValue) {
        this.baseValue = baseValue;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getIndex() {
        return index;
    }

    public void setIndex(Integer index) {
        this.index = index;
    }

    public AmpAhsurveyIndicator getParisIndicator() {
        return parisIndicator;
    }

    public void setParisIndicator(AmpAhsurveyIndicator parisIndicator) {
        this.parisIndicator = parisIndicator;
    }

    public String getTargetValue() {
        return targetValue;
    }

    public void setTargetValue(String targetValue) {
        this.targetValue = targetValue;
    }

    public AmpParisIndicatorTableWidget getWidget() {
        return widget;
    }

    public void setWidget(AmpParisIndicatorTableWidget widget) {
        this.widget = widget;
    }

}
