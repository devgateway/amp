package org.digijava.module.aim.auth;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.acegisecurity.Authentication;
import org.acegisecurity.ui.logout.LogoutHandler;
import org.digijava.module.aim.helper.Constants;

import javax.servlet.http.HttpSession;

public class AmpLogoutHandler
    implements LogoutHandler {
    /**
     * logout
     *
     * @param httpServletRequest HttpServletRequest
     * @param httpServletResponse HttpServletResponse
     * @param authentication Authentication
     * @todo Implement this org.acegisecurity.ui.logout.LogoutHandler method
     */
    public void logout(HttpServletRequest httpServletRequest,
                       HttpServletResponse httpServletResponse,
                       Authentication authentication) {
        HttpSession session = httpServletRequest.getSession();
        /*
        if(session.getAttribute("currentMember") != null) {
            session.removeAttribute("currentMember");
        }
        if(session.getAttribute("teamLeadFlag") != null) {
            session.removeAttribute("teamLeadFlag");
        }
        if(session.getAttribute("ampAdmin") != null) {
            session.removeAttribute("ampAdmin");
        }
        if (session.getAttribute(Constants.AMP_PROJECTS) != null) {
        	session.removeAttribute(Constants.AMP_PROJECTS);
        }
        if (session.getAttribute(Constants.MY_LINKS) != null) {
        	session.removeAttribute(Constants.MY_LINKS);
        }
        if (session.getAttribute(Constants.MY_REPORTS) != null) {
        	session.removeAttribute(Constants.MY_REPORTS);
        }
        if (session.getAttribute(Constants.DEFAULT_TEAM_REPORT) != null) {
        	session.removeAttribute(Constants.DEFAULT_TEAM_REPORT);
        }
        if (session.getAttribute(Constants.MY_TASKS) != null) {
        	session.removeAttribute(Constants.MY_TASKS);
        }
        if (session.getAttribute(Constants.MY_TEAM_MEMBERS) != null) {
        	session.removeAttribute(Constants.MY_TEAM_MEMBERS);
        }
        if (session.getAttribute(Constants.DIRTY_ACTIVITY_LIST) != null) {
        	session.removeAttribute(Constants.DIRTY_ACTIVITY_LIST);
        }
        if (session.getAttribute(Constants.DESKTOP_SETTINGS_CHANGED) != null) {
        	session.removeAttribute(Constants.DESKTOP_SETTINGS_CHANGED);
        }
        if (session.getAttribute(Constants.DSKTP_FLTR_CHANGED) != null) {
        	session.removeAttribute(Constants.DSKTP_FLTR_CHANGED);
        }
        */
        session.invalidate();
    }
}
