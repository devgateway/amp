/*
*   UserContact.java
*   @Author Lasha Dolidze lasha@digijava.org
*   Created:
*   CVS-ID: $Id: UserContact.java,v 1.1 2005-07-06 10:34:07 rahul Exp $
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

import java.io.IOException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.digijava.kernel.mail.DgEmailManager;
import org.digijava.module.um.exception.UMException;
import org.digijava.module.um.form.UserContactForm;
import org.digijava.kernel.util.RequestUtils;

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

        ActionErrors errors = new ActionErrors();
        if (senderEmail == null) {
            errors.add(null, new ActionError("error.um.userNotLoggedin"));
        }
        if (recipientEmail == null) {
            errors.add(null, new ActionError("error.um.userListEmprty"));
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