package org.digijava.kernel.ampapi.endpoints.performance;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.digijava.kernel.ampapi.endpoints.AmpEndpoint;
import org.digijava.kernel.ampapi.endpoints.common.CategoryValueLabel;
import org.digijava.kernel.ampapi.endpoints.common.CategoryValueService;
import org.digijava.kernel.ampapi.endpoints.dto.ResultPage;
import org.digijava.kernel.ampapi.endpoints.performance.matcher.definition.PerformanceRuleMatcherAttribute;
import org.digijava.kernel.ampapi.endpoints.performance.matcher.definition.PerformanceRuleMatcherDefinition;
import org.digijava.kernel.ampapi.endpoints.security.AuthRule;
import org.digijava.kernel.ampapi.endpoints.util.ApiMethod;
import org.digijava.module.aim.dbentity.AmpPerformanceRule;
import org.digijava.module.categorymanager.util.CategoryConstants;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.List;

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
    @ApiOperation("Retrieve and provide performance alert levels.")
    public List<CategoryValueLabel> getAlertLevels() {
        return CategoryValueService.getCategoryValues(CategoryConstants.PERFORMANCE_ALERT_LEVEL_KEY);
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
