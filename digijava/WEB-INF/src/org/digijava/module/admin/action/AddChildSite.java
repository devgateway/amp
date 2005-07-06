/*
 *   AddChildSite.java
 *   @Author Mikheil Kapanadze mikheil@digijava.org
 * 	 Created: Sep 16, 2003
 * 	 CVS-ID: $Id: AddChildSite.java,v 1.1 2005-07-06 10:34:10 rahul Exp $
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

import java.security.Policy;
import java.util.HashMap;
import java.util.Iterator;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.digijava.kernel.request.Site;
import org.digijava.kernel.security.DBPolicy;
import org.digijava.kernel.user.Group;
import org.digijava.kernel.util.DgUtil;
import org.digijava.kernel.util.SiteCache;
import org.digijava.module.admin.util.DbUtil;
import org.digijava.kernel.util.RequestUtils;

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
            ActionErrors errors = new ActionErrors();
            Object[] params = {
                childSite.getName(), currentSite.getName()};
            errors.add(null,
                       new ActionError("error.admin.unacceptibleChildSite",
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
            // Reload policy to assign correct privilegies to groups
            DBPolicy policy = (DBPolicy) Policy.getPolicy();
            policy.refresh();
        }

        return mapping.findForward("forward");
    }

}