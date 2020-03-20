package org.digijava.module.aim.action;

import javax.security.auth.Subject;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.dgfoundation.amp.ar.ReportContextData;
import org.dgfoundation.amp.visibility.AmpTreeVisibility;
import org.digijava.kernel.request.Site;
import org.digijava.kernel.security.DgSecurityManager;
import org.digijava.kernel.security.ResourcePermission;
import org.digijava.kernel.user.User;
import org.digijava.kernel.util.RequestUtils;
import org.digijava.kernel.util.UserUtils;
import org.digijava.module.aim.dbentity.AmpTeam;
import org.digijava.module.aim.dbentity.AmpTeamMember;
import org.digijava.module.aim.dbentity.AmpTemplatesVisibility;
import org.digijava.module.aim.form.LoginForm;
import org.digijava.module.aim.helper.Constants;
import org.digijava.module.aim.helper.TeamMember;
import org.digijava.module.aim.util.FeaturesUtil;
import org.digijava.module.aim.util.TeamMemberUtil;
import org.digijava.module.aim.util.TeamUtil;
import org.digijava.module.aim.util.caching.AmpCaching;
import org.digijava.module.gateperm.core.GatePermConst;
import org.digijava.module.gateperm.util.PermissionUtil;
import org.springframework.security.web.authentication.logout.SimpleUrlLogoutSuccessHandler;

public class SelectTeam extends Action {

    private static Logger logger = Logger.getLogger(SelectTeam.class);

    public ActionForward execute(ActionMapping mapping, ActionForm form,
                                 HttpServletRequest request, HttpServletResponse response)
            throws java.lang.Exception {

        HttpSession session = request.getSession();
        
        //Removing attributes from previous session (East Timor. Aug.2011)
        ReportContextData.clearSession();
        AmpCaching.clearInstance();

        LoginForm lForm = (LoginForm) form;
        
        //This is for the auto login.
        String temp = "";
        String workspaceId = (String) request.getSession().getAttribute("j_autoWorkspaceId");
        request.getSession().removeAttribute("j_autoWorkspaceId");
        if(workspaceId != null){
            temp = workspaceId;
        } else {
            temp = request.getParameter("id");
        }

        try {
            User user = RequestUtils.getUser(request);
            Long id = new Long(Long.parseLong(temp));
            AmpTeamMember member = TeamMemberUtil.getAmpTeamMember(id);
            
            //AMP Security Issues - AMP-12638
            if (member == null || member.getUser().getId() != user.getId()){
                //session.invalidate();
                throw new RuntimeException("Access denied for url: " + request.getRequestURL());
                //return mapping.findForward("forward");
            }

            // ----------------------
            Site site = RequestUtils.getSite(request);
            Subject subject = RequestUtils.getSubject(request);
            if (subject == null) {
                subject = UserUtils.getUserSubject(user);
            }
            boolean badmin = DgSecurityManager.permitted(subject, site,
                    ResourcePermission.INT_ADMIN);
            if (badmin == true) {
                session.setAttribute("ampAdmin", new String("yes"));
            } else {
                session.setAttribute("ampAdmin", new String("no"));
            }
            // -----------------------------

            TeamMember tm = TeamUtil.setupFiltersForLoggedInUser(request, member);
            PermissionUtil.putInScope(session, GatePermConst.ScopeKeys.CURRENT_MEMBER, tm);
            lForm.setLogin(true);

            //AMP-4256 - Removing all settings that might come from the other workspace
            session.removeAttribute(Constants.CURRENT_TAB_REPORT);
            session.removeAttribute(Constants.DEFAULT_TEAM_REPORT);
            session.removeAttribute(Constants.MY_REPORTS);
            session.removeAttribute(Constants.MY_ACTIVE_TABS);
            session.removeAttribute(Constants.TEAM_ID);
            session.removeAttribute(Constants.MY_REPORTS_PER_PAGE);
            session.removeAttribute(Constants.LAST_VIEWED_REPORTS);
            session.removeAttribute(Constants.UNASSIGNED_ACTIVITY_LIST);

            //See if current workspace has a FM Template attached to it
            AmpTeam currentTeam = member.getAmpTeam();
            AmpTemplatesVisibility currentTemplate = currentTeam.getFmTemplate();
            if (currentTemplate == null) {
                currentTemplate = FeaturesUtil.getDefaultAmpTemplateVisibility();
            } else {
                currentTemplate = FeaturesUtil.getTemplateById(currentTemplate.getId());

            }
            AmpTreeVisibility ampTreeVisibility = new AmpTreeVisibility();
            ampTreeVisibility.buildAmpTreeVisibility(currentTemplate);
            FeaturesUtil.setAmpTreeVisibility(request.getServletContext(), session,ampTreeVisibility);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        return mapping.findForward("forward");
    }
}
