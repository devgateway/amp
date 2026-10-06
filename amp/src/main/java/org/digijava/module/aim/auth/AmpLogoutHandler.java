package org.digijava.module.aim.auth;

import org.digijava.kernel.util.RequestUtils;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutHandler;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
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

        //save dev mode setting after logout.
        boolean wasDevMode=RequestUtils.isDevelopmentModeActive(httpServletRequest);
       // session.removeAttribute("debugFM");
//      Session jcrWriteSession     = (Session)session.getAttribute(CrConstants.JCR_WRITE_SESSION);
//      if(jcrWriteSession!=null) jcrWriteSession.logout();
//      
//      Session jcrReadSession      = (Session)session.getAttribute(CrConstants.JCR_READ_SESSION);
//      if(jcrReadSession!=null) jcrReadSession.logout();
        
        session.invalidate();

        //if user switched to dev mode before logout, then restore it.
        if (wasDevMode){
            RequestUtils.switchToDevelopmentMode(httpServletRequest);
        }
    }
}
