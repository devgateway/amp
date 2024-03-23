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

import org.apache.struts.action.*;
import org.digijava.kernel.request.Site;
import org.digijava.kernel.user.Group;
import org.digijava.kernel.util.RequestUtils;
import org.digijava.kernel.util.SiteCache;
import org.digijava.module.admin.util.DbUtil;

import java.util.HashMap;
import java.util.Iterator;

public class AddChildSite
    extends Action {

    public ActionForward execute(ActionMapping mapping,
                                 ActionForm form,
                                 javax.servlet.http.HttpServletRequest request,
                                 javax.servlet.http.HttpServletResponse
                                 response) throws java.lang.Exception {

        Site currentSite = RequestUtils.getSite(request);
        Long id = new Long(request.getParameter("id"));
        Site childSite = DbUtil.getSite(id);

        SiteCache.getInstance().getSite(currentSite);
        Site iterSite = currentSite;
        boolean permitted = true;
        do {
            if (iterSite.getId().equals(id)) {
                permitted = false;
                break;
            }
            iterSite = SiteCache.getInstance().getParentSite(iterSite);
        }
        while (iterSite != null);

        if (!permitted) {
            ActionMessages errors = new ActionMessages();
            Object[] params = {
                childSite.getName(), currentSite.getName()};
            errors.add(null,
                       new ActionMessage("error.admin.unacceptibleChildSite",
                                       params));
            saveErrors(request, errors);

        }
        else {
            HashMap parentDefGroups = new HashMap();
            Iterator iter = currentSite.getGroups().iterator();
            while (iter.hasNext()) {
                Group group = (Group) iter.next();
                if (group.isDefaultGroup()) {
                    parentDefGroups.put(group.getKey(), group);
                }
            }

            childSite.setParentId(currentSite.getId());
            iter = childSite.getGroups().iterator();
            while (iter.hasNext()) {
                Group group = (Group) iter.next();
                if (group.isDefaultGroup()) {
                    Group parentGroup = (Group) parentDefGroups.get(group.
                        getKey());
                    if (parentGroup != null) {
                        group.setParentId(parentGroup.getId());
                    }
                }
            }
            DbUtil.editSite(childSite);

            // Reload site cache
            SiteCache.getInstance().load();
        }

        return mapping.findForward("forward");
    }

}
