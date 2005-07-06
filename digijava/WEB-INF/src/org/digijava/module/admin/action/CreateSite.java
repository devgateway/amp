/*
 *   CreateSite.java
 *   @Author Mikheil Kapanadze mikheil@digijava.org
 * 	 Created: Sep 1, 2003
 * 	 CVS-ID: $Id: CreateSite.java,v 1.1 2005-07-06 10:34:10 rahul Exp $
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

import java.io.File;
import java.io.FileOutputStream;
import java.io.PrintStream;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.digijava.kernel.entity.Locale;
import org.digijava.kernel.request.Site;
import org.digijava.kernel.request.SiteDomain;
import org.digijava.kernel.security.SitePermission;
import org.digijava.kernel.user.Group;
import org.digijava.kernel.user.GroupPermission;
import org.digijava.kernel.user.User;
import org.digijava.kernel.util.DgUtil;
import org.digijava.kernel.util.SiteCache;
import org.digijava.kernel.util.SiteConfigUtils;
import org.digijava.module.admin.exception.AdminException;
import org.digijava.module.admin.form.SiteForm;
import org.digijava.module.admin.util.DbUtil;
import javax.security.auth.Subject;
import org.digijava.kernel.security.DgSecurityManager;
import org.digijava.kernel.security.ResourcePermission;
import org.digijava.kernel.siteconfig.SiteConfig;
import org.digijava.kernel.util.SiteConfigManager;
import org.digijava.kernel.siteconfig.SiteLayout;
import org.digijava.kernel.siteconfig.Layout;
import org.digijava.kernel.siteconfig.PutItem;
import org.digijava.kernel.entity.ModuleInstance;
import java.util.List;
import java.util.Collections;
import org.apache.log4j.Logger;
import java.util.TreeSet;
import java.io.*;
import org.digijava.kernel.util.SiteManager;
import org.digijava.kernel.viewmanager.ViewConfig;
import org.digijava.kernel.viewmanager.ViewConfigFactory;
import java.util.*;
import org.digijava.kernel.util.RequestUtils;
import org.digijava.kernel.util.SiteUtils;

public class CreateSite
    extends Action {

    private static Logger logger = Logger.getLogger(CreateSite.class);

    public ActionForward execute(ActionMapping mapping,
                                 ActionForm form,
                                 javax.servlet.http.HttpServletRequest request,
                                 javax.servlet.http.HttpServletResponse
                                 response) throws java.lang.Exception {

        SiteForm siteForm = (SiteForm) form;
        String siteKey = siteForm.getSiteKey().toLowerCase();

        Site someSite = SiteUtils.getSite(siteKey);
        if (someSite != null) {
            ActionErrors errors = new ActionErrors();
            errors.add(null,
                       new ActionError("error.admin.siteKeyExists"));
            saveErrors(request, errors);
            return mapping.findForward("error");
        }

        Site site = new Site(siteForm.getSiteName(), siteKey);
        Site currentSite = RequestUtils.getSite(request);
        User user = RequestUtils.getUser(request);
        Subject subject = DgSecurityManager.getSubject(request);

        Site parentSite = null;
        boolean permitted;
        Long parentId = siteForm.getParentId();

        if (parentId != null) {
            parentSite = DbUtil.getSite(parentId);
            permitted = DgSecurityManager.permitted(subject, parentSite,
                ResourcePermission.INT_ADMIN);
        }
        else {
            permitted = user.isGlobalAdmin();
        }

        if (!permitted) {
            ActionErrors errors = new ActionErrors();
            errors.add(null,
                       new ActionError("error.admin.noPermissions"));
            saveErrors(request, errors);
            return mapping.findForward("error");
        }

        HashMap parentDefGroups = new HashMap();
        if (parentId != null) {
            site.setParentId(parentSite.getId());
        }
        else {
            if (!user.isGlobalAdmin()) {
                return mapping.findForward("adminIndex");
            }
        }

        site.setSecure(siteForm.isSecure());
        site.setInvisible(siteForm.isInvisible());
        site.setInheritSecurity(true);
        site.setFolder(siteForm.getFolderName());

        // Assign domains to site
        ActionErrors errors = new ActionErrors();
        site.setSiteDomains(new HashSet());
        Iterator iter = siteForm.getSiteDomains().iterator();
        while (iter.hasNext()) {
            SiteForm.SiteDomainInfo item = (SiteForm.SiteDomainInfo) iter.next();

            SiteDomain siteDomain = new SiteDomain();
            siteDomain.setSite(site);
            site.getSiteDomains().add(siteDomain);
            siteDomain.setSiteDbDomain(item.getDomain());
            if ( (item.getPath() != null) && (item.getPath().trim().length() == 0)) {
                siteDomain.setSitePath(null);
            }
            else {
                siteDomain.setSitePath(item.getPath());
            }

            // Verify existance
            Site tmpSite = DbUtil.getSite(siteDomain.getSiteDomain(), siteDomain.getSitePath());
            if (tmpSite != null) {
                Object[] params = {
                    item.getDomain(),
                    item.getPath(), tmpSite.getName()};

                errors.add(null, new ActionError("error.admin.siteDomainExists", params));
            }
            // Assign language
            siteDomain.setLanguage(null);

            // Toggle default mark
            if (siteForm.getDefDomain() == item.getIndex()) {
                siteDomain.setDefaultDomain(true);
            }
            else {
                siteDomain.setDefaultDomain(false);
            }
        }

        if (!errors.isEmpty()) {
            saveErrors(request, errors);
            return mapping.findForward("error");
        }

        SiteUtils.addDefaultGroups(site);

        // Get required module instances from template
        ViewConfig templateViewConfig = ViewConfigFactory.getInstance().getTemplateViewConfig(siteForm.getTemplate());
        HashSet moduleInstances = new HashSet(templateViewConfig.getReferencedInstances(false));
        Iterator instancesIter = moduleInstances.iterator();
        while (instancesIter.hasNext()) {
            ModuleInstance moduleInstance = (ModuleInstance)instancesIter.next();
            moduleInstance.setSite(site);
            moduleInstance.setPermitted(true);
            moduleInstance.setRealInstance(null);
        }


        site.setModuleInstances(moduleInstances);

        SiteManager.createSiteFolder(this.getServlet().getServletContext().
                         getRealPath(SiteConfigUtils.SITE_DIR +"/" +
                                     site.getFolder())
                         , siteForm.getTemplate());

        // Force reloading site configuration
        ViewConfig viewConfig = ViewConfigFactory.getInstance().getViewConfig(site);

        viewConfig.reload();

        // Create site
        DbUtil.createSite(site);
        // Reload site cache
        SiteCache.getInstance().load();
        // Assign permissions to group principals
        SiteUtils.fixDefaultGroupPermissions(site);

        if ( (siteForm.getTargetAction() == null) ||
            (siteForm.getTargetAction().trim().length() == 0)) {
            return mapping.findForward("forward");
        }
        else {
            ActionForward forward = new ActionForward(siteForm.getTargetAction());
            return forward;
        }
    }


}
