package org.digijava.kernel.ampapi.endpoints.activity;

import com.fasterxml.jackson.annotation.JsonView;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import io.swagger.annotations.Example;
import io.swagger.annotations.ExampleProperty;
import org.dgfoundation.amp.algo.AmpCollections;
import org.digijava.kernel.ampapi.endpoints.activity.dto.ActivityInformation;
import org.digijava.kernel.ampapi.endpoints.activity.dto.ActivitySummary;
import org.digijava.kernel.ampapi.endpoints.activity.dto.ActivityView;
import org.digijava.kernel.ampapi.endpoints.activity.dto.SwaggerActivity;
import org.digijava.kernel.ampapi.endpoints.activity.field.APIField;
import org.digijava.kernel.ampapi.endpoints.activity.preview.PreviewActivityErrors;
import org.digijava.kernel.ampapi.endpoints.activity.preview.PreviewActivityFunding;
import org.digijava.kernel.ampapi.endpoints.activity.preview.PreviewActivityService;
import org.digijava.kernel.ampapi.endpoints.activity.preview.PreviewWorkspace;
import org.digijava.kernel.ampapi.endpoints.activity.utils.AmpMediaType;
import org.digijava.kernel.ampapi.endpoints.activity.utils.ApiCompat;
import org.digijava.kernel.ampapi.endpoints.async.AsyncActivityIndirectProgramUpdaterService;
import org.digijava.kernel.ampapi.endpoints.async.AsyncApiService;
import org.digijava.kernel.ampapi.endpoints.async.AsyncResult;
import org.digijava.kernel.ampapi.endpoints.async.AsyncResultCacher;
import org.digijava.kernel.ampapi.endpoints.async.AsyncStatus;
import org.digijava.kernel.ampapi.endpoints.common.JsonApiResponse;
import org.digijava.kernel.ampapi.endpoints.errors.ApiError;
import org.digijava.kernel.ampapi.endpoints.errors.ApiErrorResponseService;
import org.digijava.kernel.ampapi.endpoints.errors.ApiRuntimeException;
import org.digijava.kernel.ampapi.endpoints.security.AuthRule;
import org.digijava.kernel.ampapi.endpoints.util.ApiMethod;
import org.digijava.kernel.ampapi.filters.AmpClientModeHolder;
import org.digijava.kernel.request.TLSUtils;
import org.digijava.kernel.services.AmpFieldsEnumerator;
import org.digijava.module.aim.helper.Constants;
import org.digijava.module.aim.helper.TeamMember;
import org.digijava.module.aim.util.ActivityUtil;
import org.springframework.security.web.util.UrlUtils;

import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import static java.util.function.Function.identity;
import static java.util.stream.Collectors.toMap;
import static javax.ws.rs.core.Response.Status.BAD_REQUEST;
import static org.digijava.kernel.ampapi.endpoints.activity.ActivityEPConstants.AMP_ID_FIELD_NAME;
import static org.digijava.kernel.ampapi.endpoints.activity.ActivityEPConstants.MAX_BULK_ACTIVITIES_ALLOWED;
import static org.digijava.kernel.ampapi.endpoints.activity.ActivityEPConstants.X_ASYNC_RESULT_ID;
import static org.digijava.kernel.ampapi.endpoints.activity.ActivityEPConstants.X_ASYNC_STATUS;
import static org.digijava.kernel.ampapi.endpoints.async.AsyncActivityIndirectProgramUpdaterService.PROGRAM_UPDATER_KEY;


/**
 * AMP Activity Endpoints for Activity Import / Export
 *
 * @author acartaleanu
 */
@Path("activity")
@Api("activity")
public class InterchangeEndpoints {

    @Context
    private UriInfo uri;

    @GET
    @Path("fields/{fieldName}")
    @Produces({MediaType.APPLICATION_JSON + ";charset=utf-8", AmpMediaType.POSSIBLE_VALUES_V2_JSON})
    @ApiMethod(authTypes = AuthRule.IN_WORKSPACE, id = "getValues", ui = false)
    @ApiOperation(
            value = "Get possible values for a specific activity field",
            notes = "Returns a list of all possible values that can be used for the specified activity field.\n\n"
                    + "**Response Format Options:**\n"
                    + "- **Default format**: Flat list of values\n"
                    + "- **Tree structure**: Use Accept header: `application/vnd.possible-values-v2+json`\n\n"
                    + "**Translations:**\n"
                    + "- If a value can be translated, it will include a `value-translations` object\n"
                    + "- The `value-translations` object maps language codes to translated values\n\n"
                    + "**Example usage:** Get possible values for the 'locations~location' field")
    @ApiResponses(@ApiResponse(code = HttpServletResponse.SC_OK, message = "list of possible values",
            response = PossibleValue.class, responseContainer = "List"))
    public Response getPossibleValuesFlat(
            @PathParam("fieldName")
            @ApiParam(value = "fully qualified activity field", example = "locations~location")
                    String fieldName) {
        List<APIField> apiFields = AmpFieldsEnumerator.getEnumerator().getActivityFields();
        List<PossibleValue> possibleValues = InterchangeUtils.possibleValuesFor(fieldName, apiFields);
        MediaType responseType = MediaType.APPLICATION_JSON_TYPE;
        if (AmpMediaType.POSSIBLE_VALUES_V2_JSON.equals(ApiCompat.getRequestedMediaType())) {
            responseType = AmpMediaType.POSSIBLE_VALUES_V2_JSON_TYPE;
        } else {
            possibleValues = PossibleValue.flattenPossibleValues(possibleValues);
        }
        return Response.ok(possibleValues, responseType).build();
    }

    // TODO TO be removed after AMP-29486 is merged into FUTURE.
    // Restored so the new preview works until AMP-29486 is done.
    @GET
    @Path("fields-no-workspace/{id}")
    @Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
    @ApiMethod(id = "getDefaultFields", ui = false)
    public List<APIField> getAvailableFieldsBasedOnDefaultFM(@ApiParam(value = "FM id", required = false)
                                                             @PathParam("id") Long id) {
        return getAvailableFields(id);
    }

    // TODO TO be removed after AMP-29486 is merged into FUTURE.
    // Restored so the new preview works until AMP-29486 is done.
    @GET
    @Path("fields-no-workspace")
    @Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
    @ApiMethod(id = "getDefaultFields", ui = false)
    public List<APIField> getAvailableFieldsBasedOnDefaultFM() {
        return getAvailableFields(null);
    }

    @POST
    @Path("field/values")
    @Produces({MediaType.APPLICATION_JSON + ";charset=utf-8", AmpMediaType.POSSIBLE_VALUES_V2_JSON})
    @ApiMethod(authTypes = AuthRule.AUTHENTICATED, id = "getMultiValues", ui = false)
    @ApiOperation(
            value = "Get possible values for multiple activity fields at once",
            notes = "This endpoint allows you to retrieve possible values for multiple fields in a single request.\n\n"
                    + "**Request Body:**\n"
                    + "- Send an array of field names as JSON\n"
                    + "- Example: `[\"fundings~donor_organization_id\", \"approval_status\", \"activity_budget\"]`\n\n"
                    + "**Response Format Options:**\n"
                    + "- **Default format**: Flat list of values for each field\n"
                    + "- **Tree structure**: Use Accept header: `application/vnd.possible-values-v2+json`\n\n"
                    + "**Response Structure:**\n"
                    + "- Returns an object where keys are the requested field names\n"
                    + "- Each key contains an array of possible values for that field\n"
                    + "- Translated values include a `value-translations` object mapping language codes to translations")
    @ApiResponses(@ApiResponse(code = HttpServletResponse.SC_OK, message = "list of possible values grouped by field"))
    public Response getValues(
            @ApiParam(value = "List of fully qualified activity fields.")
                    List<String> fields) {
        Map<String, List<PossibleValue>> response;
        if (fields == null) {
            response = Collections.emptyMap();
        } else {
            ActivityUtil.loadWorkspacePrefixesIntoRequest();

            // Load an enumerator for each FM template (AMPOFFLINE-1562)
            List<APIField> mergedList = new ArrayList<>();
            AmpFieldsEnumerator.getAllEnumerators().forEach((i, item) -> {
                mergedList.addAll(item.getActivityFields());
            });

            response = fields.stream()
                    .filter(Objects::nonNull)
                    .distinct()
                    .collect(toMap(identity(), fieldName -> InterchangeUtils.possibleValuesFor(fieldName, mergedList)));
        }
        MediaType responseType = MediaType.APPLICATION_JSON_TYPE;
        if (AmpMediaType.POSSIBLE_VALUES_V2_JSON.equals(ApiCompat.getRequestedMediaType())) {
            responseType = AmpMediaType.POSSIBLE_VALUES_V2_JSON_TYPE;
        } else {
            response = AmpCollections.remap(response, PossibleValue::flattenPossibleValues);
        }
        return Response.ok(response, responseType).build();
    }

    @POST
    @Path("field/values/public")
    @Produces({MediaType.APPLICATION_JSON + ";charset=utf-8", AmpMediaType.POSSIBLE_VALUES_V2_JSON})
    @ApiMethod(id = "getMultiValues", ui = false)
    @ApiOperation(
            value = "Get publicly available values for multiple activity fields",
            notes = "This endpoint works like `/field/values` but is restricted to fields that are allowed to be shown publicly.\n\n"
                    + "**Important:** Only fields defined in `PUBLIC_ACTIVITY_FIELDS` are allowed. Other fields will result in an error.\n\n"
                    + "**Request Body:**\n"
                    + "- Send an array of field names as JSON\n"
                    + "- Example: `[\"fundings~donor_organization_id\", \"approval_status\", \"activity_budget\"]`\n\n"
                    + "**Response Format Options:**\n"
                    + "- **Default format**: Flat list of values for each field\n"
                    + "- **Tree structure**: Use Accept header: `application/vnd.possible-values-v2+json`\n\n"
                    + "**Response Structure:**\n"
                    + "- Returns an object where keys are the requested field names\n"
                    + "- Each key contains an array of possible values for that field\n"
                    + "- Translated values include a `value-translations` object mapping language codes to translations")
    @ApiResponses(@ApiResponse(code = HttpServletResponse.SC_OK, message = "list of possible values "
            + "allowed to be showed publicly grouped by field"))
    public Response getValuesPublic(
            @ApiParam(value = "list of fully qualified activity fields")
                    List<String> fields) {
        if (!ActivityEPConstants.PUBLIC_ACTIVITY_FIELDS.containsAll(fields)) {
            throw new ApiRuntimeException(Response.Status.BAD_REQUEST,
                    ApiError.toError(PreviewActivityErrors.FIELD_NOT_ALLOWED));
        }

        return getValues(fields);
    }

    @POST
    @Path("field/id-values")
    @Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
    @ApiMethod(id = "getIdValues", ui = false)
    @ApiOperation(
            value = "Get field values by their IDs",
            notes = "This endpoint allows you to retrieve specific field values when you know their IDs.\n\n"
                    + "**Request Body:**\n"
                    + "- Send a JSON object where keys are field names and values are arrays of IDs\n"
                    + "- Example: `{\"locations~location\": [1, 2], \"sectors~sector\": [5, 6]}`\n\n"
                    + "**Response Features:**\n"
                    + "- For hierarchical fields (locations, sectors, programs), the response includes ancestor values\n"
                    + "- Each value includes its ID, name, and any hierarchical information\n\n"
                    + "**Common Use Case:**\n"
                    + "- Use this endpoint when you have IDs from another source and need to get their full information")
    public Map<String, List<FieldIdValue>> getFieldValuesById(
            @ApiParam("List of fully qualified activity fields with list of ids.") Map<String, List<Long>> fieldIds) {
        return getFieldValues(null, fieldIds);
    }

    @POST
    @Path("field/id-values/{fmId}")
    @Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
    @ApiMethod(id = "getIdValues", ui = false)
    @ApiOperation(value = "Returns a list of values for all id of requested fields.",
            notes = "For fields like locations, sectors, programs the object contains the ancestor values.")
    public Map<String, List<FieldIdValue>> getFieldValuesByIdWithFM(
            @ApiParam(value = "FM id", required = true) @PathParam("fmId") Long id,
            @ApiParam("List of fully qualified activity fields with list of ids.") Map<String, List<Long>> fieldIds) {
        return getFieldValues(id, fieldIds);
    }

    private Map<String, List<FieldIdValue>> getFieldValues(Long id, Map<String, List<Long>> fieldIds) {
        List<APIField> apiFields = null;
        if (id != null) {
            apiFields = AmpFieldsEnumerator.getEnumerator(id).getActivityFields();
        } else {
            apiFields = AmpFieldsEnumerator.getEnumerator().getActivityFields();
        }
        Map<String, List<FieldIdValue>> response = InterchangeUtils.getIdValues(fieldIds, apiFields);
        return response;
    }

    @GET
    @Path("fields")
    @Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
    @ApiMethod(id = "getFields", ui = false)
    @ApiOperation(value = "Returns the full list of activity fields.",
            notes = "Provides full set of available fields and their settings/rules in a hierarchical structure.\n\n"
                    + "See [Fields Enumeration Wiki](https://wiki.dgfoundation.org/display/AMPDOC/Fields+enumeration)")
    public List<APIField> getAvailableFields(@ApiParam(value = "FM id") @QueryParam("fmId") Long fmId) {
        if (fmId != null) {
            return AmpFieldsEnumerator.getEnumerator(fmId).getActivityFields();
        }
        return AmpFieldsEnumerator.getEnumerator().getActivityFields();
    }

    /**
     * Provides full set of available fields and their settings/rules in a hierarchical structure
     * grouped by workspace member id
     *
     * @param ids
     * @return JSON with fields information grouped by ws-member-ids
     * @see <a href="https://wiki.dgfoundation.org/display/AMPDOC/Fields+enumeration">Fields Enumeration Wiki<a/>
     */
    @POST
    @Path("ws-member-fields")
    @Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
    @ApiMethod(id = "getAvailableFieldsBasedOnWs", ui = false)
    public List<APIWorkspaceMemberFieldList>
    getAvailableFieldsBasedOnWs(@ApiParam(value = "List of WS ids", required = true) List<Long> ids) {
        return AmpFieldsEnumerator.getAvailableFieldsBasedOnWs(ids, AmpFieldsEnumerator.TYPE_ACTIVITY);
    }

    @GET
    @Path("/projects")
    @Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
    @ApiMethod(authTypes = AuthRule.IN_WORKSPACE, id = "getProjectList", ui = false)
    @ApiOperation(
            value = "Returns a list of all activities/projects in the system with their permission information",
            notes = "This endpoint provides a summary of all activities in the system along with permission information for the current user:\n\n"
                    + "- The 'view' property is set to true if the user can view the project, false otherwise\n"
                    + "- The 'edit' property is set to true if the user can edit the project, false otherwise\n\n"
                    + "Pagination is supported through the offset and count parameters. If these parameters are not provided, "
                    + "the full list of projects is returned.\n\n"
                    + "For better performance with large datasets, use the pagination parameters.")
    @ApiResponses({
            @ApiResponse(code = HttpServletResponse.SC_OK,
                    message = "Returns a collection of activity summaries with permission information"),
            @ApiResponse(code = HttpServletResponse.SC_FORBIDDEN,
                    message = "User does not have permission to access the workspace")
    })
    @JsonView(ActivityView.List.class)
    public Collection<ActivitySummary> getProjects(
            @ApiParam(value = "Pagination request reference ID - used as a key for caching the full list of projects. "
                    + "If not provided, no caching is used", example = "page1")
            @QueryParam("pid") String pid,
            @ApiParam(value = "Number of projects to skip for pagination", example = "0")
            @QueryParam("offset") Integer offset,
            @ApiParam(value = "Maximum number of projects to return", example = "20")
            @QueryParam("count") Integer count) {
        TeamMember tm = (TeamMember) TLSUtils.getRequest().getSession().getAttribute(Constants.CURRENT_MEMBER);
        Collection<ActivitySummary> activityCollection = ProjectList.getActivityList(pid, tm);
        int start = 0;
        int end = activityCollection.size() - 1;
        if (offset != null && count != null && offset < activityCollection.size()) {
            start = offset.intValue();
            if (activityCollection.size() > (offset + count)) {
                end = offset + count;
            }
        }
        return new ArrayList<>(activityCollection).subList(start, end);
    }

    @GET
    @Path("/projects/{projectId}")
    @Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
    @ApiMethod(id = "getProject", ui = false)
    @ApiOperation(
                value = "Retrieves complete information for a specific activity/project",
                notes = "Returns a comprehensive JSON object containing all available fields and their values for the specified project. " +
                        "This endpoint is useful when you need to access the complete details of an activity.")
    @ApiResponses({
            @ApiResponse(code = HttpServletResponse.SC_OK, response = SwaggerActivity.class,
                    message = "Returns a complete activity object containing all fields and their values configured in the system"),
            @ApiResponse(code = HttpServletResponse.SC_NOT_FOUND,
                    message = "Activity with the specified ID was not found"),
            @ApiResponse(code = HttpServletResponse.SC_FORBIDDEN,
                    message = "User does not have permission to view this activity")
    })
    public SwaggerActivity getProject(@ApiParam(value = "The unique identifier of the project/activity to retrieve", example = "12345") @PathParam("projectId") Long projectId) {
        Map<String, Object> activity = ActivityInterchangeUtils.getActivity(projectId,
                AmpClientModeHolder.isOfflineClient());
        return new SwaggerActivity(activity);
    }

    @POST
    @Path("/projects/{projectId}")
    @Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
    @ApiMethod(authTypes = AuthRule.VIEW_ACTIVITY, id = "getProjectsFilter", ui = false)
    @ApiOperation("Provides activity information based on requested fields.")
    @ApiResponses(@ApiResponse(code = HttpServletResponse.SC_OK,
            message = "activity with requested fields and their values"))
    public Map<String, Object> getProject(
            @ApiParam("activity id") @PathParam("projectId") Long projectId,
            @ApiParam("List of fields that will be displayed") Map<String, Object> filter) {
        return ActivityInterchangeUtils.getActivity(projectId, filter);
    }

    @GET
    @Path("/info/{projectId}")
    @Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
    @ApiMethod(authTypes = AuthRule.PUBLIC_VIEW_ACTIVITY, id = "getProjectsFilter", ui = false)
    public Response getProjectInfo(@PathParam("projectId") Long projectId) {
        ActivityInformation response =
                ActivityInterchangeUtils.getActivityInformation(projectId);
        return Response.ok(response, MediaType.APPLICATION_JSON_TYPE).build();
    }

    @GET
    @Path("/project")
    @Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
    @ApiMethod(authTypes = AuthRule.AUTHENTICATED, id = "getProjectByAmpId", ui = false)
    @ApiOperation(
                value = "Retrieves an activity using its AMP ID",
                notes = "Returns a complete activity object when provided with a valid AMP ID. " +
                        "This endpoint is particularly useful when you know the AMP ID but not the internal system ID of an activity.")
    @ApiResponses({
            @ApiResponse(code = HttpServletResponse.SC_OK, response = SwaggerActivity.class,
                    message = "Returns a complete activity object containing all fields and their values configured in the system"),
            @ApiResponse(code = HttpServletResponse.SC_NOT_FOUND,
                    message = "Activity with the specified AMP ID was not found"),
            @ApiResponse(code = HttpServletResponse.SC_FORBIDDEN,
                    message = "User does not have permission to view this activity")
    })
    public SwaggerActivity getProjectByAmpId(@ApiParam(value = "The AMP ID of the activity to retrieve - this is a business identifier, not the system ID", example = "872329912") @QueryParam("amp-id") String ampId) {
        Map<String, Object> activity = ActivityInterchangeUtils.getActivityByAmpId(ampId,
                AmpClientModeHolder.isOfflineClient());
        return new SwaggerActivity(activity);
    }

    @POST
    @Path("/projects")
    @Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
    @ApiMethod(authTypes = AuthRule.AUTHENTICATED, id = "getProjectsByAmpIds", ui = false)
    @ApiOperation("Retrieve activities by AMP Ids.")
    @ApiResponses(@ApiResponse(code = HttpServletResponse.SC_OK,
            message = "A list of projects with full set of configured fields and their values. For each amp_id that is "
                    + "invalid or its export failed, the entry will provide only the 'amp_id' and the 'error'",
            examples =
            @Example(value = {
                    @ExampleProperty(
                            mediaType = "application/json;charset=utf-8",
                            value = "[\n  {\n    \"internal_id\": 912,\n    \"amp_id\": \"872329912\",\n    ...\n  }"
                                    + ",\n  "
                                    + "{\n    \"amp_id\": \"invalid\",\n    \"error\": {\n      \"0132\": "
                                    + "[{ \"Activity not found\": null }]\n    }\n  }\n]\n"
                    )
            })
    ))
    public Collection<Map<String, Object>> getProjectsByAmpIds(@ApiParam(value = "List of amp-id", required = true)
                                                                       List<String> ampIds) {
        return ActivityInterchangeUtils.getActivitiesByAmpIds(ampIds);
    }

    @POST
    @Path("/")
    @Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
    @ApiMethod(authTypes = {AuthRule.AUTHENTICATED, AuthRule.AMP_OFFLINE_OPTIONAL}, id = "addProject", ui = false)
    @ApiOperation(
            value = "Imports an activity.",
            notes = "Saving as draft will be allowed only if this is also possible in AMP Activity Form.\n"
                    + "When is_draft is false, but some required fields for submit are invalid/missing, then activity "
                    + "will be saved as draft if can-downgrade-to-draft is true. Otherwise will be rejected.\n\n"
                    + "Request to process approval fields only if you know how to properly handle them.")
    @ApiResponses({
            @ApiResponse(code = HttpServletResponse.SC_OK, reference = "ActivitySummary_Import",
                    message = "the latest project short overview"),
            @ApiResponse(code = HttpServletResponse.SC_BAD_REQUEST, reference = "JsonApiResponse_Import",
                    message = "error if invalid configuration is received")})
    @JsonView(ActivityView.Import.class)
    public JsonApiResponse<ActivitySummary> addProject(
            @ApiParam("can downgrade to draft") @QueryParam("can-downgrade-to-draft") @DefaultValue("false")
                    boolean canDowngradeToDraft,
            @ApiParam("process approval fields") @QueryParam("process-approval-fields") @DefaultValue("false")
                    boolean isProcessApprovalFields,
            @ApiParam("use created_by and modified_by from input instead of user session") @QueryParam("track-editors")
            @DefaultValue("false") boolean isTrackEditors,
            @ApiParam("activity configuration") SwaggerActivity newJson) {

        ActivityImportRules rules = new ActivityImportRules(canDowngradeToDraft, isProcessApprovalFields,
                isTrackEditors);

        return ActivityInterchangeUtils.importActivity(newJson.getMap(), false, rules, uri.getBaseUri() + "activity");
    }

    @POST
    @Path("/{projectId}")
    @Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
    @ApiMethod(authTypes = {AuthRule.AUTHENTICATED, AuthRule.AMP_OFFLINE_OPTIONAL}, id = "updateProject", ui = false)
    @ApiOperation(
            value = "Updates an activity.",
            notes = "Saving as draft will be allowed only if this is also possible in AMP Activity Form. "
                    + "When is_draft is false, but some required fields for submit are invalid/missing, then activity "
                    + "will be saved as draft if can-downgrade-to-draft is true. Otherwise will be rejected.\n\n"
                    + "Request to process approval fields only if you know how to properly handle them.\n"
                    + "Only the latest activity version is allowed to be updated. A stale activity is detected based "
                    + "on activity id and activity_group.version.\n"
                    + "The activity will be optimistically locked during the update process.")
    @ApiResponses({
            @ApiResponse(code = HttpServletResponse.SC_OK, reference = "ActivitySummary_Import",
                    message = "latest project overview"),
            @ApiResponse(code = HttpServletResponse.SC_BAD_REQUEST, reference = "JsonApiResponse_Import",
                    message = "error if invalid configuration is received")})
    @JsonView(ActivityView.Import.class)
    public JsonApiResponse<ActivitySummary> updateProject(
            @ApiParam("the id of the activity which should be updated") @PathParam("projectId") Long projectId,
            @ApiParam("can downgrade to draft") @QueryParam("can-downgrade-to-draft") @DefaultValue("false")
                    boolean canDowngradeToDraft,
            @ApiParam("process approval fields") @QueryParam("process-approval-fields") @DefaultValue("false")
                    boolean isProcessApprovalFields,
            @ApiParam("use created_by and modified_by from input instead of user session") @QueryParam("track-editors")
            @DefaultValue("false") boolean isTrackEditors,
            @ApiParam("activity configuration") SwaggerActivity newJson) {
        /*
         * Originally it was defined as PUT to avoid these type of issues checked here.
         * But it is more common to use it as POST, so let's then validate
         */
        Object internalId = newJson.getMap().get(ActivityEPConstants.AMP_ACTIVITY_ID_FIELD_NAME);
        if (!projectId.toString().equals(String.valueOf(internalId))) {
            // invalidating
            String details = "url project_id = " + projectId + ", json "
                    + ActivityEPConstants.AMP_ACTIVITY_ID_FIELD_NAME + " = " + internalId;
            return new JsonApiResponse<ActivitySummary>(
                    ApiError.toError(ActivityErrors.UPDATE_ID_MISMATCH.withDetails(details)))
                    .addDetail(ActivityEPConstants.ACTIVITY, newJson);
        }

        ActivityImportRules rules = new ActivityImportRules(canDowngradeToDraft, isProcessApprovalFields,
                isTrackEditors);

        ActivityUtil.loadWorkspacePrefixesIntoRequest();
        return ActivityInterchangeUtils.importActivity(newJson.getMap(), true, rules, uri.getBaseUri() + "activity");
    }

    @GET
    @Path("/{projectId}/preview/fundings")
    @Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
    @ApiMethod(id = "getPreviewFundings", ui = false)
    @ApiOperation(value = "Retrieve activity fundings with converted amounts and totals.",
            notes = "This endpoint is used for fetching information about activity funding.\n"
                    + "The transactions are grouped by transaction type and adjustment type.\n"
                    + "All the transactions amounts are converted in the specified currency.\n"
                    + "The response includes subtotals and totals.")
    public PreviewActivityFunding getPreviewFundingInformation(
            @ApiParam("the id of the activity")
            @PathParam("projectId") Long projectId,
            @ApiParam("the currency id in which the amount should be converted")
            @QueryParam(ActivityEPConstants.PREVIEW_CURRENCY_ID) Long currencyId) {
        return PreviewActivityService.getInstance().getPreviewActivityFunding(projectId, currencyId);
    }

    @GET
    @Path("/{projectId}/preview/workspaces")
    @Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
    @ApiMethod(authTypes = AuthRule.AUTHENTICATED, id = "getPreviewWorkspaces", ui = false)
    @ApiOperation(
                value = "Retrieves all workspaces where the specified activity is visible",
                notes = "Returns a list of workspaces that have access to view the specified activity. " +
                        "This is useful for understanding the visibility scope of an activity across different workspaces.")
    @ApiResponses({
            @ApiResponse(code = HttpServletResponse.SC_OK,
                    message = "Returns a list of workspace objects that have visibility to the specified activity"),
            @ApiResponse(code = HttpServletResponse.SC_NOT_FOUND,
                    message = "Activity with the specified ID was not found"),
            @ApiResponse(code = HttpServletResponse.SC_FORBIDDEN,
                    message = "User does not have permission to view this information")
    })
    public List<PreviewWorkspace> getPreviewWorkspaces(
            @ApiParam(value = "The unique identifier of the activity for which to retrieve workspace visibility information", example = "12345")
            @PathParam("projectId") Long projectId) {
        return PreviewActivityService.getInstance().getWorkspaces(projectId);
    }


    @POST
    @Path("/async/bulk")
    @Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
    @ApiMethod(authTypes = {AuthRule.AUTHENTICATED, AuthRule.AMP_OFFLINE_OPTIONAL}, id = "importProjects")
    @ApiOperation(
            value = "Imports asynchronous a list of activities.",
            notes = "The input body is an array of activity objects."
                    + "The format of activity object matches the existing format used by post / and POST /{projectId}."
                    + "If the header Prefer: respond-async is not present, "
                    + "then the endpoint will respond with a list of import/update result."
                    + "If the header Prefer: respond-async is present then an immediate response will be returned."
                    + "The response will contain in headers (location) the url where the results can be retrieved"
                    + "If the size is bigger than 20, the request will be rejected.")
    public Response importProjects(@QueryParam("can-downgrade-to-draft") @DefaultValue("false")
                                           boolean canDowngradeToDraft,
                                   @QueryParam("process-approval-fields") @DefaultValue("false")
                                           boolean isProcessApprovalFields,
                                   @QueryParam("track-editors") @DefaultValue("false") boolean isTrackEditors,
                                   @ApiParam("activity configuration") List<SwaggerActivity> activitiesJson) {

        String resultId = (String) TLSUtils.getRequest().getAttribute("result-id");
        if (activitiesJson != null || !activitiesJson.isEmpty()) {
            if (activitiesJson.size() > MAX_BULK_ACTIVITIES_ALLOWED) {
                ApiErrorResponseService.reportError(BAD_REQUEST, ActivityErrors.BULK_TO_BIG
                        .withDetails("Maximum activities allowed: " + MAX_BULK_ACTIVITIES_ALLOWED));
            }

            ActivityImportRules rules = new ActivityImportRules(canDowngradeToDraft, isProcessApprovalFields,
                    isTrackEditors);
            if (resultId != null) {
                AsyncApiService.getInstance().importActivities(rules, resultId, activitiesJson, uri.getBaseUri());
            } else {
                List<JsonApiResponse<ActivitySummary>> results = new ArrayList<>();

                for (SwaggerActivity act : activitiesJson) {
                    boolean toUpdate = act.getMap().containsKey(AMP_ID_FIELD_NAME);
                    results.add(ActivityInterchangeUtils.importActivity(act.getMap(), toUpdate, rules,
                            uri.getBaseUri() + "activity"));
                }

                return Response.ok(results).build();
            }
        }
        String location = String.format("%s/result/%s", UrlUtils.buildFullRequestUrl(TLSUtils.getRequest()), resultId);
        return Response.ok()
                .header("location", location)
                .build();
    }

    @GET
    @Path("/async/bulk/result/{result-id}")
    @Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
    @ApiMethod(authTypes = {AuthRule.AUTHENTICATED, AuthRule.AMP_OFFLINE_OPTIONAL}, id = "getAsyncResult")
    @ApiOperation(
            value = "Return the results generated by /async/bulk endpoint.")
    public Response getAsyncResult(@PathParam("result-id") String resultId) {

        return buildResultId(resultId);
    }

    @GET
    @Path("/updateMappings/async")
    @Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
    @ApiMethod(authTypes = AuthRule.IN_ADMIN, id = "updateMappings", ui = false)
    public Response updateMappings() {

        String resultId = (String) TLSUtils.getRequest().getAttribute(X_ASYNC_RESULT_ID);
        if (resultId == null) {
            ApiErrorResponseService.reportError(BAD_REQUEST, ActivityErrors.ONLY_SYNC
                    .withDetails("Only sync process is allowed"));
        }
        if (!AsyncResultCacher.canAddAnotherUnique(PROGRAM_UPDATER_KEY)) {
            ApiErrorResponseService.reportError(BAD_REQUEST, ActivityErrors.PROCESS_ALREADY_RUNNING
                    .withDetails("Only one process at a time is allowed"));
        } else {
            // Start the process
            AsyncActivityIndirectProgramUpdaterService.getInstance().
                    updateIndirectPrograms(PROGRAM_UPDATER_KEY + resultId);
        }
        String location = String.format("%s/result/%s", UrlUtils.buildRequestUrl(TLSUtils.getRequest()), resultId);
        return Response.ok()
                .header("location", location)
                .build();
    }

    @GET
    @Path("/updateMappings/async/result/{result-id}")
    @Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
    @ApiMethod(authTypes = AuthRule.IN_ADMIN, id = "updateMappings", ui = false)
    public Response getUpdateMappingsResult(@PathParam("result-id") String resultId) {
        return buildResultId(PROGRAM_UPDATER_KEY + resultId);
    }

    private Response buildResultId(String resultId) {
        AsyncResult asyncResult = AsyncResultCacher.getAsyncResult(resultId);

        if (asyncResult == null) {
            return Response.status(Response.Status.NOT_FOUND)
                    .header(X_ASYNC_STATUS, AsyncStatus.NOT_FOUND)
                    .type(MediaType.APPLICATION_JSON).build();
        }

        Response.ResponseBuilder responseBuilder = Response.status(Response.Status.OK)
                .type(MediaType.APPLICATION_JSON);

        if (asyncResult.getStatus() == AsyncStatus.RUNNING) {
            responseBuilder.header(X_ASYNC_STATUS, AsyncStatus.RUNNING);
        }

        return responseBuilder.entity(asyncResult.getResults()).build();
    }
}
