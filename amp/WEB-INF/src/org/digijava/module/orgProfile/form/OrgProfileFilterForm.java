/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.digijava.module.orgProfile.form;

import java.util.Collection;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;
import org.digijava.module.aim.dbentity.AmpCurrency;
import org.digijava.module.aim.dbentity.AmpOrgGroup;
import org.digijava.module.aim.dbentity.AmpOrganisation;
import org.springframework.beans.BeanWrapperImpl;

/**
 *
 * @author medea
 */
public class OrgProfileFilterForm extends ActionForm {
    
    private static final long serialVersionUID = 1L;
    private Long org;
    private Long year;
    private Long currency;
    private Boolean workspaceOnly;

    private List<AmpCurrency>currencies;
    private List<AmpOrganisation>organizations;
    private Collection<BeanWrapperImpl> years;
    
    private int transactionType;

    private List<AmpOrgGroup> orgGroups;
    private Long orgGroupId;

    public Long getOrgGroupId() {
        return orgGroupId;
    }

    public void setOrgGroupId(Long orgGroupId) {
        this.orgGroupId = orgGroupId;
    }

    public List<AmpOrgGroup> getOrgGroups() {
        return orgGroups;
    }

    public void setOrgGroups(List<AmpOrgGroup> orgGroups) {
        this.orgGroups = orgGroups;
    }

    public int getTransactionType() {
        return transactionType;
    }

    public void setTransactionType(int transactionType) {
        this.transactionType = transactionType;
    }
    

    public List<AmpCurrency> getCurrencies() {
        return currencies;
    }

    public void setCurrencies(List<AmpCurrency> currencies) {
        this.currencies = currencies;
    }

    public Long getCurrency() {
        return currency;
    }

    public void setCurrency(Long currency) {
        this.currency = currency;
    }

  

    public Long getOrg() {
        return org;
    }

    public void setOrg(Long org) {
        this.org = org;
    }

    public List<AmpOrganisation> getOrganizations() {
        return organizations;
    }

    public void setOrganizations(List<AmpOrganisation> organizations) {
        this.organizations = organizations;
    }

    public Long getYear() {
        return year;
    }

    public void setYear(Long year) {
        this.year = year;
    }

    public Collection<BeanWrapperImpl> getYears() {
        return years;
    }

    public void setYears(Collection<BeanWrapperImpl> years) {
        this.years = years;
    }
    
     public Boolean getWorkspaceOnly() {
        return workspaceOnly;
    }

    public void setWorkspaceOnly(Boolean workspaceOnly) {
        this.workspaceOnly = workspaceOnly;
    }

    @Override
    public void reset(ActionMapping mapping, HttpServletRequest request) {
        super.reset(mapping, request);
        workspaceOnly=false;
    }

}
	