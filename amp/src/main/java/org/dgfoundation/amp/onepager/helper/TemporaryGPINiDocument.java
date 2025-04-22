/**
 * Copyright (c) 2011 Development Gateway (www.developmentgateway.org)
 */
package org.dgfoundation.amp.onepager.helper;

import org.digijava.module.aim.dbentity.AmpGPINiSurveyResponse;
import org.digijava.module.aim.dbentity.AmpGPINiSurveyResponseDocument;

public class TemporaryGPINiDocument extends TemporaryDocument {
    
    private static final long serialVersionUID = 1L;
    
    private AmpGPINiSurveyResponse surveyResponse;

    @Override
    public AmpGPINiSurveyResponseDocument getExistingDocument() {
        return (AmpGPINiSurveyResponseDocument) this.existingDocument;
    }
    
    public void setExistingDocument(AmpGPINiSurveyResponseDocument existingDocument) {
        this.existingDocument = existingDocument;
    }

    public AmpGPINiSurveyResponse getSurveyResponse() {
        return surveyResponse;
    }

    public void setSurveyResponse(AmpGPINiSurveyResponse surveyResponse) {
        this.surveyResponse = surveyResponse;
    }
    
}
