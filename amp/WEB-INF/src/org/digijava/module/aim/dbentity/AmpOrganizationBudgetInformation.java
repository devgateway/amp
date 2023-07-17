

package org.digijava.module.aim.dbentity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

import org.digijava.module.aim.helper.donorReport.OrgProfileValue;
import org.digijava.module.aim.helper.donorReport.ValueTranslatabePair;
import org.digijava.module.categorymanager.dbentity.AmpCategoryValue;
import javax.persistence.*;
@Entity
@Table(name = "AMP_ORG_BUDGET_INFO")

public class AmpOrganizationBudgetInformation implements Serializable,OrgProfileValue{
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "AMP_ORG_BUDGET_INFO_seq")
    @SequenceGenerator(name = "AMP_ORG_BUDGET_INFO_seq", sequenceName = "AMP_ORG_BUDGET_INFO_seq", allocationSize = 1)    @Column(name = "id")
    private Long id;

    @Column(name = "amount")
    private Double amount;

    @Column(name = "year")
    private Long year;

    @ManyToOne
    @JoinColumn(name = "amp_currency_id")
    private AmpCurrency currency;

    @ManyToOne
    @JoinColumn(name = "amp_org_id")
    private AmpOrganisation organization;

    @ManyToOne
    @JoinColumn(name = "type")
    private AmpCategoryValue type;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "amp_budget_organizations",
            joinColumns = @JoinColumn(name = "id"),
            inverseJoinColumns = @JoinColumn(name = "org_id"))
    private Set<AmpOrganisation> organizations;
    //IATI-check: to be ignored

    @Transient
    private boolean newlyCreated;


    public Double getAmount() {
        return amount;
    }

    public Set<AmpOrganisation> getOrganizations() {
        return organizations;
    }

    public void setOrganizations(Set<AmpOrganisation> organizations) {
        this.organizations = organizations;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public boolean isNewlyCreated() {
        return newlyCreated;
    }

    public void setNewlyCreated(boolean newlyCreated) {
        this.newlyCreated = newlyCreated;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public AmpOrganisation getOrganization() {
        return organization;
    }

    public void setOrganization(AmpOrganisation organization) {
        this.organization = organization;
    }

    public AmpCurrency getCurrency() {
        return currency;
    }

    public void setCurrency(AmpCurrency currency) {
        this.currency = currency;
    }

    public AmpCategoryValue getType() {
        return type;
    }

    public void setType(AmpCategoryValue type) {
        this.type = type;
    }

    public Long getYear() {
        return year;
    }

    public void setYear(Long year) {
        this.year = year;
    }
    @Override
    public List<ValueTranslatabePair> getValuesForOrgReport(){
        List<ValueTranslatabePair> values=new ArrayList<ValueTranslatabePair>();
        values.add(new ValueTranslatabePair(Arrays.asList(new String[]{getYear().toString()}),false));
        values.add(new ValueTranslatabePair(Arrays.asList(new String[]{getType().getValue()}),true));
        Set<AmpOrganisation> orgs=getOrganizations();
        List<String> orgNames=new ArrayList<String>();
        if(orgs!=null&&!orgs.isEmpty()){
            for(AmpOrganisation org:orgs){
                orgNames.add(org.getName());
            }
        }
        values.add(new ValueTranslatabePair(orgNames,false));
        values.add(new ValueTranslatabePair(Arrays.asList(new String[]{getAmount().toString()}),false));
        values.add(new ValueTranslatabePair(Arrays.asList(new String[]{getCurrency().getCurrencyCode()}),false));
        return values;
    }

    @Override
    public String[] getSubHeaders() {
        String[] subHeaders={"Year","Type","Organization(s)","Amount","Currency"};
        return subHeaders;
    }
 
}
