package org.digijava.kernel.ampapi.endpoints.common;

import static java.util.Collections.emptyMap;
import static java.util.function.Function.identity;
import static java.util.stream.Collectors.toMap;

import java.util.List;
import java.util.Map;
import java.util.Objects;

import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.digijava.kernel.ampapi.endpoints.AmpEndpoint;
import org.digijava.kernel.ampapi.endpoints.common.fm.FMSettings;
import org.digijava.kernel.ampapi.endpoints.common.fm.FMSettingsFlat;
import org.digijava.kernel.ampapi.endpoints.common.fm.FMSettingsTree;
import org.digijava.kernel.services.AmpFieldsEnumerator;
import org.digijava.kernel.ampapi.endpoints.activity.PossibleValue;
import org.digijava.kernel.ampapi.endpoints.activity.PossibleValuesEnumerator;
import org.digijava.kernel.ampapi.endpoints.activity.field.APIField;
import org.digijava.kernel.ampapi.endpoints.common.fm.FMService;
import org.digijava.kernel.ampapi.endpoints.security.AuthRule;
import org.digijava.kernel.ampapi.endpoints.util.ApiMethod;

/**
 * Common Endpoint provides various settings (FM, settings)
 * 
 * @author Nadejda Mandrescu
 */
@Path("common")
@Api("common")
public class CommonEndpoint implements AmpEndpoint {

    @POST
    @Path("/fm")
    @Produces(MediaType.APPLICATION_JSON)
    @ApiMethod(ui = false, name = "fm", id = "")
    @ApiOperation(value = "Provides FM (Feature Manager) settings for the requested options as a tree.")
    public FMSettings<FMSettingsTree> getFMSettings(
            @ApiParam("FM Settings with requested options") FMSettingsConfig config) {
        return FMService.getFMSettings(config);
    }
    
    @POST
    @Path("/fm/flat")
    @Produces(MediaType.APPLICATION_JSON)
    @ApiMethod(ui = false, name = "fm", id = "")
    @ApiOperation(value = "Provides FM (Feature Manager) settings for the requested options in flat mode.")
    public FMSettings<FMSettingsFlat> getFMSettingsFlat(
            @ApiParam("FM Settings with requested options") FMSettingsConfig config) {
        config.setDetailsFlat(true);
        return FMService.getFMSettings(config);
    }

    @POST
    @Path("field/values")
    @Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
    @ApiMethod(authTypes = AuthRule.AUTHENTICATED, id = "getResourceMultiValues", ui = false)
    @ApiOperation(
            value = "Returns a list of possible values for each requested field.",
            notes = "If value can be translated then each possible value will contain value-translations element,"
                    + "a map where key is language code and value is translated value.\n"
                    + "### Sample request:\n"
                    + "```\n"
                    + "[\"currency\"]\n"
                    + "```\n"
                    + "### Sample response:\n"
                    + "```\n"
                    + "{\n"
                    + "  \"type\": [\n"
                    + "    {\n"
                    + "      \"id\": 34,\n"
                    + "      \"value\": \"USD\",\n"
                    + "      \"translated-value\": {\n"
                    + "        \"en\": \"USD\"\n"
                    + "      }\n"
                    + "    }\n"
                    + "  ]\n"
                    + "}\n"
                    + "```")
    public Map<String, List<PossibleValue>> getValues(
            @ApiParam("list of fully qualified resource fields") List<String> fields) {
        Map<String, List<PossibleValue>> response;
        if (fields == null) {
            response = emptyMap();
        } else {
            response = fields.stream()
                    .filter(Objects::nonNull)
                    .distinct()
                    .collect(toMap(identity(), this::possibleValuesForCommonSettingsField));
        }
        return response;
    }

    private List<PossibleValue> possibleValuesForCommonSettingsField(String fieldName) {
        List<APIField> apiFields = AmpFieldsEnumerator.getEnumerator().getCommonSettingsFields();
        return PossibleValuesEnumerator.INSTANCE.getPossibleValuesForField(fieldName, apiFields);
    }

}
