package org.digijava.module.contentrepository.helper.template;

public class SubmittedValueHolder {
	private Integer fieldOrdinalValue;
	private String submittedValue;
	
	public SubmittedValueHolder(Integer fieldOrdNum, String value){
		this.fieldOrdinalValue=fieldOrdNum;
		this.submittedValue=value;
	}
	
	public SubmittedValueHolder(){
		
	}

	public Integer getFieldOrdinalValue() {
		return fieldOrdinalValue;
	}
	public void setFieldOrdinalValue(Integer fieldOrdinalValue) {
		this.fieldOrdinalValue = fieldOrdinalValue;
	}
	public String getSubmittedValue() {
		return submittedValue;
	}
	public void setSubmittedValue(String submittedValue) {
		this.submittedValue = submittedValue;
	}	
}
