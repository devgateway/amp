package org.digijava.kernel.ampapi.endpoints.util;

import java.util.HashMap;
import java.util.UUID;

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
	//key to store the token at session level
	public static String USER_TOKEN = "org.digijava.kernel.ampapi.endpoints.security.token";
	//TOKEN store at application level
	public static String TOKENS = "org.digijava.kernel.ampapi.endpoints.security.tokens";
	//Amount of time in minutes until the token expires
	//TODO for testing
	public static Integer TOKEN_EXPIRATION=30;
	@SuppressWarnings("unchecked")
	public static String generateToken() {

		// if generate token is present we will override
		HashMap<String, AmpApiToken> tokens;

		tokens = (HashMap<String, AmpApiToken>) TLSUtils.getRequest().getServletContext()
				.getAttribute(SecurityUtil.TOKENS);
		
		if (tokens == null) {
			tokens = new HashMap<String, AmpApiToken>();
		}
		if (TLSUtils.getRequest().getSession().getAttribute(USER_TOKEN) != null) {
			// if token exist in session we remove it
			if (tokens.get(TLSUtils.getRequest().getSession().getAttribute(USER_TOKEN)) != null) {
				// we remove appliaction level context
				tokens.remove(TLSUtils.getRequest().getSession().getAttribute(USER_TOKEN));
			}
			// we remove session object
			TLSUtils.getRequest().getSession().setAttribute(USER_TOKEN, null);
		}
		String token = UUID.randomUUID().toString();
		// We create the ampampi object
		
		AmpApiToken apiToken = new AmpApiToken();
		apiToken.setToken(token);
		apiToken.setExpirationTime(new DateTime().plusMinutes(TOKEN_EXPIRATION));
		
		TLSUtils.getRequest().getSession().setAttribute(USER_TOKEN, apiToken);
		apiToken.setTeamMember((TeamMember) TLSUtils.getRequest().getSession().getAttribute(Constants.CURRENT_MEMBER));
		tokens.put(token, apiToken);
		TLSUtils.getRequest().getServletContext().setAttribute(SecurityUtil.TOKENS, tokens);
		return token;
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

			DateTime now=new DateTime();
			if (now.isAfter(apiToken.getExpirationTime())) {
				//token expired remove from session and from application
				HashMap<String, AmpApiToken> tokens;

				tokens = (HashMap<String, AmpApiToken>) TLSUtils.getRequest().getServletContext()
						.getAttribute(SecurityUtil.TOKENS);
				if(tokens!=null){
					tokens.remove(apiToken.getToken());
					TLSUtils.getRequest().getServletContext().setAttribute(SecurityUtil.TOKENS, tokens);
				}
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
	public static AmpApiToken getAmpApiTokenFromAppliaction(String token){
		return null;
	}
	
}
