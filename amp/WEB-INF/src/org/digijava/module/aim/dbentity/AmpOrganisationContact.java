package org.digijava.module.aim.dbentity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.digijava.module.aim.helper.donorReport.OrgProfileValue;
import org.digijava.module.aim.helper.donorReport.ValueTranslatabePair;

import java.util.Arrays;

public class AmpOrganisationContact implements Serializable,OrgProfileValue {
	
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
	@Override
	public List<ValueTranslatabePair> getValuesForOrgReport(){
    	List<ValueTranslatabePair> values=new ArrayList<ValueTranslatabePair>();
    	AmpContact contact=getContact();
    	values.add(new ValueTranslatabePair(Arrays.asList(new String[]{contact.getName()}),false));
    	values.add(new ValueTranslatabePair(Arrays.asList(new String[]{contact.getLastname()}),false));
    	List<String> emails=contact.getEmails();
    	values.add(new ValueTranslatabePair(emails,false));
    	return values;
    }

	@Override
	public String[] getSubHeaders() {
		String[] subHeaders={"First Name","Last Name","E-mail"};
		return subHeaders;
	}
	
}
