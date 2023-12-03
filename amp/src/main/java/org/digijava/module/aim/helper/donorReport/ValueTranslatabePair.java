package org.digijava.module.aim.helper.donorReport;

import java.util.List;

public class ValueTranslatabePair {
    private List<String>  values;
    public List<String> getValues() {
        return values;
    }
    public void setValues(List<String> values) {
        this.values = values;
    }
    private boolean translatable;
    
    public boolean isTranslatable() {
        return translatable;
    }
    public void setTranslatable(boolean translatable) {
        this.translatable = translatable;
    }
    public ValueTranslatabePair(List<String> values, boolean translatable) {
        this.values = values;
        this.translatable = translatable;
    }
    

}
