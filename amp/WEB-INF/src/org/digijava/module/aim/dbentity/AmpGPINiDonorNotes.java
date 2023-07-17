package org.digijava.module.aim.dbentity;

import java.io.Serializable;
import java.util.Date;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIdentityReference;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import io.swagger.annotations.ApiModelProperty;
import org.digijava.kernel.ampapi.endpoints.serializers.ISO8601DateDeserializer;
import org.digijava.kernel.ampapi.endpoints.serializers.ISO8601DateSerializer;
import javax.persistence.*;

@Entity
@Table(name = "AMP_GPI_NI_DONOR_NOTES")
@JsonIgnoreProperties(ignoreUnknown = true)
public class AmpGPINiDonorNotes implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "AMP_GPI_NI_DONOR_NOTES_seq")
    @SequenceGenerator(name = "AMP_GPI_NI_DONOR_NOTES_seq", sequenceName = "AMP_GPI_NI_DONOR_NOTES_seq", allocationSize = 1)
    @Column(name = "amp_gpi_ni_donor_notes_id")
    @JsonProperty("id")
    private Long ampGPINiDonorNotesId;

    @Column(name = "notes", columnDefinition = "text")
    @NotNull
    @Size(min = 1)
    @ApiModelProperty(example = "Sample Note 1")
    private String notes;

    @Column(name = "notes_date")
    @JsonSerialize(using = ISO8601DateSerializer.class)
    @JsonDeserialize(using = ISO8601DateDeserializer.class)
    @ApiModelProperty(example = "2018-11-29")
    @NotNull
    private Date notesDate;

    @Column(name = "indicator_code")

    @NotNull
    @Size(min = 1)
    @ApiModelProperty(example = "1")
    private String indicatorCode;

    @ManyToOne
    @JoinColumn(name = "donor_id", nullable = false)
    @JsonProperty("donorId")
    @JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "ampOrgId",
            resolver = EntityResolver.class, scope = AmpOrganisation.class)
    @JsonIdentityReference(alwaysAsId = true)
    @NotNull
    @ApiModelProperty(value = "Donor Agency", example = "41")
    private AmpOrganisation donor;



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
