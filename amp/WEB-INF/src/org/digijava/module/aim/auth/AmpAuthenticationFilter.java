package org.digijava.module.aim.auth;

import org.digijava.kernel.entity.Locale;
import org.digijava.kernel.exception.DgException;
import org.digijava.kernel.request.Site;
import org.digijava.kernel.request.SiteDomain;
import org.digijava.kernel.security.DgSecurityManager;
import org.digijava.kernel.security.ResourcePermission;
import org.digijava.kernel.user.User;
import org.digijava.kernel.util.DgUtil;
import org.digijava.kernel.util.RequestUtils;
import org.digijava.kernel.util.UserUtils;
import org.digijava.module.aim.dbentity.AmpTeamMember;
import org.digijava.module.aim.helper.Constants;
import org.digijava.module.aim.helper.GlobalSettingsConstants;
import org.digijava.module.aim.helper.TeamMember;
import org.digijava.module.aim.util.FeaturesUtil;
import org.digijava.module.aim.util.TeamMemberUtil;
import org.digijava.module.aim.util.TeamUtil;
import org.digijava.module.gateperm.core.GatePermConst;
import org.digijava.module.gateperm.util.PermissionUtil;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.util.Assert;

import javax.security.auth.Subject;
import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.*;

public class AmpAuthenticationFilter
    implements Filter, InitializingBean {

    public static final String FILTER_APPLIED=AmpAuthenticationFilter.class.getName() + "__APPLIED__";

    private String forwardSuccess = "/amp/index.do";
    private String forwardTeamSelect = "/amp/selectTeam.do";

    public void afterPropertiesSet() throws Exception {
        Assert.hasText(forwardSuccess);
        Assert.hasText(forwardTeamSelect);
    }

    /**
     * destroy
     *
     * @todo Implement this javax.servlet.Filter method
     */
    public void destroy() {
    }

    /**
     * doFilter
     *
     * @param servletRequest ServletRequest
     * @param servletResponse ServletResponse
     * @param filterChain FilterChain
     * @throws IOException
     * @throws ServletException
     * @todo Implement this javax.servlet.Filter method
     */
    public void doFilter(ServletRequest servletRequest,
                         ServletResponse servletResponse,
                         FilterChain filterChain) throws IOException,
        ServletException {

        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;

        HttpSession session = request.getSession();
        session.invalidate();
        

        Boolean filterApplied = (Boolean)session.getAttribute(FILTER_APPLIED);
        if (filterApplied != null && filterApplied.equals(Boolean.TRUE)) {
            filterChain.doFilter(servletRequest, servletResponse);
        }

        session.setAttribute(FILTER_APPLIED, Boolean.TRUE);

        User currentUser = null;
        try {
            currentUser = getUser(request);
        } catch(DgException ex) {
            throw new ServletException("Error getting user", ex);
        }
        if(currentUser == null) {
            filterChain.doFilter(servletRequest, servletResponse);
            return;
        }

        SiteDomain siteDomain = RequestUtils.retreiveSiteDomain(request);
        Site site = siteDomain.getSite();

        cleanupSession(session);

        String forwardTo = null;
        try {
            forwardTo = initializeTeamMembership(request, response, currentUser,
                                                 site);
        } catch(DgException ex1) {
            throw new ServletException(ex1);
        }

        if (forwardTo == null) {
            filterChain.doFilter(servletRequest, servletResponse);
        } else {
            RequestDispatcher rd = request.getRequestDispatcher(forwardTo);
            rd.forward(request, response);
        }
    }

    /**
     * init
     *
     * @param filterConfig FilterConfig
     * @throws ServletException
     * @todo Implement this javax.servlet.Filter method
     */
    public void init(FilterConfig filterConfig) throws ServletException {
    }

    protected void forwardToHome(HttpServletRequest request,
                                 HttpServletResponse response) throws
        IOException, ServletException {
        RequestDispatcher rd = request.getRequestDispatcher(forwardSuccess);
        rd.forward(request, response);
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
    }

    protected User getUser(HttpServletRequest request) throws DgException {
        SecurityContext securityContext = SecurityContextHolder.getContext();
        if(securityContext == null) {
            throw new RuntimeException(
                "No SecurityContext is available. Please configure the system properly");
        }

        Authentication currentAuth = securityContext.getAuthentication();
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

    protected String initializeTeamMembership(HttpServletRequest request,
                                              HttpServletResponse response,
                                              User usr, Site site) throws
        NotTeamMemberException, DgException {
        HttpSession session = request.getSession();

        ServletContext ampContext = session.getServletContext();

        Subject subject = UserUtils.getUserSubject(usr);
        boolean siteAdmin = DgSecurityManager.permitted(subject, site,
            ResourcePermission.INT_ADMIN);

        /*
         * if the member is part of multiple teams the below collection contains more than one element.
         * Otherwise it will have only one element.
         * The following function will return objects of type org.digijava.module.aim.dbentity.AmpTeamMember
         * The function will return null, if the user is just a site administrator or if the user is a
         * registered user but has not yet been assigned a team
         */
        Collection members = TeamMemberUtil.getTeamMembers(usr.getId());
        if(members == null || members.size() == 0) {
            if(siteAdmin == true) { // user is a site admin
                // set the session variable 'ampAdmin' to the value 'yes'
                session.setAttribute("ampAdmin", new String("yes"));
                // create a TeamMember object and set it to a session variabe 'currentMember'
                TeamMember tm = new TeamMember(usr);
                tm.setTeamName("AMP Administrator");
                session.setAttribute("currentMember", tm);
                PermissionUtil.putInScope(session, GatePermConst.ScopeKeys.CURRENT_MEMBER, tm);
                // show the index page with the admin toolbar at the bottom

                return forwardSuccess;
            } else {
                // The user is a regsitered user but not a team member
                throw new NotTeamMemberException(usr.getEmail());
            }
        } else {
            if(siteAdmin == true) {
                session.setAttribute("ampAdmin", new String("yes"));
            } else {
                session.setAttribute("ampAdmin", new String("no"));
            }
        }
        if(members.size() == 1) {
            // if the user is part of just on team, load his personalized settings

            Iterator itr = members.iterator();
            AmpTeamMember member = (AmpTeamMember) itr.next();

            synchronized(ampContext) {
                HashMap userActList = (HashMap) ampContext.getAttribute(
                    Constants.USER_ACT_LIST);
                if(userActList != null &&
                   userActList.containsKey(member.getAmpTeamMemId())) {
                    // expire all other entries

                    //logger.info("getting the value for " + member.getAmpTeamMemId());

                    Long actId = (Long) userActList.get(member.getAmpTeamMemId());
                    HashMap editActMap = (HashMap) ampContext.getAttribute(
                        Constants.EDIT_ACT_LIST);
                    String sessId = null;
                    if(editActMap != null) {
                        Iterator itr1 = editActMap.keySet().iterator();
                        while(itr1.hasNext()) {
                            sessId = (String) itr1.next();
                            Long tempActId = (Long) editActMap.get(sessId);

                            //logger.info("tempActId = " + tempActId + " actId = " + actId);
                            if(tempActId.longValue() == actId.longValue()) {
                                editActMap.remove(sessId);
                                //logger.info("Removed the entry for " + actId);
                                ampContext.setAttribute(Constants.EDIT_ACT_LIST,
                                    editActMap);
                                break;
                            }
                        }
                    }
                    userActList.remove(member.getAmpTeamMemId());
                    ampContext.setAttribute(Constants.USER_ACT_LIST,
                                            userActList);

                    HashMap tsActList = (HashMap) ampContext.getAttribute(
                        Constants.TS_ACT_LIST);
                    if(tsActList != null) {
                        tsActList.remove(actId);
                        ampContext.setAttribute(Constants.TS_ACT_LIST,
                                                tsActList);
                    }
                    ArrayList sessList = (ArrayList) ampContext.getAttribute(
                        Constants.SESSION_LIST);
                    if(sessList != null) {
                        sessList.remove(sessId);
                        Collections.sort(sessList);
                        ampContext.setAttribute(Constants.SESSION_LIST,
                                                sessList);
                    }
                }
            }

            TeamMember tm = TeamUtil.setupFiltersForLoggedInUser(request, member);
            session.setMaxInactiveInterval(FeaturesUtil.getGlobalSettingValueLong(GlobalSettingsConstants.MAX_INACTIVE_SESSION_INTERVAL).intValue());

            // forward to members desktop page

            // Users language should be selected for all his pages
            /*
             * We use translation module in the digijava framework to switch the language. Members
             * language is passed as a parameter to the url '/translation/switchLanguage.do'
             * After switching the language, '/switchLanguage.do' will redirect the request to the url specified
             * in the paramater 'rfr'
             */
            Locale navLocale = new Locale();
            navLocale.setCode(tm.getAppSettings().getLanguage());
            DgUtil.switchLanguage(navLocale, request, response);

            return forwardSuccess;

        } else if(members.size() > 1) {
            // member is part of more than one team. Show the select team page
            return forwardTeamSelect;
        }
        return null;
    }

    public String getForwardSuccess() {
        return forwardSuccess;
    }

    public String getForwardTeamSelect() {
        return forwardTeamSelect;
    }

    public void setForwardSuccess(String forwardSuccess) {
        this.forwardSuccess = forwardSuccess;
    }

    public void setForwardTeamSelect(String forwardTeamSelect) {
        this.forwardTeamSelect = forwardTeamSelect;
    }

}
