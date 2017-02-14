package org.digijava.kernel.ampapi.endpoints.filetype;

import java.util.List;
import java.util.Set;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.digijava.kernel.ampapi.endpoints.security.AuthRule;
import org.digijava.kernel.ampapi.endpoints.util.ApiMethod;
import org.digijava.module.aim.dbentity.AmpFileType;

/**
 * FileTypes Endpoint provides supported file types in AMP
 * 
 * @author Viorel Chihai
 */
@Path("filetypes")
public class FileTypes {
	
	/** 
	 * Get available file types supported by AMP
	 * 
	 * @return a list of AmpFileType objects
	 */
	@GET
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
	@Path("allowed")
	@Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
	public List<AmpFileType> getAllowedFileTypes() {
		FileTypeManager fileTypeManager = FileTypeManager.getInstance();
		
		return fileTypeManager.getAllowedFileTypes();
	}
	
	/** 
	 * Save the file types supported by AMP for file uploading
	 * 
	 * @param allowedFileType list of file types (e.g.: "msword")
	 * @return
	 */
	@POST
	@Path("allowed")
	@Consumes(MediaType.APPLICATION_JSON + ";charset=utf-8")
	// @ApiMethod(id = "allowed-filetypes", authTypes = {AuthRule.IN_ADMIN}, ui = false)
	public Response setAllowedFiletypes(Set<String> allowedFileType) {
		FileTypeManager fileTypeManager = FileTypeManager.getInstance();
		
		fileTypeManager.updateFileTypesConfig(allowedFileType);
		
		return Response.ok().build();
	}
}
