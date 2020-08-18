package org.digijava.kernel.ampapi.endpoints.activity;

import static java.util.function.Function.identity;
import static java.util.stream.Collectors.toMap;
import static org.digijava.kernel.translator.util.TrnUtil.PREFIXES;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Objects;

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

import com.fasterxml.jackson.annotation.JsonView;

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
import org.digijava.kernel.ampapi.endpoints.common.JsonApiResponse;
import org.digijava.kernel.ampapi.endpoints.errors.ApiError;
import org.digijava.kernel.ampapi.endpoints.errors.ApiRuntimeException;
import org.digijava.kernel.ampapi.endpoints.security.AuthRule;
import org.digijava.kernel.ampapi.endpoints.util.ApiMethod;
import org.digijava.kernel.request.TLSUtils;
import org.digijava.kernel.services.AmpFieldsEnumerator;
import org.digijava.kernel.translator.TranslatorWorker;
import org.digijava.module.aim.helper.Constants;
import org.digijava.module.aim.helper.TeamMember;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import io.swagger.annotations.Example;
import io.swagger.annotations.ExampleProperty;


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
            value = "Returns a list of JSON objects, each describing a possible value that might be specified "
                    + "in an activity field",
            notes = "If Accept: application/vnd.possible-values-v2+json is used then possible values will be "
                    + "represented in a tree structure.\nIf value can be translated then each possible value "
                    + "will contain value-translations element, a map where key is language code and value is "
                    + "translated value.")
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
    @Path("fields-no-workspace")
    @Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
    @ApiMethod(id = "getDefaultFields", ui = false)
    public List<APIField> getAvailableFieldsBasedOnDefaultFM() {
        return getAvailableFields();
    }

    @POST
    @Path("field/values")
    @Produces({MediaType.APPLICATION_JSON + ";charset=utf-8", AmpMediaType.POSSIBLE_VALUES_V2_JSON})
    @ApiMethod(authTypes = AuthRule.AUTHENTICATED, id = "getMultiValues", ui = false)
    @ApiOperation(
            value = "Returns a list of possible values for each requested field.",
            notes = "If Accept: application/vnd.possible-values-v2+json is used then possible values will be "
                    + "represented in a tree structure.\n\n"
                    + "If value can be translated then each possible value will contain value-translations element, "
                    + "a map where key is language code and value is translated value.\n\n"
                    + "Example body: `[\"fundings~donor_organization_id\", \"approval_status\", \"activity_budget\"]`")
    @ApiResponses(@ApiResponse(code = HttpServletResponse.SC_OK, message = "list of possible values grouped by field"))
    public Response getValues(
            @ApiParam(value = "List of fully qualified activity fields.")
            List<String> fields) {
        Map<String, List<PossibleValue>> response;
        if (fields == null) {
            response = Collections.emptyMap();
        } else {
            List<String> prefixes = TranslatorWorker.getAllPrefixes();
            TLSUtils.getRequest().setAttribute(PREFIXES, prefixes);
            List<APIField> apiFields = AmpFieldsEnumerator.getEnumerator().getActivityFields();
            response = fields.stream()
                    .filter(Objects::nonNull)
                    .distinct()
                    .collect(toMap(identity(), fieldName -> InterchangeUtils.possibleValuesFor(fieldName, apiFields)));
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
            value = "Returns a list of possible values allowed to be showed publicly for each requested field.",
            notes = "If Accept: application/vnd.possible-values-v2+json is used then possible values will be "
                    + "represented in a tree structure.\n\n"
                    + "If value can be translated then each possible value will contain value-translations element, "
                    + "a map where key is language code and value is translated value.\n\n"
                    + "Example body: `[\"fundings~donor_organization_id\", \"approval_status\", \"activity_budget\"]`")
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
    @ApiOperation(value = "Returns a list of values for all id of requested fields.",
            notes = "For fields like locations, sectors, programs the object contains the ancestor values.")
    public Map<String, List<FieldIdValue>> getFieldValuesById(
            @ApiParam("List of fully qualified activity fields with list of ids.") Map<String, List<Long>> fieldIds) {
        List<APIField> apiFields = AmpFieldsEnumerator.getEnumerator().getActivityFields();
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
    public List<APIField> getAvailableFields() {
        return AmpFieldsEnumerator.getEnumerator().getActivityFields();
    }
    
    /**
     * Provides full set of available fields and their settings/rules in a hierarchical structure
     * grouped by workspace member id
     *
     * @param wsMemberIds
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
            value = "Returns a list of all activities summary on the system, including their view and edit rights "
                    + "based on the status for the currently logged in user.",
            notes = "If the user can view the project, the 'view' property of the project is set to true. "
                    + "False otherwise. If the user can edit the project, the 'edit' property of the project "
                    + "on the JSON is set to true. False otherwise. Pagination can be used if the parameters "
                            + "are sent on the request.\nIf not parameters are sent, the full list of projects is "
                            + "returned.")
    @JsonView(ActivityView.List.class)
    public Collection<ActivitySummary> getProjects(
            @ApiParam("Current pagination request reference (random id). It acts as a key for a LRU caching "
                    + "mechanism that holds the full list of projects for the current user. If it is not "
                    + "provided no caching is used")
            @QueryParam("pid")
                    String pid,
            @ApiParam("Number of projects to skip") @QueryParam("offset") Integer offset,
            @ApiParam("Number of projects to return") @QueryParam("count") Integer count) {
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
    @ApiOperation("Provides full activity information.")
    @ApiResponses(@ApiResponse(code = HttpServletResponse.SC_OK, response = SwaggerActivity.class,
    message = "activity with full set of configured fields and their values"))
    public SwaggerActivity getProject(@ApiParam("project id") @PathParam("projectId") Long projectId) {
        Map<String, Object> activity = ActivityInterchangeUtils.getActivity(projectId);
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
    @ApiOperation("Retrieve activity by AMP Id.")
    @ApiResponses(@ApiResponse(code = HttpServletResponse.SC_OK, response = SwaggerActivity.class,
    message = "activity with full set of configured fields and their values"))
    public SwaggerActivity getProjectByAmpId(@ApiParam("AMP Id") @QueryParam("amp-id") String ampId) {
        Map<String, Object> activity = ActivityInterchangeUtils.getActivityByAmpId(ampId);
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
    @ApiOperation(value = "Retrieve workspaces where the activity is visible.")
    public List<PreviewWorkspace> getPreviewWorkspaces(
            @ApiParam("the id of the activity")
            @PathParam("projectId") Long projectId) {
        return PreviewActivityService.getInstance().getWorkspaces(projectId);
    }

}
