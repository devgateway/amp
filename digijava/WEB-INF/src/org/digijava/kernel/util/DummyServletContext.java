/*
 *   ConfigListener.java
 * 	 @Author Lasha Dolidze lasha@digijava.org
 * 	 Created: Aug 3, 2003
 * 	 CVS-ID: $Id: DummyServletContext.java,v 1.1 2005-07-06 12:00:13 rahul Exp $
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
package org.digijava.kernel.util;

import javax.servlet.ServletContext;
import java.util.Set;
import java.net.URL;
import java.net.MalformedURLException;
import java.io.InputStream;
import javax.servlet.RequestDispatcher;
import javax.servlet.Servlet;
import javax.servlet.ServletException;
import java.util.Enumeration;
import java.io.File;


public class DummyServletContext implements ServletContext {

    private File rootDir;

    public DummyServletContext(String rootPath) {
        File root = new File(rootPath);
        if (!root.exists() || !root.isDirectory()) {
            throw new IllegalArgumentException("Path " + rootPath + " does not point to directory");
        }
        this.rootDir = root;
        System.out.println("DummyServletContext: " + root.getAbsolutePath());
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

        System.out.println("DummyServletContext:.getRealPath(" + parm1 + "): " + path);
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
}
