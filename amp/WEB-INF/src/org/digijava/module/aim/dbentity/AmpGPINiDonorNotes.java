package org.digijava.module.aim.dbentity;

import java.io.Serializable;
import java.util.Date;

public class AmpGPINiDonorNotes implements Serializable {
    
    private static final long serialVersionUID = 7801819300013809821L;
    
    private Long ampGPINiDonorNotesId;
    private AmpOrganisation donor;
    private String notes;
    private Date notesDate;
    private String indicatorCode;

    public Long getAmpGPINiDonorNotesId() {
        return ampGPINiDonorNotesId;
    }

    public void setAmpGPINiDonorNotesId(Long ampGPINiDonorNotesId) {
        this.ampGPINiDonorNotesId = ampGPINiDonorNotesId;
    }

    public AmpOrganisation getDonor() {
        return donor;
    }

    public void setDonor(AmpOrganisation donor) {
        this.donor = donor;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public Date getNotesDate() {
        return notesDate;
    }

    public void setNotesDate(Date notesDate) {
        this.notesDate = notesDate;
    }

    public String getIndicatorCode() {
        return indicatorCode;
    }

    public void setIndicatorCode(String indicatorCode) {
        this.indicatorCode = indicatorCode;
    }
    
}
