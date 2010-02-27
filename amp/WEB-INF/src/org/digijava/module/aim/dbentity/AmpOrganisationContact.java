package org.digijava.module.aim.dbentity;

public class AmpOrganisationContact {
	
	private Long id;
	private AmpContact contact;
	private AmpOrganisation organisation;
	private Boolean primaryContact;
	
	public AmpOrganisationContact(){
		
	}
	
	public AmpOrganisationContact(AmpOrganisation organisation, AmpContact contact){
		this.organisation=organisation;
		this.contact=contact;
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
	public AmpOrganisation getOrganisation() {
		return organisation;
	}
	public void setOrganisation(AmpOrganisation organisation) {
		this.organisation = organisation;
	}
	public Boolean getPrimaryContact() {
		return primaryContact;
	}
	public void setPrimaryContact(Boolean primaryContact) {
		this.primaryContact = primaryContact;
	}
	
}
