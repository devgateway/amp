package org.digijava.kernel.ampapi.endpoints.common;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;

import org.digijava.kernel.ampapi.endpoints.dto.SimpleJsonBean;
import org.digijava.kernel.ampapi.endpoints.util.ApiMethod;
import org.digijava.kernel.ampapi.endpoints.util.AvailableMethod;
import org.digijava.kernel.ampapi.endpoints.util.JsonBean;
import org.digijava.kernel.entity.Locale;
import org.digijava.kernel.exception.DgException;
import org.digijava.kernel.persistence.PersistenceManager;
import org.digijava.kernel.request.TLSUtils;
import org.digijava.kernel.translator.TranslatorWorker;
import org.digijava.kernel.util.DgUtil;
import org.digijava.module.translation.form.TranslationForm;
import org.digijava.module.translation.util.TranslationManager;

@Path("translations")
public class TranslationsEndPoints {
	
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public List<AvailableMethod> getAvailableFilters() {
		return EndpointUtils.getAvailableMethods(TranslationsEndPoints.class.getName());
	}	
	
	@POST
	@Path("/label-translations")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	@ApiMethod(ui = false, id = "Translations")
	public JsonBean getLangPack(	final JsonBean param){
		
		for (String key:param.any().keySet()) {
			String newValue=
			TranslatorWorker.translateText(param.get(key).toString());
			param.set(key, newValue);
		}
		return param;
		
	}

	@GET
	@Path("/languages/")
	@ApiMethod(ui = false, id = "languajes")
	public List<SimpleJsonBean> getLanguages(){
		 List<SimpleJsonBean> languages=new  ArrayList<SimpleJsonBean>();
         try {
			List locales = TranslationManager.getLocale(PersistenceManager.getRequestDBSession());
            Iterator iter = locales.iterator();
            while (iter.hasNext()) {
                Object[] localeRecord = (Object[]) iter.next();
                
                languages.add(new SimpleJsonBean((String)localeRecord[0],(String)localeRecord[1]));
            }
			
		} catch (DgException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

//		 languages.add(new SimpleJsonBean("lang", "))
		 return languages;
	}
	
	@GET
	@Path("/languages/{langCode}")
	@ApiMethod(ui = false, id = "LanguageSwitch")
	public void switchLanguage(@PathParam("langCode") String langCode,@Context HttpServletResponse response){

        Locale locale = new Locale();
        locale.setCode(langCode);
        DgUtil.switchLanguage(locale, TLSUtils.getRequest(), response);
	}
}
