package org.digijava.module.aim.dbentity;

import java.io.Serializable;

import org.digijava.module.categorymanager.dbentity.AmpCategoryValue;

public class AmpScorecardSettingsCategoryValue implements Serializable {
    /**
     * 
     */
    private static final long serialVersionUID = -7218991247516489079L;

    private Long ampScorecardSettingsCategoryValueId;
    private AmpScorecardSettings ampScorecardSettings;
    private AmpCategoryValue ampCategoryValueStatus;

    public Long getAmpScorecardSettingsCategoryValueId() {
        return ampScorecardSettingsCategoryValueId;
    }

    public void setAmpScorecardSettingsCategoryValueId(Long ampScorecardSettingsCategoryValueId) {
        this.ampScorecardSettingsCategoryValueId = ampScorecardSettingsCategoryValueId;
    }

    public AmpScorecardSettings getAmpScorecardSettings() {
        return ampScorecardSettings;
    }

    public void setAmpScorecardSettings(AmpScorecardSettings ampScorecardSettings) {
        this.ampScorecardSettings = ampScorecardSettings;
    }

    public AmpCategoryValue getAmpCategoryValueStatus() {
        return ampCategoryValueStatus;
    }

    public void setAmpCategoryValueStatus(AmpCategoryValue ampCategoryValueStatus) {
        this.ampCategoryValueStatus = ampCategoryValueStatus;
    }

}
