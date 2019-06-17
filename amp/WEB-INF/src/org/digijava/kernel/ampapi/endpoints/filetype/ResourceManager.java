package org.digijava.kernel.ampapi.endpoints.filetype;

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
import org.digijava.kernel.ampapi.endpoints.settings.SettingsConstants;
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
    @ApiOperation(
            value = "Save the resource manager settings and file types supported by AMP for file uploading.",
            notes = "The parameters used for saving file types and settings:\n"
                    + "\n"
                    + "Field|Description\n"
                    + "---|---\n"
                    + "allowedFileType |the list of allowed file types\n"
                    + "resourceSettings |the settings of the resource manager\n"
                    + "resourceSettings.limit-file-to-upload |enable the file type validation\n"
                    + "resourceSettings.maximum-file-size |the maximum limit of the file size, in MB\n"
                    + "resourceSettings.sort-column |the column used to sort the items in the resource table. "
                    + "Possible values: \"resource_title_ASC, resource_title_DESC, type_ASC, type_DESC, "
                    + "file_name_ASC, file_name_DESC, date_ASC, date_DESC, yearOfPublication_ASC, "
                    + "yearOfPublication_DESC, size_ASC, size_DESC, cm_doc_type_ASC, cm_doc_type_DESC\"\n"
                    + "\n"
                    + "### Sample Request\n"
                    + "```json\n"
                    + "{\n"
                    + "    \"allowedFileType\" : [\"msword\", \"msexcel\", \"csv\"],\n"
                    + "    \"resourceSettings\" : {\n"
                    + "        \"limit-file-to-upload\" : \"true\",\n"
                    + "        \"maximum-file-size\" : \"20\",\n"
                    + "        \"sort-column\" : \"resource_title_ASC\"\n"
                    + "    }\n"
                    + "}\n"
                    + "```\n")
    public Response saveSettings(ResourceManagerSettings settings) throws AMPException {
        
        for (String settingKey : settings.getResourceSettings().keySet()) {
            String value = settings.getResourceSettings().get(settingKey).toString();
            DbUtil.updateGlobalSetting(SettingsConstants.ID_NAME_MAP.get(settingKey), value);
        }
        
        // after updating we rebuild global settings cache
        FeaturesUtil.buildGlobalSettingsCache(FeaturesUtil.getGlobalSettings());
        FileTypeManager fileTypeManager = FileTypeManager.getInstance();
        fileTypeManager.updateFileTypesConfig(new LinkedHashSet<>(settings.getAllowedFileType()));
        
        return Response.ok().build();
    }
}
