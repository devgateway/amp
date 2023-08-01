package org.digijava.kernel.ampapi.endpoints.security;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.digijava.kernel.ampapi.endpoints.activity.ActivityInterchangeUtils;
import org.digijava.kernel.ampapi.endpoints.common.AmpConfiguration;
import org.digijava.kernel.ampapi.endpoints.errors.ApiError;
import org.digijava.kernel.ampapi.endpoints.errors.ApiErrorMessage;
import org.digijava.kernel.ampapi.endpoints.errors.ApiErrorResponseService;
import org.digijava.kernel.ampapi.endpoints.util.ApiMethod;
import org.digijava.kernel.ampapi.filters.AmpClientModeHolder;
import org.digijava.kernel.security.RuleHierarchy;
import org.digijava.kernel.services.AmpVersionService;
import org.digijava.kernel.translator.TranslatorWorker;
import org.digijava.kernel.util.SpringUtil;
import org.digijava.module.aim.dbentity.AmpOfflineRelease;
import org.digijava.module.aim.util.FeaturesUtil;
import org.digijava.module.aim.util.TeamUtil;
import org.glassfish.jersey.server.ContainerRequest;

import java.lang.reflect.Method;
import java.util.Collection;
import java.util.Map;
import java.util.TreeMap;


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
        
        if (authRules.contains(AuthRule.AUTHENTICATED) && TeamUtil.getCurrentUser() == null) {
            ApiErrorResponseService.reportUnauthorisedAccess(SecurityErrors.NOT_AUTHENTICATED);
        }
        
        if (authRules.contains(AuthRule.AMP_OFFLINE) 
                || (authRules.contains(AuthRule.AMP_OFFLINE_OPTIONAL) && AmpClientModeHolder.isOfflineClient())) {
            
            if (!FeaturesUtil.isAmpOfflineEnabled()) {
                ApiErrorMessage errorMessage = SecurityErrors.NOT_ALLOWED.withDetails("AMP Offline is not enabled");
                ApiErrorResponseService.reportForbiddenAccess(errorMessage);
            }
            
            if (!AmpClientModeHolder.isOfflineClient()) {
                ApiErrorMessage errorMessage = SecurityErrors.NOT_ALLOWED
                        .withDetails("AMP Offline User-Agent is not present in request headers");
                ApiErrorResponseService.reportForbiddenAccess(errorMessage);
            }
            
            AmpOfflineRelease clientRelease = AmpConfiguration.detectClientRelease();
            AmpVersionService ampVersionService = SpringUtil.getBean(AmpVersionService.class);
            
            if (!ampVersionService.isAmpOfflineCompatible(clientRelease)) {
                ApiErrorResponseService.reportForbiddenAccess(SecurityErrors.NOT_ALLOWED
                        .withDetails("AMP Offline is not compatible"));
            }
        }
        
        if (authRules.contains(AuthRule.IN_WORKSPACE) && !TeamUtil.isUserInWorkspace()) {
            ApiErrorMessage errorMessage = SecurityErrors.NOT_ALLOWED.withDetails("No workspace selected");
            ApiErrorResponseService.reportForbiddenAccess(errorMessage);
        }
        
        if (authRules.contains(AuthRule.IN_ADMIN) && !TeamUtil.isCurrentMemberAdmin()) {
            ApiErrorMessage errorMessage = SecurityErrors.NOT_ALLOWED.withDetails("You must be logged-in as admin");
            ApiErrorResponseService.reportForbiddenAccess(errorMessage);
        }

        String methodInfo = String.format("%s %s.%s, authType = %s", containerReq.getMethod(),
                method.getDeclaringClass().getSimpleName(), method.getName(), authRules);

        Map<Integer, ApiErrorMessage> errors = new TreeMap<>();

        if (authRules.contains(AuthRule.VIEW_ACTIVITY) && !ActivityInterchangeUtils.isViewableActivity(containerReq)) {
            addError(methodInfo, errors, SecurityErrors.INVALID_REQUEST, "Activity doesn't exist or is not the latest version");
        }
        if (authRules.contains(AuthRule.PUBLIC_VIEW_ACTIVITY) && !ActivityInterchangeUtils.
                canViewActivityIfCreatedInPrivateWs(containerReq)) {
            ApiErrorMessage errorMessage = SecurityErrors.NOT_ALLOWED.withDetails("You must be logged-in in the "
                    + "workspace where the activity was created");
            ApiErrorResponseService.reportForbiddenAccess(errorMessage);
        }

        if (!errors.isEmpty()) {
            ApiErrorResponseService.reportForbiddenAccess(ApiError.toError(errors.values()));
        }
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
