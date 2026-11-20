package org.digijava.kernel.ampapi.endpoints.resource;

import com.fasterxml.jackson.annotation.JsonView;
import io.swagger.annotations.*;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.input.BoundedInputStream;
import org.apache.struts.upload.FormFile;
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
import org.digijava.kernel.ampapi.endpoints.filetype.FileTypeManager;
import org.digijava.kernel.ampapi.endpoints.filetype.FileTypeValidationResponse;
import org.digijava.kernel.ampapi.endpoints.filetype.FileTypeValidationStatus;
import org.digijava.kernel.services.AmpFieldsEnumerator;
import org.digijava.module.aim.helper.GlobalSettingsConstants;
import org.digijava.module.aim.util.FeaturesUtil;
import org.digijava.module.contentrepository.helper.TemporaryDocumentData;
import org.digijava.module.contentrepository.helper.StagedResourceUploadStore;
import org.digijava.module.contentrepository.util.DocumentManagerUtil;
import org.digijava.module.aim.util.ActivityUtil;
import org.glassfish.jersey.media.multipart.FormDataContentDisposition;
import org.glassfish.jersey.media.multipart.FormDataParam;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import static java.util.Collections.emptyMap;
import static java.util.function.Function.identity;
import static java.util.stream.Collectors.toMap;

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
        return AmpFieldsEnumerator.getAvailableFieldsBasedOnWs(ids, AmpFieldsEnumerator.TYPE_RESOURCE);
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
        ActivityUtil.loadWorkspacePrefixesIntoRequest();
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
    @JsonView({ResourceView.File.class, ResourceView.Link.class})
    public JsonApiResponse<AmpResource> createDocResource(
            @ApiParam(value = "resource configuration", type = "SwaggerResource") @FormDataParam("resource")
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

        @POST
        @Path("stage-upload")
        @Consumes(MediaType.MULTIPART_FORM_DATA)
        @Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
        @ApiMethod(authTypes = AuthRule.AUTHENTICATED, id = "stageResourceUpload", ui = false)
        @ApiOperation("Stage a resource file for a pending activity form")
        public List<StagedResourceUpload> stageResourceUpload(@FormDataParam("file") InputStream uploadedInputStream,
                        @FormDataParam("file") FormDataContentDisposition fileDetail,
                        @QueryParam("replaceUploadId") String replaceUploadId,
                        @Context HttpServletRequest request) {
                if (uploadedInputStream == null || fileDetail == null || fileDetail.getFileName() == null) {
                        throw new WebApplicationException("A file is required.", Response.Status.BAD_REQUEST);
                }

                long maxFileSize = FeaturesUtil.getGlobalSettingValueInteger(GlobalSettingsConstants.CR_MAX_FILE_SIZE)
                                * FileUtils.ONE_MB;
                if (request.getContentLengthLong() > maxFileSize + FileUtils.ONE_MB) {
                        throw new WebApplicationException("The file exceeds the upload size limit.",
                                        Response.Status.REQUEST_ENTITY_TOO_LARGE);
                }

                File stagedFile = null;
                try {
                        stagedFile = File.createTempFile("amp-resource-upload-", ".tmp");
// StagedResourceUploadStore owns cleanup for this temporary file.
                        FileUtils.copyInputStreamToFile(new BoundedInputStream(uploadedInputStream, maxFileSize + 1), stagedFile);
                        if (stagedFile.length() > maxFileSize) {
                                throw new WebApplicationException("The file exceeds the upload size limit.",
                                                Response.Status.REQUEST_ENTITY_TOO_LARGE);
                        }

                        String fileName = new File(fileDetail.getFileName()).getName();
                        if (FeaturesUtil.getGlobalSettingValueBoolean(GlobalSettingsConstants.LIMIT_FILE_TYPE_FOR_UPLOAD)) {
                                FileTypeValidationResponse validation;
                                try (InputStream validationStream = new FileInputStream(stagedFile)) {
                                        validation = FileTypeManager.getInstance().validateFileType(validationStream, fileName);
                                }
                                if (validation.getStatus() != FileTypeValidationStatus.ALLOWED) {
                                        throw new WebApplicationException("The uploaded file type is not allowed.",
                                                        Response.Status.BAD_REQUEST);
                                }
                        }

                        Calendar uploadedAt = Calendar.getInstance();
                        TemporaryDocumentData temporaryDocument = new TemporaryDocumentData();
                        temporaryDocument.setName(fileName);
                        temporaryDocument.setContentType(fileDetail.getType());
                        temporaryDocument.setTrueUploadedFileSize((int) stagedFile.length());
                        temporaryDocument.setFileSize(DocumentManagerUtil.bytesToMega((int) stagedFile.length()));
                        temporaryDocument.setDate(uploadedAt.getTime());
                        temporaryDocument.setYearofPublication(String.valueOf(uploadedAt.get(Calendar.YEAR)));
                        temporaryDocument.setFormFile(new StagedFormFile(stagedFile, fileName, fileDetail.getType()));
                        temporaryDocument.setStagedFile(stagedFile);
                        String uploadId = StagedResourceUploadStore.store(request, temporaryDocument, replaceUploadId);
                        stagedFile = null;

                        return Collections.singletonList(new StagedResourceUpload(uploadId));
                } catch (StagedResourceUploadStore.UploadLimitExceededException e) {
                        throw new WebApplicationException("The session has too many staged uploads. Remove an upload and try again.",
                                        Response.Status.REQUEST_ENTITY_TOO_LARGE);
                } catch (IOException e) {
                        logger.error("Failed to stage resource upload.", e);
                        throw new WebApplicationException("Failed to process the uploaded file.", e,
                                        Response.Status.BAD_REQUEST);
                } finally {
                        FileUtils.deleteQuietly(stagedFile);
                }
        }

        @DELETE
        @Path("stage-upload")
        @ApiMethod(authTypes = AuthRule.AUTHENTICATED, id = "deleteStagedResourceUpload", ui = false)
        @ApiOperation("Delete a staged resource file that is no longer needed")
        public void deleteStagedResourceUpload(@QueryParam("uploadId") String uploadId,
                        @Context HttpServletRequest request) {
                StagedResourceUploadStore.delete(request, uploadId);
        }

        public static class StagedResourceUpload {
                private String uploadId;

                public StagedResourceUpload() {
                }

                public StagedResourceUpload(String uploadId) {
                        this.uploadId = uploadId;
                }

                public String getUploadId() {
                        return uploadId;
                }

                public void setUploadId(String uploadId) {
                        this.uploadId = uploadId;
                }
        }

        private static class StagedFormFile implements FormFile, Serializable {
                private static final long serialVersionUID = 1L;

                private final File file;
                private final String fileName;
                private final String contentType;

                private StagedFormFile(File file, String fileName, String contentType) {
                        this.file = file;
                        this.fileName = fileName;
                        this.contentType = contentType;
                }

                @Override
                public String getContentType() {
                        return contentType;
                }

                @Override
                public void setContentType(String contentType) {
                        throw new UnsupportedOperationException();
                }

                @Override
                public int getFileSize() {
                        return (int) file.length();
                }

                @Override
                public void setFileSize(int fileSize) {
                        throw new UnsupportedOperationException();
                }

                @Override
                public String getFileName() {
                        return fileName;
                }

                @Override
                public void setFileName(String fileName) {
                        throw new UnsupportedOperationException();
                }

                @Override
                public byte[] getFileData() throws IOException {
                        return FileUtils.readFileToByteArray(file);
                }

                @Override
                public InputStream getInputStream() throws IOException {
                        return new FileInputStream(file);
                }

                @Override
                public void destroy() {
                }
        }

}
