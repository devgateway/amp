package org.digijava.module.aim.action;

import org.apache.log4j.Logger;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.digijava.kernel.entity.Locale;
import org.digijava.kernel.translator.util.TrnCountry;
import org.digijava.kernel.translator.util.TrnLocale;
import org.digijava.kernel.translator.util.TrnUtil;
import org.digijava.kernel.util.RequestUtils;
import org.digijava.kernel.util.SiteUtils;
import org.digijava.module.aim.dbentity.AmpGlobalSettings;
import org.digijava.module.aim.helper.Constants;
import org.digijava.module.aim.helper.CountryBean;
import org.digijava.module.aim.helper.TeamMember;
import org.digijava.module.aim.util.FeaturesUtil;
import org.digijava.module.um.form.UserRegisterForm;
import org.digijava.module.um.util.DbUtil;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpSession;
import java.util.*;

import static org.digijava.module.um.util.DbUtil.getGlobalSettingsBySection;
import static org.digijava.module.um.util.DbUtil.getSettingValue;

public class ShowUserRegister extends Action {

    private static Logger logger = Logger.getLogger(ShowUserRegister.class);

    public ActionForward execute(ActionMapping mapping, ActionForm form,
            javax.servlet.http.HttpServletRequest request,
            javax.servlet.http.HttpServletResponse response) throws java.lang.Exception {

        try {
            HttpSession session=request.getSession();
            // Evaluate if the user is not logged.
            TeamMember currentTeamMember = (TeamMember) request.getSession().getAttribute("currentMember");
            if (currentTeamMember == null) {
                // Check for permission in the FM to avoid using the URL to call
                // the action directly.
                ServletContext context = request.getSession().getServletContext();
                if (!FeaturesUtil.isVisibleModule("Login - User Management")
                        || !FeaturesUtil.isVisibleFeature("Enable New User Registration")) {
                    logger.error("UNAUTHORIZED ATTEMPT TO CREATE NEW USER.");
                    return mapping.findForward("index");
                }
            }
            
            UserRegisterForm registerForm = (UserRegisterForm) form;
            String actionFlag = request.getParameter("actionFlag");
            logger.debug("actionFlag: " + actionFlag);

            if (request.getParameter("init") != null) {
                //Remove old errors.
                registerForm.setErrors(null);
            }

            if ("".equals(actionFlag) || actionFlag == null) {
                    // set country resident data

                    HashMap countriesMap = new HashMap();
                    Iterator iterator = TrnUtil.getCountries(
                            RequestUtils.getNavigationLanguage(request).getCode())
                            .iterator();
                    while (iterator.hasNext()) {
                        TrnCountry item = (TrnCountry) iterator.next();
                        countriesMap.put(item.getIso(), item);
                    }
                    // sort countries
                    Collection<CountryBean> countrieCol = org.digijava.module.aim.util.DbUtil.getTranlatedCountries(request);

//                  if (countrieCol != null) {
//                                  sortedCountries.add(0, new Country( new Long( 0
//                       ),null,"Select a country",null,null,null,null ) );
//                      sortedCountries
//                              .add(0, new TrnCountry("-1", "-- Select a country --"));
//                  }
                    registerForm.setCountryResidence(countrieCol);
                    logger.debug("sortedCountries.size : " + countrieCol.size());

                    //set default country from global settings.
                    registerForm.setSelectedCountryResidence(FeaturesUtil.getGlobalSettingValue(Constants.GLOBAL_DEFAULT_COUNTRY));
                    List<AmpGlobalSettings> settings = getGlobalSettingsBySection("trubudget");
                    registerForm.setTruBudgetEnabled(getSettingValue(settings,"isEnabled"));
                    
                    
                    // set default web site
                    registerForm.setWebSite("http://");

                    // set Navigation languages
                    Set languages = SiteUtils.getUserLanguages(RequestUtils
                            .getSite(request));

                    HashMap translations = new HashMap();
                    iterator = TrnUtil.getLanguages(
                            RequestUtils.getNavigationLanguage(request).getCode())
                            .iterator();
                    while (iterator.hasNext()) {
                        TrnLocale item = (TrnLocale) iterator.next();
                        translations.put(item.getCode(), item);
                    }
                    //sort languages
                    List sortedLanguages = new ArrayList();
                    iterator = languages.iterator();
                    while (iterator.hasNext()) {
                        Locale item = (Locale) iterator.next();
                        sortedLanguages.add(translations.get(item.getCode()));
                    }
                    Collections.sort(sortedLanguages, TrnUtil.localeNameComparator);

                    registerForm.setNavigationLanguages(sortedLanguages);

                    // set organisation types
                    registerForm.setOrgTypeColl(DbUtil.getAllOrgTypes());

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
}
