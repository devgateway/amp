package org.digijava.module.aim.annotations.activityversioning;

public class CompareOutput {

	// Use an array in case more than 2 versions will be compared.
	private String[] stringOutput;
	private String descriptionOutput;

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

	public CompareOutput(String descriptionOutput, String[] stringOutput) {
		super();
		this.descriptionOutput = descriptionOutput;
		this.stringOutput = stringOutput;
	}

	public CompareOutput() {
		super();
	}
}
