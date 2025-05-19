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

public class AmpAhsurveyQuestion implements Serializable{
//IATI-check: to be ignored
//  @Interchangeable(fieldTitle="Question ID", id = true, importable = true)
    private Long ampQuestionId;
//  @Interchangeable(fieldTitle="Question Text", value = true)
    private String questionText;
//  @Interchangeable(fieldTitle="Parent Question", pickIdOnly=true)
    private AmpAhsurveyQuestion parentQuestion;
//  @Interchangeable(fieldTitle="Indicator ID")
    private AmpAhsurveyIndicator ampIndicatorId;
//  @Interchangeable(fieldTitle="Question Number")
    private Integer questionNumber;
//  @Interchangeable(fieldTitle="Type ID")
    private AmpAhsurveyQuestionType ampTypeId;
//  @Interchangeable(fieldTitle="Status")
    private String status;
//  @Interchangeable(fieldTitle="Questions", pickIdOnly=true)
    private Set<AmpAhsurveyQuestion> questions;
    

    public AmpAhsurveyQuestion getParentQuestion() {
        return parentQuestion;
    }
    public void setParentQuestion(AmpAhsurveyQuestion parentQuestion) {
        this.parentQuestion = parentQuestion;
    }
    public Set<AmpAhsurveyQuestion> getQuestions() {
        return questions;
    }
    public void setQuestions(Set<AmpAhsurveyQuestion> questions) {
        this.questions = questions;
    }
    public static class AhsurveyQuestionComparator implements Comparator<AmpAhsurveyQuestion>, Serializable {

        /**
         * 
         */
        private static final long serialVersionUID = 1L;

        @Override
        public int compare(AmpAhsurveyQuestion arg0, AmpAhsurveyQuestion arg1) {
            if (arg0.getQuestionNumber() != null && arg1.getQuestionNumber() != null){
                return arg0.getQuestionNumber().compareTo(arg1.getQuestionNumber());
            }
            return arg0.hashCode()-arg1.hashCode();
        }
        
    }
    
    /**
     * @return Returns the ampIndicatorId.
     */
    public AmpAhsurveyIndicator getAmpIndicatorId() {
        return ampIndicatorId;
    }
    /**
     * @param ampIndicatorId The ampIndicatorId to set.
     */
    public void setAmpIndicatorId(AmpAhsurveyIndicator ampIndicatorId) {
        this.ampIndicatorId = ampIndicatorId;
    }
    /**
     * @return Returns the ampQuestionId.
     */
    public Long getAmpQuestionId() {
        return ampQuestionId;
    }
    /**
     * @param ampQuestionId The ampQuestionId to set.
     */
    public void setAmpQuestionId(Long ampQuestionId) {
        this.ampQuestionId = ampQuestionId;
    }
    /**
     * @return Returns the ampTypeId.
     */
    public AmpAhsurveyQuestionType getAmpTypeId() {
        return ampTypeId;
    }
    /**
     * @param ampTypeId The ampTypeId to set.
     */
    public void setAmpTypeId(AmpAhsurveyQuestionType ampTypeId) {
        this.ampTypeId = ampTypeId;
    }
    /**
     * @return Returns the questionNumber.
     */
    public Integer getQuestionNumber() {
        return questionNumber;
    }
    /**
     * @param questionNumber The questionNumber to set.
     */
    public void setQuestionNumber(Integer questionNumber) {
        this.questionNumber = questionNumber;
    }
    /**
     * @return Returns the questionText.
     */
    public String getQuestionText() {
        return questionText;
    }
    /**
     * @param questionText The questionText to set.
     */
    public void setQuestionText(String questionText) {
        this.questionText = questionText;
    }
    /**
     * @return Returns the status.
     */
    public String getStatus() {
        return status;
    }
    /**
     * @param status The status to set.
     */
    public void setStatus(String status) {
        this.status = status;
    }

}
