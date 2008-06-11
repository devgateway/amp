package org.digijava.module.aim.auth;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.acegisecurity.Authentication;
import org.acegisecurity.ui.logout.LogoutHandler;
import org.digijava.kernel.util.RequestUtils;

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

        //save dev mode setting after logout.
        boolean wasDevMode=RequestUtils.isDevelopmentModeActive(httpServletRequest);
       // session.removeAttribute("debugFM");
        session.invalidate();

        //if user switched to dev mode before logout, then restore it.
        if (wasDevMode){
            RequestUtils.switchToDevelopmentMode(httpServletRequest);
        }
    }
}
