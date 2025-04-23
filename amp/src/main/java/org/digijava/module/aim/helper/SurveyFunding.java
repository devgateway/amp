/*
 * Created on 8/03/2006
 * @author akashs
 *
 */
package org.digijava.module.aim.helper;

public class SurveyFunding {

    private Long surveyId;
    private String fundingOrgName;
    private String deliveryDonorName;
    private String acronim;
    private Long orgID;

    public String getAcronim() {
        return acronim;
    }
    public void setAcronim(String acronim) {
        this.acronim = acronim;
    }
    /**
     * @return Returns the fundingOrgName.
     */
    public String getFundingOrgName() {
        return fundingOrgName;
    }
    /**
     * @param fundingOrgName The fundingOrgName to set.
     */
    public void setFundingOrgName(String fundingOrgName) {
        this.fundingOrgName = fundingOrgName;
    }
    /**
     * @return Returns the surveyId.
     */
    public Long getSurveyId() {
        return surveyId;
    }

    public String getDeliveryDonorName() {
        return deliveryDonorName;
    }

    /**
     * @param surveyId The surveyId to set.
     */
    public void setSurveyId(Long surveyId) {
        this.surveyId = surveyId;
    }

    public void setDeliveryDonorName(String deliveryDonorName) {
        this.deliveryDonorName = deliveryDonorName;
    }
    
    public Long getOrgID() {
        return orgID;
    }
    
    public void setOrgID(Long orgID) {
        this.orgID = orgID;
    }
}
