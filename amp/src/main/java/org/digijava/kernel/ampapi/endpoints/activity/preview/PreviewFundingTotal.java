package org.digijava.kernel.ampapi.endpoints.activity.preview;


import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import org.digijava.kernel.ampapi.endpoints.activity.preview.serializers.AmountSerializer;

/**
 * 
 * @author Viorel Chihai
 *
 */
public class PreviewFundingTotal {

    @JsonProperty("transaction_type")
    private String transactionType;

    @JsonProperty("adjustment_type")
    private Long adjustmentType;
    
    @JsonSerialize(using = AmountSerializer.class)
    private Double amount;

    public String getTransactionType() {
        return transactionType;
    }

    public void setTransactionType(String transactionType) {
        this.transactionType = transactionType;
    }

    public Long getAdjustmentType() {
        return adjustmentType;
    }

    public void setAdjustmentType(Long adjustmentType) {
        this.adjustmentType = adjustmentType;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

}
