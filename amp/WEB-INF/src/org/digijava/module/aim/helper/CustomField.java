package org.digijava.module.aim.helper;

public class CustomField {
	private int step;
	private String db_field_name;
	private String name;
	private String description;
	private String value;

	public void setStep(int step) {
		this.step = step;
	}

	public int getStep() {
		return step;
	}

	public void setDb_field_name(String db_field_name) {
		this.db_field_name = db_field_name;
	}

	public String getDb_field_name() {
		return db_field_name;
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
}
