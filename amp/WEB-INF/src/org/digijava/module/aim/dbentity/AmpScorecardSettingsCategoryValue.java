package org.digijava.module.aim.dbentity;

import java.io.Serializable;

import org.digijava.module.categorymanager.dbentity.AmpCategoryValue;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;

@Entity
@Table(name = "AMP_SCORECARD_SETTINGS_CATEGORY_VALUE")
@Cacheable
@org.hibernate.annotations.Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class AmpScorecardSettingsCategoryValue implements Serializable {
    /**
     * 
     */
    private static final long serialVersionUID = -7218991247516489079L;
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "AMP_SCORECARD_SETTINGS_CATEGORY_VALUE_SEQ")
    @SequenceGenerator(name = "AMP_SCORECARD_SETTINGS_CATEGORY_VALUE_SEQ", sequenceName = "AMP_SCORECARD_SETTINGS_CATEGORY_VALUE_SEQ", allocationSize = 1)
    @Column(name = "id")
    private Long ampScorecardSettingsCategoryValueId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "AMP_SCORECARD_SETTINGS_ID", nullable = false)
    private AmpScorecardSettings ampScorecardSettings;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "AMP_CATEGORY_VALUE_ID", nullable = false)
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
