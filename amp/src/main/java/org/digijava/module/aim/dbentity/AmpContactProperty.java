package org.digijava.module.aim.dbentity;

import org.digijava.module.aim.annotations.interchange.Interchangeable;
import org.digijava.module.aim.annotations.interchange.InterchangeableId;
import org.digijava.module.aim.helper.Constants;

import java.io.Serializable;

public abstract class AmpContactProperty implements Comparable, Serializable {

    @InterchangeableId
    @Interchangeable(fieldTitle = "Id")
    private Long id;

    private AmpContact contact;

    private String name;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
    
    public AmpContact getContact() {
        return contact;
    }
    
    public void setContact(AmpContact contact) {
        this.contact = contact;
    }
    
    public String getName() {
        return name;
    }
    
    public void setName(String name) {
        this.name = name;
    }

    public abstract String getValue();

    public abstract void setValue(String value);

    public static AmpContactProperty instantiate(String type) {
        if (Constants.CONTACT_PROPERTY_NAME_EMAIL.equals(type)) {
            return new AmpContactEmailProperty();
        } else if (Constants.CONTACT_PROPERTY_NAME_FAX.equals(type)) {
            return new AmpContactFaxProperty();
        } else if (Constants.CONTACT_PROPERTY_NAME_PHONE.equals(type)) {
            return new AmpContactPhoneProperty();
        } else {
            throw new IllegalArgumentException("Unknown contact property type: " + type);
        }
    }

    @Override
    public int compareTo(Object o) {
        // TODO Auto-generated method stub
        if (!(o instanceof AmpContactProperty)) {
            return -1;
        }
        AmpContactProperty a = (AmpContactProperty) o;

        if (a.getId() == null && this.getId() == null) {
            if (this.getName().equals(a.getName())) {
                if (this.getValue() != null && a.getValue() != null) {
                    if (this.getValue().equals(a.getValue())) {
                        return 0;
                    }
                }
                
            }
            return 1;

        }

        if (a.getId() == null || (a.getId() != null && this.getId() == null)) {
            return 1;
        }
        return this.getId().compareTo(a.getId());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof AmpContactProperty)) return false;

        AmpContactProperty that = (AmpContactProperty) o;
        
        return id != null && id.equals(that.id);
    }

    @Override
    public int hashCode() {
        return id != null ? id.hashCode() : 0;
    }
}
