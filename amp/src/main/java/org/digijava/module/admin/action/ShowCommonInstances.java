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
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;

import org.apache.log4j.Logger;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.digijava.kernel.entity.ModuleInstance;
import org.digijava.kernel.request.Site;
import org.digijava.kernel.util.DigiConfigManager;
import org.digijava.kernel.util.RequestUtils;
import org.digijava.kernel.viewmanager.ViewConfig;
import org.digijava.kernel.viewmanager.ViewConfigFactory;
import org.digijava.module.admin.form.CommonInstancesForm;
import org.digijava.module.admin.util.DbUtil;

public class ShowCommonInstances
    extends Action {

    private static Logger logger = Logger.getLogger(ShowCommonInstances.class);

    public ActionForward execute(ActionMapping mapping,
                 ActionForm form,
                 javax.servlet.http.HttpServletRequest request,
                 javax.servlet.http.HttpServletResponse
                 response) throws java.lang.Exception {

    if (! RequestUtils.getUser(request).isGlobalAdmin()) {
        return new ActionForward("/admin/index",true);
    }

    CommonInstancesForm formBean = (CommonInstancesForm) form;

    Site currentSite = RequestUtils.getSite(request);
    List commonInstnaces = DbUtil.getCommonInstances();

    formBean.setCommonInstances(new ArrayList());
    Iterator iter = commonInstnaces.iterator();
    while (iter.hasNext()) {
        ModuleInstance instance = (ModuleInstance)iter.next();
        CommonInstancesForm.CommonInstanceInfo info = new CommonInstancesForm.CommonInstanceInfo();

        info.setId(instance.getModuleInstanceId());
        info.setModule(instance.getModuleName());
        info.setInstance(instance.getInstanceName());
        info.setSelectedNumOfItemsInTeaser(instance.getNumberOfItemsInTeaser());

        formBean.getCommonInstances().add(info);
    }

    ViewConfig viewConfig = ViewConfigFactory.getInstance().getViewConfig(currentSite);

    HashSet modules = new HashSet();
    modules.addAll(DigiConfigManager .getModulesConfig().keySet());
    modules.addAll(viewConfig.getReferencedModules(false));
    formBean.setModules(modules);

    // populate number of news item ( default 7 items )
    //
    ArrayList list = new ArrayList();
    for( int a = 0; a < ModuleInstance.NUMBER_OF_ITEMS_IN_TEASER; a++ ) {
        list.add(new CommonInstancesForm.Item(Integer.toString(a + 1)));
    }
    formBean.setNumOfItemsInTeaser(list);
    // -----------------------------------------

    return mapping.findForward("forward");
    }

}
