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

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.sun.jersey.core.header.FormDataContentDisposition;
import com.sun.jersey.multipart.FormDataParam;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.apache.commons.io.FileUtils;
import org.digijava.kernel.ampapi.endpoints.activity.APIField;
import org.digijava.kernel.services.AmpFieldsEnumerator;
import org.digijava.kernel.ampapi.endpoints.activity.PossibleValue;
import org.digijava.kernel.ampapi.endpoints.activity.PossibleValuesEnumerator;
import org.digijava.kernel.ampapi.endpoints.errors.ApiError;
import org.digijava.kernel.ampapi.endpoints.errors.ApiErrorMessage;
import org.digijava.kernel.ampapi.endpoints.errors.ApiRuntimeException;
import org.digijava.kernel.ampapi.endpoints.errors.ErrorReportingEndpoint;
import org.digijava.kernel.ampapi.endpoints.security.AuthRule;
import org.digijava.kernel.ampapi.endpoints.util.ApiMethod;
import org.digijava.kernel.ampapi.endpoints.util.JsonBean;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Viorel Chihai
 */
@Path("resource")
@Api("resource")
public class ResourceEndpoint implements ErrorReportingEndpoint {

    private static final Logger logger = LoggerFactory.getLogger(ResourceEndpoint.class);
    
    @GET
    @Path("fields")
    @Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
    @ApiMethod(authTypes = AuthRule.AUTHENTICATED, id = "getFields", ui = false)
    @ApiOperation(
            value = "Provides full set of available fields and their settings/rules in a hierarchical structure.",
            notes = "Return JSON with fields information. See "
                    + "[Fields Enumeration Wiki](https://wiki.dgfoundation.org/display/AMPDOC/Fields+enumeration)")
    public List<APIField> getAvailableFields() {
        return AmpFieldsEnumerator.getPublicEnumerator().getResourceFields();
    }
    
    @POST
    @Path("field/values")
    @Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
    @ApiMethod(authTypes = AuthRule.AUTHENTICATED, id = "getResourceMultiValues", ui = false)
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
            List<APIField> apiFields = AmpFieldsEnumerator.getPublicEnumerator().getResourceFields();
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
    @ApiOperation(
            value = "Retrieve resource by uuid.",
            notes = "<h3>Sample response:</h3><pre>\n"
                    + " {\n"
                    + "    \"uuid\": \"05a2f2d4-58f5-4198-8a05-cf42a758ce85\",\n"
                    + "    \"title\": \"fda\",\n"
                    + "    \"file_name\": null,\n"
                    + "    \"web_link\": \"https://www.postgresql.org/docs/9.2/static/sql-createcast.html\",\n"
                    + "    \"description\": \"fdas\",\n"
                    + "    \"note\": \"fda\",\n"
                    + "    \"type\": 50,\n"
                    + "    \"url\": \"/contentrepository/downloadFile.do?uuid=05a2f2d4-58f5-4198-8a05-cf42a758ce85\",\n"
                    + "    \"year_of_publication\": \"2002\",\n"
                    + "    \"adding_date\": \"2018-05-03T15:03:40.607+0300\",\n"
                    + "    \"file_size\": 0,\n"
                    + "    \"public\": false,\n"
                    + "    \"private\": true,\n"
                    + "    \"creator_email\": \"atl@amp.org\",\n"
                    + "    \"team\": null,\n"
                    + "    \"team_member\": 14\n"
                    + " }\n"
                    + " </pre>")
    public JsonBean getResource(@PathParam("uuid") String uuid) {
        return ResourceUtil.getResource(uuid);
    }
    
    @GET
    @Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
    @ApiMethod(authTypes = AuthRule.AUTHENTICATED, id = "getAllResources", ui = false)
    @ApiOperation("Retrieve all resources from AMP.")
    public List<JsonBean> getAllResources() {
        return ResourceUtil.getAllResources();
    }
    
    @POST
    @Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
    @ApiMethod(id = "getAllResourcesByIds", ui = false)
    @ApiOperation("Retrieve resources from AMP.")
    public List<JsonBean> getAllResources(List<String> uuids) {
        return ResourceUtil.getAllResources(uuids);
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

    public JsonBean createResource(JsonBean resource) {
        ResourceImporter importer = new ResourceImporter();
        List<ApiErrorMessage> errors = importer.createResource(resource);
        return ResourceUtil.getImportResult(importer.getResource(), importer.getNewJson(), errors);
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
    public JsonBean createDocResource(
            @FormDataParam("resource") JsonBean resource,
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

            ResourceImporter importer = new ResourceImporter();
            List<ApiErrorMessage> errors = importer.createResource(resource, formFile);
            return ResourceUtil.getImportResult(importer.getResource(), importer.getNewJson(), errors);
        } catch (IOException e) {
            logger.error("Failed to process file.", e);
            throw new ApiRuntimeException(Response.Status.BAD_REQUEST,
                    ApiError.toError("Failed to process 'file' parameter."));
        } finally {
            FileUtils.deleteQuietly(file);
        }
    }

    @Override
    public Class<?> getErrorsClass() {
        return ResourceErrors.class;
    }
    
}
