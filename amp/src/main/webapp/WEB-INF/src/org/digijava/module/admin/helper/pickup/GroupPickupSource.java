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
import org.digijava.kernel.user.Group;
import org.digijava.module.admin.exception.AdminException;
import org.digijava.module.admin.util.DbUtil;


public class GroupPickupSource  {

    private static Logger logger = Logger.getLogger(GroupPickupSource.class);

    public static ArrayList getItems(HttpServletRequest request) {
        List sites = null;
        try {
            sites = DbUtil.getSites();
        }
        catch (AdminException ex) {
            logger.error("Could not get Sites list from database",ex);
            return new ArrayList();
        }

        Subject subject = DgSecurityManager.getSubject(request);
        HashMap hierarchyHelper = new HashMap();
        Iterator iter = sites.iterator();
        while (iter.hasNext()) {
            Site site = (Site) iter.next();
            if (DgSecurityManager.permitted(subject, site, ResourcePermission.INT_ADMIN)) {
                hierarchyHelper.put(site.getId(), site);
            }
        }

        HashMap processedSites = new HashMap();
        ArrayList result = new ArrayList();
        iter = hierarchyHelper.values().iterator();
        while (iter.hasNext()) {
            Site site = (Site) iter.next();
            putSite(site, hierarchyHelper, processedSites, result);
        }

        return result;

/*
        // Use linked hash map to improve delete operations
        LinkedHashMap parents = new LinkedHashMap();
        HashMap hierarchyHelper = new HashMap();
        Iterator iter = sites.iterator();
        while (iter.hasNext()) {
            Site site = (Site)iter.next();
            PickupItem pickupItem = new PickupItem();
            pickupItem.setName(site.getName());
            pickupItem.setType("site");
            pickupItem.setKey("s" + site.getId());
            pickupItem.setChildren(new ArrayList());
            Iterator groupIter = site.getGroups().iterator();
            while (iter.hasNext()) {
                Group group = (Group)groupIter.next();
                PickupItem pickGroup = new PickupItem();
                pickGroup.setId(group.getId());
                pickGroup.setType("group");
                pickGroup.setExpandable(false);
                pickGroup.setExpand(false);
                pickGroup.setKey("g" + group.getId());
                pickGroup.setChildren(null);
            }
            hierarchyHelper.put(site.getId(), pickupItem);
            if (site.getParentId() != null) {
                parents.put(site.getId(), site.getParentId());
            }
        }

        iter = hierarchyHelper.entrySet().iterator();
        while (iter.hasNext()) {
            Map.Entry entry = (Map.Entry)iter.next();
            Long id = (Long)entry.getKey();
            PickupItem pickupItem = (PickupItem)entry.getValue();
            Long parentId = (Long)parents.get(id);
            if (parentId != null) {
                PickupItem parentItem = (PickupItem)hierarchyHelper.get(parentId);
                if (parentItem != null) {
                    parentItem.getChildren().add(pickupItem);
                }
            }
        }
*/
    }

    private static PickupItem putSite(Site site, Map sites, Map processed, List destination) {
        PickupItem item = (PickupItem)processed.get(site.getId());
        if (item == null) {
            item = new PickupItem();

            item.setName(site.getName());
            item.setType("site");
            item.setKey("s" + site.getId());
            item.setChildren(new ArrayList());
            Iterator groupIter = site.getGroups().iterator();
            while (groupIter.hasNext()) {
                Group group = (Group)groupIter.next();
                PickupItem pickGroup = new PickupItem();
                pickGroup.setId(group.getId());
                pickGroup.setType("group");
                pickGroup.setExpandable(false);
                pickGroup.setExpand(false);
                pickGroup.setKey("g" + group.getId());
                pickGroup.setName(group.getName());
                pickGroup.setChildren(null);
                item.getChildren().add(pickGroup);
            }
            Site parentSite = null;
            if (site.getParentId() != null) {
                parentSite = (Site)sites.get(site.getParentId());
            }
            if (parentSite != null) {
                PickupItem adminItem = putSite(parentSite, sites, processed, destination);
                adminItem.getChildren().add(item);
            } else {
                destination.add(item);
            }

            processed.put(site.getId(), item);
        }
        return item;
    }
}
