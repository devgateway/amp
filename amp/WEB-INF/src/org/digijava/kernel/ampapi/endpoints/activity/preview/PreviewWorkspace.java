package org.digijava.kernel.ampapi.endpoints.activity.preview;

public class PreviewWorkspace {
    
    private String name;
    
    private String extraInfo;
    
    private Type type;
    
    public String getName() {
        return name;
    }
    
    public void setName(String name) {
        this.name = name;
    }
    
    public String getExtraInfo() {
        return extraInfo;
    }
    
    public void setExtraInfo(String extraInfo) {
        this.extraInfo = extraInfo;
    }
    
    public Type getType() {
        return type;
    }
    
    public void setType(Type type) {
        this.type = type;
    }
    
    public enum Type {
        ORIGIN, COMPUTED, MANAGEMENT
    };
}
