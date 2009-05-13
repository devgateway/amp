package org.digijava.module.aim.dbentity;

import org.digijava.module.contentrepository.helper.ObjectReferringDocument;

/**
 * 
 * @author Alex Gartner
 *
 */
public class AmpActivityDocument extends ObjectReferringDocument {
	private Long id;
	private AmpActivity ampActivity;
	private String documentType;
	
	
	public String getDocumentType() {
		return documentType;
	}
	public void setDocumentType(String documentType) {
		this.documentType = documentType;
	}
	public AmpActivity getAmpActivity() {
		return ampActivity;
	}
	public void setAmpActivity(AmpActivity ampActivity) {
		this.ampActivity = ampActivity;
	}
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	
	
	@Override
	protected void detach() {
		ampActivity.getActivityDocuments().remove(this);
		this.ampActivity		= null;
	}
}
