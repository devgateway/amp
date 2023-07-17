package org.digijava.module.budget.dbentity;

import java.io.Serializable;
import java.util.Set;

import org.digijava.module.aim.dbentity.AmpOrganisation;
import javax.persistence.*;
@Entity
@Table(name = "AMP_DEPARTMENTS")
@Cacheable
public class AmpDepartments implements Serializable,Comparable {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "AMP_DEPARTMENTS_seq")
    @SequenceGenerator(name = "AMP_DEPARTMENTS_seq", sequenceName = "AMP_DEPARTMENTS_seq", allocationSize = 1)
    @Column(name = "id_department")
    private Long id;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "AMP_ORGANISATION_DEPARTMENTS",
            joinColumns = @JoinColumn(name = "ID"),
            inverseJoinColumns = @JoinColumn(name = "amp_org_id"))
    private Set<AmpOrganisation> organisations;

    @Column(name = "name")
    private String name;

    @Column(name = "code")
    private String code;
    public AmpDepartments() {
    }
    
    
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
