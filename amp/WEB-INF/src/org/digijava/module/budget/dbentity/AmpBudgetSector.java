package org.digijava.module.budget.dbentity;

import java.io.Serializable;
import java.util.Set;

import org.digijava.module.aim.dbentity.AmpOrganisation;
import javax.persistence.*;
@Entity
@Table(name = "AMP_BUDGET_SECTOR")
@Cacheable
public class AmpBudgetSector implements Serializable,Comparable{
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "AMP_BUDGET_SECTOR_seq")
    @SequenceGenerator(name = "AMP_BUDGET_SECTOR_seq", sequenceName = "AMP_BUDGET_SECTOR_seq", allocationSize = 1)    @Column(name = "budged_sector_id")
    private Long idsector;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "AMP_ORGANISATION_BUDGETSECTOR",
            joinColumns = @JoinColumn(name = "idsector"),
            inverseJoinColumns = @JoinColumn(name = "amp_org_id"))
    private Set<AmpOrganisation> organisations;

    @Column(name = "sector_name")
    private String sectorname;

    @Column(name = "code")
    private String code;
    
    public AmpBudgetSector() {
        super();
    }
    
    
    
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
