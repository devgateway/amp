/*
 *   ClearMasterInstance.java
 *   @Author Mikheil Kapanadze mikheil@digijava.org
 * 	 Created: Sep 17, 2003
 * 	 CVS-ID: $Id: ClearMasterInstance.java,v 1.1 2005-07-06 10:34:10 rahul Exp $
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
import org.digijava.module.admin.form.SiteInstancesForm;

public class ClearMasterInstance extends Action {

    public ActionForward execute(ActionMapping mapping,
                                 ActionForm form,
                                 javax.servlet.http.HttpServletRequest
                                 request,
                                 javax.servlet.http.HttpServletResponse
                                 response) throws java.lang.Exception {
        SiteInstancesForm formBean = (SiteInstancesForm) form;

        int instanceIndex = Integer.parseInt(request.getParameter("index"));

        SiteInstancesForm.InstanceInfo info = (SiteInstancesForm.InstanceInfo)
            formBean.getInstances().get(instanceIndex);
        info.setMappingId(null);
        info.setMappingSite(null);
        info.setMappingInstance(null);

        return mapping.findForward("forward");
    }

}