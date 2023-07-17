package org.digijava.module.aim.dbentity ;

import java.io.Serializable;

import org.digijava.module.aim.annotations.interchange.PossibleValueId;
import org.digijava.module.aim.annotations.interchange.PossibleValueValue;
import org.digijava.module.aim.util.Identifiable;
import javax.persistence.*;

@Entity
@Table(name = "AMP_ROLE")
public class AmpRole implements Serializable, Identifiable, Comparable<AmpRole>
{

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "amp_role_seq_generator")
    @SequenceGenerator(name = "amp_role_seq_generator", sequenceName = "AMP_ROLE_seq", allocationSize = 1)
    @Column(name = "amp_role_id")
    @PossibleValueId

    private Long ampRoleId;

    @Column(name = "role_code")
    private String roleCode;

    @Column(name = "name")
    @PossibleValueValue
    private String name;

    @Column(name = "type")
    private String type;

    @Column(name = "description", columnDefinition = "text")
    private String description;

    @Column(name = "language")
    private String language;
    //IATI-check: not to be ignored, but obtained via possible values 

    
    
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
