package org.digijava.kernel.ampapi.endpoints.common;

import static javax.servlet.http.HttpServletResponse.SC_OK;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.digijava.kernel.ampapi.endpoints.dto.Language;
import org.digijava.kernel.ampapi.endpoints.dto.SimpleJsonBean;
import org.digijava.kernel.ampapi.endpoints.util.ApiMethod;
import org.digijava.kernel.ampapi.endpoints.util.AvailableMethod;
import org.digijava.kernel.ampapi.endpoints.util.JsonBean;
import org.digijava.kernel.entity.Locale;
import org.digijava.kernel.persistence.PersistenceManager;
import org.digijava.kernel.request.TLSUtils;
import org.digijava.kernel.translator.TranslatorWorker;
import org.digijava.kernel.util.DgUtil;
import org.digijava.module.translation.util.ContentTranslationUtil;
import org.digijava.module.translation.util.TranslationManager;

@Path("translations")
@Api("translations")
public class TranslationsEndPoints {
    
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
        return getLangPack(null, param);
    }

    @POST
    @Path("/translate-labels/{langCode}")
    @Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
    @Consumes(MediaType.APPLICATION_JSON + ";charset=utf-8")
    @ApiMethod(ui = false, id = "CustomLanguageTranslations")
    public JsonBean getLangPack(@PathParam("langCode") String langCode, final JsonBean param){
        String language = langCode == null ? TLSUtils.getEffectiveLangCode() : langCode;
        for (String key:param.any().keySet()) {
            String translating = param.get(key).toString();
            String newValue= TranslatorWorker.translateText(translating, language, 3l);
            //LOGGER.error("translating <" + translating + "> to <" + newValue + ">");
            param.set(key, newValue);
        }
        return param;
        
    }
    
    @GET
    @Path("/languages/")
    @Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
    @Consumes(MediaType.APPLICATION_JSON + ";charset=utf-8")
    @ApiMethod(ui = false, id = "languages")
    public List<Language> getLanguages() {
        return TranslationManager.getAmpLanguages();
    }
    
    @GET
    @Path("/languages/{langCode}")
    @ApiMethod(ui = false, id = "LanguageSwitch")
    public void switchLanguage(@PathParam("langCode") String langCode,@Context HttpServletResponse response){
        Locale locale = new Locale();
        locale.setCode(langCode);
        DgUtil.switchLanguage(locale, TLSUtils.getRequest(), response);
    }
    
    @SuppressWarnings("rawtypes")
    @GET
    @Path("/multilingual-languages/")
    @Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
    @Consumes(MediaType.APPLICATION_JSON + ";charset=utf-8")
    @ApiMethod(ui = false, id = "multilingualLanguages")
    @ApiOperation(
            value = "Gets the list of available languages for a site when multilingual is enabled.",
            notes = "When multilingual is disabled it returns the effective language (e.g. either the currently-set "
                    + "one OR the default one (\"en\")). ")
    public List<SimpleJsonBean> getMultilingualLanguages() {
        List<SimpleJsonBean> languages = new ArrayList<SimpleJsonBean>();
        List<String[]> locales = TranslationManager.getLocale(PersistenceManager.getSession());
        boolean onlyCurrentLanguage = !ContentTranslationUtil.multilingualIsEnabled();
        for(String[] localeRecord:locales) {
            boolean entryRelevant = onlyCurrentLanguage ? localeRecord[0].equals(TLSUtils.getEffectiveLangCode()) : true;
            if (entryRelevant) {
                languages.add(new SimpleJsonBean(localeRecord[0], localeRecord[1]));
            }
        }
        return languages;
    }

    @POST
    @Path("/translate")
    @Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
    @Consumes(MediaType.APPLICATION_JSON + ";charset=utf-8")
    @ApiOperation("Translate a list of labels in multiple languages at once.")
    @ApiResponses(@ApiResponse(code = SC_OK, message = "Map of translation grouped by labels and locale code."))
    public Map<String, Map<String, String>> translateLabels(List<String> labels) {
        return TranslationUtil.translateLabels(labels);
    }
}
