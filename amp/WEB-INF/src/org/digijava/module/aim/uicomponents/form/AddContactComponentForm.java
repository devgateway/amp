package org.digijava.module.aim.uicomponents.form;

import java.util.List;
import org.apache.struts.action.ActionForm;
import org.digijava.module.aim.dbentity.AmpContact;

public class AddContactComponentForm extends ActionForm {

    private static final long serialVersionUID = 1L;
    private Long[] selContactIds;
    private List<AmpContact> contacts;
    private String name;
    private String lastname;
    private String email;
    private Long title;
    private String organisationName;
    private String phone;
    private String fax;
    private String function;
    private String mobilephone;
    private String officeaddress;
    private String keyword;
    private String action;
    private String targetCollection = "";
    private Object targetForm;
    private Long contactId;

    public Long getContactId() {
        return contactId;
    }

    public void setContactId(Long contactId) {
        this.contactId = contactId;
    }
  

    public String getAction() {
        return action;
    }

    public String getTargetCollection() {
        return targetCollection;
    }

    public void setTargetCollection(String targetCollection) {
        this.targetCollection = targetCollection;
    }

    public Object getTargetForm() {
        return targetForm;
    }

    public void setTargetForm(Object targetForm) {
        this.targetForm = targetForm;
    }

    public void setAction(String action) {
        this.action = action;
    }
 

    public String getKeyword() {
        return keyword;
    }

    public void setKeyword(String keyword) {
        this.keyword = keyword;
    }

  

    public List<AmpContact> getContacts() {
        return contacts;
    }

    public void setContacts(List<AmpContact> contacts) {
        this.contacts = contacts;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFax() {
        return fax;
    }

    public void setFax(String fax) {
        this.fax = fax;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getOrganisationName() {
        return organisationName;
    }

    public void setOrganisationName(String organisationName) {
        this.organisationName = organisationName;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public Long[] getSelContactIds() {
        return selContactIds;
    }

    public void setSelContactIds(Long[] selContactIds) {
        this.selContactIds = selContactIds;
    }

    public String getTemporaryId() {
        return temporaryId;
    }

    public void setTemporaryId(String temporaryId) {
        this.temporaryId = temporaryId;
    }

    public Long getTitle() {
        return title;
    }

    public void setTitle(Long title) {
        this.title = title;
    }
    
    public String getFunction() {
		return function;
	}

	public void setFunction(String function) {
		this.function = function;
	}

	public String getMobilephone() {
		return mobilephone;
	}

	public void setMobilephone(String mobilephone) {
		this.mobilephone = mobilephone;
	}

	public String getOfficeaddress() {
		return officeaddress;
	}

	public void setOfficeaddress(String officeaddress) {
		this.officeaddress = officeaddress;
	}

	private String temporaryId; //contact's temporary id
}
