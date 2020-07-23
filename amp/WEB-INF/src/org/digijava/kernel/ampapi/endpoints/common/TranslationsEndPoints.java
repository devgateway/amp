package org.digijava.kernel.ampapi.endpoints.common;

import static javax.servlet.http.HttpServletResponse.SC_OK;
import static org.digijava.kernel.translator.util.TrnUtil.DEFAULT;
import static org.digijava.kernel.translator.util.TrnUtil.PREFIX;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import io.swagger.annotations.Example;
import io.swagger.annotations.ExampleProperty;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;

import org.digijava.kernel.ampapi.endpoints.dto.Language;
import org.digijava.kernel.ampapi.endpoints.util.ApiMethod;
import org.digijava.kernel.ampapi.endpoints.util.AvailableMethod;
import org.digijava.kernel.entity.Locale;
import org.digijava.kernel.request.TLSUtils;
import org.digijava.kernel.translator.TranslatorWorker;
import org.digijava.kernel.util.DgUtil;
import org.digijava.kernel.util.SiteUtils;
import org.digijava.module.translation.util.ContentTranslationUtil;
import org.digijava.module.translation.util.TranslationManager;
import org.springframework.web.bind.annotation.RequestBody;

@Path("translations")
@Api("translations")
public class TranslationsEndPoints {

    @GET
    @Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
    @ApiOperation("Get the list of available methods in translations endpoint.")
    public List<AvailableMethod> getAvailableMethods() {
        return EndpointUtils.getAvailableMethods(TranslationsEndPoints.class.getName());
    }

    @POST
    @Path("/label-translations")
    @Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
    @Consumes(MediaType.APPLICATION_JSON + ";charset=utf-8")
    @ApiOperation(
            value = "Translate a list of labels in default session language.",
            notes = "The object holds information regarding:\n"
                    + "\n"
                    + "Key|Value\n"
                    + "---|---\n"
                    + "the label key used for unique reference|the text to translate\n"
                    + "\n"
                    + "### Sample Input\n"
                    + "```\n"
                    + "{\n"
                    + "   \"chWS\": \"Change workspace\",\n"
                    + "   \"logout\": \"Log out\",\n"
                    + "}\n"
                    + "```"
    )
    @ApiResponses(@ApiResponse(code = SC_OK, message = "Map of translated labels.", examples =
    @Example(value = {
            @ExampleProperty(
                    mediaType = "application/json;charset=utf-8",
                    value = "{\n \"chWS\": \"Changer d'Espace de Travail\",\n \"logout\": \"Déconnecter\"\n}"
            )
    })
    ))
    @ApiMethod(ui = false, id = "Translations")
    public Map<String, String> getLangPack(
            @ApiParam(name = "param", required = true, value = "Key-label pairs to translate",
                    examples =
                            // this is not working (yet), using sample in ApiOperation.notes
                    @Example(value = {
                            @ExampleProperty(
                                    mediaType = "application/json;charset=utf-8",
                                    value = "{\n \"chWS\": \"Change workspace\",\n \"logout\": \"Log out\"\n}"
                            )
                    }))
            @RequestBody final Map<String, String> param) {
        return getLangPack(null, param);
    }

    @POST
    @Path("/translate-labels/{langCode}")
    @Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
    @Consumes(MediaType.APPLICATION_JSON + ";charset=utf-8")
    @ApiOperation(
            value = "Translate a list of labels in selected language specified by code.",
            notes = "The object holds information regarding:\n"
                    + "\n"
                    + "Key|Value\n"
                    + "---|---\n"
                    + "the label key used for unique reference|the text to translate\n"
                    + "\n"
                    + "### Sample Input\n"
                    + "```\n"
                    + "{\n"
                    + "   \"chWS\": \"Change workspace\",\n"
                    + "   \"logout\": \"Log out\",\n"
                    + "}\n"
                    + "```"
    )
    @ApiResponses(@ApiResponse(code = SC_OK, message = "Map of translated labels.", examples =
    @Example(value = {
            @ExampleProperty(
                    mediaType = "application/json;charset=utf-8",
                    value = "{\n \"chWS\": \"Changer d'Espace de Travail\",\n \"logout\": \"Déconnecter\"\n}"
            )
    })
    ))
    @ApiMethod(ui = false, id = "CustomLanguageTranslations")
    public Map<String, String> getLangPack(@PathParam("langCode") @ApiParam(example = "en") String langCode,
                                           @ApiParam(name = "param", required = true, value = "Key-label pairs to translate")
                                           @RequestBody final Map<String, String> param) {

        String language = langCode == null ? TLSUtils.getEffectiveLangCode() : langCode;

        for (String key : param.keySet()) {
            String translating = param.get(key);
            String newValue = TranslatorWorker.translateText(translating, language, SiteUtils.DEFAULT_SITE_ID);
            param.put(key, newValue);
        }

        return param;

    }

    @GET
    @Path("/languages/")
    @Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
    @Consumes(MediaType.APPLICATION_JSON + ";charset=utf-8")
    @ApiOperation("Get the list of languages used in AMP.")
    @ApiMethod(ui = false, id = "languages")
    public List<Language> getLanguages() {
        return TranslationManager.getAmpLanguages();
    }

    @GET
    @Path("/languages/{langCode}")
    @ApiOperation("Change the language used in session.")
    @ApiMethod(ui = false, id = "LanguageSwitch")
    public void switchLanguage(@PathParam("langCode") @ApiParam(name = "langCode", example = "fr") String langCode,
                               @Context HttpServletResponse response) {
        Locale locale = new Locale();
        locale.setCode(langCode);
        DgUtil.switchLanguage(locale, TLSUtils.getRequest(), response);
    }

    @GET
    @Path("/multilingual-languages/")
    @Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
    @Consumes(MediaType.APPLICATION_JSON + ";charset=utf-8")
    @ApiMethod(ui = false, id = "multilingualLanguages")
    @ApiOperation(
            value = "Gets the list of available languages for a site when multilingual is enabled.",
            notes = "When multilingual is disabled it returns the effective language (e.g. either the currently-set "
                    + "one OR the default one (\"en\")). ")
    public List<Language> getMultilingualLanguages() {
        List<Language> languages = TranslationManager.getAmpLanguages();
        if (!ContentTranslationUtil.multilingualIsEnabled()) {
            String code = TLSUtils.getEffectiveLangCode();
            languages = languages.stream().filter(l -> code.equalsIgnoreCase(l.getId())).collect(Collectors.toList());
        }
        return languages;
    }

    @POST
    @Path("/translate")
    @Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
    @Consumes(MediaType.APPLICATION_JSON + ";charset=utf-8")
    @ApiOperation(
            value = "Translate a list of labels in multiple languages at once.",
            notes = "The array holds the list of labels to translate at once:\n"
                    + "### Sample Input\n"
                    + "```\n"
                    + "[\n"
                    + "   \"Log out\"\n"
                    + "]\n"
                    + "```"
    )
    @ApiResponses(@ApiResponse(code = SC_OK, message = "Map of translation grouped by labels and locale code.",
            examples =
            @Example(value = {
                    @ExampleProperty(
                            mediaType = "application/json;charset=utf-8",
                            value = "{\n  \"Log out\": {\n    \"fr\": \"Déconnecter\"\n  }\n}"
                    )
            })
    ))
    public Map<String, Map<String, String>> translateLabels(
            @ApiParam(name = "labels", required = true) List<String> labels) {
        return TranslationUtil.translateLabels(labels);
    }

    @POST
    @Path("/translateWithPrefix")
    @Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
    @Consumes(MediaType.APPLICATION_JSON + ";charset=utf-8")
    @ApiOperation(
            value = "Translate a list of labels in multiple languages at once.",
            notes = "The array holds the list of labels to translate at once:\n"
                    + "### Sample Input\n"
                    + "```\n"
                    + "[\n"
                    + "   \"Log out\"\n"
                    + "]\n"
                    + "```"
    )
    @ApiResponses(@ApiResponse(code = SC_OK, message = "Map of translation grouped by workspace prefix and then by "
            + "labels and locale code.",
            examples =
            @Example(value = {
                    @ExampleProperty(
                            mediaType = "application/json;charset=utf-8",
                            value = "{\n  \"Log out\": {\n    \"fr\": \"Déconnecter\"\n  }\n}"
                    )
            })
    ))
    public Map<String, Map<String, Map<String, String>>> translateLabelsWithPrefix(
            @ApiParam(name = "labels", required = true) List<String> labels) {

        List<String> prefixes = TranslatorWorker.getAllPrefixes();
        Map<String, Map<String, String>> noPrefixTranslations = TranslationUtil.translateLabels(labels);
        Map<String, Map<String, Map<String, String>>> allTranslations = new HashMap<>();
        allTranslations.put(DEFAULT, noPrefixTranslations);
        prefixes.forEach(prefix -> {
            TLSUtils.getRequest().setAttribute(PREFIX, prefix);
            allTranslations.put(prefix, TranslationUtil.translateLabels(labels));
        });
        return allTranslations;
    }
}
