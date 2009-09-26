package org.digijava.module.aim.dbentity;

import java.io.Serializable;

public class AmpActivityInternalId implements Serializable {

	private static final long serialVersionUID = 469552292854192522L;
	private Long id;
	private AmpOrganisation organisation;
	private AmpActivity ampActivity;
	private String internalId;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public AmpOrganisation getOrganisation() {
		return organisation;
	}

	public void setOrganisation(AmpOrganisation organisation) {
		this.organisation = organisation;
	}

	public AmpActivity getAmpActivity() {
		return ampActivity;
	}

	public void setAmpActivity(AmpActivity ampActivity) {
		this.ampActivity = ampActivity;
	}

	public String getInternalId() {
		return internalId;
	}

	public void setInternalId(String internalId) {
		this.internalId = internalId;
	}

}
