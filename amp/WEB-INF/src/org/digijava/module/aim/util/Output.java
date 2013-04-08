package org.digijava.module.aim.util;

import java.util.List;

public class Output {

	private String[] title;

	private Object[] value;

	private List<Output> outputs;

    private Boolean translateValue = Boolean.FALSE;
	
	public String[] getTitle() {
		return title;
	}

	public void setTitle(String[] title) {
		this.title = title;
	}

	public Object[] getValue() {
		return value;
	}

	public void setValue(Object[] value) {
		this.value = value;
	}

	public List<Output> getOutputs() {
		return outputs;
	}

	public void setOutputs(List<Output> outputs) {
		this.outputs = outputs;
	}

    public Boolean getTranslateValue() {
        return translateValue;
    }

    public void setTranslateValue(Boolean translateValue) {
        this.translateValue = translateValue;
    }

    public Output(List<Output> outputs, String[] title, Object[] value, Boolean translateValue) {
		super();
		this.outputs = outputs;
		this.title = title;
		this.value = value;
        this.translateValue = translateValue;
	}
    public Output(List<Output> outputs, String[] title, Object[] value) {
        this(outputs, title, value, Boolean.FALSE);
    }

    public Output() {
		super();
	}
}
