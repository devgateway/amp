package org.digijava.kernel.ampapi.endpoints.security;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Map;
import java.util.TreeMap;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.digijava.kernel.ampapi.endpoints.activity.InterchangeUtils;
import org.digijava.kernel.ampapi.endpoints.errors.ApiError;
import org.digijava.kernel.ampapi.endpoints.errors.ApiErrorMessage;
import org.digijava.kernel.ampapi.endpoints.errors.ApiErrorResponse;
import org.digijava.kernel.ampapi.endpoints.util.ApiMethod;
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
	
	/**
	 * Main process to give authorization to call current method based on its authorization rules 
	 * @param method the method to authorize
	 * @param apiMethod method settings that store the authorization rules as well
	 * @param containerReq general container request to be used for additional information 
	 */
	public static void authorize(Method method, ApiMethod apiMethod, ContainerRequest containerReq) {
		if (apiMethod.authTypes().length == 0 
				|| apiMethod.authTypes().length == 1 && AuthRule.NONE.equals(apiMethod.authTypes()[0])) {
			// no authorization -> nothing to check, skip immediately
			return;
		}
			
		if (contains(apiMethod.authTypes(), AuthRule.AUTHENTICATED) && TeamUtil.getCurrentMember() == null) {
			ApiErrorResponse.reportUnauthorisedAccess(SecurityErrors.NOT_AUTHENTICATED);
			return;
		}

		String methodInfo = String.format("%s %s.%s, authType = %s", containerReq.getMethod(),
				method.getDeclaringClass().getSimpleName(), method.getName(), Arrays.toString(apiMethod.authTypes()));

		Map<Integer, ApiErrorMessage> errors = new TreeMap<>();
		
		for (AuthRule authType : apiMethod.authTypes()) {
			switch (authType) {
			case NONE:
				addError(methodInfo, errors, SecurityErrors.INVALID_API_METHOD, "Mixed authorization with NO authorization");
				break;
			case IN_WORKSPACE:
				if (!TeamUtil.isUserInWorkspace()) {
					addError(methodInfo, errors, SecurityErrors.NOT_ALLOWED, "No workspace selected");
				}
				break;
			case IN_ADMIN:
				if (!TeamUtil.isCurrentMemberAdmin()) {
					addError(methodInfo, errors, SecurityErrors.NOT_ALLOWED, "You must be logged-in as admin");
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
	 * Returns true if object is present in the array.
	 */
	private static <T extends Enum<T>> boolean contains(T[] array, T obj) {
		for (T item : array) {
			if (item == obj) {
				return true;
			}
		}
		return false;
	}

	/**
	 * @return true if add activity is allowed
	 */
	private static boolean addActivityAllowed() {
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
		logger.error(methodInfo + ". " + error.toString() + (details == null ? "" : " : " + details));
		if (errors.containsKey(error.id)) {
			error = errors.get(error.id); 
		}
		if (StringUtils.isNotBlank(details)) {
			error = new ApiErrorMessage(error, TranslatorWorker.translateText(details));
		}
		errors.put(error.id, error);
	}
}
