/*
 *   DeleteDomain.java
 *   @Author Mikheil Kapanadze mikheil@digijava.org
 * 	 Created: Sep 16, 2003
 * 	 CVS-ID: $Id: DeleteDomain.java,v 1.1 2005-07-06 10:34:09 rahul Exp $
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
package org.digijava.module.admin.action;

import org.apache.struts.action.Action;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.digijava.kernel.request.SiteDomain;
import org.digijava.module.admin.form.SiteForm;
import java.util.ArrayList;

public class DeleteDomain extends Action {

    public ActionForward execute(ActionMapping mapping,
                                ActionForm form,
                                javax.servlet.http.HttpServletRequest request,
                                javax.servlet.http.HttpServletResponse
                                response) throws java.lang.Exception {
       int index=Integer.parseInt(request.getParameter("id"));
       SiteForm siteForm = (SiteForm)form;
       siteForm.getSiteDomains().remove(index);
       String referrer = (String)request.getParameter("referrer");

       if (referrer.equals("createSite")) {
           return mapping.findForward("forwardCreate");
       } else {
           return mapping.findForward("forwardEdit");
       }
   }

}