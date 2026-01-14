package org.digijava.module.um.action;

import org.apache.log4j.Logger;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.digijava.kernel.entity.Locale;
import org.digijava.kernel.entity.trubudget.TruBudgetIntent;
import org.digijava.kernel.translator.util.TrnCountry;
import org.digijava.kernel.translator.util.TrnLocale;
import org.digijava.kernel.translator.util.TrnUtil;
import org.digijava.kernel.util.RequestUtils;
import org.digijava.kernel.util.SiteUtils;
import org.digijava.module.aim.dbentity.AmpGlobalSettings;
import org.digijava.module.aim.dbentity.AmpTeam;
import org.digijava.module.aim.helper.Constants;
import org.digijava.module.aim.helper.CountryBean;
import org.digijava.module.aim.helper.GlobalSettingsConstants;
import org.digijava.module.aim.util.FeaturesUtil;
import org.digijava.module.aim.util.TeamMemberUtil;
import org.digijava.module.aim.util.TeamUtil;
import org.digijava.module.um.form.AddUserForm;
import org.digijava.module.um.util.DbUtil;

import javax.servlet.http.HttpSession;
import java.util.*;

import static org.digijava.module.um.util.DbUtil.*;

public class AddUser extends Action {

    private static final Logger logger = Logger.getLogger(AddUser.class);

    public ActionForward execute(ActionMapping mapping, ActionForm form,
            javax.servlet.http.HttpServletRequest request,
            javax.servlet.http.HttpServletResponse response) throws java.lang.Exception {

        AddUserForm registerForm = (AddUserForm) form;

        try {
            
            /**
             * Test if user is administrator
             */
            HttpSession session     = request.getSession();
            String ampAdmin         = (String)session.getAttribute("ampAdmin"); 
            if ( ampAdmin == null || ampAdmin.equals("no") ) {
                return mapping.findForward("index");
            }
            
            String actionFlag = request.getParameter("actionFlag");
            logger.debug("actionFlag: " + actionFlag);

            if ("".equals(actionFlag) || actionFlag == null) {

                    // set country resident data
                    registerForm.setRegistrationByEmail(FeaturesUtil.getGlobalSettingValueBoolean(GlobalSettingsConstants.USER_REGISTRATION_BY_MAIL));

                    HashMap countriesMap = new HashMap<>();
                    Iterator iterator = TrnUtil.getCountries(
                            RequestUtils.getNavigationLanguage(request).getCode())
                            .iterator();
                    while (iterator.hasNext()) {
                        TrnCountry item = (TrnCountry) iterator.next();
                        countriesMap.put(item.getIso(), item);
                    }
                    // sort countries
                    Collection<CountryBean> countriesCol = org.digijava.module.aim.util.DbUtil.getTranlatedCountries(request);

                    registerForm.setCountryResidence(countriesCol);
                    logger.debug("sortedCountries.size : " + countriesCol.size());
                    Collection<TruBudgetIntent> intents = getTruBudgetIntents();
                    registerForm.setTruBudgetIntents(intents);

                    //set default country from global settings.
                    registerForm.setSelectedCountryResidence(FeaturesUtil.getGlobalSettingValue(Constants.GLOBAL_DEFAULT_COUNTRY));
                     List<AmpGlobalSettings> settings = getGlobalSettingsBySection("trubudget");
                     registerForm.setTruBudgetEnabled(getSettingValue(settings,"isEnabled"));


                // set default web site
                    registerForm.setWebSite("http://");

                    // set Navigation languages
                    Set languages = SiteUtils.getUserLanguages(RequestUtils
                            .getSite(request));

                    HashMap translations = new HashMap<>();
                    iterator = TrnUtil.getLanguages(
                            RequestUtils.getNavigationLanguage(request).getCode())
                            .iterator();
                    while (iterator.hasNext()) {
                        TrnLocale item = (TrnLocale) iterator.next();
                        translations.put(item.getCode(), item);
                    }
                    //sort languages
                    List sortedLanguages = new ArrayList<>();
                    iterator = languages.iterator();
                    while (iterator.hasNext()) {
                        Locale item = (Locale) iterator.next();
                        sortedLanguages.add(translations.get(item.getCode()));
                    }
                    sortedLanguages.sort(TrnUtil.localeNameComparator);

                    registerForm.setNavigationLanguages(sortedLanguages);

                    // set organisation types
                    registerForm.setOrgTypeColl(DbUtil.getAllOrgTypes());
                    addTeams(registerForm, request);

            }
            else if ("typeSelected".equals(actionFlag)) {
                //  load organisation groups related to selected organisation-type
                registerForm.setOrgGroupColl(DbUtil.getOrgGroupByType(registerForm.getSelectedOrgType()));
                if (null != registerForm.getOrgColl() && registerForm.getOrgColl().size() != 0)
                    registerForm.getOrgColl().clear();
            }
            else if ("groupSelected".equals(actionFlag))
                //  load organisations related to selected organisation-group
                registerForm.setOrgColl(DbUtil.getOrgByGroup(registerForm.getSelectedOrgGroup()));

        } catch (Exception e) {
            logger.error("Exception from ShowUserRegister :" + e);
            return mapping.findForward(null);
        }
        return mapping.findForward("forward");
    }
    private void addTeams( AddUserForm registerForm, javax.servlet.http.HttpServletRequest request){
        HttpSession session = request.getSession();
        Collection<AmpTeam> ampWorkspaces = (Collection<AmpTeam>) session.getAttribute("ampWorkspaces");
        if (ampWorkspaces == null) {
            ampWorkspaces = TeamUtil.getAllTeams();
            session.setAttribute("ampWorkspaces", ampWorkspaces);
        }
        registerForm.setWorkspaces(ampWorkspaces);
        registerForm.setAmpRoles(TeamMemberUtil.getAllTeamMemberRoles());
    }
}
