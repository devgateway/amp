/*
 *   ReactivateEmail.java
 * 	 @Author Maka Kharalashvili maka@digijava.org
 * 	 Created: Sep 1, 2003
 * 	 CVS-ID: $Id: ReactivateEmail.java,v 1.1 2005-07-06 10:34:07 rahul Exp $
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


import org.apache.struts.action.Action;
import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.digijava.kernel.Constants;
import org.digijava.kernel.user.User;
import org.digijava.module.um.form.UserUnSubscribeForm;
import org.digijava.module.um.util.DbUtil;

/**
 * <p>Title: DiGiJava</p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company: </p>
 * @author not attributable
 * @version 1.0
 */

public class ReactivateEmail
    extends Action {

        public ActionForward execute(ActionMapping mapping,
                                     ActionForm form,
                                     javax.servlet.http.HttpServletRequest
                                     request,
                                     javax.servlet.http.HttpServletResponse
                                     response) throws
            java.lang.Exception {

            UserUnSubscribeForm unsubscribeForm = (UserUnSubscribeForm) form;

            User user = (User) request.getSession(true).getAttribute(Constants.USER);

            if (user != null) {
                user.setActive(false);
                DbUtil.updateUser(user);
            }
            else {
                ActionErrors errors = new ActionErrors();
                errors.add(ActionErrors.GLOBAL_ERROR,
                           new ActionError("error.logon.invalid"));

            }

            return mapping.findForward("forward");

        }
    }
