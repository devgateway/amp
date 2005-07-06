/*
 *   ShowMasterInstances.java
 *   @Author Mikheil Kapanadze mikheil@digijava.org
 * 	 Created: Sep 17, 2003
 * 	 CVS-ID: $Id: ShowMasterInstances.java,v 1.1 2005-07-06 10:34:10 rahul Exp $
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
import org.digijava.kernel.request.SiteDomain;
import org.digijava.module.admin.form.SiteForm;
import org.digijava.module.admin.form.SiteInstancesForm;
import org.digijava.kernel.entity.ModuleInstance;
import org.digijava.module.admin.util.DbUtil;
import org.digijava.kernel.request.Site;
import org.digijava.kernel.util.DgUtil;
import org.digijava.module.admin.form.ReferencedInstForm;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import org.digijava.kernel.util.RequestUtils;

public class ShowMasterInstances
    extends Action {

    public ActionForward execute(ActionMapping mapping,
                                 ActionForm form,
                                 javax.servlet.http.HttpServletRequest request,
                                 javax.servlet.http.HttpServletResponse
                                 response) throws java.lang.Exception {

        ReferencedInstForm formBean = (ReferencedInstForm) form;
        //int index = Integer.parseInt(request.getParameter("id"));
        Site site = RequestUtils.getSite(request);
        List sites = DbUtil.getSitesToReference(site.getId(),
                                                formBean.getModule());
        formBean.setSites(new ArrayList());
        formBean.getSites().addAll(sites);

        formBean.setInstances(new ArrayList());
        Iterator iter = formBean.getSites().iterator();
        Site found = null;
        Site first = null;
        while (iter.hasNext()) {
            Site oneSite = (Site) iter.next();
            if (first == null) {
                first = oneSite;
            }
            if (formBean.getSiteId() != null) {
                if (oneSite.getId().equals(formBean.getSiteId())) {
                    found = oneSite;
                    break;
                }
            }
            else {
                if (oneSite.getId().equals(formBean.getInstanceId())) {
                    found = oneSite;
                    break;
                }
            }
        }
        if (found == null) {
            found = first;
        }
        if (found != null) {
            Iterator iter2 = found.getModuleInstances().iterator();
            while (iter2.hasNext()) {
                ModuleInstance item = (ModuleInstance) iter2.next();
                if (item.getModuleName().equals(formBean.getModule())) {
                    formBean.getInstances().add(item);
                }
            }
        }

        return mapping.findForward("forward");
    }

}