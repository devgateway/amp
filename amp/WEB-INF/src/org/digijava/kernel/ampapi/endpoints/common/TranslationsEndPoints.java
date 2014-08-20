package org.digijava.kernel.ampapi.endpoints.common;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.digijava.kernel.ampapi.endpoints.util.JsonBean;
import org.digijava.kernel.translator.TranslatorWorker;

@Path("translations")
public class TranslationsEndPoints {
	@POST
	@Path("/label-translations")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public JsonBean getLangPack(	final JsonBean param){
		
		for (String key:param.any().keySet()) {
			String newValue=
			TranslatorWorker.translateText(param.get(key).toString());
			param.set(key, newValue);
		}
		return param;
		
	}
}
