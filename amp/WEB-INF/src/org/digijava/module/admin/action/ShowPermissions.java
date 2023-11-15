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

import org.apache.log4j.Logger;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.digijava.kernel.entity.ModuleInstance;
import org.digijava.kernel.request.Site;
import org.digijava.kernel.security.DigiSecurityManager;
import org.digijava.kernel.security.ModuleInstancePermission;
import org.digijava.kernel.security.ResourcePermission;
import org.digijava.kernel.security.SitePermission;
import org.digijava.kernel.security.principal.GroupPrincipal;
import org.digijava.kernel.user.Group;
import org.digijava.kernel.user.GroupPermission;
import org.digijava.kernel.util.RequestUtils;
import org.digijava.module.admin.exception.AdminException;
import org.digijava.module.admin.form.GroupPermissionsForm;
import org.digijava.module.admin.util.DbUtil;

import java.security.Permission;
import java.security.PermissionCollection;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.List;

public class ShowPermissions
    extends Action {

    private static Logger logger = Logger.getLogger(ShowPermissions.class);

    public ActionForward execute(ActionMapping mapping,
                                 ActionForm form,
                                 javax.servlet.http.HttpServletRequest request,
                                 javax.servlet.http.HttpServletResponse
                                 response) throws java.lang.Exception {


        Site currentSite = RequestUtils.getSite(request);
        GroupPermissionsForm groupPermsForm = (GroupPermissionsForm) form;
        Group group = null;
        try {
            group = DbUtil.getGroup(groupPermsForm.getGroupId());
        }
        catch (AdminException ex) {
            logger.debug("Exception caught ",ex);
            return mapping.findForward("error");
        }


        groupPermsForm.setResources(new ArrayList());
        if (group.getSite().getId().equals(currentSite.getId())) {
            GroupPermissionsForm.Resource res = new GroupPermissionsForm.
                Resource();
            res.setId("-");
            res.setName(currentSite.getName());
            groupPermsForm.getResources().add(res);
        }
        Iterator iter = currentSite.getModuleInstances().iterator();
        while (iter.hasNext()) {
            ModuleInstance inst = (ModuleInstance) iter.next();
            if (inst.getRealInstance() == null) {
                GroupPermissionsForm.Resource res = new GroupPermissionsForm.
                    Resource();
                res.setId(inst.getModuleInstanceId().toString());
                res.setName(inst.getModuleName() + ":" + inst.getInstanceName());
                groupPermsForm.getResources().add(res);
            }
        }

        groupPermsForm.setPermissions(new ArrayList());
        ArrayList foreignPerms = new ArrayList();
        GroupPrincipal groupPrincipal = new GroupPrincipal(group.getId().
            longValue());
        PermissionCollection permissions = DigiSecurityManager.getPermissions(
            groupPrincipal);
        if (permissions != null) {
            Enumeration permissionEnum = permissions.elements();
            while (permissionEnum.hasMoreElements()) {
                Permission permission = (Permission) permissionEnum.nextElement();
                if (permission instanceof ResourcePermission) {
                    ResourcePermission rp = (ResourcePermission) permission;
                    String resourceId = null;
                    if (permission instanceof ModuleInstancePermission) {
                        ModuleInstancePermission mip = (
                            ModuleInstancePermission)
                            permission;
                        /**
                             * @todo getModuleInstance() must be called from kernel package
                         */
                        ModuleInstance modInst = DbUtil.getModuleInstance(mip.
                            getModuleInstanceId());
                        if (modInst.getSite().getId().equals(currentSite.getId())) {
                            resourceId = mip.getModuleInstanceId().toString();
                        }
                        else {
                            if (group.getSite().getId().equals(currentSite.
                                getId())) {
                                GroupPermissionsForm.ForeignPermissionInfo
                                    foreignInfo =
                                    new GroupPermissionsForm.
                                    ForeignPermissionInfo();
                                foreignInfo.setSite(modInst.getSite());
                                foreignInfo.setModule(modInst.getModuleName());
                                foreignInfo.setInstance(modInst.getInstanceName());
                                foreignInfo.setActions(mip.getActions());
                                foreignPerms.add(foreignInfo);
                            }
                        }
                    }
                    else
                    if (permission instanceof SitePermission) {
                        SitePermission sp = (SitePermission) permission;
                        if (sp.getSiteId().equals(currentSite.getId()) &&
                            group.getSite().getId().equals(currentSite.getId())) {
                            resourceId = "-";
                        }
                    }
                    if (resourceId != null) {
                        Iterator actionIter = rp.getSecurityActions().iterator();
                        while (actionIter.hasNext()) {
                            String item = (String) actionIter.next();
                            GroupPermissionsForm.PermissionInfo info = new
                                GroupPermissionsForm.PermissionInfo();
                            info.setId(0);
                            info.setResourceId(resourceId);
                            info.setAction(item);
                            groupPermsForm.getPermissions().add(info);
                        }
                    }
                }
            }
        }

/*        iter = group.getPermissions().iterator();
        while (iter.hasNext()) {
            GroupPermission perm = (GroupPermission) iter.next();

            // Get module instance, if this is a module permission
            ModuleInstance modInst = null;
            if (perm.getPermissionType() ==
                GroupPermission.MODULE_INSTANCE_PERMISSION) {
                Long targetId = new Long(perm.getTargetName());
                modInst = DbUtil.getModuleInstance(targetId);
            }

            // User can edit only permission on the current site or the
            // module instance, owned by the current site
            if ( ( (perm.getPermissionType() == GroupPermission.SITE_PERMISSION) &&
                  group.getSite().getId().equals(currentSite.getId())
                  )
                || ((modInst != null) &&
                modInst.getSite().getId().equals(currentSite.getId()))) {

                StringTokenizer tokenizer = new StringTokenizer(perm.getActions(),
                    ",");
                while (tokenizer.hasMoreTokens()) {
                    String action = tokenizer.nextToken().trim();

                    GroupPermissionsForm.PermissionInfo info = new
                        GroupPermissionsForm.
                        PermissionInfo();
                    info.setId(perm.getGroupPermissionId());
                    if (perm.getPermissionType() ==
                        GroupPermission.MODULE_INSTANCE_PERMISSION) {
                        info.setResourceId(perm.getTargetName());
                    }
                    else {
                        info.setResourceId("-");
                    }
                    info.setAction(action);
                    groupPermsForm.getPermissions().add(info);
                }
            }
            else {
                // Display other permissions for group, owned by the current
                // site
                logger.debug(modInst + "::" + group.getSite().getId() + "::" + currentSite.getId());
                if (modInst!= null && group.getSite().getId().equals(currentSite.getId())) {
                    GroupPermissionsForm.ForeignPermissionInfo foreignInfo =
                        new GroupPermissionsForm.ForeignPermissionInfo();
                    foreignInfo.setSite(modInst.getSite());
                    foreignInfo.setModule(modInst.getModuleName());
                    foreignInfo.setInstance(modInst.getInstanceName());
                    foreignInfo.setActions(perm.getActions());
                    foreignPerms.add(foreignInfo);
                }
            }
        }
        */
        if (! group.getSite().getId().equals(currentSite.getId())) {
            groupPermsForm.setSiteName(group.getSite().getName());
        } else {
            groupPermsForm.setSiteName(null);
        }

        groupPermsForm.setInheritedPermissions(new ArrayList());
        List inhPerms = DbUtil.getInheritedPermissions(group.getId());
        iter = null;
        if(inhPerms != null) iter = inhPerms.iterator();
        if(iter != null)
        while (iter.hasNext()) {
            GroupPermission perm = (GroupPermission)iter.next();
            GroupPermissionsForm.ForeignPermissionInfo inheritedInfo = new
                GroupPermissionsForm.ForeignPermissionInfo();
            inheritedInfo.setActions(perm.getActions());
            if (perm.getPermissionType() == GroupPermission.MODULE_INSTANCE_PERMISSION) {
                Long targetId = new Long(perm.getTargetName());
                ModuleInstance modInst = DbUtil.getModuleInstance(targetId);
                inheritedInfo.setSite(modInst.getSite());
                inheritedInfo.setModule(modInst.getModuleName());
                inheritedInfo.setInstance(modInst.getInstanceName());
            } else {
                inheritedInfo.setSite(perm.getGroup().getSite());
                inheritedInfo.setModule(null);
                inheritedInfo.setInstance(null);
            }
            groupPermsForm.getInheritedPermissions().add(inheritedInfo);
        }

        groupPermsForm.setForeignPerms(foreignPerms);
        groupPermsForm.setGroupName(group.getName());

        return mapping.findForward("forward");
    }

}
