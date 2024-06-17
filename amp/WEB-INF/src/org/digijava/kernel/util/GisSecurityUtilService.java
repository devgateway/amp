package org.digijava.kernel.util;

import org.digijava.kernel.ampapi.endpoints.util.GisConstants;
import org.digijava.module.aim.util.FeaturesUtil;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;

@Component
public class GisSecurityUtilService {
    public boolean isUserAllowedAccess(HttpServletRequest request, Authentication authentication) {
        boolean loginRequired = FeaturesUtil.isVisibleFeature(GisConstants.LOGIN_REQUIRED);
        if (loginRequired)
        {
            return !(authentication instanceof AnonymousAuthenticationToken) && authentication.isAuthenticated();

        }
        return true;
    }
}
