/*
 *   AddInstance.java
 *   @Author Mikheil Kapanadze mikheil@digijava.org
 * 	 Created: Sep 5, 2003
 * 	 CVS-ID: $Id: AddInstance.java,v 1.1 2005-07-06 10:34:10 rahul Exp $
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

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.digijava.kernel.Constants;
import org.digijava.kernel.request.Site;
import org.digijava.kernel.request.SiteDomain;
import org.digijava.kernel.siteconfig.Layout;
import org.digijava.kernel.siteconfig.PutItem;
import org.digijava.kernel.siteconfig.SiteConfig;
import org.digijava.kernel.siteconfig.SiteLayout;
import org.digijava.kernel.util.SiteConfigManager;
import org.digijava.module.admin.form.SiteInstancesForm;
import org.digijava.kernel.entity.ModuleInstance;

public class AddInstance extends Action {

    public ActionForward execute(ActionMapping mapping,
                                 ActionForm form,
                                 javax.servlet.http.HttpServletRequest request,
                                 javax.servlet.http.HttpServletResponse
                                 response) throws java.lang.Exception {

        SiteDomain siteDomain = (SiteDomain) request.getAttribute(Constants.
            CURRENT_SITE);
        Site currentSite = siteDomain.getSite();
        SiteInstancesForm formBean = (SiteInstancesForm) form;

        SiteInstancesForm.InstanceInfo info = new SiteInstancesForm.InstanceInfo();
        info.setPermitted(true);
        formBean.getInstances().add(info);
/*        ModuleInstance moduleInstance = new ModuleInstance();

        formBean.getInstances().add(moduleInstance);
        Iterator iter = formBean.getModules().iterator();
        while (iter.hasNext()) {
            String item = (String)iter.next();
            moduleInstance.setModuleName(item);
            break;
        }
*/
        return mapping.findForward("forward");
    }

}