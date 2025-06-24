package org.digijava.module.aim.dbentity;

import org.apache.commons.collections.ComparatorUtils;

import java.io.Serializable;
import java.util.Date;
import java.util.Set;

public class AmpGPINiSurvey implements Serializable, Cloneable, Comparable<AmpGPINiSurvey> {
    
    private static final long serialVersionUID = -4889980304099658852L;
    
    private Long ampGPINiSurveyId;
    
    private Date surveyDate;
    
    private AmpOrgRole ampOrgRole;
    
    private Set<AmpGPINiSurveyResponse> responses;

    public Long getAmpGPINiSurveyId() {
        return ampGPINiSurveyId;
    }

    public void setAmpGPINiSurveyId(Long ampGPINiSurveyId) {
        this.ampGPINiSurveyId = ampGPINiSurveyId;
    }

    public Date getSurveyDate() {
        return surveyDate;
    }

    public void setSurveyDate(Date surveyDate) {
        this.surveyDate = surveyDate;
    }

    public AmpOrgRole getAmpOrgRole() {
        return ampOrgRole;
    }

    public void setAmpOrgRole(AmpOrgRole ampOrgRole) {
        this.ampOrgRole = ampOrgRole;
    }

    public Set<AmpGPINiSurveyResponse> getResponses() {
        return responses;
    }

    public void setResponses(Set<AmpGPINiSurveyResponse> responses) {
        this.responses = responses;
    }

    @Override
    public int compareTo(AmpGPINiSurvey o) {
        return ComparatorUtils.nullLowComparator(null).compare(getAmpOrgRole(), o.getAmpOrgRole());
    }
    
    @Override
    public Object clone() throws CloneNotSupportedException {
        // TODO Auto-generated method stub
        return super.clone();
    }
    
}
