package org.digijava.kernel.ampapi.endpoints.filetype;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

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
import org.digijava.module.aim.dbentity.AmpGlobalSettings;
import org.digijava.module.aim.util.FeaturesUtil;
import org.hsqldb.lib.HashSet;
import org.jfree.util.Log;

/**
 * FileTypes Endpoint provides supported file types in AMP
 * 
 * @author Viorel Chihai
 */
@Path("resourcemanager")
public class ResourceManager {

	/**
	 * Get available file types supported by AMP
	 * 
	 * @return a list of AmpFileType objects
	 */
	@GET
	@Path("file-types")
	@Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
	public List<AmpFileType> getAllFiletypes() {
		FileTypeManager fileTypeManager = FileTypeManager.getInstance();

		return fileTypeManager.getAllFileTypes();
	}

	/**
	 * Get allowed file types for file uploading
	 * 
	 * @return a list of allowed AmpFileType objects
	 */
	@GET
	@Path("file-types/allowed")
	@Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
	public List<AmpFileType> getAllowedFileTypes() {
		FileTypeManager fileTypeManager = FileTypeManager.getInstance();

		return fileTypeManager.getAllowedFileTypes();
	}

	/**
	 * Save the file types supported by AMP for file uploading
	 * 
	 * @param allowedFileType
	 *            list of file types (e.g.: "msword")
	 * @return
	 * @throws AMPException
	 */
	@POST
	@Path("save-settings")
	@Consumes(MediaType.APPLICATION_JSON + ";charset=utf-8")
	// @ApiMethod(id = "allowed-filetypes", authTypes = {AuthRule.IN_ADMIN}, ui
	// = false)
	@SuppressWarnings("unchecked")
	public Response saveSettings(JsonBean settings) throws AMPException {
		// TODO catch exception
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

	@GET
	@Path("test")
	@Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
	public JsonBean test() {
		Set<String> allowedType = new LinkedHashSet<String>();
		allowedType.add("bmp");
		allowedType.add("csv");
		allowedType.add("bmp");
		allowedType.add("gif");
		allowedType.add("jpeg");
		allowedType.add("zipArc");
		JsonBean j = new JsonBean();
		j.set("allowedFileType", allowedType);
		Map<String, String> m = new HashMap<String, String>();
		m.put("maximum-file-size", "12");
		m.put("limit-file-to-upload", "false");
		m.put("sort-column", "date_DESC");
		j.set("resourceSettings", m);

		System.out.println(j.toString());
		return j;
	}
}
