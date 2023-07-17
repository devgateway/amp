/*
 * Created on 1/03/2006
 * 
 * @author akashs
 * 
 */
package org.digijava.module.aim.dbentity;

import java.io.Serializable;

import org.digijava.module.aim.annotations.interchange.Interchangeable;
import javax.persistence.*;

@Entity
@Table(name = "AMP_AHSURVEY_QUESTION_TYPE")

public class AmpAhsurveyQuestionType implements Serializable{
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "amp_ahsurvey_question_type_seq_generator")
    @SequenceGenerator(name = "amp_ahsurvey_question_type_seq_generator", sequenceName = "AMP_AHSURVEY_QUESTION_TYPE_seq", allocationSize = 1)
    @Column(name = "amp_type_id")
    private Long ampTypeId;

    @Column(name = "name")
    private String name;

    @Column(name = "description")
    private String desc;

    
    /**
     * @return Returns the desc.
     */
    public String getDesc() {
        return desc;
    }
    /**
     * @param desc The desc to set.
     */
    public void setDesc(String desc) {
        this.desc = desc;
    }
    /**
     * @return Returns the name.
     */
    public String getName() {
        return name;
    }
    /**
     * @param name The name to set.
     */
    public void setName(String name) {
        this.name = name;
    }
    /**
     * @return Returns the typeId.
     */
    public Long getAmpTypeId() {
        return ampTypeId;
    }
    /**
     * @param typeId The typeId to set.
     */
    public void setAmpTypeId(Long ampTypeId) {
        this.ampTypeId = ampTypeId;
    }

}
