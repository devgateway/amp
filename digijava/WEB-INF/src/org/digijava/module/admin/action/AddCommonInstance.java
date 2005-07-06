/*
 *   AddCommonInstance.java
 *   @Author Maka Kharalashvili maka@powerdot.org
 * 	 Created: Jul 7, 2004
 * 	 CVS-ID: $Id: AddCommonInstance.java,v 1.1 2005-07-06 10:34:10 rahul Exp $
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
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.digijava.kernel.Constants;
import org.digijava.kernel.request.Site;
import org.digijava.kernel.request.SiteDomain;
import org.digijava.module.admin.form.CommonInstancesForm;
import org.digijava.kernel.util.DgUtil;

public class AddCommonInstance
      extends Action {

    public ActionForward execute(ActionMapping mapping,
				 ActionForm form,
				 javax.servlet.http.HttpServletRequest request,
				 javax.servlet.http.HttpServletResponse
				 response) throws java.lang.Exception {

	if (!DgUtil.isModuleInstanceAdministrator(request)) {
	    return new ActionForward("/admin/index", true);
	}

	CommonInstancesForm formBean = (CommonInstancesForm) form;

	CommonInstancesForm.CommonInstanceInfo info = new CommonInstancesForm.
	      CommonInstanceInfo();
	formBean.getCommonInstances().add(info);
	return mapping.findForward("forward");
    }

}