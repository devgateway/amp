package org.digijava.module.aim.dbentity;

import java.io.Serializable;
import java.util.ArrayList;

import org.digijava.module.aim.util.Output;

public class AmpActivityContact implements Versionable, Comparable, Serializable, Cloneable {
	
	private Long id;
	private AmpActivityVersion activity;
	private AmpContact contact;
	private Boolean primaryContact;
	private String contactType; // Donor/MOFED funding,Project Coordinator,Sector Ministry or Implementing/Executing Agency Contact Information
	
	public AmpActivityVersion getActivity() {
		return activity;
	}
	public void setActivity(AmpActivityVersion activity) {
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
	
	@Override
	public boolean equalsForVersioning(Object obj) {
		AmpActivityContact aux = (AmpActivityContact) obj;
		String original = "" + this.contact.getId();
		String copy = "" + aux.contact.getId();
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
				new Output(null, new String[] { "Contact:&nbsp;" }, new Object[] { this.contact.getLastname() + " "
						+ this.contact.getName() }));
		return out;
	}

	@Override
	public Object getValue() {
		return "" + this.contactType + "-" + this.primaryContact;
	}
	@Override
	public int compareTo(Object o) {
		// TODO Auto-generated method stub
		if (!(o instanceof AmpActivityContact)) return -1;
		AmpActivityContact aac = (AmpActivityContact)o;
		int result=this.getContact().compareTo(aac.getContact());
		if(result==0){
			result=this.activity.compareTo(aac.getActivity());
		}
		if(result==0){
			result=this.contactType.compareTo(aac.getContactType());
		}
		return result;
	}
	
	@Override
	public Object prepareMerge(AmpActivityVersion newActivity) throws CloneNotSupportedException {
		AmpActivityContact aux = (AmpActivityContact) clone();
		aux.activity = newActivity;
		aux.id = null;
		//this.contact = (AmpContact) this.contact.clone();
		this.contact.getActivityContacts().add(aux);
		return aux;
	}

	@Override
	public Object clone() throws CloneNotSupportedException {
		// TODO Auto-generated method stub
		return super.clone();
	}
}