package org.digijava.module.budget.dbentity;

import org.digijava.module.aim.dbentity.AmpOrganisation;

import java.io.Serializable;
import java.util.Set;


public class AmpBudgetSector implements Serializable,Comparable{
    private static final long serialVersionUID = 1L;
    
    public AmpBudgetSector() {
        super();
    }
    private Long idsector;
    private String sectorname;
    private String code;
    private Set<AmpOrganisation> organisations;
    
    
    
    public Long getIdsector() {
        return idsector;
    }
    public Set<AmpOrganisation> getOrganisations() {
        return organisations;
    }
    public void setOrganisations(Set<AmpOrganisation> organisations) {
        this.organisations = organisations;
    }
    public void setIdsector(Long idsector) {
        this.idsector = idsector;
    }
    public String getSectorname() {
        return sectorname;
    }
    public void setSectorname(String sectorname) {
        this.sectorname = sectorname;
    }
    public String getCode() {
        return code;
    }
    public void setCode(String code) {
        this.code = code;
    }
    @Override
    public int compareTo(Object o) {
        if (!(o instanceof AmpBudgetSector)) 
            throw new ClassCastException();
        AmpBudgetSector bsector = (AmpBudgetSector) o;
        if (this.sectorname != null) {
            if (bsector.sectorname != null) {
                return (this.sectorname.trim().toLowerCase().
                        compareTo(bsector.sectorname.trim().toLowerCase()));
            } else {
                return (this.sectorname.trim().toLowerCase().compareTo(""));
            }
        } else {
            if (bsector.sectorname != null) {
                return ("".compareTo(bsector.sectorname.trim().toLowerCase()));
            } else {
                return 0;
            }           
        }
    }
    
    
}
