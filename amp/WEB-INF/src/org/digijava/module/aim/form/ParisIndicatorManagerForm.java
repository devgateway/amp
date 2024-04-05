/**
 * created on 01/05/06
 * 
 * @author Govind G Dalwani
 * 
 */
package org.digijava.module.aim.form;
/**
 * @author Govind G Dalwani
 * 
 */

import org.apache.struts.action.ActionForm;

import java.io.Serializable;
import java.util.Collection;


public class ParisIndicatorManagerForm extends ActionForm implements Serializable
{
    
    Collection parisIndicatorName = null;
    Collection formQuestion = null;
    String piQuestionGot = null;
    Integer piQuestId = null;
    Long piQuestTypeId = null;
    Long piQuestionIndicatorId = null;
    Long piQuestionId = null;
    Long Indicatorvalue = null;
    String addNewIndicatorText =null;
    String addNewIndicatorCode = null;
    Collection formQuestId = null;
    String editIndicatorText=null;
    Long addNewQuestionIndicatorId =null;
    Integer indicatorNumber = null;
    Integer numberOfQuestions =null;
    
    
    /*Collection formEditQuestionGot=null;
    Collection formEditQuestionTypeGot=null;
    Long formABC = null;
    String addNewQuestionDescription=null;
    Long  addNewQuestionId = null;*/
    
    
    
    private boolean errorFlag;
    
    /**
     * @return Returns the parisIndicatorName.
     */
    public Collection getParisIndicatorName() {
        return parisIndicatorName;
    }
    /**
     * @param parisIndicatorName The parisIndicatorName to set.
     */
    public void setParisIndicatorName(Collection parisIndicatorName) {
        this.parisIndicatorName = parisIndicatorName;
    }

    /**
     * @return Returns the formQuestion.
     */
    public Collection getFormQuestion() {
        return formQuestion;
    }
    /**
     * @param formQuestion The formQuestion to set.
     */
    public void setFormQuestion(Collection formQuestion) {
        this.formQuestion = formQuestion;
    }
    
    /**
     * @return Returns the piQuestionGot.
     */
    public String getPiQuestionGot() {
        return piQuestionGot;
    }
    /**
     * @param piQuestionGot The piQuestionGot to set.
     */
    public void setPiQuestionGot(String piQuestionGot) {
        this.piQuestionGot = piQuestionGot;
    }
    
    /**
     * @return Returns the piQuestId.
     */
    public Integer getPiQuestId() {
        return piQuestId;
    }
    /**
     * @param piQuestId The piQuestId to set.
     */
    public void setPiQuestId(Integer piQuestId) {
        this.piQuestId = piQuestId;
    }
    /**
     * @return Returns the piQuestTypeId.
     */
    public Long getPiQuestTypeId() {
        return piQuestTypeId;
    }
    /**
     * @param piQuestTypeId The piQuestTypeId to set.
     */
    public void setPiQuestTypeId(Long piQuestTypeId) {
        this.piQuestTypeId = piQuestTypeId;
    }
    
    /**
     * @return Returns the piQuestionIndicatorId.
     */
    public Long getPiQuestionIndicatorId() {
        return piQuestionIndicatorId;
    }
    /**
     * @param piQuestionIndicatorId The piQuestionIndicatorId to set.
     */
    public void setPiQuestionIndicatorId(Long piQuestionIndicatorId) {
        this.piQuestionIndicatorId = piQuestionIndicatorId;
    }
    /**
     * @return Returns the piQuestionId.
     */
    public Long getPiQuestionId() {
        return piQuestionId;
    }
    /**
     * @param piQuestionId The piQuestionId to set.
     */
    public void setPiQuestionId(Long piQuestionId) {
        this.piQuestionId = piQuestionId;
    }
    
    /**
     * @return Returns the addNewIndicatorText.
     */
    public String getAddNewIndicatorText() {
        return addNewIndicatorText;
    }
    /**
     * @param addNewIndicatorText The addNewIndicatorText to set.
     */
    public void setAddNewIndicatorText(String addNewIndicatorText) {
        this.addNewIndicatorText = addNewIndicatorText;
    }
    
    /**
     * @return Returns the addNewIndicatorCode.
     */
    public String getAddNewIndicatorCode() {
        return addNewIndicatorCode;
    }
    /**
     * @param addNewIndicatorCode The addNewIndicatorCode to set.
     */
    public void setAddNewIndicatorCode(String addNewIndicatorCode) {
        this.addNewIndicatorCode = addNewIndicatorCode;
    }
    /**
     * @return Returns the formQuestId.
     */
    public Collection getFormQuestId() {
        return formQuestId;
    }
    /**
     * @param formQuestId The formQuestId to set.
     */
    public void setFormQuestId(Collection formQuestId) {
        this.formQuestId = formQuestId;
    }
    /**
     * @return Returns the errorFlag.
     */
    public boolean isErrorFlag() {
        return errorFlag;
    }
    /**
     * @param errorFlag The errorFlag to set.
     */
    public void setErrorFlag(boolean errorFlag) {
        this.errorFlag = errorFlag;
    }
        public Long getAddNewQuestionIndicatorId() {
        return addNewQuestionIndicatorId;
    }
    /**
     * @param addNewQuestionIndicatorId The addNewQuestionIndicatorId to set.
     */
    public void setAddNewQuestionIndicatorId(Long addNewQuestionIndicatorId) {
        this.addNewQuestionIndicatorId = addNewQuestionIndicatorId;
    }
    /**
     * @return Returns the indicatorvalue.
     */
    public Long getIndicatorvalue() {
        return Indicatorvalue;
    }
    /**
     * @param indicatorvalue The indicatorvalue to set.
     */
    public void setIndicatorvalue(Long indicatorvalue) {
        Indicatorvalue = indicatorvalue;
    }
    /**
     * @return Returns the editIndicatorText.
     */
    public String getEditIndicatorText() {
        return editIndicatorText;
    }
    /**
     * @param editIndicatorText The editIndicatorText to set.
     */
    public void setEditIndicatorText(String editIndicatorText) {
        this.editIndicatorText = editIndicatorText;
    }
    /**
     * @return Returns the indicatorNumber.
     */
    public Integer getIndicatorNumber() {
        return indicatorNumber;
    }
    /**
     * @param indicatorNumber The indicatorNumber to set.
     */
    public void setIndicatorNumber(Integer indicatorNumber) {
        this.indicatorNumber = indicatorNumber;
    }
    /**
     * @return Returns the numberOfQuestions.
     */
    public Integer getNumberOfQuestions() {
        return numberOfQuestions;
    }
    /**
     * @param numberOfQuestions The numberOfQuestions to set.
     */
    public void setNumberOfQuestions(Integer numberOfQuestions) {
        this.numberOfQuestions = numberOfQuestions;
    }
    

}
