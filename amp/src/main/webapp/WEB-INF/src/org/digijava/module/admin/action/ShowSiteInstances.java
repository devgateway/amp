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
import java.util.Set;
import java.util.TreeSet;

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
import org.digijava.module.admin.form.SiteInstancesForm;
import org.digijava.module.admin.util.DbUtil;

public class ShowSiteInstances extends Action {

    public ActionForward execute(ActionMapping mapping,
                                 ActionForm form,
                                 javax.servlet.http.HttpServletRequest request,
                                 javax.servlet.http.HttpServletResponse
                                 response) throws java.lang.Exception {


        Site currentSite = RequestUtils.getSite(request);
        Site realSite = DbUtil.getSite(currentSite.getId());
        
        Set<String> modulesConfigInstanceNames = DigiConfigManager.getModulesConfig().keySet();

        SiteInstancesForm formBean = (SiteInstancesForm) form;
        formBean.setInstances(new ArrayList());
        Iterator iter = realSite.getModuleInstances().iterator();
        while (iter.hasNext()) {
            ModuleInstance inst = (ModuleInstance) iter.next();
            if (modulesConfigInstanceNames.contains(inst.getModuleName())) {
                SiteInstancesForm.InstanceInfo info = new SiteInstancesForm.InstanceInfo();
                info.setId(inst.getModuleInstanceId());
                info.setModule(inst.getModuleName());
                info.setInstance(inst.getInstanceName());
                info.setSelectedNumOfItemsInTeaser(inst.getNumberOfItemsInTeaser());
                if (inst.getRealInstance() != null) {
                    info.setMappingId(inst.getRealInstance().getModuleInstanceId());
                    info.setMappingSite(inst.getRealInstance().getSite().getName());
                    info.setMappingInstance(inst.getInstanceName());
                    info.setPermitted(inst.isPermitted());
                } else {
                    info.setMappingId(null);
                    info.setMappingSite(null);
                    info.setMappingInstance(null);
                    info.setPermitted(inst.isPermitted());
                }
    
                formBean.getInstances().add(info);
            }
        }

        ViewConfig viewConfig = ViewConfigFactory.getInstance().getViewConfig(currentSite);

        TreeSet modules = new TreeSet();
        modules.addAll(modulesConfigInstanceNames);
        modules.addAll(viewConfig.getReferencedModules(false));
        formBean.setModules(modules);

        List refInstances = DbUtil.getReferencedInstances(currentSite.
            getId());

        HashSet permittedInst = new HashSet();
        iter = refInstances.iterator();
        while (iter.hasNext()) {
            ModuleInstance item = (ModuleInstance)iter.next();
            if (item.isPermitted()) {
                permittedInst.add(item.getModuleInstanceId());
            }
        }
        String[] permitArr = new String[permittedInst.size()];
        iter = permittedInst.iterator();
        int i=0;
        while (iter.hasNext()) {
            Long item = (Long)iter.next();
            permitArr[i] = item.toString();
            i++;
        }
        formBean.setRefInstances(new ArrayList(refInstances));
        formBean.setPermittedInstances(permitArr);

        // populate number of news item ( default 7 items )
        //
        ArrayList list = new ArrayList();
        for( int a = 0; a < ModuleInstance.NUMBER_OF_ITEMS_IN_TEASER; a++ ) {
            list.add(new SiteInstancesForm.Item(Integer.toString(a + 1)));
        }
        // -----------------------------------------

        ModuleInstance defInst = realSite.getDefaultInstance();
        Long defInstId = (defInst==null)?new Long(0):defInst.getModuleInstanceId();
        formBean.setDefaultModeuleIndex(defInstId.toString());
        formBean.setNumOfItemsInTeaser(list);


        return mapping.findForward("forward");
    }

}
