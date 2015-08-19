/**
 * 
 */
package org.digijava.kernel.ampapi.endpoints.security;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.digijava.kernel.ampapi.endpoints.activity.InterchangeUtils;
import org.digijava.kernel.ampapi.endpoints.errors.ApiError;
import org.digijava.kernel.ampapi.endpoints.errors.ApiErrorMessage;
import org.digijava.kernel.ampapi.endpoints.errors.ApiErrorResponse;
import org.digijava.kernel.ampapi.endpoints.util.AmpApiToken;
import org.digijava.kernel.ampapi.endpoints.util.ApiMethod;
import org.digijava.kernel.ampapi.endpoints.util.SecurityUtil;
import org.digijava.kernel.translator.TranslatorWorker;
import org.digijava.module.aim.helper.TeamMember;
import org.digijava.module.aim.util.FeaturesUtil;
import org.digijava.module.aim.util.TeamUtil;

import com.sun.jersey.spi.container.ContainerRequest;

/**
 * Authorizes current API request based on the given set of required actions 
 * 
 * @author Nadejda Mandrescu
 */
public class ActionAuthorizer {
	protected static final Logger logger = Logger.getLogger(ActionAuthorizer.class);

	public static void authorize(Method method, ApiMethod apiMethod, ContainerRequest containerReq) {
		if (apiMethod.authTypes().length == 0 
				|| apiMethod.authTypes().length == 1 && AuthRule.NONE.equals(apiMethod.authTypes()[0])) {
			// no authorization -> nothing to check, skip immediately
			return;
		}
			
		String methodInfo = String.format("%s %s.%s, authType = %s", containerReq.getMethod(),
				method.getDeclaringClass().getSimpleName(), method.getName(), apiMethod.authTypes());
		
		Map<Integer, ApiErrorMessage> errors = new TreeMap<Integer, ApiErrorMessage>();
		
		for (AuthRule authType : apiMethod.authTypes()) {
			switch (authType) {
			case NONE:
				addError(methodInfo, errors, SecurityErrors.INVALID_API_METHOD, "Mixed authorization with NO authorization");
				break;
			case TOKEN:
				/* AMP-20664: we'll need to refactor so that token authentication done in same place as other authorization actions 
				verifyTokenMatch(containerReq);
				*/
				break;
			case IN_WORKSPACE:
				if (!TeamUtil.isUserInWorkspace()) {
					addError(methodInfo, errors, SecurityErrors.NOT_ALLOWED, "No workspace selected");
				}
				break;
			case ADD_ACTIVITY:
				if (!addActivityAllowed()) {
					addError(methodInfo, errors, SecurityErrors.NOT_ALLOWED, "Adding activity is not allowed");
				}
				break;
			case EDIT_ACTIVITY:
				if (!InterchangeUtils.isEditableActivity(containerReq)) {
					addError(methodInfo, errors, SecurityErrors.NOT_ALLOWED, "No right to edit this activity");
				}
				break;
			case VIEW_ACTIVITY:
				if (!InterchangeUtils.isViewableActivity(containerReq)) {
					addError(methodInfo, errors, SecurityErrors.INVALID_REQUEST, "Activity doesn't exist or is not the latest version");
				}
				break;
			}
		}
		if (!errors.isEmpty()) {
			ApiErrorResponse.reportForbiddenAccess(ApiError.toError(errors.values()));
		}
	}
	
	/**
	 * Verifies that a valid token is used
	 */
	private static void verifyTokenMatch(ContainerRequest containerReq) {
		ApiErrorMessage error = null;
		AmpApiToken sessionToken = SecurityUtil.getTokenFromSession();
		if (sessionToken == null) {
			error = SecurityErrors.NO_SESSION_TOKEN;
		} else {
			String token = containerReq.getHeaderValue("X-Auth-Token");
			List<ApiErrorMessage>errors = new ArrayList<ApiErrorMessage>();

			SecurityUtil.getAmpApiTokenFromApplication(token, errors);
			
			if (errors.size()>0) {
				error = errors.get(0);
			} else if (!token.equals(sessionToken.getToken())) {
				error = SecurityErrors.INVALID_TOKEN;
			}
		}
		if (error != null) {
			logger.error(error.description);
			ApiErrorResponse.reportUnauthorisedAccess(error);
		}
	}
	
	/**
	 * @return true if add activity is allowed
	 */
	public static boolean addActivityAllowed() {
		TeamMember tm = TeamUtil.getCurrentMember();
		return !TeamUtil.isCurrentMemberAdmin() && tm != null && Boolean.TRUE.equals(tm.getAddActivity()) && 
				(FeaturesUtil.isVisibleField("Add Activity Button") || FeaturesUtil.isVisibleField("Add SSC Button"));
	}
	
	/**
	 * Merges errors of the same type
	 * @param errors current set of errors
	 * @param error  new error
	 * @param value  new error additional details
	 */
	private static void addError(String methodInfo, Map<Integer, ApiErrorMessage> errors, ApiErrorMessage error, String details) {
		logger.error(methodInfo + ". " + error.toString() + details == null ? "" : " : " + details);
		if (errors.containsKey(error.id)) {
			error = errors.get(error.id); 
		}
		if (StringUtils.isNotBlank(details)) {
			error = new ApiErrorMessage(error, TranslatorWorker.translateText(details));
		}
		errors.put(error.id, error);
	}
	
}
