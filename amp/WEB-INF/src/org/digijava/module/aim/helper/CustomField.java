package org.digijava.module.aim.helper;

public class CustomField {
	private int step;
	private String name;
	private String description;
	private String value;
	private String ampActivityPropertyName;

	public void setStep(int step) {
		this.step = step;
	}

	public int getStep() {
		return step;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public String getValue() {
		return value;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getDescription() {
		return description;
	}

	public void setAmpActivityPropertyName(String ampActivityPropertyName) {
		this.ampActivityPropertyName = ampActivityPropertyName;
	}

	public String getAmpActivityPropertyName() {
		return ampActivityPropertyName;
	}

}
