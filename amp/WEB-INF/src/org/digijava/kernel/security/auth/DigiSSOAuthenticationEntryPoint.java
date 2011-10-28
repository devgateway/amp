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
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

import org.acegisecurity.AuthenticationException;
import org.acegisecurity.ui.AuthenticationEntryPoint;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.digijava.kernel.security.HttpLoginManager;
import javax.servlet.RequestDispatcher;

public class DigiSSOAuthenticationEntryPoint
    implements AuthenticationEntryPoint {
    /**
     * commence
     *
     * @param servletRequest ServletRequest
     * @param servletResponse ServletResponse
     * @param authenticationException AuthenticationException
     * @throws IOException
     * @throws ServletException
     * @todo Implement this org.acegisecurity.ui.AuthenticationEntryPoint
     *   method
     */
    public void commence(ServletRequest servletRequest,
                         ServletResponse servletResponse,
                         AuthenticationException authenticationException) throws
        IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;

        if (authenticationException instanceof HttpLoginManager.RedirectRequiredException) {
            HttpLoginManager.redirectToLoginSite(request, response);
            return;
        }

        RequestDispatcher rd = request.getRequestDispatcher("/showLayout.do?layout=login");
        rd.forward(servletRequest, servletResponse);
    }
}
