package org.digijava.kernel.ampapi.endpoints.mimetype;

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
import org.digijava.module.aim.dbentity.AmpMimeType;

/**
 * MimeTypes Endpoint provides supported mime types in AMP
 * 
 * @author Viorel Chihai
 */
@Path("mimetypes")
public class MimeTypes {
	
	/** 
	 * Get available mime types supported by Apache Tika
	 * 
	 * @return a list of AmpMimeType objects
	 */
	@GET
	@Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
	public List<AmpMimeType> getAllMimetypes() {
		MimeTypeManager mimeTypeManager = MimeTypeManager.getInstance();
		
		return mimeTypeManager.getAllMimeTypes();
	}
	
	/** 
	 * Get allowed mime types for file uploading
	 * 
	 * @return a list of allowed AmpMimeType objects
	 */
	@GET
	@Path("allowed")
	@Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
	public List<AmpMimeType> getAllowedMimetypes() {
		MimeTypeManager mimeTypeManager = MimeTypeManager.getInstance();
		
		return mimeTypeManager.getAllowedMimeTypes();
	}
	
	/** 
	 * Save the mime types supported by AMP for file uploading
	 * 
	 * @param allowedContentMimeType list of content mime types (e.g.: "application/msword")
	 * @return
	 */
	@POST
	@Path("allowed")
	@Consumes(MediaType.APPLICATION_JSON + ";charset=utf-8")
	@ApiMethod(id = "allowed-mimetypes", authTypes = {AuthRule.IN_ADMIN}, ui = false)
	public Response setAllowedMimetypes(Set<String> allowedContentMimeType) {
		MimeTypeManager mimeTypeManager = MimeTypeManager.getInstance();
		
		mimeTypeManager.updateMimeTypesConfig(allowedContentMimeType);
		
		return Response.ok().build();
	}
}
