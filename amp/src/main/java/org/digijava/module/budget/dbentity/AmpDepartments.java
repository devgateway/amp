package org.digijava.module.budget.dbentity;

import java.io.Serializable;
import java.util.Set;

import org.digijava.module.aim.dbentity.AmpOrganisation;

public class AmpDepartments implements Serializable,Comparable {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    public AmpDepartments() {
    }
    private Long id;
    private String name;
    private String code;
    private Set<AmpOrganisation> organisations;
    
    
    public Set<AmpOrganisation> getOrganisations() {
        return organisations;
    }
    public void setOrganisations(Set<AmpOrganisation> organisations) {
        this.organisations = organisations;
    }
    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getCode() {
        return code;
    }
    public void setCode(String code) {
        this.code = code;
    }
    @Override
    public int compareTo(Object o) {
        if (!(o instanceof AmpDepartments)) 
            throw new ClassCastException();
        AmpDepartments dep = (AmpDepartments) o;
        if (this.name != null) {
            if (dep.name != null) {
                return (this.name.trim().toLowerCase().
                        compareTo(dep.name.trim().toLowerCase()));
            } else {
                return (this.name.trim().toLowerCase().compareTo(""));
            }
        } else {
            if (dep.name != null) {
                return ("".compareTo(dep.name.trim().toLowerCase()));
            } else {
                return 0;
            }           
        }
    }
    
}
