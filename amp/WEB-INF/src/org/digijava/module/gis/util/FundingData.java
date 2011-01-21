package org.digijava.module.gis.util;

import java.math.BigDecimal;
import java.util.List;

/**
 * <p>Title: </p>
 *
 * <p>Description: </p>
 *
 * <p>Copyright: Copyright (c) 2008</p>
 *
 * <p>Company: </p>
 *
 * @author not attributable
 * @version 1.0
 */
public class FundingData {

    private BigDecimal commitment;
    private BigDecimal disbursement;
    private BigDecimal expenditure;

    List activityLocationFundingList;



    public FundingData() {
        this.commitment = new BigDecimal(0);
        this.disbursement = new BigDecimal(0);
        this.expenditure = new BigDecimal(0);

    }

    public FundingData(BigDecimal commitment, BigDecimal disbursement, BigDecimal expenditure) {
        this.commitment = commitment;
        this.disbursement = disbursement;
        this.expenditure = expenditure;
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

    public List getActivityLocationFundingList() {
        return activityLocationFundingList;
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

    public void setActivityLocationFundingList(List activityLocationFundingList) {
        this.activityLocationFundingList = activityLocationFundingList;
    }

}
