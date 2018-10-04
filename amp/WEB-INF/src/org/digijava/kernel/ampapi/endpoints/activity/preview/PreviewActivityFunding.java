package org.digijava.kernel.ampapi.endpoints.activity.preview;

import org.codehaus.jackson.annotate.JsonProperty;
import org.codehaus.jackson.map.annotate.JsonSerialize;

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

    private Long currency;

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

    public Long getCurrency() {
        return currency;
    }

    public void setCurrency(Long currency) {
        this.currency = currency;
    }

}
