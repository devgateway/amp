package org.digijava.kernel.ampapi.endpoints.activity;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.core.PathSegment;

import com.sun.jersey.spi.container.ContainerRequest;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.dgfoundation.amp.Util;
import org.digijava.kernel.ampapi.endpoints.activity.field.APIField;
import org.digijava.kernel.ampapi.endpoints.common.EPConstants;
import org.digijava.kernel.ampapi.endpoints.common.EndpointUtils;
import org.digijava.kernel.ampapi.endpoints.errors.ApiError;
import org.digijava.kernel.ampapi.endpoints.errors.ApiErrorMessage;
import org.digijava.kernel.ampapi.endpoints.errors.ApiErrorResponseService;
import org.digijava.kernel.ampapi.endpoints.errors.ApiRuntimeException;
import org.digijava.kernel.ampapi.endpoints.exception.ApiExceptionMapper;
import org.digijava.kernel.ampapi.endpoints.util.JsonBean;
import org.digijava.kernel.exception.DgException;
import org.digijava.kernel.persistence.PersistenceManager;
import org.digijava.kernel.request.TLSUtils;
import org.digijava.kernel.services.AmpFieldsEnumerator;
import org.digijava.module.aim.dbentity.AmpActivityVersion;
import org.digijava.module.aim.dbentity.AmpAnnualProjectBudget;
import org.digijava.module.aim.dbentity.AmpTeamMember;
import org.digijava.module.aim.helper.Constants;
import org.digijava.module.aim.helper.CurrencyWorker;
import org.digijava.module.aim.helper.TeamMember;
import org.digijava.module.aim.util.ActivityUtil;
import org.digijava.module.aim.util.ActivityVersionUtil;
import org.digijava.module.aim.util.DecimalWraper;
import org.digijava.module.aim.util.FeaturesUtil;
import org.digijava.module.aim.util.TeamMemberUtil;
import org.digijava.module.aim.util.TeamUtil;
import org.digijava.module.aim.util.ValidationStatus;
import org.hibernate.CacheMode;

/**
 * @author Octavian Ciubotaru
 */
public final class ActivityInterchangeUtils {

    private static final Logger logger = Logger.getLogger(ActivityInterchangeUtils.class);

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
    public static JsonBean importActivity(JsonBean newJson, boolean update, ActivityImportRules rules,
            String endpointContextPath) {
        List<APIField> activityFields = AmpFieldsEnumerator.getEnumerator().getActivityFields();
        
        return new ActivityImporter(activityFields, rules)
                .importOrUpdate(newJson, update, endpointContextPath)
                .getResult();
    }

    @SuppressWarnings("unchecked")
    public static boolean validateFilterActivityFields(JsonBean filterJson, JsonBean result, List<APIField> fields) {
        List<String> filteredItems = new ArrayList<String>();

        if (filterJson != null) {
            try {
                filteredItems = (List<String>) filterJson.get(ActivityEPConstants.FILTER_FIELDS);
                if (filteredItems == null) {
                    addFilterValidationErrorToResult(result);

                    return false;
                }
            } catch (Exception e) {
                logger.warn("Error in validating fields of the filter attribute. " + e.getMessage());
                addFilterValidationErrorToResult(result);

                return false;
            }
        }

        try {
            for (String field : filteredItems) {
                PossibleValuesEnumerator.INSTANCE.getPossibleValuesForField(field, fields);
            }
        } catch (ApiRuntimeException e) {
            result.set(EPConstants.ERROR, e.getUnwrappedError());
            return false;
        }

        return true;
    }

    private static void addFilterValidationErrorToResult(JsonBean result) {
        String message = "Invalid filter. The usage should be {\"" + ActivityEPConstants.FILTER_FIELDS
                + "\" : [\"field1\", \"field2\", ..., \"fieldn\"]}";

        result.set(EPConstants.ERROR, ApiError.toError(message).getErrors());
    }

    /**
     * @param teamMember    team member
     * @param activityId    activity id
     * @return true if team member can edit the activity
     */
    public static boolean isEditableActivity(TeamMember teamMember, Long activityId) {
        // we reuse the same approach as the one done by Project List EP
        return activityId != null && ActivityUtil.getEditableActivityIdsNoSession(teamMember).contains(activityId);
    }

    /**
     * @return true if add activity is allowed
     */
    public static boolean addActivityAllowed(TeamMember tm) {
        return tm != null && Boolean.TRUE.equals(tm.getAddActivity())
                && (FeaturesUtil.isVisibleField("Add Activity Button")
                        || FeaturesUtil.isVisibleField("Add SSC Button"));
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

        ActivityInformation activityInformation = new ActivityInformation(projectId);
        TeamMember tm = (TeamMember) TLSUtils.getRequest().getSession().getAttribute(Constants.CURRENT_MEMBER);
        activityInformation.setActivityTeam(project.getTeam());
        if (tm != null) {
            activityInformation.setEdit(isEditableActivity(tm, projectId));
            if (activityInformation.isEdit()) {
                activityInformation.setValidate(ActivityUtil.canValidateActivity(project, tm));
            }
            activityInformation.setValidationStatus(ActivityUtil.getValidationStatus(project, tm));
            if (activityInformation.getValidationStatus() == ValidationStatus.AUTOMATIC_VALIDATION) {
                activityInformation.setDaysForAutomaticValidation(ActivityUtil.daysToValidation(project));
            }

            AmpTeamMember ampCurrentMember = TeamMemberUtil.getAmpTeamMember(tm.getMemberId());

            boolean isCurrentWorkspaceManager = ampCurrentMember.getAmpMemberRole().getTeamHead();
            boolean isPartOfMamanagetmentWorkspace = ampCurrentMember.getAmpTeam().getAccessType()
                    .equalsIgnoreCase(Constants.ACCESS_TYPE_MNGMT);

            activityInformation.setUpdateCurrentVersion(isCurrentWorkspaceManager && !isPartOfMamanagetmentWorkspace);
            activityInformation.setVersionHistory(ActivityUtil.getActivityHistories(projectId));
        } else {
            // if not logged in but the show version history in public preview is on, then we should show
            // version history information
            if (FeaturesUtil.isVisibleFeature("Version History")) {
                activityInformation.setVersionHistory(ActivityUtil.getActivityHistories(projectId));
                activityInformation.setUpdateCurrentVersion(false);
            }
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

    /**
     * Batch export of activities by amp-ids
     * @param ampIds
     * @return
     */
    public static Collection<JsonBean> getActivitiesByAmpIds(List<String> ampIds) {
        Map<String, JsonBean> jsonActivities = new HashMap<>();
        ActivityExporter exporter = new ActivityExporter(null);
        // TODO report duplicate/empty amp-ids?
        Set<String> uniqueAmpIds = new HashSet(ampIds);
        uniqueAmpIds.remove("");
        ampIds = new ArrayList<>(uniqueAmpIds);
        // temporary until the root cause for stale cache is fixed
        PersistenceManager.getSession().setCacheMode(CacheMode.REFRESH);

        for (int fromIndex = 0; fromIndex < ampIds.size(); fromIndex += ActivityEPConstants.BATCH_DB_QUERY_SIZE) {
            int end = Math.min(ampIds.size(), fromIndex + ActivityEPConstants.BATCH_DB_QUERY_SIZE);
            List<String> currentAmpIds = ampIds.subList(fromIndex, end);
            List<AmpActivityVersion> activities = ActivityUtil.getActivitiesByAmpIds(currentAmpIds);
            activities.forEach(activity -> {
                String ampId = activity.getAmpId();
                JsonBean result = new JsonBean();
                try {
                    ActivityUtil.initializeForApi(activity);
                    result = exporter.export(activity);
                } catch (Exception e) {
                    result.set(EPConstants.ERROR, ApiError.toError(
                            ApiExceptionMapper.INTERNAL_ERROR.withDetails(e.getMessage())).getErrors());
                    result.set(ActivityEPConstants.AMP_ID_FIELD_NAME, ampId);
                } finally {
                    PersistenceManager.getSession().evict(activity);
                }
                jsonActivities.put(ampId, result);
            });
            PersistenceManager.getSession().clear();
        }
        reportActivitiesNotFound(uniqueAmpIds, jsonActivities);
        // Always succeed on normal exit, no matter if some activities export failed
        EndpointUtils.setResponseStatusMarker(HttpServletResponse.SC_OK);
        return jsonActivities.values();
    }

    private static void reportActivitiesNotFound(Set<String> ampIds, Map<String, JsonBean> processedActivities) {
        if (processedActivities.size() != ampIds.size()) {
            ampIds.removeAll(processedActivities.keySet());
            ampIds.forEach(ampId -> {
                JsonBean notFoundJson = new JsonBean();
                notFoundJson.set(EPConstants.ERROR, ApiError.toError(ActivityErrors.ACTIVITY_NOT_FOUND).getErrors());
                notFoundJson.set(ActivityEPConstants.AMP_ID_FIELD_NAME, ampId);
                processedActivities.put(ampId, notFoundJson);
            });
        }
    }

    public static JsonBean getActivityByAmpId(String ampId) {
        Long activityId = ActivityUtil.findActivityIdByAmpId(ampId);
        return getActivity(activityId);
    }

    /**
     * Activity Export as JSON
     *
     * @param projectId is amp_activity_id
     * @return
     */
    public static JsonBean getActivity(Long projectId) {
        return getActivity(projectId, null);
    }

    public static AmpActivityVersion loadActivity(Long projectId) {
        AmpActivityVersion activity = null;
        try {
            activity = ActivityUtil.loadActivity(projectId);
            if (activity == null) {
                //so far project will never be null since an exception will be thrown
                //I leave the code prepared to throw the appropriate response code
                ApiErrorResponseService.reportResourceNotFound(ActivityErrors.ACTIVITY_NOT_FOUND);
            }
        } catch (DgException e) {
            throw new RuntimeException(e);
        }
        return activity;
    }

    /**
     * Activity Export as JSON
     *
     * @param projectId is amp_activity_id
     * @param filter is the JSON with a list of fields
     * @return
     */
    public static JsonBean getActivity(Long projectId, JsonBean filter) {
        return getActivity(loadActivity(projectId), filter);
    }

    /**
     * Activity Export as JSON
     *
     * @param activity is the activity
     * @param filter is the JSON with a list of fields
     * @return Json Activity
     */
    public static JsonBean getActivity(AmpActivityVersion activity, JsonBean filter) {
        try {
            ActivityExporter exporter = new ActivityExporter(filter);

            return exporter.export(activity);
        } catch (Exception e) {
            logger.error("Error in loading activity. " + e.getMessage());
            throw new RuntimeException(e);
        }
    }

    /**
     * Gets the value at the specified path from the JSON description of the activity.
     *
     * @param activity a JsonBean description of the activity
     * @param path path to the field
     * @return null if the path abruptly stops before reaching the end, or the value itself,
     * if the end of the path is reached
     */
    public static Object getFieldValuesFromJsonActivity(JsonBean activity, String path) {
        String fieldPath = path;

        JsonBean currentBranch = activity;
        while (fieldPath.contains("~")) {
            String pathSegment = fieldPath.substring(0, fieldPath.indexOf('~'));
            Object obj = currentBranch.get(pathSegment);
            if (obj != null && JsonBean.class.isAssignableFrom(obj.getClass())) {
                currentBranch = (JsonBean) obj;
            } else {
                return null;
            }
            fieldPath = path.substring(fieldPath.indexOf('~') + 1);
        }
        //path is complete, object is set to proper value
        return currentBranch.get(fieldPath);
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
}
