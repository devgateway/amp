/*
 * Created on 1/03/2006
 * 
 * @author akashs
 * 
 */
package org.digijava.module.aim.dbentity;

import java.io.Serializable;
import java.util.Comparator;

public class AmpAhsurveyQuestion implements Serializable{

	private Long ampQuestionId;
	private String questionText;
	private AmpAhsurveyIndicator ampIndicatorId;
	private Integer questionNumber;
	private AmpAhsurveyQuestionType ampTypeId;
	private String status;
	
	public static class AhsurveyQuestionComparator implements Comparator<AmpAhsurveyQuestion>, Serializable {

		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;

		@Override
		public int compare(AmpAhsurveyQuestion arg0, AmpAhsurveyQuestion arg1) {
			if (arg0.getQuestionNumber() != null && arg1.getQuestionNumber() != null){
				return arg0.getQuestionNumber().compareTo(arg1.getQuestionNumber());
			}
			return arg0.hashCode()-arg1.hashCode();
		}
		
	}
	
	/**
	 * @return Returns the ampIndicatorId.
	 */
	public AmpAhsurveyIndicator getAmpIndicatorId() {
		return ampIndicatorId;
	}
	/**
	 * @param ampIndicatorId The ampIndicatorId to set.
	 */
	public void setAmpIndicatorId(AmpAhsurveyIndicator ampIndicatorId) {
		this.ampIndicatorId = ampIndicatorId;
	}
	/**
	 * @return Returns the ampQuestionId.
	 */
	public Long getAmpQuestionId() {
		return ampQuestionId;
	}
	/**
	 * @param ampQuestionId The ampQuestionId to set.
	 */
	public void setAmpQuestionId(Long ampQuestionId) {
		this.ampQuestionId = ampQuestionId;
	}
	/**
	 * @return Returns the ampTypeId.
	 */
	public AmpAhsurveyQuestionType getAmpTypeId() {
		return ampTypeId;
	}
	/**
	 * @param ampTypeId The ampTypeId to set.
	 */
	public void setAmpTypeId(AmpAhsurveyQuestionType ampTypeId) {
		this.ampTypeId = ampTypeId;
	}
	/**
	 * @return Returns the questionNumber.
	 */
	public Integer getQuestionNumber() {
		return questionNumber;
	}
	/**
	 * @param questionNumber The questionNumber to set.
	 */
	public void setQuestionNumber(Integer questionNumber) {
		this.questionNumber = questionNumber;
	}
	/**
	 * @return Returns the questionText.
	 */
	public String getQuestionText() {
		return questionText;
	}
	/**
	 * @param questionText The questionText to set.
	 */
	public void setQuestionText(String questionText) {
		this.questionText = questionText;
	}
	/**
	 * @return Returns the status.
	 */
	public String getStatus() {
		return status;
	}
	/**
	 * @param status The status to set.
	 */
	public void setStatus(String status) {
		this.status = status;
	}

}
