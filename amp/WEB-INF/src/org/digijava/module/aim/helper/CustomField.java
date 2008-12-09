package org.digijava.module.aim.helper;

public class CustomField<T> {
	private int step;
	private String name;
	private String description;
	private T value;
	private String ampActivityPropertyName;
	private String FM_field;

	public void setStep(int step) {
		this.step = step;
	}

	public int getStep() {
		return step;
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

	public void setFM_field(String fM_field) {
		FM_field = fM_field;
	}

	public String getFM_field() {
		return FM_field;
	}

	public void setValue(T value) {
		this.value = value;
	}

	public T getValue() {
		return value;
	}

}
