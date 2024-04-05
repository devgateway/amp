package org.digijava.module.aim.dbentity;

import org.apache.commons.lang3.StringUtils;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

public class AmpGPINiSurveyResponse implements Serializable, Cloneable {

    private static final long serialVersionUID = -6656563271238273140L;
    
    private Long ampGPINiSurveyResponseId;
    private transient Long oldKey;
    private AmpGPINiSurvey ampGPINiSurvey;
    private AmpGPINiQuestion ampGPINiQuestion;
    private Long integerResponse;
    private String textResponse;
    private AmpGPINiQuestionOption questionOption;
    private Set<AmpGPINiSurveyResponseDocument> supportingDocuments;

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
