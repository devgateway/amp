package org.digijava.module.aim.dbentity;

import java.io.Serializable;
import java.util.Date;
import java.util.Set;

public class AmpGPINiSurvey implements Serializable {
	private static final long serialVersionUID = -4889980304099658852L;
	private Long ampGPINiSurveyId;
	private Date surveyDate;
	private AmpActivityVersion ampActivityId;
	private Set<AmpGPINiSurveyResponse> responses;
	
	public Long getAmpGPINiSurveyId() {
		return ampGPINiSurveyId;
	}
	public void setAmpGPINiSurveyId(Long ampGPINiSurveyId) {
		this.ampGPINiSurveyId = ampGPINiSurveyId;
	}
	public Date getSurveyDate() {
		return surveyDate;
	}
	public void setSurveyDate(Date surveyDate) {
		this.surveyDate = surveyDate;
	}
	public AmpActivityVersion getAmpActivityId() {
		return ampActivityId;
	}
	public void setAmpActivityId(AmpActivityVersion ampActivityId) {
		this.ampActivityId = ampActivityId;
	}
	public Set<AmpGPINiSurveyResponse> getResponses() {
		return responses;
	}
	public void setResponses(Set<AmpGPINiSurveyResponse> responses) {
		this.responses = responses;
	}

}
