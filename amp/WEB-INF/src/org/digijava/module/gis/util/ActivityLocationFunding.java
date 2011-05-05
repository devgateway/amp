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
                                   BigDecimal expenditure,
                                   AmpActivityVersion activity)
    {
        this.commitment = commitment;
        this.disbursement = disbursement;
        this.expenditure = expenditure;
        this.activity = activity;
    }

    private String fmtCommitment;
    private String fmtDisbursement;
    private String fmtExpenditure;


    private BigDecimal commitment;
    private BigDecimal disbursement;
    private BigDecimal expenditure;
    private AmpActivityVersion activity;

    private Set donorOrgs;
    private Set topSectors;

    public AmpActivityVersion getActivity() {
        return activity;
    }

    public BigDecimal getCommitment() {
        return commitment;
    }

    public BigDecimal getDisbursement() {
        return disbursement;
    }

    public BigDecimal getExpenditure() {
        return expenditure;
    }

    public String getFmtCommitment() {
        return fmtCommitment;
    }

    public String getFmtDisbursement() {
        return fmtDisbursement;
    }

    public String getFmtExpenditure() {
        return fmtExpenditure;
    }

    public Set getDonorOrgs() {
        return donorOrgs;
    }

    public Set getTopSectors() {
        return topSectors;
    }

    public void setActivity(AmpActivity activity) {
        this.activity = activity;
    }

    public void setCommitment(BigDecimal commitment) {
        this.commitment = commitment;
    }

    public void setDisbursement(BigDecimal disbursement) {
        this.disbursement = disbursement;
    }

    public void setExpenditure(BigDecimal expenditure) {
        this.expenditure = expenditure;
    }

    public void setFmtCommitment(String fmtCommitment) {
        this.fmtCommitment = fmtCommitment;
    }

    public void setFmtDisbursement(String fmtDisbursement) {
        this.fmtDisbursement = fmtDisbursement;
    }

    public void setFmtExpenditure(String fmtExpenditure) {
        this.fmtExpenditure = fmtExpenditure;
    }

    public void setDonorOrgs(Set donorOrgs) {
        this.donorOrgs = donorOrgs;
    }

    public void setTopSectors(Set topSectors) {
        this.topSectors = topSectors;
    }

}
