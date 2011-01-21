/*
 * Created on 1/03/2006
 *
 * @author akashs
 *
 */
package org.digijava.module.aim.dbentity;

import java.util.Set;

import org.digijava.module.aim.form.EditActivityForm.Survey;

public class AmpAhsurvey {

	private Long ampAHSurveyId;

	//private AmpFunding ampFundingId;
	//private Integer surveyYear;
	//point of delivery donor
	private AmpActivity ampActivityId;
	private AmpOrganisation ampDonorOrgId;
    private AmpOrganisation pointOfDeliveryDonor;
	private Set responses;

	/**
	 * @return Returns the ampAHSurveyId.
	 */
	public Long getAmpAHSurveyId() {
		return ampAHSurveyId;
	}
	/**
	 * @param ampAHSurveyId The ampAHSurveyId to set.
	 */
	public void setAmpAHSurveyId(Long ampAHSurveyId) {
		this.ampAHSurveyId = ampAHSurveyId;
	}
	/**
	 * @return Returns the responses.
	 */
	public Set getResponses() {
		return responses;
	}
	/**
	 * @param responses The responses to set.
	 */
	public void setResponses(Set responses) {
		this.responses = responses;
	}
	/**
	 * @return Returns the ampActivityId.
	 */
	public AmpActivity getAmpActivityId() {
		return ampActivityId;
	}
	/**
	 * @param ampActivityId The ampActivityId to set.
	 */
	public void setAmpActivityId(AmpActivity ampActivityId) {
		this.ampActivityId = ampActivityId;
	}
	/**
	 * @return Returns the ampDonorOrgId.
	 */
	public AmpOrganisation getAmpDonorOrgId() {
		return ampDonorOrgId;
	}

    public AmpOrganisation getPointOfDeliveryDonor() {
        return pointOfDeliveryDonor;
    }

    /**
	 * @param ampDonorOrgId The ampDonorOrgId to set.
	 */
	public void setAmpDonorOrgId(AmpOrganisation ampDonorOrgId) {
		this.ampDonorOrgId = ampDonorOrgId;
	}

    public void setPointOfDeliveryDonor(AmpOrganisation pointOfDeliveryDonor) {
        this.pointOfDeliveryDonor = pointOfDeliveryDonor;
    }
    
    @Override
	public boolean equals(Object obj) {
		if(obj instanceof AmpAhsurvey) {
			AmpAhsurvey aux = (AmpAhsurvey) obj;
			if(aux.getAmpAHSurveyId() == this.getAmpAHSurveyId()) {
				return true;
			} else {
				return false;
			}
		} else {
			return false;
		}
	}
	
	@Override
	public int hashCode() {
		if(this.ampAHSurveyId != null) {
			return this.ampAHSurveyId.intValue();
		} else {
			return 0;
		}
	}
}
