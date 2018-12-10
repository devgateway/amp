package org.digijava.kernel.ampapi.endpoints.activity;

import static java.util.function.Function.identity;
import static java.util.stream.Collectors.toMap;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import javax.servlet.http.HttpServletResponse;
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

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.dgfoundation.amp.algo.AmpCollections;
import org.digijava.kernel.ampapi.endpoints.activity.preview.PreviewActivityFunding;
import org.digijava.kernel.ampapi.endpoints.activity.preview.PreviewActivityService;
import org.digijava.kernel.ampapi.endpoints.activity.preview.PreviewWorkspace;
import org.digijava.kernel.ampapi.endpoints.activity.utils.AmpMediaType;
import org.digijava.kernel.ampapi.endpoints.activity.utils.ApiCompat;
import org.digijava.kernel.ampapi.endpoints.common.EndpointUtils;
import org.digijava.kernel.ampapi.endpoints.errors.ErrorReportingEndpoint;
import org.digijava.kernel.ampapi.endpoints.security.AuthRule;
import org.digijava.kernel.ampapi.endpoints.util.ApiMethod;
import org.digijava.kernel.ampapi.endpoints.util.JsonBean;
import org.digijava.kernel.request.TLSUtils;
import org.digijava.kernel.services.AmpFieldsEnumerator;
import org.digijava.module.aim.helper.Constants;
import org.digijava.module.aim.helper.TeamMember;


/**
 * AMP Activity Endpoints for Activity Import / Export
 *
 * @author acartaleanu
 */
@Path("activity")
@Api("activity")
public class InterchangeEndpoints implements ErrorReportingEndpoint {

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
        List<APIField> apiFields = AmpFieldsEnumerator.getPublicEnumerator().getActivityFields();
        List<PossibleValue> possibleValues = InterchangeUtils.possibleValuesFor(fieldName, apiFields);
        MediaType responseType = MediaType.APPLICATION_JSON_TYPE;
        if (AmpMediaType.POSSIBLE_VALUES_V2_JSON.equals(ApiCompat.getRequestedMediaType())) {
            responseType = AmpMediaType.POSSIBLE_VALUES_V2_JSON_TYPE;
        } else {
            possibleValues = PossibleValue.flattenPossibleValues(possibleValues);
        }
        return Response.ok(possibleValues, responseType).build();
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
            @ApiParam(value = "list of fully qualified activity fields")
            List<String> fields) {
        Map<String, List<PossibleValue>> response;
        if (fields == null) {
            response = Collections.emptyMap();
        } else {
            List<APIField> apiFields = AmpFieldsEnumerator.getPublicEnumerator().getActivityFields();
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
    @Path("field/id-values")
    @Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
    @ApiMethod(id = "getIdValues", ui = false)
    @ApiOperation(value = "Returns a list of values for all id of requested fields.",
            notes = "For fields like locations, sectors, programs the object contains the ancestor values.")
    public Map<String, List<FieldIdValue>> getFieldValuesById(
            @ApiParam("List of fully qualified activity fields with list of ids.") Map<String, List<Long>> fieldIds) {
        List<APIField> apiFields = AmpFieldsEnumerator.getPublicEnumerator().getActivityFields();
        Map<String, List<FieldIdValue>> response = InterchangeUtils.getIdValues(fieldIds, apiFields);

        return response;
    }

    @GET
    @Path("fields")
    @Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
    @ApiMethod(id = "getFields", ui = false)
    @ApiOperation(value = "Provides full set of available fields and their settings/rules in a hierarchical "
            + "structure.\n\n"
            + "See [Fields Enumeration Wiki](https://wiki.dgfoundation.org/display/AMPDOC/Fields+enumeration)")
    public List<APIField> getAvailableFields() {
        return AmpFieldsEnumerator.getPublicEnumerator().getActivityFields();
    }
    
    // TODO remove it as part of AMP-25568
    @GET
    @Path("fields-no-workspace")
    @Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
    @ApiMethod(authTypes = AuthRule.AUTHENTICATED, id = "getDefaultFields", ui = false)
    public List<APIField> getAvailableFieldsBasedOnDefaultFM() {
        return getAvailableFields();
    }

    @GET
    @Path("/projects")
    @Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
    @ApiMethod(authTypes = AuthRule.IN_WORKSPACE, id = "getProjectList", ui = false)
    @ApiOperation(
            value = "Returns a JSON object with the list of all projects on the system, including its view and edit "
                    + "status for the current logged user.",
            notes = "If the user can view the project, the 'view' property of the project is set to true. False "
                    + "otherwise. If the user can edit the project, the 'edit' property of the project on the JSON "
                    + "is set to true. False otherwise. Pagination can be used if the parameters are sent on the "
                    + "request. If not parameters are sent, the full list of projects is returned.")
    @ApiResponses(@ApiResponse(code = HttpServletResponse.SC_OK,
            message = "list of JsonBean with all the projects on the system"))
    public Collection<JsonBean> getProjects(
            @ApiParam("Current pagination request reference (random id). It acts as a key for a LRU caching "
                    + "mechanism that holds the full list of projects for the current user. If it is not "
                    + "provided no caching is used")
            @QueryParam("pid")
            String pid,
            @ApiParam("Number of projects to skip") @QueryParam("offset") Integer offset,
            @ApiParam("Number of projects to return") @QueryParam("count") Integer count) {
        TeamMember tm = (TeamMember) TLSUtils.getRequest().getSession().getAttribute(Constants.CURRENT_MEMBER);
        Collection<JsonBean> activityCollection = ProjectList.getActivityList(pid, tm);
        int start = 0;
        int end = activityCollection.size() - 1;
        if (offset != null && count != null && offset < activityCollection.size()) {
            start = offset.intValue();
            if (activityCollection.size() > (offset + count)) {
                end = offset + count;
            }
        }
        return new ArrayList(activityCollection).subList(start, end);
    }

    @GET
    @Path("/projects/{projectId}")
    @Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
    @ApiMethod(id = "getProject", ui = false)
    @ApiOperation("Provides full project information")
    @ApiResponses(@ApiResponse(code = HttpServletResponse.SC_OK,
            message = "project with full set of configured fields and their values"))
    public JsonBean getProject(@ApiParam("project id") @PathParam("projectId") Long projectId) {
        return InterchangeUtils.getActivity(projectId);
    }

    @POST
    @Path("/projects/{projectId}")
    @Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
    @ApiMethod(authTypes = AuthRule.VIEW_ACTIVITY, id = "getProjectsFilter", ui = false)
    @ApiOperation("Provides full project information")
    @ApiResponses(@ApiResponse(code = HttpServletResponse.SC_OK,
            message = "project with full set of configured fields and their values"))
    public JsonBean getProject(
            @ApiParam("project id") @PathParam("projectId") Long projectId,
            @ApiParam("jsonBean with a list of fields that will be displayed") JsonBean filter) {
        return InterchangeUtils.getActivity(projectId, filter);
    }

    @GET
    @Path("/info/{projectId}")
    @Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
    @ApiMethod(authTypes = AuthRule.PUBLIC_VIEW_ACTIVITY, id = "getProjectsFilter", ui = false)
    public Response getProjectInfo(@PathParam("projectId") Long projectId) {
        ActivityInformation response =
        InterchangeUtils.getActivityInformation(projectId);
        return Response.ok(response, MediaType.APPLICATION_JSON_TYPE).build();
    }

    @GET
    @Path("/project")
    @Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
    @ApiMethod(authTypes = AuthRule.AUTHENTICATED, id = "getProjectByAmpId", ui = false)
    @ApiOperation("Retrieve project by AMP Id.")
    @ApiResponses(@ApiResponse(code = HttpServletResponse.SC_OK,
            message = "project with full set of configured fields and their values"))
    public JsonBean getProjectByAmpId(@ApiParam("AMP Id") @QueryParam("amp-id") String ampId) {
        return InterchangeUtils.getActivityByAmpId(ampId);
    }

    @POST
    @Path("/")
    @Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
    @ApiMethod(authTypes = {AuthRule.AUTHENTICATED, AuthRule.AMP_OFFLINE_OPTIONAL}, id = "addProject", ui = false)
    @ApiOperation(
            value = "Imports an activity.",
            notes = "Original behaviour: is_draft field cannot be specified. If saving as draft is allowed then "
                    + "activity will be saved as draft. Otherwise activity will be saved as submitted.\n\n"
                    + "AMP Offline behaviour (User-Agent: AMPOffline): is_draft field is importable and it's"
                    + "value is always honored.")
    @ApiResponses({
            @ApiResponse(code = HttpServletResponse.SC_OK, message = "latest project overview"),
            @ApiResponse(code = HttpServletResponse.SC_BAD_REQUEST,
                    message = "error if invalid configuration is received")})
    public JsonBean addProject(@ApiParam("activity configuration") JsonBean newJson) {
        return InterchangeUtils.importActivity(newJson, false, uri.getBaseUri() + "activity");
    }

    @POST
    @Path("/{projectId}")
    @Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
    @ApiMethod(authTypes = {AuthRule.AUTHENTICATED, AuthRule.AMP_OFFLINE_OPTIONAL}, id = "updateProject", ui = false)
    @ApiOperation(
            value = "Updates an activity",
            notes = "Original behaviour: is_draft field cannot be specified. If existing activity was submitted then "
                    + "at import this status will be kept if possible. Otherwise activity will be saved as draft.\n\n"
                    + "AMP Offline behaviour (User-Agent: AMPOffline): is_draft field is importable and it's value "
                    + "is always honored.\n\n"
                    + "AMP Offline must use optimistic lock in order to update activity. For other clients locking is "
                    + "optional. Locking is achieved by sending last known value of activity_group.version. "
                    + "If activity was updated in meantime then version will be different and subsequent updates "
                    + "will fail with appropriate message.")
    @ApiResponses({
            @ApiResponse(code = HttpServletResponse.SC_OK, message = "latest project overview"),
            @ApiResponse(code = HttpServletResponse.SC_BAD_REQUEST,
                    message = "error if invalid configuration is received")})
    public JsonBean updateProject(
            @ApiParam("the id of the activity which should be updated") @PathParam("projectId") Long projectId,
            @ApiParam("activity configuration") JsonBean newJson) {
        /*
         * Originally it was defined as PUT to avoid these type of issues checked here.
         * But it is more common to use it as POST, so let's then validate
         */ 
        Object internalId = newJson.get(ActivityEPConstants.AMP_ACTIVITY_ID_FIELD_NAME);
        if (!projectId.toString().equals(String.valueOf(internalId))) {
            // invalidating
            String details = "url project_id = " + projectId + ", json " + ActivityEPConstants.AMP_ACTIVITY_ID_FIELD_NAME +
                    " = " + internalId;
            EndpointUtils.addGeneralError(newJson, ActivityErrors.UPDATE_ID_MISMATCH.withDetails(details));
        }

        return InterchangeUtils.importActivity(newJson, true, uri.getBaseUri() + "activity");
    }

    @GET
    @Path("/{project-id}/preview/fundings")
    @Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
    @ApiMethod(id = "getPreviewFundings", ui = false)
    @ApiOperation(value = "Retrieve activity fundings with converted amounts and totals.",
            notes = "This endpoint is used for fetching information about activity funding. "
                    + "The transactions are grouped by transaction type and adjustment type. "
                    + "All the transactions amounts are converted in the specified currency. "
                    + "The response includes subtotals and totals.")
    public PreviewActivityFunding getPreviewFundingInformation(
            @ApiParam("the id of the activity")
            @PathParam("project-id") Long projectId,
            @ApiParam("the currency id in which the amount should be converted")
            @QueryParam(ActivityEPConstants.PREVIEW_CURRENCY_ID) Long currencyId) {
        return PreviewActivityService.getInstance().getPreviewActivityFunding(projectId, currencyId);
    }
    
    @GET
    @Path("/{project-id}/preview/workspaces")
    @Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
    @ApiMethod(id = "getPreviewWorkspaces", ui = false)
    @ApiOperation(value = "Retrieve workspaces where the activity is visible.")
    public List<PreviewWorkspace> getPreviewWorkspaces(
            @ApiParam("the id of the activity")
            @PathParam("project-id") Long projectId) {
        return PreviewActivityService.getInstance().getWorkspaces(projectId);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Class getErrorsClass() {
        return ActivityErrors.class;
    }
}
