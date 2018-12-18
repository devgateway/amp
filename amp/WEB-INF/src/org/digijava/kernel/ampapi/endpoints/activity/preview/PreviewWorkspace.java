package org.digijava.kernel.ampapi.endpoints.activity.preview;

public class PreviewWorkspace {
    
    private String name;
    
    private Type type;
    
    private String extraInfo;
    
    public PreviewWorkspace(String name, Type type) {
        this.name = name;
        this.type = type;
    }
    
    public PreviewWorkspace(String name, Type type, String extraInfo) {
        this(name, type);
        this.extraInfo = extraInfo;
    }
    
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
        TEAM, COMPUTED, MANAGEMENT
    };
}
