package org.digijava.module.aim.dbentity;

import java.io.Serializable;

public class AmpGPISurveyQuestionType implements Serializable {

    private Long ampTypeId;
    private String name; // 'yes-no', 'calculated'
    private String desc;

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getAmpTypeId() {
        return ampTypeId;
    }

    public void setAmpTypeId(Long ampTypeId) {
        this.ampTypeId = ampTypeId;
    }

}
