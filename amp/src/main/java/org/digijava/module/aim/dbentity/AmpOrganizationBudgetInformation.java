

package org.digijava.module.aim.dbentity;

import org.digijava.module.aim.helper.donorReport.OrgProfileValue;
import org.digijava.module.aim.helper.donorReport.ValueTranslatabePair;
import org.digijava.module.categorymanager.dbentity.AmpCategoryValue;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;


public class AmpOrganizationBudgetInformation implements Serializable,OrgProfileValue{

    //IATI-check: to be ignored
    //  @Interchangeable(fieldTitle="ID")
    private Long id;
//  @Interchangeable(fieldTitle="Type")
    private AmpCategoryValue type;
//  @Interchangeable(fieldTitle="Year")
    private Long year;
//  @Interchangeable(fieldTitle="Amount")
    private Double amount;
//  @Interchangeable(fieldTitle="Currency")
    private AmpCurrency currency;
//  @Interchangeable(fieldTitle="Organization", recursive=true)
    private AmpOrganisation organization;
    private boolean newlyCreated;
//    @Interchangeable(fieldTitle="Organizations", recursive=true)
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
