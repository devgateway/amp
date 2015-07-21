package org.digijava.module.aim.dbentity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import org.digijava.module.aim.annotations.interchange.Interchangeable;
import org.digijava.module.aim.annotations.translation.TranslatableClass;
import org.digijava.module.aim.annotations.translation.TranslatableField;
import org.digijava.module.aim.helper.Constants;
import org.digijava.module.aim.util.Output;
import org.digijava.module.categorymanager.dbentity.AmpCategoryValue;

/**
 * holds contact user's information
 * @author Dare
 *
 */
@TranslatableClass (displayName = "Contact")
public class AmpContact implements Comparable, Serializable, Cloneable, Versionable {
	@Interchangeable(fieldTitle="ID", id=true)
	private Long id;
	@Interchangeable(fieldTitle="Name", value=true)
	private String name;
	@Interchangeable(fieldTitle="Last Name")
	private String lastname;
	@Interchangeable(fieldTitle="Title")
	private AmpCategoryValue title;

	@TranslatableField
	@Interchangeable(fieldTitle="Organization Name")
	private String organisationName;
	@TranslatableField
	@Interchangeable(fieldTitle="Function")
	private String function;
	@Interchangeable(fieldTitle="Office Address")
	private String officeaddress;
	@Interchangeable(fieldTitle="Temporary ID")
	private String temporaryId;
	
	@Interchangeable(fieldTitle="Name and Last Name")
	private String nameAndLastName;
	@Interchangeable(fieldTitle="Full Name")
	private String fullname;
	

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

	@Override
	 public int compareTo(Object arg0) {
        // TODO Auto-generated method stub
        AmpContact a = (AmpContact) arg0;
        if (this.getId() != null && a.getId() != null) {
            return this.getId().compareTo(a.getId());
        } else {
        	if(this.getTemporaryId()!=null&&a.getTemporaryId()!=null){
        		return this.getTemporaryId().compareTo(a.getTemporaryId());
        	}
            return 1;
        }
    }
	
	public String getNameAndLastName() {
		nameAndLastName = (name != null) ? name + " " + lastname : name;
		return nameAndLastName;
	    }

	public void setNameAndLastName(String nameAndLastName) {
		this.nameAndLastName = nameAndLastName;
	}

	public String getFullname() {
		return fullname;
	}

	public void setFullname(String fullname) {
		this.fullname = fullname;
	}

	@Override
	public Object clone() throws CloneNotSupportedException {
		return super.clone();
	}


	public List<String> getEmails (){
		List<String> emails =null;
		if (this.properties!= null ) {
			for (AmpContactProperty prop : this.properties) {
				if (prop.getName().equals(Constants.CONTACT_PROPERTY_NAME_EMAIL)){
					if(emails ==null){
						emails= new ArrayList<String>();
					}
					emails.add(prop.getValue());
				}
				
			}
		}		
		return emails;
	}
	
	@Override
	public boolean equalsForVersioning(Object obj) {
		AmpContact aux = (AmpContact) obj;
		String original = "" + this.getId();
		String copy = "" + aux.getId();
		if (original.equals(copy)) {
			return true;
		}
		return false;
	}

	@Override
	public Output getOutput() {
		Output out = new Output();
		out.setOutputs(new ArrayList<Output>());
		out.getOutputs().add(
				new Output(null, new String[] { "Contact" }, new Object[] { this.getLastname() + " "
						+ this.getName() }));
		return out;
	}

	@Override
	public Object getValue() {
		return "" + this.name + "-" + this.lastname;
	}
	
	@Override
	public Object prepareMerge(AmpActivityVersion newActivity) throws CloneNotSupportedException {
		AmpContact aux = (AmpContact) clone();
		aux.id = null;
        if(this.getActivityContacts()==null){
            this.setActivityContacts(new TreeSet<AmpActivityContact>());
        }
		return aux;
	}


}