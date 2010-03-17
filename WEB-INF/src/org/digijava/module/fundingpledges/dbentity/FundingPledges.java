package org.digijava.module.fundingpledges.dbentity;

import java.util.Set;

import org.digijava.module.aim.dbentity.AmpCurrency;
import org.digijava.module.aim.dbentity.AmpLocation;
import org.digijava.module.aim.dbentity.AmpOrganisation;
import org.digijava.module.aim.dbentity.AmpSector;
import org.digijava.module.categorymanager.dbentity.AmpCategoryValue;

/**
 * 
 * @author Diego Dimunzio
 * 
 */
public class FundingPledges {
	private long id;
	private String title;
	private AmpOrganisation organization;
	private Set<AmpSector> sectorlist;
	private Set<AmpLocation> locationlist;
	
	// "Point of Contact at Donors Conference on March 31st"
	private String Name; 
	private String Address; 
	private String Email; 
	private String Title; 
	private String Ministry; 
	private String Telephone; 
	private String Fax;
	private AmpOrganisation contactorganization;
	
	//"is Point of Contact for Follow Up"
	 
	private String Name_1; 
	private String Address_1; 
	private String Email_1; 
	private String Title_1; 
	private String Ministry_1; 
	private String Telephone_1; 
	private String Fax_1;
 	private AmpOrganisation contactorganization_1;
	
	public String getName() {
		return Name;
	}
	public void setName(String name) {
		Name = name;
	}
	public String getAddress() {
		return Address;
	}
	public void setAddress(String address) {
		Address = address;
	}
	public String getEmail() {
		return Email;
	}
	public void setEmail(String email) {
		Email = email;
	}
	public String getMinistry() {
		return Ministry;
	}
	public void setMinistry(String ministry) {
		Ministry = ministry;
	}
	public String getTelephone() {
		return Telephone;
	}
	public void setTelephone(String telephone) {
		Telephone = telephone;
	}
	public String getFax() {
		return Fax;
	}
	public void setFax(String fax) {
		Fax = fax;
	}
	public String getName_1() {
		return Name_1;
	}
	public void setName_1(String name_1) {
		Name_1 = name_1;
	}
	public String getAddress_1() {
		return Address_1;
	}
	public void setAddress_1(String address_1) {
		Address_1 = address_1;
	}
	public String getEmail_1() {
		return Email_1;
	}
	public void setEmail_1(String email_1) {
		Email_1 = email_1;
	}
	public String getTitle_1() {
		return Title_1;
	}
	public void setTitle_1(String title_1) {
		Title_1 = title_1;
	}
	public String getMinistry_1() {
		return Ministry_1;
	}
	public void setMinistry_1(String ministry_1) {
		Ministry_1 = ministry_1;
	}
	public String getTelephone_1() {
		return Telephone_1;
	}
	public void setTelephone_1(String telephone_1) {
		Telephone_1 = telephone_1;
	}
	public String getFax_1() {
		return Fax_1;
	}
	public void setFax_1(String fax_1) {
		Fax_1 = fax_1;
	}
	public AmpOrganisation getContactorganization() {
		return contactorganization;
	}
	public void setContactorganization(AmpOrganisation contactorganization) {
		this.contactorganization = contactorganization;
	}
	public AmpOrganisation getContactorganization_1() {
		return contactorganization_1;
	}
	public void setContactorganization_1(AmpOrganisation contactorganization_1) {
		this.contactorganization_1 = contactorganization_1;
	}
	
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public AmpOrganisation getOrganization() {
		return organization;
	}
	public void setOrganization(AmpOrganisation organization) {
		this.organization = organization;
	}
	public Set<AmpSector> getSectorlist() {
		return sectorlist;
	}
	public void setSectorlist(Set<AmpSector> sectorlist) {
		this.sectorlist = sectorlist;
	}
	public Set<AmpLocation> getLocationlist() {
		return locationlist;
	}
	public void setLocationlist(Set<AmpLocation> locationlist) {
		this.locationlist = locationlist;
	}
	
}