package org.digijava.module.aim.dbentity;

import java.io.Serializable;
import java.util.Date;
import java.util.Set;

import org.apache.commons.collections.ComparatorUtils;
import org.apache.struts.tiles.taglib.GetAttributeTag;

public class AmpGPINiSurvey implements Serializable, Comparable<AmpGPINiSurvey> {
	
	private static final long serialVersionUID = -4889980304099658852L;
	
	private Long ampGPINiSurveyId;
	private Date surveyDate;
	private AmpActivityVersion activity;
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

	public AmpActivityVersion getActivity() {
		return activity;
	}

	public void setActivity(AmpActivityVersion activity) {
		this.activity = activity;
	}

	public Set<AmpGPINiSurveyResponse> getResponses() {
		return responses;
	}

	public void setResponses(Set<AmpGPINiSurveyResponse> responses) {
		this.responses = responses;
	}

	@Override
	public int compareTo(AmpGPINiSurvey o) {
		return ComparatorUtils.nullLowComparator(null).compare(getActivity(), o.getActivity());
	}

}
