/*
 *   SaveInstances.java
 *   @Author Mikheil Kapanadze mikheil@digijava.org
 * 	 Created: Sep 5, 2003
 * 	 CVS-ID: $Id: SaveInstances.java,v 1.1 2005-07-06 10:34:10 rahul Exp $
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

import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;

import org.apache.log4j.Logger;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.digijava.kernel.entity.ModuleInstance;
import org.digijava.kernel.request.Site;
import org.digijava.kernel.util.DgUtil;
import org.digijava.kernel.util.SiteCache;
import org.digijava.module.admin.form.SiteInstancesForm;
import org.digijava.module.admin.util.DbUtil;
import org.digijava.kernel.util.RequestUtils;

public class SaveInstances extends Action {

 private static Logger logger = Logger.getLogger(SaveInstances.class);

    public ActionForward execute(ActionMapping mapping,
                                 ActionForm form,
                                 javax.servlet.http.HttpServletRequest request,
                                 javax.servlet.http.HttpServletResponse
                                 response) throws java.lang.Exception {

        Site currentSite = RequestUtils.getSite(request);
        Site realSite = DbUtil.getSite(currentSite.getId());
        SiteInstancesForm formBean = (SiteInstancesForm) form;

        HashSet editedItems = new HashSet();
        HashSet finalItems = new HashSet(formBean.getInstances());
        ModuleInstance defaultModuleInstance = null;
        Long defModuleInstanceId = new Long(formBean.getDefaultModeuleIndex());

        HashMap instances = new HashMap();
        Iterator iter = realSite.getModuleInstances().iterator();
        while (iter.hasNext()) {
            ModuleInstance  inst = (ModuleInstance) iter.next();
            instances.put(inst.getModuleInstanceId(), inst);
            if (inst.getModuleInstanceId().equals(defModuleInstanceId)) {
              defaultModuleInstance = inst;
            }
        }

        ActionErrors errors = new ActionErrors();
        iter = formBean.getInstances().iterator();
        while (iter.hasNext()) {
            SiteInstancesForm.InstanceInfo info = (SiteInstancesForm.
                InstanceInfo) iter.next();

            ModuleInstance moduleInstance = (ModuleInstance)instances.get(info.getId());
            if (moduleInstance == null) {
                moduleInstance = new ModuleInstance();
                moduleInstance.setSite(realSite);

                realSite.getModuleInstances().add(moduleInstance);
            } else {
                instances.remove(info.getId());
            }
            moduleInstance.setModuleName(info.getModule());
            moduleInstance.setInstanceName(info.getInstance());
            moduleInstance.setNumberOfItemsInTeaser(info.getSelectedNumOfItemsInTeaser());

            if (info.getMappingId()== null || info.getMappingId().equals(new Long(0))) {
                moduleInstance.setPermitted(true);
                moduleInstance.setRealInstance(null);
            } else {
                ModuleInstance realInst = null;
                realInst = DbUtil.getModuleInstance(info.
                    getMappingId());

                // if instance mapping was set, create assignment
                if ( (realInst != null) &&
                    (!realInst.getSite().getId().equals(currentSite.getId()))
                    && (realInst.getRealInstance() == null) &&
                    (realInst.getModuleName().equals(info.getModule()))) {
                   // If mapping was not changed, leave permitted flag as is
                   // else set it to false
                   if ( (moduleInstance.getRealInstance() != null) &&
                       (moduleInstance.getRealInstance().getModuleInstanceId().
                        equals(realInst.getModuleInstanceId()))) {
                       moduleInstance.setPermitted(moduleInstance.isPermitted());
                   }
                   else {
                       moduleInstance.setPermitted(false);
                   }
                   moduleInstance.setRealInstance(realInst);
                } else {
                    Object[] params = {
                        info.getModule(), info.getInstance(),
                        realInst.getSite().getName(), realInst.getInstanceName()};
                    errors.add(null,
                               new ActionError("error.admin.unacceptableMaster"));
                }
            }
        }
        if (!errors.isEmpty()) {
            saveErrors(request, errors);
            return mapping.findForward("error");
        } else {

            HashSet toDelete = new HashSet();
            iter = instances.values().iterator();
            while (iter.hasNext()) {
                ModuleInstance moduleInstance = (ModuleInstance) iter.next();
                moduleInstance.setSite(null);
                toDelete.add(moduleInstance.getModuleInstanceId());
            }
            logger.debug("ToDelete.size = " + toDelete.size());

            HashSet checked = new HashSet();
            if (formBean.getPermittedInstances() != null) {
                for (int i = 0; i < formBean.getPermittedInstances().length; i++) {
                    checked.add(new Long(formBean.getPermittedInstances()[i]));
                }
            }

            List refInstances = DbUtil.getReferencedInstances(realSite.getId());
            iter = refInstances.iterator();
            while (iter.hasNext()) {
                ModuleInstance item = (ModuleInstance)iter.next();
                if (toDelete.contains(item.getRealInstance().getModuleInstanceId())) {
                    item.setRealInstance(null);
                    item.setPermitted(true);
                } else {
                    if (checked.contains(item.getModuleInstanceId())) {
                        item.setPermitted(true);
                    } else {
                        item.setPermitted(false);
                    }
                }
            }

            realSite.setDefaultInstance(defaultModuleInstance);

            DbUtil.editSiteInstances(realSite, refInstances);

            SiteCache.getInstance().load();


            return mapping.findForward("forward");
        }
    }
}