/**
 * 
 */
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
import org.digijava.kernel.ampapi.endpoints.activity.APIField;
import org.digijava.kernel.ampapi.endpoints.activity.AmpFieldsEnumerator;
import org.digijava.kernel.ampapi.endpoints.activity.PossibleValue;
import org.digijava.kernel.ampapi.endpoints.activity.PossibleValuesEnumerator;
import org.digijava.kernel.ampapi.endpoints.common.fm.FMService;
import org.digijava.kernel.ampapi.endpoints.security.AuthRule;
import org.digijava.kernel.ampapi.endpoints.util.ApiMethod;
import org.digijava.kernel.ampapi.endpoints.util.JsonBean;

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
    @ApiOperation(
            value = "Provides FM (Feature Manager) settings for the requested options.",
            notes = "The settings will be taken from the current FM template.\n"
                    + "\n"
                    + "Field|Description\n"
                    + "-----|-----------\n"
                    + "reporting-fields|true/false (default) - to list or not reporting-fields\n"
                    + "enabled-modules|true/false (default) - to list or not enabled modules\n"
                    + "detail-modules|"
                    + "Option1: [\"all-enabled\"] (if present, other entries are skipped)<br>"
                    + "Option2: a list of valid module names like [\"GIS\"], or [\"GIS\", \"Dashboards\"]<br>"
                    + "Option3: [] (i.e. None, which is the default if omitted)\n"
                    + "detail-flat|true (default)/false - if to detail as a flat list or in a tree form\n"
                    + "full-enabled-paths|true (default)/false - if only fully enabled paths to detail\n"
                    + "fm-paths|by default no filtering - an array of FM paths that are required, "
                    + "applies only to tree structures\n"
                    + "\n"
                    + "### Sample Request\n"
                    + "```\n"
                    + "{\n"
                    + "   \"reporting-fields\" : true,\n"
                    + "   \"enabled-modules\" : true,\n"
                    + "   \"detail-modules\" : [\"GIS\",\"Dashboards\", ...],\n"
                    + "   \"detail-flat\" : true,\n"
                    + "   \"full-enabled-paths\" : true,\n"
                    + "   \"fm-paths\": [\"/PROJECT MANAGEMENT/Funding/Funding Information/Delivery rate\"]\n"
                    + "}\n"
                    + "```\n"
                    + "\n"
                    + "### Sample Output\n"
                    + "```\n"
                    + "{\n"
                    + " \"reporting-fields\" : [\"Project Title\", \"Primary Sector\", ...],\n"
                    + " \"enabled-modules\" : [\"GIS\", \"Dashboards\", ...],\n"
                    + " \"GIS\" : [\"/gis-enabled-setting1/enabled-childX\", \"/enabled-setting2\", ...],\n"
                    + " ...\n"
                    + "}\n"
                    + "```\n"
                    + "\n"
                    + "### Use cases\n"
                    + "1. Detail flat & fully enabled paths => /Activity Form/Organiation/Donor Organization\n"
                    + "\n"
                    + "2. Detail flat & partial enabled paths => "
                    + "/Activity Form[true]/Organiation[false]/Donor Organization[true]\n"
                    + "\n"
                    + "3. Detail tree & fully enabled paths =>\n"
                    + "    ```\n"
                    + "    \"REPORTING\": {\n"
                    + "         \"Measures\": {\n"
                    + "             \"Actual Disbursements\": {},\n"
                    + "             ...\n"
                    + "    ```\n"
                    + "\n"
                    + "4. Detail tree & fully enabled paths =>\n"
                    + "    ```\n"
                    + "    \"REPORTING\": {\n"
                    + "         \"__enabled\" : true, // omitted if fullEnabledPaths are requested (same below)\n"
                    + "         \"Measures\": {\n"
                    + "             \"__enabled\" : true,\n"
                    + "             \"Actual Disbursements\": {\n"
                    + "                 \"__enabled\" : true\n"
                    + "             },\n"
                    + "             ...\n"
                    + "    ```\n")
    @ApiResponses(@ApiResponse(code = HttpServletResponse.SC_OK, message = "list of FM settings as a JSON Object"))
    public JsonBean getFMSettings(
            @ApiParam("a JSON object with requested options (see the provided example)") JsonBean config) {
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
        List<APIField> apiFields = AmpFieldsEnumerator.PUBLIC_ENUMERATOR.getCommonSettingsFields();
        return PossibleValuesEnumerator.INSTANCE.getPossibleValuesForField(fieldName, apiFields);
    }

}
