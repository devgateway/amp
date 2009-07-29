package org.digijava.module.aim.helper;

public abstract class CustomField<T> {
	private CustomFieldStep step;
	private String name;
	private String description;
	protected T value;
	private String ampActivityPropertyName;
	private String FM_field;

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

	public abstract void setValue(T value);

	public T getValue() {
		return value;
	}

	public void setStep(CustomFieldStep step) {
		this.step = step;
	}

	public CustomFieldStep getStep() {
		return step;
	}

}
