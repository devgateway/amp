/*
 * Created on 1/03/2006
 * 
 * @author akashs
 * 
 */
package org.digijava.module.aim.dbentity;

import java.io.Serializable;

import org.digijava.module.aim.annotations.interchange.Interchangeable;

public class AmpAhsurveyQuestionType implements Serializable{
//IATI-check: to be ignored
//  @Interchangeable(fieldTitle="ID", id = true)
    private Long ampTypeId;
//  @Interchangeable(fieldTitle="Name", importable = true, value = true)
    private String name;    // 'yes-no', 'calculated'
//  @Interchangeable(fieldTitle="Description", importable = true)
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
