package org.digijava.module.aim.auth;

import java.io.IOException;
import java.util.Collection;
import java.util.Iterator;

import javax.security.auth.Subject;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.digijava.kernel.exception.DgException;
import org.digijava.kernel.request.Site;
import org.digijava.kernel.request.SiteDomain;
import org.digijava.kernel.security.DgSecurityManager;
import org.digijava.kernel.security.ResourcePermission;
import org.digijava.kernel.user.User;
import org.digijava.kernel.util.RequestUtils;
import org.digijava.kernel.util.UserUtils;
import org.digijava.module.aim.dbentity.AmpApplicationSettings;
import org.digijava.module.aim.dbentity.AmpTeamMember;
import org.digijava.module.aim.helper.Constants;
import org.digijava.module.aim.util.AuditLoggerUtil;
import org.digijava.module.aim.util.DbUtil;
import org.digijava.module.aim.util.TeamMemberUtil;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

public class AmpAuthenticationProcessingFilter
    extends UsernamePasswordAuthenticationFilter {

    public Authentication attemptAuthentication(HttpServletRequest request) throws
        AuthenticationException {

        String id = request.getParameter("j_autoWorkspaceId");
        request.getSession().setAttribute("j_autoWorkspaceId", id);
        Authentication authResult = super.attemptAuthentication(request, null);
        User currentUser = null;
        try {
            currentUser = getUser(authResult);
        } catch(DgException ex) {
            throw new RuntimeException(ex);
        }
        SecurityContextHolder.getContext().setAuthentication(authResult);
        
        SiteDomain siteDomain = RequestUtils.retreiveSiteDomain(request);
        Site site = siteDomain.getSite();
        Subject subject = UserUtils.getUserSubject(currentUser);
        boolean siteAdmin = DgSecurityManager.permitted(subject, site,
            ResourcePermission.INT_ADMIN);
        /*
         * if the member is part of multiple teams the below collection contains more than one element.
         * Otherwise it will have only one element.
         * The following function will return objects of type org.digijava.module.aim.dbentity.AmpTeamMember
         * The function will return null, if the user is just a site administrator or if the user is a
         * registered user but has not yet been assigned a team
         */
        Collection members = TeamMemberUtil.getTeamMembers(currentUser.getEmail());
        if(members == null || members.size() == 0) {
            if(!siteAdmin) { // user is a site admin
                // The user is a regsitered user but not a team member
                throw new NotTeamMemberException(currentUser.getEmail());
            }
        }
        
//        /*
//         * Checking user Activity settings
//         */
//        Iterator itr = members.iterator();
//        while (itr.hasNext()){
//          AmpTeamMember member = (AmpTeamMember) itr.next();
//          AmpApplicationSettings ampAppSettings = DbUtil.getMemberAppSettings(member.getAmpTeamMemId());
//            if (ampAppSettings == null)   //if the user hasn't got the personalized settings
//              throw new InvalidUserException(currentUser.getEmail());
//        }
        AuditLoggerUtil.logUserLogin(request,currentUser, Constants.LOGIN_ACTION);
        return authResult;
    }


    protected void onSuccessfulAuthentication(HttpServletRequest request,
                                              HttpServletResponse response,
                                              Authentication authResult) throws
        IOException {
        User currentUser = null;
        try {
            currentUser = getUser(authResult);
        } catch(DgException ex) {
            throw new RuntimeException(ex);
        }

        if(currentUser == null) {
            return;
        }

        SiteDomain siteDomain = RequestUtils.retreiveSiteDomain(request);
        Site site = siteDomain.getSite();

        HttpSession session = request.getSession();
        cleanupSession(session);
        /*
        try {
            initializeTeamMembership(request, response, currentUser,
                                     site);
        } catch(DgException ex1) {
            throw new RuntimeException(ex1);
        }
*/
    }

    protected void cleanupSession(HttpSession session) {
        // clear the session variables
        if(session.getAttribute("currentMember") != null) {
            session.removeAttribute("currentMember");
        }
        if(session.getAttribute("teamLeadFlag") != null) {
            session.removeAttribute("teamLeadFlag");
        }
        if(session.getAttribute("ampAdmin") != null) {
            session.removeAttribute("ampAdmin");
        }
        if(session.getAttribute("publicuser") != null) {
            session.removeAttribute("publicuser");
        }
    }

    protected User getUser(Authentication currentAuth) throws DgException {
        if(currentAuth == null) {
            return null;
        }

        if(currentAuth.getPrincipal() == null) {
            return null;
        }

        User user;
        Object principal = currentAuth.getPrincipal();
        if(principal instanceof Long) {
            Long userId = (Long) principal;
            user = UserUtils.getUser(userId);
        } else {
            String userName;
            if(principal instanceof UserDetails) {
                UserDetails userDetails = (UserDetails) principal;
                userName = userDetails.getUsername();
            } else {
                userName = principal.toString();
            }
            user = UserUtils.getUserByEmailAddress(userName);
        }

        return user;
    }
}
