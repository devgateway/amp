package org.digijava.module.aim.util;

import java.util.List;

public class Output {

    private String[] title;

    private Object[] value;

    private List<Output> outputs;

    private Boolean translateValue = Boolean.FALSE;
    
    private boolean deletedValues = false;
    
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

    public int contains (String title) {
        int retVal = -1;
        for (int idx = 0; idx < this.title.length; idx ++) {
            if (title.equals(this.title[idx])) {
                retVal = idx;
                break;
            }
        }
        return retVal;
    }

    public Output getOutputByTitle(String title) {
        Output retVal = null;
        for (Output out : this.outputs) {
            if (out.contains(title) > -1) {
                retVal = out;
                break;
            }
        }
        return retVal;
    }

    public Output getOutputByValues(Object[] values) {
        Output retVal = null;
        for (Output out : this.outputs) {
            boolean matched = true;
            if (out.getValue().length == values.length) {
                for (int idx = 0; idx < out.getValue().length; idx ++) {
                    if (!out.getValue()[idx].equals(values[idx])) {
                        matched = false;
                        break;
                    }
                }
                if (matched) {
                    retVal = out;
                    break;
                }
            }
        }
        return retVal;
    }

    public boolean hasDeletedValues() {
        return deletedValues;
    }

    public void setDeletedValues(boolean deletedValues) {
        this.deletedValues = deletedValues;
    }
    
}
