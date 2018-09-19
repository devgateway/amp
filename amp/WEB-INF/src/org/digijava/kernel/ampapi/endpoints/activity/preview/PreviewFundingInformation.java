package org.digijava.kernel.ampapi.endpoints.activity.preview;

import java.util.List;

import org.codehaus.jackson.annotate.JsonProperty;
import org.codehaus.jackson.map.annotate.JsonSerialize;

/**
 * 
 * @author Viorel Chihai
 *
 */
public class PreviewFundingInformation {

    private List<PreviewFunding> fundings;
    
    private List<PreviewFundingTotal> totals;
    
    @JsonSerialize(using = AmountSerializer.class)
    @JsonProperty("undisbursed_balance")
    private Double undisbursedBalance;

    @JsonSerialize(using = AmountSerializer.class)
    @JsonProperty("delivery_rate")
    private Double deliveryRate;
    
    public List<PreviewFunding> getFundings() {
        return fundings;
    }

    public void setFundings(List<PreviewFunding> fundings) {
        this.fundings = fundings;
    }

    public List<PreviewFundingTotal> getTotals() {
        return totals;
    }

    public void setTotals(List<PreviewFundingTotal> totals) {
        this.totals = totals;
    }

    public Double getUndisbursedBalance() {
        return undisbursedBalance;
    }

    public void setUndisbursedBalance(Double undisbursedBalance) {
        this.undisbursedBalance = undisbursedBalance;
    }

    public Double getDeliveryRate() {
        return deliveryRate;
    }

    public void setDeliveryRate(Double deliveryRate) {
        this.deliveryRate = deliveryRate;
    }
    
}
