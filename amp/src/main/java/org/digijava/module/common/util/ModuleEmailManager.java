/*
 *   ModuleEmailManager.java
 *   @Author Maka Kharalashvili maka@digijava.org
 *   Created: Apr 21, 2004
 *  CVS-ID: $Id$
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

package org.digijava.module.common.util;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;

import org.apache.log4j.Logger;
import org.digijava.kernel.entity.Locale;
import org.digijava.kernel.entity.ModuleInstance;
import org.digijava.kernel.mail.DgEmailManager;
import org.digijava.kernel.request.Site;
import org.digijava.kernel.security.ResourcePermission;
import org.digijava.kernel.translator.TranslatorWorker;
import org.digijava.kernel.user.User;
import org.digijava.kernel.util.DgUtil;
import org.digijava.kernel.util.DigiConfigManager;
import org.digijava.kernel.util.SiteUtils;

/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2004</p>
 * <p>Company: </p>
 * @author not attributable
 * @version 1.0
 */

public class ModuleEmailManager {

    private static Logger logger = Logger.getLogger(ModuleEmailManager.class);

    /**
     * Sends alert email to admins if the module mode is set-up as moderated
     * @param moduleInstance Module Instance
     * @param title title of created item
     * @param source source name of created item
     * @param url source url of created item
     * @param description description of created item
     * @param link reference link to site, where created item can be viewed
     * @throws java.lang.Exception
     */
    public static void sendAdminEmail(ModuleInstance moduleInstance,
                                      String title,
                                      String sourceName, String sourceUrl,
                                      String description, String link) throws
        java.lang.
        Exception {

        String from = null;
        String subject = null;
        String message = null;
        //
        Locale locale = null;
        //
        Site site = moduleInstance.getSite();
        String moduleName = moduleInstance.getModuleName();
        String siteName = moduleInstance.getSite().getName();
        //
        Set adminList = null;
        if (SiteUtils.isSendAlertsToAdmin(site)) {
            logger.debug(
                "Sending email alerts to site admins and module admins");
            adminList = DgUtil.getSitePermittedUsers(site,
                ResourcePermission.ADMIN);
            adminList.addAll(DgUtil.getModuleInstancePermittedUsers(
                moduleInstance, ResourcePermission.ADMIN, false));
        }
        else {
            // get current module instance administrators
            logger.debug("Sending email alerts to module admins only");
            adminList = DgUtil.getModuleInstancePermittedUsers(
                moduleInstance, ResourcePermission.ADMIN, false);
        }
        //
        if (adminList != null) {
            Iterator iter = adminList.iterator();
            while (iter.hasNext()) {
                User currAdmin = (User) iter.next();

                if (currAdmin.getUserLangPreferences() != null) {
                    locale = currAdmin.getUserLangPreferences().
                        getAlertsLanguage();
                    if (locale == null) {
                        locale = SiteUtils.getDefaultLanguages(site);
                        if (locale == null) {
                            locale = new Locale();
                            locale.setCode("en");
                        }
                    }
                }
                else {
                    locale = SiteUtils.getDefaultLanguages(site);
                    if (locale == null) {
                        locale = new Locale();
                        locale.setCode("en");
                    }

                }

                //construct from
                TranslatorWorker trnWorker = TranslatorWorker.getInstance(
                    "alerts:newcontent:emailAdmin");

                from = trnWorker.getFromGroup(
                    "alerts:newcontent:emailAdmin", locale.getCode(),
                    site,
                    DigiConfigManager.getConfig().getSmtp().getFrom()).
                    getMessage();

                //construct subject
                trnWorker = TranslatorWorker.getInstance(
                    "alerts:newcontent:subject");
                subject = trnWorker.
                    getFromGroup(
                    "alerts:newcontent:subject", locale.getCode(),
                    site,
                    (String) ("New content awaiting review" + " {" + moduleName +
                              "}")).
                    getMessage();

                HashMap hMap = new HashMap();

                hMap.put("siteName", siteName);
                hMap.put("moduleName", moduleName);
                hMap.put("title", title);
                hMap.put("sourceName", sourceName);
                hMap.put("sourceUrl", sourceUrl);
                hMap.put("description", description);
                hMap.put("link", link);

                subject = DgUtil.fillPattern(subject, hMap);

                //contruct message
                trnWorker = TranslatorWorker.getInstance(
                    "alerts:newcontent:body");
                message = trnWorker.
                    getFromGroup(
                    "alerts:newcontent:body", locale.getCode(),
                    site,
                    (String) ("There is new content awaiting review on {" +
                              siteName +
                              "} page: \n" +
                              "\n" +
                              "Title: {" + title + "} \n" +
                              "Source: {" + sourceName + "} \n" +
                              "URL: {" + sourceUrl + "} \n" +
                              "Description: {" + description + " }\n" +
                              "Submitted to: {" + siteName + "} \n" +
                              "\n" +
                              "Use the link below to review this content:\n {" +
                              link +
                              "}")).getMessage();

                message = DgUtil.fillPattern(message, hMap);

                DgEmailManager.sendMail(currAdmin.getEmailUsedForNotification(), from,
                                        subject, message,
                                        locale, true);


            }
        }

    }

    /**
     * Sends notification email alert to specified user, if the module mode is set-up as moderated
     * @param moduleInstance Module Instance
     * @param user author user of an item, to whom notification email alert is sent to
     * @param title title of an item, whos author is user
     * @param url source url of an item, whos author is user
     * @throws java.lang.Exception
     */
    public static void sendUserEmail(ModuleInstance moduleInstance,
                                     User user,
                                     String title,
                                     String url, String type) throws Exception {

        String from = null;
        String subject = null;
        String message = null;
        //
        Locale locale = null;

        //
        Site site = moduleInstance.getSite();

        String moduleName = moduleInstance.getModuleName();
        String instanceName = moduleInstance.getInstanceName();
        String siteName = moduleInstance.getSite().getName();
        //

        if (user.getUserLangPreferences() != null) {
            locale = user.getUserLangPreferences().getAlertsLanguage();
            if (locale == null) {
                locale = SiteUtils.getDefaultLanguages(site);
                if (locale == null) {
                    locale = new Locale();
                    locale.setCode("en");
                }
            }
        }
        else {
            locale = SiteUtils.getDefaultLanguages(site);
            if (locale == null) {
                locale = new Locale();
                locale.setCode("en");
            }

        }

        //construct from
        from = TranslatorWorker.getInstance("alerts:newcontent:emailUser").
            getFromGroup(
            "alerts:newcontent:emailUser", locale.getCode(),
            site,
            DigiConfigManager.getConfig().getSmtp().getFrom()).getMessage();

        //construct subject
        String subjectKey = moduleName + ":" + instanceName + ":alert:" + type +
            ":subject";
        subject = TranslatorWorker.getInstance(subjectKey).
            getFromGroup(
            subjectKey, locale.getCode(),
            site, (String) ("Your {" + siteName + "} content")).
            getMessage();

        HashMap hMap = new HashMap();

        hMap.put("siteName", siteName);
        hMap.put("moduleName", moduleName);
        hMap.put("title", title);
        hMap.put("sourceUrl", url);

        subject = DgUtil.fillPattern(subject, hMap);

        //contruct message
        String messageKey = moduleName + ":" + instanceName + ":alert:" + type +
            ":body";
        message = TranslatorWorker.getInstance(messageKey).
            getFromGroup(
            messageKey, locale.getCode(),
            site,
            (String) ("{" + siteName + "} \n" + "Title: {" + title + "} \n" +
                      "URL: {" + url + "}")).
            getMessage();

        message = DgUtil.fillPattern(message, hMap);

        DgEmailManager.sendMail(user.getEmailUsedForNotification(), from,
                                subject, message,
                                locale, true);

    }

}
