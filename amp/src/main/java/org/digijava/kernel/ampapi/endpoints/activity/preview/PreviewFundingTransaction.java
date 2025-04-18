package org.digijava.kernel.ampapi.endpoints.activity.preview;


import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import org.digijava.kernel.ampapi.endpoints.activity.preview.serializers.AmountSerializer;
import org.digijava.kernel.ampapi.endpoints.activity.preview.serializers.DateSerializer;

import java.util.Date;

/**
 * @author Viorel Chihai
 */
public class    PreviewFundingTransaction {

    @JsonProperty("transaction_id")
    private Long transactionId;

    @JsonSerialize(using = AmountSerializer.class)
    @JsonProperty("transaction_amount")
    private Double transactionAmount;

    @JsonSerialize(using = DateSerializer.class)
    @JsonProperty("transaction_date")
    private Date transactionDate;

    @JsonSerialize(using = DateSerializer.class)
    @JsonProperty("reporting_date")
    private Date reportingDate;

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

    public Date getTransactionDate() {
        return transactionDate;
    }

    public void setTransactionDate(Date transactionDate) {
        this.transactionDate = transactionDate;
    }

    public Date getReportingDate() {
        return reportingDate;
    }

    public void setReportingDate(Date reportingDate) {
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
