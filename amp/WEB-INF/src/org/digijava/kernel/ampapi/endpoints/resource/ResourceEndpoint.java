package org.digijava.kernel.ampapi.endpoints.resource;

import static java.util.Collections.emptyMap;
import static java.util.function.Function.identity;
import static java.util.stream.Collectors.toMap;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.fasterxml.jackson.annotation.JsonView;
import com.sun.jersey.core.header.FormDataContentDisposition;
import com.sun.jersey.multipart.FormDataParam;

import org.apache.commons.io.FileUtils;
import org.digijava.kernel.ampapi.endpoints.activity.APIWorkspaceMemberFieldList;
import org.digijava.kernel.ampapi.endpoints.activity.PossibleValue;
import org.digijava.kernel.ampapi.endpoints.activity.PossibleValuesEnumerator;
import org.digijava.kernel.ampapi.endpoints.activity.field.APIField;
import org.digijava.kernel.ampapi.endpoints.common.JsonApiResponse;
import org.digijava.kernel.ampapi.endpoints.errors.ApiError;
import org.digijava.kernel.ampapi.endpoints.errors.ApiRuntimeException;
import org.digijava.kernel.ampapi.endpoints.resource.dto.AmpResource;
import org.digijava.kernel.ampapi.endpoints.resource.dto.ResourceView;
import org.digijava.kernel.ampapi.endpoints.resource.dto.SwaggerListResource;
import org.digijava.kernel.ampapi.endpoints.resource.dto.SwaggerResource;
import org.digijava.kernel.ampapi.endpoints.security.AuthRule;
import org.digijava.kernel.ampapi.endpoints.util.ApiMethod;
import org.digijava.kernel.services.AmpFieldsEnumerator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

/**
 * @author Viorel Chihai
 */
@Path("resource")
@Api("resource")
public class ResourceEndpoint {

    private static final Logger logger = LoggerFactory.getLogger(ResourceEndpoint.class);

    private static ResourceService resourceService = new ResourceService();

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
        return AmpFieldsEnumerator.getAvailableResourceFieldsBasedOnWs(ids);
    }

    @GET
    @Path("fields")
    @Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
    @ApiMethod(id = "getFields", ui = false)
    @ApiOperation(
            value = "Provides full set of available fields and their settings/rules in a hierarchical structure.",
            notes = "Return JSON with fields information. See "
                    + "[Fields Enumeration Wiki](https://wiki.dgfoundation.org/display/AMPDOC/Fields+enumeration)")
    public List<APIField> getAvailableFields() {
        return AmpFieldsEnumerator.getEnumerator().getResourceFields();
    }

    @POST
    @Path("field/values")
    @Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
    @ApiMethod(id = "getResourceMultiValues", ui = false)
    @ApiOperation(
            value = "Returns a list of possible values for each requested field.",
            notes = "If value can be translated then each possible value will contain value-translations element, "
                    + "a map where key is language code and value is translated value.")
    public Map<String, List<PossibleValue>> getValues(
            @ApiParam("list of fully qualified resource fields") List<String> fields) {
        Map<String, List<PossibleValue>> response;
        if (fields == null) {
            response = emptyMap();
        } else {
            List<APIField> apiFields = AmpFieldsEnumerator.getEnumerator().getResourceFields();
            response = fields.stream()
                    .filter(Objects::nonNull)
                    .distinct()
                    .collect(toMap(identity(), fieldName -> possibleValuesFor(fieldName, apiFields)));
        }
        return response;
    }

    private List<PossibleValue> possibleValuesFor(String fieldName, List<APIField> apiFields) {
        return PossibleValuesEnumerator.INSTANCE.getPossibleValuesForField(fieldName, apiFields);
    }

    @GET
    @Path("{uuid}")
    @Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
    @ApiMethod(id = "getResource", ui = false)
    @ApiOperation("Retrieve resource by uuid")
    @ApiResponses({
        @ApiResponse(code = HttpServletResponse.SC_OK, reference = "AmpResource_Full",
                message = "resource with all fields"),
        @ApiResponse(code = HttpServletResponse.SC_BAD_REQUEST, reference = "JsonApiResponse",
                message = "error if invalid configuration is received")})
    @JsonView(ResourceView.Full.class)
    public JsonApiResponse<AmpResource> getResource(@PathParam("uuid") String uuid) {
        return resourceService.getResource(uuid);
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
    @ApiMethod(authTypes = AuthRule.AUTHENTICATED, id = "getAllResources", ui = false)
    @ApiOperation(value = "Retrieve all resources from AMP.")
    @ApiResponses({
        @ApiResponse(code = HttpServletResponse.SC_OK, response = SwaggerListResource.class,
                message = "list of resources with full information"),
        @ApiResponse(code = HttpServletResponse.SC_BAD_REQUEST, reference = "JsonApiResponse",
                message = "error if a probel encountered")})
    public List<JsonApiResponse> getAllResources() {
        return resourceService.getAllResources();
    }

    @POST
    @Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
    @ApiMethod(id = "getAllResourcesByIds", ui = false)
    @ApiOperation("Retrieve resources from AMP.")
    @ApiResponses({
        @ApiResponse(code = HttpServletResponse.SC_OK, response = SwaggerListResource.class,
                message = "list of resources with full information"),
        @ApiResponse(code = HttpServletResponse.SC_BAD_REQUEST, reference = "JsonApiResponse",
                message = "error if a probel encountered")})
    public List<JsonApiResponse> getAllResources(List<String> uuids) {
        return resourceService.getAllResources(uuids);
    }

    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
    @ApiMethod(authTypes = {AuthRule.AUTHENTICATED, AuthRule.AMP_OFFLINE_OPTIONAL}, id = "createResource", ui = false)
    @ApiOperation(value = "Create new web link resource.",
            notes = "Returns brief representation of resource.\n\n"
                    + "<h3>Sample request body:</h3><pre>\n"
                    + "{\n"
                    + "  \"title\": \"Resource title\",\n"
                    + "  \"description\": \"Resource description\",\n"
                    + "  \"note\": \"Resource note\",\n"
                    + "  \"web_link\": \"https://sample.resource.com/\"\n"
                    + "}\n"
                    + "</pre>")
    @ApiResponses({
        @ApiResponse(code = HttpServletResponse.SC_OK, response = AmpResource.class,
                message = "the brief representationresource"),
        @ApiResponse(code = HttpServletResponse.SC_BAD_REQUEST, reference = "JsonApiResponse_Link",
                message = "error if invalid configuration is received")})
    @JsonView(ResourceView.Link.class)
    public JsonApiResponse<AmpResource> createResource(@ApiParam("resource configuration") SwaggerResource resource) {
        return new ResourceImporter().createResource(resource.getMap()).getResult();
    }

    @PUT
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    @Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
    @ApiMethod(authTypes = {AuthRule.AUTHENTICATED, AuthRule.AMP_OFFLINE_OPTIONAL}, 
                    id = "createResourceWithDoc", ui = false)
    @ApiOperation(value = "Create new web link or document resource.",
            notes = "Returns brief representation of resource.\n\n"
                    + "<h3>Sample resource parameter:</h3><pre>\n"
                    + "{\n"
                    + "  \"title\": \"Resource title\",\n"
                    + "  \"description\": \"Resource description\",\n"
                    + "  \"note\": \"Resource note\"\n"
                    + "}\n"
                    + "</pre>")
    @ApiResponses({
        @ApiResponse(code = HttpServletResponse.SC_OK, response = AmpResource.class,
                message = "the brief representationresource"),
        @ApiResponse(code = HttpServletResponse.SC_BAD_REQUEST, reference = "JsonApiResponse_File-or-Link",
                message = "error if invalid configuration is received")})
    @JsonView({ ResourceView.File.class, ResourceView.Link.class })
    public JsonApiResponse<AmpResource> createDocResource(
            @FormDataParam("resource") @ApiParam(value = "resource configuration", type = "SwaggerResource")
            SwaggerResource resource,
            @FormDataParam("file") InputStream uploadedInputStream,
            @FormDataParam("file") FormDataContentDisposition fileDetail) {

        if (resource == null) {
            throw new ApiRuntimeException(Response.Status.BAD_REQUEST, ApiError.toError(
                    "Parameter 'resource' is not specified or Content-Type for 'resource' is wrong."));
        }

        File file = null;
        JerseyFileAdapter formFile = null;
        try {
            if (uploadedInputStream != null) {
                file = File.createTempFile("createResourceWithDoc", null);
                FileUtils.copyInputStreamToFile(uploadedInputStream, file);
                formFile = new JerseyFileAdapter(fileDetail, file);
            }
            return new ResourceImporter().createResource(resource.getMap(), formFile).getResult();
        } catch (IOException e) {
            logger.error("Failed to process file.", e);
            throw new ApiRuntimeException(Response.Status.BAD_REQUEST,
                    ApiError.toError("Failed to process 'file' parameter."));
        } finally {
            FileUtils.deleteQuietly(file);
        }
    }

}
