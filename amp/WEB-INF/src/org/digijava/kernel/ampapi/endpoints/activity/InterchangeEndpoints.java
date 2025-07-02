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
    @ApiOperation(
            value = "Get field values by their IDs using a specific FM template",
            notes = "This endpoint works like `/field/id-values` but allows you to specify a Feature Manager (FM) template ID.\n\n"
                    + "**Path Parameter:**\n"
                    + "- `fmId`: The ID of the Feature Manager template to use for field definitions\n\n"
                    + "**Request Body:**\n"
                    + "- Send a JSON object where keys are field names and values are arrays of IDs\n"
                    + "- Example: `{\"locations~location\": [1, 2], \"sectors~sector\": [5, 6]}`\n\n"
                    + "**Response Features:**\n"
                    + "- For hierarchical fields (locations, sectors, programs), the response includes ancestor values\n"
                    + "- Each value includes its ID, name, and any hierarchical information\n\n"
                    + "**When to Use:**\n"
                    + "- Use this endpoint when you need field values according to a specific FM template configuration")
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
    @ApiOperation(
            value = "Get complete list of activity fields and their configuration",
            notes = "This endpoint provides a comprehensive list of all available activity fields with their settings and validation rules.\n\n"
                    + "**Query Parameters:**\n"
                    + "- `fmId` (optional): Feature Manager template ID to get fields specific to that template\n\n"
                    + "**Response Structure:**\n"
                    + "- Returns a hierarchical structure of all fields\n"
                    + "- Each field includes its properties, validation rules, and dependencies\n"
                    + "- Parent-child relationships between fields are preserved\n\n"
                    + "**Common Use Cases:**\n"
                    + "- Building dynamic forms based on the field configuration\n"
                    + "- Understanding field requirements before submitting data\n"
                    + "- Discovering available fields and their relationships\n\n"
                    + "For more details, see the [Fields Enumeration Wiki](https://wiki.dgfoundation.org/display/AMPDOC/Fields+enumeration)")
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
            value = "Get a list of all activities with permission information",
            notes = "This endpoint retrieves a summary of all activities in the system along with the current user's permissions for each activity.\n\n"
                    + "**Permission Information:**\n"
                    + "- `view`: `true` if the user can view the activity, `false` otherwise\n"
                    + "- `edit`: `true` if the user can edit the activity, `false` otherwise\n\n"
                    + "**Pagination:**\n"
                    + "- Use `offset` and `count` parameters to paginate through large result sets\n"
                    + "- Example: `offset=0&count=20` returns the first 20 activities\n"
                    + "- If pagination parameters are omitted, all activities are returned (not recommended for large datasets)\n\n"
                    + "**Caching:**\n"
                    + "- The `pid` parameter enables caching of the full activity list\n"
                    + "- Use a consistent `pid` value across requests to benefit from caching\n"
                    + "- Example: `pid=page1` for the first page, `pid=page2` for the second page, etc.\n\n"
                    + "**Performance Tips:**\n"
                    + "- Always use pagination for better performance with large datasets\n"
                    + "- Use the `pid` parameter to take advantage of caching")
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
                value = "Get complete details of a specific activity by ID",
                notes = "This endpoint retrieves all information about a single activity identified by its internal ID.\n\n"
                        + "**Path Parameter:**\n"
                        + "- `projectId`: The internal system ID of the activity to retrieve\n\n"
                        + "**Response Content:**\n"
                        + "- Returns a complete JSON object with all available fields and their values\n"
                        + "- Includes all configured fields for the activity based on the current Feature Manager settings\n"
                        + "- Hierarchical data (like funding information) is properly nested in the response\n\n"
                        + "**Common Use Cases:**\n"
                        + "- Viewing all details of a specific activity\n"
                        + "- Retrieving an activity for editing\n"
                        + "- Getting a complete snapshot of an activity's current state\n\n"
                        + "**Note:** If you only need specific fields, consider using the POST version of this endpoint with a field filter.")
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
                value = "Get activity details using AMP ID instead of system ID",
                notes = "This endpoint works like `GET /projects/{projectId}` but uses the AMP ID (business identifier) instead of the internal system ID.\n\n"
                        + "**Query Parameter:**\n"
                        + "- `amp-id`: The AMP ID (business identifier) of the activity to retrieve\n\n"
                        + "**Response Content:**\n"
                        + "- Returns a complete JSON object with all available fields and their values\n"
                        + "- Identical to the response from `GET /projects/{projectId}`\n\n"
                        + "**When to Use This Endpoint:**\n"
                        + "- When you have the AMP ID from reports or external references\n"
                        + "- When working with business identifiers rather than system IDs\n"
                        + "- When integrating with systems that reference activities by their AMP ID\n\n"
                        + "**Example Request:**\n"
                        + "- `GET /rest/activity/project?amp-id=872329912`")
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
            value = "Create a new activity by importing its data",
            notes = "This endpoint allows you to create a new activity by providing its complete data structure.\n\n"
                    + "**Request Body:**\n"
                    + "- Send a complete activity object with all required fields\n"
                    + "- The structure should match what's returned by the GET endpoints\n"
                    + "- Include the `is_draft` field to indicate if this is a draft activity\n\n"
                    + "**Query Parameters:**\n"
                    + "- `can-downgrade-to-draft` (boolean): If `true`, allows saving as draft when validation fails for submission\n"
                    + "- `process-approval-fields` (boolean): If `true`, processes approval fields (use with caution)\n"
                    + "- `track-editors` (boolean): If `true`, uses the provided created_by/modified_by values instead of current user\n\n"
                    + "**Draft Handling:**\n"
                    + "- Draft saving is only allowed if it's also possible in the AMP Activity Form\n"
                    + "- When `is_draft=false` but required fields are missing, the activity will be:\n"
                    + "  - Saved as draft if `can-downgrade-to-draft=true`\n"
                    + "  - Rejected if `can-downgrade-to-draft=false`\n\n"
                    + "**Important Notes:**\n"
                    + "- Only process approval fields if you know how to properly handle them\n"
                    + "- All required fields must be provided according to the current Feature Manager configuration")
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
            value = "Update an existing activity by ID",
            notes = "This endpoint allows you to update an existing activity by providing its complete data structure.\n\n"
                    + "**Path Parameter:**\n"
                    + "- `projectId`: The internal system ID of the activity to update\n\n"
                    + "**Request Body:**\n"
                    + "- Send a complete activity object with all required fields\n"
                    + "- Must include the same `internal_id` as the `projectId` in the path\n"
                    + "- Include the `is_draft` field to indicate if this is a draft activity\n\n"
                    + "**Query Parameters:**\n"
                    + "- `can-downgrade-to-draft` (boolean): If `true`, allows saving as draft when validation fails for submission\n"
                    + "- `process-approval-fields` (boolean): If `true`, processes approval fields (use with caution)\n"
                    + "- `track-editors` (boolean): If `true`, uses the provided created_by/modified_by values instead of current user\n\n"
                    + "**Draft Handling:**\n"
                    + "- Draft saving is only allowed if it's also possible in the AMP Activity Form\n"
                    + "- When `is_draft=false` but required fields are missing, the activity will be:\n"
                    + "  - Saved as draft if `can-downgrade-to-draft=true`\n"
                    + "  - Rejected if `can-downgrade-to-draft=false`\n\n"
                    + "**Versioning and Locking:**\n"
                    + "- Only the latest activity version can be updated\n"
                    + "- Stale activities are detected based on activity ID and activity_group.version\n"
                    + "- The activity is optimistically locked during the update process to prevent conflicts\n\n"
                    + "**Important Notes:**\n"
                    + "- Only process approval fields if you know how to properly handle them\n"
                    + "- All required fields must be provided according to the current Feature Manager configuration")
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
    @ApiOperation(
            value = "Get activity funding information with currency conversion",
            notes = "This endpoint provides detailed funding information for an activity with amounts converted to a specified currency.\n\n"
                    + "**Path Parameter:**\n"
                    + "- `projectId`: The internal system ID of the activity\n\n"
                    + "**Query Parameter:**\n"
                    + "- `currency-id`: The ID of the currency to convert all amounts to\n\n"
                    + "**Response Features:**\n"
                    + "- Transactions are grouped by transaction type (commitments, disbursements, etc.)\n"
                    + "- Within each type, transactions are further grouped by adjustment type\n"
                    + "- All transaction amounts are converted to the specified currency\n"
                    + "- Response includes subtotals for each group and grand totals\n\n"
                    + "**Common Use Cases:**\n"
                    + "- Generating financial reports in a specific currency\n"
                    + "- Analyzing funding data with consistent currency values\n"
                    + "- Previewing how funding data will appear in reports")
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
                value = "Get all workspaces that can view a specific activity",
                notes = "This endpoint returns a list of all workspaces that have visibility access to the specified activity.\n\n"
                        + "**Path Parameter:**\n"
                        + "- `projectId`: The internal system ID of the activity\n\n"
                        + "**Response Content:**\n"
                        + "- Returns an array of workspace objects\n"
                        + "- Each workspace object includes its ID, name, and other relevant details\n"
                        + "- Only includes workspaces that have permission to view the activity\n\n"
                        + "**Common Use Cases:**\n"
                        + "- Understanding the visibility scope of an activity\n"
                        + "- Determining which teams have access to a particular activity\n"
                        + "- Troubleshooting visibility issues across workspaces\n\n"
                        + "**Note:** This endpoint requires authentication and appropriate permissions to access the activity information.")
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
            value = "Import multiple activities in a single request (bulk import)",
            notes = "This endpoint allows you to create or update multiple activities at once, with optional asynchronous processing.\n\n"
                    + "**Request Body:**\n"
                    + "- Send an array of activity objects\n"
                    + "- Each activity object follows the same format as used in `POST /` and `POST /{projectId}`\n"
                    + "- Maximum 20 activities per request (requests with more will be rejected)\n\n"
                    + "**Query Parameters:**\n"
                    + "- `can-downgrade-to-draft` (boolean): If `true`, allows saving as draft when validation fails\n"
                    + "- `process-approval-fields` (boolean): If `true`, processes approval fields (use with caution)\n"
                    + "- `track-editors` (boolean): If `true`, uses the provided created_by/modified_by values\n\n"
                    + "**Synchronous vs. Asynchronous Processing:**\n"
                    + "- **Synchronous** (default): Returns a list of import/update results immediately\n"
                    + "- **Asynchronous**: Add header `Prefer: respond-async` to process in background\n"
                    + "  - Returns immediately with a `location` header containing the URL to check results\n"
                    + "  - Use the returned URL to poll for completion status\n\n"
                    + "**When to Use Asynchronous Mode:**\n"
                    + "- For larger batches (approaching the 20 activity limit)\n"
                    + "- When immediate response is not required\n"
                    + "- To avoid timeouts with complex activities\n\n"
                    + "**Note:** Each activity is validated individually. Some may succeed while others fail.")
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
            value = "Get results of an asynchronous bulk import operation",
            notes = "This endpoint allows you to check the status and results of a previously initiated asynchronous bulk import.\n\n"
                    + "**Path Parameter:**\n"
                    + "- `result-id`: The unique identifier returned in the location header of the async bulk import request\n\n"
                    + "**Response Headers:**\n"
                    + "- `X-Async-Status`: Indicates the current status of the operation (`RUNNING` or `COMPLETED`)\n\n"
                    + "**Response Content:**\n"
                    + "- When processing is complete: Returns an array of import results, one for each activity\n"
                    + "- Each result includes success/failure status and any validation errors\n"
                    + "- When still processing: Returns partial results of activities processed so far\n\n"
                    + "**Common Use Cases:**\n"
                    + "- Polling for completion of a bulk import operation\n"
                    + "- Retrieving results after an asynchronous import\n"
                    + "- Checking which activities succeeded or failed in a bulk operation\n\n"
                    + "**Note:** It's recommended to poll this endpoint at reasonable intervals (e.g., every few seconds) until processing is complete.")
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
