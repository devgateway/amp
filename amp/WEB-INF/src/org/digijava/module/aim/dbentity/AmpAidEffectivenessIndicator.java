package org.digijava.module.aim.dbentity;

import java.io.Serializable;
import java.util.List;

/**
 * The Amp Aid Effectiveness Indicator domain class
 */
public class AmpAidEffectivenessIndicator implements Serializable {

    private Long ampIndicatorId;
    private String ampIndicatorName;
    private String tooltipText;

    // default is true
    private boolean active;

    // deafult is true
    private boolean mandatory;

    // 0 - selectbox list, 1 - dropdown list
    private int indicatorType;

    private List<AmpAidEffectivenessIndicatorOption> options;



    public Long getAmpIndicatorId() {
        return ampIndicatorId;
    }

    public void setAmpIndicatorId(Long ampIndicatorId) {
        this.ampIndicatorId = ampIndicatorId == 0 ? null : ampIndicatorId;
    }

    public String getAmpIndicatorName() {
        return ampIndicatorName;
    }

    public void setAmpIndicatorName(String ampIndicatorName) {
        this.ampIndicatorName = ampIndicatorName;
    }

    public String getTooltipText() {
        return tooltipText;
    }

    public void setTooltipText(String tooltipText) {
        this.tooltipText = tooltipText;
    }

    public boolean getActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public boolean getMandatory() {
        return mandatory;
    }

    public void setMandatory(boolean mandatory) {
        this.mandatory = mandatory;
    }

    public int getIndicatorType() {
        return indicatorType;
    }

    public void setIndicatorType(int indicatorType) {
        this.indicatorType = indicatorType;
    }

    public List<AmpAidEffectivenessIndicatorOption> getOptions() {
        return options;
    }

    public void setOptions(List<AmpAidEffectivenessIndicatorOption> options) {
        this.options = options;
    }


}
