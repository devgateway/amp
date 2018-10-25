package org.digijava.kernel.ampapi.endpoints.activity.preview;

import org.codehaus.jackson.annotate.JsonProperty;
import org.codehaus.jackson.map.annotate.JsonSerialize;

/**
 * 
 * @author Viorel Chihai
 *
 */
public class PreviewFundingTotal {

    @JsonProperty("transaction_type")
    private Long transactionType;

    @JsonProperty("adjustment_type")
    private Long adjustmentType;
    
    @JsonSerialize(using = AmountSerializer.class)
    private Double amount;

    public Long getTransactionType() {
        return transactionType;
    }

    public void setTransactionType(Long transactionType) {
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
