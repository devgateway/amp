package org.digijava.module.gis.util;

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

    private Double commitment;
    private Double disbursement;
    private Double expenditure;



    public FundingData() {
        this.commitment = new Double(0);
        this.disbursement = new Double(0);
        this.expenditure = new Double(0);

    }

    public FundingData(Double commitment, Double disbursement, Double expenditure) {
        this.commitment = commitment;
        this.disbursement = disbursement;
        this.expenditure = expenditure;
    }

    public Double getCommitment() {
        return commitment;
    }

    public Double getDisbursement() {
        return disbursement;
    }

    public Double getExpenditure() {
        return expenditure;
    }

    public void setCommitment(Double commitment) {
        this.commitment = commitment;
    }

    public void setDisbursement(Double disbursement) {
        this.disbursement = disbursement;
    }

    public void setExpenditure(Double expenditure) {
        this.expenditure = expenditure;
    }
}
