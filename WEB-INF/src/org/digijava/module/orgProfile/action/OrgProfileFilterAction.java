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
import org.digijava.module.aim.dbentity.AmpCategoryValueLocations;
import org.digijava.module.aim.dbentity.AmpCurrency;
import org.digijava.module.aim.dbentity.AmpOrgGroup;
import org.digijava.module.aim.dbentity.AmpOrganisation;
import org.digijava.module.aim.helper.Constants;
import org.digijava.module.aim.helper.GlobalSettingsConstants;
import org.digijava.module.aim.helper.TeamMember;
import org.digijava.module.aim.util.CurrencyUtil;
import org.digijava.module.aim.util.DbUtil;
import org.digijava.module.aim.util.DynLocationManagerUtil;
import org.digijava.module.aim.util.FeaturesUtil;
import org.digijava.module.aim.util.LocationUtil;
import org.digijava.module.orgProfile.form.OrgProfileFilterForm;
import org.digijava.module.orgProfile.helper.FilterHelper;
import org.springframework.beans.BeanWrapperImpl;

/**
 *
 * @author medea
 */
public class OrgProfileFilterAction extends Action {

    @Override
    public ActionForward execute(ActionMapping mapping, ActionForm form,
            HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        HttpSession session = request.getSession();
        TeamMember tm = (TeamMember) session.getAttribute("currentMember");
        OrgProfileFilterForm orgForm = (OrgProfileFilterForm) form;
        String reset = request.getParameter("reset");
        if (reset != null && reset.equals("true")) {
            orgForm.setCurrencyId(null);
            orgForm.setOrgGroupId(null);
            orgForm.setFiscalCalendarId(null);
            orgForm.setYear(null);
            orgForm.setOrgIds(null);
            orgForm.setTransactionType(Constants.COMMITMENT);
            orgForm.setLargestProjectNumb(10);
            orgForm.setSelRegionId(null);
            orgForm.setSelZoneIds(null);
            orgForm.setYearsInRange(5);
        }

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
        if (orgForm.getCurrencyId() == null) {
            Long currId = tm.getAppSettings().getCurrencyId();
            if (currId != null) {
                orgForm.setCurrencyId(currId);
            } else {
                orgForm.setCurrencyId(CurrencyUtil.getAmpcurrency("USD").getAmpCurrencyId());
            }
        }
        List<AmpOrgGroup> orgGroups = new ArrayList(DbUtil.getAllOrgGroups());
        orgForm.setOrgGroups(orgGroups);
        List<AmpOrganisation> orgs = null;
        if (orgForm.getOrgGroupId() == null || orgForm.getOrgGroupId() == -1) {
            // all groups
            orgForm.setOrgGroupId(-1l);
            orgs = new ArrayList<AmpOrganisation>(DbUtil.getAllDonorOrgs());
        } else {
            orgs = DbUtil.getDonorOrganisationByGroupId(orgForm.getOrgGroupId());
        }
        orgForm.setOrganizations(orgs);
        orgForm.setYears(new ArrayList<BeanWrapperImpl>());
        Long yearFrom = Long.parseLong(FeaturesUtil.getGlobalSettingValue(Constants.GlobalSettings.YEAR_RANGE_START));
        Long countYear = Long.parseLong(FeaturesUtil.getGlobalSettingValue(Constants.GlobalSettings.NUMBER_OF_YEARS_IN_RANGE));
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
        Collection calendars = DbUtil.getAllFisCalenders();
        if (calendars != null) {
            orgForm.setFiscalCalendars(new ArrayList(calendars));
        }
        if (orgForm.getFiscalCalendarId() == null) {
            Long fisCalId = tm.getAppSettings().getFisCalId();
            if (fisCalId == null) {
                String value = FeaturesUtil.getGlobalSettingValue(GlobalSettingsConstants.DEFAULT_CALENDAR);
                if (value != null) {
                    fisCalId = Long.parseLong(value);
                }

            }
            orgForm.setFiscalCalendarId(fisCalId);
        }
        if (orgForm.getLargestProjectNumb() == null) {
            orgForm.setLargestProjectNumb(10);
        }
        if (orgForm.getRegions() == null) {
            orgForm.setRegions(new ArrayList<AmpCategoryValueLocations>(DynLocationManagerUtil.getLocationsOfTypeRegionOfDefCountry()));
        }
        Long regionId = orgForm.getSelRegionId();
        List<AmpCategoryValueLocations> zones = new ArrayList<AmpCategoryValueLocations>();

        if (regionId != null && regionId != -1) {
            AmpCategoryValueLocations region = LocationUtil.getAmpCategoryValueLocationById(regionId);
            if (region.getChildLocations() != null) {
                zones.addAll(region.getChildLocations());

            }

        }
        orgForm.setZones(zones);

        FilterHelper filter = null;
        if (orgForm.getWorkspaceOnly() != null && orgForm.getWorkspaceOnly()) {
            filter = new FilterHelper(orgForm, tm);
        } else {
            filter = new FilterHelper(orgForm);
        }
        session.setAttribute("orgProfileFilter", filter);
        return mapping.findForward("forward");
    }
}
