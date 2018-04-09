package org.digijava.kernel.ampapi.endpoints.security;

import java.lang.reflect.Method;
import java.util.Collection;
import java.util.Map;
import java.util.TreeMap;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.digijava.kernel.ampapi.endpoints.activity.InterchangeUtils;
import org.digijava.kernel.ampapi.endpoints.errors.ApiError;
import org.digijava.kernel.ampapi.endpoints.errors.ApiErrorMessage;
import org.digijava.kernel.ampapi.endpoints.errors.ApiErrorResponse;
import org.digijava.kernel.ampapi.endpoints.util.ApiMethod;
import org.digijava.kernel.security.RuleHierarchy;
import org.digijava.kernel.translator.TranslatorWorker;
import org.digijava.module.aim.helper.GlobalSettingsConstants;
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

    private static RuleHierarchy<AuthRule> ruleHierarchy = new RuleHierarchy.Builder<AuthRule>()
            .addRuleDependency(AuthRule.IN_WORKSPACE, AuthRule.AUTHENTICATED)
            .addRuleDependency(AuthRule.IN_ADMIN, AuthRule.AUTHENTICATED)
            .addRuleDependency(AuthRule.VIEW_ACTIVITY, AuthRule.IN_WORKSPACE)
            .build();

    /**
     * Main process to give authorization to call current method based on its authorization rules 
     * @param method the method to authorize
     * @param apiMethod method settings that store the authorization rules as well
     * @param containerReq general container request to be used for additional information 
     */
    public static void authorize(Method method, ApiMethod apiMethod, ContainerRequest containerReq) {
        if (apiMethod.authTypes().length == 0) {
            // no authorization -> nothing to check, skip immediately
            return;
        }

        Collection<AuthRule> authRules = ruleHierarchy.getEffectiveRules(apiMethod.authTypes());
        
        if (authRules.contains(AuthRule.AMP_OFFLINE_ENABLED) && !isAmpOfflineEnabled()) {
            ApiErrorMessage errorMessage = SecurityErrors.NOT_ALLOWED.withDetails("AMP Offline is not enabled");
            ApiErrorResponse.reportForbiddenAccess(errorMessage);
        }

        if (authRules.contains(AuthRule.AUTHENTICATED) && TeamUtil.getCurrentUser() == null) {
            ApiErrorResponse.reportUnauthorisedAccess(SecurityErrors.NOT_AUTHENTICATED);
            return;
        }

        if (authRules.contains(AuthRule.IN_WORKSPACE) && !TeamUtil.isUserInWorkspace()) {
            ApiErrorMessage errorMessage = SecurityErrors.NOT_ALLOWED.withDetails("No workspace selected");
            ApiErrorResponse.reportForbiddenAccess(errorMessage);
            return;
        }

        if (authRules.contains(AuthRule.IN_ADMIN) && !TeamUtil.isCurrentMemberAdmin()) {
            ApiErrorMessage errorMessage = SecurityErrors.NOT_ALLOWED.withDetails("You must be logged-in as admin");
            ApiErrorResponse.reportForbiddenAccess(errorMessage);
            return;
        }

        String methodInfo = String.format("%s %s.%s, authType = %s", containerReq.getMethod(),
                method.getDeclaringClass().getSimpleName(), method.getName(), authRules);

        Map<Integer, ApiErrorMessage> errors = new TreeMap<>();

        if (authRules.contains(AuthRule.VIEW_ACTIVITY) && !InterchangeUtils.isViewableActivity(containerReq)) {
            addError(methodInfo, errors, SecurityErrors.INVALID_REQUEST, "Activity doesn't exist or is not the latest version");
        }

        if (!errors.isEmpty()) {
            ApiErrorResponse.reportForbiddenAccess(ApiError.toError(errors.values()));
        }
    }
    
    private static boolean isAmpOfflineEnabled() {
        return FeaturesUtil.getGlobalSettingValueBoolean(GlobalSettingsConstants.AMP_OFFLINE_ENABLED);
    }

    /**
     * Merges errors of the same type
     * @param errors  current set of errors
     * @param error   new error
     * @param details new error additional details
     */
    private static void addError(String methodInfo, Map<Integer, ApiErrorMessage> errors, ApiErrorMessage error, String details) {
        logger.error(methodInfo + ". " + error.toString() + (details == null ? "" : " : " + details));
        if (errors.containsKey(error.id)) {
            error = errors.get(error.id); 
        }
        if (StringUtils.isNotBlank(details)) {
            error = error.withDetails(TranslatorWorker.translateText(details));
        }
        errors.put(error.id, error);
    }
}
