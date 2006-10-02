package org.digijava.module.aim.auth;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.acegisecurity.Authentication;
import org.acegisecurity.ui.logout.LogoutHandler;
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

        if(session.getAttribute("currentMember") != null) {
            session.removeAttribute("currentMember");
        }

        if(session.getAttribute("teamLeadFlag") != null) {
            session.removeAttribute("teamLeadFlag");
        }

        if(session.getAttribute("ampAdmin") != null) {
            session.removeAttribute("ampAdmin");
        }

    }
}
