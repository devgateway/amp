/*
 *   RemoveChildSite.java
 *   @Author Mikheil Kapanadze mikheil@digijava.org
 * 	 Created: Sep 16, 2003
 * 	 CVS-ID: $Id: RemoveChildSite.java,v 1.1 2005-07-06 10:34:09 rahul Exp $
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

import java.util.Iterator;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.digijava.kernel.request.Site;
import org.digijava.kernel.user.Group;
import org.digijava.kernel.util.DgUtil;
import org.digijava.kernel.util.SiteCache;
import org.digijava.module.admin.util.DbUtil;
import org.digijava.kernel.util.RequestUtils;

public class RemoveChildSite
    extends Action {

    public ActionForward execute(ActionMapping mapping,
                                 ActionForm form,
                                 javax.servlet.http.HttpServletRequest request,
                                 javax.servlet.http.HttpServletResponse
                                 response) throws java.lang.Exception {

        Site currentSite = RequestUtils.getSite(request);
        Long id = new Long(request.getParameter("id"));

        Site childSite = DbUtil.getSite(id);
        if (childSite.getParentId().equals(currentSite.getId())) {
            childSite.setParentId(null);
            Iterator iter = childSite.getGroups().iterator();
            while (iter.hasNext()) {
                Group group = (Group) iter.next();
                if (group.isDefaultGroup()) {
                    group.setParentId(null);
                }
            }

            DbUtil.editSite(childSite);
            SiteCache.getInstance().load();
        }

        return mapping.findForward("forward");
    }

}
