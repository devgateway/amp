package org.digijava.module.aim.dbentity;

import java.io.Serializable;

public class AmpGPINiSurveyResponse implements Serializable {

	private static final long serialVersionUID = -6656563271238273140L;
	
	private Long ampGPINiSurveyResponseId;
	private AmpGPINiSurvey ampGPINiSurvey;
	private AmpGPINiQuestion ampGPINiQuestion;
	private Long integerResponse;
	private String textResponse;
	private AmpGPINiQuestionOption questionOption;
	private String resourceUUID;

	public Long getAmpGPINiSurveyResponseId() {
		return ampGPINiSurveyResponseId;
	}

	public void setAmpGPINiSurveyResponseId(Long ampGPINiSurveyResponseId) {
		this.ampGPINiSurveyResponseId = ampGPINiSurveyResponseId;
	}

	public AmpGPINiSurvey getAmpGPINiSurvey() {
		return ampGPINiSurvey;
	}

	public void setAmpGPINiSurvey(AmpGPINiSurvey ampGPINiSurvey) {
		this.ampGPINiSurvey = ampGPINiSurvey;
	}

	public AmpGPINiQuestion getAmpGPINiQuestion() {
		return ampGPINiQuestion;
	}

	public void setAmpGPINiQuestion(AmpGPINiQuestion ampGPINiQuestion) {
		this.ampGPINiQuestion = ampGPINiQuestion;
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

	public AmpGPINiQuestionOption getQuestionOption() {
		return questionOption;
	}

	public void setQuestionOption(AmpGPINiQuestionOption questionOption) {
		this.questionOption = questionOption;
	}

	public String getResourceUUID() {
		return resourceUUID;
	}

	public void setResourceUUID(String resourceUUID) {
		this.resourceUUID = resourceUUID;
	}
}
