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
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.regex.Pattern;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.digijava.kernel.security.HttpLoginManager;
import org.springframework.beans.factory.InitializingBean;

public class SSOAuthenticationFilter
    extends HttpServlet implements Filter, InitializingBean {

    private static Logger logger = Logger.getLogger(SSOAuthenticationFilter.class);

    private Pattern[] excludedPatterns;
    private boolean digiStyleLogin = true;
    private List excludedUrlPatters = new ArrayList();

    public void init(FilterConfig filterConfig) throws ServletException {
        /*
        String excludedUrlsParam = filterConfig.getInitParameter(
            "excluded-pattern");

        excludedPatterns = null;

        if (excludedUrlsParam != null) {
            String[] parameters = excludedUrlsParam.split(",");
            excludedPatterns = new Pattern[parameters.length];

            for (int i = 0; i < parameters.length; i++) {
                excludedPatterns[i] = Pattern.compile(parameters[i]);
            }

        }
        */
    }

    public void doFilter(ServletRequest servletRequest,
                         ServletResponse servletResponse,
                         FilterChain filterChain) throws IOException,
        ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;

        if (excludedPatterns != null) {
            int i = 0;
            for (; i < excludedPatterns.length; i++) {
                int contextPathLen = request.getContextPath().length();
                if (excludedPatterns[i].matcher(request.getRequestURI().substring(contextPathLen)).matches()) {
                    break;
                }
            }

            if (i < excludedPatterns.length) {
                filterChain.doFilter(request, response);
                return;
            }
        }

        request = HttpLoginManager.processLogin(request, digiStyleLogin);

        filterChain.doFilter(request, servletResponse);
    }

    public boolean isDigiStyleLogin() {
        return digiStyleLogin;
    }

    public List getExcludedUrlPatters() {
        return excludedUrlPatters;
    }

    public void setDigiStyleLogin(boolean digiStyleLogin) {
        this.digiStyleLogin = digiStyleLogin;
    }

    public void setExcludedUrlPatters(List excludedUrlPatters) {
        this.excludedUrlPatters = excludedUrlPatters;
    }

    public void afterPropertiesSet() throws Exception {
        excludedPatterns = new Pattern[excludedUrlPatters.size()];
        int i = 0;
        for (Iterator iter = excludedUrlPatters.iterator(); iter.hasNext(); ) {
            String item = (String) iter.next();
            excludedPatterns[i++] = Pattern.compile(item);
        }
    }
}
