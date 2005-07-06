/*
 *   DgHttpRequestWrapper.java
 * 	 @Author Lasha Dolidze lasha@digijava.org
 * 	 Created:
 * 	 CVS-ID: $Id: DgHttpRequestWrapper.java,v 1.1 2005-07-06 10:34:08 rahul Exp $
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

package org.digijava.kernel.request;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.Cookie;
import java.util.Enumeration;
import java.security.Principal;
import javax.servlet.http.HttpSession;
import java.io.UnsupportedEncodingException;
import javax.servlet.ServletInputStream;
import java.io.IOException;
import java.util.Map;
import java.io.BufferedReader;
import java.util.Locale;
import javax.servlet.RequestDispatcher;
import java.util.HashMap;
import java.util.ArrayList;
import java.util.Iterator;
import org.digijava.kernel.Constants;
import java.util.Set;
import java.util.Collection;

/**
 * <p>Title: DiGiJava</p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company: </p>
 * @author not attributable
 * @version 1.0
 */

public class DgHttpRequestWrapper
    implements HttpServletRequest {

    private HttpServletRequest request;

    public DgHttpRequestWrapper(HttpServletRequest request) {
        this.request = request;
    }

    public String getAuthType() {
        return request.getAuthType();
    }

    public Cookie[] getCookies() {
        return request.getCookies();
    }

    public long getDateHeader(String name) {
        return request.getDateHeader(name);
    }

    public String getHeader(String name) {
        return request.getHeader(name);
    }

    public Enumeration getHeaders(String name) {
        return request.getHeaders(name);
    }

    public Enumeration getHeaderNames() {
        return request.getHeaderNames();
    }

    public int getIntHeader(String name) {
        return request.getIntHeader(name);
    }

    public String getMethod() {
        return request.getMethod();
    }

    public String getPathInfo() {
        return request.getPathInfo();
    }

    public String getPathTranslated() {
        return request.getPathTranslated();
    }

    public String getContextPath() {
        return request.getContextPath();
    }

    public String getQueryString() {
        return request.getQueryString();
    }

    public String getRemoteUser() {
        return request.getRemoteUser();
    }

    public boolean isUserInRole(String role) {
        return request.isUserInRole(role);
    }

    public Principal getUserPrincipal() {
        return request.getUserPrincipal();
    }

    public String getRequestedSessionId() {
        return request.getRequestedSessionId();
    }

    public String getRequestURI() {
        return request.getRequestURI();
    }

    public StringBuffer getRequestURL() {
        return request.getRequestURL();
    }

    public String getServletPath() {
        return request.getServletPath();
    }

    public HttpSession getSession(boolean create) {
        return request.getSession(create);
    }

    public HttpSession getSession() {
        return request.getSession();
    }

    public boolean isRequestedSessionIdValid() {
        return request.isRequestedSessionIdValid();
    }

    public boolean isRequestedSessionIdFromCookie() {
        return request.isRequestedSessionIdFromCookie();
    }

    public boolean isRequestedSessionIdFromURL() {
        return request.isRequestedSessionIdFromURL();
    }

    public boolean isRequestedSessionIdFromUrl() {
        return request.isRequestedSessionIdFromUrl();
    }

    public Object getAttribute(String name) {
        return request.getAttribute(name);
    }

    public Enumeration getAttributeNames() {
        return request.getAttributeNames();
    }

    public String getCharacterEncoding() {
        return request.getCharacterEncoding();
    }

    public void setCharacterEncoding(String encode) throws java.io.
        UnsupportedEncodingException {
        request.setCharacterEncoding(encode);
    }

    public int getContentLength() {
        return request.getContentLength();
    }

    public String getContentType() {
        return request.getContentType();
    }

    public ServletInputStream getInputStream() throws java.io.IOException {
        return request.getInputStream();
    }

    public String getParameter(String name) {

        HashMap map = (HashMap)request.getAttribute(Constants.DIGI_PARAM_MAP);
        Object objectMap = null;

        if( map != null ) {
            objectMap = map.get(name);
            if (objectMap != null) {
                if (objectMap instanceof String[]) {
                    return ( (String[]) objectMap).toString();
                }
                else {
                    return (String) objectMap;
                }
            }
        }

        return request.getParameter(name);
    }

    public Enumeration getParameterNames() {

        HashMap map = (HashMap)request.getAttribute(Constants.DIGI_PARAM_MAP);
        Set objectSet = null;

        if( map != null ) {
            objectSet = map.keySet();
            if( objectSet !=  null )
                return new EnumerationWrapper(objectSet);
        }

        return request.getParameterNames();
    }

    public String[] getParameterValues(String name) {

        HashMap map = (HashMap)request.getAttribute(Constants.DIGI_PARAM_MAP);
        Collection values = null;

        if( map != null ) {
            values = map.values();
            if( values !=  null )
                return (String[])values.toArray();
        }

        return request.getParameterValues(name);
    }

    public Map getParameterMap() {

        HashMap map = (HashMap)request.getAttribute(Constants.DIGI_PARAM_MAP);
        if( map != null ) {
            return map;
        }

        return request.getParameterMap();
    }

    public String getProtocol() {
        return request.getProtocol();
    }

    public String getScheme() {
        return request.getScheme();
    }

    public String getServerName() {
        return request.getServerName();
    }

    public int getServerPort() {
        return request.getServerPort();
    }

    public BufferedReader getReader() throws java.io.IOException {
        return request.getReader();
    }

    public String getRemoteAddr() {
        return request.getRemoteAddr();
    }

    public String getRemoteHost() {
        return request.getRemoteHost();
    }

    public void setAttribute(String name, Object value) {
        request.setAttribute(name,value);
    }

    public void removeAttribute(String name) {
        request.removeAttribute(name);
    }

    public Locale getLocale() {
        return request.getLocale();
    }

    public Enumeration getLocales() {
        return request.getLocales();
    }

    public boolean isSecure() {
        return request.isSecure();
    }

    public RequestDispatcher getRequestDispatcher(String path) {
        return request.getRequestDispatcher(path);
    }

    public String getRealPath(String name) {
        return request.getRealPath(name);
    }

}