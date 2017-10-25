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

package org.digijava.kernel.config.moduleconfig;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.digijava.kernel.config.HibernateClasses;

public class ModuleConfig {

    private String name;
    private String defaultInstance;
    private List actions;
    private Security security;
    private HibernateClasses hibernateClasses;
    private Set webappContextListeners;

    public ModuleConfig() {
        actions = new ArrayList();
        webappContextListeners = new HashSet();
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

    public Set getWebappContextListeners() {
        return webappContextListeners;
    }

    public void setWebappContextListeners(Set webappContextListeners) {
        this.webappContextListeners = webappContextListeners;
    }

    public void addWebappContextListener(String listener) {
        webappContextListeners.add(listener);
    }
}
