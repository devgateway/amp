/*
 * Created on 1/03/2006
 * 
 * @author akashs
 * 
 */
package org.digijava.module.aim.dbentity;

public class AmpAhsurveyResponse {

	private Long ampReponseId;
	private AmpAhsurvey ampAHSurveyId;
	private AmpAhsurveyQuestion ampQuestionId;
	private String response;
	
	/**
	 * @return Returns the questionId.
	 */
	public AmpAhsurveyQuestion getAmpQuestionId() {
		return ampQuestionId;
	}
	/**
	 * @param questionId The questionId to set.
	 */
	public void setAmpQuestionId(AmpAhsurveyQuestion ampQuestionId) {
		this.ampQuestionId = ampQuestionId;
	}
	/**
	 * @return Returns the reponseId.
	 */
	public Long getAmpReponseId() {
		return ampReponseId;
	}
	/**
	 * @param reponseId The reponseId to set.
	 */
	public void setAmpReponseId(Long ampReponseId) {
		this.ampReponseId = ampReponseId;
	}
	/**
	 * @return Returns the response.
	 */
	public String getResponse() {
		return response;
	}
	/**
	 * @param response The response to set.
	 */
	public void setResponse(String response) {
		this.response = response;
	}
	/**
	 * @return Returns the surveyId.
	 */
	public AmpAhsurvey getAmpAHSurveyId() {
		return ampAHSurveyId;
	}
	/**
	 * @param surveyId The surveyId to set.
	 */
	public void setAmpAHSurveyId(AmpAhsurvey ampAHSurveyId) {
		this.ampAHSurveyId = ampAHSurveyId;
	}

}
