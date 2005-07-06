/*
 *   ShowMasterLayout.java
 *   @Author Mikheil Kapanadze mikheil@digijava.org
 * 	 Created: Jul 4, 2003
     * 	 CVS-ID: $Id: ShowMasterLayout.java,v 1.1 2005-07-06 10:34:08 rahul Exp $
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

import java.io.IOException;
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
import javax.servlet.RequestDispatcher;
import org.digijava.kernel.persistence.PersistenceManager;
import org.digijava.kernel.exception.*;

/**
     * This servlet is associated to the "/" pattern. It will be loaded when someone
 * opens URL like http://www.mysite.org:8080/digijava/dir1/ and forwards to
 * mainLayout.do for this site
 * @author Mikheil Kapanadze
 * @version 1.0
 */
public class ShowMasterLayout
    extends HttpServlet
    implements Filter {

  private static Logger logger = Logger.getLogger(ShowMasterLayout.class);

  FilterConfig filterConfig;

  //Initialize global variables
  public void init() throws ServletException {
  }

  //Clean up resources
  public void destroy() {
  }

  public void init(FilterConfig config) throws ServletException {
    this.filterConfig = filterConfig;
  }

  public void doFilter(ServletRequest request, ServletResponse response,
                       FilterChain chain) throws IOException,
      ServletException {

    HttpServletRequest httpRequest = (HttpServletRequest) request;
    HttpServletResponse httpResponse = (HttpServletResponse) response;

    String uri = httpRequest.getRequestURI();
    if (uri.indexOf(".") == -1) {
      boolean lastSlash = uri.endsWith("/");
      String newPath = "index.do";
      /*
                      if (uri.endsWith("/admin") || uri.endsWith("/admin/")) {
          newPath = "default/showLayout.do";
                      } else {
          newPath ="showLayout.do";
                      }
       */

      // Remove context path and append "index.do"
      newPath = uri.substring(httpRequest.getContextPath().length()) +
          (lastSlash ? "" : "/") + newPath;
      logger.debug("New Path is:" + newPath);
      RequestDispatcher rd = request.getRequestDispatcher(newPath);
      rd.forward(request, response);
      //( (HttpServletResponse) response).sendRedirect(newPath);
    }
    else {
      if (!DgUrlProcessor.doProcess(httpRequest, httpResponse))
        chain.doFilter(request, response);
    }

    //Close DB Session related with this request if it exists
    try {
      PersistenceManager.closeRequestDBSessionIfNeeded();
    }
    catch (DgException ex) {
      logger.warn("Error closing hibernate session", ex);
    }
  }
}