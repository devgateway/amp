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

import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

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
public class CommonEndpoint {
    
    /**
     * Provides FM (Feature Manager) settings for the requested options.
     * 
     * The settings will be taken from the current FM template.
     * <dl>
     * <b>reporting-fields</b><dd>true/false (default) - to list or not reporting-fields</dd>
     * <b>enabled-modules</b><dd>true/false (default) - to list or not enabled modules</dd>
     * <b>detail-modules</b>
     *     <dd>Option1: ["all-enabled"] (if present, other entries are skipped)</dd>
     *     <dd>Option2: a list of valid module names like ["GIS"], or ["GIS", "Dashboards"]</dd>
     *     <dd>Option3: [] (i.e. None, which is the default if omitted)</dd>
     * </br>
     * <h5>Additions:</h5>
     * <b>detail-flat</b> <dd>true (default)/false - if to detail as a flat list or in a tree form</dd>
     * <b>full-enabled-paths</b> <dd>true (default)/false - if only fully enabled paths to detail</dd>
     * <b>fm-paths</b> <dd>by default no filtering - an array of FM paths that are required,
     * applies only to tree structures</dd>
     * </br>
     * <h3>Sample Request:</h3><pre>
     * {
     *    "reporting-fields" : true,
     *    "enabled-modules" : true,
     *    "detail-modules" : ["GIS","Dashboards", ...],
     *    "detail-flat" : true,
     *    "full-enabled-paths" : true,
     *    "fm-paths": ["/PROJECT MANAGEMENT/Funding/Funding Information/Delivery rate"]
     *  }</pre>
     * </br>
     * <h3>Sample Output:</h3><pre>
     * {
     *  "reporting-fields" : ["Project Title", "Primary Sector", ...],
     *  "enabled-modules" : ["GIS", "Dashboards", ...],
     *  "GIS" : ["/gis-enabled-setting1/enabled-childX", "/enabled-setting2", ...],
     *  ...
     * }</pre>
     * 
     * <h3>Use cases:</h3><pre>
     * 1) Detail flat & fully enabled paths => /Activity Form/Organiation/Donor Organization
     * 2) Detail flat & partial enabled paths => /Activity Form[true]/Organiation[false]/Donor Organization[true]
     * 3) Detail tree & fully enabled paths =>
     * "REPORTING": {
     *      "Measures": {
     *          "Actual Disbursements": {},
     *          ...
     * 4) Detail tree & fully enabled paths =>
     * "REPORTING": {
     *      "__enabled" : true,         // omitted if fullEnabledPaths are requested (same below) 
     *      "Measures": {
     *          "__enabled" : true,
     *          "Actual Disbursements": {
     *              "__enabled" : true
     *          },
     *          ...
     * </pre>
     * @param config a JSON object with requested options (see the provided example) 
     * @return list of FM settings as a JSON Object
     */
    @POST
    @Path("/fm")
    @Produces(MediaType.APPLICATION_JSON)
    @ApiMethod(ui = false, name = "fm", id = "")
    public JsonBean getFMSettings(JsonBean config) {
        return FMService.getFMSettings(config);
    }
    
    /**
     * Returns a list of possible values for each requested field.
     * <p>If value can be translated then each possible value will contain value-translations element, a map where key
     * is language code and value is translated value.</p>
     * <h3>Sample request:</h3><pre>
     * ["currency"]
     * </pre>
     * <h3>Sample response:</h3><pre>
     * {
     *   "type": [
     *     {
     *       "id": 34,
     *       "value": "USD",
     *       "translated-value": {
     *         "en": "USD"
     *       }
     *     }
     *   ]
     * }
     * </pre>
     *
     * @implicitParam translations|string|query|false|||||false|pipe separated list of language codes
     * @param fields list of fully qualified resource fields
     * @return list of possible values grouped by field
     */
    @POST
    @Path("field/values")
    @Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
    @ApiMethod(authTypes = AuthRule.AUTHENTICATED, id = "getResourceMultiValues", ui = false)
    public Map<String, List<PossibleValue>> getValues(List<String> fields) {
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
        return PossibleValuesEnumerator.INSTANCE.getPossibleValuesForField(fieldName, CommonSettings.class, null);
    }
    
}

    
