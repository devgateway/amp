/*
 * Created on 9/03/2006
 * @author akashs
 * 
 */
package org.digijava.module.aim.helper;

public class Question {

    private Long questionId = null;
    private String questionText = null;
    private String questionType = null;
    private String response = null;
    private Long responseId = null;
    
    /**
     * @return Returns the questionId.
     */
    public Long getQuestionId() {
        return questionId;
    }
    /**
     * @param questionId The questionId to set.
     */
    public void setQuestionId(Long questionId) {
        this.questionId = questionId;
    }
    /**
     * @return Returns the qType.
     */
    public String getQuestionType() {
        return questionType;
    }
    /**
     * @param type The qType to set.
     */
    public void setQuestionType(String questionType) {
        this.questionType = questionType;
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
     * @return Returns the reponse.
     */
    public String getResponse() {
        return response;
    }
    /**
     * @param reponse The reponse to set.
     */
    public void setResponse(String response) {
        this.response = response;
    }
    /**
     * @return Returns the responseId.
     */
    public Long getResponseId() {
        return responseId;
    }
    /**
     * @param responseId The responseId to set.
     */
    public void setResponseId(Long responseId) {
        this.responseId = responseId;
    }

}
