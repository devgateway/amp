package org.digijava.module.aim.dbentity;

public class AmpActivityContact {
	
	private Long id;
	private AmpActivity activity;
	private AmpContact contact;
	private Boolean primaryContact;
	private String contactType; // Donor/MOFED funding Contact Information, Project Coordinator Contact Information,Sector Ministry Contact Information
	
	public AmpActivity getActivity() {
		return activity;
	}
	public void setActivity(AmpActivity activity) {
		this.activity = activity;
	}
	public AmpContact getContact() {
		return contact;
	}
	public void setContact(AmpContact contact) {
		this.contact = contact;
	}
	public Boolean getPrimaryContact() {
		return primaryContact;
	}
	public void setPrimaryContact(Boolean primaryContact) {
		this.primaryContact = primaryContact;
	}
	public String getContactType() {
		return contactType;
	}
	public void setContactType(String contactType) {
		this.contactType = contactType;
	}
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}	
}
