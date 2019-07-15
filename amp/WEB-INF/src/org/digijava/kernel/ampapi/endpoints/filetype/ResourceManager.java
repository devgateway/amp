package org.digijava.kernel.ampapi.endpoints.filetype;

import static org.digijava.module.aim.helper.GlobalSettingsConstants.CR_MAX_FILE_SIZE;
import static org.digijava.module.aim.helper.GlobalSettingsConstants.DEFAULT_RESOURCES_SORT_COLUMN;
import static org.digijava.module.aim.helper.GlobalSettingsConstants.LIMIT_FILE_TYPE_FOR_UPLOAD;

import java.util.LinkedHashSet;
import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.dgfoundation.amp.error.AMPException;
import org.digijava.kernel.ampapi.endpoints.security.AuthRule;
import org.digijava.kernel.ampapi.endpoints.util.ApiMethod;
import org.digijava.module.admin.util.DbUtil;
import org.digijava.module.aim.dbentity.AmpFileType;
import org.digijava.module.aim.util.FeaturesUtil;

/**
 * FileTypes Endpoint provides supported file types in AMP
 * 
 * @author Viorel Chihai
 */
@Path("resourcemanager")
@Api("resourcemanager")
public class ResourceManager {

    @GET
    @Path("file-types")
    @Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
    @ApiMethod(ui = false, id = "getAllFiletypes", authTypes = AuthRule.IN_ADMIN)
    @ApiOperation("Get all available file types supported by AMP.")
    public List<AmpFileType> getAllFiletypes() {
        FileTypeManager fileTypeManager = FileTypeManager.getInstance();

        return fileTypeManager.getAllFileTypes();
    }

    @GET
    @Path("file-types/allowed")
    @Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
    @ApiMethod(ui = false, id = "getAllowedFileTypes", authTypes = AuthRule.AUTHENTICATED)
    @ApiOperation("Get allowed file types for file uploading in AMP.")
    public List<AmpFileType> getAllowedFileTypes() {
        FileTypeManager fileTypeManager = FileTypeManager.getInstance();

        return fileTypeManager.getAllowedFileTypes();
    }

    @POST
    @Path("save-settings")
    @Consumes(MediaType.APPLICATION_JSON + ";charset=utf-8")
    @ApiMethod(ui = false, id = "saveSettings", authTypes = AuthRule.IN_ADMIN)
    @ApiOperation(value = "Save the resource manager settings and file types supported by AMP for file uploading.")
    public Response saveSettings(ResourceManagerSettings settings) throws AMPException {
        ResourceSettings resourceSettings = settings.getResourceSettings();
        if (resourceSettings != null) {
            if (resourceSettings.getLimitFileToUpload() != null) {
                DbUtil.updateGlobalSetting(LIMIT_FILE_TYPE_FOR_UPLOAD,
                        Boolean.toString(resourceSettings.getLimitFileToUpload()));
            }
            
            if (resourceSettings.getMaximumFileSize() != null) {
                DbUtil.updateGlobalSetting(CR_MAX_FILE_SIZE, Long.toString(resourceSettings.getMaximumFileSize()));
            }
            
            if (resourceSettings.getSortColumn() != null) {
                DbUtil.updateGlobalSetting(DEFAULT_RESOURCES_SORT_COLUMN, resourceSettings.getSortColumn());
            }
    
            // after updating we rebuild global settings cache
            FeaturesUtil.buildGlobalSettingsCache(FeaturesUtil.getGlobalSettings());
        }
    
        FileTypeManager fileTypeManager = FileTypeManager.getInstance();
        fileTypeManager.updateFileTypesConfig(new LinkedHashSet<>(settings.getAllowedFileType()));
        
        return Response.ok().build();
    }
}
