package org.digijava.kernel.ampapi.endpoints.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

import javax.servlet.ServletContext;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.digijava.kernel.ampapi.endpoints.errors.ApiErrorMessage;
import org.digijava.kernel.ampapi.endpoints.errors.ApiErrorResponse;
import org.digijava.kernel.ampapi.endpoints.security.SecurityErrors;
import org.digijava.kernel.request.TLSUtils;
import org.digijava.module.aim.helper.Constants;
import org.digijava.module.aim.helper.TeamMember;
import org.joda.time.DateTime;
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
	//Amount of time in minutes until the token expires
	//TODO for testing
	public static Integer TOKEN_EXPIRATION=30;

	public static String USER_ENDPOINT_PATH="/security/user";

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
		tokens.remove(TLSUtils.getRequest().getSession().getAttribute(USER_TOKEN));
		// we remove session object
		TLSUtils.getRequest().getSession().setAttribute(USER_TOKEN, null);
		String token = UUID.randomUUID().toString();
		// We create the ampampi object
		
		AmpApiToken apiToken = new AmpApiToken();
		apiToken.setToken(token);
		apiToken.setExpirationTime(new DateTime().plusMinutes(TOKEN_EXPIRATION));
		
		TLSUtils.getRequest().getSession().setAttribute(USER_TOKEN, apiToken);
		apiToken.setTeamMember((TeamMember) TLSUtils.getRequest().getSession().getAttribute(Constants.CURRENT_MEMBER));
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

			DateTime now = new DateTime();
			if (now.isAfter(apiToken.getExpirationTime())) {
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
                if (new DateTime().isAfter(requestApiToken.getExpirationTime())) {
                    logger.debug(SecurityErrors.TOKEN_EXPIRED.description);
                    errors.add(SecurityErrors.TOKEN_EXPIRED);
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
			if (TLSUtils.getRequest().getSession() != null
					&& TLSUtils.getRequest().getSession().getAttribute(Constants.CURRENT_MEMBER) != null) {
				// we check if the user has a token in session and that token is
				// valid
				AmpApiToken sessionAapiToken = (AmpApiToken) TLSUtils
						.getRequest().getSession().getAttribute(USER_TOKEN);
				if (sessionAapiToken == null) {
					// the user is logged in but without token
					error=SecurityErrors.NO_SESSION_TOKEN;
				} else {
					if (new DateTime().isAfter(sessionAapiToken.getExpirationTime())) {
						// the token in session has expired trhow exception
						error = SecurityErrors.TOKEN_EXPIRED;
					} else {
						return;// no session needs to be restored since the user
								// has a token in session and its valid
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
					if (new DateTime().isAfter(apiToken.getExpirationTime())) {
						// the toke has expired
						error = SecurityErrors.TOKEN_EXPIRED;
					} else {
						// we restore the session
						TLSUtils.getRequest()
								.getSession()
								.setAttribute(Constants.CURRENT_MEMBER,
										apiToken.getTeamMember());
						//session restored adding a request parameter to remove it later
						TLSUtils.getRequest().setAttribute(REMOVE_SESSION, "true");
					}
				}

			}
		}
		if(error!=null){
			logger.debug(error.description);
			ApiErrorResponse.reportUnauthorisedAccess(error);
		}
	}
	/** Remove token from application level
	 * @param sc ServletContext
	 * @param token token to remove
	 */
	public static void removeTokenFromContext(ServletContext sc,String token) {
		HashMap<String, AmpApiToken> tokens;

		tokens = (HashMap<String, AmpApiToken>) sc.getAttribute(SecurityUtil.TOKENS);

		if (tokens != null) {
			tokens.remove(token);
			sc.setAttribute(SecurityUtil.TOKENS,tokens);
		}
	}
}
