package org.digijava.kernel.ampapi.endpoints.activity.preview;

import java.util.List;

import org.codehaus.jackson.annotate.JsonProperty;
import org.codehaus.jackson.map.annotate.JsonSerialize;

/**
 * 
 * @author Viorel Chihai
 *
 */
public class PreviewFundingDetail {

    @JsonProperty("transaction_type")
    private Long transactionType;

    @JsonProperty("adjustment_type")
    private Long adjustmentType;

    private List<PreviewFundingTransaction> transactions;
    
    @JsonSerialize(using = AmountSerializer.class)
    private Double subtotal;

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

    public List<PreviewFundingTransaction> getTransactions() {
        return transactions;
    }

    public void setTransactions(List<PreviewFundingTransaction> transactions) {
        this.transactions = transactions;
    }

    public Double getSubtotal() {
        return subtotal;
    }

    public void setSubtotal(Double subtotal) {
        this.subtotal = subtotal;
    }

}
