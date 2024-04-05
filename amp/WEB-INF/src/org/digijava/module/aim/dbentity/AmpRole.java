package org.digijava.module.aim.dbentity ;

import org.digijava.module.aim.annotations.interchange.PossibleValueId;
import org.digijava.module.aim.annotations.interchange.PossibleValueValue;
import org.digijava.module.aim.util.Identifiable;

import java.io.Serializable;

public class AmpRole implements Serializable, Identifiable, Comparable<AmpRole>
{
    //IATI-check: not to be ignored, but obtained via possible values 
    @PossibleValueId
    private Long ampRoleId ;
    private String roleCode ;
    @PossibleValueValue
    private String name ;
    private String type ;
    private String description ;
    private String language ;
    
    
    /**
     * @return
     */
    public Long getAmpRoleId() {
        return ampRoleId;
    }

    /**
     * @return
     */
    public String getDescription() {
        return description;
    }

    /**
     * @return
     */
    public String getLanguage() {
        return language;
    }

    /**
     * @return
     */
    public String getName() {
        return name;
    }

    /**
     * @return
     */
    public String getRoleCode() {
        return roleCode;
    }

    /**
     * @return
     */
    public String getType() {
        return type;
    }

    /**
     * @param long1
     */
    public void setAmpRoleId(Long long1) {
        ampRoleId = long1;
    }

    /**
     * @param string
     */
    public void setDescription(String string) {
        description = string;
    }

    /**
     * @param string
     */
    public void setLanguage(String string) {
        language = string;
    }

    /**
     * @param string
     */
    public void setName(String string) {
        name = string;
    }

    /**
     * @param string
     */
    public void setRoleCode(String string) {
        roleCode = string;
    }

    /**
     * @param string
     */
    public void setType(String string) {
        type = string;
    }

    @Override
    public String toString() {  
        return name;
    }

    @Override
    public int compareTo(AmpRole o) {
        if(this.getAmpRoleId()!=null && o.getAmpRoleId()!=null) return this.getAmpRoleId().compareTo(o.getAmpRoleId());
        return -1;
    }

    @Override
    public Object getIdentifier() {

        return ampRoleId;
    }
    
}   
