package org.digijava.module.aim.annotations.activityversioning;

import java.lang.reflect.Field;

public class CompareOutput {

	// TODO: Use an array in case more than 2 versions will be compared.
	private String[] stringOutput;
	private String descriptionOutput;
	// Metadata for creating a new activity with merged values.
	private Field fieldOutput;
	private Object[] originalValueOutput;

	public String[] getStringOutput() {
		return stringOutput;
	}

	public void setStringOutput(String[] stringOutput) {
		this.stringOutput = stringOutput;
	}

	public String getDescriptionOutput() {
		return descriptionOutput;
	}

	public void setDescriptionOutput(String descriptionOutput) {
		this.descriptionOutput = descriptionOutput;
	}

	public CompareOutput(String descriptionOutput, String[] stringOutput, Field fieldOutput, Object[] originalValueOuput) {
		super();
		this.descriptionOutput = descriptionOutput;
		this.stringOutput = stringOutput;
		this.fieldOutput = fieldOutput;
		this.originalValueOutput = originalValueOuput;
	}

	public CompareOutput() {
		super();
	}

	public Field getFieldOutput() {
		return fieldOutput;
	}

	public void setFieldOutput(Field fieldOutput) {
		this.fieldOutput = fieldOutput;
	}

	public Object[] getOriginalValueOutput() {
		return originalValueOutput;
	}

	public void setOriginalValueOutput(Object[] originalValueOutput) {
		this.originalValueOutput = originalValueOutput;
	}
}
