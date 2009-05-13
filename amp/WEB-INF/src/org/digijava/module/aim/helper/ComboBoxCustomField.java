package org.digijava.module.aim.helper;


import java.util.LinkedHashMap;

public class ComboBoxCustomField extends CustomField<String> {
	private LinkedHashMap<String,String> options;

	public ComboBoxCustomField() {
	}
	
	public void setOptions(LinkedHashMap<String,String> options) {
		this.options = options;
	}

	public LinkedHashMap<String,String> getOptions() {
		return options;
	}

	@Override
	public void setValue(String value) {
		this.value = value;
	}
}
