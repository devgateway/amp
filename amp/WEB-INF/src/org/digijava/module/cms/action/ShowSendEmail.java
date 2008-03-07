/*
*   ShowSendContact.java
*   @Author Lasha Dolidze lasha@digijava.org
*   Created:
*   CVS-ID: $Id$
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

import java.io.IOException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.digijava.kernel.Constants;
import org.digijava.kernel.user.User;
import org.digijava.module.um.exception.UMException;
import org.digijava.module.um.util.DbUtil;
import org.digijava.kernel.util.DgUtil;
import org.digijava.module.um.form.UserContactForm;
import org.digijava.kernel.util.RequestUtils;
import org.digijava.module.cms.form.SendEmailForm;
import org.digijava.module.cms.util.CmsUtil;
import org.digijava.kernel.persistence.WorkerException;

/**
 * <p>Title: DiGiJava</p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company: </p>
 * @author not attributable
 * @version 1.0
 */

public class ShowSendEmail extends Action {

     public ActionForward execute(ActionMapping mapping, ActionForm form,
                                  HttpServletRequest request,
                                  HttpServletResponse response) throws
      IOException, UMException, WorkerException {

      SendEmailForm sendForm = (SendEmailForm) form;
      User   user = RequestUtils.getUser(request);

      if (user != null) {

          String url = RequestUtils.getFullModuleUrl(request);
          String itemId = sendForm.getItemId();

          sendForm.setFromEmail(user.getEmail());
          sendForm.setFromName(user.getName());
          sendForm.setSubject(CmsUtil.getEmailSubject(request));
          sendForm.setMessage(CmsUtil.getEmailMessage(request, user.getName(), url + "showContentItemDetails.do?itemId=" + itemId));
      }
      else {
          ActionErrors errors = new ActionErrors();
          errors.add(null, new ActionError("error.cms.userNotLoggedin"));
      }

      return mapping.findForward("forward");
    }
 }

