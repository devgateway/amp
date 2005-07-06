/*
 *   Security.java
 *   @Author Mikheil Kapanadze mikheil@digijava.org
 * 	 Created: Sep 3, 2003
 * 	 CVS-ID: $Id: Security.java,v 1.1 2005-07-06 10:34:20 rahul Exp $
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
package org.digijava.kernel.config.moduleconfig;

import java.util.List;
import java.util.ArrayList;

public class Security {

    String defaultAction;
    List actions;
    boolean loginRequired;

    public Security() {
        actions = new ArrayList();
        loginRequired = false;
    }

    public List getActions() {
        return actions;
    }

    public void setActions(List actions) {
        this.actions = actions;
    }

    public String getDefaultAction() {
        return defaultAction;
    }

    public void setDefaultAction(String defaultAction) {
        this.defaultAction = defaultAction;
    }

    public void addAction(Action action) {
        actions.add(action);
    }

    public boolean isLoginRequired() {
        return loginRequired;
    }

    public void setLoginRequired(boolean loginRequired) {
        this.loginRequired = loginRequired;
    }

    public void setLoginRequired(String loginRequired) {
        this.loginRequired = Boolean.getBoolean(loginRequired);
    }

}