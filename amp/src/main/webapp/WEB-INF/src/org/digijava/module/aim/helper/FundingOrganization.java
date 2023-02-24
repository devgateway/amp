package org.digijava.module.aim.helper;

import java.util.Collection;
import java.util.List;

import org.digijava.module.aim.dbentity.AmpFunding;
import org.digijava.module.aim.dbentity.AmpOrganisation;

/**
 * @author jose
 */
public class FundingOrganization implements Comparable{
    
    private Long ampOrgId;
    private String orgName ;
    //Collection of Funding objects
    private List<Funding> fundings;
    private String currentOrganization;
    Boolean fundingActive;
    String fundingActiveString;
    Boolean delegatedCooperation;
    String delegatedCooperationString;
    Boolean delegatedPartner;
    String delegatedPartnerString;
    String fundingorgid;
    
    public FundingOrganization()
    {
        
    }
    
    public FundingOrganization(AmpFunding ampFunding)
    {
        AmpOrganisation org = ampFunding.getAmpDonorOrgId();
        this.setAmpOrgId(org.getAmpOrgId());
        this.setOrgName(org.getName());
        this.setFundingorgid(org.getFundingorgid());
        this.setFundingActive(ampFunding.getActive());
        this.setDelegatedCooperation(ampFunding.getDelegatedCooperation());
        this.setDelegatedPartner(ampFunding.getDelegatedPartner());

        if (this.getDelegatedCooperation() != null && this.getDelegatedCooperation() ) {
            this.setDelegatedCooperationString("checked");
        }
        else
            this.setDelegatedCooperationString("unchecked");

        if (this.getDelegatedPartner() != null && this.getDelegatedPartner() ) {
            this.setDelegatedPartnerString("checked");
        }
        else
            this.setDelegatedPartnerString("unchecked");    
    }
    
    public String getFundingorgid() {
        return fundingorgid;
    }

    public void setFundingorgid(String fundingorgid) {
        this.fundingorgid = fundingorgid;
    }


    
    
    public Long getAmpOrgId() {
        return ampOrgId;
    }
    
    public void setAmpOrgId(Long ampOrgId) {
        this.ampOrgId = ampOrgId;
    }
    
    public String getOrgName() {
        return orgName;
    }
    
    public void setOrgName(String orgName) {
        this.orgName = orgName;
    }
    
    public List<Funding> getFundings() {
        return fundings;
    }
    
    public void setFundings(List<Funding> fundings) {
        this.fundings = fundings;
    }
    public String getCurrentOrganization() {
        return currentOrganization;
    }
    public void setCurrentOrganization(String currentOrganization) {
        this.currentOrganization = currentOrganization;
    }
    
    public boolean equals(Object e) {
        if (e instanceof FundingOrganization) {
            FundingOrganization forg = (FundingOrganization) e;
            return ampOrgId.longValue() == forg.getAmpOrgId().longValue();
        }
        throw new ClassCastException();
    }

    public int compareTo(Object arg0) {
        // TODO Auto-generated method stub
        FundingOrganization comparedOrganization = (FundingOrganization)arg0;
        return this.orgName.compareTo(comparedOrganization.orgName);
        //return -1;
    }

    public Boolean getDelegatedCooperation() {
        return delegatedCooperation;
    }

    public void setDelegatedCooperation(Boolean delegatedCooperation) {
        this.delegatedCooperation = delegatedCooperation;
    }

    public Boolean getDelegatedPartner() {
        return delegatedPartner;
    }

    public void setDelegatedPartner(Boolean delegatedPartner) {
        this.delegatedPartner = delegatedPartner;
    }

    public Boolean getFundingActive() {
        return fundingActive;
    }

    public void setFundingActive(Boolean fundingActive) {
        this.fundingActive = fundingActive;
    }

    public String getDelegatedCooperationString() {
        return delegatedCooperationString;
    }

    public void setDelegatedCooperationString(String delegatedCooperationString) {
        this.delegatedCooperationString = delegatedCooperationString;
    }

    public String getDelegatedPartnerString() {
        return delegatedPartnerString;
    }

    public void setDelegatedPartnerString(String delegatedPartnerString) {
        this.delegatedPartnerString = delegatedPartnerString;
    }

    public String getFundingActiveString() {
        return fundingActiveString;
    }

    public void setFundingActiveString(String fundingActiveString) {
        this.fundingActiveString = fundingActiveString;
    }

    
}
