package org.digijava.kernel.security.auth;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.digijava.kernel.ampapi.endpoints.util.AmpApiToken;
import org.digijava.kernel.ampapi.endpoints.util.SecurityUtil;
import org.springframework.security.web.authentication.logout.LogoutHandler;

/**
 * Custom logout handler to perfom actions before the session expires and after the user clicks logout
 * @author JulianEduardo
 *
 */
public class AmpCustomLogoutHandler implements LogoutHandler {

	@Override
	public void logout(HttpServletRequest request,
			HttpServletResponse response,
			org.springframework.security.core.Authentication authentication) {

		AmpApiToken ampApiToken = (AmpApiToken) request.getSession().getAttribute(SecurityUtil.USER_TOKEN);
		
		if (ampApiToken != null) {
			SecurityUtil.removeTokenFromContext(request.getServletContext(), ampApiToken.getToken());
		}
	}
}