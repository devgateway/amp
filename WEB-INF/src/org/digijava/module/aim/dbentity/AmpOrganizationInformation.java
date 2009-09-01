

package org.digijava.module.aim.dbentity;

import java.io.Serializable;
import org.digijava.module.aim.helper.Constants;


public class AmpOrganizationInformation implements Serializable{
    private Long id;
    private Integer type;
    private Long year;
    private Double percent;
    private Double amount;
    private AmpCurrency currency;
    private AmpOrganisation organization;
    private boolean newlyCreated;


    public Double getAmount() {
        return amount;
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

    public Double getPercent() {
        return percent;
    }

    public void setPercent(Double percent) {
        this.percent= percent;
    }

    public int getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public Long getYear() {
        return year;
    }

    public void setYear(Long year) {
        this.year = year;
    }
    
    public String getName() {
        String name = "";
        if (type == Constants.ORG_INFO_TYPE_ANNUAL_BUDGET_ADMIN) {
            name = "Annual Budget of internal/administrative functioning";
        } else {
            if (type == Constants.ORG_INFO_TYPE_ANNUAL_BUDGET_PROGRAM) {
                name = "Program Annual Budget";
            }
        }
        return name;
    }

}
