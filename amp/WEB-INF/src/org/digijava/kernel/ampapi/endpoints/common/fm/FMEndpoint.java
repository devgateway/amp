/**
 * 
 */
package org.digijava.kernel.ampapi.endpoints.common.fm;

import java.util.List;

import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.digijava.kernel.ampapi.endpoints.util.ApiMethod;
import org.digijava.kernel.ampapi.endpoints.util.JsonBean;

/**
 * Feature Manager Endpoint provides FM settings
 * 
 * @author Nadejda Mandrescu
 */
@Path("common")
public class FMEndpoint {
    
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
    @ApiMethod(ui=false, name="fm", id="")
    public JsonBean getFMSettings(JsonBean config) {
        return FMService.getCurrentUserFMSettings(config);
    }
    
    /**
     * Provides FM (Feature Manager) settings for the requested options grouped by workspace member ids
     * The settings will be taken from the each FM template. 
     * 
     * <h3>Sample Output:</h3><pre>
     * {
     *   "ws-member-ids" : [1, 3, 5, ...],
     *   "fm-tree" : {
     *     "reporting-fields" : ["Project Title", "Primary Sector", ...],
     *     "enabled-modules" : ["GIS", "Dashboards", ...],
     *     "GIS" : ["/gis-enabled-setting1/enabled-childX", "/enabled-setting2", ...],
     *     ...
     *   }
     * },
     * {
     *   "ws-member-ids" : [4, 7],
     *   "fm-tree" : {
     *     "reporting-fields" : ["Project Title", "Primary Sector", ...],
     *     "enabled-modules" : ["GIS", ...],
     *     "GIS" : ["/gis-enabled-setting1/enabled-childX", "/enabled-setting2", ...],
     *     ...
     *   }
     * }
     * </pre>
     * 
     * 
     * 
     * @param config config a JSON object with requested options (see the example from /fm) 
     * @return list of FM settings grouped by workspace member ids
     */
    @POST
    @Path("/fm-by-ws-member")
    @Produces(MediaType.APPLICATION_JSON)
    @ApiMethod(ui = false, name = "fm-by-ws-member", id = "fm-by-ws-member")
    public List<JsonBean> getFMSettingsByWsMember(JsonBean config) {
        return FMService.getFMSettingsGroupedByWsMember(config);
    }
}

    
