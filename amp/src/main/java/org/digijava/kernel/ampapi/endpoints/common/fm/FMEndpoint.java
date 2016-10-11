/**
 * 
 */
package org.digijava.kernel.ampapi.endpoints.common.fm;

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
     * Feature Manager Endpoint 
     * <dl>
     * <dt>reporting-fields</dt> <dd>true / false (default) - to list or not reporting-fields
     * <dt>enabled-modules</dt> <dd>true / false (default) - to list or not enabled modules
     * <dt>detail-modules</dt> <dd><pre>
     *         Option1: [“all-enabled”] (if present, other entries are skipped)
     *         Option2: a list of valid module names like [“GIS”], or [“GIS”, “Dashboards”]
     *         Option3: [] (i.e. None, which is the default if omitted)
     *         
     *         Additions:
     *          <dt>detail-flat</dt> <dd> true (default) / false - if to detail as a flat list or in a tree form
     *          <dt>full-enabled-paths<dt> <dd> true (default) / false - if only fully enabled paths to detail</dd>
     *  </pre>
     * @param config a JSON input. Sample Request:
     * <pre>
     *  {
     *    “reporting-fields” : true,
     *    “enabled-modules” : true,
     *    “detail-modules” : [“GIS”,” Dashboards”, ...]
     *    "detail-flat" : true,
     *    "full-enabled-paths" : true
     *  }
     * </pre>
     * @return requested information. Sample Output:
     * <pre>
     * {
     *  “reporting-fields” : [“Project Title”, “Primary Sector”, ...],
     *  “enabled-modules” : [“GIS”, “Dashboards”, ...],
     *  “GIS” : ["/gis-enabled-setting1/enabled-childX", "/enabled-setting2", ...],
     *  ...
     * }
     * 
     * Use cases:
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
     */
	@POST
	@Path("/fm")
	@Produces(MediaType.APPLICATION_JSON)
	@ApiMethod(ui=false, name="fm", id="")
	public JsonBean getFMSettings(JsonBean config) {
		return FMService.getFMSettings(config);
	}
}

	