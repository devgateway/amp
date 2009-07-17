package org.digijava.module.aim.form ;

import java.util.List;


public class ReferenceForm extends MainProjectDetailsForm
{
	private Long id ;
	private List referenceDocs;
	private boolean validLogin;
	private Long[] allReferenceDocNameIds;
	
	
	public Long[] getAllReferenceDocNameIds() {
		return allReferenceDocNameIds;
	}
	public void setAllReferenceDocNameIds(Long[] allReferenceDocNameIds) {
		this.allReferenceDocNameIds = allReferenceDocNameIds;
	}
	public boolean isValidLogin() {
		return validLogin;
	}
	public void setValidLogin(boolean validLogin) {
		this.validLogin = validLogin;
	}
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public List getReferenceDocs() {
		return referenceDocs;
	}
	public void setReferenceDocs(List referenceDocs) {
		this.referenceDocs = referenceDocs;
	}
}