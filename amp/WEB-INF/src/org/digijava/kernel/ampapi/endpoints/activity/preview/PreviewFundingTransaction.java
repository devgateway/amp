package org.digijava.kernel.ampapi.endpoints.activity.preview;


import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

/**
 * 
 * @author Viorel Chihai
 *
 */
public class PreviewFundingTransaction {

    @JsonProperty("transaction_id")
    private Long transactionId;
    
    @JsonSerialize(using = AmountSerializer.class)
    @JsonProperty("transaction_amount")
    private Double transactionAmount;
    
    @JsonProperty("transaction_date")
    private String transactionDate;

    @JsonProperty("reporting_date")
    private String reportingDate;

    @JsonProperty("adjustment_type")
    private Long adjustmentType;

    @JsonProperty("transaction_type")
    private Long transactionType;

    public Long getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(Long transactionId) {
        this.transactionId = transactionId;
    }

    public Double getTransactionAmount() {
        return transactionAmount;
    }

    public void setTransactionAmount(Double transactionAmount) {
        this.transactionAmount = transactionAmount;
    }

    public String getTransactionDate() {
        return transactionDate;
    }

    public void setTransactionDate(String transactionDate) {
        this.transactionDate = transactionDate;
    }

    public String getReportingDate() {
        return reportingDate;
    }

    public void setReportingDate(String reportingDate) {
        this.reportingDate = reportingDate;
    }

    public Long getAdjustmentType() {
        return adjustmentType;
    }

    public void setAdjustmentType(Long adjustmentType) {
        this.adjustmentType = adjustmentType;
    }

    public Long getTransactionType() {
        return transactionType;
    }

    public void setTransactionType(Long transactionType) {
        this.transactionType = transactionType;
    }
}
