/*
 *   SecureTag.java
 *   @Author Mikheil Kapanadze mikheil@digijava.org
 * 	 Created: Sep 22, 2003
 * 	 CVS-ID: $Id: SecureTag.java,v 1.1 2005-07-06 10:34:07 rahul Exp $
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
package org.digijava.kernel.taglib;

import java.io.IOException;
import java.util.StringTokenizer;
import javax.security.auth.Subject;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.jsp.tagext.BodyTagSupport;

import org.apache.struts.tiles.ComponentContext;
import org.digijava.kernel.Constants;
import org.digijava.kernel.entity.ModuleInstance;
import org.digijava.kernel.request.Site;
import org.digijava.kernel.security.DgSecurityManager;
import org.digijava.kernel.security.ResourcePermission;
import org.digijava.kernel.user.User;
import org.digijava.kernel.util.DgUtil;
import org.apache.log4j.Logger;
import org.digijava.kernel.util.RequestUtils;

public class SecureTag
    extends BodyTagSupport {

    private static Logger logger = Logger.getLogger(SecureTag.class);


    private Boolean globalAdmin = null;
    private String actions;
    private Boolean authenticated = null;
    private Boolean granted = new Boolean(true);

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
                User user = RequestUtils.getUser(request);
                if ( (user != null && authenticated.booleanValue()) ||
                    (user == null && !authenticated.booleanValue())
                    ) {
                    return EVAL_BODY_INCLUDE;
                }
                else {
                    return SKIP_BODY;
                }
            }
            else {

                ComponentContext context = ComponentContext.getContext(request);
                ModuleInstance moduleInstance = null;

                if (context != null) {
                    moduleInstance = (ModuleInstance) context.getAttribute(
                        Constants.
                        MODULE_INSTANCE_OBJECT);
                }
                else {
                    moduleInstance = (ModuleInstance) request.getAttribute(
                        Constants.
                        MODULE_INSTANCE_OBJECT);
                }

                ModuleInstance realInstance = DgUtil.getRealModuleInstance(
                    request);

                Subject subject = DgSecurityManager.getSubject(request);
                Site currentSite = RequestUtils.getSite(request);
                StringTokenizer st = new StringTokenizer(actions, ",");
                boolean permitted = false;
                boolean reverse = (granted != null && !granted.booleanValue());
                while (st.hasMoreElements()) {
                    ResourcePermission permission = null;
                    int action = ResourcePermission.
                        getActionCode(st.nextToken().
                                      trim().toUpperCase());
                    if (realInstance == null) {
                        permitted = DgSecurityManager.permitted(subject,
                            currentSite, action);
                    }
                    else {
                        permitted = DgSecurityManager.permitted(subject,
                            currentSite, realInstance, action);
                    }
                    if (permitted) {
                        break;
                    }
                }
                if ( (reverse && !permitted) || (!reverse && permitted )) {
                    return EVAL_BODY_INCLUDE;
                }
                else {
                    return SKIP_BODY;
                }
            }
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

    public void setGranted(Boolean granted) {
        this.granted = granted;
    }

}