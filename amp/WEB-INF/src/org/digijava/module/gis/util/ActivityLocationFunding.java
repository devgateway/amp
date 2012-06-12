package org.digijava.module.gis.util;

import java.math.BigDecimal;
import java.util.Set;

import org.digijava.module.aim.dbentity.AmpActivity;
import org.digijava.module.aim.dbentity.AmpActivityVersion;
import org.digijava.module.aim.helper.FormatHelper;

public class ActivityLocationFunding {

    public ActivityLocationFunding() {

    }

    public ActivityLocationFunding(BigDecimal commitment,
                                   BigDecimal disbursement,
                                   BigDecimal expenditure,
                                   BigDecimal plannedDisbursement)
    {
        this.commitment = commitment;
        this.disbursement = disbursement;
        this.expenditure = expenditure;
        this.plannedDisbursement = plannedDisbursement;

    }

    private Long activityId;
    private String activityName;


    private BigDecimal commitment;
    private BigDecimal disbursement;
    private BigDecimal expenditure;
    private BigDecimal plannedDisbursement;


    private Set donorOrgs;
    private Set topSectors;
    private Set locations;

    public Set getLocations() {
        return locations;
    }

    public void setLocations(Set locations) {
        this.locations = locations;
    }

    public Long getActivityId() {
        return activityId;
    }

    public void setActivityId(Long activityId) {
        this.activityId = activityId;
    }

    public String getActivityName() {
        return activityName;
    }

    public void setActivityName(String activityName) {
        this.activityName = activityName;
    }

    public String getFmtCommitment() {
        return commitment != null ? FormatHelper.formatNumber(commitment.doubleValue()):null;
    }

    public String getFmtDisbursement() {
        return disbursement != null ? FormatHelper.formatNumber(disbursement.doubleValue()):null;
    }
    public String getFmtExpenditure() {
        return expenditure != null ? FormatHelper.formatNumber(expenditure.doubleValue()):null;
    }

    public BigDecimal getCommitment() {
        return commitment;
    }

    public void setCommitment(BigDecimal commitment) {
        this.commitment = commitment;
    }

    public BigDecimal getDisbursement() {
        return disbursement;
    }

    public void setDisbursement(BigDecimal disbursement) {
        this.disbursement = disbursement;
    }

    public BigDecimal getExpenditure() {
        return expenditure;
    }

    public void setExpenditure(BigDecimal expenditure) {
        this.expenditure = expenditure;
    }

    public Set getDonorOrgs() {
        return donorOrgs;
    }

    public void setDonorOrgs(Set donorOrgs) {
        this.donorOrgs = donorOrgs;
    }

    public Set getTopSectors() {
        return topSectors;
    }

    public void setTopSectors(Set topSectors) {
        this.topSectors = topSectors;
    }

    public BigDecimal getPlannedDisbursement() {
        return plannedDisbursement;
    }

    public void setPlannedDisbursement(BigDecimal plannedDisbursement) {
        this.plannedDisbursement = plannedDisbursement;
    }
}
