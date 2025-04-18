package org.dgfoundation.amp.onepager.util;

public class FMInfo {
    private AmpFMTypes type;
    private String name;

    public FMInfo(AmpFMTypes type, String name) {
        this.type = type;
        this.name = name;
    }

    public AmpFMTypes getType() {
        return type;
    }
    public void setType(AmpFMTypes type) {
        this.type = type;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    
    
}
