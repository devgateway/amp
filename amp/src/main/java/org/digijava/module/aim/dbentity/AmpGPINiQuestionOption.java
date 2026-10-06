package org.digijava.module.aim.dbentity;

import java.io.Serializable;

public class AmpGPINiQuestionOption implements Serializable {
    
    private static final long serialVersionUID = 3456453082099936257L;

    private Long ampGPINiQuestionOptionId;
    private AmpGPINiQuestion ampGPINiQuestion;
    private String code;
    private String optionText;
    private String description;

    public Long getAmpGPINiQuestionOptionId() {
        return ampGPINiQuestionOptionId;
    }

    public void setAmpGPINiQuestionOptionId(Long amapGPINiQuestionOptionId) {
        this.ampGPINiQuestionOptionId = amapGPINiQuestionOptionId;
    }

    public AmpGPINiQuestion getAmpGPINiQuestion() {
        return ampGPINiQuestion;
    }

    public void setAmpGPINiQuestion(AmpGPINiQuestion ampGPINiQuestion) {
        this.ampGPINiQuestion = ampGPINiQuestion;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }
    
    public String getOptionText() {
        return optionText;
    }

    public void setOptionText(String optionText) {
        this.optionText = optionText;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

}
