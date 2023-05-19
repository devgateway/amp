package org.digijava.kernel.ampapi.endpoints.activity;

import com.sun.jersey.spi.container.ContainerRequest;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.dgfoundation.amp.Util;
import org.digijava.kernel.ampapi.endpoints.activity.dto.ActivityInformation;
import org.digijava.kernel.ampapi.endpoints.activity.dto.ActivitySummary;
import org.digijava.kernel.ampapi.endpoints.activity.dto.TeamMemberInformation;
import org.digijava.kernel.ampapi.endpoints.activity.field.APIField;
import org.digijava.kernel.ampapi.endpoints.common.EPConstants;
import org.digijava.kernel.ampapi.endpoints.common.EndpointUtils;
import org.digijava.kernel.ampapi.endpoints.common.JsonApiResponse;
import org.digijava.kernel.ampapi.endpoints.errors.ApiError;
import org.digijava.kernel.ampapi.endpoints.errors.ApiErrorResponse;
import org.digijava.kernel.ampapi.endpoints.errors.ApiErrorResponseService;
import org.digijava.kernel.ampapi.endpoints.errors.ApiRuntimeException;
import org.digijava.kernel.ampapi.endpoints.errors.GenericErrors;
import org.digijava.kernel.ampapi.endpoints.security.SecurityService;
import org.digijava.kernel.ampapi.endpoints.security.dto.UserSessionInformation;
import org.digijava.kernel.ampapi.filters.AmpClientModeHolder;
import org.digijava.kernel.exception.DgException;
import org.digijava.kernel.persistence.PersistenceManager;
import org.digijava.kernel.request.TLSUtils;
import org.digijava.kernel.services.AmpFieldsEnumerator;
import org.digijava.kernel.user.User;
import org.digijava.kernel.util.UserUtils;
import org.digijava.module.aim.dbentity.AmpActivityVersion;
import org.digijava.module.aim.dbentity.AmpAnnualProjectBudget;
import org.digijava.module.aim.dbentity.AmpOrgRole;
import org.digijava.module.aim.dbentity.AmpTeamMember;
import org.digijava.module.aim.helper.Constants;
import org.digijava.module.aim.helper.CurrencyWorker;
import org.digijava.module.aim.helper.TeamMember;
import org.digijava.module.aim.helper.Workspace;
import org.digijava.module.aim.util.ActivityUtil;
import org.digijava.module.aim.util.ActivityVersionUtil;
import org.digijava.module.aim.util.DbUtil;
import org.digijava.module.aim.util.DecimalWraper;
import org.digijava.module.aim.util.FeaturesUtil;
import org.digijava.module.aim.util.TeamMemberUtil;
import org.digijava.module.aim.util.TeamUtil;
import org.digijava.module.aim.util.ValidationStatus;
import org.hibernate.CacheMode;

import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.core.PathSegment;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import static org.digijava.kernel.ampapi.endpoints.common.EPConstants.ACTIVITY_DOCUMENTS;
import static org.digijava.module.aim.helper.GlobalSettingsConstants.EXEMPT_ORGANIZATION_DOCUMENTS;

/**
 * @author Octavian Ciubotaru
 */
public final class ActivityInterchangeUtils {

    private static final Logger logger = Logger.getLogger(ActivityInterchangeUtils.class);

    public static final String WORKSPACE_PREFIX = "workspacePrefix";
    public static final String ACTIVITY_FM_ID = "activityFMId";

    private ActivityInterchangeUtils() {
    }

    /**
     * Imports or Updates an activity
     * @param newJson new activity configuration
     * @param update flags whether this is an import or an update request
     * @param rules activity import rules
     * @param endpointContextPath full API method path where this method has been called
     *
     * @return latest project overview or an error if invalid configuration is received
     */
    public static JsonApiResponse<ActivitySummary> importActivity(Map<String, Object> newJson, boolean update,
            ActivityImportRules rules, String endpointContextPath) {

        // Load the enumerator for the FM template associated to the activity's workspace (AMPOFFLINE-1562)
        APIField activityField;
        Long fmId = getFMTemplateId(newJson);
        if (fmId != null) {
            activityField = AmpFieldsEnumerator.getEnumerator(fmId).getActivityField();
        } else {
            activityField = AmpFieldsEnumerator.getEnumerator().getActivityField();
        }

        StringBuffer sourceURL = TLSUtils.getRequest().getRequestURL();

        return new ActivityImporter(activityField, rules)
                .importOrUpdate(newJson, update, endpointContextPath, sourceURL.toString())
                .getResult();
    }

    private static Long getFMTemplateId(Map<String, Object> newJson) {
        if (AmpClientModeHolder.isOfflineClient()) {
            Workspace team = TeamUtil.getWorkspace(Long.parseLong(newJson.get("team").toString()));
            return team.getFmTemplate() != null ? team.getFmTemplate().getId() : null;
        }
        return null;
    }

    /**
     * Validates fields filter
     * @param filterJson
     * @param fields
     * @return ApiErrorResponse if error detected or null
     */
    public static ApiErrorResponse validateFilterActivityFields(Map<String, Object> filterJson, List<APIField> fields) {
        List<String> filteredItems = new ArrayList<String>();

        if (filterJson != null) {
            try {
                filteredItems = (List<String>) filterJson.get(ActivityEPConstants.FILTER_FIELDS);
                if (filteredItems == null) {
                    return getFilterValidationError();
                }
            } catch (Exception e) {
                logger.warn("Error in validating fields of the filter attribute. " + e.getMessage());
                return getFilterValidationError();
            }
        }

        try {
            for (String field : filteredItems) {
                PossibleValuesEnumerator.INSTANCE.getPossibleValuesForField(field, fields);
            }
        } catch (ApiRuntimeException e) {
            return ApiError.toError(e.getUnwrappedError().toString());
        }

        return null;
    }

    private static ApiErrorResponse getFilterValidationError() {
        String message = "Invalid filter. The usage should be {\"" + ActivityEPConstants.FILTER_FIELDS
                + "\" : [\"field1\", \"field2\", ..., \"fieldn\"]}";
        return ApiError.toError(message);
    }

    /**
     * @param containerReq
     * @return true if request is valid to view an activity
     */
    public static boolean isViewableActivity(ContainerRequest containerReq) {
        Long id = getRequestId(containerReq);
        // we reuse the same approach as the one done by Project List EP
        // however there are some known issues: AMP-20496
        return id != null && ProjectList.getViewableActivityIds(TeamUtil.getCurrentMember()).contains(id);
    }

    private static Long getRequestId(ContainerRequest containerReq) {
        List<PathSegment> paths = containerReq.getPathSegments();
        Long id = null;
        if (paths != null && paths.size() > 0) {
            PathSegment segment = paths.get(paths.size() - 1);
            if (StringUtils.isNumeric(segment.getPath())) {
                id = Long.valueOf(segment.getPath());
            }
        }
        return id;
    }

    public static ActivityInformation getActivityInformation(Long projectId) {
        AmpActivityVersion project = loadActivity(projectId);
        AmpActivityVersion previousActivity = ActivityUtil.getPreviousVersion(projectId);
        ActivityInformation activityInformation = new ActivityInformation(projectId);
        TeamMember tm = (TeamMember) TLSUtils.getRequest().getSession().getAttribute(Constants.CURRENT_MEMBER);
        if (project.getTeam().getTeamLead() != null) {
            User activityWorkspaceTeamLead = project.getTeam().getTeamLead().getUser();
            activityInformation.setActivityWorkspaceLeadData(activityWorkspaceTeamLead.getFirstNames() + " "
                    + activityWorkspaceTeamLead.getLastName() + " " + activityWorkspaceTeamLead.getEmail());
        }
        activityInformation.setActivityWorkspace(project.getTeam());


        if (tm != null) {

            activityInformation.setDaysForAutomaticValidation(ActivityUtil.daysToValidation(project));

            AmpTeamMember ampCurrentMember = TeamMemberUtil.getAmpTeamMember(tm.getMemberId());
            activityInformation.setTeamMember(new TeamMemberInformation(ampCurrentMember));

            boolean isCurrentWorkspaceManager = ampCurrentMember.getAmpMemberRole().getTeamHead();
            boolean isPartOfMamanagetmentWorkspace = ampCurrentMember.getAmpTeam().getAccessType()
                    .equalsIgnoreCase(Constants.ACCESS_TYPE_MNGMT);


            activityInformation.setUpdateCurrentVersion(isCurrentWorkspaceManager && !isPartOfMamanagetmentWorkspace);
            activityInformation.setVersionHistory(ActivityUtil.getActivityHistories(projectId));

            activityInformation.setValidationStatus(ActivityUtil.getValidationStatus(project, tm));
            if (activityInformation.getValidationStatus() == ValidationStatus.AUTOMATIC_VALIDATION) {
                activityInformation.setDaysForAutomaticValidation(ActivityUtil.daysToValidation(project));
            }
            activityInformation.setShowChangeSummary(previousActivity != null);
        } else {
            // if not logged in but the show version history in public preview is on, then we should show
            // version history information
            if (FeaturesUtil.isVisibleFeature("Version History")) {
                activityInformation.setVersionHistory(ActivityUtil.getActivityHistories(projectId));
                activityInformation.setUpdateCurrentVersion(false);
            }
            activityInformation.setShowChangeSummary(previousActivity != null
                    && FeaturesUtil.isVisibleField("Show Change Summary"));

        }

        activityInformation.setAmpActiviylastVersionId(ActivityVersionUtil.getLastVersionForVersion(projectId));

        return activityInformation;
    }

    public static boolean canViewActivityIfCreatedInPrivateWs(ContainerRequest containerReq) {
        Long id = getRequestId(containerReq);
        AmpActivityVersion project = loadActivity(id);
        TeamMember tm = (TeamMember) TLSUtils.getRequest().getSession().getAttribute(Constants.CURRENT_MEMBER);
        return !(project.getTeam().getIsolated() && (tm == null || !tm.getTeamId().equals(project.getTeam().
                getAmpTeamId())));
    }


    public static Collection<Map<String, Object>> getActivitiesByIds(List<String> ids, ActivityFilterIdEnum discriminator) {
        Map<String, Map<String, Object>> jsonActivities = new HashMap<>();
        ActivityExporter exporter = new ActivityExporter(null);
        // TODO report duplicate/empty amp-ids?
        Set<String> uniqueAmpIds = new HashSet(ids);
        uniqueAmpIds.remove("");
        ids = new ArrayList<>(uniqueAmpIds);
        // temporary until the root cause for stale cache is fixed
        PersistenceManager.getSession().setCacheMode(CacheMode.REFRESH);

        for (int fromIndex = 0; fromIndex < ids.size(); fromIndex += ActivityEPConstants.BATCH_DB_QUERY_SIZE) {
            int end = Math.min(ids.size(), fromIndex + ActivityEPConstants.BATCH_DB_QUERY_SIZE);
            List<String> currentAmpIds = ids.subList(fromIndex, end);
            List<AmpActivityVersion> activities = new ArrayList<>();
            switch (discriminator){
                case AMP_ID: {
                    activities = ActivityUtil.getActivitiesByAmpIds(currentAmpIds);
                    break;
                }
                case BUDGET_CODE_PROJECT: {
                    activities = ActivityUtil.getActivitiesByBudgetCodeProject(currentAmpIds);
                    break;
                }
            }

            activities.forEach(activity -> {
                String ampId = activity.getAmpId();
                Map<String, Object> result = new LinkedHashMap<>();
                try {

                    // AMPOFFLINE-1528
                    ActivityUtil.setCurrentWorkspacePrefixIntoRequest(activity);

                    ActivityUtil.initializeForApi(activity);
                    Long fmId = activity.getTeam().getFmTemplate() != null
                            ? activity.getTeam().getFmTemplate().getId()
                            : null;
                    result = exporter.export(activity, fmId);
                } catch (Exception e) {
                    result.put(EPConstants.ERROR, ApiError.toError(
                            GenericErrors.INTERNAL_ERROR.withDetails(e.getMessage())).getErrors());
                    result.put(ActivityEPConstants.AMP_ID_FIELD_NAME, ampId);
                    logger.error(e.getMessage());
                    e.printStackTrace();
                } finally {
                    PersistenceManager.getSession().evict(activity);
                }
                jsonActivities.put(ampId, result);
            });
            PersistenceManager.getSession().clear();
        }
        reportActivitiesNotFound(uniqueAmpIds, jsonActivities, discriminator);
        // Always succeed on normal exit, no matter if some activities export failed
        EndpointUtils.setResponseStatusMarker(HttpServletResponse.SC_OK);
        return jsonActivities.values();
    }

    /**
     * Batch export of activities by amp-ids
     * @param ampIds
     * @return
     */
    public static Collection<Map<String, Object>> getActivitiesByAmpIds(List<String> ampIds) {
        return getActivitiesByIds(ampIds, ActivityFilterIdEnum.AMP_ID);
    }

    public static Collection<Map<String, Object>> getActivitiesByBudgetCodeProjectIds(List<String> projectCodeIds) {
        return getActivitiesByIds(projectCodeIds, ActivityFilterIdEnum.BUDGET_CODE_PROJECT);
    }

    private static void reportActivitiesNotFound(Set<String> ampIds,
                                                 Map<String, Map<String, Object>> processedActivities,
                                                 ActivityFilterIdEnum discriminator) {
        if (processedActivities.size() != ampIds.size()) {
            ampIds.removeAll(processedActivities.keySet());
            ampIds.forEach(ampId -> {
                Map<String, Object> notFoundJson = new LinkedHashMap<>();
                notFoundJson.put(EPConstants.ERROR, ApiError.toError(ActivityErrors.ACTIVITY_NOT_FOUND).getErrors());
                switch (discriminator){
                    case AMP_ID: {
                        notFoundJson.put(ActivityEPConstants.AMP_ID_FIELD_NAME, ampId);
                        break;
                    }
                    case BUDGET_CODE_PROJECT: {
                        notFoundJson.put(ActivityEPConstants.PROJECT_CODE_FIELD_NAME, ampId);
                        break;
                    }
                }

                processedActivities.put(ampId, notFoundJson);
            });
        }
    }

    public static Map<String, Object> getActivityByAmpId(String ampId, boolean isOffLineClientCall) {
        Long activityId = ActivityUtil.findActivityIdByAmpId(ampId);
        if (activityId == null) {
            ApiErrorResponseService.reportResourceNotFound(ActivityErrors.ACTIVITY_NOT_FOUND.withDetails(ampId));
        }
        return getActivity(activityId, isOffLineClientCall);
    }

    /**
     * Activity Export as JSON
     *
     * @param projectId is amp_activity_id
     * @return
     */
    public static Map<String, Object> getActivity(Long projectId, boolean isOfflineClientCall) {
        Map<String, Object> activity = getActivity(projectId, null);
        if (!isOfflineClientCall
                && FeaturesUtil.isVisibleModule("/REPORTING/Activity Preview/Hide Documents if no donor")) {
            filterPropertyBasedOnUserPermission(activity, projectId);
        }
            return activity;
    }

    private static void filterPropertyBasedOnUserPermission(Map<String, Object> activity, Long projectId) {
        final Long donorRole = DbUtil.getAmpRole(Constants.FUNDING_AGENCY).getAmpRoleId();
        UserSessionInformation userInformation = SecurityService.getInstance().getUserSessionInformation();
        if (userInformation != null) {
            User user = UserUtils.getUser(userInformation.getUserId());
            if (user != null) {
                if (user.getAssignedOrgs() != null && !user.getAssignedOrgs().isEmpty()) {
                    if (!userBelongToExemptOrgForDocumentVisualization(user)) {
                        List organizationIds = user.getAssignedOrgs().stream()
                                .map(ampOrganisation -> ampOrganisation.getAmpOrgId()).collect(Collectors.toList());
                        List<AmpOrgRole> ampOrgRoles =
                                ActivityUtil.getAmpRolesForActivityAndOrganizationsAndRole(projectId,
                                        organizationIds, donorRole);
                        if (ampOrgRoles == null || ampOrgRoles.size() == 0) {
                            activity.replace(ACTIVITY_DOCUMENTS, null);
                        }
                    }
                } else {
                    activity.remove(ACTIVITY_DOCUMENTS);
                }
            }
        }
    }

    private static boolean userBelongToExemptOrgForDocumentVisualization(User user) {
        return user.getAssignedOrgs().stream()
                .filter(ampOrganisation
                        -> ampOrganisation.getIdentifier().equals(
                                FeaturesUtil.getGlobalSettingValueLong(EXEMPT_ORGANIZATION_DOCUMENTS)))
                .count() > 0;
    }

    public static AmpActivityVersion loadActivity(Long actId) {
        try {
            if (PersistenceManager.getSession().get(AmpActivityVersion.class, actId) == null) {
                ApiErrorResponseService.reportResourceNotFound(
                        ActivityErrors.ACTIVITY_NOT_FOUND.withDetails(actId.toString()));
            }

            return ActivityUtil.loadActivity(actId);
        } catch (DgException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Activity Export as JSON
     *
     * @param projectId is amp_activity_id
     * @param filter is the JSON with a list of fields
     * @return
     */
    public static Map<String, Object> getActivity(Long projectId, Map<String, Object> filter) {
        if (PersistenceManager.getSession().get(AmpActivityVersion.class, projectId) == null) {
            ApiErrorResponseService.reportResourceNotFound(
                    ActivityErrors.ACTIVITY_NOT_FOUND.withDetails(projectId.toString()));
        }

        return getActivity(loadActivity(projectId), filter);
    }

    /**
     * Activity Export as JSON
     *
     * @param activity is the activity
     * @param filter is the JSON with a list of fields
     * @return Json Activity
     */
    public static Map<String, Object> getActivity(AmpActivityVersion activity, Map<String, Object> filter) {
        try {
            ActivityExporter exporter = new ActivityExporter(filter);
            Long fmId = activity.getTeam().getFmTemplate() != null ? activity.getTeam().getFmTemplate().getId() : null;
            Map<String, Object> activityMap = exporter.export(activity, fmId);
            return activityMap;
        } catch (Exception e) {
            logger.error("Error in loading activity. " + e.getMessage());
            throw new RuntimeException(e);
        }
    }

    /**
     * @param apb AmpAnnualProject budget having amount, currency and date
     * @parm toCurrCode target currency code to which apb amount would be calculated
     * @return double the amount in toCurrCode
     */
    public static Double doPPCCalculations(AmpAnnualProjectBudget apb, String toCurrCode) {
        DecimalWraper calculatedAmount = new DecimalWraper();

        if (apb.getAmpCurrencyId() != null && apb.getYear() != null && apb.getAmount() != null && toCurrCode != null) {
            String frmCurrCode = apb.getAmpCurrencyId().getCurrencyCode();
            java.sql.Date dt = new java.sql.Date(apb.getYear().getTime());
            Double amount = apb.getAmount();

            double frmExRt = Util.getExchange(frmCurrCode, dt);
            double toExRt = frmCurrCode.equalsIgnoreCase(toCurrCode) ? frmExRt : Util.getExchange(toCurrCode, dt);

            calculatedAmount = CurrencyWorker.convertWrapper(amount, frmExRt, toExRt, dt);
        } else {
            logger.error("Some info is missed in PPC Calculations");
        }

        return calculatedAmount.doubleValue();
    }

    public enum ActivityFilterIdEnum {
        AMP_ID, BUDGET_CODE_PROJECT
    }

}
