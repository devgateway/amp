package org.digijava.kernel.ampapi.endpoints.activity.preview;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import java.util.List;
import java.util.Map;


/**
 * 
 * @author Viorel Chihai
 *
 */
public class PreviewFunding {
    
    @JsonProperty("donor_organization_id")
    private Long donorOrganizationId;
    
    @JsonProperty("funding_id")
    private Long fundingId;

    @JsonProperty("funding_details")
    private Map<String,List<PreviewFundingTransaction>> transactions;
    
    @JsonSerialize(using = AmountSerializer.class)
    @JsonProperty("undisbursed_balance")
    private Double undisbursedBalance;

    public Long getDonorOrganizationId() {
        return donorOrganizationId;
    }

    public void setDonorOrganizationId(Long donorOrganizationId) {
        this.donorOrganizationId = donorOrganizationId;
    }

    public Double getUndisbursedBalance() {
        return undisbursedBalance;
    }

    public void setUndisbursedBalance(Double undisbursedBalance) {
        this.undisbursedBalance = undisbursedBalance;
    }

    public Long getFundingId() {
        return fundingId;
    }

    public void setFundingId(Long fundingId) {
        this.fundingId = fundingId;
    }

    public Map<String, List<PreviewFundingTransaction>> getTransactions() {
        return transactions;
    }

    public void setTransactions(Map<String, List<PreviewFundingTransaction>> transactions) {
        this.transactions = transactions;
    }
}
