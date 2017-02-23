package org.digijava.module.aim.dbentity;

import java.io.Serializable;

public class AmpGPINiSurveyResponse implements Serializable {
	
	private static final long serialVersionUID = -6656563271238273140L;
	private Long ampGPINiSurveyResponseId;
	private AmpGPINiSurvey ampGPINiSurveyId;
	private AmpGPINiQuestion ampGPINiQuestionId;
	private Long integerResponse;
	private String textResponse;
	private AmpGPINiQuestionOption questionOptionId;	
	private String resourceUuid;
	
	public Long getAmpGPINiSurveyResponseId() {
		return ampGPINiSurveyResponseId;
	}
	public void setAmpGPINiSurveyResponseId(Long ampGPINiSurveyResponseId) {
		this.ampGPINiSurveyResponseId = ampGPINiSurveyResponseId;
	}
	public AmpGPINiSurvey getAmpGPINiSurveyId() {
		return ampGPINiSurveyId;
	}
	public void setAmpGPINiSurveyId(AmpGPINiSurvey ampGPINiSurveyId) {
		this.ampGPINiSurveyId = ampGPINiSurveyId;
	}
	public AmpGPINiQuestion getAmpGPINiQuestionId() {
		return ampGPINiQuestionId;
	}
	public void setAmpGPINiQuestionId(AmpGPINiQuestion ampGPINiQuestionId) {
		this.ampGPINiQuestionId = ampGPINiQuestionId;
	}
	public Long getIntegerResponse() {
		return integerResponse;
	}
	public void setIntegerResponse(Long integerResponse) {
		this.integerResponse = integerResponse;
	}
	public String getTextResponse() {
		return textResponse;
	}
	public void setTextResponse(String textResponse) {
		this.textResponse = textResponse;
	}
	public AmpGPINiQuestionOption getQuestionOptionId() {
		return questionOptionId;
	}
	public void setQuestionOptionId(AmpGPINiQuestionOption questionOptionId) {
		this.questionOptionId = questionOptionId;
	}
	public String getResourceUuid() {
		return resourceUuid;
	}
	public void setResourceUuid(String resourceUuid) {
		this.resourceUuid = resourceUuid;
	}
}