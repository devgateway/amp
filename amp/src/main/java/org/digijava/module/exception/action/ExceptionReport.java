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

package org.digijava.module.exception.action;

import org.apache.log4j.Logger;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.digijava.kernel.config.ExceptionEmails;
import org.digijava.kernel.entity.Locale;
import org.digijava.kernel.mail.DgEmailManager;
import org.digijava.kernel.request.Site;
import org.digijava.kernel.util.DigiConfigManager;
import org.digijava.kernel.util.RequestUtils;
import org.digijava.module.exception.form.DigiExceptionReportForm;
import org.digijava.module.exception.util.ModuleErrorStack;

import javax.mail.Address;
import javax.mail.internet.InternetAddress;
import java.util.Iterator;

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
        Site site = RequestUtils.getSite(request);

        ModuleErrorStack template = new ModuleErrorStack(reportForm.
            getExceptionInfo(), reportForm.getName(), reportForm.getEmail(),
            reportForm.getMessage(), reportForm.getIssueId(), site);

        subject = template.getEmailSubject();
        body = template.getEmailMessage(request);

        sendEmail(subject, body, reportForm);

        return new ActionForward("/showLayout.do", true);
    }

    /**
     *
     * @param form
     * @throws java.lang.Exception
     */
    public void sendEmail(String subject, String body,
                          DigiExceptionReportForm form) throws
        java.lang.Exception {

        Locale currentLocale = new Locale();
        currentLocale.setCode("en");

        String emailFrom = "\"" + form.getName() + "\" <" + form.getEmail() +
            ">";

        ExceptionEmails exceptionEmails = DigiConfigManager.getConfig().
            getExceptionEmails();
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
