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

import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;
import javax.security.auth.Subject;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.digijava.kernel.Constants;
import org.digijava.kernel.request.Site;
import org.digijava.kernel.security.DgSecurityManager;
import org.digijava.kernel.security.ResourcePermission;
import org.digijava.kernel.translator.util.TrnUtil;
import org.digijava.kernel.user.User;
import org.digijava.kernel.util.DgUtil;
import org.digijava.kernel.util.SiteUtils;
import org.digijava.module.admin.form.AllSitesForm;
import org.digijava.module.admin.util.DbUtil;
import org.digijava.module.admin.util.SiteCallback;
import org.digijava.kernel.entity.Locale;
import org.digijava.kernel.util.RequestUtils;


/**
 * <p>Title: DiGiJava</p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company: </p>
 * @author not attributable
 * @version 1.0
 */

public class ShowAllSites
    extends Action {

    public ActionForward execute(ActionMapping mapping, ActionForm form,
                                 HttpServletRequest request,
                                 HttpServletResponse response) throws
        java.lang.Exception {

        AllSitesForm formBean = (AllSitesForm) form;
        User user = RequestUtils.getUser(request);
        List topSites = null;
        List sites = new ArrayList();

        if (formBean.getSelectedSiteId() == null) {
            topSites = DbUtil.getTopLevelSites();
            formBean.setChildren(false);
            formBean.setParentSiteName(null);
        }
        else {
            topSites = SiteUtils.getChildSites(formBean.getSelectedSiteId().longValue());
            formBean.setChildren(true);
            formBean.setParentSiteName(DbUtil.getSite(formBean.
                getSelectedSiteId()).getName());
        }

        if (topSites != null) {

            //populate Locale according to Navigation Language
            Locale locale = new Locale();
            locale.setCode(RequestUtils.getNavigationLanguage(request).
                           getCode());

            List sortedTopSites = TrnUtil.sortByTranslation(topSites,
                                                 locale,
                                                 new SiteCallback());

            ListIterator iterator = sortedTopSites.listIterator();
            while (iterator.hasNext()) {

                String editSite;
                String viewSite;
                String admin;

                boolean editableSite;
                boolean hasChildren;

                AllSitesForm.SiteInfo si = new AllSitesForm.SiteInfo();

                Site currentSite = (Site) iterator.next();
                //
                Subject subj = DgSecurityManager.getSubject(request);
                editableSite = DgSecurityManager.permitted(subj, currentSite,
                    ResourcePermission.INT_ADMIN);
                //
                if (SiteUtils.getChildSites(currentSite.getId().longValue()).
                    isEmpty()) {
                    hasChildren = false;
                }
                else {
                    hasChildren = true;
                }

                viewSite = DgUtil.getSiteUrl(currentSite, request);
                editSite = viewSite + "/admin/default/showEditSite.do";
                admin = viewSite + "/admin";

                si.setEditSite(editSite);
                si.setViewSite(viewSite);
                si.setAdmin(admin);
                si.setEditableSite(editableSite);
                si.setHasChildren(hasChildren);
                si.setSiteName(currentSite.getName());
                si.setSiteId(currentSite.getSiteId());
                si.setId(currentSite.getId());

                sites.add(si);
            }
            if (user.isGlobalAdmin()) {
                formBean.setAddSite(true);
            }
            formBean.setSites(sites);
        } else {
            formBean.setSites(null);
        }

        return mapping.findForward("forward");
    }

}
