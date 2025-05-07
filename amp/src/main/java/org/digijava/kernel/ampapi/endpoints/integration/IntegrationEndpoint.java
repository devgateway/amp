package org.digijava.kernel.ampapi.endpoints.integration;

import com.sun.jersey.core.header.FormDataContentDisposition;
import com.sun.jersey.multipart.FormDataParam;
import org.digijava.kernel.ampapi.endpoints.activity.utils.AmpMediaType;
import org.digijava.kernel.ampapi.endpoints.errors.ApiError;
import org.digijava.kernel.ampapi.endpoints.errors.ApiErrorResponse;
import org.digijava.kernel.ampapi.endpoints.errors.ApiRuntimeException;
import org.digijava.kernel.ampapi.endpoints.errors.GenericErrors;
import org.digijava.kernel.ampapi.endpoints.integration.dto.DagRunsRequestDTO;
import org.digijava.kernel.ampapi.endpoints.integration.dto.DagRunsResponseDTO;
import org.digijava.kernel.ampapi.endpoints.integration.dto.FileInformationDTO;
import org.digijava.kernel.ampapi.endpoints.integration.dto.FileUploadedResponseDTO;
import org.digijava.kernel.ampapi.endpoints.integration.service.DagService;
import org.digijava.kernel.ampapi.endpoints.integration.service.DagServiceImpl;
import org.digijava.kernel.ampapi.endpoints.integration.service.FileInformationServiceImpl;
import org.digijava.kernel.ampapi.endpoints.integration.service.FileUploaderServiceImpl;
import org.json.JSONArray;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestBody;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Optional;

@Path("integration")
public class IntegrationEndpoint {

    private static final Logger LOGGER = LoggerFactory.getLogger(DagService.class.getName());


    @POST
    @Path("/dag/dagRuns")
    @Produces({MediaType.APPLICATION_JSON + ";charset=utf-8", AmpMediaType.POSSIBLE_VALUES_V2_JSON})
    public DagRunsResponseDTO dagRuns(@RequestBody DagRunsRequestDTO dagRunsRequest) {
        try {
            DagRunsResponseDTO responseDTO = DagServiceImpl.getInstance().dagRuns(dagRunsRequest);
            LOGGER.info("DagRun triggered successfully with payload: {}", dagRunsRequest);
            return responseDTO;

        } catch (Exception e) {
            LOGGER.error("Failed to trigger DagRun with payload: {}", dagRunsRequest, e);
            ApiErrorResponse error = ApiError.toError(GenericErrors.INTERNAL_ERROR.withDetails(e.getMessage()));
            throw new ApiRuntimeException(Response.Status.INTERNAL_SERVER_ERROR, error);
        }
    }

    @POST
    @Path("/upload/file")
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    @Produces({MediaType.APPLICATION_JSON + ";charset=utf-8", AmpMediaType.POSSIBLE_VALUES_V2_JSON})
    public FileUploadedResponseDTO uploadFile(@FormDataParam("file") InputStream fileInputStream,
                                              @FormDataParam("file") FormDataContentDisposition fileMetaData) {
        try {
            FileUploadedResponseDTO dto = FileUploaderServiceImpl.getInstance().uploadFile(fileInputStream, fileMetaData);
            return dto;
        } catch (IOException e) {
            ApiErrorResponse error = ApiError.toError(GenericErrors.INTERNAL_ERROR.withDetails(e.getMessage()));
            throw new ApiRuntimeException(Response.Status.INTERNAL_SERVER_ERROR, error);
        }
    }

    @GET
    @Path("/file-information")
    @Produces({MediaType.APPLICATION_JSON + ";charset=utf-8", AmpMediaType.POSSIBLE_VALUES_V2_JSON})
    public List<FileInformationDTO> getAllFileInformation(@QueryParam(value = "page") Integer page,
                                                          @QueryParam(value = "size") Integer size) {
        if (page == null) {
            page = 0;
        }
        if (size == null) {
            size = 10;
        }
        return FileInformationServiceImpl.getInstance().getAllFileInformation(page, size);
    }

    @GET
    @Path("/file-information/{id}")
    @Produces({MediaType.APPLICATION_JSON + ";charset=utf-8", AmpMediaType.POSSIBLE_VALUES_V2_JSON})
    public FileInformationDTO getFileInformationById(@PathParam("id") Long id) {
        Optional<FileInformationDTO> response = FileInformationServiceImpl.getInstance().getFileInformationById(id);
        if (response.isPresent()) {
            return response.get();
        } else {
            throw new ApiRuntimeException(Response.Status.NOT_FOUND, ApiError.toError("NOT FOUND"));
        }
    }

    @GET
    @Path("/file-information/name/{fileName}")
    @Produces({MediaType.APPLICATION_JSON + ";charset=utf-8", AmpMediaType.POSSIBLE_VALUES_V2_JSON})
    public FileInformationDTO getFileInformationByFileName(@PathParam("fileName") String fileName) {
        Optional<FileInformationDTO> fileInformation = FileInformationServiceImpl.getInstance().getFileInformationByFileName(fileName);
        if (fileInformation.isPresent()) {
            return fileInformation.get();
        } else {
            throw new ApiRuntimeException(Response.Status.NOT_FOUND, ApiError.toError("NOT FOUND"));
        }
    }

}
