/*
 *   DigiExceptionReport.java
 *   @Author Lasha Dolidze lasha@digijava.org
 * 	 Created: Jul 4, 2003
     * 	 CVS-ID: $Id: ExceptionReport.java,v 1.1 2005-07-06 10:34:31 rahul Exp $
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

package org.digijava.module.exception.action;

import org.apache.log4j.Logger;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.digijava.kernel.request.Site;
import org.digijava.kernel.util.RequestUtils;
import org.digijava.kernel.viewmanager.ViewConfig;
import org.digijava.kernel.viewmanager.ViewConfigFactory;
import org.apache.struts.action.ExceptionHandler;
import org.apache.struts.config.ExceptionConfig;
import javax.servlet.ServletException;
import org.digijava.kernel.viewmanager.*;
import javax.mail.internet.InternetAddress;
import org.digijava.kernel.mail.DgEmailManager;
import javax.mail.Address;
import org.digijava.kernel.translator.TranslatorWorker;
import org.digijava.kernel.entity.Locale;
import org.digijava.kernel.entity.Message;
import org.digijava.kernel.util.DigiConfigManager;
import org.digijava.kernel.config.ExceptionEmails;
import java.util.*;
import org.digijava.module.exception.form.DigiExceptionReportForm;
import org.digijava.module.exception.util.ModuleErrorStack;

/**
 *
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2004</p>
 * <p>Company: </p>
 * @author not attributable
 * @version 1.0
 *
 *
 */
public final class ExceptionReport
    extends Action {

    private static Logger logger = Logger.getLogger(ExceptionReport.class);

    public ActionForward execute(
        ActionMapping mapping,
        ActionForm form,
        javax.servlet.http.HttpServletRequest request,
        javax.servlet.http.HttpServletResponse
        response) throws
        java.lang.Exception {

        DigiExceptionReportForm reportForm = (DigiExceptionReportForm) form;

        String subject = null;
        String body = null;

        ModuleErrorStack template = reportForm.getExceptionTemplate();
        subject = template.getEmailSubject(request);
        body = template.getEmailMessage(request);

        sendEmail(subject, body, reportForm);

        HashMap moduleErrorStacks = reportForm.getErrorStck();
        if( moduleErrorStacks != null ) {
            moduleErrorStacks.remove(template.getModuleInstance());
        }
        reportForm.setExceptionTemplate(null);

        return new ActionForward("/showLayout.do", true);
    }

    /**
     *
     * @param form
     * @throws java.lang.Exception
     */
    public void sendEmail(String subject, String body, DigiExceptionReportForm form) throws
        java.lang.Exception {

        Message message;

        TranslatorWorker worker = TranslatorWorker.getInstance("ep:");
        Locale currentLocale = new Locale(); //RequestUtils.getNavigationLanguage(request);
        currentLocale.setCode("en");

        String emailFrom = "\"" + form.getName() + "\" <" + form.getEmail() + ">";

        ExceptionEmails exceptionEmails = DigiConfigManager.getConfig().getExceptionEmails();
        Iterator iter = exceptionEmails.getEmails().iterator();
        while (iter.hasNext()) {
            String email = (String) iter.next();

            logger.debug("Exception email send to: " + email);
            InternetAddress address = new InternetAddress(email);
            DgEmailManager.sendMail(new Address[] {address},
                                    emailFrom,
                                    subject, body, currentLocale, true);
        }

    }

}