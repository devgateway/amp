/*
 * AMPActionServlet.java
 * Created : 02-Aug-2005
 */
package org.digijava.module.aim.config;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.apache.struts.action.ActionServlet;

public class AMPActionServlet extends ActionServlet {
    private static Logger logger = Logger.getLogger(AMPActionServlet.class);

    protected void process(HttpServletRequest request, HttpServletResponse response)
            throws IOException, ServletException {
 
        HttpSession session = request.getSession();
        String sessId = session.getId();
        
        ServletContext ampContext = getServletContext();
        ArrayList sessionList = (ArrayList) ampContext.getAttribute("sessionList");
        int index = -1;
        if (sessionList != null) {
            index = Collections.binarySearch(sessionList,sessId);    
        }
        
        if (index > -1) {
            logger.debug("Editing the activity .....");
            String url = request.getRequestURL().toString();
            int tempIndex = url.indexOf("/aim/");
            if (tempIndex != -1) {
                logger.debug("URL = " + url);
                String pggrp = request.getParameter("edit");
                if (pggrp == null || !(pggrp.trim().equalsIgnoreCase("true"))) {
                    logger.debug("Moving from edit pages....Resetting fields");
                    sessionList.remove(index);
                    Collections.sort(sessionList);
                    HashMap activityMap = (HashMap) ampContext.
                    		getAttribute("editActivityList");
                    activityMap.remove(sessId);
                    ampContext.setAttribute("sessionList",sessionList);
                    ampContext.setAttribute("editActivityList",activityMap);
                }
            }
        }
        super.process(request, response);
    }
}