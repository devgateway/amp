package org.digijava.module.aim.dbentity;

import java.io.Serializable;
import java.util.Comparator;
import java.util.Set;

public class AmpGPISurveyIndicator implements Serializable {

	private Long ampIndicatorId;
	private String name;
	private Integer totalQuestions;
	private Integer indicatorNumber;
	private String indicatorCode;
	private String status;
	private Set<AmpGPISurveyQuestion> questions;
	private Set<AmpGPISurveyIndicatorCalcFormula> calcFormulas;

	public static class GPISurveyIndicatorComparator implements Comparator<AmpGPISurveyIndicator>, Serializable {

		private static final long serialVersionUID = 1L;

		@Override
		public int compare(AmpGPISurveyIndicator arg0, AmpGPISurveyIndicator arg1) {
			if (arg0.getIndicatorNumber() != null && arg1.getIndicatorNumber() != null) {
				return arg0.getIndicatorNumber().compareTo(arg1.getIndicatorNumber());
			}
			return arg0.hashCode() - arg1.hashCode();
		}
	}

	public String getIndicatorCode() {
		return indicatorCode;
	}

	public void setIndicatorCode(String indicatorCode) {
		this.indicatorCode = indicatorCode;
	}

	public Long getAmpIndicatorId() {
		return ampIndicatorId;
	}

	public void setAmpIndicatorId(Long ampIndicatorId) {
		this.ampIndicatorId = ampIndicatorId;
	}

	public String getName() {
		return name;
	}

	public String getNameTrn() {
		return name.toLowerCase().replaceAll(" ", "").replaceAll("%", "");
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public Integer getTotalQuestions() {
		return totalQuestions;
	}

	public void setTotalQuestions(Integer totalQuestions) {
		this.totalQuestions = totalQuestions;
	}

	public Integer getIndicatorNumber() {
		return indicatorNumber;
	}

	public void setIndicatorNumber(Integer indicatorNumber) {
		this.indicatorNumber = indicatorNumber;
	}

	public Set<AmpGPISurveyQuestion> getQuestions() {
		return questions;
	}

	public Set<AmpGPISurveyIndicatorCalcFormula> getCalcFormulas() {
		return calcFormulas;
	}

	public void setQuestions(Set<AmpGPISurveyQuestion> questions) {
		this.questions = questions;
	}

	public void setCalcFormulas(Set<AmpGPISurveyIndicatorCalcFormula> calcFormulas) {
		this.calcFormulas = calcFormulas;
	}

}
