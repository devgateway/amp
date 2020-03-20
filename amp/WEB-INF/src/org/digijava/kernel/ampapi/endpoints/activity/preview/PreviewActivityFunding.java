package org.digijava.kernel.ampapi.endpoints.activity.preview;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

/**
 * 
 * @author Viorel Chihai
 *
 */
public class PreviewActivityFunding {

    @JsonProperty("funding_information")
    private PreviewFundingInformation fundingInformation;

    @JsonSerialize(using = AmountSerializer.class)
    @JsonProperty("ppc_amount")
    private Double ppcAmount;

    @JsonSerialize(using = AmountSerializer.class)
    @JsonProperty("rpc_amount")
    private Double rpcAmount;

    private String currency;

    public PreviewFundingInformation getFundingInformation() {
        return fundingInformation;
    }

    public void setFundingInformation(PreviewFundingInformation fundingInformation) {
        this.fundingInformation = fundingInformation;
    }

    public Double getPpcAmount() {
        return ppcAmount;
    }

    public void setPpcAmount(Double ppcAmount) {
        this.ppcAmount = ppcAmount;
    }

    public Double getRpcAmount() {
        return rpcAmount;
    }

    public void setRpcAmount(Double rpcAmount) {
        this.rpcAmount = rpcAmount;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }
}
