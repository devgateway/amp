package org.digijava.module.aim.dbentity;

import java.io.Serializable;
import javax.persistence.*;

@Entity
@Table(name = "AMP_GPI_SURVEY_QUESTION_TYPE")
public class AmpGPISurveyQuestionType implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "AMP_GPI_SURVEY_QUESTION_TYPE_seq")
    @SequenceGenerator(name = "AMP_GPI_SURVEY_QUESTION_TYPE_seq", sequenceName = "AMP_GPI_SURVEY_QUESTION_TYPE_seq", allocationSize = 1)
    @Column(name = "amp_type_id")
    private Long ampTypeId;

    @Column(name = "name")
    private String name;

    @Column(name = "description")
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
