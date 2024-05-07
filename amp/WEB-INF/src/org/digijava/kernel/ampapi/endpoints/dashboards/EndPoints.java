package org.digijava.kernel.ampapi.endpoints.dashboards;

import com.fasterxml.jackson.annotation.JsonView;
import com.google.common.base.MoreObjects;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.dgfoundation.amp.ar.ArConstants;
import org.dgfoundation.amp.ar.ColumnConstants;
import org.dgfoundation.amp.ar.MeasureConstants;
import org.dgfoundation.amp.newreports.*;
import org.dgfoundation.amp.nireports.NiReportsEngine;
import org.dgfoundation.amp.reports.saiku.export.SaikuReportHtmlRenderer;
import org.digijava.kernel.ampapi.endpoints.common.EndpointUtils;
import org.digijava.kernel.ampapi.endpoints.dashboards.services.*;
import org.digijava.kernel.ampapi.endpoints.gis.SettingsAndFiltersParameters;
import org.digijava.kernel.ampapi.endpoints.indicator.AmpDashboard.DashboardCoreIndicatorValue;
import org.digijava.kernel.ampapi.endpoints.indicator.AmpDashboard.DashboardIndicatorCoreData;
import org.digijava.kernel.ampapi.endpoints.indicator.IndicatorYearValues;
import org.digijava.kernel.ampapi.endpoints.indicator.ProgramIndicatorValues;
import org.digijava.kernel.ampapi.endpoints.indicator.YearValue;
import org.digijava.kernel.ampapi.endpoints.indicator.manager.IndicatorManagerService;
import org.digijava.kernel.ampapi.endpoints.indicator.manager.MEIndicatorDTO;
import org.digijava.kernel.ampapi.endpoints.indicator.manager.ProgramSchemeDTO;
import org.digijava.kernel.ampapi.endpoints.indicator.manager.SectorDTO;
import org.digijava.kernel.ampapi.endpoints.reports.ReportFormParameters;
import org.digijava.kernel.ampapi.endpoints.reports.ReportsUtil;
import org.digijava.kernel.ampapi.endpoints.security.AuthRule;
import org.digijava.kernel.ampapi.endpoints.settings.SettingsUtils;
import org.digijava.kernel.ampapi.endpoints.util.ApiMethod;
import org.digijava.kernel.ampapi.endpoints.util.FilterUtils;
import org.digijava.kernel.exception.DgException;
import org.digijava.module.aim.dbentity.AmpSectorScheme;
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
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static org.dgfoundation.amp.ar.MeasureConstants.ACTUAL_DISBURSEMENTS;
import static org.dgfoundation.amp.ar.MeasureConstants.PLANNED_DISBURSEMENTS;
import static org.digijava.kernel.ampapi.endpoints.common.EPConstants.REPORT_TYPE_ID_MAP;

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
    @Path("sectorClassification")
    @Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
    @ApiMethod(id = "getMeSectorConfiguration")
    @ApiOperation(value = "Retrieve and provide a list of M&E sector configurations.")
    public final List<SectorClassificationDTO> getSectorSchemes() {
        return new MeService().getSectorClassification();
    }

    @GET
    @Path("indicatorsByClassification/{id}")
    @Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
    @ApiMethod(id = "getMeIndicatorsByClassification")
    @ApiOperation(value = "Retrieve and provide a list of M&E indicators by classification.")
    public final List<MEIndicatorDTO> getIndicatorsByClassification(@PathParam("id") Long classificationId) {
        return new MeService().getIndicatorsBySectorClassification(classificationId);
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

    @OPTIONS
    @Path("/me/indicatorReportsByProgramCountry")
    @ApiOperation(
            value = "Describe options for endpoint",
            notes = "Enables Cross-Origin Resource Sharing for endpoint")
    public Response describeIndicatorReportsByProgramCountry() {
        return PublicServices.buildOkResponseWithOriginHeaders("");
    }

    /**
     * Returns array of indicators values
     * [
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
     *         "indicatorId": 11,
     *         "indicatorName": ""
     *     }
     * ]
     * @param params
     * @return
     */
    @POST
    @Path("/me/indicatorReportsByProgramCountry")
    @Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
    @ApiMethod(id = "getValuesForIndicatorsCountryProgram")
    @ApiOperation(value = "Returns indicator report values for all indicators.")
    public Response getIndicatorYearValuesByIndicatorsCountryProgram(SettingsAndFiltersParameters params) {
        List<ProgramIndicatorValues> resp = new MeService().getIndicatorYearValuesByIndicatorCountryProgramId(params);
        return PublicServices.buildOkResponseWithOriginHeaders(resp);
    }

    @GET
    @Path("/me/indicatorsBySector/{id}")
    @Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
    @ApiMethod(id = "getMeIndicatorsBySectorReport")
    @ApiOperation(value = "Retrieve and provide a list of M&E indicators by sector.")
    public final List<MEIndicatorDTO> getIndicatorsBySector(@PathParam("id") Long sectorId) {
        return new MeService().getIndicatorsBySector(sectorId);
    }

    @POST
    @Path("/me/dashboardCoreIndicatorData")
    @Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
    @ApiMethod(id = "getCoreIndicatorData")
    @ApiOperation(value = "Returns indicator report values for all indicators.")
    public Response getCoreIndicatorData(ReportFormParameters formParams) {
        ReportSpecificationImpl
                spec = new ReportSpecificationImpl("indicator-data", ArConstants.INDICATOR_TYPE);
        spec.addColumn(new ReportColumn(ColumnConstants.LOCATION_ADM_LEVEL_0));
        spec.addColumn(new ReportColumn(ColumnConstants.NATIONAL_PLANNING_OBJECTIVES_LEVEL_1));
        spec.addColumn(new ReportColumn(ColumnConstants.DONOR_AGENCY));
        spec.addColumn(new ReportColumn(ColumnConstants.INDICATOR_NAME));

        spec.getHierarchies().add(new ReportColumn(ColumnConstants.LOCATION_ADM_LEVEL_0));
        spec.getHierarchies().add(new ReportColumn(ColumnConstants.NATIONAL_PLANNING_OBJECTIVES_LEVEL_1));
        spec.getHierarchies().add(new ReportColumn(ColumnConstants.DONOR_AGENCY));
        spec.getHierarchies().add(new ReportColumn(ColumnConstants.INDICATOR_NAME));

        spec.addMeasure(new ReportMeasure(MeasureConstants.INDICATOR_ACTUAL_VALUE));
        spec.setSummaryReport(true);
        spec.setGroupingCriteria(GroupingCriteria.GROUPING_YEARLY);

        new MeService().applySettingsAndFilters(new SettingsAndFiltersParameters(), spec);
        GeneratedReport report = EndpointUtils.runReport(spec);
        List<DashboardIndicatorCoreData> resp = processReportData(report);
        SaikuReportHtmlRenderer htmlRenderer = new SaikuReportHtmlRenderer(report);
        return PublicServices.buildOkResponseWithOriginHeaders(htmlRenderer.renderTable().toString());
    }

    private List<DashboardIndicatorCoreData> processReportData(GeneratedReport report) {

        ReportOutputColumn countryData = report.leafHeaders.get(0);
        ReportOutputColumn pilar = report.leafHeaders.get(1);
        ReportOutputColumn donorData = report.leafHeaders.get(2);
        ReportOutputColumn indicatorsData = report.leafHeaders.get(3);

        List<DashboardIndicatorCoreData> ampDashboardCoreIndicator = new ArrayList<>();
        for (ReportArea child : report.reportContents.getChildren()) {
            TextCell countryDataCell = (TextCell) child.getContents().get(countryData);
            if (child.getChildren() != null) {
                for (ReportArea pilarData : child.getChildren()) {
                    TextCell pilarCell = (TextCell) pilarData.getContents().get(pilar);
                        for (ReportArea donor : pilarData.getChildren()) {
                            TextCell donorCell = (TextCell) donor.getContents().get(donorData);
                            DashboardIndicatorCoreData fundingReport = new DashboardIndicatorCoreData();
                            fundingReport.setDonor(donorCell.value.toString());
                            fundingReport.setPillar(pilarCell.value.toString());
                            fundingReport.setCountry(countryDataCell.value.toString());
                            List<DashboardCoreIndicatorValue> valuesList = new ArrayList<DashboardCoreIndicatorValue>();
                            for(ReportArea indicator : donor.getChildren()){
                                DashboardCoreIndicatorValue value = new DashboardCoreIndicatorValue();
                                TextCell indicatorCell = (TextCell) indicator.getContents().get(indicatorsData);
                                value.setIndicator(indicatorCell.value.toString());
                                value.setIndicator_id(indicatorCell.entityId);

//                                for (Map.Entry<ReportOutputColumn, ReportCell> entry : indicator.getContents().entrySet()) {
//                                    ReportOutputColumn col = entry.getKey();
//
//                                    if (col.parentColumn != null
//                                            && col.originalColumnName.equals(MeasureConstants.INDICATOR_ACTUAL_VALUE)
//                                            && col.parentColumn.parentColumn != null
//                                            && col.parentColumn.parentColumn.originalColumnName.equals(
//                                            NiReportsEngine.FUNDING_COLUMN_NAME)
//                                            && col.parentColumn.parentColumn.parentColumn == null) {
//                                        AmountCell cell = (AmountCell) entry.getValue();
//                                        BigDecimal actualValue = cell.extractValue();
//                                        value.setActualValue(actualValue);
//                                    }
//                                }
                                valuesList.add(value);
                            }
                            fundingReport.setValues(valuesList);
                            ampDashboardCoreIndicator.add(fundingReport);
                        }
                }
            }
        }

        return ampDashboardCoreIndicator;
    }
}


