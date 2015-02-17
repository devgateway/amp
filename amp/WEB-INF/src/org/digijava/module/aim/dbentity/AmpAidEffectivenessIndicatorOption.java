package org.digijava.module.aim.dbentity;

import java.io.Serializable;

/**
 * The amp aid effectiveness indicator option domain class
 * An option could be anything, not just predefined
 */
public class AmpAidEffectivenessIndicatorOption implements Serializable {

    private Long ampIndicatorOptionId;
    private String ampIndicatorOptionName;
    private Boolean defaultOption;
    private AmpAidEffectivenessIndicator indicator;

    public AmpAidEffectivenessIndicator getIndicator() {
        return indicator;
    }

    public void setIndicator(AmpAidEffectivenessIndicator indicator) {
        this.indicator = indicator;
    }

    public Long getAmpIndicatorOptionId() {
        return ampIndicatorOptionId;
    }

    public void setAmpIndicatorOptionId(Long ampIndicatorOptionId) {
        this.ampIndicatorOptionId = ampIndicatorOptionId == 0 ? null : ampIndicatorOptionId;
    }

    public String getAmpIndicatorOptionName() {
        return ampIndicatorOptionName;
    }

    public void setAmpIndicatorOptionName(String ampIndicatorOptionName) {
        this.ampIndicatorOptionName = ampIndicatorOptionName;
    }

    public Boolean getDefaultOption() {
        return defaultOption == null ? false : defaultOption;
    }

    public void setDefaultOption(Boolean defaultOption) {
        this.defaultOption = defaultOption;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof AmpAidEffectivenessIndicatorOption)) return false;

        AmpAidEffectivenessIndicatorOption that = (AmpAidEffectivenessIndicatorOption) o;

        return !(ampIndicatorOptionId != null ? !ampIndicatorOptionId.equals(that.ampIndicatorOptionId) : that.ampIndicatorOptionId != null);
    }

    @Override
    public int hashCode() {
        return ampIndicatorOptionId != null ? ampIndicatorOptionId.hashCode() : 0;
    }
}
