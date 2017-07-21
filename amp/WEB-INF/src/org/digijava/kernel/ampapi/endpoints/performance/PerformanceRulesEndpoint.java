package org.digijava.kernel.ampapi.endpoints.performance;

import java.util.List;

import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import org.digijava.kernel.ampapi.endpoints.common.CategoryValueService;
import org.digijava.kernel.ampapi.endpoints.performance.matchers.PerformanceRuleMatcher;
import org.digijava.kernel.ampapi.endpoints.performance.matchers.PerformanceRuleMatcherAttribute;
import org.digijava.kernel.ampapi.endpoints.security.AuthRule;
import org.digijava.kernel.ampapi.endpoints.util.ApiMethod;
import org.digijava.kernel.ampapi.endpoints.util.JsonBean;
import org.digijava.module.aim.dbentity.AmpPerformanceRule;
import org.digijava.module.categorymanager.util.CategoryConstants;

/**
 * 
 * @author Viorel Chihai
 *
 */
@Path("performance")
public class PerformanceRulesEndpoint {

    private PerfomanceRuleManager performanceRuleManager = PerfomanceRuleManager.getInstance();

    @GET
    @Path("rules")
    @Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
    @ApiMethod(ui = false, id = "getRules", authTypes = { AuthRule.IN_ADMIN })
    public List<AmpPerformanceRule> getRules() {
        return performanceRuleManager.getPerformanceRules();
    }

    @GET
    @Path("rules/{id}")
    @Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
    @ApiMethod(ui = false, id = "getRule", authTypes = { AuthRule.IN_ADMIN })
    public AmpPerformanceRule getRule(@PathParam("id") long id) {
        return performanceRuleManager.getPerformanceRuleById(id);
    }

    @POST
    @Path("rules")
    @Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
    @ApiMethod(ui = false, id = "saveRule", authTypes = { AuthRule.IN_ADMIN })
    public void saveRule(AmpPerformanceRule performanceRule) {
        performanceRuleManager.savePerformanceRule(performanceRule);
    }

    @PUT
    @Path("rules/{id}")
    @Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
    @ApiMethod(ui = false, id = "updateRule", authTypes = { AuthRule.IN_ADMIN })
    public void updateRule(@PathParam("id") long id, AmpPerformanceRule performanceRule) {
        performanceRule.setId(id);
        performanceRuleManager.updatePerformanceRule(performanceRule);
    }

    @DELETE
    @Path("rules/{id}")
    @Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
    @ApiMethod(ui = false, id = "deleteRule", authTypes = { AuthRule.IN_ADMIN })
    public void deleteRule(@PathParam("id") long id) {
        performanceRuleManager.deletePerformanceRule(id);
    }

    @GET
    @Path("admin")
    @Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
    @ApiMethod(ui = false, id = "getRulesPage", authTypes = { AuthRule.IN_ADMIN })
    public ResultPage<AmpPerformanceRule> getRulesPage(@QueryParam("page") int page, @QueryParam("size") int size) {
        return performanceRuleManager.getPerformanceRules(page, size);
    }
    
    /**
     * Retrieve and provide performance levels. </br>
     * <dl>
     * The access types JSON object holds information regarding:
     * <dt><b>id</b>
     * <dd>- the id of the performance level
     * <dt><b>orig-name</b>
     * <dd>- the original name, not translated of the performance level
     * <dt><b>name</b>
     * <dd>- the name, translated of the performance level
     * </dl>
     * </br>
     * </br>
     *
     * <h3>Sample Output:</h3>
     * 
     * <pre>
     *  [
     *     {
     *      "id" : 123,
     *      "orig-name" : "Minor",
     *      "name" : "Minor"
     *     },
     *     ....
     *  ]
     * </pre>
     *
     * @return a list of JSON objects with the available performance levels
     */
    @GET
    @Path("/levels")
    @Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
    @ApiMethod(ui = false, id = "getLevels", authTypes = { AuthRule.IN_ADMIN })
    public List<JsonBean> getLevels() {
        return CategoryValueService.getCategoryValues(CategoryConstants.PERFORMANCE_LEVEL_KEY, true);
    }
    
    @GET
    @Path("/types")
    @Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
    @ApiMethod(ui = false, id = "getTypes", authTypes = { AuthRule.IN_ADMIN })
    public List<PerformanceRuleMatcher> getTypes() {
        return performanceRuleManager.getTypes();
    }
    
    @GET
    @Path("/attributes")
    @Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
    @ApiMethod(ui = false, id = "getAttributes", authTypes = { AuthRule.IN_ADMIN })
    public List<PerformanceRuleMatcherAttribute> getAttributes(@QueryParam("ruleType") String ruleType) {
        return performanceRuleManager.getAttributes(ruleType);
    }

}
