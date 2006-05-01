/*
 * Created on 8/03/2006
 * @author akashs
 * 
 */
package org.digijava.module.aim.helper;

public class SurveyFunding {

	private Long surveyId;
	private String fundingOrgName;
	
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
	/**
	 * @param surveyId The surveyId to set.
	 */
	public void setSurveyId(Long surveyId) {
		this.surveyId = surveyId;
	}
}
