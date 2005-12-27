/*
 * AMPActionServlet.java
 * Created : 02-Aug-2005
 */
package org.digijava.module.aim.config;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.apache.struts.action.ActionServlet;
import org.digijava.module.aim.helper.TeamMember;

public class AMPActionServlet extends ActionServlet {

	private ServletContext ampContext;
	
	private static Logger logger = Logger.getLogger(AMPActionServlet.class);
    
	public void init(ServletConfig servletConfig) throws ServletException {
		ampContext = servletConfig.getServletContext();
		super.init(servletConfig);
	}

	public ServletContext getServletContext() {
		return ampContext;
	}
	
    protected void process(HttpServletRequest request, HttpServletResponse response)
            throws IOException, ServletException {
 
        HttpSession session = request.getSession();
        String sessId = session.getId();
        String url = request.getRequestURL().toString();
        
        if (url.charAt(url.length()-1) == '/' || url.endsWith("/index.do") || 
        		url.endsWith("/login.do")) {
        	
        	String siteAdmin = (String) session.getAttribute("ampAdmin");
    		TeamMember tm = (TeamMember) session.getAttribute(
			"currentMember");
    		if (tm != null) {
    			String fwdUrl = "viewMyDesktop.do";
    			response.sendRedirect(fwdUrl);        		
    		} else if (siteAdmin != null && "yes".equals(siteAdmin)) {
				String fwdUrl = "admin.do";
				response.sendRedirect(fwdUrl);        		
        	} else {
        		super.process(request, response);	
        	}
        } else {
            ArrayList sessionList = (ArrayList) ampContext.getAttribute("sessionList");
            int index = -1;
            if (sessionList != null) {
                index = Collections.binarySearch(sessionList,sessId);    
            }
            
            if (index > -1) {
                logger.debug("Editing the activity .....");

                int tempIndex = url.indexOf("/aim/");
                if (tempIndex != -1) {
                    String pggrp = request.getParameter("edit");
                    if (pggrp == null || !(pggrp.trim().equalsIgnoreCase("true"))) {
                        logger.debug("Moving from edit pages....Resetting fields");
                        sessionList.remove(index);
                        Collections.sort(sessionList);
                        synchronized (ampContext) {
                            HashMap activityMap = (HashMap) ampContext.
                            		getAttribute("editActivityList");
                            activityMap.remove(sessId);
                            ampContext.setAttribute("sessionList",sessionList);
                            ampContext.setAttribute("editActivityList",activityMap);
    					}
                    }
                }
            }
            super.process(request, response);        
        }
    }
}