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

package org.digijava.kernel.request;

import java.io.IOException;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.digijava.kernel.exception.DgException;
import org.digijava.kernel.persistence.PersistenceManager;
import org.digijava.kernel.request.searchfriendly.FriendlyRequestProcessor;
import org.digijava.kernel.util.RequestUtils;

/**
 * This servlet is associated to the "/" pattern. It will be loaded when someone
 * opens URL like http://www.mysite.org:8080/digijava/dir1/ and forwards to
 * mainLayout.do for this site
 * @author Mikheil Kapanadze
 * @version 1.0
 */
public class ShowMasterLayout
    extends HttpServlet implements Filter {

    private static Logger logger = Logger.getLogger(ShowMasterLayout.class);

    //FilterConfig filterConfig;

    //Initialize global variables
    public void init() throws ServletException {
    }

    //Clean up resources
    public void destroy() {
    }

    public void init(FilterConfig config) throws ServletException {
        //this.filterConfig = filterConfig;
    }

    public void doFilter(ServletRequest request, ServletResponse response,
                         FilterChain chain) throws IOException,
        ServletException {

        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;
        if (logger.isDebugEnabled()) {
            logger.debug("New request arrived\n" +
                "=============================================================\n" +
                RequestUtils.getRequestInfo(httpRequest));
        }

            String uri = httpRequest.getRequestURI();
            int customPartStart = httpRequest.getContextPath().length();
            if (customPartStart == 1) {
                customPartStart = 0;
            }

            // Pass servlets
            if (uri.startsWith("/servlet/", customPartStart) ||
                uri.startsWith("/services/", customPartStart) ||
                uri.startsWith("/saiku/", customPartStart) ||
                uri.startsWith("/rest/", customPartStart)) {
                chain.doFilter(request, response);
            }
            else {
                if (uri.indexOf(".") == -1) {
                    boolean lastSlash = uri.endsWith("/");
                    String newPath = "index.do";

                    // Remove context path and append "index.do"
                    /**
                     * @todo needs to be refactored but not today. Mikheil
                     * 7 years later: not today either. Constantin
                     */
                    newPath = uri.substring(httpRequest.getContextPath().length()) +
                        (lastSlash ? "" : "/") + newPath;
                    if (logger.isDebugEnabled()) {
                        logger.debug("New Path is:" + newPath);
                    }
                    RequestDispatcher rd = request.getRequestDispatcher(newPath);
                    rd.forward(request, response);
                }

                else {
                    boolean processed = false;
                    try {
                        processed = FriendlyRequestProcessor.forwardRequest(httpRequest, httpResponse);
                    }
                    catch (DgException ex1) {
                        throw new ServletException(ex1);
                    }

                    if (!processed) {
                        chain.doFilter(request, response);
                    }
                }
            }
    }
}
