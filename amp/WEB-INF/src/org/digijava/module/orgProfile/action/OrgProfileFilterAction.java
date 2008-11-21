/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.digijava.module.orgProfile.action;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import javax.servlet.http.HttpSession;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.digijava.module.aim.dbentity.AmpCurrency;
import org.digijava.module.aim.dbentity.AmpOrganisation;
import org.digijava.module.aim.util.CurrencyUtil;
import org.digijava.module.aim.util.DbUtil;
import org.digijava.module.aim.util.FeaturesUtil;
import org.digijava.module.orgProfile.form.OrgProfileFilterForm;
import org.digijava.module.orgProfile.helper.FilterHelper;
import org.springframework.beans.BeanWrapperImpl;

/**
 *
 * @author medea
 */
public class OrgProfileFilterAction extends Action {

    public ActionForward execute(ActionMapping mapping, ActionForm form,
            HttpServletRequest request,
            HttpServletResponse response) throws Exception {

        OrgProfileFilterForm orgForm = (OrgProfileFilterForm) form;

        // create filter dropdowns
        Collection currency = CurrencyUtil.getAmpCurrency();
        List<AmpCurrency> validcurrencies = new ArrayList<AmpCurrency>();
        orgForm.setCurrencies(validcurrencies);
        //Only currencies which have exchanges rates
        for (Iterator iter = currency.iterator(); iter.hasNext();) {
            AmpCurrency element = (AmpCurrency) iter.next();
            if (CurrencyUtil.isRate(element.getCurrencyCode()) == true) {
                orgForm.getCurrencies().add((CurrencyUtil.getCurrencyByCode(element.getCurrencyCode())));
            }
        }
        if(orgForm.getCurrency()==null){
            orgForm.setCurrency(CurrencyUtil.getAmpcurrency("USD").getAmpCurrencyId());
        }
        // Org profile is only for Mul and Bil organizations 
        List<AmpOrganisation> orgs=DbUtil.getBilMulOrganisations();
        orgForm.setOrganizations(orgs);
        orgForm.setYears(new ArrayList<BeanWrapperImpl>());
        if(orgForm.getOrg()==null&&orgs.size()>0){
            orgForm.setOrg(((AmpOrganisation)orgs.get(0)).getAmpOrgId());
            
        }
        Long yearFrom = Long.parseLong(FeaturesUtil.getGlobalSettingValue(org.digijava.module.aim.helper.Constants.GlobalSettings.YEAR_RANGE_START));
        Long countYear = Long.parseLong(FeaturesUtil.getGlobalSettingValue(org.digijava.module.aim.helper.Constants.GlobalSettings.NUMBER_OF_YEARS_IN_RANGE));
        for (long i = yearFrom; i <= (yearFrom + countYear); i++) {
			orgForm.getYears().add(new BeanWrapperImpl(new Long(i)));
        }
        Long year =null;
        try{
                year=Long.parseLong(FeaturesUtil.getGlobalSettingValue("Current Fiscal Year"));
        }
        catch(NumberFormatException ex){
           year=new Long(Calendar.getInstance().get(Calendar.YEAR));
        }
        
        if(orgForm.getYear()==null){
            orgForm.setYear(year);
        }
			
        HttpSession session = request.getSession();
        session.setAttribute("orgProfileFilter", new FilterHelper(orgForm));
        return mapping.findForward("forward");
    }
}
