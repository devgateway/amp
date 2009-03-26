package org.digijava.module.aim.helper;

public class CheckCustomField extends CustomField<Boolean>{

	private String labelTrue;
	private String labelFalse;
	public void setLabelTrue(String labelTrue) {
		this.labelTrue = labelTrue;
	}
	public String getLabelTrue() {
		return labelTrue;
	}
	public void setLabelFalse(String labelFalse) {
		this.labelFalse = labelFalse;
	}
	public String getLabelFalse() {
		return labelFalse;
	}
	@Override
	public void setValue(Boolean value) {
		this.value = value;
	}
	
	public void setBooleanValue(Boolean value) {
		this.value = value;
	}
	
	public Boolean getBooleanValue() {
		return value;
	}	

}
