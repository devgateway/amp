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

import org.digijava.module.aim.dbentity.AmpPerformanceRule;

/**
 * 
 * @author Viorel Chihai
 *
 */
@Path("performance")
public class PerformanceRulesEndpoint {

    PerfomanceRuleManager performanceRuleManager = PerfomanceRuleManager.getInstance();

    @GET
    @Path("rules")
    @Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
    public List<AmpPerformanceRule> getPerformanceRules() {
        return performanceRuleManager.getAllPerformanceRules();
    }

    @GET
    @Path("rules/{id}")
    @Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
    public AmpPerformanceRule getPerformanceRule(@PathParam("id") long id) {
        return performanceRuleManager.getPerformanceRuleById(id);
    }

    @POST
    @Path("rules")
    @Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
    public void savePerformanceRule(AmpPerformanceRule performanceRule) {
        performanceRuleManager.savePerformanceRule(performanceRule);
    }

    @PUT
    @Path("rules/{id}")
    @Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
    public void updatePerformanceRule(@PathParam("id") long id, AmpPerformanceRule performanceRule) {
        performanceRule.setId(id);
        performanceRuleManager.updatePerformanceRule(performanceRule);
    }

    @DELETE
    @Path("rules/{id}")
    @Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
    public void deletePerformanceRule(@PathParam("id") long id) {
        performanceRuleManager.deletePerformanceRule(id);
    }

    @GET
    @Path("admin")
    @Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
    public ResultPage<AmpPerformanceRule> getResultsPage(@QueryParam("page") int page, @QueryParam("size") int size) {

        return performanceRuleManager.getAdminPage(page, size);
    }

}
