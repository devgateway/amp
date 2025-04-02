package org.digijava.module.aim.dbentity;

import java.io.Serializable;
import java.util.Set;

public class AmpGPINiIndicator implements Serializable {

    private static final long serialVersionUID = 2838422344917663439L;
    
    private Long ampGPINiIndicatorId;
    private String code;
    private String name;
    private String description;
    private Set<AmpGPINiQuestion> questions;

    public Long getAmpGPINiIndicatorId() {
        return ampGPINiIndicatorId;
    }

    public void setAmpGPINiIndicatorId(Long ampGPINiIndicatorId) {
        this.ampGPINiIndicatorId = ampGPINiIndicatorId;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Set<AmpGPINiQuestion> getQuestions() {
        return questions;
    }

    public void setQuestions(Set<AmpGPINiQuestion> questions) {
        this.questions = questions;
    }

}
