/**
 * This file is part of DiGi project (www.digijava.org).
 * DiGi is a multi-site portal system written in Java/J2EE.
 *
 * Copyright (C) 2002-2007 Development Gateway Foundation, Inc.
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301,
 * USA.
 */

package org.digijava.module.admin.action;

import java.util.HashSet;
import java.util.Iterator;

import javax.security.auth.Subject;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.digijava.kernel.entity.ModuleInstance;
import org.digijava.kernel.request.Site;
import org.digijava.kernel.request.SiteDomain;
import org.digijava.kernel.security.DgSecurityManager;
import org.digijava.kernel.security.ResourcePermission;
import org.digijava.kernel.user.User;
import org.digijava.kernel.util.RequestUtils;
import org.digijava.kernel.util.SiteCache;
import org.digijava.kernel.util.SiteConfigUtils;
import org.digijava.kernel.util.SiteManager;
import org.digijava.kernel.util.SiteUtils;
import org.digijava.kernel.viewmanager.ViewConfig;
import org.digijava.kernel.viewmanager.ViewConfigFactory;
import org.digijava.module.admin.form.SiteForm;
import org.digijava.module.admin.util.DbUtil;

public class CreateSite extends Action {

    public ActionForward execute(ActionMapping mapping,
                                 ActionForm form,
                                 javax.servlet.http.HttpServletRequest request,
                                 javax.servlet.http.HttpServletResponse
                                 response) throws java.lang.Exception {

        SiteForm siteForm = (SiteForm) form;
        String siteKey = siteForm.getSiteKey().toLowerCase();

        Site someSite = SiteCache.lookupByName(siteKey);
        if (someSite != null) {
            ActionMessages errors = new ActionMessages();
            errors.add(null,
                       new ActionMessage("error.admin.siteKeyExists"));
            saveErrors(request, errors);
            return mapping.findForward("error");
        }

        Site site = new Site(siteForm.getSiteName(), siteKey);
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
            ActionMessages errors = new ActionMessages();
            errors.add(null,
                       new ActionMessage("error.admin.noPermissions"));
            saveErrors(request, errors);
            return mapping.findForward("error");
        }

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
        ActionMessages errors = new ActionMessages();
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

                errors.add(null, new ActionMessage("error.admin.siteDomainExists", params));
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
            switch (item.getEnableSecurity()) {
                case 0:
                    siteDomain.setEnableSecurity(Boolean.FALSE);
                    break;
                case 1:
                    siteDomain.setEnableSecurity(Boolean.TRUE);
                    break;
                default:
                    siteDomain.setEnableSecurity(null);
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
