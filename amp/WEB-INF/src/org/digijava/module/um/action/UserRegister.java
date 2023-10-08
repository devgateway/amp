/**
 * This file is part of DiGi project (www.digijava.org).
 * DiGi is a multi-site portal system written in Java/J2EE.
 *
 * Copyright (C) 2002-2007 Development Gateway Foundation, Inc.
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301,
 * USA.
 */

package org.digijava.module.um.action;

import org.apache.log4j.Logger;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.digijava.kernel.Constants;
import org.digijava.kernel.dbentity.Country;
import org.digijava.kernel.entity.Locale;
import org.digijava.kernel.entity.OrganizationType;
import org.digijava.kernel.entity.UserLangPreferences;
import org.digijava.kernel.entity.UserPreferences;
import org.digijava.kernel.entity.trubudget.TruBudgetIntent;
import org.digijava.kernel.request.SiteDomain;
import org.digijava.kernel.user.User;
import org.digijava.kernel.util.DgUtil;
import org.digijava.kernel.util.I18NHelper;
import org.digijava.kernel.util.RequestUtils;
import org.digijava.module.aim.dbentity.AmpGlobalSettings;
import org.digijava.module.um.form.UserRegisterForm;
import org.digijava.module.um.util.DbUtil;
import org.digijava.module.um.util.UmUtil;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.digijava.module.um.util.DbUtil.*;

/**
 * <p>Title: DiGiJava</p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company: </p>
 * @author not attributable
 * @version 1.0 UserRegisterAction
 * @deprecated
 */

public class UserRegister
    extends Action {

    // log4J class initialize String
    private static Logger logger = I18NHelper.getKernelLogger(
        UserRegister.class);

    public ActionForward execute(ActionMapping mapping,
                                 ActionForm form,
                                 javax.servlet.http.HttpServletRequest request,
                                 javax.servlet.http.HttpServletResponse
                                 response) throws
        java.lang.Exception {

        UserRegisterForm userRegisterForm = (UserRegisterForm) form;

        if (userRegisterForm == null) {
            logger.warn("UserRegisterForm is null");
        }

        // create new user class
        User user = new User(userRegisterForm.getEmail().toLowerCase(),
                             userRegisterForm.getFirstNames(),
                             userRegisterForm.getLastName());

        // set client IP address
        user.setModifyingIP(RequestUtils.getRemoteAddress(request));

        // set password
        user.setPassword(userRegisterForm.getPassword().trim());
        user.setSalt(userRegisterForm.getPassword().trim());

        // set Website
        user.setUrl(userRegisterForm.getWebSite());

        // set mailing address
        user.setAddress(userRegisterForm.getMailingAddress());

//set trubudget details
        List<AmpGlobalSettings> settings = getGlobalSettingsBySection("trubudget");

        if (getSettingValue(settings,"isEnabled").equalsIgnoreCase("true")) {
            String keyGen = UmUtil.generateAESKey(128);
            user.setTruBudgetKeyGen(keyGen);
            String encryptedTruPassword = UmUtil.encrypt(userRegisterForm.getTruBudgetPassword(), keyGen);
            user.setTruBudgetPassword(encryptedTruPassword);
            String[] intents = userRegisterForm.getSelectedTruBudgetIntents();
            List<TruBudgetIntent> truBudgetIntents = new ArrayList<>();
            if (intents != null) {
                truBudgetIntents = getTruBudgetIntentsByName(intents);
            }
            logger.info("Intents: " + truBudgetIntents);
            user.setInitialTruBudgetIntents(new HashSet<>(user.getTruBudgetIntents()));
            user.setTruBudgetIntents(new HashSet<>(truBudgetIntents));
        }

        // set organization name
        user.setOrganizationName(userRegisterForm.getOrganizationName());

        // set organization type
        user.setOrganizationType(new OrganizationType(userRegisterForm.getSelectedOrganizationType()));

        // set country
        user.setCountry( new Country(userRegisterForm.getSelectedCountryResidence()) );

        // set referral
        user.setReferral(userRegisterForm.getHowDidyouSelect());

        // set default language
        user.setRegisterLanguage(RequestUtils.getNavigationLanguage(request));

        // ----- Fill user preferences
        UserPreferences preference = user.getUserPreference();
        if( preference == null ) {
            SiteDomain siteDomain = (SiteDomain) request.getAttribute(Constants.CURRENT_SITE);

            // ------------- SET USER PREFERENCE
            preference = new UserPreferences(user, siteDomain.getSite());

            preference.setPublicProfile(userRegisterForm.getMembersProfile());
            preference.setReceiveAlerts(userRegisterForm.isNewsLetterRadio());
            user.setUserPreference(preference);
            // -----------------

            // ------------- SET USER LANGUAGES
            UserLangPreferences userLangPreferences = new UserLangPreferences(user, DgUtil.getRootSite(siteDomain.getSite()));
            Locale language = new Locale();
            language.setCode(userRegisterForm.getSelectedLanguage());

            // set alert language
            userLangPreferences.setAlertsLanguage(language);

            // set navigation language
            userLangPreferences.setNavigationLanguage(RequestUtils.getNavigationLanguage(request));


            // set content language
            Set list = new HashSet();
            for( int i =  0; i < userRegisterForm.getContentSelectedLanguages().length; i++ ) {
                language = new Locale();
                language.setCode(userRegisterForm.getContentSelectedLanguages()[i]);
                list.add(language);
            }
            userLangPreferences.setContentLanguages(list);

            user.setUserLangPreferences(userLangPreferences);
            // --------------------------------
        }
       // -----

       // if email register get error message
        if (DbUtil.isRegisteredEmail(user.getEmail())) {

            // if set debug mode then print out
            if (logger.isDebugEnabled()) {
                logger.debug(String.format("Email %s already exists", user.getEmail()));
            }

            // email already exists
/*            ActionMessages errors = new ActionMessages();
            errors.add(ActionMessages.GLOBAL_MESSAGE,
                       new ActionMessage("error.registration.emailexits"));
            saveErrors(request, errors);
            return (new ActionForward(mapping.getInput())); */
            return (mapping.findForward("failure"));
        }
        else {

            // register user in database
            DbUtil.registerUser(user);

            // send mail
//            DgUtil.sendMail(userRegisterForm.getEmail(), "DIGI mailer",
//                          "Thank you Message");
            // -----------------------------------------

            // if set debug mode then print out
            if (logger.isDebugEnabled()) {
                logger.debug(String.format("The message send to %s", user.getEmail()));
            }
        }
        //////////////
        return mapping.findForward("success");
    }

}
