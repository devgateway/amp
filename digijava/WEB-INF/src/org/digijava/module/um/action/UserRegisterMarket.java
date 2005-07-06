/*
 *   UserRegisterMarket.java
 *   @Author Lasha Dolidze lasha@digijava.org
 * 	 Created: Aug 3, 2003
 * 	 CVS-ID: $Id: UserRegisterMarket.java,v 1.1 2005-07-06 10:34:07 rahul Exp $
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

import java.util.*;
import javax.mail.Address;
import javax.mail.internet.*;

import org.apache.log4j.*;
import org.apache.struts.action.*;
import org.digijava.kernel.*;
import org.digijava.kernel.config.*;
import org.digijava.kernel.dbentity.*;
import org.digijava.kernel.entity.*;
import org.digijava.kernel.entity.Locale;
import org.digijava.kernel.entity.Message;
import org.digijava.kernel.mail.*;
import org.digijava.kernel.request.*;
import org.digijava.kernel.security.*;
import org.digijava.kernel.translator.*;
import org.digijava.kernel.user.*;
import org.digijava.kernel.util.*;
import org.digijava.module.um.form.*;
import org.digijava.module.um.util.*;

/**
 * <p>Title: DiGiJava</p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company: </p>
 * @author not attributable
 * @version 1.0 UserRegisterAction
 */

public class UserRegisterMarket
    extends Action {

    // log4J class initialize String
    private static Logger logger = I18NHelper.getKernelLogger(
        UserRegisterMarket.class);

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

        // set Website
        user.setUrl(userRegisterForm.getWebSite());

        // register through
        user.setRegisteredThrough(RequestUtils.getSite(request));

        // set mailing address
        user.setAddress(userRegisterForm.getMailingAddress());

        // set organization name
        user.setOrganizationName(userRegisterForm.getOrganizationName());

        // set country
        user.setCountry(new Country(userRegisterForm.
                                    getSelectedCountryResidence()));

        // set default language
        user.setRegisterLanguage(RequestUtils.getNavigationLanguage(request));

        SiteDomain siteDomain = (SiteDomain) request.getAttribute(Constants.
            CURRENT_SITE);

        // ------------- SET USER LANGUAGES
        UserLangPreferences userLangPreferences = new UserLangPreferences(user,
            DgUtil.getRootSite(siteDomain.getSite()));
        Locale language = new Locale();
        language.setCode(userRegisterForm.getSelectedLanguage());

        // set alert language
        userLangPreferences.setAlertsLanguage(language);

        // set navigation language
        userLangPreferences.setNavigationLanguage(RequestUtils.getNavigationLanguage(
            request));

        user.setUserLangPreferences(userLangPreferences);
        // --------------------------------

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
            String sessionId = HttpLoginManager.loginByCredentials(request,
                      response, user.getEmail(), user.getPassword(),
                      false);
            //DgSecurityManager.isUserLogon(user.getEmail(), user.getPassword(), request);

            // send mail alert
            sendEmail(user, password, request);
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
        Message message;
        String youHaveRegistered;
        String thankYou;
        String siteName;
        String domanName = DgUtil.getCurrRootUrl(request);
        String youProvided;
        String nameC;
        String emailShort;
        String passwordC;
        String saveMessage;
        String freeAlerts;
        String youCanReceive;
        String visitC;
        String emailFrom;

        TranslatorWorker worker = TranslatorWorker.getInstance("ep:");

        Locale currentLocale = RequestUtils.getNavigationLanguage(request);

        // get SiteName
        message = worker.get("param:SiteName",
                             currentLocale.getCode(), site.getId().toString());
        if (message == null) {
            siteName = site.getName();
        }
        else {
            siteName = message.getMessage();
        }

        // get ThankYou
        message = worker.get("ep:login:ThankYou",
                             currentLocale.getCode(), site.getId().toString());
        if (message == null) {
            thankYou = "Thank you for registering at " + siteName + "!";
        }
        else {
            thankYou = message.getMessage();
            HashMap map = new HashMap();
            map.put("sitename", siteName);
            thankYou = DgUtil.fillPattern(thankYou, map);
        }

        // get YouHaveRegistered
        message = worker.get("ep:login:YouHaveRegistered",
                             currentLocale.getCode(), site.getId().toString());
        if (message == null) {
            youHaveRegistered = "You have registered with";
        }
        else {
            youHaveRegistered = message.getMessage();
        }

        // get YouProvided
        message = worker.get("ep:login:YouProvided", currentLocale.getCode(),
                             site.getId().toString());
        if (message == null) {
            youProvided =
                "You have provided us with the following account information";
        }
        else {
            youProvided = message.getMessage();
        }

        // ep:name_c
        message = worker.get("ep:name_c", currentLocale.getCode(),
                             site.getId().toString());
        if (message == null) {
            nameC = "Name";
        }
        else {
            nameC = message.getMessage();
        }

        // ep:EmailShort
        message = worker.get("ep:EmailShort", currentLocale.getCode(),
                             site.getId().toString());
        if (message == null) {
            emailShort = "Email";
        }
        else {
            emailShort = message.getMessage();
        }

        // ep:password_c
        message = worker.get("ep:password_c", currentLocale.getCode(),
                             site.getId().toString());
        if (message == null) {
            passwordC = "Password";
        }
        else {
            passwordC = message.getMessage();
        }

        // ep:login:SaveMessage
        message = worker.get("ep:login:SaveMessage", currentLocale.getCode(),
                             site.getId().toString());
        if (message == null) {
            saveMessage =
                "We suggest you save this message in case you forget your account information.";
        }
        else {
            saveMessage = message.getMessage();
        }

        // ep:login:FREEALERTS
        message = worker.get("ep:login:FREEALERTS", currentLocale.getCode(),
                             site.getId().toString());
        if (message == null) {
            freeAlerts = "FREE ALERTS";
        }
        else {
            freeAlerts = message.getMessage();
        }

        // ep:login:YouCanReceive
        message = worker.get("ep:login:YouCanReceive", currentLocale.getCode(),
                             site.getId().toString());
        if (message == null) {
            youCanReceive = "As a registered user, you can receive automated email alerts whenever a tender notice that meets your specific business interest is published.";
        }
        else {
            youCanReceive = message.getMessage();
        }

        // ep:visit_c
        message = worker.get("ep:visit_c", currentLocale.getCode(),
                             site.getId().toString());
        if (message == null) {
            visitC = "Visit";
        }
        else {
            visitC = message.getMessage();
        }

        // param:email:support
        Smtp smtp = DigiConfigManager.getConfig().getSmtp();
        message = worker.get("param:email:support", currentLocale.getCode(),
                             site.getId().toString());
        if (message == null) {
            emailFrom = smtp.getFrom();
        }
        else {
            message = worker.get("param:email:support", "en",
                                 site.getId().toString());
            if (message == null) {
                emailFrom = smtp.getFrom();
            }
            else {
                emailFrom = message.getMessage();
            }
        }

        String subject = thankYou;
        String text =
            youHaveRegistered + " " + siteName + " (" + domanName + ").\n\n" +
            youProvided + ":\n" +
            nameC + ": " + user.getName() + "\n" +
            emailShort + ": " + user.getEmail() + "\n" +
            passwordC + ": " + password + "\n\n" +
            saveMessage + "\n\n" +
            freeAlerts + "\n" +
            youCanReceive + "\n" +
            visitC + " " + domanName + "/eproc/NoticeAlert.do\n";

        InternetAddress address = new InternetAddress(user.getEmail());
        DgEmailManager.sendMail(new Address[] {address},
                                "\"" + siteName + "\" <" + emailFrom + ">",
                                subject, text, currentLocale,true);
    }

}