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

package org.digijava.module.admin.helper.pickup;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import javax.security.auth.Subject;
import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.digijava.kernel.request.Site;
import org.digijava.kernel.security.DgSecurityManager;
import org.digijava.kernel.security.ResourcePermission;
import org.digijava.module.admin.exception.AdminException;
import org.digijava.module.admin.util.DbUtil;


public class SitePickupSource  {

    private static Logger logger = Logger.getLogger(SitePickupSource.class);

    public static ArrayList getItems(HttpServletRequest request) {
        List sites = null;
        try {
            sites = DbUtil.getSites();
        }
        catch (AdminException ex) {
            logger.error("Could not get Sites list from database ",ex);
            return new ArrayList();
        }

        Subject subject = DgSecurityManager.getSubject(request);
        HashMap hierarchyHelper = new HashMap();
        Iterator iter = sites.iterator();
        while (iter.hasNext()) {
            Site site = (Site) iter.next();
            //if (DgSecurityManager.permitted(subject, site, ResourcePermission.INT_ADMIN)) {
                hierarchyHelper.put(site.getId(), site);
            //}
        }

        HashMap processedSites = new HashMap();
        ArrayList result = new ArrayList();
        iter = hierarchyHelper.values().iterator();
        while (iter.hasNext()) {
            Site site = (Site) iter.next();
            putSite(subject, site, hierarchyHelper, processedSites, result);
        }

        return result;

    }

    private static PickupItem putSite(Subject subject, Site site, Map sites, Map processed, List destination) {
        PickupItem item = (PickupItem)processed.get(site.getId());
        if (item == null) {
            item = new PickupItem();

            item.setName(site.getName());
            item.setType("site");
            item.setKey("s" + site.getId());
            item.setChildren(new ArrayList());
            if (DgSecurityManager.permitted(subject, site, ResourcePermission.INT_ADMIN)) {
                item.setId(site.getId());
            }
            item.setExpandable(false);
            item.setExpand(false);

            Site parentSite = null;
            if (site.getParentId() != null) {
                parentSite = (Site)sites.get(site.getParentId());
            }
            if (parentSite != null) {
                PickupItem adminItem = putSite(subject, parentSite, sites, processed, destination);
                adminItem.setExpandable(true);
                adminItem.getChildren().add(item);
            } else {
                destination.add(item);
            }

            processed.put(site.getId(), item);
        }
        return item;
    }
}
