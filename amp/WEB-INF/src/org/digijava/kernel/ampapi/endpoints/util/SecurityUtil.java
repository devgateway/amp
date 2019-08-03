package org.digijava.kernel.ampapi.endpoints.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.digijava.kernel.ampapi.endpoints.errors.ApiErrorMessage;
import org.digijava.kernel.ampapi.endpoints.errors.ApiErrorResponseService;
import org.digijava.kernel.ampapi.endpoints.security.ApiAuthentication;
import org.digijava.kernel.ampapi.endpoints.security.SecurityErrors;
import org.digijava.kernel.request.TLSUtils;
import org.digijava.kernel.user.User;
import org.digijava.kernel.util.UserUtils;
import org.digijava.module.aim.dbentity.AmpTeamMember;
import org.digijava.module.aim.helper.Constants;
import org.digijava.module.aim.helper.TeamMember;
import org.digijava.module.aim.util.TeamMemberUtil;

/**
 * Utility class to handle API security related issues
 * @author Julian de Anquin
 *
 */
public class SecurityUtil {
    protected static final Logger logger = Logger.getLogger(SecurityUtil.class);

    //key to store the token at session level
    public static String USER_TOKEN = "org.digijava.kernel.ampapi.endpoints.security.token";
    //TOKEN store at application level
    public static String TOKENS = "org.digijava.kernel.ampapi.endpoints.security.tokens";
    public static String REMOVE_SESSION = "org.digijava.kernel.ampapi.endpoints.security.remove-session";


    @SuppressWarnings("unchecked")
    public static AmpApiToken generateToken() {

        // if generate token is present we will override
        HashMap<String, AmpApiToken> tokens;

        tokens = (HashMap<String, AmpApiToken>) TLSUtils.getRequest().getServletContext()
                .getAttribute(SecurityUtil.TOKENS);
        
        if (tokens == null) {
            tokens = new HashMap<String, AmpApiToken>();
        }
        
        //we remove application token
        AmpApiToken tokenFromSession = (AmpApiToken) TLSUtils.getRequest().getSession().getAttribute(USER_TOKEN);
        if (tokenFromSession != null) {
            tokens.remove(tokenFromSession.getToken());
        }
        
        // we remove session object
        TLSUtils.getRequest().getSession().setAttribute(USER_TOKEN, null);
        String token = UUID.randomUUID().toString();
        // We create the ampampi object

        TeamMember teamMember = (TeamMember) TLSUtils.getRequest().getSession().getAttribute(Constants.CURRENT_MEMBER);
        User user = (User) TLSUtils.getRequest().getSession().getAttribute(Constants.CURRENT_USER);

        AmpApiToken apiToken = new AmpApiToken(token, user, teamMember);

        TLSUtils.getRequest().getSession().setAttribute(USER_TOKEN, apiToken);

        tokens.put(token, apiToken);
        TLSUtils.getRequest().getServletContext().setAttribute(SecurityUtil.TOKENS, tokens);
        
        return apiToken;
    }

    /**
     * Get the token from session. if the token expired remove it from session
     * and from application level.
     * 
     * @return ApiToken if valid, null if not preset or expired
     */
    public static AmpApiToken getTokenFromSession() {
        AmpApiToken apiToken = (AmpApiToken) TLSUtils.getRequest().getSession().getAttribute(USER_TOKEN);
        if (apiToken != null) {

            if (apiToken.isExpired()) {
                //token expired remove from session and from application

                TLSUtils.getRequest().getSession().setAttribute(USER_TOKEN,null);

                HashMap<String, AmpApiToken> tokens;

                tokens = (HashMap<String, AmpApiToken>) TLSUtils.getRequest().getServletContext()
                        .getAttribute(SecurityUtil.TOKENS);
                if (tokens != null) {
                    tokens.remove(apiToken.getToken());
                    TLSUtils.getRequest().getServletContext().setAttribute(SecurityUtil.TOKENS, tokens);
                }
                //if the token is expired, then we need to return null
                apiToken = null;
            }
        }
        return apiToken;
    }
    /**
     * Get user information information from token. IF the http session expired the token might still be valid, however if the user
     * logged of the token will be removed from application level 
     * @param token
     * @return
     */
    public static AmpApiToken getAmpApiTokenFromApplication(String token,
            List<ApiErrorMessage> errors) {
        HashMap<String, AmpApiToken> tokens;
        AmpApiToken requestApiToken = null;
        if (StringUtils.isBlank(token)) {
            errors.add(SecurityErrors.NO_REQUEST_TOKEN);
            requestApiToken = null;
        } else {
            tokens = (HashMap<String, AmpApiToken>) TLSUtils.getRequest()
                    .getServletContext().getAttribute(SecurityUtil.TOKENS);
            requestApiToken = (tokens == null) ? null : tokens.get(token);
            if (requestApiToken == null) {
                errors.add(SecurityErrors.NO_SESSION_TOKEN);
            } else {
                if (requestApiToken.isExpired()) {
                    logger.debug(SecurityErrors.TOKEN_EXPIRED.description);
                    errors.add(SecurityErrors.TOKEN_EXPIRED);
                }
                ApiErrorMessage errorMessage = validateToken(requestApiToken);
                if (errorMessage != null) {
                    errors.add(errorMessage);
                }
            }
        }
        return requestApiToken;
    }

    public static void validateTokenAndRestoreSession(String token) {
        ApiErrorMessage error = null;

        if (token == null) {
            error = SecurityErrors.INVALID_TOKEN;
        } else {
            // If the user has a token in session and the token is valid we will
            // use that session
            HttpServletRequest request = TLSUtils.getRequest();
            HttpSession session = request.getSession();
            if (session.getAttribute(Constants.CURRENT_MEMBER) != null) {
                // we check if the user has a token in session and that token is
                // valid
                AmpApiToken sessionAapiToken = (AmpApiToken) session.getAttribute(USER_TOKEN);
                if (sessionAapiToken == null) {
                    // the user is logged in but without token
                    error=SecurityErrors.NO_SESSION_TOKEN;
                } else {
                    if (sessionAapiToken.isExpired()) {
                        // the token in session has expired trhow exception
                        error = SecurityErrors.TOKEN_EXPIRED;
                    } else {
                        error = validateToken(sessionAapiToken);

                        if (error == null) {
                            sessionAapiToken.touch();
                            return;// no session needs to be restored since the user
                            // has a token in session and its valid
                        }
                    }
                }

            } else {
                // no user in session we are rebuilding session from token
                List<ApiErrorMessage>errors=new ArrayList<ApiErrorMessage>();
                AmpApiToken apiToken = getAmpApiTokenFromApplication(token,errors);
                // at this point the token exists
                if (apiToken == null || errors.size()>0) {
                    if(errors.size()>0){
                        error=errors.get(0);
                    }else{
                        error = SecurityErrors.INVALID_TOKEN;
                    }
                } else {
                    if (apiToken.isExpired()) {
                        // the toke has expired
                        error = SecurityErrors.TOKEN_EXPIRED;
                    } else {
                        apiToken.touch();
                        // we restore the session
                        session.setAttribute(Constants.CURRENT_USER, apiToken.getUser());
                        session.setAttribute(Constants.CURRENT_MEMBER, apiToken.getTeamMember());
                        //session restored adding a request parameter to remove it later
                        request.setAttribute(REMOVE_SESSION, "true");
                    }
                }

            }
        }
        if(error!=null){
            logger.debug(error.description);
            ApiErrorResponseService.reportUnauthorisedAccess(error);
        }
    }

    /**
     * Checks validity of the token. Token becomes invalid if user:
     * <ul><li>was blocked
     * <li>was removed from the team
     * <li>changed password
     * </ul>
     * @return null if no errors, otherwise the error describing the reason
     */
    private static ApiErrorMessage validateToken(AmpApiToken ampApiToken) {
        String email = ampApiToken.getUser().getEmail();

        User user = UserUtils.getUserByEmailAddress(email);

        ApiErrorMessage errorMessage = ApiAuthentication.performSecurityChecks(user, TLSUtils.getRequest());
        if (errorMessage != null) {
            return errorMessage;
        }

        if (ampApiToken.getTeamMember() != null && isNotInWorkspace(ampApiToken.getTeamMember().getTeamId(), email)) {
            return SecurityErrors.INVALID_TEAM;
        }

        if (isPasswordChanged(ampApiToken, user)) {
            return SecurityErrors.PASSWORD_CHANGED;
        }

        return null;
    }

    /**
     * Returns true if user is still part of the workspace.
     */
    private static boolean isNotInWorkspace(Long workspaceId, String email) {
        AmpTeamMember teamMember = TeamMemberUtil.getAmpTeamMemberByEmailAndTeam(email, workspaceId);
        return teamMember == null;
    }

    /**
     * Returns true if password changed since the token was created.
     */
    private static boolean isPasswordChanged(AmpApiToken ampApiToken, User user) {
        return !user.getPassword().equals(ampApiToken.getUser().getPassword());
    }

}
