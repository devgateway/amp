/*
 * Created on 8/03/2006
 * @author akashs
 *  
 */
package org.digijava.module.aim.form;

import org.apache.struts.action.ActionForm;

import java.util.Collection;

public class SurveyForm extends ActionForm {

    // Holds all the surveys across all the fundings for a particular activity 
    // in SurveyFunding helper class objects
    private Collection survey;
    
    private Long ampActivityId;
    private String tabIndex;
    
    /**
     * @return Returns the survey.
     */
    public Collection getSurvey() {
        return survey;
    }
    /**
     * @param survey The survey to set.
     */
    public void setSurvey(Collection survey) {
        this.survey = survey;
    }
    /**
     * @return Returns the ampActivityId.
     */
    public Long getAmpActivityId() {
        return ampActivityId;
    }
    /**
     * @param ampActivityId The ampActivityId to set.
     */
    public void setAmpActivityId(Long ampActivityId) {
        this.ampActivityId = ampActivityId;
    }
    /**
     * @return Returns the tabIndex.
     */
    public String getTabIndex() {
        return tabIndex;
    }
    /**
     * @param tabIndex The tabIndex to set.
     */
    public void setTabIndex(String tabIndex) {
        this.tabIndex = tabIndex;
    }
}
