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

import org.apache.log4j.Logger;
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
import org.digijava.module.aim.helper.GlobalSettingsConstants;
import org.digijava.module.aim.util.FeaturesUtil;
import org.digijava.module.translation.util.TranslationManager;

@Path("translations")
public class TranslationsEndPoints {
	
	private static final Logger LOGGER = Logger.getLogger(TranslationsEndPoints.class); 
	
	@GET
	@Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
	public List<AvailableMethod> getAvailableFilters() {
		return EndpointUtils.getAvailableMethods(TranslationsEndPoints.class.getName());
	}	
	
	@POST
	@Path("/label-translations")
	@Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
	@Consumes(MediaType.APPLICATION_JSON + ";charset=utf-8")
	@ApiMethod(ui = false, id = "Translations")
	public JsonBean getLangPack(final JsonBean param){
		
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
	
	
	/**
	 * Gets the list of available languages for a site when multilingual is enabled.
	 * When multilingual is disabled it returns the effective language (e.g. either the currently-set one OR 
	 * the default one ("en")). 
	 * @return List <SimpleJsonBean> with the available Locale
	 */
	@SuppressWarnings("rawtypes")
	@GET
	@Path("/multilingual-languages/")
	@ApiMethod(ui = false, id = "multilingualLanguages")
	public List<SimpleJsonBean> getMultilingualLanguages() {
		List<SimpleJsonBean> languages = new ArrayList<SimpleJsonBean>();
		try {
			List locales = TranslationManager.getLocale(PersistenceManager.getRequestDBSession());
			boolean onlyCurrentLanguage = !"true".equalsIgnoreCase(FeaturesUtil
					.getGlobalSettingValue(GlobalSettingsConstants.MULTILINGUAL));
			Iterator iter = locales.iterator();
			while (iter.hasNext()) {
				Object[] localeRecord = (Object[]) iter.next();
				if (onlyCurrentLanguage) {
					if (localeRecord[0].equals(TLSUtils.getEffectiveLangCode())) {
						languages.add(new SimpleJsonBean((String) localeRecord[0], (String) localeRecord[1]));
						break;
					}
				} else {
					languages.add(new SimpleJsonBean((String) localeRecord[0], (String) localeRecord[1]));
				}
			}

		} catch (DgException e) {
			LOGGER.warn("Couldn't obtain the list of locales", e);
		}
		return languages;
	}

}
