package org.digijava.module.contentrepository.dbentity.template;

import java.util.Set;

public abstract class TemplateField {
	private Long id;
	private String fieldName; //should be removed 
	
	private String value; //for now : static text or checkbox value
	private Set<PossibleValue> possibleValues; // select,multiple select , multibox
	
	private TemplateDoc templateDoc;
	private Integer ordinalNumber; //field has it's ordinal number in template
		
	public String getType(){
		return "tf"; //template field
	}
	
	/**
	 * how field get's rendered on the page
	 */
	public abstract String getRendered();
	
	//whether this field is allowed to have multiple pre-defined values
	public boolean getHasMultipleValues() {
		return true;
	}
	
	public TemplateDoc getTemplateDoc() {
		return templateDoc;
	}

	public void setTemplateDoc(TemplateDoc templateDoc) {
		this.templateDoc = templateDoc;
	}

	public Integer getOrdinalNumber() {
		return ordinalNumber;
	}

	public void setOrdinalNumber(Integer ordinalNumber) {
		this.ordinalNumber = ordinalNumber;
	}
	

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public Set<PossibleValue> getPossibleValues() {
		return possibleValues;
	}

	public void setPossibleValues(Set<PossibleValue> possibleValues) {
		this.possibleValues = possibleValues;
	}

	public String getFieldName() {
		return fieldName;
	}

	public void setFieldName(String fieldName) {
		this.fieldName = fieldName;
	}		
}
