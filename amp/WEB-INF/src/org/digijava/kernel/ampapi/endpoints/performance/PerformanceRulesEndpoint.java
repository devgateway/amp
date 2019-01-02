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

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.digijava.kernel.ampapi.endpoints.AmpEndpoint;
import org.digijava.kernel.ampapi.endpoints.common.CategoryValueService;
import org.digijava.kernel.ampapi.endpoints.dto.ResultPage;
import org.digijava.kernel.ampapi.endpoints.performance.matcher.definition.PerformanceRuleMatcherAttribute;
import org.digijava.kernel.ampapi.endpoints.performance.matcher.definition.PerformanceRuleMatcherDefinition;
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
@Api("performance")
public class PerformanceRulesEndpoint implements AmpEndpoint {

    private PerformanceRuleManager performanceRuleManager = PerformanceRuleManager.getInstance();

    @GET
    @Path("rules")
    @Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
    @ApiMethod(ui = false, id = "getRules", authTypes = { AuthRule.IN_ADMIN })
    @ApiOperation("Retrieves all performance rule objects.")
    public List<AmpPerformanceRule> getRules() {
        return performanceRuleManager.getPerformanceRules();
    }

    @GET
    @Path("rules/{id}")
    @Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
    @ApiMethod(ui = false, id = "getRule", authTypes = { AuthRule.IN_ADMIN })
    @ApiOperation("Retrieves a performance rule object by id.")
    public AmpPerformanceRule getRule(
            @ApiParam("the ID that will be used to query the database") @PathParam("id") long id) {
        return performanceRuleManager.getPerformanceRuleById(id);
    }

    @POST
    @Path("rules")
    @Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
    @ApiMethod(ui = false, id = "saveRule", authTypes = { AuthRule.IN_ADMIN })
    @ApiOperation("Saves a performance rule object.")
    public void saveRule(AmpPerformanceRule performanceRule) {
        performanceRuleManager.savePerformanceRule(performanceRule);
    }

    @PUT
    @Path("rules/{id}")
    @Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
    @ApiMethod(ui = false, id = "updateRule", authTypes = { AuthRule.IN_ADMIN })
    @ApiOperation("Updates a performance rule object by id.")
    public void updateRule(
            @ApiParam("the ID that will be used to query the database") @PathParam("id") long id,
            @ApiParam("performance rule") AmpPerformanceRule performanceRule) {
        performanceRule.setId(id);
        performanceRuleManager.updatePerformanceRule(performanceRule);
    }

    @DELETE
    @Path("rules/{id}")
    @Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
    @ApiMethod(ui = false, id = "deleteRule", authTypes = { AuthRule.IN_ADMIN })
    @ApiOperation("Deletes the performance rule object by id.")
    public void deleteRule(@ApiParam("the ID that will be used to query the database") @PathParam("id") long id) {
        performanceRuleManager.deletePerformanceRule(id);
    }

    @GET
    @Path("admin")
    @Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
    @ApiMethod(ui = false, id = "getRulesPage", authTypes = { AuthRule.IN_ADMIN })
    @ApiOperation("Retrieves a list of performance rule objects.")
    public ResultPage<AmpPerformanceRule> getRulesPage(
            @ApiParam("the offset page") @QueryParam("page") int page,
            @ApiParam("number of records per page") @QueryParam("size") int size) {
        return performanceRuleManager.getPerformanceRules(page, size);
    }
    
    @GET
    @Path("/levels")
    @Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
    @ApiMethod(ui = false, id = "getLevels", authTypes = { AuthRule.IN_ADMIN })
    @ApiOperation(
            value = "Retrieve and provide performance alert levels.",
            notes = "<dl>\n"
                    + "The access types JSON object holds information regarding:\n"
                    + "<dt><b>id</b>\n"
                    + "<dd>- the id of the performance level\n"
                    + "<dt><b>orig-name</b>\n"
                    + "<dd>- the performance alert level name, not translated\n"
                    + "<dt><b>name</b>\n"
                    + "<dd>- the performance alert level name, translated\n"
                    + "</dl>\n"
                    + "</br>\n"
                    + "</br>\n"
                    + "<h3>Sample Output:</h3>\n"
                    + "<pre>\n"
                    + " [\n"
                    + "    {\n"
                    + "     \"id\" : 123,\n"
                    + "     \"orig-name\" : \"Minor\",\n"
                    + "     \"name\" : \"Minor\"\n"
                    + "    },\n"
                    + "    ....\n"
                    + " ]\n"
                    + "</pre>")
    public List<JsonBean> getAlertLevels() {
        return CategoryValueService.getCategoryValues(CategoryConstants.PERFORMANCE_ALERT_LEVEL_KEY, true);
    }
    
    @GET
    @Path("/types")
    @Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
    @ApiMethod(ui = false, id = "getTypes", authTypes = { AuthRule.IN_ADMIN })
    @ApiOperation("Retrieves all performance rule types.")
    public List<PerformanceRuleMatcherDefinition> getTypes() {
        return performanceRuleManager.getPerformanceRuleDefinitions();
    }
    
    @GET
    @Path("/attributes")
    @Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
    @ApiMethod(ui = false, id = "getAttributes", authTypes = { AuthRule.IN_ADMIN })
    @ApiOperation("Retrieves parameters(attributes) of the specified rule type.")
    public List<PerformanceRuleMatcherAttribute> getAttributes(
            @ApiParam("the name of the type that will be used to query the database")
            @QueryParam("rule-type") String ruleType) {
        return performanceRuleManager.getMatcherDefinition(ruleType).getAttributes();
    }

}
