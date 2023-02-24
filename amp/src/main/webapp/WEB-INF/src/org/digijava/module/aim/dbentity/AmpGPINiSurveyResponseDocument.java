package org.digijava.module.aim.dbentity;

import java.io.Serializable;

import org.digijava.module.contentrepository.helper.ObjectReferringDocument;

public class AmpGPINiSurveyResponseDocument extends ObjectReferringDocument implements Serializable, Cloneable {

    private static final long serialVersionUID = 1515092862391006968L;

    private Long id;
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
