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

package org.digijava.kernel.taglib;

import java.io.IOException;
import java.io.PrintWriter;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.Iterator;
import java.util.StringTokenizer;

import javax.security.auth.Subject;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.BodyTagSupport;

import org.apache.log4j.Logger;
import org.digijava.kernel.entity.ModuleInstance;
import org.digijava.kernel.request.Site;
import org.digijava.kernel.security.DigiSecurityManager;
import org.digijava.kernel.security.ModuleInstancePermission;
import org.digijava.kernel.security.ResourcePermission;
import org.digijava.kernel.security.SitePermission;
import org.digijava.kernel.security.permission.ObjectPermission;
import org.digijava.kernel.user.Group;
import org.digijava.kernel.user.User;
import org.digijava.kernel.util.RequestUtils;
import org.digijava.module.aim.helper.Constants;
import org.digijava.module.aim.helper.TeamMember;

public class SecureTag
    extends BodyTagSupport {

    private static final long serialVersionUID = 1L;

    private static Logger logger = Logger.getLogger(SecureTag.class);

    private Boolean globalAdmin = null;
    private String actions;
    private String group;
    private Boolean authenticated = null;
    private Boolean granted = new Boolean(true);
    private String idName = null;
    private String idProperty = null;
    private String idScope = null;
    private String permissionClass = null;

    /**
     * Process the start tag.
     *
     * @exception JspException if a JSP exception has occurred
     */
    public int doStartTag() {
        HttpServletRequest request =
            (HttpServletRequest) pageContext.getRequest();
        int paramCount = 0;
        if (globalAdmin != null) {
            paramCount++;
        }
        if (actions != null) {
            paramCount++;
        }
        if (authenticated != null) {
            paramCount++;
        }
        if (getGroup() != null) {
            paramCount++;
        }

        if (paramCount != 1) {
            try {
                pageContext.getOut().write("Secure tag usage: &lt;digi:secure globalAdmin=\"true|false\" | authenticated=\"true|false\" | actions=\"<i><b>action-list</b></i>\" | (actions=\"<i><b>action-list</b></i>\" && granted=\"true|false\") &gt;<i><b>body-content</b></i>&lt;/digi:secure&gt;");
            }
            catch (IOException ex) {
                throw new RuntimeException(ex);
            }
            return SKIP_BODY;
        }
        if (globalAdmin != null) {
            boolean isCurrentUserGlobalAdmin = false;
            User user = RequestUtils.getUser(request);
            if (user != null && user.isGlobalAdmin()) {
                isCurrentUserGlobalAdmin = true;
            }
            /**
             * @todo use XOR here
             */
            if ( (isCurrentUserGlobalAdmin && globalAdmin.booleanValue()) ||
                (!isCurrentUserGlobalAdmin && !globalAdmin.booleanValue())
                ) {
                return EVAL_BODY_INCLUDE;
            }
            else {
                return SKIP_BODY;
            }
        }
        else {
            if (authenticated != null) {
                User user           = RequestUtils.getUser(request);
                boolean userIsNull = user==null;
                if (userIsNull) {
                    TeamMember tm   = (TeamMember)request.getSession().getAttribute(Constants.CURRENT_MEMBER);
                    if (tm != null)
                        userIsNull  = false;
                }
                if ( (!userIsNull && authenticated.booleanValue()) ||
                    (userIsNull && !authenticated.booleanValue())
                    ) {
                    return EVAL_BODY_INCLUDE;
                }
                else {
                    return SKIP_BODY;
                }
            } else if (getGroup() != null){
                Site site = RequestUtils.getSite(request);
                Iterator siteGroupIter = site.getGroups().iterator();
                while (siteGroupIter.hasNext()){
                    Group siteGroup = (Group) siteGroupIter.next();
                    if (siteGroup.getName().equals(this.getGroup())){
                        User user = RequestUtils.getUser(request);
                        if (user == null) return SKIP_BODY;
                        Iterator userGroupIter = user.getGroups().iterator();
                        while(userGroupIter.hasNext()){
                            Group userGroup = (Group)userGroupIter.next();
                            if (userGroup.getName().equals(this.getGroup())){
                                return EVAL_BODY_INCLUDE;
                            }
                        }
                        return SKIP_BODY;
                    }
                }
                return SKIP_BODY;
            } else {
                try {
                    Subject subject = RequestUtils.getSubject(request);
                    StringTokenizer st = new StringTokenizer(actions, ",");
                    boolean permitted = false;
                    boolean reverse = (granted != null && !granted.booleanValue());
                    while (st.hasMoreElements()) {
                        ResourcePermission permission = assemblePermission(st.
                            nextToken().
                            trim().toUpperCase());
                        if (permission instanceof ObjectPermission) {
                            ObjectPermission perm = (ObjectPermission)permission;
                            permitted = perm.getSecurityManager().
                                checkPermission(subject, perm);
                        } else {
                            permitted = DigiSecurityManager.checkPermission(
                                subject,
                                permission);
                        }
                        if (permitted) {
                            break;
                        }
                    }
                    if ( (reverse && !permitted) || (!reverse && permitted)) {
                        return EVAL_BODY_INCLUDE;
                    }
                    else {
                        return SKIP_BODY;
                    }
                }
                catch (Exception ex) {
                    logger.error("Error in secure tag", ex);
                    ex.printStackTrace(new PrintWriter(pageContext.getOut()));
                    return SKIP_BODY;
                }
            }
        }
    }

    private ResourcePermission assemblePermission(String action) throws
        ClassNotFoundException, JspException, SecurityException,
        NoSuchMethodException, InvocationTargetException,
        IllegalArgumentException, IllegalAccessException,
        InstantiationException {
        HttpServletRequest request =
            (HttpServletRequest) pageContext.getRequest();
        if (permissionClass == null) {
            ModuleInstance realInstance = RequestUtils.getRealModuleInstance(
                request);
            if (realInstance == null) {
                Site currentSite = RequestUtils.getSite(request);
                SitePermission sitePermission = new SitePermission(currentSite,
                    action);
                return sitePermission;
            }
            else {
                ModuleInstancePermission modInstPerm = new
                    ModuleInstancePermission(
                        realInstance, action);
                return modInstPerm;
            }
        }
        else {
            Class clazz = Class.forName(permissionClass);
            Object identity =
                org.apache.struts.taglib.TagUtils.getInstance().lookup(pageContext, idName,
                idProperty, idScope);
            Constructor constructor = clazz.getConstructor(new Class[] {
                identity.getClass(), String.class});
            ObjectPermission perm = (ObjectPermission) constructor.newInstance(new
                Object[] {identity, action});
            return perm;
        }
    }

    public String getActions() {
        return actions;
    }

    public void setActions(String actions) {
        this.actions = actions;
    }

    public boolean isGlobalAdmin() {
        return globalAdmin == null ? false : globalAdmin.booleanValue();
    }

    public void setGlobalAdmin(boolean globalAdmin) {
        this.globalAdmin = new Boolean(globalAdmin);
    }

    public boolean isAuthenticated() {
        return authenticated == null ? false : authenticated.booleanValue();
    }

    public void setAuthenticated(boolean authenticated) {
        this.authenticated = new Boolean(authenticated);
    }

    public Boolean getGranted() {
        return granted;
    }

    public String getIdName() {
        return idName;
    }

    public String getIdProperty() {
        return idProperty;
    }

    public String getIdScope() {
        return idScope;
    }

    public Logger getLogger() {
        return logger;
    }

    public String getPermissionClass() {
        return permissionClass;
    }

    public void setGranted(Boolean granted) {
        this.granted = granted;
    }

    public void setIdName(String idName) {
        this.idName = idName;
    }

    public void setIdProperty(String idProperty) {
        this.idProperty = idProperty;
    }

    public void setIdScope(String idScope) {
        this.idScope = idScope;
    }

    public void setLogger(Logger logger) {
        this.logger = logger;
    }

    public void setPermissionClass(String permissionClass) {
        this.permissionClass = permissionClass;
    }

    public void setGroup(String group) {
        this.group = group;
    }

    public String getGroup() {
        return group;
    }

}
