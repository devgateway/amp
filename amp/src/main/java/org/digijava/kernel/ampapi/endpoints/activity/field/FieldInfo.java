package org.digijava.kernel.ampapi.endpoints.activity.field;

public class FieldInfo {
    
    private String type;
    
    private Integer maxLength;
    
    public FieldInfo(String type, Integer maxLength) {
        this.type = type;
        this.maxLength = maxLength;
    }
    
    public String getType() {
        return type;
    }
    
    public void setType(String type) {
        this.type = type;
    }
    
    public Integer getMaxLength() {
        return maxLength;
    }
    
    public void setMaxLength(Integer maxLength) {
        this.maxLength = maxLength;
    }
}
