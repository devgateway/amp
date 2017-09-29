package org.digijava.kernel.ampapi.endpoints.security;

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

    public static ApiErrorMessage login(final User currentUser, final HttpServletRequest request) {
        final SiteDomain siteDomain = RequestUtils.retreiveSiteDomain(request);
        final Site site = siteDomain.getSite();
        final Subject subject = UserUtils.getUserSubject(currentUser);
        final boolean siteAdmin = DgSecurityManager.permitted(subject, site,
                ResourcePermission.INT_ADMIN);

        if(currentUser.isBanned()) { // user is banned
            SecurityContextHolder.getContext().setAuthentication(null);
            return SecurityErrors.USER_BANNED;
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
            if(!siteAdmin) { // user is a site Admin
                // The user is a registered user but not a team member
                SecurityContextHolder.getContext().setAuthentication(null);
                return SecurityErrors.NO_TEAM;
            }
        }

        //Suspended login
        final List<SuspendLogin> su = UmUtil.getUserSuspendReasons (currentUser);
        if (su != null && !su.isEmpty()) {
            if(!siteAdmin) {
                final List<String> suReasons = new ArrayList<>();
                for (SuspendLogin suObject : su) {
                    suReasons.add(suObject.getReasonText());
                }
                SecurityContextHolder.getContext().setAuthentication(null);
                return new ApiErrorMessage(10, suReasons.toString());
            }
        }


        AuditLoggerUtil.logUserLogin(request, currentUser, Constants.LOGIN_ACTION);
        return null;
    }
}
