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
import org.digijava.kernel.entity.ModuleInstance;
import org.digijava.kernel.request.Site;
import org.digijava.kernel.util.RequestUtils;
import org.digijava.module.admin.form.ReferencedInstForm;
import org.digijava.module.admin.util.DbUtil;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class ShowMasterInstances
    extends Action {

    public ActionForward execute(ActionMapping mapping,
                                 ActionForm form,
                                 javax.servlet.http.HttpServletRequest request,
                                 javax.servlet.http.HttpServletResponse
                                 response) throws java.lang.Exception {

        ReferencedInstForm formBean = (ReferencedInstForm) form;
        //int index = Integer.parseInt(request.getParameter("id"));
        Site site = RequestUtils.getSite(request);
        List sites = DbUtil.getSitesToReference(site.getId(),
                                                formBean.getModule());
        formBean.setSites(new ArrayList());
        formBean.getSites().addAll(sites);

        formBean.setInstances(new ArrayList());
        Iterator iter = formBean.getSites().iterator();
        Site found = null;
        Site first = null;
        while (iter.hasNext()) {
            Site oneSite = (Site) iter.next();
            if (first == null) {
                first = oneSite;
            }
            if (formBean.getSiteId() != null) {
                if (oneSite.getId().equals(formBean.getSiteId())) {
                    found = oneSite;
                    break;
                }
            }
            else {
                if (oneSite.getId().equals(formBean.getInstanceId())) {
                    found = oneSite;
                    break;
                }
            }
        }
        if (found == null) {
            found = first;
        }
        if (found != null) {
            Iterator iter2 = found.getModuleInstances().iterator();
            while (iter2.hasNext()) {
                ModuleInstance item = (ModuleInstance) iter2.next();
                if (item.getModuleName().equals(formBean.getModule())) {
                    formBean.getInstances().add(item);
                }
            }
        }

        return mapping.findForward("forward");
    }

}
