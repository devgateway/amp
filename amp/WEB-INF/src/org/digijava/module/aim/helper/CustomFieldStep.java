package org.digijava.module.aim.helper;

import java.util.ArrayList;
import java.util.List;

public class CustomFieldStep {
	private int step;
	private String name;
	private List<CustomField<?>> customFields = new ArrayList<CustomField<?>>();
	
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

	public List<CustomField<?>> getCustomFields() {
		return customFields;
	}

	public void addCustomField(CustomField<?> cf){
		customFields.add(cf);
	}
}
