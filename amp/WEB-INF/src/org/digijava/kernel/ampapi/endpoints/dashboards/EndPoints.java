package org.digijava.kernel.ampapi.endpoints.dashboards;

import com.fasterxml.jackson.annotation.JsonView;
import com.google.common.base.MoreObjects;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.digijava.kernel.ampapi.endpoints.common.EndpointUtils;
import org.digijava.kernel.ampapi.endpoints.dashboards.services.AidPredictabilityChartData;
import org.digijava.kernel.ampapi.endpoints.dashboards.services.AmpColorThresholdWrapper;
import org.digijava.kernel.ampapi.endpoints.dashboards.services.DashboardsService;
import org.digijava.kernel.ampapi.endpoints.dashboards.services.FundingTypeChartData;
import org.digijava.kernel.ampapi.endpoints.dashboards.services.HeatMap;
import org.digijava.kernel.ampapi.endpoints.dashboards.services.HeatMapConfigService;
import org.digijava.kernel.ampapi.endpoints.dashboards.services.HeatMapConfigs;
import org.digijava.kernel.ampapi.endpoints.dashboards.services.HeatMapService;
import org.digijava.kernel.ampapi.endpoints.dashboards.services.MeService;
import org.digijava.kernel.ampapi.endpoints.dashboards.services.ProjectAmounts;
import org.digijava.kernel.ampapi.endpoints.dashboards.services.PublicServices;
import org.digijava.kernel.ampapi.endpoints.dashboards.services.TopChartData;
import org.digijava.kernel.ampapi.endpoints.dashboards.services.TopChartType;
import org.digijava.kernel.ampapi.endpoints.dashboards.services.TopDescription;
import org.digijava.kernel.ampapi.endpoints.dashboards.services.TopsChartService;
import org.digijava.kernel.ampapi.endpoints.gis.SettingsAndFiltersParameters;
import org.digijava.kernel.ampapi.endpoints.indicator.IndicatorYearValues;
import org.digijava.kernel.ampapi.endpoints.indicator.manager.IndicatorManagerService;
import org.digijava.kernel.ampapi.endpoints.indicator.manager.MEIndicatorDTO;
import org.digijava.kernel.ampapi.endpoints.indicator.manager.ProgramSchemeDTO;
import org.digijava.kernel.ampapi.endpoints.indicator.manager.SectorDTO;
import org.digijava.kernel.ampapi.endpoints.security.AuthRule;
import org.digijava.kernel.ampapi.endpoints.util.ApiMethod;
import org.digijava.kernel.exception.DgException;
import org.digijava.module.esrigis.dbentity.AmpApiState;
import org.digijava.module.esrigis.dbentity.ApiStateType;

import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.Consumes;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.GET;
import javax.ws.rs.OPTIONS;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;

import static org.dgfoundation.amp.ar.MeasureConstants.ACTUAL_DISBURSEMENTS;
import static org.dgfoundation.amp.ar.MeasureConstants.PLANNED_DISBURSEMENTS;

/**
 * @author Diego Dimunzio
 * - All dashboards end points
 */

@Path("dashboard")
@Api("dashboard")
public class EndPoints {

    @GET
    @Path("/tops")
    @Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
    @ApiMethod(ui = false, id = "topsList")
    @ApiOperation(
            value = "List properties for top funding charts.",
            notes = "Always returns \"Donor Agency\", \"Region\", \"Primary Sector\".")
    public List<TopDescription> getAdminLevelsTotalsist() {
        return DashboardsService.getTopsList();
    }



    @OPTIONS
    @Path("/tops/{type}")
    @ApiOperation(
            value = "Describe options for endpoint",
            notes = "Enables Cross-Origin Resource Sharing for endpoint")
    public Response describeTopsDashboard() {
        return PublicServices.buildOkResponseWithOriginHeaders("");
    }

    @POST
    @Path("/tops/{type}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
    @ApiMethod(ui = false, id = "tops")
    @ApiOperation("Get top funding by property")
    @ApiResponses(@ApiResponse(code = HttpServletResponse.SC_OK, message = "Top chart data",
            response = TopChartData.class))
    public Response getAdminLevelsTotals(SettingsAndFiltersParameters config,
                                         @ApiParam("Property") @PathParam("type") TopChartType type,
                                         @DefaultValue("5") @QueryParam("limit") Integer limit) {
        return PublicServices.buildOkResponseWithOriginHeaders(
                new TopsChartService(config, type, limit).buildChartDataAggregated());
    }

    @POST
    @Path("/tops/{type}/{id}")
    @Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
    @ApiMethod(ui = false, id = "topsDataDetail")
    @ApiOperation("List projects for a top funding property value")
    public ProjectAmounts getChartsDataDetail(DashboardFormParameters config,
                                              @ApiParam("Property") @PathParam("type") TopChartType type,
                                              @ApiParam("Property value") @PathParam("id") Long id) {
        int offset = DashboardsService.getOffset(config);
        return new TopsChartService(config, type, id).buildChartData(offset);
    }

    @POST
    @Path("/aid-predictability")
    @Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
    @ApiMethod(ui = false, id = "aidPredictability")
    @ApiOperation(
            value = "Get aid predictability data",
            notes = "Planned & actual disbursements grouped by year")
    public AidPredictabilityChartData getAidPredictability(SettingsAndFiltersParameters filter) {
        return DashboardsService.getAidPredictability(filter);
    }

    @POST
    @Path("/aid-predictability/{year}/{measure}")
    @Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
    @ApiMethod(ui = false, id = "aidPredictabilityDataDetail")
    @ApiOperation("List projects for one aid predictability item")
    public ProjectAmounts getAidPredictabilityProjects(
            DashboardFormParameters filter,
            @PathParam("year") String year,
            @ApiParam(allowableValues = PLANNED_DISBURSEMENTS + "," + ACTUAL_DISBURSEMENTS)
            @PathParam("measure") String measure) {
        return DashboardsService.getAidPredictabilityProjects(filter, year, measure);
    }

    @OPTIONS
    @Path("/ftype")
    @ApiOperation(
            value = "Describe options for endpoint",
            notes = "Enables Cross-Origin Resource Sharing for endpoint")
    public Response describeFundingTypeDashboard() {
        return PublicServices.buildOkResponseWithOriginHeaders("");
    }

    @POST
    @Path("/ftype")
    @Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
    @ApiMethod(ui = false, id = "ftype")
    @ApiOperation("Get funding type chart data")
    @ApiResponses(@ApiResponse(code = HttpServletResponse.SC_OK, message = "Funding type chart data",
            response = FundingTypeChartData.class))
    public Response getFundingType(SettingsAndFiltersParameters config) {
        return PublicServices.buildOkResponseWithOriginHeaders(DashboardsService.getFundingTypeChartData(config));
    }

    @OPTIONS
    @Path("/finstrument")
    @ApiOperation(
            value = "Describe options for endpoint",
            notes = "Enables Cross-Origin Resource Sharing for endpoint")
    public Response describeFinancingInstrument() {
        return PublicServices.buildOkResponseWithOriginHeaders("");
    }

    @POST
    @Path("/finstrument")
    @Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
    @ApiMethod(ui = false, id = "ftype")
    @ApiOperation("Get financing instrument chart data")
    @ApiResponses(@ApiResponse(code = HttpServletResponse.SC_OK, message = "Funding istrumet chart data",
            response = FundingTypeChartData.class))
    public Response getFinancingInstrument(SettingsAndFiltersParameters config) {
        return PublicServices.buildOkResponseWithOriginHeaders(
                DashboardsService.getFinancingInstrumentChartData(config));
    }


    @POST
    @Path("/ftype/{year}/{id}")
    @Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
    @ApiMethod(ui = false, id = "ftypeDataDetail")
    @ApiOperation("List projects for funding type & year")
    public ProjectAmounts getFundingTypeProjects(DashboardFormParameters config,
                                                 @PathParam("year") String year,
                                                 @ApiParam("id of the funding type") @PathParam("id") Integer id) {
        return DashboardsService.getProjectsByFundingTypeAndYear(config, year, id);
    }

    @POST
    @Path("/saved-charts")
    @Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
    @ApiMethod(ui = false, id = "SaveChart")
    @ApiOperation("Save the state of a chart")
    public AmpApiState saveChart(@JsonView(AmpApiState.DetailView.class) AmpApiState chart) {
        return EndpointUtils.saveApiState(chart, ApiStateType.C);
    }

    @GET
    @Path("/saved-charts/{chartId}")
    @Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
    @ApiMethod(ui = false, id = "ChartById")
    @JsonView(AmpApiState.DetailView.class)
    @ApiOperation("Get the state of a chart")
    public AmpApiState getSavedChart(@PathParam("chartId") Long chartId) {
        return EndpointUtils.getApiState(chartId, ApiStateType.C);
    }

    @GET
    @Path("/saved-charts")
    @Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
    @ApiMethod(ui = false, id = "ChartList")
    @ApiOperation("Retrieve a list of saved charts.")
    @JsonView(AmpApiState.BriefView.class)
    public List<AmpApiState> getSavedCharts() {
        return EndpointUtils.getApiStateList(ApiStateType.C);
    }

    @POST
    @Path("/tops/ndd/{id}")
    @Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
    @ApiMethod(ui = false, id = "ndd_projects")
    @ApiOperation("List peace marked projects for a secondary program")
    public ProjectAmounts getAdminLevelsTotals(DashboardFormParameters config, @PathParam("id") Integer id) {
        //TODO: Once we implement details for all top charts we can change the path to '/tops/details/' 
        // and send the type of chart and category id as params. 
        return DashboardsService.getPeaceMarkerProjectsByCategory(config, id);
    }

    @POST
    @Path("/heat-map/{type}")
    @Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
    @ApiMethod(ui = false, id = "heatMap")
    @ApiOperation(
            value = "Build Heat Map",
            notes = "IMPORTANT NOTE ABOUT /{type} PARAMETER: This extra parameter is needed here because the UI "
                    + "differentiates each heatmap in the dashboard by its url, "
                    + "so we need for each heatmap (by Sector, Location or Program) an extra parameter that isn't "
                    + "actually used on the backend.")
    public HeatMap getHeatMap(
            @ApiParam(allowableValues = "sec, loc, prg") @PathParam("type") String type,
            HeatMapParameters config) {
        return new HeatMapService(config).buildHeatMap();
    }


    @POST
    @Path("/heat-map/{type}/{xId}/{yId}")
    @Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
    @ApiMethod(ui = false, id = "heatMapDataDetail")
    @ApiOperation("List projects for one Heat Map cell")
    public ProjectAmounts getHeatMapDataDetail(ListHeatMapProjectsParam config,
                                               @ApiParam(allowableValues = "sec, loc, prg")
                                               @PathParam("type") String type,
                                               @ApiParam("id from x axis of Heat Map matrix.")
                                               @PathParam("xId") Long xId,
                                               @ApiParam("id from y axis of Heat Map matrix.")
                                               @PathParam("yId") Long yId) {

        Integer offset = MoreObjects.firstNonNull(config.getOffset(), 0);
        return new HeatMapService(config, xId, yId).buildHeatMapDetail(offset);
    }

    @GET
    @Path("/heat-map/configs")
    @Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
    @ApiMethod(ui = false, id = "heatMapConfigs")
    @ApiOperation("Provides possible HeatMap Configurations.")
    public HeatMapConfigs getHeatMapConfigs() {
        return new HeatMapConfigService().getHeatMapConfigs();
    }

    @GET
    @Path("/heat-map/settings")
    @Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
    @ApiMethod(ui = false, id = "readHeatMapSettings", authTypes = {AuthRule.IN_ADMIN})
    @ApiOperation(
            value = "List HeatMap colors and thresholds",
            notes = "The user must be logged-in as admin to call this method.")
    public AmpColorThresholdWrapper getHeatMapSettings() {
        return new HeatMapConfigService().getHeatMapAdminSettings();
    }

    @POST
    @Path("/heat-map/settings")
    @Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
    @ApiMethod(ui = false, id = "readHeatMapSettings", authTypes = {AuthRule.IN_ADMIN})
    @ApiOperation(
            value = "Update HeatMap color thresholds",
            notes = "Note: for now we have a fixed set of colors, but in future we may want to allow different "
                    + "number of colors and nuances. Only thresholds can be changed.")
    public void setHeatMapSettings(AmpColorThresholdWrapper config) {
        new HeatMapConfigService().saveHeatMapAdminSettings(config);
    }

    @GET
    @Path("sectors")
    @Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
    @ApiMethod(id = "getMeSectors")
    @ApiOperation(value = "Retrieve and provide a list of M&E sectors.")
    public final List<SectorDTO> getSectors() {
        return new IndicatorManagerService().getSectors();
    }

    @GET
    @Path("indicators")
    @Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
    @ApiMethod(id = "getMeIndicators")
    @ApiOperation(value = "Retrieve and provide a list of M&E indicators.")
    public final List<MEIndicatorDTO> getIndicators() {
        return new IndicatorManagerService().getMEIndicators();
    }

    @GET
    @Path("programConfiguration")
    @Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
    @ApiMethod(id = "getMeProgramConfiguration")
    @ApiOperation(value = "Retrieve and provide a list of M&E program configurations.")
    public final List<ProgramSchemeDTO> getProgramConfiguration() {
        return MeService.getProgramConfiguration();
    }

    /**
     * Returns indicator values for indicators attached to a program
     *   [{
     *         "baseValue": 1000,
     *         "actualValues": [
     *             {
     *                 "year": 2021,
     *                 "value": 0
     *             },
     *             {
     *                 "year": 2022,
     *                 "value": 3000.000000000000
     *             },
     *             {
     *                 "year": 2023,
     *                 "value": 553.000000000000
     *             }
     *         ],
     *         "targetValue": 3000,
     *         "indicatorId": 11
     *     },]
     * @param id
     * @param params
     * @return
     */
    @POST
    @Path("/me/programReport/{id}")
    @Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
    @ApiMethod(id = "getMeProgramReport")
    @ApiOperation(value = "Returns indicator values for program.")
    public List<IndicatorYearValues> getIndicatorYearValuesByProgram(@PathParam("id") Long id,
                                                                     SettingsAndFiltersParameters params) {
        return new MeService().getIndicatorValuesByProgramId(id, params);
    }

    @GET
    @Path("/me/indicatorsByProgram/{id}")
    @Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
    @ApiMethod(id = "getMeIndicatorsByProgram")
    @ApiOperation(value = "Retrieve and provide a list of M&E indicators by program.")
    public final List<MEIndicatorDTO> getIndicatorsByProgram(@PathParam("id") Long programId) {
        return new MeService().getIndicatorsByProgram(programId);
    }

    /**
     * Returns indicator values for indicator
     * {
     *         "baseValue": 1000,
     *         "actualValues": [
     *             {
     *                 "year": 2021,
     *                 "value": 0
     *             },
     *             {
     *                 "year": 2022,
     *                 "value": 3000.000000000000
     *             },
     *             {
     *                 "year": 2023,
     *                 "value": 553.000000000000
     *             }
     *         ],
     *         "targetValue": 3000,
     *         "indicatorId": 11
     *     }
     * @param id
     * @param params
     * @return
     */
    @POST
    @Path("/me/indicatorReport/{id}")
    @Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
    @ApiMethod(id = "getValuesForIndicator")
    @ApiOperation(value = "Returns indicator values for indicator.")
    public IndicatorYearValues getIndicatorYearValuesByIndicator(@PathParam("id") Long id,
                                                                 SettingsAndFiltersParameters params) {
        return new MeService().getIndicatorYearValuesByIndicatorId(id, params);
    }

    @GET
    @Path("/me/indicatorsBySector/{id}")
    @Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
    @ApiMethod(id = "getMeIndicatorsBySectorReport")
    @ApiOperation(value = "Retrieve and provide a list of M&E indicators by sector.")
    public final List<MEIndicatorDTO> getIndicatorsBySector(@PathParam("id") Long sectorId) {
        return new MeService().getIndicatorsBySector(sectorId);
    }
}
