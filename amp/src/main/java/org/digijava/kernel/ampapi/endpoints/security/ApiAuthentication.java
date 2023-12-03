package org.digijava.kernel.ampapi.endpoints.security;

import org.apache.log4j.Logger;
import org.digijava.kernel.ampapi.endpoints.errors.ApiErrorMessage;
import org.digijava.kernel.request.Site;
import org.digijava.kernel.request.SiteDomain;
import org.digijava.kernel.security.DgSecurityManager;
import org.digijava.kernel.security.ResourcePermission;
import org.digijava.kernel.user.User;
import org.digijava.kernel.util.RequestUtils;
import org.digijava.kernel.util.UserUtils;
import org.digijava.module.aim.helper.Constants;
import org.digijava.module.aim.util.AuditLoggerUtil;
import org.digijava.module.aim.util.TeamMemberUtil;
import org.digijava.module.um.dbentity.SuspendLogin;
import org.digijava.module.um.util.UmUtil;
import org.springframework.security.core.context.SecurityContextHolder;

import javax.security.auth.Subject;
import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Created by esoliani on 13/06/16.
 */
public final class ApiAuthentication {

    protected static Logger logger = Logger.getLogger(ApiAuthentication.class);

    public static ApiErrorMessage login(final User currentUser, final HttpServletRequest request) {
        ApiErrorMessage errorMessage = performSecurityChecks(currentUser, request);

        if (errorMessage != null) {
            SecurityContextHolder.getContext().setAuthentication(null);
            logger.warn(String.format("User '%s' failed to login. Cause: '%s'.",
                    currentUser.getEmail(), errorMessage.description));
            return errorMessage;
        }

        AuditLoggerUtil.logUserLogin(request, currentUser, Constants.LOGIN_ACTION);

        return null;
    }

    public static ApiErrorMessage performSecurityChecks(final User currentUser, final HttpServletRequest request) {
        if(currentUser.isBanned()) { // user is banned
            return SecurityErrors.USER_BANNED;
        }

        if (isAdmin(currentUser, request)) {
            return null;
        }

        /*
         * if the member is part of multiple teams the below collection contains more than one element.
         * Otherwise it will have only one element.
         * The following function will return objects of type org.digijava.module.aim.dbentity.AmpTeamMember
         * The function will return null, if the user is just a site administrator or if the user is a
         * registered user but has not yet been assigned a team
         */
        final Collection members = TeamMemberUtil.getTeamMembers(currentUser.getEmail());
        if(members == null || members.size() == 0) {
            // The user is a registered user but not a team member
            return SecurityErrors.NO_TEAM;
        }

        //Suspended login
        final List<SuspendLogin> su = UmUtil.getUserSuspendReasons (currentUser);
        if (su != null && !su.isEmpty()) {
            final List<String> suReasons = new ArrayList<>();
            for (SuspendLogin suObject : su) {
                suReasons.add(suObject.getReasonText());
            }
            
            return SecurityErrors.USER_SUSPENDED.withDetails(suReasons);
        }

        return null;
    }

    public static boolean isAdmin(User currentUser, HttpServletRequest request) {
        SiteDomain siteDomain = RequestUtils.retreiveSiteDomain(request);
        Site site = siteDomain.getSite();
        Subject subject = UserUtils.getUserSubject(currentUser);

        return DgSecurityManager.permitted(subject, site, ResourcePermission.INT_ADMIN);
    }
}
