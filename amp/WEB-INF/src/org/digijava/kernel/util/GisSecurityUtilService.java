package org.digijava.kernel.util;

import org.digijava.module.aim.util.FeaturesUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;

@Component
public class GisSecurityUtilService {
    public boolean isUserAllowedAccess(HttpServletRequest request, Authentication authentication) {
        boolean loginRequired = FeaturesUtil.getGlobalSettingValueBoolean("Login Required For GIS");
        if (loginRequired)
        {

            return !(authentication instanceof AnonymousAuthenticationToken) && authentication.isAuthenticated();

        }
        return true;
    }
}
