package org.digijava.module.gis.util;

import java.math.BigDecimal;
import java.util.Set;

import org.digijava.module.aim.dbentity.AmpActivity;
import org.digijava.module.aim.dbentity.AmpActivityVersion;

public class ActivityLocationFunding {

    public ActivityLocationFunding() {

    }

    public ActivityLocationFunding(BigDecimal commitment,
                                   BigDecimal disbursement,
                                   BigDecimal expenditure)
    {
        this.commitment = commitment;
        this.disbursement = disbursement;
        this.expenditure = expenditure;

    }

    private Long activityId;
    private String activityName;

    private String fmtCommitment;
    private String fmtDisbursement;
    private String fmtExpenditure;


    private BigDecimal commitment;
    private BigDecimal disbursement;
    private BigDecimal expenditure;


    private Set donorOrgs;
    private Set topSectors;

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
        return fmtCommitment;
    }

    public void setFmtCommitment(String fmtCommitment) {
        this.fmtCommitment = fmtCommitment;
    }

    public String getFmtDisbursement() {
        return fmtDisbursement;
    }

    public void setFmtDisbursement(String fmtDisbursement) {
        this.fmtDisbursement = fmtDisbursement;
    }

    public String getFmtExpenditure() {
        return fmtExpenditure;
    }

    public void setFmtExpenditure(String fmtExpenditure) {
        this.fmtExpenditure = fmtExpenditure;
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
}
