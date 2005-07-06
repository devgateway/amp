/*
 *   UserRegisterGateway.java
 *   @Author Lasha Dolidze lasha@digijava.org
 * 	 Created: Aug 3, 2003
 * 	 CVS-ID: $Id: UserRegisterGateway.java,v 1.1 2005-07-06 10:34:07 rahul Exp $
 *
 *   This file is part of DiGi project (www.digijava.org).
 *   DiGi is a multi-site portal system written in Java/J2EE.
 *
 *   Confidential and Proprietary, Subject to the Non-Disclosure
 *   Agreement, Version 1.0, between the Development Gateway
 *   Foundation, Inc and the Recipient -- Copyright 2001-2004 Development
 *   Gateway Foundation, Inc.
 *
 *   Unauthorized Disclosure Prohibited.
 *
 *************************************************************************/

package org.digijava.module.um.action;

import java.util.HashSet;
import java.util.Set;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.digijava.kernel.Constants;
import org.digijava.kernel.dbentity.Country;
import org.digijava.kernel.entity.Locale;
import org.digijava.kernel.entity.OrganizationType;
import org.digijava.kernel.entity.UserPreferences;
import org.digijava.kernel.request.SiteDomain;
import org.digijava.kernel.user.User;
import org.digijava.kernel.util.I18NHelper;
import org.digijava.module.um.form.UserRegisterForm;
import org.digijava.module.um.util.DbUtil;
import org.digijava.kernel.entity.UserLangPreferences;
import org.digijava.kernel.util.DgUtil;
import org.digijava.kernel.request.Site;
import org.digijava.kernel.entity.Interests;
import java.util.*;
import org.digijava.kernel.security.DgSecurityManager;
import org.digijava.kernel.entity.ContentAlert;
import org.digijava.kernel.mail.DgEmailManager;
import org.digijava.kernel.util.RequestUtils;
import org.digijava.kernel.security.HttpLoginManager;
import org.digijava.kernel.util.SiteUtils;
import org.digijava.kernel.util.DigiConfigManager;
import org.digijava.kernel.config.Smtp;
import javax.mail.internet.InternetAddress;
import javax.mail.Address;
import org.digijava.kernel.translator.TranslatorWorker;
import org.digijava.kernel.entity.Message;
import org.digijava.module.um.util.UmUtil;

/**
 * <p>Title: DiGiJava</p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company: </p>
 * @author not attributable
 * @version 1.0 UserRegisterAction
 */

public class UserRegisterGateway
    extends Action {

    // log4J class initialize String
    private static Logger logger = I18NHelper.getKernelLogger(
        UserRegisterGateway.class);

    public ActionForward execute(ActionMapping mapping,
                                 ActionForm form,
                                 javax.servlet.http.HttpServletRequest request,
                                 javax.servlet.http.HttpServletResponse
                                 response) throws
        java.lang.Exception {

        UserRegisterForm userRegisterForm = (UserRegisterForm) form;

        if (userRegisterForm == null) {
            String debugKey = "Module.Um.UserRegisterAction.formnull";
            logger.l7dlog(Level.WARN, debugKey, null, null);
        }

        // create new user class
        User user = new User(userRegisterForm.getEmail().toLowerCase(),
                             userRegisterForm.getFirstNames(),
                             userRegisterForm.getLastName());

        String password = userRegisterForm.getPassword().trim();

        // set client IP address
        user.setModifyingIP(RequestUtils.getRemoteAddress(request));

        // set password
        user.setPassword(password);
        user.setSalt(userRegisterForm.getPassword().trim());

        // register through
        user.setRegisteredThrough(RequestUtils.getSite(request));

        // set Website
        user.setUrl(userRegisterForm.getWebSite());

        // set mailing address
        user.setAddress(userRegisterForm.getMailingAddress());

        // set organization name
        user.setOrganizationName(userRegisterForm.getOrganizationName());

        // set other organization from input box
        user.setOrganizationTypeOther(userRegisterForm.getOrganizationTypeOther());

        user.setActive(true);
        user.setEmailVerified(true);

        if (userRegisterForm.getSelectedOrganizationType().equalsIgnoreCase(
            "-1")) {
            user.setOrganizationType(null);
        }
        else {
            logger.debug("Organization is " +
                         userRegisterForm.getSelectedOrganizationType());
            // set organization type
            user.setOrganizationType(new OrganizationType(userRegisterForm.
                getSelectedOrganizationType()));
        }

        // set country
        user.setCountry(new Country(userRegisterForm.
                                    getSelectedCountryResidence()));

        // set referral
        user.setReferral(userRegisterForm.getHowDidyouSelect());

        // set default language
        user.setRegisterLanguage(RequestUtils.getNavigationLanguage(request));

        // ----- Fill user preferences
        UserPreferences preference = user.getUserPreference();
        if (preference == null) {
            SiteDomain siteDomain = (SiteDomain) request.getAttribute(Constants.
                CURRENT_SITE);

            // ------------- SET USER PREFERENCE
            preference = new UserPreferences(user,
                                             DgUtil.getRootSite(siteDomain.
                getSite()));

            preference.setPublicProfile(userRegisterForm.getMembersProfile());
            preference.setReceiveAlerts(userRegisterForm.isNewsLetterRadio());
            user.setUserPreference(preference);
            // -----------------

            // ------------- SET USER LANGUAGES
            UserLangPreferences userLangPreferences = new UserLangPreferences(
                user, DgUtil.getRootSite(siteDomain.getSite()));
            Locale language = new Locale();
            language.setCode(userRegisterForm.getSelectedLanguage());

            // set alert language
            userLangPreferences.setAlertsLanguage(language);

            // set navigation language
            userLangPreferences.setNavigationLanguage(RequestUtils.
                getNavigationLanguage(request));

            // set content language
            Set list = new HashSet();
            for (int i = 0;
                 i < userRegisterForm.getContentSelectedLanguages().length; i++) {
                language = new Locale();
                language.setCode(userRegisterForm.getContentSelectedLanguages()[
                                 i]);
                list.add(language);
            }
            userLangPreferences.setContentLanguages(list);

            user.setUserLangPreferences(userLangPreferences);
            // --------------------------------
        }
        // -----

        // Topics --------------
        if (userRegisterForm.getTopicselectedItems() != null) {
            Set list = new HashSet();
            Iterator iter = userRegisterForm.getTopicitems().iterator();
            while (iter.hasNext()) {
                Interests item = (Interests) iter.next();
                for (int i = 0;
                     i < userRegisterForm.getTopicselectedItems().length; i++) {
                    if (item.getSite().getId().longValue() ==
                        Long.parseLong(userRegisterForm.getTopicselectedItems()[
                                       i])) {
                        Interests interests = new Interests();
                        interests.setSite(item.getSite());
                        interests.setUser(user);
                        interests.setJoinDate(new Date());
                        interests.setContentAlert( (new ContentAlert(item.
                            getContentAlert().getValue(),
                            item.getContentAlert().getName())));
                        interests.setReceiveAlerts(true);
                        list.add(interests);
                        break;
                    }
                }
            }

            if( userRegisterForm.isNewsLetterRadio() ) {
                Site site = SiteUtils.getSite(Constants.ALL_TOPICS_SITE);
                Site parentSite = null;
                if (site.getParentId() != null) {
                    parentSite = SiteUtils.getSite(site.getParentId());
                    Interests interests = new Interests();
                    interests.setSite(parentSite);
                    interests.setUser(user);
                    interests.setJoinDate(new Date());
                    interests.setContentAlert( (new ContentAlert(new Long(1296000),"")));
                    interests.setReceiveAlerts(true);
                    list.add(interests);
                }
            }
            // --------------

            user.setInterests(list);
        }
        // if email register get error message
        if (DbUtil.isRegisteredEmail(user.getEmail())) {

            // if set debug mode then print out
            if (logger.isDebugEnabled()) {
                String debugKey = "Module.Um.UserRegisterAction.emailexits";
                Object params[] = {
                    user.getEmail()};
                logger.l7dlog(Level.DEBUG, debugKey, params, null);
            }

            // email already exists
            ActionErrors errors = new ActionErrors();
            errors.add(ActionErrors.GLOBAL_ERROR,
                       new ActionError("error.registration.emailexits"));
            saveErrors(request, errors);
            return (new ActionForward(mapping.getInput()));
        }
        else {

            // register user in database
            DbUtil.registerUser(user);

            // log in
//            DgSecurityManager.isUserLogon(user.getEmail(), user.getPassword(), request);
            String sessionId = HttpLoginManager.loginByCredentials(request,
                response, user.getEmail(), user.getPassword(),
                false);

            // send mail
/*            DgEmailManager.sendMail(userRegisterForm.getEmail(), "DIGI mailer",
                                    "Gateway Thank you Message",
                                    RequestUtils.getNavigationLanguage(request));*/

            sendEmail(user, password,request);
            // -----------------------------------------

            // if set debug mode then print out
            if (logger.isDebugEnabled()) {
                String debugKey = "Module.Um.UserRegisterAction.mailsend";
                Object params[] = {
                    user.getEmail()};
                logger.l7dlog(Level.DEBUG, debugKey, params, null);
            }

            SiteDomain currentDomain = RequestUtils.getSiteDomain(request);
            String rootUrl = SiteUtils.getSiteURL(currentDomain,
                                                  request.getScheme(),
                                                  request.getServerPort(),
                                                  request.getContextPath()) +
                "/um/user/showUserRegisterSuccess.do";

            HttpLoginManager.passSessionIdToReferrer(request, response,
                sessionId, rootUrl);
        }
        //////////////
        return mapping.findForward("success");
    }


    /**
     * Send email alert
     *
     * @param user
     * @throws java.lang.Exception
     */
    public void sendEmail(User user, String password,
                          javax.servlet.http.HttpServletRequest request) throws
        java.lang.Exception {

        Site site = RequestUtils.getSite(request);
        String domanName = DgUtil.getCurrRootUrl(request);
        Message message;
        String siteName;
        String dear;
        String thankYou;
        String confirmation;
        String myAccount;
        String firstNameC;
        String lastNameC;
        String emailC;
        String passwordC;
        String organizationTypeC;
        String organizationNameC;
        String mailingAddressC;
        String residenceC;
        String yourWebsiteC;
        String toModify;
        String theDevelopment;
        String please;
        String emailFrom;


        TranslatorWorker worker = TranslatorWorker.getInstance("ep:");
        Locale currentLocale = new Locale();//RequestUtils.getNavigationLanguage(request);
        currentLocale.setCode("en");

        // get SiteName
        message = worker.get("param:SiteName",
                             currentLocale.getCode(), site.getId().toString());
        if (message == null) {
            siteName = site.getName();
        }
        else {
            siteName = message.getMessage();
        }

        // get dear
        message = worker.get("ep:dear_c", currentLocale.getCode(), site.getId().toString());
        if (message == null) {
            dear = "Dear";
        }
        else {
            dear = message.getMessage();
        }
        // -------

        // get thank You
        thankYou = "Thank you for registering with the Development Gateway, an Internet portal for knowledge sharing and resources on development issues. Please keep this message for your records.";
        // ------------

        // get confirmation
        confirmation = "Here is confirmation of your registration:";
        // ---------------

        // get my_account
        message = worker.get("registr:my_account", currentLocale.getCode(), site.getId().toString());
        if (message == null) {
            myAccount = "Account information";
        }
        else {
            myAccount = message.getMessage();
        }
        // -------

        // get firstNameC
        message = worker.get("register:first_name", currentLocale.getCode(), site.getId().toString());
        if (message == null) {
            firstNameC = "First name:";
        }
        else {
            firstNameC = message.getMessage();
        }
        // -------

        // get lastNameC
        message = worker.get("register:last_name", currentLocale.getCode(), site.getId().toString());
        if (message == null) {
            lastNameC = "Last name:";
        }
        else {
            lastNameC = message.getMessage();
        }
        // -------

        // get emailC
        message = worker.get("register:email", currentLocale.getCode(), site.getId().toString());
        if (message == null) {
            emailC = "Email:";
        }
        else {
            emailC = message.getMessage();
        }
        // -------

        // get passwordC
        message = worker.get("register:password", currentLocale.getCode(), site.getId().toString());
        if (message == null) {
            passwordC = "Password:";
        }
        else {
            passwordC = message.getMessage();
        }
        // -------

        // get organizationTypeC
        message = worker.get("register:organization_type", currentLocale.getCode(), site.getId().toString());
        if (message == null) {
            organizationTypeC = "Organization type:";
        }
        else {
            organizationTypeC = message.getMessage();
        }
        // -------

        // get organizationNameC
        message = worker.get("register:organization_name", currentLocale.getCode(), site.getId().toString());
        if (message == null) {
            organizationNameC = "Organization name:";
        }
        else {
            organizationNameC = message.getMessage();
        }
        // -------

        // get mailingAddressC
        message = worker.get("register:mailing_address", currentLocale.getCode(), site.getId().toString());
        if (message == null) {
            mailingAddressC = "Mailing address:";
        }
        else {
            mailingAddressC = message.getMessage();
        }
        // -------

        // get residenceC
        message = worker.get("register:residence", currentLocale.getCode(), site.getId().toString());
        if (message == null) {
            residenceC = "Country of residence:";
        }
        else {
            residenceC = message.getMessage();
        }
        // -------

        // get yourWebsiteC
        message = worker.get("register:your_website", currentLocale.getCode(), site.getId().toString());
        if (message == null) {
            yourWebsiteC = "Your website:";
        }
        else {
            yourWebsiteC = message.getMessage();
        }
        // -------

        toModify = "To modify this information or subscribe to more subject areas please visit 'My Gateway' at:";
        theDevelopment = "The Development Gateway is about knowledge sharing, and the submission of content from its users is critical. Please add a website, an article, or any other material that you feel should be included in the knowledge base. ";
        please = "Please do not reply to this e-mail. We welcome your feedback at:";

        // param:email:support
        Smtp smtp = DigiConfigManager.getConfig().getSmtp();
        message = worker.getFromGroup("param:email:support", currentLocale.getCode(),
                             site, "\"" + siteName + "\" <" + smtp.getFrom() + ">");
                emailFrom = message.getMessage();


        String subject = myAccount;
        String text =
            dear + " " + user.getName() + "\n\n" +
            thankYou + "\n\n" +
            confirmation + "\n\n" +
            "***************\n" + myAccount + "\n***************\n\n" +
            firstNameC + " " + user.getFirstNames() + "\n" +
            lastNameC + " " + user.getLastName() + "\n" +
            emailC + " " + user.getEmail() + "\n" +
            passwordC + " " + password + "\n";
            if( user.getOrganizationType() != null ) {
                text += organizationTypeC + " " + UmUtil.getOrganizationTypeById(user.getOrganizationType().getId()) + "\n";
            } else {
                text += organizationTypeC + " \n";
            }
            if( user.getOrganizationName() != null ) {
                text += organizationNameC + " " + user.getOrganizationName() + "\n";
            } else {
                text += organizationNameC + " \n";
            }
            if( user.getAddress() != null ) {
                text += mailingAddressC + " " + user.getAddress() + "\n";
            } else {
                text += mailingAddressC + " \n";
            }
            if( user.getCountry() != null ) {
                text += residenceC + " " + UmUtil.getCountryNameByIso( user.getCountry().getIso() ) + "\n";
            } else {
                text += residenceC + " \n";
            }
            if( user.getUrl() != null ) {
                text += yourWebsiteC + " " + user.getUrl() + "\n\n";
            } else {
                text += yourWebsiteC + " \n\n";
            }
            text += toModify + "\n" + domanName + "/um/user/showUserAccount.do" + "\n\n" +
            theDevelopment + "\n\n" +
            please + "\n" + "http://www.developmentgateway.org/aboutus/feedback" + "\n\n";

        InternetAddress address = new InternetAddress(user.getEmail());
        DgEmailManager.sendMail(new Address[] {address},
                                emailFrom,
                                subject, text, currentLocale,true);
    }

}
