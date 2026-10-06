package org.digijava.module.aim.helper;

import java.util.Date;

public class Commitments {
    
    private double amount;
    private Long donorId;
    private String currencyCode;
    private Date transactionDate;
    
    /**
     * @return Returns the amount.
     */
    public double getAmount() {
        return amount;
    }
    /**
     * @param amount The amount to set.
     */
    public void setAmount(double amount) {
        this.amount = amount;
    }
    /**
     * @return Returns the donorId.
     */
    public Long getDonorId() {
        return donorId;
    }
    /**
     * @param donorId The donorId to set.
     */
    public void setDonorId(Long donorId) {
        this.donorId = donorId;
    }
    /**
     * @return Returns the transactionDate.
     */
    public Date getTransactionDate() {
        return transactionDate;
    }
    /**
     * @param transactionDate The transactionDate to set.
     */
    public void setTransactionDate(Date transactionDate) {
        this.transactionDate = transactionDate;
    }
    /**
     * @return Returns the currencyCode.
     */
    public String getCurrencyCode() {
        return currencyCode;
    }
    /**
     * @param currencyCode The currencyCode to set.
     */
    public void setCurrencyCode(String currencyCode) {
        this.currencyCode = currencyCode;
    }
    
    
}
