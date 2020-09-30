package org.digijava.kernel.ampapi.endpoints.aitranslation;

import static org.digijava.module.aim.helper.GlobalSettingsConstants.MACHINE_TRANSLATION_ENABLED;
import static org.digijava.module.aim.helper.GlobalSettingsConstants.MACHINE_TRANSLATION_MAX_CHARACTERS;

import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import io.swagger.annotations.Example;
import io.swagger.annotations.ExampleProperty;
import org.apache.http.HttpStatus;
import org.digijava.kernel.ampapi.endpoints.security.AuthRule;
import org.digijava.kernel.ampapi.endpoints.util.ApiMethod;
import org.digijava.module.aim.util.FeaturesUtil;

/**
 * @author Octavian Ciubotaru
 */
@Path("machine-translation")
@Api("machine-translation")
public class MachineTranslationEndpoint {

    private static AsyncTranslator translator = new AsyncTranslator();

    private static final int SC_TOO_MANY_REQS = 429;

    @POST
    @Path("translate")
    @Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
    @ApiMethod(authTypes = AuthRule.AUTHENTICATED, id = "translate")
    @ApiOperation(value = "Batch translate",
            notes = "This is a long running operation and invoking this action will start a batch translation if the "
                    + "request was accepted.\n\nReturns operation id. "
                    + "This id can be used to retrieve the result of the translation.\n\n"
                    + "This is machine translation used to translate arbitrary texts, "
                    + "for localization see /translations.")
    @ApiResponses({
            @ApiResponse(code = HttpStatus.SC_OK, message = "Translation request accepted", response = String.class,
                    examples = @Example(value = @ExampleProperty(
                            mediaType = "application/json;charset=utf-8",
                            value = "00000000-0000-0000-0000-000000000000"))),
            @ApiResponse(code = SC_TOO_MANY_REQS, message = "Too many translation requests, try again later.")})
    public String translate(TranslationRequest request) {
        String id = translator.translate(request);
        if (id == null) {
            throw new WebApplicationException(SC_TOO_MANY_REQS);
        }
        return id;
    }

    @GET
    @Path("operation/{id}")
    @Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
    @ApiMethod(authTypes = AuthRule.AUTHENTICATED, id = "getTranslationResult")
    @ApiOperation(value = "Get the latest state of translation operation.",
            notes = "Clients can use this method to poll the operation result. Once the operation finished and its "
                    + "result was set to client, the operation is deleted.")
    @ApiResponses({
            @ApiResponse(code = HttpStatus.SC_OK, message = "Success", response = TranslationOperation.class,
                    examples = @Example(@ExampleProperty(mediaType = "application/json;charset=utf-8",
                            value = "{\"done\":true,\"result\":{\"Hello\":\"Bonjour\"}"))),
            @ApiResponse(code = HttpStatus.SC_NOT_FOUND, message = "Operation not found")})
    public TranslationOperation getTranslationResult(
            @ApiParam("Operation Id")
            @PathParam("id") String id) {
        TranslationOperation operation = translator.getOperation(id);
        if (operation == null) {
            throw new WebApplicationException(HttpStatus.SC_NOT_FOUND);
        } else {
            return operation;
        }
    }

    @GET
    @Path("config")
    @Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
    @ApiMethod(authTypes = AuthRule.AUTHENTICATED, id = "getSettings")
    @ApiOperation(value = "Get the configuration of the machine translation service.",
            notes = "Useful only for IATI Importer to decide which fields can be translated.")
    public TranslationConfig getSettings() {
        boolean mtEnabled = FeaturesUtil.getGlobalSettingValueBoolean(MACHINE_TRANSLATION_ENABLED);
        int maxChars = FeaturesUtil.getGlobalSettingValueInteger(MACHINE_TRANSLATION_MAX_CHARACTERS);
        return new TranslationConfig(mtEnabled, maxChars);
    }
}
