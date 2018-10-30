package org.digijava.kernel.ampapi.endpoints.dashboards;

import java.util.List;

import javax.ws.rs.DefaultValue;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import org.digijava.kernel.ampapi.endpoints.common.EndpointUtils;
import org.digijava.kernel.ampapi.endpoints.dashboards.services.DashboardsService;
import org.digijava.kernel.ampapi.endpoints.dashboards.services.HeatMapConfigs;
import org.digijava.kernel.ampapi.endpoints.dashboards.services.HeatMapService;
import org.digijava.kernel.ampapi.endpoints.dashboards.services.TopsChartService;
import org.digijava.kernel.ampapi.endpoints.errors.ErrorReportingEndpoint;
import org.digijava.kernel.ampapi.endpoints.security.AuthRule;
import org.digijava.kernel.ampapi.endpoints.util.ApiMethod;
import org.digijava.kernel.ampapi.endpoints.util.JsonBean;


/**
 * 
 * @author Diego Dimunzio
 * - All dashboards end points
 */

@Path("dashboard")
public class EndPoints implements ErrorReportingEndpoint {

    /**
     * Retrieve a list of available top for the dashboard charts with their names.
     * </br>
     * <dl>
     * This EP was hardcoded and will return always "Donor Agency", "Region", "Primary Sector".
     * </br>
     * The JSON object holds information regarding:
     * <dt><b>id</b><dd> - the id of top
     * <dt><b>name</b><dd> - the name of top
     * </dl></br></br>
     *
     * </br>
     * <h3>Sample Output:</h3><pre>
     * [
     *   {
     *     "id": "do",
     *     "name": "Donor Agency"
     *   },
     *   {
     *     "id": "re",
     *     "name": "Region"
     *   },
     *   {
     *     "id": "ps",
     *     "name": "Primary Sector"
     *   }
     * ]</pre>
     *
     * @return a list of JSON objects with the tops
     */
    @GET
    @Path("/tops")
    @Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
    @ApiMethod(ui = false, id = "topsList")
    public List<JsonBean> getAdminLevelsTotalslist() {
        return DashboardsService.getTopsList();
    }

    /**
     * Retrieve top donors values for dashboards chart.
     * </br>
     * <dl>
     * where Type (Chart type) :
     *    do = Donor
     *    re = Region
     *    ps = Primary Sector
     *    dg = Donor Group
     * </br>
     * The JSON object holds information regarding:
     * <dt><b>currency</b><dd> - currency of the report that is going to be run to retrieve the information
     * <dt><b>values</b><dd> - an array with a list of donors and the amount
     * <dt><b>total</b><dd> -  total amount
     * <dt><b>sumarizedTotal</b><dd> - sumarized total amount
     * <dt><b>maxLimit</b><dd> - number of donors
     * <dt><b>name</b><dd> - name of the report
     * </dl></br></br>
     *
     * <h3>Sample Input:</h3><pre>
     * {
     *  "filters": {},
     *  "settings": {
     *      "funding-type": ["Actual Commitments","Actual Disbursements"],
     *      "currency-code": "USD",
     *      "calendar-id": "123",
     *      "year-range": {
     *          "from": "2014",
     *          "to": "2015"
     *      }
     *  }
     * }</pre>
     * </br>
     * <h3>Sample Output:</h3><pre>
     * {
     *   "currency": "USD",
     *   "values": [
     *     {
     *       "name": "DFAT",
     *       "id": 633,
     *       "amount": 627838042.569743,
     *       "formattedAmount": "627,838,043"
     *     },
     *     {
     *       "name": "ADB",
     *       "id": 634,
     *       "amount": 300051591,
     *       "formattedAmount": "300,051,591"
     *     },
     *     ....
     *   ],
     *   "total": 2398018313.370719,
     *   "sumarizedTotal": "2,4B",
     *   "maxLimit": 85,
     *   "totalPositive": 2398018313.3707194,
     *   "name": "Top Donor Agencies",
     *   "title": "Top Donor Agencies"
     * }</pre>
     *
     * @param config a JSON object with the config
     * @param type chart type
     * @param limit limit of result. default 5.
     *
     * @return a JSON objects with a tops donors
     */
    @POST 
    @Path("/tops/{type}")
    @Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
    @ApiMethod(ui = false, id = "tops")
    //TODO: Implement Filters
    public JsonBean getAdminLevelsTotals(JsonBean config,
            @PathParam("type") String type,
            @DefaultValue("5") @QueryParam("limit") Integer limit) {
        //return DashboardsService.getTops(type, null, limit, config);
        return new TopsChartService(config, type, limit).buildChartData();
    }

    /**
     * Retrieve a list of projects by type query for selected id.
     * </br>
     * <dl>
     * where Type (Chart type) :
     *    do = Donor
     *    re = Region
     *    ps = Primary Sector
     *    dg = Donor Group
     * </br>
     * The JSON object holds information regarding:
     * <dt><b>totalRecords</b><dd> - number total of projects.
     * <dt><b>values</b><dd> - array with a list of projects.
     *     name - name of the project.
     *     amount - amount of the project.
     *     formattedAmount - formatted amount of the project.
     *     id - id of the project.
     * </dl></br></br>
     *
     * <h3>Sample Input:</h3><pre>
     * {
     *  "filters": {},
     *  "settings": {
     *      "funding-type": ["Actual Commitments","Actual Disbursements"],
     *      "currency-code": "USD",
     *      "calendar-id": "123",
     *      "year-range": {
     *          "from": "2014",
     *          "to": "2015"
     *      }
     *  }
     * }</pre>
     * </br>
     * <h3>Sample Output:</h3><pre>
     * {
     *     "totalRecords": 10,
     *     "values": [{
     *         "name": "Alimentation en eau de la ville d'Abidjan à partir de la nappe du Sud Comoé (Bonoua) - Phase I",
     *         "amount": 104422920.000000000000,
     *         "formattedAmount": "104 422 920",
     *         "id": 19003
     *     },
     *  .....
     *  {
     *         "name": "Construction de l'autoroute Abidjan-Bassam",
     *         "amount": 114777280.000000000000,
     *         "formattedAmount": "114 777 280",
     *         "id": 19111
     *     }]
     * }</pre>
     *
     * @param config a JSON object with the config
     * @param type chart type
     * @param id of the category to query the projects.
     *
     * @return a JSON objects with a list of projects
     */
    @POST
    @Path("/tops/{type}/{id}")
    @Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
    @ApiMethod(ui = false, id = "topsDataDetail")
    public JsonBean getChartsDataDetail(JsonBean config, @PathParam("type") String type, @PathParam("id") Long id) {
        return new TopsChartService(config, type, id).buildChartData();
    }

    /**
     * Retrieve aid predictability values for dashboards chart.
     * </br>
     * <dl>
     * The JSON object holds information regarding:
     * <dt><b>years</b><dd> - an array of years with the funding type and the amount
     * <dt><b>totals</b><dd> - total by funding type
     * <dt><b>currency</b><dd> - currency of the report that is going to be run to retrieve the information
     * <dt><b>name</b><dd> - name of the report that is going to be run to retrieve the information
     * <dt><b>title</b><dd> - title of the report that is going to be run to retrieve the information
     * <dt><b>measure</b><dd> - measure type "disbursements"
     * </dl></br></br>
     *
     * <h3>Sample Input:</h3><pre>
     * {
     *  "filters": {},
     *  "settings": {
     *      "funding-type": ["Actual Commitments","Actual Disbursements"],
     *      "currency-code": "USD",
     *      "calendar-id": "123",
     *      "year-range": {
     *          "from": "2014",
     *          "to": "2015"
     *      }
     *  }
     * }</pre>
     * </br>
     * <h3>Sample Output:</h3><pre>
     * {
     *   "years": [
     *     {
     *       "planned disbursements": {
     *         "amount": 204848995.768391,
     *         "formattedAmount": "204,848,996"
     *       },
     *       "actual disbursements": {
     *         "amount": 299328665.562998,
     *         "formattedAmount": "299,328,666"
     *       },
     *       "year": "2014"
     *     },
     *     {
     *       "planned disbursements": {
     *         "amount": 234745905.771138,
     *         "formattedAmount": "234,745,906"
     *       },
     *       "actual disbursements": {
     *         "amount": 244360092.647041,
     *         "formattedAmount": "244,360,093"
     *       },
     *       "year": "2015"
     *     },
     *
     *   ],
     *   "totals": {
     *     "planned disbursements": {
     *       "amount": 2025184338.353028,
     *       "formattedAmount": "2,025,184,338"
     *     },
     *     "actual disbursements": {
     *       "amount": 2079819299.507724,
     *       "formattedAmount": "2,079,819,300"
     *     }
     *   },
     *   "currency": "USD",
     *   "name": "Aid Predictability",
     *   "title": "Aid predictability",
     *   "measure": "disbursements"
     * }</pre>
     *
     * @param filter a JSON with a filter and the settings
     *
     * @return a JSONObject objects with the years, amounts by funding type, currency and total amount
     */
    @POST 
    @Path("/aid-predictability")
    @Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
    @ApiMethod(ui = false, id = "aidPredictability")
    public JsonBean getAidPredictability(JsonBean filter) throws Exception {
        return DashboardsService.getAidPredictability(filter);
    }

    /**
     * Retrieve a list of projects by aid predictability year.
     * </br>
     * <dl>
     * The JSON object holds information regarding:
     * <dt><b>totalRecords</b><dd> - number total of projects.
     * <dt><b>values</b><dd> - array with a list of projects.
     *     name - name of the project.
     *     amount - amount of the project.
     *     formattedAmount - formatted amount of the project.
     *     id - id of the project.
     * </dl></br></br>
     *
     * <h3>Sample Input:</h3><pre>
     * {
     *  "filters": {},
     *  "settings": {
     *      "funding-type": ["Actual Commitments","Actual Disbursements"],
     *      "currency-code": "USD",
     *      "calendar-id": "123",
     *      "year-range": {
     *          "from": "2014",
     *          "to": "2015"
     *      }
     *  }
     * }</pre>
     * </br>
     * <h3>Sample Output:</h3><pre>
     * {
     *     "totalRecords": 10,
     *     "values": [{
     *         "name": "Alimentation en eau de la ville d'Abidjan à partir de la nappe du Sud Comoé (Bonoua) - Phase I",
     *         "amount": 104422920.000000000000,
     *         "formattedAmount": "104 422 920",
     *         "id": 19003
     *     },
     *  .....
     *  {
     *         "name": "Construction de l'autoroute Abidjan-Bassam",
     *         "amount": 114777280.000000000000,
     *         "formattedAmount": "114 777 280",
     *         "id": 19111
     *     }]
     * }</pre>
     *
     * @param filter a JSON with a filter and the settings
     * @param year a year to query the projects
     * @param measure
     *
     * @return a JSON objects with the projects list.
     */
    @POST
    @Path("/aid-predictability/{year}/{measure}")
    @Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
    @ApiMethod(ui = false, id = "aidPredictabilityDataDetail")
    public JsonBean getAidPredictabilityDataDetail(JsonBean filter, @PathParam("year") String year,
            @PathParam("measure") String measure) {
        return DashboardsService.getAidPredictability(filter, measure, year);
    }

    /**
     * Get funding types by year.
     * </br>
     * <dl>
     * the parameter adjtype is never used
     * The JSON object holds information regarding:
     * <dt><b>total</b><dd> - total amount
     * <dt><b>sumarizedTotal</b><dd> - sumarized total amount
     * <dt><b>currency</b><dd> - currency of the report
     * <dt><b>values</b><dd> - an array with the year and a list of types of assistence and the amount
     * <dt><b>name</b><dd> - name of the report
     * <dt><b>title</b><dd> - title of the report
     * </dl></br></br>
     *
     * </br>
     * <h3>Sample Input:</h3><pre>
     * {
     *  "filters": {},
     *  "settings": {
     *      "funding-type": ["Actual Commitments","Actual Disbursements"],
     *      "currency-code": "USD",
     *      "calendar-id": "123",
     *      "year-range": {
     *          "from": "2014",
     *          "to": "2015"
     *      }
     *  }
     * }</pre>
     * </br>
     * <h3>Sample Output:</h3><pre>
     * {
     *   "total": 154123105.30153,
     *   "sumarizedTotal": "154,1M",
     *   "currency": "USD",
     *   "values": [
     *     {
     *       "Year": "2007",
     *       "values": [
     *         {
     *           "type": "Loan",
     *           "amount": 0,
     *           "formattedAmount": "0"
     *         },
     *         {
     *           "type": "Loan",
     *           "amount": 0,
     *           "formattedAmount": "0"
     *         }
     *
     *       ]
     *     },
     *     {
     *       "Year": "2008",
     *       "values": [
     *         {
     *           "type": "Loan",
     *           "amount": 0,
     *           "formattedAmount": "0"
     *         },
     *         {
     *           "type": "Loan",
     *           "amount": 0,
     *           "formattedAmount": "0"
     *         },
     *         {
     *           "type": "Grant",
     *           "amount": 6500000,
     *           "formattedAmount": "6,500,000"
     *         },
     *         ....
     *       ]
     *     },
     *     ....
     *   ],
     *   "name": "Funding Type",
     *   "title": "Funding type"
     * }</pre>
     *
     * @param config a JSON object with the configuration that is going to be used by the report to get the funding-type
     * @param adjtype a funding type
     *
     * @return a JSON object
     */
    @POST 
    @Path("/ftype")
    @Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
    @ApiMethod(ui = false, id = "ftype")
    //TODO: Implement Filters
    public JsonBean getfundingtype(JsonBean config,
            @DefaultValue("ac") @QueryParam("adjtype") String adjtype) {
        return DashboardsService.getFundingType(adjtype, config);
    }

    /**
     * Retrieve a list of projects by funding types year.
     * </br>
     * <dl>
     * The JSON object holds information regarding:
     * <dt><b>totalRecords</b><dd> - number total of projects.
     * <dt><b>values</b><dd> - array with a list of projects.
     *     name - name of the project.
     *     amount - amount of the project.
     *     formattedAmount - formatted amount of the project.
     *     id - id of the project.
     * </dl></br></br>
     *
     * </br>
     * <h3>Sample Input:</h3><pre>
     * {
     *  "filters": {},
     *  "settings": {
     *      "funding-type": ["Actual Commitments","Actual Disbursements"],
     *      "currency-code": "USD",
     *      "calendar-id": "123",
     *      "year-range": {
     *          "from": "2014",
     *          "to": "2015"
     *      }
     *  }
     * }</pre>
     * </br>
     * <h3>Sample Output:</h3><pre>
     * {
     *     "totalRecords": 10,
     *     "values": [{
     *         "name": "Alimentation en eau de la ville d'Abidjan à partir de la nappe du Sud Comoé (Bonoua) - Phase I",
     *         "amount": 104422920.000000000000,
     *         "formattedAmount": "104 422 920",
     *         "id": 19003
     *     },
     *  .....
     *  {
     *         "name": "Construction de l'autoroute Abidjan-Bassam",
     *         "amount": 114777280.000000000000,
     *         "formattedAmount": "114 777 280",
     *         "id": 19111
     *     }]
     * }</pre>
     *
     * @param config a JSON object with the configuration that is going to be used by the report to get the funding-type
     * @param adjtype a funding type
     * @param year a year to query the projects
     * @param id of the funding type
     *
     * @return a JSON objects with the projects list.
     */
    @POST
    @Path("/ftype/{year}/{id}")
    @Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
    @ApiMethod(ui = false, id = "ftypeDataDetail")
    //TODO: Implement Filters
    public JsonBean getfundingtypeDataDetail(JsonBean config,
                                   @DefaultValue("ac") @QueryParam("adjtype") String adjtype, 
                                   @PathParam("year") String year, @PathParam("id") Integer id) {
        return DashboardsService.getFundingType(adjtype, config, year, id);
    }

    /**
     * Save the state of a chart to be able to share it.
     * </br>
     * <dl>
     * The JSON object holds information regarding:
     * <dt><b>mapId</b><dd> - map id
     * </dl></br></br>
     *
     * <h3>Sample Input:</h3><pre>
     * {
     *     "title" : "Dashboard",
     *     "description" : "Saved dashboard",
     *     "stateBlob" : "{"chart:/rest/dashboard/tops/do":{"limit":5,"adjtype":"Actual Commitments","view":"bar","big":false},"chart:/rest/dashboard/tops/dg":{"limit":5,"adjtype":"Actual Commitments","view":"bar","big":false},"chart:/rest/dashboard/tops/re":{"limit":5,"adjtype":"A (...)"
     * }</pre>
     * </br>
     * <h3>Sample Output:</h3><pre>
     * {
     *     "mapId": 155
     * }</pre>
     *
     * @param pChart a JSON object with the config
     *
     * @return a JSON object with the map Id
     */
    @POST
    @Path("/saved-charts")
    @Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
    @ApiMethod(ui = false, id = "SaveChart")
    public JsonBean savedMaps(final JsonBean pChart) {
        return EndpointUtils.saveApiState(pChart,"C");
    }

    /**
     * Retrieve a saved chart by Id.
     * </br>
     * <dl>
     * The JSON object holds information regarding:
     * <dt><b>id</b><dd> - map id
     * <dt><b>title</b><dd> - a chart title
     * <dt><b>description</b><dd> - a chart description
     * <dt><b>stateBlob</b><dd> - a chart blob
     * <dt><b>created</b><dd> - a creation date
     * <dt><b>lastAccess</b><dd> - a last access date
     * </dl></br></br>
     *
     * </br>
     * <h3>Sample Output:</h3><pre>
     * {
     *     "id": 155,
     *     "title": "title",
     *     "description": "description",
     *     "stateBlob": "{"chart:/rest/dashboard/tops/do":{"limit":5,"adjtype":"Actual Commitments","view":"bar","big":false},"chart:/rest/dashboard/tops/dg":{"limit":5,"adjtype":"Actual Commitments","view":"bar","big":false},"chart:/rest/dashboard/tops/re":{"limit":5,"adjtype":"A (...)",
     *     "created": "15/12/2016T11:02Z",
     *     "lastAccess": "15/12/2016T11:12Z"
     * }</pre>
     *
     * @param chartId chart Id to query
     *
     * @return a JSON object with the chart information
     */
    @GET
    @Path("/saved-charts/{chartId}")
    @Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
    @ApiMethod(ui = false, id = "ChartById")
    public JsonBean savedCharts(@PathParam("chartId") Long chartId) {
        return EndpointUtils.getApiState(chartId);

    }

    /**
     * Retrieve a list of saved charts.
     * </br>
     * <dl>
     * The JSON object holds information regarding:
     * <dt><b>id</b><dd> - map id
     * <dt><b>title</b><dd> - a chart title
     * <dt><b>description</b><dd> - a chart description
     * <dt><b>created</b><dd> - a creation date
     * </dl></br></br>
     *
     * </br>
     * <h3>Sample Output:</h3><pre>
     * [
     *   {
     *     "id": 11,
     *     "title": "Dashboard",
     *     "description": "Saved dashboard",
     *     "created": "19/11/2014T19:53Z"
     *   },
     *   {
     *     "id": 6,
     *     "title": "Dashboard",
     *     "description": "Saved dashboard",
     *     "created": "19/11/2014T14:01Z"
     *   },
     *   ....
     * ]</pre>
     *
     * @return a list of JSON objects
     */
    @GET
    @Path("/saved-charts")
    @Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
    @ApiMethod(ui = false, id = "ChartList")
    public List<JsonBean> savedCharts() {
        String type="C";
        return EndpointUtils.getApiStateList(type);
    }

    /**
     * Retrieve a list of peace marked projects by category.
     * </br>
     * <dl>
     * The JSON object holds information regarding:
     * <dt><b>name</b><dd> - donor name
     * <dt><b>amount</b><dd> - amount
     * <dt><b>formattedAmount</b><dd> - formatted amount
     * <dt><b>id</b><dd> - donor id
     * </dl></br></br>
     *
     * <h3>Sample Input:</h3><pre>
     * {
     *  "filters": {},
     *  "settings": {
     *      "funding-type": ["Actual Commitments","Actual Disbursements"],
     *      "currency-code": "USD",
     *      "calendar-id": "123",
     *      "year-range": {
     *          "from": "2014",
     *          "to": "2015"
     *      }
     *  }
     * }</pre>
     * </br>
     * <h3>Sample Output:</h3><pre>
     * {
     *   "values": [
     *     {
     *       "name": "DFAT",
     *       "amount": 627838042.569743,
     *       "formattedAmount": "627,838,043"
     *       "id": 633,
     *     },
     *     {
     *       "name": "ADB",
     *       "amount": 300051591,
     *       "formattedAmount": "300,051,591"
     *       "id": 634,
     *     },
     *     ....
     *   ]
     * }</pre>
     *
     * @param config a JSON with the config
     * @param id the id to query
     *
     * @return a list of JSON objects with the donors
     */
    @POST
    @Path("/tops/ndd/{id}")
    @Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
    @ApiMethod(ui = false, id = "ndd_projects")
    public JsonBean getAdminLevelsTotals(JsonBean config, @PathParam("id") Integer id) {
        //TODO: Once we implement details for all top charts we can change the path to '/tops/details/' 
        // and send the type of chart and category id as params. 
        return DashboardsService.getPeaceMarkerProjectsByCategory(config, id);
    }
    
    /**
     * Build Heat Map.
     * </br>
     * <dl>
     * IMPORTANT NOTE ABOUT /{type} PARAMETER: This extra parameter is needed here because the UI differentiates each heatmap in the dashboard by its url,
     *  so we need for each heatmap (by Sector, Location or Program) an extra parameter that isnt actually used on the backend.
     * </dl></br></br>
     *
     * <h3>Sample Input:</h3><pre>
     * {
     *  “xCount” : 25, // default 25, set -1 to no limit. +1 ("Others") will be added if more than that available
     *  “yCount” : 10, // default 10, set -1 to no limit. +1 ("Others") will be added if more than that available
     *  “xColumn” : “Primary Sector”, // must be OrigName
     *  “yColumn” : “Donor Group”, // must be origName
     *  “filters”: { ... }, // usual filters input
     *  “settings” : { ... } // usual settings input, and Dashboard specific with Measure selection
     * }</pre>
     * </br>
     * <h3>Sample Output:</h3><pre>
     * {
     *  “summary” : [“Primary Sector”, “Donor Group”, “Actual Commitments”],
     *  “xDataSet” : [“Education”, “Health”, ...], // may end with "Others" (translated) for anything cut off
     *  “yDataSet” : [“World Bank Group”, “ADB”, ...], // may end with "Others" (translated) for anything cut off
     *  “xPTotals” : [100, ...], // percentage, 100 for each X per current rules
     *  “xTotals” : [“5 000”, …], // formatted abmounts
     *  “yPTotals” : [17, ...],
     *  “yTotals”: [“800”, …],
     *  “matrix” : [[{“p”: 100, “dv” : “12 000”}, ...], null, [...], ...], // p = % amount, dv = display value
     *  "xTotalCount" : 30,// the actual total count of entries for X. Can be used to detect if "Other" is present on X
     *  "yTotalCount" : 20 // the actual total count of entries for Y. Can be used to detect if "Other" is present on Y
     * }</pre>
     *
     * @param config a JSON with the config
     *
     * @return a JSON objects
     */
    @POST
    @Path("/heat-map/{type}")
    @Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
    @ApiMethod(ui = false, id = "heatMap")
    public JsonBean getHeatMap(JsonBean config) {
        return new HeatMapService(config).buildHeatMap();
    }


    /**
     * Retrieve a list of projects query by xId and yId of Heat Map.
     * </br>
     * <dl>
     * The JSON object holds information regarding:
     * <dt><b>totalRecords</b><dd> - number total of projects.
     * <dt><b>values</b><dd> - array with a list of projects.
     *     name - name of the project.
     *     amount - amount of the project.
     *     formattedAmount - formatted amount of the project.
     *     id - id of the project.
     * </dl></br></br>
     *
     * <h3>Sample Input:</h3><pre>
     * {
     *  “xCount” : 25, // default 25, set -1 to no limit. +1 ("Others") will be added if more than that available
     *  “yCount” : 10, // default 10, set -1 to no limit. +1 ("Others") will be added if more than that available
     *  “xColumn” : “Primary Sector”, // must be OrigName
     *  “yColumn” : “Donor Group”, // must be origName
     *  “filters”: { ... }, // usual filters input
     *  “settings” : { ... } // usual settings input, and Dashboard specific with Measure selection
     * }</pre>
     * </br>
     * <h3>Sample Output:</h3><pre>
     * {
     *     "totalRecords": 10,
     *     "values": [{
     *         "name": "Alimentation en eau de la ville d'Abidjan à partir de la nappe du Sud Comoé (Bonoua) - Phase I",
     *         "amount": 104422920.000000000000,
     *         "formattedAmount": "104 422 920",
     *         "id": 19003
     *     },
     *  .....
     *  {
     *         "name": "Construction de l'autoroute Abidjan-Bassam",
     *         "amount": 114777280.000000000000,
     *         "formattedAmount": "114 777 280",
     *         "id": 19111
     *     }]
     * }</pre>
     *
     * @param config a JSON with the config
     * @param xId id of the x dimention of Heat Map matrix.
     * @param yId id of the y dimention of Heat Map matrix.
     *
     * @return a JSON objects with a list of projects
     */
    @POST
    @Path("/heat-map/{type}/{xId}/{yId}")
    @Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
    @ApiMethod(ui = false, id = "heatMapDataDetail")
    public JsonBean getHeatMapDataDetail(JsonBean config, @PathParam("xId") Long xId, @PathParam("yId") Long yId) {
        return new HeatMapService(config, xId, yId).buildHeatMapDetail();
    }
    
    /**
     * Provides possible HeatMap Configurations.
     * </br>
     * <dl>
     * This EP doesn't receive any parameters and return a list of the visibly columns, a list of charts and the colors to use for every threshold.
     * </dl></br></br>
     *
     * <h3>Sample Output:</h3><pre>
     * {
     *     “columns” : [{“name” : “Donor Group”, “origName”: “Donor Group”},
     *                  {“name” : “Primary Sector”, “origName”: “...”},
     *                  {“name” : “Primary Sector Sub-Sector”, ...},
     *                  …
     *                  {“name” : “Secondary Program Level 8”, ...}
     *                  ],
     *     “charts” : [{
     *                 “type” : “S”, // other options: “P”, “L”
     *                 “name” : “Fragmentation by Donor and Sector”, //name will be always in English, not traslated
     *                 “yColumns” : [0], xColumns : [1, 2, 3] // indexes ref of all used columns
     *                 }, ....],
     *     “amountColors” :  [ {0 : “#d05151”}, {1 : #e68787}, ...] // i.e. for values >= 1, use #e68787 color
     * }</pre>
     *
     * @return a JSON objects with the existing HeatMap configurations
     */
    @GET
    @Path("/heat-map/configs")
    @Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
    @ApiMethod(ui = false, id = "heatMapConfigs")
    public JsonBean getHeatMapConfigs() {
        return new HeatMapConfigs().getHeatMapConfigs();
    }

    /**
     * Provides HeatMap Admin Settings.
     * </br>
     * <dl>
     * The user must be logged-in as admin to call this method.
     * </br>
     * The JSON object holds information regarding:
     * <dt><b>amountColors</b><dd> - a list of colors
     *     id - color id
     *     amountFrom - a floating point number
     *     color
     *     name - translated name
     * </dl></br></br>
     *
     * <h3>Sample Output:</h3><pre>
     * {
     *     “amountColors” :[ {       // i.e. for values >= 0, use #d05151 color
     *     “id” : 1,
     *     “amountFrom” : 0,
     *     “color” : “#d05151”,
     *     “name” : “Dark Red”
     *     }, …
     *     ]
     * }
     * @implicitParam X-Auth-Token|string|header
     * @return JSON structure of HeatMap Administrative Settings
     */
    @GET
    @Path("/heat-map/settings")
    @Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
    @ApiMethod(ui = false, id = "readHeatMapSettings", authTypes = {AuthRule.IN_ADMIN})
    public JsonBean getHeatMapSettings() {
        return new HeatMapConfigs().getHeatMapAdminSettings();
    }

    /**
     * Updates HeatMapSettings with new configuration.
     * </br>
     * <dl>
     * Note: for now we have a fixed set, but in future we may want to allow different number of colors and nuances
     * </dl></br></br>
     *
     * <h3>Sample Input:</h3><pre>
     * {
     *     “amountColors” : [{ “id”: 1, “color” : “#d05151”, “amountFrom” : 0}, ...]
     * }</pre>
     * </br>
     * <h3>Sample Output:</h3><pre>
     * {
     *     “error” : {
     *         “1234” : [“Invalid color threshold”]
     *         ...
     *     }
     * }
     * @implicitParam X-Auth-Token|string|header
     * @param config
     * @return
     * @throws Exception 
     */
    @POST
    @Path("/heat-map/settings")
    @Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
    @ApiMethod(ui = false, id = "readHeatMapSettings", authTypes = {AuthRule.IN_ADMIN})
    public JsonBean setHeatMapSettings(JsonBean config) throws Exception {
        return new HeatMapConfigs().saveHeatMapAdminSettings(config);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Class getErrorsClass() {
        return DashboardErrors.class;
    }
}
