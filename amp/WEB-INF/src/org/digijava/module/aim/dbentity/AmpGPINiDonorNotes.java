package org.digijava.module.aim.dbentity;

import java.io.Serializable;

public class AmpGPINiDonorNotes implements Serializable {
	private static final long serialVersionUID = 7801819300013809821L;
	private Long ampGPINiDonorNotesId;
	private AmpOrganisation donorId;
	private String notes;

	public Long getAmpGPINiDonorNotesId() {
		return ampGPINiDonorNotesId;
	}

	public void setAmpGPINiDonorNotesId(Long ampGPINiDonorNotesId) {
		this.ampGPINiDonorNotesId = ampGPINiDonorNotesId;
	}

	public AmpOrganisation getDonorId() {
		return donorId;
	}

	public void setDonorId(AmpOrganisation donorId) {
		this.donorId = donorId;
	}

	public String getNotes() {
		return notes;
	}

	public void setNotes(String notes) {
		this.notes = notes;
	}
}
