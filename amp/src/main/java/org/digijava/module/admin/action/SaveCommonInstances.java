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
import java.util.Iterator;
import java.util.List;

import org.apache.log4j.Logger;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.digijava.kernel.entity.ModuleInstance;
import org.digijava.kernel.util.DgUtil;
import org.digijava.kernel.util.SiteCache;
import org.digijava.module.admin.form.CommonInstancesForm;
import org.digijava.module.admin.util.DbUtil;

public class SaveCommonInstances
      extends Action {

    private static Logger logger = Logger.getLogger(SaveCommonInstances.class);

    public ActionForward execute(ActionMapping mapping,
                 ActionForm form,
                 javax.servlet.http.HttpServletRequest request,
                 javax.servlet.http.HttpServletResponse
                 response) throws java.lang.Exception {

    if (!DgUtil.isModuleInstanceAdministrator(request)) {
        return new ActionForward("/admin/index", true);
    }

    CommonInstancesForm formBean = (CommonInstancesForm) form;
    List commonInstances = new ArrayList();

    Iterator iter = formBean.getCommonInstances().iterator();
    while (iter.hasNext()) {
        CommonInstancesForm.CommonInstanceInfo info = (CommonInstancesForm.
          CommonInstanceInfo) iter.next();

        ModuleInstance moduleInstance = new ModuleInstance();

        moduleInstance.setSite(null);
        moduleInstance.setPermitted(true);

        moduleInstance.setInstanceName(info.getInstance());
        moduleInstance.setModuleInstanceId(info.getId());
        moduleInstance.setModuleName(info.getModule());
        moduleInstance.setNumberOfItemsInTeaser(info.getSelectedNumOfItemsInTeaser());

        commonInstances.add(moduleInstance);
    }

    DbUtil.editCommonInstances(commonInstances);
    SiteCache.getInstance().load();

    return mapping.findForward("forward");
    }
}
