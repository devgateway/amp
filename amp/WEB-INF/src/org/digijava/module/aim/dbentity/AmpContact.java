package org.digijava.module.aim.dbentity;

import java.util.Set;

import org.digijava.module.categorymanager.dbentity.AmpCategoryValue;

/**
 * holds contact user's information
 * @author Dare
 *
 */
public class AmpContact {
	private Long id;
	private String name;
	private String lastname;
	private AmpCategoryValue title;
	private String organisationName;
	private String function;
	private String officeaddress;
	private String temporaryId;
	
	/**
	 * currently these fields are not usable, but will become when we decide 
	 * to link contact list to calendar and messaging 
	 */
	private Boolean shared; //is contact shared between amp users
	private AmpTeamMember creator; //who created the contact
	
	private Set<AmpActivityContact> activityContacts;
	private Set<AmpOrganisationContact> organizationContacts;
	private Set<AmpContactProperty> properties;
    
	//private Set<AmpOrganisation> organizations;
    
    public AmpContact(){
    	
    }
    
    public AmpContact(String name, String lastName){
    	this.name=name;
    	this.lastname=lastName;
    }
	
	public Long getId() {
		return id;
	}
	public String getLastname() {
		return lastname;
	}
	public void setLastname(String lastname) {
		this.lastname = lastname;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}	
	public AmpCategoryValue getTitle() {
		return title;
	}
	public void setTitle(AmpCategoryValue title) {
		this.title = title;
	}
	public String getOrganisationName() {
		return organisationName;
	}
	public void setOrganisationName(String organisationName) {
		this.organisationName = organisationName;
	}
	public Boolean getShared() {
		return shared;
	}
	public void setShared(Boolean shared) {
		this.shared = shared;
	}
	public AmpTeamMember getCreator() {
		return creator;
	}
	public void setCreator(AmpTeamMember creator) {
		this.creator = creator;
	}

	public Set<AmpActivityContact> getActivityContacts() {
		return activityContacts;
	}
	public void setActivityContacts(Set<AmpActivityContact> activityContacts) {
		this.activityContacts = activityContacts;
	}
	public String getTemporaryId() {
		return temporaryId;
	}
	public void setTemporaryId(String temporaryId) {
		this.temporaryId = temporaryId;
	}
	
	public String getFunction() {
		return function;
	}
	public void setFunction(String function) {
		this.function = function;
	}
	public String getOfficeaddress() {
		return officeaddress;
	}
	public void setOfficeaddress(String officeaddress) {
		this.officeaddress = officeaddress;
	}
	public Set<AmpContactProperty> getProperties() {
		return properties;
	}
	public void setProperties(Set<AmpContactProperty> properties) {
		this.properties = properties;
	}

	public Set<AmpOrganisationContact> getOrganizationContacts() {
		return organizationContacts;
	}

	public void setOrganizationContacts(Set<AmpOrganisationContact> organizationContacts) {
		this.organizationContacts = organizationContacts;
	}	
	
}