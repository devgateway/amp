package org.digijava.module.aim.dbentity;

import java.io.Serializable;

import org.digijava.module.aim.annotations.interchange.Interchangeable;
import org.digijava.module.aim.annotations.translation.TranslatableClass;
import org.digijava.module.aim.util.ContactInfoUtil;
import org.digijava.module.categorymanager.dbentity.AmpCategoryValue;

@TranslatableClass (displayName = "Contact Property")
public class AmpContactProperty  implements Comparable, Serializable {
	
	private Long id;
	private AmpContact contact;
	
	@Interchangeable(fieldTitle = "Name")
	private String name;
	
	@Interchangeable(fieldTitle = "Value")
	private String value;
	
	@Interchangeable(fieldTitle = "Extension Value")
	private String extensionValue;
        /*this value is not saved in db, let's hope
         somebody will refactor phone and use this*/
    
	private AmpCategoryValue categoryValue;

    public String getActualValue() {
        return actualValue;
    }

    public void setActualValue(String actualValue) {
        this.actualValue = actualValue;
    }
        //dummy variable
        private String actualValue;

    public AmpCategoryValue getCategoryValue() {
        return categoryValue;
    }

    public void setCategoryValue(AmpCategoryValue categoryValue) {
        this.categoryValue = categoryValue;
    }

    public String getActualPhoneNumber() {
        return ContactInfoUtil.getActualPhoneNumber(value);
    }

    public String getPhoneCategory() {
        return ContactInfoUtil.getPhoneCategory(value);
    }
    
    public String getValueAsFormatedPhoneNum () {
        return ContactInfoUtil.getFormatedPhoneNum(value);
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
				} else {
					if (this.getActualValue() != null
							&& a.getActualValue() != null) {
						if (this.getActualValue().equals(this.getActualValue())) {
							return 0;
						} 
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

        if (id != null ? !id.equals(that.id) : that.id != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return id != null ? id.hashCode() : 0;
    }
}
