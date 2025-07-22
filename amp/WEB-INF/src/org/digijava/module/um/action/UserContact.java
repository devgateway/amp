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
import org.apache.struts.action.*;
import org.digijava.kernel.mail.DgEmailManager;
import org.digijava.kernel.util.RequestUtils;
import org.digijava.module.um.exception.UMException;
import org.digijava.module.um.form.UserContactForm;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * <p>Title: DiGiJava</p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company: </p>
 * @author not attributable
 * @version 1.0
 */

public class UserContact
    extends Action {

    private static Logger logger = Logger.getLogger(UserContact.class);

    public ActionForward execute(ActionMapping mapping, ActionForm form,
                                 HttpServletRequest request,
                                 HttpServletResponse response) throws
        java.lang.Exception, IOException, UMException {

        UserContactForm userForm = (UserContactForm) form;

        String senderEmail = userForm.getSenderEmail();
        String recipientEmail = userForm.getRecipientEmail();
        String subject = userForm.getSubject();
        String message = userForm.getMessage();

        logger.debug("%%%% RECEPIENT MAIL: " + recipientEmail );
        logger.debug("%%%% subject MAIL: " + subject );
        logger.debug("%%%% message MAIL: " + message );
        logger.debug("%%%% senderEmail MAIL: " + senderEmail );

        ActionMessages errors = new ActionMessages();
        if (senderEmail == null) {
            errors.add(null, new ActionMessage("error.um.userNotLoggedin"));
        }
        if (recipientEmail == null) {
            errors.add(null, new ActionMessage("error.um.userListEmprty"));
        }

        DgEmailManager.sendMail(recipientEmail, subject, message,
                                RequestUtils.getNavigationLanguage(request));

        if (userForm.isCopytomyself()) {
            DgEmailManager.sendMail(senderEmail, subject, message,
                                    RequestUtils.getNavigationLanguage(request));
        }

        return mapping.findForward("success");
    }

}
