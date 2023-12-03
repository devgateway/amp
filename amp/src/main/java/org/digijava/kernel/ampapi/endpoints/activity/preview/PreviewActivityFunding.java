package org.digijava.kernel.ampapi.endpoints.activity.preview;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import org.digijava.kernel.ampapi.endpoints.activity.preview.regional.PreviewRegionalFundingItem;
import org.digijava.kernel.ampapi.endpoints.activity.preview.serializers.AmountSerializer;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Viorel Chihai
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

    @JsonProperty("regional_commitments")
    private List<PreviewRegionalFundingItem> regionalCommitments;
    @JsonProperty("regional_disbursements")
    private List<PreviewRegionalFundingItem> regionalDisbursements;
    @JsonProperty("regional_expenditures")
    private List<PreviewRegionalFundingItem> regionalExpenditures;

    public PreviewActivityFunding() {
        regionalCommitments = new ArrayList<>();
        regionalDisbursements = new ArrayList<>();
        regionalExpenditures = new ArrayList<>();
    }

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

    public List<PreviewRegionalFundingItem> getRegionalCommitments() {
        return regionalCommitments;
    }

    public void setRegionalCommitments(List<PreviewRegionalFundingItem> regionalCommitments) {
        this.regionalCommitments = regionalCommitments;
    }

    public List<PreviewRegionalFundingItem> getRegionalDisbursements() {
        return regionalDisbursements;
    }

    public void setRegionalDisbursements(List<PreviewRegionalFundingItem> regionalDisbursements) {
        this.regionalDisbursements = regionalDisbursements;
    }

    public List<PreviewRegionalFundingItem> getRegionalExpenditures() {
        return regionalExpenditures;
    }

    public void setRegionalExpenditures(List<PreviewRegionalFundingItem> regionalExpenditures) {
        this.regionalExpenditures = regionalExpenditures;
    }
}
