/*
 * RegionalFunding.java
 * Created : 01-Sep-2005
 */

package org.digijava.module.aim.helper;

import org.digijava.module.aim.annotations.interchange.Interchangeable;

import java.util.Collection;
import java.util.Comparator;

/**
 * The class represents a regional funding object which contains 
 * data about a region and the funding allocated to that region
 * 
 * @author Priyajith
 */
public class RegionalFunding {
        
    public static class RegionalFundingComparator implements Comparator<RegionalFunding> {

        @Override
        public int compare(RegionalFunding o1, RegionalFunding o2) {
            return o1.getRegionName().compareTo(o2.getRegionName());
        }
    }
    

    /** The database primary key of the region */
    private Long regionId;
    
    /** The region name */
    private String regionName;
    
    /**
     * The collection of funding details object which is of type
     * org.digijava.module.aim.helper.FundingDetail
     */
    @Interchangeable(fieldTitle="Commitments")
    private Collection<FundingDetail> commitments;
    @Interchangeable(fieldTitle="Disbursements")
    private Collection<FundingDetail> disbursements;
    @Interchangeable(fieldTitle="Expenditures")
    private Collection<FundingDetail> expenditures;
    @Interchangeable(fieldTitle="Total Commitments")
    private double totCommitments;
    @Interchangeable(fieldTitle="Total Disbursements")
    private double totDisbursements;
    @Interchangeable(fieldTitle="Total Expenditures")
    private double totExpenditures;
    @Interchangeable(fieldTitle="Total Undisbursed")
    private double totUnDisbursed;
    @Interchangeable(fieldTitle="Total Unexpended")
    private double totUnExpended;
    
    public RegionalFunding() {
        totCommitments = 0;
        totDisbursements = 0;
        totExpenditures = 0;
        totUnDisbursed = 0;
        totUnExpended = 0;
    }

    /**
     * @return Returns the regionId.
     */
    public Long getRegionId() {
        return regionId;
    }

    /**
     * @param regionId The regionId to set.
     */
    public void setRegionId(Long regionId) {
        this.regionId = regionId;
    }

    /**
     * @return Returns the regionName.
     */
    public String getRegionName() {
        return regionName;
    }

    /**
     * @param regionName The regionName to set.
     */
    public void setRegionName(String regionName) {
        this.regionName = regionName;
    }

    /**
     * @return Returns the commitments.
     */
    public Collection<FundingDetail> getCommitments() {
        return commitments;
    }

    /**
     * @param commitments The commitments to set.
     */
    public void setCommitments(Collection<FundingDetail> commitments) {
        this.commitments = commitments;
    }

    /**
     * @return Returns the disbursements.
     */
    public Collection<FundingDetail> getDisbursements() {
        return disbursements;
    }

    /**
     * @param disbursements The disbursements to set.
     */
    public void setDisbursements(Collection<FundingDetail> disbursements) {
        this.disbursements = disbursements;
    }

    /**
     * @return Returns the expenditures.
     */
    public Collection<FundingDetail> getExpenditures() {
        return expenditures;
    }

    /**
     * @param expenditures The expenditures to set.
     */
    public void setExpenditures(Collection<FundingDetail> expenditures) {
        this.expenditures = expenditures;
    }

    /* (non-Javadoc)
     * @see java.lang.Object#equals(java.lang.Object)
     */
    public boolean equals(Object arg) {
        if (arg instanceof RegionalFunding) {
            RegionalFunding regFund = (RegionalFunding) arg;
            return regFund.regionId.equals(regionId);
        } else {
            throw new ClassCastException();         
        }
    }

    /**
     * @return Returns the totCommitments.
     */
    public double getTotCommitments() {
        return totCommitments;
    }

    /**
     * @param totCommitments The totCommitments to set.
     */
    public void setTotCommitments(double totCommitments) {
        this.totCommitments = totCommitments;
    }

    /**
     * @return Returns the totDisbursements.
     */
    public double getTotDisbursements() {
        return totDisbursements;
    }

    /**
     * @param totDisbursements The totDisbursements to set.
     */
    public void setTotDisbursements(double totDisbursements) {
        this.totDisbursements = totDisbursements;
    }

    /**
     * @return Returns the totExpenditures.
     */
    public double getTotExpenditures() {
        return totExpenditures;
    }

    /**
     * @param totExpenditures The totExpenditures to set.
     */
    public void setTotExpenditures(double totExpenditures) {
        this.totExpenditures = totExpenditures;
    }

    /**
     * @return Returns the totUnDisbursed.
     */
    public double getTotUnDisbursed() {
        return totUnDisbursed;
    }

    /**
     * @param totUnDisbursed The totUnDisbursed to set.
     */
    public void setTotUnDisbursed(double totUnDisbursed) {
        this.totUnDisbursed = totUnDisbursed;
    }

    /**
     * @return Returns the totUnExpended.
     */
    public double getTotUnExpended() {
        return totUnExpended;
    }

    /**
     * @param totUnExpended The totUnExpended to set.
     */
    public void setTotUnExpended(double totUnExpended) {
        this.totUnExpended = totUnExpended;
    }
    
    

}
