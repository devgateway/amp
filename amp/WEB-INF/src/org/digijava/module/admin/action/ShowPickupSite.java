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
import org.digijava.module.admin.form.PickupForm;
import org.digijava.module.admin.helper.pickup.PickupItem;
import org.digijava.module.admin.helper.pickup.SitePickupSource;

import java.util.*;

public class ShowPickupSite
    extends Action {

    public ActionForward execute(ActionMapping mapping,
                                 ActionForm form,
                                 javax.servlet.http.HttpServletRequest request,
                                 javax.servlet.http.HttpServletResponse
                                 response) throws java.lang.Exception {
        PickupForm pickupForm = (PickupForm) form;
        LinkedHashSet expanded = new LinkedHashSet();

        String expandKey = request.getParameter("expand");

        Iterator iter = pickupForm.getItems().iterator();
        while (iter.hasNext()) {
            PickupItem currentInfo = (PickupItem)iter.next();
            if (currentInfo.getKey().equals(expandKey)) {
                currentInfo.setExpand(!currentInfo.isExpand());
            }
            if (currentInfo.isExpand()) {
                expanded.add(currentInfo.getKey());
            }
        }

        ArrayList items = SitePickupSource.getItems(request);
        pickupForm.setItems(new ArrayList());
        generateFlatStructure(0, items, pickupForm.getItems(), expanded);

        return mapping.findForward("forward");
    }

    private void generateFlatStructure(int level, Collection source,
                                           Collection destination, HashSet expanded) {
            if (source == null) {
                return;
            }
            Iterator iter = source.iterator();
            while (iter.hasNext()) {
                PickupItem info = (PickupItem) iter.
                    next();
                info.setLevel(level);
                destination.add(info);
                if (expanded.contains(info.getKey())) {
                    info.setExpand(true);
                    generateFlatStructure(level + 1, info.getChildren(),
                                          destination, expanded);
                }
                else {
                    info.setExpand(false);
                }
            }
        }

/*
    public ActionForward execute(ActionMapping mapping,
                                 ActionForm form,
                                 javax.servlet.http.HttpServletRequest request,
                                 javax.servlet.http.HttpServletResponse
                                 response) throws java.lang.Exception {
        Site currentSite = DgUtil.getCurrentSite(request);
        Site realSite = DbUtil.getSite(currentSite.getId());
        Subject subject = DgSecurityManager.getSubject(request);

        PickupSiteForm pickupForm = (PickupSiteForm) form;

        HashSet expanded = new HashSet();
        Long id = null;
        try {
            id = new Long(request.getParameter("expand"));
        }
        catch (NumberFormatException ex) {
        }

        Iterator iter = pickupForm.getSiteInfos().iterator();
        while (iter.hasNext()) {
            PickupSiteForm.PickupInfo currentInfo = (PickupSiteForm.PickupInfo)
                iter.next();
            if (currentInfo.getId().equals(id)) {
                currentInfo.setExpand(!currentInfo.isExpand());
                expanded.remove(id);
            }
            if (currentInfo.isExpand()) {
                expanded.add(currentInfo.getId());
            }
        }
        // Create hierarchy
        ArrayList rootSiteInfos = new ArrayList();
        HashMap processed = new HashMap();
        List sites = DbUtil.getSites();
        iter = sites.iterator();
        while (iter.hasNext()) {
            Site site = (Site) iter.next();
            if (DgSecurityManager.permitted(subject, site, ResourcePermission.INT_ADMIN)) {
                PickupSiteForm.PickupInfo currentInfo = (PickupSiteForm.
                    PickupInfo)
                    processed.get(site.getId());
                if (currentInfo == null) {
                    currentInfo = new PickupSiteForm.PickupInfo();
                    currentInfo.setChildren(new ArrayList());
                    currentInfo.setId(site.getId());
                    processed.put(site.getId(), currentInfo);
                }
                currentInfo.setName(site.getName());
                if (site.getParentId() == null) {
                    rootSiteInfos.add(currentInfo);
                }
                else {
                    // Get parent
                    PickupSiteForm.PickupInfo parentInfo = (PickupSiteForm.
                        PickupInfo)
                        processed.get(site.getParentId());
                    // If parent does not exist yet, create it. This information
                    // will be filled later in this loop
                    if (parentInfo == null) {
                        parentInfo = new PickupSiteForm.PickupInfo();
                        parentInfo.setChildren(new ArrayList());
                        parentInfo.setId(site.getParentId());
                        processed.put(site.getParentId(), parentInfo);
                    }

                    parentInfo.getChildren().add(currentInfo);
                }
            }

        }
        pickupForm.setSiteInfos(new ArrayList());
        generateFlatStructure(0, rootSiteInfos, pickupForm.getSiteInfos(),
                              expanded);

        return mapping.findForward("forward");
    }

    private void generateFlatStructure(int level, Collection source,
                                       Collection destination, HashSet expanded) {
        Iterator iter = source.iterator();
        while (iter.hasNext()) {
            PickupSiteForm.PickupInfo info = (PickupSiteForm.PickupInfo) iter.
                next();
            info.setLevel(level);
            destination.add(info);
            if (expanded.contains(info.getId())) {
                info.setExpand(true);
                generateFlatStructure(level + 1, info.getChildren(),
                                      destination, expanded);
            }
            else {
                info.setExpand(false);
            }
        }
    }
*/
}
