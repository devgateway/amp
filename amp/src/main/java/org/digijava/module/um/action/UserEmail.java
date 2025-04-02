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

import java.util.HashMap;

import javax.mail.Address;
import javax.mail.internet.InternetAddress;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.digijava.kernel.config.Smtp;
import org.digijava.kernel.entity.Locale;
import org.digijava.kernel.entity.Message;
import org.digijava.kernel.mail.DgEmailManager;
import org.digijava.kernel.request.Site;
import org.digijava.kernel.translator.TranslatorWorker;
import org.digijava.kernel.user.User;
import org.digijava.kernel.util.DgUtil;
import org.digijava.kernel.util.DigiConfigManager;
import org.digijava.kernel.util.I18NHelper;
import org.digijava.kernel.util.RequestUtils;
import org.digijava.kernel.util.UserUtils;
import org.digijava.module.um.form.UserEmailForm;
import org.digijava.module.um.util.DbUtil;
import org.digijava.module.um.util.UmUtil;

/**
 *
 * <p>Title: DiGiJava</p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company: </p>
 * @author not attributable
 * @version 1.0 UserEmailAction
 */
public class UserEmail
        extends Action {

    // log4J class initialize String
    private static Logger logger = I18NHelper.getKernelLogger(UserEmail.class);

    public ActionForward execute(ActionMapping mapping,
                                 ActionForm form,
                                 javax.servlet.http.HttpServletRequest request,
                                 javax.servlet.http.HttpServletResponse
                                         response) throws
            java.lang.Exception {

        UserEmailForm userEmailForm = (UserEmailForm) form;
        if (StringUtils.isBlank(userEmailForm.getEmail())) {
            return new ActionForward("/", true);
        }
        ActionMessages errors = new ActionMessages();

        // Get SMTP config
        Smtp smtp = DigiConfigManager.getConfig().getSmtp();

        // get random SHA1 code to generate URL, and put in database
        String code = UmUtil.getRandomSHA1();

        String email = userEmailForm.getEmail().toLowerCase();

        // First check if user alredy exits in database
        if (DbUtil.isRegisteredEmail(email)) {
            User u = UserUtils.getUserByEmailAddress(email);
            // Create and fill Reset object and update into database
            DbUtil.saveResetPassword(u.getId(), code);
            // -----------------------------------------

            // send mail alert

            try {
                sendEmail(email, u.getEmailUsedForNotification(), code, request);
            }
            catch (Exception ex) {
                errors.add(ActionMessages.GLOBAL_MESSAGE,
                        new ActionMessage("error.registration.sendmail"));
                saveErrors(request, errors);
                return (new ActionForward(mapping.getInput()));
            }
            // -----------------------------------------

            logger.debug("Reset password mail send to " + email);

        }
        else {
            logger.debug("Email " + email + " not exists");

            // email not exists
            errors.add(ActionMessages.GLOBAL_MESSAGE,
                    new ActionMessage("error.registration.noemail"));
            saveErrors(request, errors);
            return (new ActionForward(mapping.getInput()));
        }

        return mapping.findForward("success");
    }

    /**
     * Sends reset password confirmation email
     * @param email the email to send to
     * @param code the security reset code
     * @param request
     * @throws java.lang.Exception
     */
    public void sendEmail(String email, String notificationEmail, String code,
                          javax.servlet.http.HttpServletRequest request) throws
            java.lang.Exception {

        Message message;
        String siteName;
        String emailFrom;
        TranslatorWorker worker;
        Site site = RequestUtils.getSite(request);
        String domanName = DgUtil.getCurrRootUrl(request);

        Locale currentLocale = RequestUtils.getNavigationLanguage(request);

        // get SiteName
        worker = TranslatorWorker.getInstance(site.getName());
        message = worker.getByBody(site.getName(),currentLocale.getCode(), site.getId());
        if (message == null) {
            siteName = site.getName();
        } else {
            siteName = message.getMessage();
        }

        HashMap<String, String> hMap = new HashMap<String, String>();
        hMap.put("siteName", siteName);
        hMap.put("email", email);
        hMap.put("link", domanName + "/um/user/showResetForm.do?email=" + email + "&code=" +code);

        // get newpassword subject
        worker = TranslatorWorker.getInstance("alerts:newpassword:subject");
        String subject = worker.getFromGroup("alerts:newpassword:subject",
                currentLocale.getCode(), site,
                "New password - " + " {" +
                        siteName + "}").getMessage();

        subject = DgUtil.fillPattern(subject, hMap);
        // -------------------------

        // get newpassword body
        worker = TranslatorWorker.getInstance("alerts:resetpassword:body");
        String body = worker.getFromGroup("alerts:resetpassword:body",
                currentLocale.getCode(), site,
                "PASSWORD\n\nSomeone using the e-mail {email} has asked the {siteName} to reset the password for this account.\n" +
                        "If the request came from you, click on the link below and create a new password.\n" +
                        "If you did not send the request, please disregard this e-mail.\n\n{link}").getMessage();

        body = DgUtil.fillPattern(body, hMap);
        // -------------------------

        Smtp smtp = DigiConfigManager.getConfig().getSmtp();
        emailFrom = worker.getFromGroup("param:email:support",
                currentLocale.getCode(),
                site,
                "\"" + siteName + "\" <" + smtp.getFrom() +
                        ">").getMessage();


        InternetAddress address = new InternetAddress(notificationEmail);
        DgEmailManager.sendMail(new Address[] {address}
                               ,emailFrom,
                               subject, body, currentLocale, true);
    }

}
