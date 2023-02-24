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

package org.digijava.kernel.util;

import java.io.File;
import java.io.InputStream;
import java.net.URL;
import java.util.Enumeration;
import java.util.EventListener;
import java.util.Map;
import java.util.Set;

import javax.servlet.*;
import javax.servlet.descriptor.JspConfigDescriptor;


public class DummyServletContext implements ServletContext {

    private File rootDir;

    public DummyServletContext(String rootPath) {
        File root = new File(rootPath);
        if (!root.exists() || !root.isDirectory()) {
            throw new IllegalArgumentException("Path " + rootPath + " does not point to directory");
        }
        this.rootDir = root;
    }

    public ServletContext getContext(String parm1) {
        /**@todo Implement this javax.servlet.ServletContext method*/
        throw new java.lang.UnsupportedOperationException("Method getContext() not yet implemented.");
    }
    public int getMajorVersion() {
        /**@todo Implement this javax.servlet.ServletContext method*/
        throw new java.lang.UnsupportedOperationException("Method getMajorVersion() not yet implemented.");
    }
    public int getMinorVersion() {
        /**@todo Implement this javax.servlet.ServletContext method*/
        throw new java.lang.UnsupportedOperationException("Method getMinorVersion() not yet implemented.");
    }

    @Override
    public int getEffectiveMajorVersion() {
        return 0;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public int getEffectiveMinorVersion() {
        return 0;  //To change body of implemented methods use File | Settings | File Templates.
    }

    public String getMimeType(String parm1) {
        /**@todo Implement this javax.servlet.ServletContext method*/
        throw new java.lang.UnsupportedOperationException("Method getMimeType() not yet implemented.");
    }
    public Set getResourcePaths(String parm1) {
        /**@todo Implement this javax.servlet.ServletContext method*/
        throw new java.lang.UnsupportedOperationException("Method getResourcePaths() not yet implemented.");
    }
    public URL getResource(String parm1) throws java.net.MalformedURLException {
        /**@todo Implement this javax.servlet.ServletContext method*/
        throw new java.lang.UnsupportedOperationException("Method getResource() not yet implemented.");
    }
    public InputStream getResourceAsStream(String parm1) {
        /**@todo Implement this javax.servlet.ServletContext method*/
        throw new java.lang.UnsupportedOperationException("Method getResourceAsStream() not yet implemented.");
    }
    public RequestDispatcher getRequestDispatcher(String parm1) {
        /**@todo Implement this javax.servlet.ServletContext method*/
        throw new java.lang.UnsupportedOperationException("Method getRequestDispatcher() not yet implemented.");
    }
    public RequestDispatcher getNamedDispatcher(String parm1) {
        /**@todo Implement this javax.servlet.ServletContext method*/
        throw new java.lang.UnsupportedOperationException("Method getNamedDispatcher() not yet implemented.");
    }
    public Servlet getServlet(String parm1) throws javax.servlet.ServletException {
        /**@todo Implement this javax.servlet.ServletContext method*/
        throw new java.lang.UnsupportedOperationException("Method getServlet() not yet implemented.");
    }
    public Enumeration getServlets() {
        /**@todo Implement this javax.servlet.ServletContext method*/
        throw new java.lang.UnsupportedOperationException("Method getServlets() not yet implemented.");
    }
    public Enumeration getServletNames() {
        /**@todo Implement this javax.servlet.ServletContext method*/
        throw new java.lang.UnsupportedOperationException("Method getServletNames() not yet implemented.");
    }
    public void log(String parm1) {
        /**@todo Implement this javax.servlet.ServletContext method*/
        throw new java.lang.UnsupportedOperationException("Method log() not yet implemented.");
    }
    public void log(Exception parm1, String parm2) {
        /**@todo Implement this javax.servlet.ServletContext method*/
        throw new java.lang.UnsupportedOperationException("Method log() not yet implemented.");
    }
    public void log(String parm1, Throwable parm2) {
        /**@todo Implement this javax.servlet.ServletContext method*/
        throw new java.lang.UnsupportedOperationException("Method log() not yet implemented.");
    }
    public String getRealPath(String parm1) {
        String path;
        if (parm1.startsWith("/")) {
            path = rootDir.getAbsolutePath() + parm1;
        } else {
            path = rootDir.getAbsolutePath() + "/" + parm1;
        }
        return path;
    }
    public String getServerInfo() {
        /**@todo Implement this javax.servlet.ServletContext method*/
        throw new java.lang.UnsupportedOperationException("Method getServerInfo() not yet implemented.");
    }
    public String getInitParameter(String parm1) {
        /**@todo Implement this javax.servlet.ServletContext method*/
        throw new java.lang.UnsupportedOperationException("Method getInitParameter() not yet implemented.");
    }
    public Enumeration getInitParameterNames() {
        /**@todo Implement this javax.servlet.ServletContext method*/
        throw new java.lang.UnsupportedOperationException("Method getInitParameterNames() not yet implemented.");
    }

    @Override
    public boolean setInitParameter(String s, String s2) {
        return false;  //To change body of implemented methods use File | Settings | File Templates.
    }

    public Object getAttribute(String parm1) {
        /**@todo Implement this javax.servlet.ServletContext method*/
        throw new java.lang.UnsupportedOperationException("Method getAttribute() not yet implemented.");
    }
    public Enumeration getAttributeNames() {
        /**@todo Implement this javax.servlet.ServletContext method*/
        throw new java.lang.UnsupportedOperationException("Method getAttributeNames() not yet implemented.");
    }
    public void setAttribute(String parm1, Object parm2) {
        /**@todo Implement this javax.servlet.ServletContext method*/
        throw new java.lang.UnsupportedOperationException("Method setAttribute() not yet implemented.");
    }
    public void removeAttribute(String parm1) {
        /**@todo Implement this javax.servlet.ServletContext method*/
        throw new java.lang.UnsupportedOperationException("Method removeAttribute() not yet implemented.");
    }
    public String getServletContextName() {
        /**@todo Implement this javax.servlet.ServletContext method*/
        throw new java.lang.UnsupportedOperationException("Method getServletContextName() not yet implemented.");
    }

    @Override
    public ServletRegistration.Dynamic addServlet(String s, String s2) {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public ServletRegistration.Dynamic addServlet(String s, Servlet servlet) {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public ServletRegistration.Dynamic addServlet(String s, Class<? extends Servlet> aClass) {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public <T extends Servlet> T createServlet(Class<T> tClass) throws ServletException {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public ServletRegistration getServletRegistration(String s) {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public Map<String, ? extends ServletRegistration> getServletRegistrations() {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public FilterRegistration.Dynamic addFilter(String s, String s2) {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public FilterRegistration.Dynamic addFilter(String s, Filter filter) {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public FilterRegistration.Dynamic addFilter(String s, Class<? extends Filter> aClass) {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public <T extends Filter> T createFilter(Class<T> tClass) throws ServletException {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public FilterRegistration getFilterRegistration(String s) {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public Map<String, ? extends FilterRegistration> getFilterRegistrations() {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public SessionCookieConfig getSessionCookieConfig() {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void setSessionTrackingModes(Set<SessionTrackingMode> sessionTrackingModes) throws IllegalStateException, IllegalArgumentException {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public Set<SessionTrackingMode> getDefaultSessionTrackingModes() {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public Set<SessionTrackingMode> getEffectiveSessionTrackingModes() {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void addListener(Class<? extends EventListener> aClass) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void addListener(String s) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public <T extends EventListener> void addListener(T t) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public <T extends EventListener> T createListener(Class<T> tClass) throws ServletException {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void declareRoles(String... strings) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public ClassLoader getClassLoader() {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public JspConfigDescriptor getJspConfigDescriptor() {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    public String getContextPath() {
        /**@todo Implement this javax.servlet.ServletContext method*/
        throw new java.lang.UnsupportedOperationException("Method getContextPath() not yet implemented.");
    }

    @Override
    public String getVirtualServerName() {
        return null;
    }
}
