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
public class PerformanceRulesEndpoint {

    private PerformanceRuleManager performanceRuleManager = PerformanceRuleManager.getInstance();

    /**
     * Retrieves all performance rule objects.
     * </br>
     * <dl>
     * </br>
     * Each performance rule object holds information regarding:
     * <dt><b>id</b><dd> - performance rule id
     * <dt><b>name</b><dd> - performance rule name
     * <dt><b>description</b><dd> - performance rule description
     * <dt><b>type-class-name</b><dd> - type of performance rule definition
     * <dt><b>level</b><dd> - performance issue level
     * <dt><b>attributes</b><dd> - performance rule attributes
     * </dl></br></br>
     * 
     * <h3>Sample Output:</h3>
     * <pre>
     * [
     *   {
     *     "id" : 1,
     *     "name" : "Rule No Disbursments 2",
     *     "description" : "hello",
     *     "type-class-name" : "noDisbursementsAfterFundingDate",
     *     "level" : {
     *              "id": 2309
     *              "value" : "Major"
     *     },
     *     "enabled" : true,
     *     "attributes": [
     *       {
     *          "name": "timeAmount",
     *          "value": "20",
     *          "type": "AMOUNT"
     *       }
     *     ]
     *   }
     *   ...
     *   {
     *     "id" : 2,
     *     "name" : "Rule No Disbursments 12",
     *     "description" : "test 2",
     *     "type-class-name" : "noDisbursementsAfterFundingDate",
     *     "level" : {
     *              "id": 2302
     *              "value" : "Minor"
     *     },
     *     "enabled" : false,
     *     "attributes": [
     *       {
     *          "name": "timeAmount",
     *          "value": "10",
     *          "type": "AMOUNT"
     *       }
     *     ]
     *   }
     *  ]
     * </pre>
     *
     * @return all available performance rules
     */
    @GET
    @Path("rules")
    @Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
    @ApiMethod(ui = false, id = "getRules", authTypes = { AuthRule.IN_ADMIN })
    public List<AmpPerformanceRule> getRules() {
        return performanceRuleManager.getPerformanceRules();
    }

    /**
     * Retrieves a performance rule object by id.
     * </br>
     * <dl>
     * </br>
     * The JSON object holds information regarding:
     * <dt><b>id</b><dd> - performance rule id
     * <dt><b>name</b><dd> - performance rule name
     * <dt><b>description</b><dd> - performance rule description
     * <dt><b>type-class-name</b><dd> - type of performance rule definition
     * <dt><b>level</b><dd> - performance issue level
     * <dt><b>enabled</b><dd> - if the performance rule is enabled
     * <dt><b>attributes</b><dd> - performance rule attributes
     * </dl></br></br>
     * 
     * <h3>Sample Output:</h3>
     * <pre>
     * {
     *     "id" : 1,
     *     "name" : "Rule No Disbursments 2",
     *     "description" : "hello",
     *     "type-class-name" : "noDisbursementsAfterFundingDate",
     *     "level" : {
     *              "id": 2309
     *              "value" : "Minor"
     *     },
     *     "enabled" : true,
     *     "attributes": [
     *       {
     *          "name": "timeAmount",
     *          "value": "20",
     *          "type": "AMOUNT"
     *       }
     *     ]
     *  }
     * </pre>
     * 
     * @param id the ID that will be used to query the database
     * @return performance rule object
     */
    @GET
    @Path("rules/{id}")
    @Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
    @ApiMethod(ui = false, id = "getRule", authTypes = { AuthRule.IN_ADMIN })
    public AmpPerformanceRule getRule(@PathParam("id") long id) {
        return performanceRuleManager.getPerformanceRuleById(id);
    }

    /**
     * Saves a performance rule object.
     * </br>
     * <dl>
     * </br>
     * The JSON object holds information regarding:
     * <dt><b>name</b><dd> - performance rule name
     * <dt><b>description</b><dd> - performance rule description
     * <dt><b>type-class-name</b><dd> - type of performance rule definition
     * <dt><b>level</b><dd> - performance issue level
     * <dt><b>enabled</b><dd> - if the performance rule is enabled
     * <dt><b>attributes</b><dd> - performance rule attributes
     * </dl></br></br>
     * 
     * <h3>Sample Input:</h3>
     * <pre>
     * {
     *     "name" : "Rule No Disbursments 2",
     *     "description" : "hello",
     *     "type-class-name" : "noDisbursementsAfterFundingDate",
     *     "level" : {
     *              "id": 2309
     *     },
     *     "enabled" : true,
     *     "attributes": [
     *       {
     *          "name": "timeAmount",
     *          "value": "20",
     *          "type": "AMOUNT"
     *       }
     *     ]
     *  }
     * </pre>
     * 
     * @param performanceRule the performance rule object in JSON format that will be inserted into the database
     */
    @POST
    @Path("rules")
    @Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
    @ApiMethod(ui = false, id = "saveRule", authTypes = { AuthRule.IN_ADMIN })
    public void saveRule(AmpPerformanceRule performanceRule) {
        performanceRuleManager.savePerformanceRule(performanceRule);
    }

    /**
     * Updates a performance rule object by id.
     * </br>
     * <dl>
     * </br>
     * The JSON object holds information regarding:
     * <dt><b>name</b><dd> - performance rule name
     * <dt><b>description</b><dd> - performance rule description
     * <dt><b>type-class-name</b><dd> - type of performance rule definition
     * <dt><b>enabled</b><dd> - if the rule is enabled
     * <dt><b>level</b><dd> - performance issue level
     * <dt><b>enabled</b><dd> - if the performance rule is enabled
     * <dt><b>attributes</b><dd> - performance rule attributes
     * </dl></br></br>
     * 
     * <h3>Sample Input:</h3>
     * <pre>
     * {
     *     "name" : "Rule No Disbursments 2",
     *     "description" : "hello",
     *     "type-class-name" : "noDisbursementsAfterFundingDate",
     *     "level" : {
     *              "id": 2310
     *     },
     *     "enabled" : true,
     *     "attributes": [
     *       {
     *          "name": "timeAmount",
     *          "value": "20",
     *          "type": "AMOUNT"
     *       }
     *     ]
     *  }
     * </pre>
     * 
     * @param id the ID that will be used to query the database
     * @param performanceRule the performance rule object in JSON format that will be used for updating
     */
    @PUT
    @Path("rules/{id}")
    @Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
    @ApiMethod(ui = false, id = "updateRule", authTypes = { AuthRule.IN_ADMIN })
    public void updateRule(@PathParam("id") long id, AmpPerformanceRule performanceRule) {
        performanceRule.setId(id);
        performanceRuleManager.updatePerformanceRule(performanceRule);
    }

    /**
     * Deletes the performance rule object by id.
     * 
     * @param id the ID that will be used to query the database
     */
    @DELETE
    @Path("rules/{id}")
    @Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
    @ApiMethod(ui = false, id = "deleteRule", authTypes = { AuthRule.IN_ADMIN })
    public void deleteRule(@PathParam("id") long id) {
        performanceRuleManager.deletePerformanceRule(id);
    }

    /**
     * Retrieves a list of performance rule objects. </br>
     * <dl>
     * </br>
     * The JSON object holds information regarding:
     * <dt><b>data</b><dd> - list of performance rule objects
     * <dt><b>totalRecords</b><dd> - total number of performance rule records
     * </dl></br></br>
     * 
     * <h3>Sample Output:</h3>
     * <pre>
     * {
     * "data" : [
     *   {
     *      "id": 10,
     *      "name": "Rule No Disbursments",
     *      "description": "Description 2as",
     *      "type-class-name": "disbursementsAfterActivityDate",
     *      "enabled": true,
     *      "level": {
     *          "id": 272,
     *          "value": "Critical"
     *      },
     *      "attributes": [
     *          {
     *              "name": "timeAmount",
     *              "value": "30",
     *              "type": "AMOUNT"
     *          }
     *      ]
     *   }
     *  ],
     *  "totalRecords" : 5  
     *  } 
     * </pre>
     * @param page the offset page
     * @param size number of records per page
     * @return A JSON object with a list of performance rules and paging information
     */
    @GET
    @Path("admin")
    @Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
    @ApiMethod(ui = false, id = "getRulesPage", authTypes = { AuthRule.IN_ADMIN })
    public ResultPage<AmpPerformanceRule> getRulesPage(@QueryParam("page") int page, @QueryParam("size") int size) {
        return performanceRuleManager.getPerformanceRules(page, size);
    }
    
    /**
     * Retrieve and provide performance alert levels. </br>
     * <dl>
     * The access types JSON object holds information regarding:
     * <dt><b>id</b>
     * <dd>- the id of the performance level
     * <dt><b>orig-name</b>
     * <dd>- the performance alert level name, not translated
     * <dt><b>name</b>
     * <dd>- the performance alert level name, translated
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
     * @return all available performance alert levels
     */
    @GET
    @Path("/levels")
    @Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
    @ApiMethod(ui = false, id = "getLevels", authTypes = { AuthRule.IN_ADMIN })
    public List<JsonBean> getAlertLevels() {
        return CategoryValueService.getCategoryValues(CategoryConstants.PERFORMANCE_ALERT_LEVEL_KEY, true);
    }
    
    /**
     * Retrieves all performance rule types.
     * </br>
     * <dl>
     * </br>
     * Each performance rule type object holds information regarding:
     * <dt><b>name</b><dd> - type name
     * <dt><b>description</b><dd> - type description
     * <dt><b>message</b><dd> - type message
     * </dl></br></br>
     * 
     * <h3>Sample Output:</h3>
     * <pre>
     * [
     *   {
     *      "name": "noUpdatedStatusAfterFundingDate",
     *      "description": "No updated status after selected funding date",
     *      "message": "{timeAmount} {timeUnit} went by after the '{fundingDate}' and the project status was not 
     *        modified to '{activityStatus}'"
     *   },
     *   {
     *      "name": "noDisbursementsAfterFundingDate",
     *      "description": "No disbursements after selected funding date",
     *      "message": "{timeAmount} {timeUnit} have passed since the '{fundingDate}' 
     *        and still no disbursement from donor"
     *   }
     * ]
     * </pre>
     *
     * @return all available performance rule types
     */
    @GET
    @Path("/types")
    @Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
    @ApiMethod(ui = false, id = "getTypes", authTypes = { AuthRule.IN_ADMIN })
    public List<PerformanceRuleMatcherDefinition> getTypes() {
        return performanceRuleManager.getPerformanceRuleDefinitions();
    }
    
    /**
     * Retrieves parameters(attributes) of the specified rule type.
     * </br>
     * <dl>
     * </br>
     * Each attribute object holds information regarding:
     * <dt><b>name</b><dd> - attribute name
     * <dt><b>description</b><dd> - attribute description
     * <dt><b>type</b><dd> - attribute type
     * <dt><b>possible-values</b><dd> - possible values of the attribute
     * </dl></br></br>
     * 
     * <h3>Sample Output:</h3>
     * <pre>
     * [
     *  {
     *      "name": "timeAmount",
     *      "description": "Time Amount",
     *      "type": "AMOUNT",
     *      "possible-values": null
     *  },
     *  {
     *      "name": "fundingDate",
     *      "description": "Funding Date",
     *      "type": "FUNDING_DATE",
     *      "possible-values": [
     *          {
     *              "name": "fundingClassificationDate",
     *              "label": "Funding Classification Date",
     *              "visible": true,
     *              "translated-label": "Funding Classification Date"
     *          },
     *          {
     *              "name": "fundingEffectiveDate",
     *              "label": "Effective Funding Date",
     *              "visible": true,
     *              "translated-label": "Effective Funding Date"
     *          },
     *          {
     *              "name": "fundingClosingDate",
     *              "label": "Funding Closing Date",
     *              "visible": false,
     *              "translated-label": "Funding Closing Date"
     *          }
     *      ]
     *  }
     * ]
     * </pre>
     *
     * @param ruleType the name of the type that will be used to query the database
     * @return available performance rule parameters(attributes) of the specified type
     */
    @GET
    @Path("/attributes")
    @Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
    @ApiMethod(ui = false, id = "getAttributes", authTypes = { AuthRule.IN_ADMIN })
    public List<PerformanceRuleMatcherAttribute> getAttributes(@QueryParam("rule-type") String ruleType) {
        return performanceRuleManager.getMatcherDefinition(ruleType).getAttributes();
    }

}
