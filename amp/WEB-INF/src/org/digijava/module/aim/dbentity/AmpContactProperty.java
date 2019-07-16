package org.digijava.module.aim.dbentity;

import java.io.Serializable;

import org.digijava.kernel.ampapi.endpoints.activity.ActivityEPConstants;
import org.digijava.kernel.ampapi.endpoints.contact.ContactEPConstants;
import org.digijava.kernel.ampapi.endpoints.contact.ContactPhoneTypePossibleValuesProvider;
import org.digijava.kernel.ampapi.endpoints.contact.PhoneDiscriminatorContextMatcher;
import org.digijava.module.aim.annotations.interchange.Interchangeable;
import org.digijava.module.aim.annotations.interchange.PossibleValues;
import org.digijava.module.aim.annotations.interchange.RegexDiscriminator;
import org.digijava.module.aim.annotations.translation.TranslatableClass;
import org.digijava.module.aim.util.ContactInfoUtil;
import org.digijava.module.categorymanager.dbentity.AmpCategoryValue;

@TranslatableClass (displayName = "Contact Property")
public class AmpContactProperty  implements Comparable, Serializable {
    
    private Long id;
    private AmpContact contact;

    private String name;
    
    @Interchangeable(fieldTitle = "Value", required = ActivityEPConstants.REQUIRED_ALWAYS, importable = true, 
            regexPatterns = {
                    @RegexDiscriminator(parent = ContactEPConstants.EMAIL, 
                            regexPattern = ActivityEPConstants.REGEX_PATTERN_EMAIL),
                    @RegexDiscriminator(parent = ContactEPConstants.PHONE, 
                            regexPattern = ActivityEPConstants.REGEX_PATTERN_PHONE),
                    @RegexDiscriminator(parent = ContactEPConstants.FAX, 
                            regexPattern = ActivityEPConstants.REGEX_PATTERN_PHONE)
                    })
    private String value;
    
    @Interchangeable(fieldTitle = "Extension Value", importable = true, 
            context = PhoneDiscriminatorContextMatcher.class, 
            regexPattern = ActivityEPConstants.REGEX_PATTERN_PHONE_EXTENSION)
    private String extensionValue;

    @PossibleValues(ContactPhoneTypePossibleValuesProvider.class)
    @Interchangeable(fieldTitle = "Type", importable = true, pickIdOnly = true,
            context = PhoneDiscriminatorContextMatcher.class)
    private AmpCategoryValue type;

    public AmpCategoryValue getType() {
        return type;
    }

    public void setType(AmpCategoryValue type) {
        this.type = type;
    }

    public String getPhoneCategory() {
        if (type != null) {
            return type.getValue();
        } else {
            return "None";
        }
    }
    
    public String getValueAsFormatedPhoneNum () {
        return ContactInfoUtil.getFormattedPhoneNum(type, value);
    }

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
    
    public String getValue() {
        return value;
    }
    
    public void setValue(String value) {
        this.value = value;
    }
    
    public String getExtensionValue() {
        return extensionValue;
    }

    public void setExtensionValue(String extensionValue) {
        this.extensionValue = extensionValue;
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
