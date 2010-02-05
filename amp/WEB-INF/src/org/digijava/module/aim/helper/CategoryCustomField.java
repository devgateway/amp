package org.digijava.module.aim.helper;

import org.apache.log4j.Logger;

public class CategoryCustomField extends CustomField<Long> {
	private static Logger logger = Logger.getLogger(CategoryCustomField.class);
	private String categoryName;

	public void setCategoryName(String categoryName) {
		this.categoryName = categoryName;
	}

	public String getCategoryName() {
		return categoryName;
	}

	public void setValue(Long value) {
		this.value = value;
	}
	
	public void setValue(String value){
		try{
			this.value = Long.parseLong(value);
		}catch(NumberFormatException e){
			logger.error("Wrong value specified: " + value , e);
		}
	}
	
	public void setLongValue(Long value){
		this.setValue(value);
	}
	
	public Long getLongValue(){
		return this.value;
	}	
}
