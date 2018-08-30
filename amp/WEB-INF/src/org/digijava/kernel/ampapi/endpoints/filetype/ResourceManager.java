package org.digijava.kernel.ampapi.endpoints.filetype;

import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.dgfoundation.amp.error.AMPException;
import org.digijava.kernel.ampapi.endpoints.security.AuthRule;
import org.digijava.kernel.ampapi.endpoints.settings.SettingsConstants;
import org.digijava.kernel.ampapi.endpoints.util.ApiMethod;
import org.digijava.kernel.ampapi.endpoints.util.JsonBean;
import org.digijava.module.admin.util.DbUtil;
import org.digijava.module.aim.dbentity.AmpFileType;
import org.digijava.module.aim.util.FeaturesUtil;

/**
 * FileTypes Endpoint provides supported file types in AMP
 * 
 * @author Viorel Chihai
 */
@Path("resourcemanager")
public class ResourceManager {

    /**
     * Get all available file types supported by AMP.
     * </br>
     * <dl>
     * The JSON object holds information regarding:
     * <dt><b>name</b><dd> - the name of the file type
     * <dt><b>description</b><dd> - the description of the file type
     * <dt><b>mimeTypes</b><dd> - the list of mimetypes of the file type
     * <dt><b>extensions</b><dd> - the list of extensions of the file type
     * </dl></br></br>
     *
     * <h3>Sample Output:</h3><pre>
     * [
     *  {
     *   "name": "msword",
     *   "description": "MS Word",
     *   "mimeTypes": [
     *     "application/msword",
     *     "application/vnd.openxmlformats-officedocument.wordprocessingml.document",
     *     "application/vnd.openxmlformats-officedocument.wordprocessingml.template"
     *    ],
     *    "extensions": [
     *      ".doc",
     *      ".dot",
     *      ".docx",
     *      ".dotx"
     *    ]
     *  },
     *  ...
     * ]</pre>
     * 
     * @return a list of AmpFileType objects
     */
    @GET
    @Path("file-types")
    @Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
    @ApiMethod(ui = false, id = "getAllFiletypes", authTypes = AuthRule.IN_ADMIN)
    public List<AmpFileType> getAllFiletypes() {
        FileTypeManager fileTypeManager = FileTypeManager.getInstance();

        return fileTypeManager.getAllFileTypes();
    }

    /**
     * Get allowed file types for file uploading in AMP.
     * </br>
     * <dl>
     * The JSON object holds information regarding:
     * <dt><b>name</b><dd> - the name of the file type
     * <dt><b>description</b><dd> - the description of the file type
     * <dt><b>mimeTypes</b><dd> - the list of mimetypes of the file type
     * <dt><b>extensions</b><dd> - the list of extensions of the file type
     * </dl></br></br>
     *
     * <h3>Sample Output:</h3><pre>
     * [
     *  {
     *   "name": "msword",
     *   "description": "MS Word",
     *   "mimeTypes": [
     *     "application/msword",
     *     "application/vnd.openxmlformats-officedocument.wordprocessingml.document",
     *     "application/vnd.openxmlformats-officedocument.wordprocessingml.template"
     *    ],
     *    "extensions": [
     *      ".doc",
     *      ".dot",
     *      ".docx",
     *      ".dotx"
     *    ]
     *  },
     *  ...
     * ]</pre>
     * 
     * @return a list of AmpFileType objects
     */
    @GET
    @Path("file-types/allowed")
    @Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
    @ApiMethod(ui = false, id = "getAllowedFileTypes", authTypes = AuthRule.AUTHENTICATED)
    public List<AmpFileType> getAllowedFileTypes() {
        FileTypeManager fileTypeManager = FileTypeManager.getInstance();

        return fileTypeManager.getAllowedFileTypes();
    }

    /**
     * Save the resource manager settings and file types supported by AMP for file uploading.
     * 
     * The parameters used for saving file types and settings:
     * <dl>
     * <b>allowedFileType</b><dd>the list of allowed file types</dd>
     * <b>resourceSettings</b><dd>the settings of the resource manager</dd>
     * </dl>
     * </br>
     *  The <b>resourceSettings</b> object has the following attributes:
     *  <dl>
     *  <b>limit-file-to-upload</b><dd>enable the file type validation</dd>
     *  <b>maximum-file-size</b><dd>the maximum limit of the file size, in MB</dd>
     *  <b>sort-column</b><dd>the column used to sort the items in the resource table. 
     *  Available values: sort-column possible values: "resource_title_ASC|resource_title_DESC|type_ASC|type_DESC
     *  |file_name_ASC|file_name_DESC|date_ASC|date_DESC|yearOfPublication_ASC|yearOfPublication_DESC|size_ASC
     *  |size_DESC|cm_doc_type_ASC|cm_doc_type_DESC"</dd>
     *  </dl>
     *
     * <h3>Sample Request:</h3><pre>
     * {
     *    "allowedFileType" : ["msword", "msexcel", "csv"],
     *    "resourceSettings" : {
     *    "limit-file-to-upload" : "true",
     *    "maximum-file-size" : "20",
     *    "sort-column" : "resource_title_ASC"
     *    }
     * }</pre>
     * 
     * @param parameters the JSON object containing the requested parameters
     * @return Response - the HttpResponse with the status code
     * @throws AMPException
     */
    @POST
    @Path("save-settings")
    @Consumes(MediaType.APPLICATION_JSON + ";charset=utf-8")
    @ApiMethod(ui = false, id = "saveSettings", authTypes = AuthRule.IN_ADMIN)
    @SuppressWarnings("unchecked")
    public Response saveSettings(JsonBean settings) throws AMPException {
        List<String> allowedFileType = (List<String>) settings.get("allowedFileType");
        Map<String, Object> map = (Map<String, Object>) settings.get("resourceSettings");
        
        for (String settingKey : map.keySet()) {
            String value = map.get(settingKey).toString();
            DbUtil.updateGlobalSetting(SettingsConstants.ID_NAME_MAP.get(settingKey), value);
        }
        // after updating we rebuild global settings cache
        FeaturesUtil.buildGlobalSettingsCache(FeaturesUtil.getGlobalSettings());
        FileTypeManager fileTypeManager = FileTypeManager.getInstance();
        fileTypeManager.updateFileTypesConfig(new LinkedHashSet<String>(allowedFileType));
        
        return Response.ok().build();
    }
}
