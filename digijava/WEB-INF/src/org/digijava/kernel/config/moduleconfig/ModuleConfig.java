/*
 *   ModuleConfig.java
 *   @Author Mikheil Kapanadze mikheil@digijava.org
 * 	 Created: Sep 3, 2003
 * 	 CVS-ID: $Id: ModuleConfig.java,v 1.1 2005-07-06 10:34:20 rahul Exp $
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

import java.util.HashMap;
import java.util.List;
import java.util.ArrayList;
import org.digijava.kernel.config.HibernateClasses;

public class ModuleConfig {

    String name;
    String defaultInstance;
    List actions;
    Security security;
    HibernateClasses hibernateClasses;

    public ModuleConfig() {
        actions = new ArrayList();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Security getSecurity() {
        return security;
    }

    public void setSecurity(Security security) {
        this.security = security;
    }

    public void addAction(Action action) {
        actions.add(action);
    }

    public List getActions() {
        return actions;
    }

    public HibernateClasses getHibernateClasses() {
        return hibernateClasses;
    }

    public void setHibernateClasses(HibernateClasses hibernateClasses) {
        this.hibernateClasses = hibernateClasses;
    }

    public Action getAction(String pattern) {

        String searchPatter = null;
        if (pattern.startsWith("/")) {
            searchPatter = pattern;
        }
        else {
            searchPatter = "/" + pattern;
        }

        for (int i = 0; i < actions.size(); i++) {
            Action tmpAction = (Action) actions.get(i);
            if (tmpAction.getPattern().matches(searchPatter))
                return tmpAction;
        }

        return null;
    }

    public String getDefaultInstance() {
        return defaultInstance;
    }

    public void setDefaultInstance(String defaultInstance) {
        this.defaultInstance = defaultInstance;
    }

}