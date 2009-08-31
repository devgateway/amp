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
import org.digijava.module.aim.dbentity.AmpOrgGroup;
import org.digijava.module.aim.dbentity.AmpOrganisation;
import org.digijava.module.aim.helper.GlobalSettingsConstants;
import org.digijava.module.aim.helper.TeamMember;
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
        if(orgForm.getCurrencyId()==null){
            orgForm.setCurrencyId(CurrencyUtil.getAmpcurrency("USD").getAmpCurrencyId());
        }
        // Org profile is only for Mul and Bil organizations
        List<AmpOrgGroup> orgGroups=new ArrayList(DbUtil.getBilMulOrgGroups());
        orgForm.setOrgGroups(orgGroups);
        if(orgForm.getOrgGroupId()!=null&&orgForm.getOrgGroupId()!=-1){
            if (orgGroups.size() > 0) {
                List<AmpOrganisation> orgs = DbUtil.getOrganisationByGroupId(orgForm.getOrgGroupId());
                orgForm.setOrganizations(orgs);
            }
        }
        else{
            orgForm.setOrganizations(DbUtil.getBilMulOrganisations());
        }

        orgForm.setYears(new ArrayList<BeanWrapperImpl>());
        Long yearFrom = Long.parseLong(FeaturesUtil.getGlobalSettingValue(org.digijava.module.aim.helper.Constants.GlobalSettings.YEAR_RANGE_START));
        Long countYear = Long.parseLong(FeaturesUtil.getGlobalSettingValue(org.digijava.module.aim.helper.Constants.GlobalSettings.NUMBER_OF_YEARS_IN_RANGE));
        for (long i = yearFrom; i <= (yearFrom + countYear); i++) {
			orgForm.getYears().add(new BeanWrapperImpl(new Long(i)));
        }
        if (orgForm.getYear() == null) {
            Long year = null;
            try {
                year = Long.parseLong(FeaturesUtil.getGlobalSettingValue("Current Fiscal Year"));
            } catch (NumberFormatException ex) {
                year = new Long(Calendar.getInstance().get(Calendar.YEAR));
            }
            orgForm.setYear(year);
        }
        Collection calendars=DbUtil.getAllFisCalenders();
        if (calendars != null) {
            orgForm.setFiscalCalendars(new ArrayList(calendars));
        }
        if(orgForm.getFiscalCalendarId()==null){
				String value = FeaturesUtil.getGlobalSettingValue(GlobalSettingsConstants.DEFAULT_CALENDAR);
				if (value != null) {
					orgForm.setFiscalCalendarId(Long.parseLong(value));
				}
        }
        FilterHelper filter=null;
        HttpSession session = request.getSession();
        if(orgForm.getWorkspaceOnly()!=null&&orgForm.getWorkspaceOnly()){
            TeamMember tm =(TeamMember) session.getAttribute("currentMember");
            filter=new FilterHelper(orgForm,tm);
        }
        else{
            filter=new FilterHelper(orgForm);
        }

       session.setAttribute("orgProfileFilter", filter);
      
        return mapping.findForward("forward");
    }
}
