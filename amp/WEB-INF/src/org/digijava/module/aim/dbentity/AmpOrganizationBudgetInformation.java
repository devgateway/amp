

package org.digijava.module.aim.dbentity;

import java.io.Serializable;
import java.util.Set;
import org.digijava.module.categorymanager.dbentity.AmpCategoryValue;


public class AmpOrganizationBudgetInformation implements Serializable{
    private Long id;
    private AmpCategoryValue type;
    private Long year;
    private Double amount;
    private AmpCurrency currency;
    private AmpOrganisation organization;
    private boolean newlyCreated;
    private Set<AmpOrganisation> organizations;


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

 
}
