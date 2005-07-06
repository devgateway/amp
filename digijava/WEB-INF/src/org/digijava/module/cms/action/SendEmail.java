/*
 *   SendContact.java
 *   @Author Lasha Dolidze lasha@digijava.org
 *   Created:
 *   CVS-ID: $Id: SendEmail.java,v 1.1 2005-07-06 10:34:09 rahul Exp $
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

package org.digijava.module.cms.action;

import javax.mail.Address;
import javax.mail.internet.InternetAddress;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.Action;
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
import org.digijava.kernel.util.RequestUtils;
import org.digijava.module.cms.form.SendEmailForm;

/**
 * <p>Title: DiGiJava</p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company: </p>
 * @author not attributable
 * @version 1.0
 */

public class SendEmail
    extends Action {

    public ActionForward execute(ActionMapping mapping, ActionForm form,
                                 HttpServletRequest request,
                                 HttpServletResponse response) throws
        Exception {

        SendEmailForm sendForm = (SendEmailForm) form;

        sendEmail(sendForm.getTo(), sendForm.getSubject(), sendForm.getMessage(),
                  request);

        if (sendForm.isCopyToMySelf()) {

            User user = RequestUtils.getUser(request);
            if (user != null) {

                sendEmail(user.getEmail(), sendForm.getSubject(),
                          sendForm.getMessage(), request);
            }
            else {
                return mapping.findForward("error");
            }
        }

        return mapping.findForward("success");
    }

    /**
     * Send email alert
     *
     * @param user
     * @throws java.lang.Exception
     */
    public void sendEmail(String to, String subject, String body,
                          HttpServletRequest request) throws
        java.lang.Exception {

        Message message;
        String siteName;
        String emailFrom;
        TranslatorWorker worker;
        Site site = RequestUtils.getSite(request);
        String domanName = DgUtil.getCurrRootUrl(request);

        Locale currentLocale = RequestUtils.getNavigationLanguage(request);

        // get SiteName
        worker = TranslatorWorker.getInstance("param:SiteName");
        message = worker.get("param:SiteName",
                             currentLocale.getCode(), site.getId().toString());
        if (message == null) {
            siteName = site.getName();
        }
        else {
            siteName = message.getMessage();
        }

        Smtp smtp = DigiConfigManager.getConfig().getSmtp();
        worker = TranslatorWorker.getInstance("param:email:support");
        emailFrom = worker.getFromGroup("param:email:support",
                                        currentLocale.getCode(),
                                        site,
                                        "\"" + siteName + "\" <" + smtp.getFrom() +
                                        ">").getMessage();

        InternetAddress address = new InternetAddress(to);
        DgEmailManager.sendMail(new Address[] {address}
                                , emailFrom,
                                subject, body, currentLocale, true);
    }

}