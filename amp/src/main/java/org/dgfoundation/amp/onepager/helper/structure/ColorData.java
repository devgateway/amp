package org.dgfoundation.amp.onepager.helper.structure;

public class ColorData {
    
    private Long id;
    
    private String value;
    
    public ColorData() {
    }
    
    public ColorData(Long id, String value) {
        this.id = id;
        this.value = value;
    }
    
    public Long getId() {
        return id;
    }
    
    public void setId(Long id) {
        this.id = id;
    }
    
    public String getValue() {
        return value;
    }
    
    public void setValue(String value) {
        this.value = value;
    }
}
