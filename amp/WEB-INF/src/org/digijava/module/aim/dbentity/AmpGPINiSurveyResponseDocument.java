package org.digijava.module.aim.dbentity;

import java.io.Serializable;

import org.digijava.module.contentrepository.helper.ObjectReferringDocument;
import javax.persistence.*;

@Entity
@Table(name = "AMP_GPI_NI_SURVEY_RESPONSE_DOCUMENT")
public class AmpGPINiSurveyResponseDocument extends ObjectReferringDocument implements Serializable, Cloneable {

    private static final long serialVersionUID = 1515092862391006968L;
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "AMP_GPI_NI_SURVEY_RESPONSE_DOCUMENT_seq")
    @SequenceGenerator(name = "AMP_GPI_NI_SURVEY_RESPONSE_DOCUMENT_seq", sequenceName = "AMP_GPI_NI_SURVEY_RESPONSE_DOCUMENT_seq", allocationSize = 1)
    @Column(name = "amp_gpi_ni_survey_response_document_id")
    private Long id;

    @Column(name = "uuid")
    private String uuid;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "amp_gpi_ni_survey_response_id", nullable = false)
    private AmpGPINiSurveyResponse surveyResponse;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public AmpGPINiSurveyResponse getSurveyResponse() {
        return surveyResponse;
    }

    public void setSurveyResponse(AmpGPINiSurveyResponse surveyResponse) {
        this.surveyResponse = surveyResponse;
    }

    @Override
    protected void detach() {
        surveyResponse.getSupportingDocuments().remove(this);
        this.surveyResponse = null;
    }

    @Override
    public Object clone() throws CloneNotSupportedException {
        return super.clone();
    }
    
}
