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
import org.dgfoundation.amp.ar.ArConstants;
import org.dgfoundation.amp.ar.ReportContextData;
import org.digijava.kernel.request.Site;
import org.digijava.kernel.security.DgSecurityManager;
import org.digijava.kernel.security.ResourcePermission;
import org.digijava.kernel.user.User;
import org.digijava.kernel.util.RequestUtils;
import org.digijava.kernel.util.UserUtils;
import org.digijava.module.aim.dbentity.AmpApplicationSettings;
import org.digijava.module.aim.dbentity.AmpTeam;
import org.digijava.module.aim.dbentity.AmpTeamMember;
import org.digijava.module.aim.dbentity.AmpTeamMemberRoles;
import org.digijava.module.aim.form.LoginForm;
import org.digijava.module.aim.helper.ApplicationSettings;
import org.digijava.module.aim.helper.Constants;
import org.digijava.module.aim.helper.TeamMember;
import org.digijava.module.aim.util.DbUtil;
import org.digijava.module.aim.util.TeamMemberUtil;
import org.digijava.module.aim.util.caching.AmpCaching;
import org.digijava.module.gateperm.core.GatePermConst;
import org.digijava.module.gateperm.util.PermissionUtil;

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
            	session.invalidate();
            	return mapping.findForward("forward");
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

           



            AmpTeamMemberRoles lead = TeamMemberUtil
                    .getAmpTeamHeadRole();

            TeamMember tm = new TeamMember();

            if (lead != null) {
                if (lead.getAmpTeamMemRoleId().equals(
                        member.getAmpMemberRole().getAmpTeamMemRoleId()) ||
                        //very ugly but we have no choice - only one team head role possible :(
                        member.getAmpMemberRole().getRole().equals("Top Management")
                        ) {
                    session.setAttribute("teamLeadFlag", new String("true"));
                    tm.setTeamHead(true);
                } else {
                    session.setAttribute("teamLeadFlag", new String("false"));
                    tm.setTeamHead(false);
                }
            } else {
                session.setAttribute("teamLeadFlag", new String("false"));
                tm.setTeamHead(false);
            }

            AmpApplicationSettings ampAppSettings = DbUtil
                    .getMemberAppSettings(member.getAmpTeamMemId());
            ApplicationSettings appSettings = new ApplicationSettings();
            appSettings.setAppSettingsId(ampAppSettings.getAmpAppSettingsId());
            appSettings.setDefRecsPerPage(ampAppSettings.getDefaultRecordsPerPage());
            appSettings.setDefReportsPerPage((ampAppSettings.getDefaultReportsPerPage()==null)?0:ampAppSettings.getDefaultReportsPerPage());
            appSettings.setCurrencyId(ampAppSettings.getCurrency()
                    .getAmpCurrencyId());
            if (ampAppSettings.getFiscalCalendar() == null) {
                logger.info("AmpAppSettings.getFisCal is null");
            } else {
                logger.info("AmpAppSettings.getFisCal is not null");
                if (ampAppSettings.getFiscalCalendar().getAmpFiscalCalId() == null) {
                    logger.info("AmpAppSettings.getFisCal.id is null");
                } else {
                    logger.info("AmpAppSettings.getFisCal.id is not null");
                }
            }
            appSettings.setFisCalId(ampAppSettings.getFiscalCalendar()
                    .getAmpFiscalCalId());
            appSettings.setLanguage(ampAppSettings.getLanguage());
            appSettings.setDefaultAmpReport(ampAppSettings.getDefaultTeamReport());
            appSettings.setValidation(ampAppSettings.getValidation());

            tm.setMemberId(member.getAmpTeamMemId());
            tm.setMemberName(member.getUser().getName());
            tm.setRoleId(member.getAmpMemberRole().getAmpTeamMemRoleId());
            tm.setRoleName(member.getAmpMemberRole().getRole());
            tm.setTeamId(member.getAmpTeam().getAmpTeamId());
            session.setAttribute(Constants.TEAM_ID, tm.getTeamId());
            tm.setTeamName(member.getAmpTeam().getName());
            tm.setTeamType(member.getAmpTeam().getTeamCategory());
            tm.setAppSettings(appSettings);
            tm.setEmail(member.getUser().getEmail());
            tm.setTeamAccessType(member.getAmpTeam().getAccessType());
            tm.setComputation(member.getAmpTeam().getComputation());
            tm.setUseFilters(member.getAmpTeam().getUseFilter());
            tm.setAddActivity(member.getAmpTeam().getAddActivity());
            tm.setPledger(member.getUser().getPledger());
            tm.setPublishDocuments(member.getPublishDocPermission());
            tm.setApprover(member.getAmpMemberRole().isApprover());
			
            if (DbUtil.isUserTranslator(member.getUser().getId()) == true) {
                tm.setTranslator(true);
            } else {
                tm.setTranslator(false);
            }
            
            session.setAttribute(Constants.CURRENT_MEMBER, tm);
            AmpTeam.initializeTeamFiltersSession(member, request, session);
            PermissionUtil.putInScope(session, GatePermConst.ScopeKeys.CURRENT_MEMBER, tm);
            session.setMaxInactiveInterval(-1);
            lForm.setLogin(true);

            //AMP-4256 - Removing all settings that might come from the other workspace
            session.removeAttribute(Constants.CURRENT_TAB_REPORT);
            session.removeAttribute(Constants.DEFAULT_TEAM_REPORT);
			session.removeAttribute(Constants.MY_REPORTS);
			session.removeAttribute(Constants.MY_ACTIVE_TABS);
			session.removeAttribute(Constants.TEAM_ID);
            session.removeAttribute(Constants.MY_REPORTS_PER_PAGE);
            session.removeAttribute(Constants.LAST_VIEWED_REPORTS);
            
        } catch (Exception e) {
            e.printStackTrace(System.out);
        }

        return mapping.findForward("forward");
    }
}
