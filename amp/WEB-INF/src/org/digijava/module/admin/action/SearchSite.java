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

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.digijava.kernel.request.Site;
import org.digijava.kernel.security.DgSecurityManager;
import org.digijava.kernel.security.ResourcePermission;
import org.digijava.kernel.util.DgUtil;
import org.digijava.kernel.util.RequestUtils;
import org.digijava.module.admin.form.SearchSiteForm;
import org.digijava.module.admin.util.DbUtil;

import java.util.ArrayList;
import java.util.List;

public class SearchSite
    extends Action {

    public ActionForward execute(ActionMapping mapping,
                                 ActionForm form,
                                 javax.servlet.http.HttpServletRequest request,
                                 javax.servlet.http.HttpServletResponse
                                 response) throws java.lang.Exception {

        SearchSiteForm siteForm = (SearchSiteForm) form;
        List siteList = null;
        ArrayList sites = new ArrayList();


        siteList = DbUtil.searchSite(siteForm.getSiteKey().trim());

        for (Object o : siteList) {
            Site currentSite = (Site) o;

            String adminLink;
            boolean delete;

            boolean permitted = DgSecurityManager.permitted(RequestUtils.
                            getSubject(request), currentSite,
                    ResourcePermission.INT_ADMIN);

            if (permitted) {
                adminLink = DgUtil.getSiteUrl(currentSite, request) + "/admin";
                delete = true;
            } else {
                adminLink = null;
                delete = false;
            }


            SearchSiteForm.SiteInfo si = new SearchSiteForm.SiteInfo();

            si.setId(currentSite.getId());

            si.setSiteName(currentSite.getName());
            si.setSiteId(currentSite.getSiteId());
            si.setSiteDomains(currentSite.getSiteDomains());

            si.setAdminLink(adminLink);
            si.setDelete(delete);
            sites.add(si);
        }

        siteForm.setSites(sites);

        return mapping.findForward("forward");

    }

}
