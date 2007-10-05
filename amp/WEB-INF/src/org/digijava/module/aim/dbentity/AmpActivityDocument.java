package org.digijava.module.aim.dbentity;
/**
 * 
 * @author Alex Gartner
 *
 */
public class AmpActivityDocument {
	private Long id;
	private String uuid;
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
	public String getUuid() {
		return uuid;
	}
	public void setUuid(String uuid) {
		this.uuid = uuid;
	}
}
