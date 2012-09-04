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

package org.digijava.kernel.security.auth;

import java.io.IOException;
import java.net.URLEncoder;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.Authentication;
import org.springframework.security.context.SecurityContextHolder;
import org.springframework.security.ui.logout.LogoutFilter;
import org.springframework.security.ui.logout.LogoutHandler;
import org.springframework.util.Assert;

public class CasLogoutFilter
    extends LogoutFilter {

    //~ Instance fields ================================================================================================

    private String filterProcessesUrl = "/j_spring_logout";
    private String logoutSuccessUrl;
    private String logoutUrl;
    private LogoutHandler[] handlers;

    //~ Constructors ===================================================================================================

    public CasLogoutFilter(String logoutUrl, String logoutSuccessUrl, LogoutHandler[] handlers) {
        super(logoutSuccessUrl, handlers);
        Assert.hasText(logoutSuccessUrl, "LogoutSuccessUrl required");
        Assert.hasText(logoutUrl, "LogoutUrl required");
        Assert.notEmpty(handlers, "LogoutHandlers are required");

        this.logoutUrl = logoutUrl;
        this.logoutSuccessUrl = logoutSuccessUrl;
        this.handlers = handlers;
    }

    /**
     * Allow subclasses to modify when a logout should tak eplace.
     *
     * @param request the request
     * @param response the response
     *
     * @return <code>true</code> if logout should occur, <code>false</code> otherwise
     */
    protected boolean requiresLogout(HttpServletRequest request, HttpServletResponse response) {
        String uri = request.getRequestURI();
        int pathParamIndex = uri.indexOf(';');

        if (pathParamIndex > 0) {
            // strip everything after the first semi-colon
            uri = uri.substring(0, pathParamIndex);
        }

        return uri.endsWith(filterProcessesUrl);
    }

    /**
     * Allow subclasses to modify the redirection message.
     *
     * @param request the request
     * @param response the response
     * @param url the URL to redirect to
     *
     * @throws IOException in the event of any failure
     */
    protected void sendRedirect(HttpServletRequest request, HttpServletResponse response, String url)
        throws IOException {
        final String urlEncodedService = response.encodeURL(url);

        final StringBuffer buffer = new StringBuffer(255);

        synchronized (buffer) {
            buffer.append(this.logoutUrl);
            buffer.append("?service=");
            buffer.append(URLEncoder.encode(urlEncodedService, "UTF-8"));
        }
        response.sendRedirect(buffer.toString());
    }

    public void setFilterProcessesUrl(String filterProcessesUrl) {
        Assert.hasText(filterProcessesUrl, "FilterProcessesUrl required");
        this.filterProcessesUrl = filterProcessesUrl;
    }
}
