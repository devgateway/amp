package org.digijava.kernel.ampapi.endpoints.dashboards;

import static javax.servlet.http.HttpServletResponse.SC_OK;

import javax.ws.rs.DefaultValue;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import java.util.List;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.digijava.kernel.ampapi.endpoints.common.EndpointUtils;
import org.digijava.kernel.ampapi.endpoints.dashboards.services.TopsChartService;
import org.digijava.kernel.ampapi.endpoints.dashboards.services.DashboardsService;
import org.digijava.kernel.ampapi.endpoints.dashboards.services.HeatMapConfigs;
import org.digijava.kernel.ampapi.endpoints.dashboards.services.HeatMapService;
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
@Api("dashboard")
public class EndPoints implements ErrorReportingEndpoint {

    @GET
    @Path("/tops")
    @Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
    @ApiMethod(ui = false, id = "topsList")
    @ApiOperation(
            value = "Retrieve a list of available top for the dashboard charts with their names.",
            notes = "This EP was hardcoded and will return always \"Donor Agency\", \"Region\", \"Primary Sector\".\n"
                    + "\n"
                    + "The JSON object holds information regarding:\n"
                    + "\n"
                    + "Field|Description\n"
                    + "---|---\n"
                    + "id|the id of top\n"
                    + "name|the name of top\n"
                    + "\n"
                    + "### Sample Output\n"
                    + "```\n"
                    + " [\n"
                    + "   {\n"
                    + "     \"id\": \"do\",\n"
                    + "     \"name\": \"Donor Agency\"\n"
                    + "   },\n"
                    + "   {\n"
                    + "     \"id\": \"re\",\n"
                    + "     \"name\": \"Region\"\n"
                    + "   },\n"
                    + "   {\n"
                    + "     \"id\": \"ps\",\n"
                    + "     \"name\": \"Primary Sector\"\n"
                    + "   }\n"
                    + " ]\n"
                    + "```\n")
    @ApiResponses(@ApiResponse(code = SC_OK, message = "a list of JSON objects with the tops"))
    public List<JsonBean> getAdminLevelsTotalslist() {
        return DashboardsService.getTopsList();
    }

    @POST
    @Path("/tops/{type}")
    @Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
    @ApiMethod(ui = false, id = "tops")
    @ApiOperation(
            value = "Retrieve top donors values for dashboards chart.",
            notes = "Chart type:\n"
                    + "- do = Donor\n"
                    + "- re = Region\n"
                    + "- ps = Primary Sector\n"
                    + "- dg = Donor Group\n"
                    + "\n"
                    + "The JSON object holds information regarding:\n"
                    + "\n"
                    + "Field|Description\n"
                    + "---|---\n"
                    + "currency|currency of the report that is going to be run to retrieve the information\n"
                    + "values|an array with a list of donors and the amount\n"
                    + "total| total amount\n"
                    + "sumarizedTotal|sumarized total amount\n"
                    + "maxLimit|number of donors\n"
                    + "name|name of the report\n"
                    + " \n"
                    + "### Sample Input\n"
                    + "```\n"
                    + "{\n"
                    + "  \"filters\": {},\n"
                    + "  \"settings\": {\n"
                    + "      \"funding-type\": [\"Actual Commitments\",\"Actual Disbursements\"],\n"
                    + "      \"currency-code\": \"USD\",\n"
                    + "      \"calendar-id\": \"123\",\n"
                    + "      \"year-range\": {\n"
                    + "          \"from\": \"2014\",\n"
                    + "          \"to\": \"2015\"\n"
                    + "      }\n"
                    + "  }\n"
                    + "}\n"
                    + "```\n"
                    + " \n"
                    + "\n"
                    + " ### Sample Output\n"
                    + "```\n"
                    + " {\n"
                    + "   \"currency\": \"USD\",\n"
                    + "   \"values\": [\n"
                    + "     {\n"
                    + "       \"name\": \"DFAT\",\n"
                    + "       \"id\": 633,\n"
                    + "       \"amount\": 627838042.569743,\n"
                    + "       \"formattedAmount\": \"627,838,043\"\n"
                    + "     },\n"
                    + "     {\n"
                    + "       \"name\": \"ADB\",\n"
                    + "       \"id\": 634,\n"
                    + "       \"amount\": 300051591,\n"
                    + "       \"formattedAmount\": \"300,051,591\"\n"
                    + "     },\n"
                    + "     ....\n"
                    + "   ],\n"
                    + "   \"total\": 2398018313.370719,\n"
                    + "   \"sumarizedTotal\": \"2,4B\",\n"
                    + "   \"maxLimit\": 85,\n"
                    + "   \"totalPositive\": 2398018313.3707194,\n"
                    + "   \"name\": \"Top Donor Agencies\",\n"
                    + "   \"title\": \"Top Donor Agencies\"\n"
                    + " }\n"
                    + "```")
    @ApiResponses(@ApiResponse(code = SC_OK, message = "a JSON objects with a tops donors"))
    //TODO: Implement Filters
    public JsonBean getAdminLevelsTotals(JsonBean config,
            @ApiParam(value = "Chart type", allowableValues = "do,re,ps,dg") @PathParam("type") String type,
            @DefaultValue("5") @QueryParam("limit") Integer limit) {
        //return DashboardsService.getTops(type, null, limit, config);
        return new TopsChartService(config, type, limit).buildChartData();
    }

    @POST
    @Path("/tops/{type}/{id}")
    @Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
    @ApiMethod(ui = false, id = "topsDataDetail")
    @ApiOperation(
            value = "Retrieve a list of projects by type query for selected id.",
            notes = "<dl>\n"
                    + "where Type (Chart type) :\n"
                    + "   do = Donor\n"
                    + "   re = Region\n"
                    + "   ps = Primary Sector\n"
                    + "   dg = Donor Group\n"
                    + "</br>\n"
                    + "The JSON object holds information regarding:\n"
                    + "<dt><b>totalRecords</b><dd> - number total of projects.\n"
                    + "<dt><b>values</b><dd> - array with a list of projects.\n"
                    + "    name - name of the project.\n"
                    + "    amount - amount of the project.\n"
                    + "    formattedAmount - formatted amount of the project.\n"
                    + "    id - id of the project.\n"
                    + "</dl></br></br>\n"
                    + "<h3>Sample Input:</h3><pre>\n"
                    + "{\n"
                    + " \"filters\": {},\n"
                    + " \"settings\": {\n"
                    + "     \"funding-type\": [\"Actual Commitments\",\"Actual Disbursements\"],\n"
                    + "     \"currency-code\": \"USD\",\n"
                    + "     \"calendar-id\": \"123\",\n"
                    + "     \"year-range\": {\n"
                    + "         \"from\": \"2014\",\n"
                    + "         \"to\": \"2015\"\n"
                    + "     }\n"
                    + " }\n"
                    + "}</pre>\n"
                    + "</br>\n"
                    + "<h3>Sample Output:</h3><pre>\n"
                    + "{\n"
                    + "    \"totalRecords\": 10,\n"
                    + "    \"values\": [{\n"
                    + "        \"name\": \"Alimentation en eau de la ville d'Abidjan à partir de la nappe du "
                    + "Sud Comoé (Bonoua) - Phase I\",\n"
                    + "        \"amount\": 104422920.000000000000,\n"
                    + "        \"formattedAmount\": \"104 422 920\",\n"
                    + "        \"id\": 19003\n"
                    + "    },\n"
                    + " .....\n"
                    + " {\n"
                    + "        \"name\": \"Construction de l'autoroute Abidjan-Bassam\",\n"
                    + "        \"amount\": 114777280.000000000000,\n"
                    + "        \"formattedAmount\": \"114 777 280\",\n"
                    + "        \"id\": 19111\n"
                    + "    }]\n"
                    + "}</pre>")
    public JsonBean getChartsDataDetail(JsonBean config,
            @ApiParam("chart type") @PathParam("type") String type,
            @ApiParam("id of the category to query the projects") @PathParam("id") Long id) {
        return new TopsChartService(config, type, id).buildChartData();
    }

    @POST
    @Path("/aid-predictability")
    @Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
    @ApiMethod(ui = false, id = "aidPredictability")
    @ApiOperation(
            value = "Retrieve aid predictability values for dashboards chart.",
            notes = "<dl>\n"
                    + "The JSON object holds information regarding:\n"
                    + "<dt><b>years</b><dd> - an array of years with the funding type and the amount\n"
                    + "<dt><b>totals</b><dd> - total by funding type\n"
                    + "<dt><b>currency</b><dd> - currency of the report that is going to be run to retrieve "
                    + "the information\n"
                    + "<dt><b>name</b><dd> - name of the report that is going to be run to retrieve the information\n"
                    + "<dt><b>title</b><dd> - title of the report that is going to be run to retrieve the information\n"
                    + "<dt><b>measure</b><dd> - measure type \"disbursements\"\n"
                    + "</dl></br></br>\n"
                    + "Returns a JSONObject objects with the years, amounts by funding type, currency and total "
                    + "amount.\n"
                    + "<h3>Sample Input:</h3><pre>\n"
                    + "{\n"
                    + " \"filters\": {},\n"
                    + " \"settings\": {\n"
                    + "     \"funding-type\": [\"Actual Commitments\",\"Actual Disbursements\"],\n"
                    + "     \"currency-code\": \"USD\",\n"
                    + "     \"calendar-id\": \"123\",\n"
                    + "     \"year-range\": {\n"
                    + "         \"from\": \"2014\",\n"
                    + "         \"to\": \"2015\"\n"
                    + "     }\n"
                    + " }\n"
                    + "}</pre>\n"
                    + "</br>\n"
                    + "<h3>Sample Output:</h3><pre>\n"
                    + "{\n"
                    + "  \"years\": [\n"
                    + "    {\n"
                    + "      \"planned disbursements\": {\n"
                    + "        \"amount\": 204848995.768391,\n"
                    + "        \"formattedAmount\": \"204,848,996\"\n"
                    + "      },\n"
                    + "      \"actual disbursements\": {\n"
                    + "        \"amount\": 299328665.562998,\n"
                    + "        \"formattedAmount\": \"299,328,666\"\n"
                    + "      },\n"
                    + "      \"year\": \"2014\"\n"
                    + "    },\n"
                    + "    {\n"
                    + "      \"planned disbursements\": {\n"
                    + "        \"amount\": 234745905.771138,\n"
                    + "        \"formattedAmount\": \"234,745,906\"\n"
                    + "      },\n"
                    + "      \"actual disbursements\": {\n"
                    + "        \"amount\": 244360092.647041,\n"
                    + "        \"formattedAmount\": \"244,360,093\"\n"
                    + "      },\n"
                    + "      \"year\": \"2015\"\n"
                    + "    },\n"
                    + "  ],\n"
                    + "  \"totals\": {\n"
                    + "    \"planned disbursements\": {\n"
                    + "      \"amount\": 2025184338.353028,\n"
                    + "      \"formattedAmount\": \"2,025,184,338\"\n"
                    + "    },\n"
                    + "    \"actual disbursements\": {\n"
                    + "      \"amount\": 2079819299.507724,\n"
                    + "      \"formattedAmount\": \"2,079,819,300\"\n"
                    + "    }\n"
                    + "  },\n"
                    + "  \"currency\": \"USD\",\n"
                    + "  \"name\": \"Aid Predictability\",\n"
                    + "  \"title\": \"Aid predictability\",\n"
                    + "  \"measure\": \"disbursements\"\n"
                    + "}</pre>")
    public JsonBean getAidPredictability(JsonBean filter) throws Exception {
        return DashboardsService.getAidPredictability(filter);
    }

    @POST
    @Path("/aid-predictability/{year}/{measure}")
    @Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
    @ApiMethod(ui = false, id = "aidPredictabilityDataDetail")
    @ApiOperation(
            value = "Retrieve a list of projects by aid predictability year.",
            notes = "<dl>\n"
                    + "The JSON object holds information regarding:\n"
                    + "<dt><b>totalRecords</b><dd> - number total of projects.\n"
                    + "<dt><b>values</b><dd> - array with a list of projects.\n"
                    + "    name - name of the project.\n"
                    + "    amount - amount of the project.\n"
                    + "    formattedAmount - formatted amount of the project.\n"
                    + "    id - id of the project.\n"
                    + "</dl></br></br>\n"
                    + "<h3>Sample Input:</h3><pre>\n"
                    + "{\n"
                    + " \"filters\": {},\n"
                    + " \"settings\": {\n"
                    + "     \"funding-type\": [\"Actual Commitments\",\"Actual Disbursements\"],\n"
                    + "     \"currency-code\": \"USD\",\n"
                    + "     \"calendar-id\": \"123\",\n"
                    + "     \"year-range\": {\n"
                    + "         \"from\": \"2014\",\n"
                    + "         \"to\": \"2015\"\n"
                    + "     }\n"
                    + " }\n"
                    + "}</pre>\n"
                    + "</br>\n"
                    + "<h3>Sample Output:</h3><pre>\n"
                    + "{\n"
                    + "    \"totalRecords\": 10,\n"
                    + "    \"values\": [{\n"
                    + "        \"name\": \"Alimentation en eau de la ville d'Abidjan à partir de la nappe du "
                    + "Sud Comoé (Bonoua) - Phase I\",\n"
                    + "        \"amount\": 104422920.000000000000,\n"
                    + "        \"formattedAmount\": \"104 422 920\",\n"
                    + "        \"id\": 19003\n"
                    + "    },\n"
                    + " .....\n"
                    + " {\n"
                    + "        \"name\": \"Construction de l'autoroute Abidjan-Bassam\",\n"
                    + "        \"amount\": 114777280.000000000000,\n"
                    + "        \"formattedAmount\": \"114 777 280\",\n"
                    + "        \"id\": 19111\n"
                    + "    }]\n"
                    + "}</pre>")
    public JsonBean getAidPredictabilityDataDetail(JsonBean filter, @PathParam("year") Integer year, @PathParam("measure")
            String measure) throws Exception {
        return DashboardsService.getAidPredictability(filter, year, measure);
    }

    @POST
    @Path("/ftype")
    @Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
    @ApiMethod(ui = false, id = "ftype")
    @ApiOperation(
            value = "Get funding types by year.",
            notes = "<dl>\n"
                    + "The JSON object holds information regarding:\n"
                    + "<dt><b>total</b><dd> - total amount\n"
                    + "<dt><b>sumarizedTotal</b><dd> - sumarized total amount\n"
                    + "<dt><b>currency</b><dd> - currency of the report\n"
                    + "<dt><b>values</b><dd> - an array with the year and a list of types of assistence and the "
                    + "amount\n"
                    + "<dt><b>name</b><dd> - name of the report\n"
                    + "<dt><b>title</b><dd> - title of the report\n"
                    + "</dl></br></br>\n"
                    + "</br>\n"
                    + "<h3>Sample Input:</h3><pre>\n"
                    + "{\n"
                    + " \"filters\": {},\n"
                    + " \"settings\": {\n"
                    + "     \"funding-type\": [\"Actual Commitments\",\"Actual Disbursements\"],\n"
                    + "     \"currency-code\": \"USD\",\n"
                    + "     \"calendar-id\": \"123\",\n"
                    + "     \"year-range\": {\n"
                    + "         \"from\": \"2014\",\n"
                    + "         \"to\": \"2015\"\n"
                    + "     }\n"
                    + " }\n"
                    + "}</pre>\n"
                    + "</br>\n"
                    + "<h3>Sample Output:</h3><pre>\n"
                    + "{\n"
                    + "  \"total\": 154123105.30153,\n"
                    + "  \"sumarizedTotal\": \"154,1M\",\n"
                    + "  \"currency\": \"USD\",\n"
                    + "  \"values\": [\n"
                    + "    {\n"
                    + "      \"Year\": \"2007\",\n"
                    + "      \"values\": [\n"
                    + "        {\n"
                    + "          \"type\": \"Loan\",\n"
                    + "          \"amount\": 0,\n"
                    + "          \"formattedAmount\": \"0\"\n"
                    + "        },\n"
                    + "        {\n"
                    + "          \"type\": \"Loan\",\n"
                    + "          \"amount\": 0,\n"
                    + "          \"formattedAmount\": \"0\"\n"
                    + "        }\n"
                    + "      ]\n"
                    + "    },\n"
                    + "    {\n"
                    + "      \"Year\": \"2008\",\n"
                    + "      \"values\": [\n"
                    + "        {\n"
                    + "          \"type\": \"Loan\",\n"
                    + "          \"amount\": 0,\n"
                    + "          \"formattedAmount\": \"0\"\n"
                    + "        },\n"
                    + "        {\n"
                    + "          \"type\": \"Loan\",\n"
                    + "          \"amount\": 0,\n"
                    + "          \"formattedAmount\": \"0\"\n"
                    + "        },\n"
                    + "        {\n"
                    + "          \"type\": \"Grant\",\n"
                    + "          \"amount\": 6500000,\n"
                    + "          \"formattedAmount\": \"6,500,000\"\n"
                    + "        },\n"
                    + "        ....\n"
                    + "      ]\n"
                    + "    },\n"
                    + "    ....\n"
                    + "  ],\n"
                    + "  \"name\": \"Funding Type\",\n"
                    + "  \"title\": \"Funding type\"\n"
                    + "}</pre>")
    //TODO: Implement Filters
    public JsonBean getfundingtype(
            @ApiParam("a JSON object with the configuration that is going to be "
                    + "used by the report to get the funding-type") JsonBean config,
            @DefaultValue("ac") @QueryParam("adjtype") String adjtype) {
        return DashboardsService.fundingtype(adjtype,config);
    }

    @POST
    @Path("/ftype/{year}/{id}")
    @Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
    @ApiMethod(ui = false, id = "ftypeDataDetail")
    @ApiOperation(
            value = "Retrieve a list of projects by funding types year.",
            notes = "<dl>\n"
                    + "The JSON object holds information regarding:\n"
                    + "<dt><b>totalRecords</b><dd> - number total of projects.\n"
                    + "<dt><b>values</b><dd> - array with a list of projects.\n"
                    + "    name - name of the project.\n"
                    + "    amount - amount of the project.\n"
                    + "    formattedAmount - formatted amount of the project.\n"
                    + "    id - id of the project.\n"
                    + "</dl></br></br>\n"
                    + "</br>\n"
                    + "<h3>Sample Input:</h3><pre>\n"
                    + "{\n"
                    + " \"filters\": {},\n"
                    + " \"settings\": {\n"
                    + "     \"funding-type\": [\"Actual Commitments\",\"Actual Disbursements\"],\n"
                    + "     \"currency-code\": \"USD\",\n"
                    + "     \"calendar-id\": \"123\",\n"
                    + "     \"year-range\": {\n"
                    + "         \"from\": \"2014\",\n"
                    + "         \"to\": \"2015\"\n"
                    + "     }\n"
                    + " }\n"
                    + "}</pre>\n"
                    + "</br>\n"
                    + "<h3>Sample Output:</h3><pre>\n"
                    + "{\n"
                    + "    \"totalRecords\": 10,\n"
                    + "    \"values\": [{\n"
                    + "        \"name\": \"Alimentation en eau de la ville d'Abidjan à partir de la nappe du "
                    + "Sud Comoé (Bonoua) - Phase I\",\n"
                    + "        \"amount\": 104422920.000000000000,\n"
                    + "        \"formattedAmount\": \"104 422 920\",\n"
                    + "        \"id\": 19003\n"
                    + "    },\n"
                    + " .....\n"
                    + " {\n"
                    + "        \"name\": \"Construction de l'autoroute Abidjan-Bassam\",\n"
                    + "        \"amount\": 114777280.000000000000,\n"
                    + "        \"formattedAmount\": \"114 777 280\",\n"
                    + "        \"id\": 19111\n"
                    + "    }]\n"
                    + "}</pre>"
    )
    //TODO: Implement Filters
    public JsonBean getfundingtypeDataDetail(
            @ApiParam("a JSON object with the configuration that is going to be used by "
                    + "the report to get the funding-type") JsonBean config,
            @DefaultValue("ac") @QueryParam("adjtype") String adjtype,
            @PathParam("year") Integer year,
            @ApiParam("id of the funding type") @PathParam("id") Integer id) {
        return DashboardsService.fundingtype(adjtype,config, year, id);
    }

    @POST
    @Path("/saved-charts")
    @Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
    @ApiMethod(ui = false, id = "SaveChart")
    @ApiOperation(
            value = "Save the state of a chart to be able to share it.",
            notes = "</br>\n"
                    + "<dl>\n"
                    + "The JSON object holds information regarding:\n"
                    + "<dt><b>mapId</b><dd> - map id\n"
                    + "</dl></br></br>\n"
                    + "\n"
                    + "<h3>Sample Input:</h3><pre>\n"
                    + "{\n"
                    + "    \"title\" : \"Dashboard\",\n"
                    + "    \"description\" : \"Saved dashboard\",\n"
                    + "    \"stateBlob\" : \"{\"chart:/rest/dashboard/tops/do\":{\"limit\":5,\"adjtype\":"
                    + "\"Actual Commitments\",\"view\":\"bar\",\"big\":false},\"chart:/rest/dashboard/tops/dg\""
                    + ":{\"limit\":5,\"adjtype\":\"Actual Commitments\",\"view\":\"bar\",\"big\":false},"
                    + "\"chart:/rest/dashboard/tops/re\":{\"limit\":5,\"adjtype\":\"A (...)\"\n"
                    + "}</pre>\n"
                    + "</br>\n"
                    + "<h3>Sample Output:</h3><pre>\n"
                    + "{\n"
                    + "    \"mapId\": 155\n"
                    + "}</pre>\n")
    public JsonBean savedMaps(final JsonBean pChart) {
        return EndpointUtils.saveApiState(pChart,"C");
    }

    @GET
    @Path("/saved-charts/{chartId}")
    @Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
    @ApiMethod(ui = false, id = "ChartById")
    @ApiOperation(
            value = "Retrieve a saved chart by Id.",
            notes = "<dl>\n"
                    + "The JSON object holds information regarding:\n"
                    + "<dt><b>id</b><dd> - map id\n"
                    + "<dt><b>title</b><dd> - a chart title\n"
                    + "<dt><b>description</b><dd> - a chart description\n"
                    + "<dt><b>stateBlob</b><dd> - a chart blob\n"
                    + "<dt><b>created</b><dd> - a creation date\n"
                    + "<dt><b>lastAccess</b><dd> - a last access date\n"
                    + "</dl></br></br>\n"
                    + "\n"
                    + "</br>\n"
                    + "<h3>Sample Output:</h3><pre>\n"
                    + "{\n"
                    + "    \"id\": 155,\n"
                    + "    \"title\": \"title\",\n"
                    + "    \"description\": \"description\",\n"
                    + "    \"stateBlob\": \"{\"chart:/rest/dashboard/tops/do\":{\"limit\":5,\"adjtype\":"
                    + "\"Actual Commitments\",\"view\":\"bar\",\"big\":false},\"chart:/rest/dashboard/tops/dg\":"
                    + "{\"limit\":5,\"adjtype\":\"Actual Commitments\",\"view\":\"bar\",\"big\":false},"
                    + "\"chart:/rest/dashboard/tops/re\":{\"limit\":5,\"adjtype\":\"A (...)\",\n"
                    + "    \"created\": \"15/12/2016T11:02Z\",\n"
                    + "    \"lastAccess\": \"15/12/2016T11:12Z\"\n"
                    + "}</pre>\n")
    public JsonBean savedCharts(@PathParam("chartId") Long chartId) {
        return EndpointUtils.getApiState(chartId);

    }

    @GET
    @Path("/saved-charts")
    @Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
    @ApiMethod(ui = false, id = "ChartList")
    @ApiOperation(
            value = "Retrieve a list of saved charts.",
            notes = "<dl>\n"
                    + "The JSON object holds information regarding:\n"
                    + "<dt><b>id</b><dd> - map id\n"
                    + "<dt><b>title</b><dd> - a chart title\n"
                    + "<dt><b>description</b><dd> - a chart description\n"
                    + "<dt><b>created</b><dd> - a creation date\n"
                    + "</dl></br></br>\n"
                    + "</br>\n"
                    + "<h3>Sample Output:</h3><pre>\n"
                    + "[\n"
                    + "  {\n"
                    + "    \"id\": 11,\n"
                    + "    \"title\": \"Dashboard\",\n"
                    + "    \"description\": \"Saved dashboard\",\n"
                    + "    \"created\": \"19/11/2014T19:53Z\"\n"
                    + "  },\n"
                    + "  {\n"
                    + "    \"id\": 6,\n"
                    + "    \"title\": \"Dashboard\",\n"
                    + "    \"description\": \"Saved dashboard\",\n"
                    + "    \"created\": \"19/11/2014T14:01Z\"\n"
                    + "  },\n"
                    + "  ....\n"
                    + "]</pre>")
    public List<JsonBean> savedCharts() {
        String type="C";
        return EndpointUtils.getApiStateList(type);
    }

    @POST
    @Path("/tops/ndd/{id}")
    @Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
    @ApiMethod(ui = false, id = "ndd_projects")
    @ApiOperation(
            value = "Retrieve a list of peace marked projects by category.",
            notes = "</br>\n"
                    + "<dl>\n"
                    + "The JSON object holds information regarding:\n"
                    + "<dt><b>name</b><dd> - donor name\n"
                    + "<dt><b>amount</b><dd> - amount\n"
                    + "<dt><b>formattedAmount</b><dd> - formatted amount\n"
                    + "<dt><b>id</b><dd> - donor id\n"
                    + "</dl></br></br>\n"
                    + "<h3>Sample Input:</h3><pre>\n"
                    + "{\n"
                    + " \"filters\": {},\n"
                    + " \"settings\": {\n"
                    + "     \"funding-type\": [\"Actual Commitments\",\"Actual Disbursements\"],\n"
                    + "     \"currency-code\": \"USD\",\n"
                    + "     \"calendar-id\": \"123\",\n"
                    + "     \"year-range\": {\n"
                    + "         \"from\": \"2014\",\n"
                    + "         \"to\": \"2015\"\n"
                    + "     }\n"
                    + " }\n"
                    + "}</pre>\n"
                    + "</br>\n"
                    + "<h3>Sample Output:</h3><pre>\n"
                    + "{\n"
                    + "  \"values\": [\n"
                    + "    {\n"
                    + "      \"name\": \"DFAT\",\n"
                    + "      \"amount\": 627838042.569743,\n"
                    + "      \"formattedAmount\": \"627,838,043\"\n"
                    + "      \"id\": 633,\n"
                    + "    },\n"
                    + "    {\n"
                    + "      \"name\": \"ADB\",\n"
                    + "      \"amount\": 300051591,\n"
                    + "      \"formattedAmount\": \"300,051,591\"\n"
                    + "      \"id\": 634,\n"
                    + "    },\n"
                    + "    ....\n"
                    + "  ]\n"
                    + "}</pre>")
    public JsonBean getAdminLevelsTotals(JsonBean config, @PathParam("id") Integer id) {
        //TODO: Once we implement details for all top charts we can change the path to '/tops/details/' 
        // and send the type of chart and category id as params. 
        return DashboardsService.getPeaceMarkerProjectsByCategory(config, id);
    }
    
    @POST
    @Path("/heat-map/{type}")
    @Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
    @ApiMethod(ui = false, id = "heatMap")
    @ApiOperation(
            value = "Build Heat Map.",
            notes = "<dl>\n"
                    + "IMPORTANT NOTE ABOUT /{type} PARAMETER: This extra parameter is needed here because the UI "
                    + "differentiates each heatmap in the dashboard by its url,\n"
                    + " so we need for each heatmap (by Sector, Location or Program) an extra parameter that isnt "
                    + "actually used on the backend.\n"
                    + "</dl></br></br>\n"
                    + "<h3>Sample Input:</h3><pre>\n"
                    + "{\n"
                    + " “xCount” : 25, // default 25, set -1 to no limit. +1 (\"Others\") will be added "
                    + "if more than that available\n"
                    + " “yCount” : 10, // default 10, set -1 to no limit. +1 (\"Others\") will be added "
                    + "if more than that available\n"
                    + " “xColumn” : “Primary Sector”, // must be OrigName\n"
                    + " “yColumn” : “Donor Group”, // must be origName\n"
                    + " “filters”: { ... }, // usual filters input\n"
                    + " “settings” : { ... } // usual settings input, and Dashboard specific with Measure selection\n"
                    + "}</pre>\n"
                    + "</br>\n"
                    + "<h3>Sample Output:</h3><pre>\n"
                    + "{\n"
                    + " “summary” : [“Primary Sector”, “Donor Group”, “Actual Commitments”],\n"
                    + " “xDataSet” : [“Education”, “Health”, ...], // may end with \"Others\" (translated) "
                    + "for anything cut off\n"
                    + " “yDataSet” : [“World Bank Group”, “ADB”, ...], // may end with \"Others\" (translated) "
                    + "for anything cut off\n"
                    + " “xPTotals” : [100, ...], // percentage, 100 for each X per current rules\n"
                    + " “xTotals” : [“5 000”, …], // formatted abmounts\n"
                    + " “yPTotals” : [17, ...],\n"
                    + " “yTotals”: [“800”, …],\n"
                    + " “matrix” : [[{“p”: 100, “dv” : “12 000”}, ...], null, [...], ...], // p = % amount, "
                    + "dv = display value\n"
                    + " \"xTotalCount\" : 30,// the actual total count of entries for X. Can be used to detect "
                    + "if \"Other\" is present on X\n"
                    + " \"yTotalCount\" : 20 // the actual total count of entries for Y. Can be used to detect "
                    + "if \"Other\" is present on Y\n"
                    + "}</pre>"
    )
    public JsonBean getHeatMap(JsonBean config) {
        return new HeatMapService(config).buildHeatMap();
    }


    @POST
    @Path("/heat-map/{type}/{xId}/{yId}")
    @Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
    @ApiMethod(ui = false, id = "heatMapDataDetail")
    @ApiOperation(
            value = "Retrieve a list of projects query by xId and yId of Heat Map.",
            notes = "<dl>\n"
                    + "The JSON object holds information regarding:\n"
                    + "<dt><b>totalRecords</b><dd> - number total of projects.\n"
                    + "<dt><b>values</b><dd> - array with a list of projects.\n"
                    + "    name - name of the project.\n"
                    + "    amount - amount of the project.\n"
                    + "    formattedAmount - formatted amount of the project.\n"
                    + "    id - id of the project.\n"
                    + "</dl></br></br>\n"
                    + "<h3>Sample Input:</h3><pre>\n"
                    + "{\n"
                    + " “xCount” : 25, // default 25, set -1 to no limit. +1 (\"Others\") will be added "
                    + "if more than that available\n"
                    + " “yCount” : 10, // default 10, set -1 to no limit. +1 (\"Others\") will be added "
                    + "if more than that available\n"
                    + " “xColumn” : “Primary Sector”, // must be OrigName\n"
                    + " “yColumn” : “Donor Group”, // must be origName\n"
                    + " “filters”: { ... }, // usual filters input\n"
                    + " “settings” : { ... } // usual settings input, and Dashboard specific with Measure selection\n"
                    + "}</pre>\n"
                    + "</br>\n"
                    + "<h3>Sample Output:</h3><pre>\n"
                    + "{\n"
                    + "    \"totalRecords\": 10,\n"
                    + "    \"values\": [{\n"
                    + "        \"name\": \"Alimentation en eau de la ville d'Abidjan à partir de la nappe du "
                    + "Sud Comoé (Bonoua) - Phase I\",\n"
                    + "        \"amount\": 104422920.000000000000,\n"
                    + "        \"formattedAmount\": \"104 422 920\",\n"
                    + "        \"id\": 19003\n"
                    + "    },\n"
                    + " .....\n"
                    + " {\n"
                    + "        \"name\": \"Construction de l'autoroute Abidjan-Bassam\",\n"
                    + "        \"amount\": 114777280.000000000000,\n"
                    + "        \"formattedAmount\": \"114 777 280\",\n"
                    + "        \"id\": 19111\n"
                    + "    }]\n"
                    + "}</pre>")
    public JsonBean getHeatMapDataDetail(JsonBean config,
            @ApiParam("id of the x dimention of Heat Map matrix.") @PathParam("xId") Long xId,
            @ApiParam("id of the y dimention of Heat Map matrix.") @PathParam("yId") Long yId) {
        return new HeatMapService(config, xId, yId).buildHeatMapDetail();
    }
    
    @GET
    @Path("/heat-map/configs")
    @Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
    @ApiMethod(ui = false, id = "heatMapConfigs")
    @ApiOperation(
            value = "Provides possible HeatMap Configurations.",
            notes = "<dl>\n"
                    + "This EP doesn't receive any parameters and return a list of the visibly columns, a "
                    + "list of charts and the colors to use for every threshold.\n"
                    + "</dl></br></br>\n"
                    + "<h3>Sample Output:</h3><pre>\n"
                    + "{\n"
                    + "    “columns” : [{“name” : “Donor Group”, “origName”: “Donor Group”},\n"
                    + "                 {“name” : “Primary Sector”, “origName”: “...”},\n"
                    + "                 {“name” : “Primary Sector Sub-Sector”, ...},\n"
                    + "                 …\n"
                    + "                 {“name” : “Secondary Program Level 8”, ...}\n"
                    + "                 ],\n"
                    + "    “charts” : [{\n"
                    + "                “type” : “S”, // other options: “P”, “L”\n"
                    + "                “name” : “Fragmentation by Donor and Sector”, //name will be always in "
                    + "English, not traslated\n"
                    + "                “yColumns” : [0], xColumns : [1, 2, 3] // indexes ref of all used columns\n"
                    + "                }, ....],\n"
                    + "    “amountColors” :  [ {0 : “#d05151”}, {1 : #e68787}, ...] // i.e. for values >= 1, "
                    + "use #e68787 color\n"
                    + "}</pre>")
    public JsonBean getHeatMapConfigs() {
        return new HeatMapConfigs().getHeatMapConfigs();
    }

    @GET
    @Path("/heat-map/settings")
    @Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
    @ApiMethod(ui = false, id = "readHeatMapSettings", authTypes = {AuthRule.IN_ADMIN})
    @ApiOperation(
            value = "Provides HeatMap Admin Settings.",
            notes = "<dl>\n"
                    + "The user must be logged-in as admin to call this method.\n"
                    + "</br>\n"
                    + "The JSON object holds information regarding:\n"
                    + "<dt><b>amountColors</b><dd> - a list of colors\n"
                    + "    id - color id\n"
                    + "    amountFrom - a floating point number\n"
                    + "    color\n"
                    + "    name - translated name\n"
                    + "</dl></br></br>\n"
                    + "<h3>Sample Output:</h3><pre>\n"
                    + "{\n"
                    + "    “amountColors” :[ {       // i.e. for values >= 0, use #d05151 color\n"
                    + "    “id” : 1,\n"
                    + "    “amountFrom” : 0,\n"
                    + "    “color” : “#d05151”,\n"
                    + "    “name” : “Dark Red”\n"
                    + "    }, …\n"
                    + "    ]\n"
                    + "}\n"
                    + "</pre>")
    public JsonBean getHeatMapSettings() {
        return new HeatMapConfigs().getHeatMapAdminSettings();
    }

    @POST
    @Path("/heat-map/settings")
    @Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
    @ApiMethod(ui = false, id = "readHeatMapSettings", authTypes = {AuthRule.IN_ADMIN})
    @ApiOperation(
            value = "Updates HeatMapSettings with new configuration.",
            notes = "<dl>\n"
                    + "Note: for now we have a fixed set, but in future we may want to allow different "
                    + "number of colors and nuances\n"
                    + "</dl></br></br>\n"
                    + "<h3>Sample Input:</h3><pre>\n"
                    + "{\n"
                    + "    “amountColors” : [{ “id”: 1, “color” : “#d05151”, “amountFrom” : 0}, ...]\n"
                    + "}</pre>\n"
                    + "</br>\n"
                    + "<h3>Sample Output:</h3><pre>\n"
                    + "{\n"
                    + "    “error” : {\n"
                    + "        “1234” : [“Invalid color threshold”]\n"
                    + "        ...\n"
                    + "    }\n"
                    + "}\n"
                    + "</pre>")
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
