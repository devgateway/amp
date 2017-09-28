/*
 * Created on 1/03/2006
 * 
 * @author akashs
 * 
 */
package org.digijava.module.aim.dbentity;

import java.io.Serializable;
import java.util.Comparator;
import java.util.Set;

import org.digijava.module.aim.annotations.interchange.Interchangeable;

public class AmpGPISurveyQuestion implements Serializable {

    //IATI-check: to be ignored
//  @Interchangeable(fieldTitle="ID", id = true)
    private Long ampQuestionId;
//  @Interchangeable(fieldTitle="Question Text", value = true)
    private String questionText;
    
//  @Interchangeable(fieldTitle="Parent question", pickIdOnly=true)
    private AmpGPISurveyQuestion parentQuestion;
//  @Interchangeable(fieldTitle="Indicator", pickIdOnly=true)
    private AmpGPISurveyIndicator ampIndicatorId;
//  @Interchangeable(fieldTitle="Question Number")
    private Integer questionNumber;
//  @Interchangeable(fieldTitle="Question Type")
    private AmpGPISurveyQuestionType ampTypeId;
//  @Interchangeable(fieldTitle="Status")
    private String status;
//  @Interchangeable(fieldTitle="Questions", pickIdOnly=true)
    private Set<AmpGPISurveyQuestion> questions;

    public AmpGPISurveyQuestion getParentQuestion() {
        return parentQuestion;
    }

    public void setParentQuestion(AmpGPISurveyQuestion parentQuestion) {
        this.parentQuestion = parentQuestion;
    }

    public Set<AmpGPISurveyQuestion> getQuestions() {
        return questions;
    }

    public void setQuestions(Set<AmpGPISurveyQuestion> questions) {
        this.questions = questions;
    }

    public static class GPISurveyQuestionComparator implements Comparator<AmpGPISurveyQuestion>, Serializable {

        private static final long serialVersionUID = 1L;

        @Override
        public int compare(AmpGPISurveyQuestion arg0, AmpGPISurveyQuestion arg1) {
            if (arg0.getQuestionNumber() != null && arg1.getQuestionNumber() != null) {
                return arg0.getQuestionNumber().compareTo(arg1.getQuestionNumber());
            }
            return arg0.hashCode() - arg1.hashCode();
        }

    }

    public AmpGPISurveyIndicator getAmpIndicatorId() {
        return ampIndicatorId;
    }

    public void setAmpIndicatorId(AmpGPISurveyIndicator ampIndicatorId) {
        this.ampIndicatorId = ampIndicatorId;
    }

    public Long getAmpQuestionId() {
        return ampQuestionId;
    }

    public void setAmpQuestionId(Long ampQuestionId) {
        this.ampQuestionId = ampQuestionId;
    }

    public AmpGPISurveyQuestionType getAmpTypeId() {
        return ampTypeId;
    }

    public void setAmpTypeId(AmpGPISurveyQuestionType ampTypeId) {
        this.ampTypeId = ampTypeId;
    }

    public Integer getQuestionNumber() {
        return questionNumber;
    }

    public void setQuestionNumber(Integer questionNumber) {
        this.questionNumber = questionNumber;
    }

    public String getQuestionText() {
        return questionText;
    }

    public void setQuestionText(String questionText) {
        this.questionText = questionText;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

}
