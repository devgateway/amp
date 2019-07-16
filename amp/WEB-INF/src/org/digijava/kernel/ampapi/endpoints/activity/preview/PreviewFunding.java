package org.digijava.kernel.ampapi.endpoints.activity.preview;

import java.util.List;

import org.codehaus.jackson.annotate.JsonProperty;
import org.codehaus.jackson.map.annotate.JsonSerialize;

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
    private List<PreviewFundingDetail> fundingDetails;
    
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

    public List<PreviewFundingDetail> getFundingDetails() {
        return fundingDetails;
    }

    public void setFundingDetails(List<PreviewFundingDetail> fundingDetails) {
        this.fundingDetails = fundingDetails;
    }

    public Long getFundingId() {
        return fundingId;
    }

    public void setFundingId(Long fundingId) {
        this.fundingId = fundingId;
    }
    
}
