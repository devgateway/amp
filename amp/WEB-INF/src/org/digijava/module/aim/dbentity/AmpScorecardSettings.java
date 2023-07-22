package org.digijava.module.aim.dbentity;

import org.hibernate.annotations.CacheConcurrencyStrategy;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "AMP_SCORECARD_SETTINGS")
@Cacheable
@org.hibernate.annotations.Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class AmpScorecardSettings implements Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = -2696192720129725674L;
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "amp_scorecard_settings_seq")
    @SequenceGenerator(name = "amp_scorecard_settings_seq", sequenceName = "AMP_SCORECARD_SETTINGS_seq", allocationSize = 1)
    @Column(name = "id")
    private Long ampScorecardSettingsId;

    @Column(name = "validation_period")
    private Boolean validationPeriod;

    @Column(name = "validation_time")
    private Integer validationTime;

    @Column(name = "percentage_threshold")
    private Double percentageThreshold;

    @Column(name = "quarters")
    private String quarters;

    @OneToMany(mappedBy = "ampScorecardSettings", cascade = {CascadeType.ALL}, orphanRemoval = true)
    private Set<AmpScorecardSettingsCategoryValue> closedStatuses = new HashSet<>();


    public Long getAmpScorecardSettingsId() {
        return ampScorecardSettingsId;
    }

    public void setAmpScorecardSettingsId(Long ampScorecardSettingsId) {
        this.ampScorecardSettingsId = ampScorecardSettingsId;
    }

    public Boolean getValidationPeriod() {
        return validationPeriod;
    }

    public void setValidationPeriod(Boolean validationPeriod) {
        this.validationPeriod = validationPeriod;
    }

    public Integer getValidationTime() {
        return validationTime;
    }

    public void setValidationTime(Integer validationTime) {
        this.validationTime = validationTime;
    }

    public Set<AmpScorecardSettingsCategoryValue> getClosedStatuses() {
        return closedStatuses;
    }

    public void setClosedStatuses(Set<AmpScorecardSettingsCategoryValue> closedStatuses) {
        this.closedStatuses = closedStatuses;
    }

    public Double getPercentageThreshold() {
        return percentageThreshold;
    }

    public void setPercentageThreshold(Double percentageThreshold) {
        this.percentageThreshold = percentageThreshold;
    }

    public String getQuarters() {
        return quarters;
    }

    public void setQuarters(String quarters) {
        this.quarters = quarters;
    }

    public List<String> getQuartersAsList() {
        return quarters == null ? new ArrayList<>() : Arrays.asList(quarters.split(","));
    }
}
