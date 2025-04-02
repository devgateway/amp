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

package org.digijava.kernel.request.service;

import org.digijava.kernel.service.AbstractServiceImpl;
import org.digijava.kernel.service.ServiceContext;
import org.digijava.kernel.service.ServiceException;
import gnu.jel.CompilationException;
import gnu.jel.CompiledExpression;
import gnu.jel.Evaluator;
import gnu.jel.Library;

public class IgnoredAgentsService
    extends AbstractServiceImpl {

    private String allowedAgents;
    private String ignoredAgents;
    private boolean ignoreUndefined = false;
    private boolean checkAllowedFirst = true;
    private boolean ignoreByDefault = false;

    private CompiledExpression allowedExpression;
    private CompiledExpression ignoredExpression;


    protected void processInitEvent(ServiceContext serviceContext) throws
        ServiceException {
        Class[] stLib = new Class[1];
        stLib[0] = java.lang.Math.class;

        Class[] dotLib = new Class[3];
        dotLib[0] = java.lang.String.class;
        dotLib[1] = java.lang.Double.class;
        dotLib[2] = gnu.jel.reflect.Double.class;

        Library lib =
            new Library(stLib, new Class[] {IgnoredAgentsServiceMethods.class},
                        dotLib, null, null);

        try {
            if (allowedAgents != null && allowedAgents.trim().length() != 0) {
                allowedExpression = Evaluator.compile(allowedAgents, lib);
            }
            else {
                allowedExpression = null;
            }

            if (ignoredAgents != null && ignoredAgents.trim().length() != 0) {
                ignoredExpression = Evaluator.compile(ignoredAgents, lib);
            }
            else {
                ignoredExpression = null;
            }
        }
        catch (CompilationException ex) {
            throw new ServiceException(ex);
        }
    }

    public String getAllowedAgents() {
        return allowedAgents;
    }

    public boolean isCheckAllowedFirst() {
        return checkAllowedFirst;
    }

    public String getIgnoredAgents() {
        return ignoredAgents;
    }

    public boolean isIgnoreUndefined() {
        return ignoreUndefined;
    }

    public boolean isIgnoreByDefault() {
        return ignoreByDefault;
    }

    public void setAllowedAgents(String allowedAgents) {
        this.allowedAgents = allowedAgents;
    }

    public void setCheckAllowedFirst(boolean checkAllowedFirst) {
        this.checkAllowedFirst = checkAllowedFirst;
    }

    public void setIgnoredAgents(String ignoredAgents) {
        this.ignoredAgents = ignoredAgents;
    }

    public void setIgnoreUndefined(boolean ignoreUndefined) {
        this.ignoreUndefined = ignoreUndefined;
    }

    public void setIgnoreByDefault(boolean ignoreByDefault) {
        this.ignoreByDefault = ignoreByDefault;
    }

    public boolean isIgnoredAgent(String agent) {
        if (agent == null) {
            return ignoreUndefined;
        }

        String realAgent = agent.trim();

        if (realAgent.length() == 0) {
            return ignoreUndefined;
        }
        IgnoredAgentsServiceMethods methods = new IgnoredAgentsServiceMethods(agent);
        Object [] methodsArr = new Object[] {methods};
        try {
            if (checkAllowedFirst) {
                if (allowedExpression != null &&
                    allowedExpression.evaluate_boolean(methodsArr)) {
                    return false;
                }
                if (ignoredExpression != null && ignoredExpression.evaluate_boolean(methodsArr)) {
                    return true;
                }
            }
            else {
                if (ignoredExpression != null && ignoredExpression.evaluate_boolean(methodsArr)) {
                    return true;
                }
                if (allowedExpression != null && allowedExpression.evaluate_boolean(methodsArr)) {
                    return false;
                }
            }
        }
        catch (Throwable ex) {
        }
        return ignoreByDefault;
    }
}
