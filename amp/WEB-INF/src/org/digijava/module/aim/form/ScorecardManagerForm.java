package org.digijava.module.aim.form;


import org.apache.struts.action.ActionForm;

public class ScorecardManagerForm extends ActionForm {

	private static final long serialVersionUID = 1701410613054380912L;
	private Boolean validationPeriod;
	private Integer validationTime;
	private String action;
	
	public Boolean getValidationPeriod() {
		return validationPeriod;
	}
	public void setValidationPeriod(Boolean validationPeriod) {
		this.validationPeriod = validationPeriod;
	}
	public Integer getValidationTime() {
		return validationTime;
	}
	public void setValidationTime(Integer validationTime) {
		this.validationTime = validationTime;
	}
	public String getAction() {
		return action;
	}
	public void setAction(String action) {
		this.action = action;
	}
	

}
