package org.digijava.module.aim.action;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;

import javax.security.auth.Subject;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.dgfoundation.amp.visibility.AmpTreeVisibility;
import org.digijava.kernel.entity.Locale;
import org.digijava.kernel.exception.DgException;
import org.digijava.kernel.request.Site;
import org.digijava.kernel.security.DgSecurityManager;
import org.digijava.kernel.security.ResourcePermission;
import org.digijava.kernel.translator.TranslatorWorker;
import org.digijava.kernel.user.User;
import org.digijava.kernel.util.DgUtil;
import org.digijava.kernel.util.RequestUtils;
import org.digijava.kernel.util.UserUtils;
import org.digijava.module.aim.auth.NotTeamMemberException;
import org.digijava.module.aim.dbentity.AmpApplicationSettings;
import org.digijava.module.aim.dbentity.AmpTeam;
import org.digijava.module.aim.dbentity.AmpTeamMember;
import org.digijava.module.aim.dbentity.AmpTemplatesVisibility;
import org.digijava.module.aim.form.LoginForm;
import org.digijava.module.aim.helper.ApplicationSettings;
import org.digijava.module.aim.helper.Constants;
import org.digijava.module.aim.helper.TeamMember;
import org.digijava.module.aim.util.DbUtil;
import org.digijava.module.aim.util.FeaturesUtil;
import org.digijava.module.aim.util.TeamMemberUtil;
import org.digijava.module.categorymanager.dbentity.AmpCategoryValue;
import org.digijava.module.categorymanager.util.CategoryConstants;
import org.digijava.module.categorymanager.util.CategoryManagerUtil;
import org.digijava.module.gateperm.core.GatePermConst;
import org.digijava.module.gateperm.util.PermissionUtil;
import org.digijava.module.visualization.dbentity.AmpDashboard;
import org.digijava.module.visualization.util.DashboardUtil;

/**
 * Shows the index page
 * <p/>
 * A user cannot login to AMP if he has already logged in, using the same session.
 * If the user is logged in and then if he tries to access the index page or login page
 * he will be automatically redirected to the MyDesktop page
 */
public class ViewAmp
        extends Action {
    public ActionForward execute(ActionMapping mapping, ActionForm form,
                                 HttpServletRequest request,
                                 HttpServletResponse response) throws java.lang.
            Exception {
    	
    	String workspaceId = (String) request.getSession().getAttribute("j_autoWorkspaceId");
        User user = RequestUtils.getUser(request);
        Site site = RequestUtils.getSite(request);
        
        initializeTeamMembership(request, response, user,
                site);
        /*
         * If the user information is present in the current session, then forward to the
         * MyDesktop page, when he tries to login again in that same session.
         */
        HttpSession session = request.getSession();
        session.setAttribute("isUserLogged",
                new String("true"));
        
        String siteAdmin = (String) session.getAttribute("ampAdmin");
        TeamMember tm = (TeamMember) session.getAttribute("currentMember");
        
        Collection members = TeamMemberUtil.getTeamMembers(user.getEmail());
        session.setAttribute(Constants.USER_WORKSPACES, members);
        
       
        Collection<AmpDashboard> dashboards = org.digijava.module.visualization.util.DbUtil.getDashboardsToShowInMenu();
        
        session.setAttribute(Constants.MENU_DASHBOARDS, DashboardUtil.generateIdToNameForDashboards(dashboards));
        
        if (tm != null && tm.getTeamId() != null &&
        	tm.getTeamId().longValue() > 0) {
            String fwdUrl = "showDesktop.do";
            response.sendRedirect(fwdUrl);
            return null;
        } else if (siteAdmin != null && "yes".equals(siteAdmin)) {
            String fwdUrl = "/aim/admin.do";
            response.sendRedirect(fwdUrl);
            return null;
        } else if(workspaceId != null){
        	response.sendRedirect("selectTeam.do");
        	return null;
        }




        // No menber info means that we could not set it automatically
        LoginForm lForm = (LoginForm) form; // login form instance
       
        session.setAttribute("currentUser", user);
		lForm.setMembers(members);

        Collection<AmpCategoryValue> workspaceGroups = CategoryManagerUtil.getAmpCategoryValueCollectionByKey(CategoryConstants.WORKSPACE_GROUP_KEY);
        lForm.setWorkspaceGroups(workspaceGroups);

       
        //response.sendRedirect("showSelectTeam.do");



        return mapping.findForward("selectTeam");

        //throw new AuthenticationException("Please log in") {
        //};
        //return null;
    }

    public static void initializeTeamMembership(HttpServletRequest request,
                                                HttpServletResponse response,
                                                User usr, Site site) throws
            NotTeamMemberException, DgException {

        HttpSession session = request.getSession();
        if (session.getAttribute(Constants.TEAM_ID) != null) {
            session.removeAttribute(Constants.TEAM_ID);
        }

        ServletContext ampContext = session.getServletContext();

        Subject subject = RequestUtils.getSubject(request);
        if (subject == null) {
            subject = UserUtils.getUserSubject(usr);
        }
        boolean siteAdmin = DgSecurityManager.permitted(subject, site,
                ResourcePermission.INT_ADMIN);

        /*
         * if the member is part of multiple teams the below collection contains more than one element.
         * Otherwise it will have only one element.
         * The following function will return objects of type org.digijava.module.aim.dbentity.AmpTeamMember
         * The function will return null, if the user is just a site administrator or if the user is a
         * registered user but has not yet been assigned a team
         */
        Collection members = TeamMemberUtil.getTeamMembers(usr.getEmail());
        if (members == null || members.size() == 0) {
			//
			if (siteAdmin == true) { // user is a site admin
                // set the session variable 'ampAdmin' to the value 'yes'
                session.setAttribute("ampAdmin", new String("yes"));
                // create a TeamMember object and set it to a session variabe 'currentMember'
                TeamMember tm = new TeamMember(usr);
                tm.setTeamName(TranslatorWorker.translateText("AMP Administrator"));
                session.setAttribute("currentMember", tm);
                PermissionUtil.putInScope(session, GatePermConst.ScopeKeys.CURRENT_MEMBER, tm);
                // show the index page with the admin toolbar at the bottom

                return;
            } else {
                // The user is a regsitered user but not a team member
                // this statement is not reachable, according to login logic
                // AmpAuthenticationProcessingFilter handles it
                throw new NotTeamMemberException(usr.getEmail());
            }
        } else {
            if (members.size() == 1) {
                if (siteAdmin == true) {
                    session.setAttribute("ampAdmin", new String("yes"));
                } else {
                    session.setAttribute("ampAdmin", new String("no"));
                }
            }
        }
        if (members.size() == 1) {
            // if the user is part of just on team, load his personalized settings

            Iterator itr = members.iterator();
            AmpTeamMember member = (AmpTeamMember) itr.next();

            synchronized (ampContext) {
                HashMap<Long, Long> userActList = (HashMap<Long, Long>) ampContext.getAttribute(Constants.USER_ACT_LIST);
                if (userActList != null &&
                        userActList.containsKey(member.getAmpTeamMemId())) {
                    // expire all other entries

                    //logger.info("getting the value for " + member.getAmpTeamMemId());

                    Long actId = userActList.get(member.getAmpTeamMemId());
                    HashMap<String, Long> editActMap = (HashMap<String, Long>) ampContext.getAttribute(Constants.EDIT_ACT_LIST);
                    String sessId = null;
                    if (editActMap != null) {
                        Iterator<String> itr1 = editActMap.keySet().iterator();
                        while (itr1.hasNext()) {
                        	sessId = itr1.next();
                            Long tempActId = (Long) editActMap.get(sessId);

                            //logger.info("tempActId = " + tempActId + " actId = " + actId);
                            if (tempActId.longValue() == actId.longValue()) {
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
                    if (tsActList != null) {
                        tsActList.remove(actId);
                        ampContext.setAttribute(Constants.TS_ACT_LIST,
                                tsActList);
                    }
                    ArrayList sessList = (ArrayList) ampContext.getAttribute(
                            Constants.SESSION_LIST);
                    if (sessList != null) {
                        sessList.remove(sessId);
                        Collections.sort(sessList);
                        ampContext.setAttribute(Constants.SESSION_LIST,
                                sessList);
                    }
                }
            }

            TeamMember tm = new TeamMember(member);

			//now teamHead is configured within TeamMember constructor, but leaving this special case here
			//is it still needed? if yes, then should be moved within TeamMemberUtil.isHeadRole()
            if (
                        //very ugly but we have no choice - only one team head role possible :(
                        member.getAmpMemberRole().getRole().equals("Top Management")
                        ) {
                    tm.setTeamHead(true);
                }
            session.setAttribute("teamLeadFlag", String.valueOf(tm.getTeamHead()));
            
            // Get the team members application settings
            AmpApplicationSettings ampAppSettings = DbUtil.getTeamAppSettings(member.getAmpTeam().getAmpTeamId());
            ApplicationSettings appSettings = new ApplicationSettings(ampAppSettings);
            //appSettings.setLanguage(ampAppSettings.getLanguage());

            //as the users is member of only one team we mas ensure that the correct
            //template settings are load for him
            AmpTeam currentTeam = member.getAmpTeam();
            AmpTemplatesVisibility currentTemplate = currentTeam.getFmTemplate();
            if (currentTemplate == null) {
                currentTemplate = FeaturesUtil.getDefaultAmpTemplateVisibility();
            }
            AmpTreeVisibility ampTreeVisibility = new AmpTreeVisibility();
            ampTreeVisibility.buildAmpTreeVisibility(currentTemplate);
            FeaturesUtil.setAmpTreeVisibility(request.getServletContext(), session,ampTreeVisibility);
            
            session.setAttribute(Constants.TEAM_ID, tm.getTeamId());
            tm.setAppSettings(appSettings);
            session.setAttribute(Constants.CURRENT_MEMBER, tm);            
            AmpTeam.initializeTeamFiltersSession(member, request, session);
			
            // Set the session infinite. i.e. session never timeouts
            session.setMaxInactiveInterval(-1);

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
        } else if (members.size() > 1) {
            // member is part of more than one team. Show the select team page
            // Moved to ViewAmp action
            return;
        }
        return;
    }

}
