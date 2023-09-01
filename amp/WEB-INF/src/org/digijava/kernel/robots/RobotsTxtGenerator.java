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

package org.digijava.kernel.robots;

import org.digijava.kernel.request.SiteDomain;
import org.digijava.kernel.robots.config.RobotsConfig;
import org.digijava.kernel.service.ServiceManager;
import org.digijava.kernel.util.SiteCache;

import javax.servlet.*;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;

public class RobotsTxtGenerator
    extends HttpServlet implements Filter {

    private ServletContext context;

    public void init(FilterConfig filterConfig) throws ServletException {
        this.context =  filterConfig.getServletContext();
    }

    public void doFilter(ServletRequest servletRequest,
                         ServletResponse servletResponse,
                         FilterChain filterChain) throws IOException,
        ServletException {

        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;

        RobotsTxtService service = (RobotsTxtService) ServiceManager.
            getInstance().getService("robotsService");
        if (service == null) {
            filterChain.doFilter(request, response);
            return;
        }

        SiteDomain siteDomain = SiteCache.getInstance().getSiteDomain(request.
            getServerName(), null);
        if (siteDomain == null) {
            response.sendError(HttpServletResponse.SC_NOT_FOUND);
            return;
        }


        RobotsConfig config = service.getRobotsConfigs().getRobotsConfig(siteDomain.getSiteDomain());

        if (config == null) {
            response.sendError(HttpServletResponse.SC_NOT_FOUND);
            return;
        } else {
            if (config.getContent() != null &&
                config.getContent().trim().length() != 0) {
                response.setContentType("text/plain");
                PrintWriter writer = servletResponse.getWriter();
                writer.write(config.getContent());
                writer.flush();
                return;
            }

            if (config.getPath() != null &&
                config.getPath().trim().length() != 0) {

              InputStream robotsTxt = context.getResourceAsStream(
                  config.getPath().trim());
              if (robotsTxt == null) {
                response.sendError(HttpServletResponse.SC_NOT_FOUND);
                return;
              } else {
                response.setContentType("text/plain");
                BufferedReader rd = new BufferedReader(new InputStreamReader(robotsTxt));
                PrintWriter writer = servletResponse.getWriter();

                String buffer;
                while ((buffer = rd.readLine()) != null) {
                  writer.println(buffer);
                }
                writer.flush();
                rd.close();

                return;
              }
            }
            response.sendError(HttpServletResponse.SC_NOT_FOUND);
            return;
        }
    }
}
