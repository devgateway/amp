/*
 *   ShowCreateSite.java
 *   @Author Mikheil Kapanadze mikheil@digijava.org
 * 	 Created: Sep 1, 2003
 * 	 CVS-ID: $Id: ShowCreateSite.java,v 1.1 2005-07-06 10:34:09 rahul Exp $
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
import org.digijava.kernel.user.User;
import org.digijava.module.admin.form.SiteForm;
import org.digijava.module.admin.util.DbUtil;
import java.io.File;
import org.digijava.kernel.util.SiteConfigUtils;
import java.util.ArrayList;
import org.digijava.kernel.request.Site;
import org.apache.log4j.Logger;

public class ShowCreateSite
    extends Action {

    private static Logger logger = Logger.getLogger(ShowCreateSite.class);

    public ActionForward execute(ActionMapping mapping,
                                 ActionForm form,
                                 javax.servlet.http.HttpServletRequest request,
                                 javax.servlet.http.HttpServletResponse
                                 response) throws java.lang.Exception {

        SiteForm siteForm = (SiteForm)form;
        //siteForm.setLanguages(DbUtil.getLanguages());
        SiteForm.SiteDomainInfo siteDomain = new SiteForm.SiteDomainInfo();
        siteForm.setSiteDomains(new ArrayList());
        siteForm.getSiteDomains().add(siteDomain);
        siteForm.setSiteName(null);
        siteForm.setSiteKey(null);
        siteForm.setFolderName(null);
        siteForm.setInvisible(false);


        File templateDir = new File(this.getServlet().getServletContext().
                                    getRealPath(
            SiteConfigUtils.TEMPLATE_DIR));

        File[] templateDirContent = templateDir.listFiles();
        siteForm.setTemplates(new ArrayList());
        for (int i = 0; i< templateDirContent.length; i++) {
            if (templateDirContent[i].isDirectory()) {
                File configFile = new File(templateDirContent[i].getAbsolutePath() + "/site-config.xml");
                if (configFile.exists()) {
                    siteForm.getTemplates().add(templateDirContent[i].getName());
                }
            }
        }

        Long parentId = null;

        parentId = siteForm.getParentId();
        if (parentId != null) {
            Site parentSite = DbUtil.getSite(parentId);
            siteForm.setParentSiteName(parentSite.getName());
        } else {
            siteForm.setParentSiteName(null);
        }

        return mapping.findForward("forward");
    }

}