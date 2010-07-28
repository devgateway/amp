package org.digijava.module.aim.dbentity;

import org.digijava.module.aim.util.ContactInfoUtil;

public class AmpContactProperty {
	private Long id;
	private AmpContact contact;
	private String name;
	private String value;

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
	
}
