package org.digijava.module.aim.dbentity;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;
import javax.persistence.*;
import java.util.Set;

@Entity
@Table(name = "AMP_GPI_NI_SURVEY_RESPONSE")
public class AmpGPINiSurveyResponse implements Serializable, Cloneable {

    private static final long serialVersionUID = -6656563271238273140L;
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "AMP_GPI_NI_SURVEY_RESPONSE_seq")
    @SequenceGenerator(name = "AMP_GPI_NI_SURVEY_RESPONSE_seq", sequenceName = "AMP_GPI_NI_SURVEY_RESPONSE_seq", allocationSize = 1)
    @Column(name = "amp_gpi_ni_survey_response_id")
    private Long ampGPINiSurveyResponseId;

    @Column(name = "integer_response")
    private Long integerResponse;

    @Column(name = "text_response", columnDefinition = "text")
    private String textResponse;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "question_option_id")
    private AmpGPINiQuestionOption questionOption;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "amp_gpi_ni_survey_id", nullable = false)
    private AmpGPINiSurvey ampGPINiSurvey;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "amp_gpi_ni_question_id", nullable = false)
    private AmpGPINiQuestion ampGPINiQuestion;

    @OneToMany(mappedBy = "ampGPINiSurveyResponse", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<AmpGPINiSurveyResponseDocument> supportingDocuments;
    private transient Long oldKey;


    public Long getAmpGPINiSurveyResponseId() {
        return ampGPINiSurveyResponseId;
    }

    public void setAmpGPINiSurveyResponseId(Long ampGPINiSurveyResponseId) {
        this.ampGPINiSurveyResponseId = ampGPINiSurveyResponseId;
    }

    public AmpGPINiSurvey getAmpGPINiSurvey() {
        return ampGPINiSurvey;
    }

    public void setAmpGPINiSurvey(AmpGPINiSurvey ampGPINiSurvey) {
        this.ampGPINiSurvey = ampGPINiSurvey;
    }

    public AmpGPINiQuestion getAmpGPINiQuestion() {
        return ampGPINiQuestion;
    }

    public void setAmpGPINiQuestion(AmpGPINiQuestion ampGPINiQuestion) {
        this.ampGPINiQuestion = ampGPINiQuestion;
    }

    public Long getIntegerResponse() {
        return integerResponse;
    }

    public void setIntegerResponse(Long integerResponse) {
        this.integerResponse = integerResponse;
    }

    public String getTextResponse() {
        return textResponse;
    }

    public void setTextResponse(String textResponse) {
        this.textResponse = textResponse;
    }

    public AmpGPINiQuestionOption getQuestionOption() {
        return questionOption;
    }

    public void setQuestionOption(AmpGPINiQuestionOption questionOption) {
        this.questionOption = questionOption;
    }

    public Long getOldKey() {
        return oldKey;
    }

    public void setOldKey(Long oldKey) {
        this.oldKey = oldKey;
    }

    public Object clone() throws CloneNotSupportedException {
        AmpGPINiSurveyResponse clonedResponse = new AmpGPINiSurveyResponse();
        clonedResponse.setAmpGPINiSurveyResponseId(null);
        clonedResponse.setOldKey(this.ampGPINiSurveyResponseId);
        clonedResponse.setAmpGPINiQuestion(this.ampGPINiQuestion);
        clonedResponse.setTextResponse(this.textResponse);
        clonedResponse.setIntegerResponse(this.integerResponse);
        clonedResponse.setQuestionOption(this.questionOption);
        if (this.supportingDocuments != null && this.supportingDocuments.size() > 0) {
            final Set<AmpGPINiSurveyResponseDocument> clDocuments = new HashSet<AmpGPINiSurveyResponseDocument>();
            this.supportingDocuments.forEach(d -> {
                try {
                    AmpGPINiSurveyResponseDocument clDoc = (AmpGPINiSurveyResponseDocument) d.clone();
                    clDoc.setId(null);
                    clDoc.setSurveyResponse(clonedResponse);
                    clDocuments.add(clDoc);
                } catch (CloneNotSupportedException e) {
                    throw new RuntimeException(e);
                }
            });
            if (clonedResponse.getSupportingDocuments() == null) {
                clonedResponse.setSupportingDocuments(new HashSet<AmpGPINiSurveyResponseDocument>());
            }
            clonedResponse.getSupportingDocuments().clear();
            clonedResponse.getSupportingDocuments().addAll(clDocuments);
        }
        
        return clonedResponse;
    };
    
    public Set<AmpGPINiSurveyResponseDocument> getSupportingDocuments() {
        return supportingDocuments;
    }

    public void setSupportingDocuments(Set<AmpGPINiSurveyResponseDocument> supportingDocuments) {
        this.supportingDocuments = supportingDocuments;
    }

    public boolean isEmpty() {
        if (ampGPINiQuestion != null) {
            switch(ampGPINiQuestion.getType()) {
                case INTEGER:
                    return integerResponse == null;
                case DECIMAL:
                case FREE_TEXT:
                    return StringUtils.isBlank(textResponse);
                case LINK:
                case DOCUMENT:
                    return supportingDocuments == null || supportingDocuments.isEmpty();
                case SINGLE_CHOICE:
                case MULTIPLE_CHOICE:
                    return questionOption == null;
                
                default:
                    return StringUtils.isBlank(textResponse);
            }
        }
        
        return false;
    }
    
}
