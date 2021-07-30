/*
 * AMPActionServlet.java
 * Created : 02-Aug-2005
 */
package org.digijava.module.aim.config;

import org.apache.log4j.Logger;
import org.apache.struts.action.ActionServlet;
import org.digijava.module.aim.helper.Constants;

import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;

/**
 * Overrides the default struts ActionServlet
 *
 * When a user initiates an edit session, the system keep tracks of the activity id
 * that is being edited. Then if the user moves out of the edit screens without actually
 * saving the edited data, the system should have to remove the activity id from the
 * list of activities being edited. Essentially, the system locks any activity that
 * is being edited by a particular user and if the user abandons his wish to save the
 * edited values, the system has to unlock those activities.
 *
 * @author Priyajith
 *
 */
public class AMPActionServlet
    extends ActionServlet {

  private ServletContext ampContext; // the application scope variable
  private static Logger logger = Logger.getLogger(AMPActionServlet.class);

  public void init(ServletConfig servletConfig) throws ServletException {
    ampContext = servletConfig.getServletContext();
    super.init(servletConfig);
  }

  public ServletContext getServletContext() {
    return ampContext;
  }

  protected void process(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
    request.setCharacterEncoding("UTF-8");

    HttpSession session = request.getSession();
    String sessId = session.getId();
    String url = request.getRequestURL().toString();

    /*
     * Check whether user have initiated an edit session.
     * When ever user starts an edit session, his session id is stored in an application
     * context variable. The system first checks whether the current session id is
     * present in the sessionList. The sessionId's are stored in a sorted order.
     *
     * Two attributes are stored in the application scope object for locking the activities
     * 1. sessionList - This will store all the sessionId who have inititated an edit
     * 2. editActivityList - This attribute is a Map which will store the sessionId as the key
     *    against the activityId being edited by that session.
     *
     */
    ArrayList sessionList = null;
    synchronized (ampContext) {
      sessionList = (ArrayList) ampContext.getAttribute(Constants.SESSION_LIST);
    }
    int index = -1;
    if (sessionList != null) {
      index = Collections.binarySearch(sessionList, sessId);
    }

    /*
     * If the values of index is greater than -1, it means that the user have started an edit session.
     * Then the system will check whether the user is still doing the editing or is moving out of edit pages
     */
    if (index > -1) {
      int tempIndex = url.indexOf("/" + Constants.AIM_MODULE_KEY + "/");
      if (tempIndex != -1 && !url.endsWith("displayFlag.do") && !url.endsWith("selectLocation.do")
              && !url.endsWith("locationSelected.do") && !url.endsWith("editIPAContract.do")
              && !url.endsWith("addDisbOrderToContract.do") && !url.endsWith("addDisbOrderToDisb.do")) {
        String pggrp = request.getParameter("edit");
        /*
         * The value of the request parameter 'edit tells the system, whether the user is still in the edit
         * pages, if the values of this parameter is 'null' or a string value other than 'true', the user have
         * moved out of the edit pages. Then the system will remove the current sesionId and the activityId
         * that the user was editing from the application context variables
         */

        if (pggrp == null || ! (pggrp.trim().equalsIgnoreCase("true"))) {
          synchronized (ampContext) {
            sessionList.remove(index);
            Collections.sort(sessionList);

            Long actId = null;

            HashMap activityMap = (HashMap) ampContext
                .getAttribute(Constants.EDIT_ACT_LIST);

            actId = (Long) activityMap.get(sessId);
            activityMap.remove(sessId);

            HashMap tsaList = (HashMap) ampContext.getAttribute(Constants.
                TS_ACT_LIST);
            if (tsaList != null) {
              tsaList.remove(actId);
            }

            HashMap uList = (HashMap) ampContext.getAttribute(Constants.
                USER_ACT_LIST);
            if (uList != null) {
              Iterator itr = uList.keySet().iterator();
              while (itr.hasNext()) {
                Long uId = (Long) itr.next();
                Long tempActId = (Long) uList.get(uId);
                if (tempActId.longValue() == actId.longValue()) {
                  uList.remove(uId);
                  break;
                }
              }
            }

            ampContext.setAttribute(Constants.SESSION_LIST, sessionList);
            ampContext.setAttribute(Constants.EDIT_ACT_LIST, activityMap);
            ampContext.setAttribute(Constants.USER_ACT_LIST, uList);
            ampContext.setAttribute(Constants.TS_ACT_LIST, tsaList);
          }
        }
      }
    }
    // Call the struts default ActionServlet process() method
    super.process(request, response);  // JSP rendered here
    }
}
