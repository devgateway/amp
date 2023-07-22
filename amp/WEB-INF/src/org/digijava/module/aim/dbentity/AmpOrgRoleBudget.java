package org.digijava.module.aim.dbentity;

import java.io.Serializable;

import org.digijava.kernel.validators.common.RequiredValidator;
import org.digijava.module.aim.annotations.interchange.Interchangeable;
import org.digijava.module.aim.annotations.interchange.InterchangeableBackReference;
import org.digijava.module.aim.annotations.interchange.InterchangeableId;
import org.digijava.module.aim.annotations.interchange.InterchangeableValidator;

import javax.persistence.*;

@Entity
@Table(name = "AMP_ORG_ROLE_BUDGET")
public class AmpOrgRoleBudget implements Serializable, Comparable<AmpOrgRoleBudget>, Cloneable {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "AMP_ORG_ROLE_BUDGET_seq")
    @SequenceGenerator(name = "AMP_ORG_ROLE_BUDGET_seq", sequenceName = "AMP_ORG_ROLE_BUDGET_seq", allocationSize = 1)
    @Column(name = "amp_orgrole_budget_id")
    @InterchangeableId
    @Interchangeable(fieldTitle = "Id")
    private Long ampOrgRoleBudgetId;

    @Column(name = "budget_code")
    @Interchangeable(fieldTitle = "Budget Code", importable = true,
            interValidators = @InterchangeableValidator(RequiredValidator.class))
    private String budgetCode;

    @ManyToOne(fetch = FetchType.LAZY)
    @InterchangeableBackReference
    @JoinColumn(name = "amp_org_role_id", referencedColumnName = "amp_org_role_id")
    private AmpOrgRole ampOrgRole;

    
    public Long getAmpOrgRoleBudgetId() {
        return ampOrgRoleBudgetId;
    }



    public void setAmpOrgRoleBudgetId(Long ampOrgRoleBudgetId) {
        this.ampOrgRoleBudgetId = ampOrgRoleBudgetId;
    }



    public String getBudgetCode() {
        return budgetCode;
    }



    public void setBudgetCode(String budgetCode) {
        this.budgetCode = budgetCode;
    }



    public AmpOrgRole getAmpOrgRole() {
        return ampOrgRole;
    }



    public void setAmpOrgRole(AmpOrgRole ampOrgRole) {
        this.ampOrgRole = ampOrgRole;
    }
    
    @Override
    public int compareTo(AmpOrgRoleBudget o) {
        if(this.getAmpOrgRoleBudgetId()!=null)
            return this.getAmpOrgRoleBudgetId().compareTo(o.getAmpOrgRoleBudgetId());
            else return -1;
    }
    
    @Override
    public Object clone() throws CloneNotSupportedException {
        return super.clone();
    }
    
}
