package org.digijava.module.aim.dbentity;

import java.io.Serializable;

import org.digijava.module.aim.annotations.interchange.Interchangeable;
import org.digijava.module.aim.annotations.interchange.InterchangeableId;
import org.digijava.module.aim.helper.Constants;
import javax.persistence.*;

@Entity
@Table(name = "AMP_CONTACT_PROPERTIES")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "name", discriminatorType = DiscriminatorType.STRING)
public abstract class AmpContactProperty implements Comparable, Serializable {
    @Id
    @GeneratedValue(generator = "ampContactPropertiesSeq")
    @SequenceGenerator(name = "ampContactPropertiesSeq", sequenceName = "AMP_CONTACT_PROPERTIES_seq", allocationSize = 1)
    @Column(name = "property_id")
    @InterchangeableId
    @Interchangeable(fieldTitle = "Id")
    private Long id;

    @Column(name = "value")
    private String value;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "contact_id")
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
