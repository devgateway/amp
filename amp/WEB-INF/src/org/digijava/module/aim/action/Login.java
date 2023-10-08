package org.digijava.module.aim.action;

import org.apache.log4j.Logger;
import org.apache.struts.action.*;
import org.digijava.kernel.cache.AbstractCache;
import org.digijava.kernel.cache.ehcache.EhCacheWrapper;
import org.digijava.kernel.request.SiteDomain;
import org.digijava.kernel.security.HttpLoginManager;
import org.digijava.kernel.translator.TranslatorWorker;
import org.digijava.kernel.user.User;
import org.digijava.kernel.util.DgUtil;
import org.digijava.kernel.util.RequestUtils;
import org.digijava.kernel.util.SiteUtils;
import org.digijava.kernel.util.UserUtils;
import org.digijava.module.aim.dbentity.AmpGlobalSettings;
import org.digijava.module.aim.dbentity.AmpTeamMember;
import org.digijava.module.aim.form.LoginForm;
import org.digijava.module.aim.helper.Constants;
import org.digijava.module.aim.helper.TeamMember;
import org.digijava.module.aim.util.TeamMemberUtil;
import org.digijava.module.aim.util.TeamUtil;
import org.digijava.module.gateperm.core.GatePermConst;
import org.digijava.module.gateperm.util.PermissionUtil;
import org.digijava.module.um.model.TruLoginRequest;
import org.digijava.module.um.model.TruLoginResponse;
import org.digijava.module.um.util.UmUtil;
import reactor.core.publisher.Mono;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.*;

import static org.digijava.module.um.util.DbUtil.*;


/**
 * Validates a user using the user name and the password.
 * Shows the Desktop page if successfull otherwise shows them login page.
 * If the user belongs to multiple teams, a 'select team' page is shown.
 *
 * @author Priyajith
 */
public class Login extends Action {

    private static Logger logger = Logger.getLogger(Login.class);
    private ServletContext ampContext = null;

    public ActionForward execute(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response)
            throws java.lang.Exception {

        //redirecting to index.do so spring handles the access and we don't give the user an error
        response.sendRedirect("index.do");
        //throw new IllegalAccessException("This code must not be accessed any more");

        LoginForm lForm = (LoginForm) form; // login form instance
        ampContext = getServlet().getServletContext();

        ActionMessages errors = new ActionMessages();
        HttpSession session = request.getSession();

        String sessionId = null;

        try {

            if (lForm.getUserId() != null && lForm.getPassword() != null) {

                /*
                 * Validates the user with the username and password and stores the login
                 * result in an object cache which can be accessed by the sessionId which
                 * the function returns.
                 */
                sessionId = HttpLoginManager.loginByCredentials(request,
                        response, lForm.getUserId().toLowerCase(), lForm
                                .getPassword(), false);

                if (sessionId != null) {

                    /*
                     * Used to get the login reult from the sessionId returned by the
                     * HttpLoginManager.loginByCredentials(...) function
                     */
                    HttpLoginManager.LoginInfo loginInfo = HttpLoginManager
                            .loginBySessionId(request, response, sessionId,
                                    false);

                    if (loginInfo.getLoginResult() == HttpLoginManager.LOGIN_RESULT_INVALID) {
                        // invalid login
                        lForm.setLogin(false);
                        errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage(
                                "error.aim.invalidLogin"));
                        saveErrors(request, errors);
                        return mapping.getInputForward();
                    } else if (loginInfo.getLoginResult() == HttpLoginManager.LOGIN_RESULT_BANNED) {
                        // user banned
                        lForm.setLogin(false);
                        errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage(
                                "error.aim.userBanned"));
                        saveErrors(request, errors);
                        return mapping.getInputForward();
                    } else if (loginInfo.getLoginResult() == HttpLoginManager.LOGIN_RESULT_OK) {
                        // valid user.

                        // clear the session variables
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
                        // problem in login. login again
                        lForm.setLogin(false);
                        errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage(
                                "error.aim.loginFailed"));
                        saveErrors(request, errors);
                        return mapping.getInputForward();
                    }
                } else {
                    // problem in login. login again
                    lForm.setLogin(false);
                    errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage(
                            "error.aim.loginFailed"));
                    saveErrors(request, errors);
                    return mapping.getInputForward();
                }


                // Get the DgUser object from the username
                User usr = UserUtils.getUserByEmailAddress(lForm.getUserId());

                // Check whether the user is a site admin or not
                boolean siteAdmin = DgUtil.isSiteAdministrator(request);

                /*
                 * if the member is part of multiple teams the below collection contains more than one element.
                 * Otherwise it will have only one element.
                 * The following function will return objects of type org.digijava.module.aim.dbentity.AmpTeamMember
                 * The function will return null, if the user is just a site administrator or if the user is a
                 * registered user but has not yet been assigned a team
                 */
                //
                List<AmpGlobalSettings> settings = getGlobalSettingsBySection("trubudget");
                if (getSettingValue(settings,"isEnabled").equalsIgnoreCase("true") && usr.getTruBudgetEnabled()) {


                    //login to trubudget
                    TruLoginRequest truLoginRequest = new TruLoginRequest();
                    truLoginRequest.setApiVersion(getSettingValue(settings, "apiVersion"));
                    TruLoginRequest.Data data = new TruLoginRequest.Data();
                    TruLoginRequest.User user1 = new TruLoginRequest.User();

                    user1.setPassword(UmUtil.decrypt(usr.getTruBudgetPassword(),usr.getTruBudgetKeyGen()));
                    user1.setId(lForm.getUserId().split("@")[0]);
                    data.setUser(user1);
                    truLoginRequest.setData(data);
                    try {
                        Mono<TruLoginResponse> truResp = loginToTruBudget(truLoginRequest, settings);
                        truResp.subscribe(truLoginResponse -> {
                            logger.info("Trubudget login response: " + truLoginResponse);
                            AbstractCache myCache = new EhCacheWrapper("trubudget");
                            myCache.put("truBudgetToken",truLoginResponse.getData().getUser().getToken());
                            myCache.put("truBudgetUser",usr.getEmail().split("@")[0]);
                            myCache.put("truBudgetPassword",usr.getEmail());
                        });
                    } catch (Exception e) {
                        logger.info("Trubudget login: " + e.getMessage(), e);
                    }
//                    ProjectUtil.createProject(null);
                }



                Collection members = TeamMemberUtil.getTeamMembers(lForm.getUserId());
                if (members == null || members.size() == 0) {
                    if (siteAdmin == true) { // user is a site admin
                        // set the session variable 'ampAdmin' to the value 'yes'
                        session.setAttribute("ampAdmin", "yes");
                        // create a TeamMember object and set it to a session variabe 'currentMember'
                        TeamMember tm = new TeamMember(usr);
                        tm.setTeamName(TranslatorWorker.translateText("AMP Administrator"));
                        session.setAttribute(Constants.CURRENT_MEMBER, tm);
                        // show the index page with the admin toolbar at the bottom

                        return mapping.findForward("admin");
                    } else {
                        // The user is a regsitered user but not a team member
                        lForm.setLogin(false);
                        errors.add(ActionMessages.GLOBAL_MESSAGE,
                                new ActionMessage(
                                        "error.aim.userNotTeamMember"));
                        saveErrors(request, errors);
                        return mapping.getInputForward();
                    }
                } else {
                    if (siteAdmin == true) {
                        session.setAttribute("ampAdmin", "yes");
                    } else {
                        session.setAttribute("ampAdmin", "no");
                    }
                }
                if (members.size() == 1) {
                    // if the user is part of just on team, load his personalized settings

                    Iterator itr = members.iterator();
                    AmpTeamMember member = (AmpTeamMember) itr.next();

                    synchronized (ampContext) {
                        HashMap userActList = (HashMap) ampContext.getAttribute(Constants.USER_ACT_LIST);
                        if (userActList != null &&
                                userActList.containsKey(member.getAmpTeamMemId())) {
                            // expire all other entries

                            //logger.info("getting the value for " + member.getAmpTeamMemId());

                            Long actId = (Long) userActList.get(member.getAmpTeamMemId());
                            HashMap editActMap = (HashMap) ampContext.getAttribute(Constants.EDIT_ACT_LIST);
                            String sessId = null;
                            if (editActMap != null)  {
                                for (Object o : editActMap.keySet()) {
                                    sessId = (String) o;
                                    Long tempActId = (Long) editActMap.get(sessId);

                                    //logger.info("tempActId = " + tempActId + " actId = " + actId);
                                    if (tempActId.longValue() == actId.longValue()) {
                                        editActMap.remove(sessId);
                                        //logger.info("Removed the entry for " + actId);
                                        ampContext.setAttribute(Constants.EDIT_ACT_LIST, editActMap);
                                        break;
                                    }
                                }
                            }
                            userActList.remove(member.getAmpTeamMemId());
                            ampContext.setAttribute(Constants.USER_ACT_LIST,userActList);

                            HashMap tsActList = (HashMap) ampContext.getAttribute(Constants.TS_ACT_LIST);
                            if (tsActList != null) {
                                tsActList.remove(actId);
                                ampContext.setAttribute(Constants.TS_ACT_LIST,tsActList);
                            }
                            ArrayList sessList = (ArrayList) ampContext.getAttribute(Constants.SESSION_LIST);
                            if (sessList != null) {
                                sessList.remove(sessId);
                                Collections.sort(sessList);
                                ampContext.setAttribute(Constants.SESSION_LIST,sessList);
                            }
                        }
                    }

                    TeamMember tm = TeamUtil.setupFiltersForLoggedInUser(request, member);
                    if (usr != null) {
                        tm.setEmail(usr.getEmail());
                    }
                    
                    PermissionUtil.putInScope(session, GatePermConst.ScopeKeys.CURRENT_MEMBER, tm);
                    
                    // Set the session infinite. i.e. session never timeouts
                    session.setMaxInactiveInterval(-1);
                    lForm.setLogin(true);

                    // forward to members desktop page

                    SiteDomain currentDomain = RequestUtils.getSiteDomain(request);
                    String context = SiteUtils.getSiteURL(currentDomain, request.getScheme(),
                                    request.getServerPort(),
                                    request.getContextPath());

                    // Users language should be selected for all his pages
                    /*
                     * We use translation module in the digijava framework to switch the language. Members
                     * language is passed as a parameter to the url '/translation/switchLanguage.do'
                     * After switching the language, '/switchLanguage.do' will redirect the request to the url specified
                     * in the paramater 'rfr'
                     */
                    String url = context + "/translation/switchLanguage.do?code=" +
                        tm.getAppSettings().getLanguage() +"&rfr="+context+"/aim/showDesktop.do";

                    response.sendRedirect(url);

                } else if (members.size() > 1) {
                    // member is part of more than one team. Show the select team page
                    lForm.setMembers(members);
                    return mapping.findForward("selectTeam");
                }
            } else {
                String siteAdmin = (String) session.getAttribute("ampAdmin");
                TeamMember tm = (TeamMember) session.getAttribute("currentMember");
                if (tm != null && session.getAttribute(Constants.TEAM_ID) != null) {
                    String fwdUrl = "showDesktop.do";
                    response.sendRedirect(fwdUrl);
                } else if (siteAdmin != null && "yes".equals(siteAdmin)) {
                    String fwdUrl = "admin.do";
                    response.sendRedirect(fwdUrl);
                }
            }

        } catch (Exception e) {
            logger.error("Exception " + e.getMessage());
            e.printStackTrace(System.out);
        }
        return mapping.findForward("forward");
    }
}
