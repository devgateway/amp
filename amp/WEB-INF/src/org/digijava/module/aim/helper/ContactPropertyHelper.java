package org.digijava.module.aim.helper;

public class ContactPropertyHelper {
    private String phoneType; //home, work, cell phone

    private Long phoneTypeId; //home, work, cell phone

    private String name;

    public Long getPhoneTypeId() {
        return phoneTypeId == null ? new Long(0) : this.phoneTypeId;
    }

    public void setPhoneTypeId(Long phoneTypeId) {
        this.phoneTypeId = phoneTypeId;
    }


    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    private String value;
    
    public String getPhoneType() {
        return phoneType;
    }
    public void setPhoneType(String phoneType) {
        this.phoneType = phoneType;
    }
    public String getValue() {
        return value;
    }
    public void setValue(String value) {
        this.value = value;
    }
    
    public boolean equals(Object obj) {
        if (obj == null) throw new NullPointerException();
        
        if (!(obj instanceof ContactPropertyHelper)) throw new ClassCastException();
        
        ContactPropertyHelper temp = (ContactPropertyHelper) obj;
        if (temp.getValue() == null)
            return false;
        return (temp.getValue().equals(this.getValue()));
    }
    
}
