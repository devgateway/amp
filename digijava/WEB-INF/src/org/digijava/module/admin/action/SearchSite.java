/*
 *   SearchSite.java
 *   @Author Mikheil Kapanadze mikheil@digijava.org
 * 	 Created: Sep 5, 2003
 * 	 CVS-ID: $Id: SearchSite.java,v 1.1 2005-07-06 10:34:10 rahul Exp $
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
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.digijava.kernel.request.Site;
import org.digijava.kernel.request.SiteDomain;
import org.digijava.module.admin.exception.AdminException;
import org.digijava.module.admin.form.SearchSiteForm;
import org.digijava.module.admin.util.DbUtil;
import org.digijava.kernel.Constants;
import org.digijava.kernel.user.User;
import org.digijava.kernel.security.DgSecurityManager;
import org.digijava.kernel.security.ResourcePermission;
import org.digijava.kernel.util.DgUtil;
import java.util.Set;
import java.util.Iterator;

public class SearchSite
    extends Action {

    public ActionForward execute(ActionMapping mapping,
                                 ActionForm form,
                                 javax.servlet.http.HttpServletRequest request,
                                 javax.servlet.http.HttpServletResponse
                                 response) throws java.lang.Exception {

        SearchSiteForm siteForm = (SearchSiteForm) form;
        User user = (User) request.getSession(true).getAttribute(Constants.USER);
        List siteList = null;
        ArrayList sites = new ArrayList();


        siteList = DbUtil.searchSite(siteForm.getSiteKey().trim());

        ListIterator iterator = siteList.listIterator();
        while (iterator.hasNext()) {
            Site currentSite = (Site) iterator.next();

            String adminLink;
            boolean delete;

            boolean permitted = DgSecurityManager.permitted(user.getSubject(),currentSite,
                                                            ResourcePermission.INT_ADMIN);

            if (permitted) {
                adminLink = DgUtil.getSiteUrl(currentSite,request)+"/admin";
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