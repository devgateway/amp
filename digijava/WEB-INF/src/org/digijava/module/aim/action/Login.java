package org.digijava.module.aim.action;

import java.util.Collection;
import java.util.Iterator;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.digijava.kernel.entity.UserLangPreferences;
import org.digijava.kernel.request.Site;
import org.digijava.kernel.request.SiteDomain;
import org.digijava.kernel.security.HttpLoginManager;
import org.digijava.kernel.user.User;
import org.digijava.kernel.util.DgUtil;
import org.digijava.kernel.util.RequestUtils;
import org.digijava.kernel.util.SiteUtils;
import org.digijava.kernel.util.UserUtils;
import org.digijava.module.aim.dbentity.AmpApplicationSettings;
import org.digijava.module.aim.dbentity.AmpTeamMember;
import org.digijava.module.aim.dbentity.AmpTeamMemberRoles;
import org.digijava.module.aim.form.LoginForm;
import org.digijava.module.aim.helper.ApplicationSettings;
import org.digijava.module.aim.helper.TeamMember;
import org.digijava.module.aim.util.DbUtil;
import org.digijava.module.aim.util.TeamUtil;

public class Login extends Action {

	private static Logger logger = Logger.getLogger(Login.class);

	public ActionForward execute(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws java.lang.Exception {

		LoginForm lForm = (LoginForm) form;
		logger.debug("login");
		ActionErrors errors = new ActionErrors();
		HttpSession session = request.getSession();

		String sessionId = null;

		try {

			if (lForm.getUserId() != null && lForm.getPassword() != null) {

				sessionId = HttpLoginManager.loginByCredentials(request,
						response, lForm.getUserId().toLowerCase(), lForm
								.getPassword(), false);

				if (sessionId != null) {

					HttpLoginManager.LoginInfo loginInfo = HttpLoginManager
							.loginBySessionId(request, response, sessionId,
									false);

					if (loginInfo.getLoginResult() == HttpLoginManager.LOGIN_RESULT_INVALID) {
						// invalid login
						lForm.setLogin(false);
						errors.add(ActionErrors.GLOBAL_ERROR, new ActionError(
								"error.aim.invalidLogin"));
						saveErrors(request, errors);
						return mapping.getInputForward();
					} else if (loginInfo.getLoginResult() == HttpLoginManager.LOGIN_RESULT_BANNED) {
						// user banned
						lForm.setLogin(false);
						errors.add(ActionErrors.GLOBAL_ERROR, new ActionError(
								"error.aim.userBanned"));
						saveErrors(request, errors);
						return mapping.getInputForward();
					} else if (loginInfo.getLoginResult() == HttpLoginManager.LOGIN_RESULT_OK) {
						// valid user.
						if (session.getAttribute("currentMember") != null) {
							session.removeAttribute("currentMember");
						}
						if (session.getAttribute("teamLeadFlag") != null) {
							session.removeAttribute("teamLeadFlag");
						}
						if (session.getAttribute("ampAdmin") != null) {
							session.removeAttribute("ampAdmin");
						}
					} else {
						// login again
						lForm.setLogin(false);
						errors.add(ActionErrors.GLOBAL_ERROR, new ActionError(
								"error.aim.loginFailed"));
						saveErrors(request, errors);
						return mapping.getInputForward();
					}
				} else {
					// login again
					lForm.setLogin(false);
					errors.add(ActionErrors.GLOBAL_ERROR, new ActionError(
							"error.aim.loginFailed"));
					saveErrors(request, errors);
					return mapping.getInputForward();
				}
				
				User usr = DbUtil.getUser(lForm.getUserId());
				boolean siteAdmin = DgUtil.isSiteAdministrator(request);

				Collection members = TeamUtil.getTeamMembers(lForm.getUserId());
				if (members == null || members.size() == 0) {
					if (siteAdmin == true) {
						session.setAttribute("ampAdmin", new String("yes"));
						TeamMember tm = new TeamMember();

						tm.setMemberName(usr.getName());
						tm.setMemberId(usr.getId());
						tm.setTeamName("AMP Administrator");
						session.setAttribute("currentMember", tm);
						return mapping.findForward("index");
					} else {
						lForm.setLogin(false);
						errors.add(ActionErrors.GLOBAL_ERROR,
								new ActionError(
										"error.aim.userNotTeamMember"));
						saveErrors(request, errors);
						return mapping.getInputForward();
					}
				} else {
					if (siteAdmin == true) {
						session.setAttribute("ampAdmin", new String("yes"));
					} else {
						session.setAttribute("ampAdmin", new String("no"));
					}
				}
				if (members.size() == 1) {
					Iterator itr = members.iterator();
					AmpTeamMember member = (AmpTeamMember) itr.next();

					AmpTeamMemberRoles lead = org.digijava.module.aim.util.DbUtil
							.getAmpTeamHeadRole();

					TeamMember tm = new TeamMember();

					if (lead != null) {
						if (lead.getAmpTeamMemRoleId().equals(
										member.getAmpMemberRole().getAmpTeamMemRoleId())) {
							session.setAttribute("teamLeadFlag", new String(
									"true"));
							tm.setTeamHead(true);
						} else {
							session.setAttribute("teamLeadFlag", new String(
									"false"));
							tm.setTeamHead(false);
						}
					} else {
						session.setAttribute("teamLeadFlag",
								new String("false"));
						tm.setTeamHead(false);
					}

					AmpApplicationSettings ampAppSettings = DbUtil
							.getMemberAppSettings(member.getAmpTeamMemId());
					ApplicationSettings appSettings = new ApplicationSettings();
					appSettings.setAppSettingsId(ampAppSettings
							.getAmpAppSettingsId());
					appSettings.setDefRecsPerPage(ampAppSettings
							.getDefaultRecordsPerPage().intValue());
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
					appSettings.setPerspective(ampAppSettings
							.getDefaultPerspective());

					tm.setMemberId(member.getAmpTeamMemId());
					tm.setMemberName(member.getUser().getName());
					tm.setRoleId(member.getAmpMemberRole()
							.getAmpTeamMemRoleId());
					tm.setRoleName(member.getAmpMemberRole().getRole());
					tm.setTeamId(member.getAmpTeam().getAmpTeamId());
					tm.setTeamName(member.getAmpTeam().getName());
					tm.setRead(member.getReadPermission().booleanValue());
					tm.setWrite(member.getWritePermission().booleanValue());
					tm.setDelete(member.getDeletePermission().booleanValue());
					tm.setAppSettings(appSettings);
					if (usr != null) {
						tm.setEmail(usr.getEmail());
					}

					if (DbUtil.isUserTranslator(member.getUser().getId()) == true) {
						tm.setTranslator(true);
					} else {
						tm.setTranslator(false);
					}
					session.setAttribute("currentMember", tm);
					session.setMaxInactiveInterval(-1);
					lForm.setLogin(true);

					SiteDomain currentDomain = RequestUtils.getSiteDomain(request);
					
					String context = SiteUtils.getSiteURL(currentDomain, request.getScheme(),
		                            request.getServerPort(),
		                            request.getContextPath());					
					
					String url = context + "/translation/switchLanguage.do?code=" +
						tm.getAppSettings().getLanguage() +"&rfr="+context+"/aim/viewMyDesktop.do";
					
					response.sendRedirect(url);

				} else if (members.size() > 1) {
					lForm.setMembers(members);
					return mapping.findForward("selectTeam");
				}
			}

		} catch (Exception e) {
			e.printStackTrace(System.out);
		}
		return mapping.findForward("forward");
	}
}
