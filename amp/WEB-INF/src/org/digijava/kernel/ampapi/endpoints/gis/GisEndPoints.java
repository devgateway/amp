package org.digijava.kernel.ampapi.endpoints.gis;

import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.StringTokenizer;

import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.PathSegment;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.StreamingOutput;

import com.fasterxml.jackson.annotation.JsonView;
import com.fasterxml.jackson.databind.node.POJONode;
import com.fasterxml.jackson.databind.node.TextNode;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.apache.log4j.Logger;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.dgfoundation.amp.newreports.AmountsUnits;
import org.digijava.kernel.ampapi.endpoints.common.EndpointUtils;
import org.digijava.kernel.ampapi.endpoints.dto.gis.IndicatorLayers;
import org.digijava.kernel.ampapi.endpoints.errors.ErrorReportingEndpoint;
import org.digijava.kernel.ampapi.endpoints.gis.services.ActivityList;
import org.digijava.kernel.ampapi.endpoints.gis.services.ActivityLocationExporter;
import org.digijava.kernel.ampapi.endpoints.gis.services.ActivityService;
import org.digijava.kernel.ampapi.endpoints.gis.services.ActivityStructuresExporter;
import org.digijava.kernel.ampapi.endpoints.gis.services.AdmLevel;
import org.digijava.kernel.ampapi.endpoints.gis.services.AdmLevelTotals;
import org.digijava.kernel.ampapi.endpoints.gis.services.BoundariesService;
import org.digijava.kernel.ampapi.endpoints.gis.services.Boundary;
import org.digijava.kernel.ampapi.endpoints.gis.services.GisUtils;
import org.digijava.kernel.ampapi.endpoints.gis.services.LocationService;
import org.digijava.kernel.ampapi.endpoints.gis.services.MapTilesService;
import org.digijava.kernel.ampapi.endpoints.gis.services.PublicGapAnalysis;
import org.digijava.kernel.ampapi.endpoints.gis.services.RecentlyUpdatedActivities;
import org.digijava.kernel.ampapi.endpoints.indicator.Indicator;
import org.digijava.kernel.ampapi.endpoints.indicator.IndicatorUtils;
import org.digijava.kernel.ampapi.endpoints.performance.PerformanceRuleManager;
import org.digijava.kernel.ampapi.endpoints.reports.ReportsUtil;
import org.digijava.kernel.ampapi.endpoints.security.AuthRule;
import org.digijava.kernel.ampapi.endpoints.util.ApiMethod;
import org.digijava.kernel.ampapi.endpoints.util.AvailableMethod;
import org.digijava.kernel.ampapi.endpoints.util.JsonBean;
import org.digijava.kernel.ampapi.exception.AmpApiException;
import org.digijava.kernel.ampapi.helpers.geojson.FeatureCollectionGeoJSON;
import org.digijava.kernel.ampapi.helpers.geojson.FeatureGeoJSON;
import org.digijava.kernel.ampapi.helpers.geojson.PointGeoJSON;
import org.digijava.kernel.ampapi.helpers.geojson.objects.ClusteredPoints;
import org.digijava.kernel.ampapi.postgis.entity.AmpLocator;
import org.digijava.kernel.ampapi.postgis.util.QueryUtil;
import org.digijava.module.aim.dbentity.AmpIndicatorLayer;
import org.digijava.module.aim.dbentity.AmpStructure;
import org.digijava.module.categorymanager.dbentity.AmpCategoryValue;
import org.digijava.module.esrigis.dbentity.AmpApiState;
import org.digijava.module.esrigis.dbentity.AmpMapConfig;
import org.digijava.module.esrigis.helpers.DbHelper;
import org.digijava.module.esrigis.helpers.MapConstants;


/**
 * Class that holds entrypoing for GIS api methods
 * 
 * @author ddimunzio@developmentgateway.org jdeanquin@developmentgateway.org
 * 
 */
@Path("gis")
@Api("gis")
public class GisEndPoints implements ErrorReportingEndpoint {
    private static final Logger logger = Logger.getLogger(GisEndPoints.class);
    
    @GET
    @Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
    @ApiOperation("Get available filters")
    public List<AvailableMethod> getAvailableFilters() {
        return EndpointUtils.getAvailableMethods(GisEndPoints.class.getName());
    }   

    @POST
    @Path("/cluster")
    @Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
    @ApiMethod(ui = false, id = "ClusterPointsByAdmin")
    @ApiOperation("Returns Aggregate ADM info by ADM Level")
    public final FeatureCollectionGeoJSON getClusteredPointsByAdm(
            @ApiParam("filter") final PerformanceFilterParameters config) throws AmpApiException {

        List<ClusteredPoints> c = LocationService.getClusteredPoints(config);
        FeatureCollectionGeoJSON result = new FeatureCollectionGeoJSON();
        for (ClusteredPoints clusteredPoints : c) {
            if (!clusteredPoints.getLon().equalsIgnoreCase("") && !clusteredPoints.getLat().equalsIgnoreCase("")){ 
            result.features.add(getPoint(new Double(clusteredPoints.getLon()),
                    new Double(clusteredPoints.getLat()),
                    clusteredPoints.getActivityids(),
                    clusteredPoints.getAdmin(), clusteredPoints.getAdmId()));
            }
        }

        return result;
    }

    private FeatureGeoJSON getPoint(Double lat, Double lon,
            List<Long> activityid, String adm, Long admId) {
        FeatureGeoJSON fgj = new FeatureGeoJSON();
        PointGeoJSON pg = new PointGeoJSON();
        pg.coordinates.add(lat);
        pg.coordinates.add(lon);
        fgj.properties.put("activityid", new POJONode(activityid));
        fgj.properties.put("admName", new TextNode(adm));
        fgj.properties.put("admId", new POJONode(admId));
        fgj.geometry = pg;
        return fgj;
    }

    @SuppressWarnings("unchecked")
    @POST
    @Path("/structures")
    @Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
    @ApiMethod(ui = false, id = "Structures")
    @ApiOperation("Returns Aggregate ADM info by ADM Level")
    public final FeatureCollectionGeoJSON getProjectSites(
            @ApiParam("filter") final PerformanceFilterParameters config,
            @QueryParam("startFrom") Integer startFrom,
            @QueryParam("size")Integer size) throws AmpApiException {
        FeatureCollectionGeoJSON f = new FeatureCollectionGeoJSON();
        List<AmpStructure> al = LocationService.getStructures( config);
        int start = 0;
        int end = al.size() -1;
        if (startFrom!=null && size!=null && startFrom < al.size()) {
            start = startFrom;
            if (al.size()>(startFrom + size)) {
                end = startFrom + size;
            }
        }
        
        for (; start <= end; start++) {
            AmpStructure structure = al.get(start);
            FeatureGeoJSON fgj = LocationService.buildFeatureGeoJSON(structure);
            if (fgj != null) {
                f.features.add(fgj);
            }

        }
        return f;
    }
 
    @POST
    @Path("/saved-maps")
    @Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
    @ApiMethod(ui = false, id = "SaveMap")
    @ApiOperation("Save map state")
    public AmpApiState savedMaps(final @JsonView(AmpApiState.DetailView.class) AmpApiState pMap) {
        return EndpointUtils.saveApiState(pMap,"G");
    }

    @GET
    @Path("/saved-maps/{mapId}")
    @Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
    @ApiMethod(ui = false, id = "MapById")
    @JsonView(AmpApiState.DetailView.class)
    @ApiOperation("Get map state")
    public AmpApiState savedMaps(@PathParam("mapId") Long mapId) {
        return EndpointUtils.getApiState(mapId);
    }

    @GET
    @Path("/saved-maps")
    @Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
    @ApiMethod(ui = false, id = "MapList")
    @JsonView(AmpApiState.BriefView.class)
    @ApiOperation("List map states")
    public List<AmpApiState> savedMaps() {
        String type="G";
        return EndpointUtils.getApiStateList(type);
    }

    @GET
    @Path("/indicator-layers")
    @Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
    @ApiMethod(ui = false, id = "IndicatorLayers")
    @ApiOperation("List indicator layers")
    public List<IndicatorLayers> getIndicatorLayers() {
        List<IndicatorLayers> indicatorLayers = new ArrayList<IndicatorLayers>();
        List<AmpMapConfig> mapsConfigs = DbHelper.getMaps();
        for (AmpMapConfig ampMapConfig : mapsConfigs) {
            IndicatorLayers i = new IndicatorLayers();
            i.setId(ampMapConfig.getId());
            i.setTitle(ampMapConfig.getConfigName());
            i.setLink(ampMapConfig.getMapUrl());
            i.setLegendNotes(ampMapConfig.getLegendNotes());
            String type = MapConstants.mapTypeNames.get(ampMapConfig
                    .getMapType());
            i.setType(type);
            indicatorLayers.add(i);
        }
        return indicatorLayers;
    }

    @POST
    @Path("/activities")
    @Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
    @ApiMethod(ui = false, id = "ActivitiesNewLists")
    @ApiOperation("List activities")
    public ActivityList getActivitiesNew(PerformanceFilterParameters config,
            @QueryParam("start") Integer page,
            @QueryParam("size") Integer pageSize) {
        logger.error(String.format("Requesting %s pagesize from %s page", pageSize, page));
        Integer reqNumber = (pageSize == null) || (page == null) ? null : pageSize * page;
        return ActivityService.getActivities(config, null, reqNumber, pageSize);
    }

    @POST
    @Path("/activities/{activityId}") //once its done remove the New
    @Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
    @ApiMethod(ui = false, id = "ActivitiesById")
    @ApiOperation(value = "List activities", notes = "activityId is a comma separated list of activity ids")
    public ActivityList getActivities(
            @ApiParam("config") PerformanceFilterParameters config,
            @PathParam("activityId") PathSegment activityIds) {
        return ActivityService.getActivities(config,
                Arrays.asList(activityIds.getPath().split("\\s*,\\s*")),
                null, null);
    }

    @POST
    @Path("/locationstotals/{admlevel}")
    @Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
    @ApiMethod(ui = false, id = "locationstotals")
    @ApiOperation("Return funding data for an administrative level")
    public AdmLevelTotals getAdminLevelsTotals(
            PerformanceFilterParameters filters,
            @PathParam("admlevel") AdmLevel admlevel) {
        LocationService ls = new LocationService();
        // this Service was resetting the amount units so far (used by this EP only), now changed its interface to allow other "users" to not reset it
        return ls.getTotals(admlevel.getLabel(), filters, AmountsUnits.AMOUNTS_OPTION_UNITS);
    }
    
    
    @GET
    @Path("/indicators")
    @Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
    @ApiMethod(ui = false, id = "IndicatorsList")
    @ApiOperation("List indicators")
    @JsonView(Indicator.IndicatorView.class)
    public List<Indicator> getIndicators(@QueryParam("admLevel") AdmLevel admLevel) {
        List<AmpIndicatorLayer> indicators;
        if (admLevel !=null) {
            indicators = QueryUtil.getIndicatorByCategoryValue(admLevel);
            return IndicatorUtils.getApiIndicatorsForGis(indicators, false);
        }
        else {
            indicators = QueryUtil.getIndicatorLayers();
            return IndicatorUtils.getApiIndicatorsForGis(indicators, true);
        }
    }

    /**
     * FIXME simplify
     * this operation is used to:
     * 1. retrieve plain values (which should be GET /gis/indicators/{indicatorId}/values)
     * 2. do gap analysis (which should be POST /gis/indicators/{indicatorId}/do-gap-analysis)
     */
    @POST
    @Path("/indicators/{indicatorId}")
    @Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
    @ApiMethod(ui = false, id = "IndicatorById")
    @ApiOperation("Get indicator")
    @JsonView(Indicator.IndicatorView.class)
    public Indicator getIndicatorsById(
            SavedIndicatorGapAnalysisParameters input,
            @PathParam ("indicatorId") Long indicatorId) {
        boolean isGapAnalysis = EndpointUtils.getSingleValue(input.getGapAnalysis(), Boolean.FALSE);
        return IndicatorUtils.getIndicatorsAndLocationValues(indicatorId, input, isGapAnalysis);
    }

    @GET
    @Path("/can-do-gap-analysis")
    @Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
    @ApiMethod(ui = false, id = "canDoGapAnalysis")
    @ApiOperation("Clarifies if Gap Analysis can be done over for the indicator layer based on its indicator "
            + "Type and ADM level")
    public boolean canDoGapAnalysis(
            @QueryParam("indicatorTypeId") Long indicatorTypeId,
            @QueryParam("admLevelId") Long admLevelId) {
        return new PublicGapAnalysis().canDoGapAnalysis(indicatorTypeId, admLevelId);
    }

    @POST
    @Path("/do-gap-analysis")
    @Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
    @ApiMethod(ui = false, id = "doGapAnalysis")
    @ApiOperation(
            value = "Runs Gap Analysis directly over external indicator data, not from DB.",
            notes = "For saved indicators Gap Analysis see `POST /gis/indicators/{indicatorId}`.")
    @JsonView(Indicator.IndicatorView.class)
    public Indicator doGapAnalysis(RuntimeIndicatorGapAnalysisParameters input) {
        return new PublicGapAnalysis().doPublicGapAnalysis(input);
    }
    
    @POST
    @Path("/process-public-layer")
    @Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
    @ApiMethod(ui = false, id = "processPublicLayer")
    @ApiOperation("Just returns the same data so the GIS code doesn't break with public layers.")
    public JsonBean processPublicLayer(JsonBean input) {
        // Due to problems on the frontend for now we receive the public layer data and if there is no gap analysis then we return it without changes.
        return input;
    }    
    
    @GET
    @Path("/export-map/")
    @Produces("application/vnd.ms-excel")
    @ApiMethod(ui = false, id = "MapExport")
    @ApiOperation("Export map id from current filters")
     public StreamingOutput getExportMap(
             @Context HttpServletResponse webResponse,
            @QueryParam("mapId") Long mapId,
            @ApiParam(value = "1=locations, 2=structures", allowableValues = "1,2")
            @DefaultValue("1") @QueryParam("exportType") Long exportType) throws AmpApiException
    {
        final HSSFWorkbook wb;
        String name="";
        JsonBean filter=null;
        if(mapId!=null){
            AmpApiState map = EndpointUtils.getSavedMap(mapId);
            filter = JsonBean.getJsonBeanFromString(map.getStateBlob());
        }
        //since it comes from a saved map and it has another structure we will use the linkedhash map thats inside
        //the Json bean to filter
        LinkedHashMap<String, Object> filters=(LinkedHashMap<String, Object>)filter.get("filters");
        if(exportType==1){
            name="map-export-project-sites.xls";
            wb = new ActivityStructuresExporter(filters).export(name);
        }else{
            name="map-export-administrative-Locations.xls";
            wb = new ActivityLocationExporter(filters).export(name);
        }
        webResponse.setHeader("Content-Disposition","attachment; filename=" + name);

        StreamingOutput streamOutput = new StreamingOutput(){
            public void write(OutputStream output) throws IOException, WebApplicationException {
                try {
                    wb.write(output);
                } catch (Exception e) {
                    throw new WebApplicationException(e);
                }
            }
        };
        return streamOutput;
      }
    

    @GET
    @Path("/clusters")
    @Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
    @ApiMethod(ui = false, id = "ClusterLevels")
    @ApiOperation("List clusters")
    public List<Cluster> getClusterLevels() {
        List<AmpCategoryValue> values = QueryUtil.getClusterLevels();
        List<Cluster> clusters = new ArrayList<>();
        List<Boundary> boundaries = getBoundaries();
        for (AmpCategoryValue value : values) {
            if (hasMapBoundaries(value, boundaries)) {
                AdmLevel adminLevel = AdmLevel.fromString("adm-" + value.getIndex());
                clusters.add(new Cluster(value.getId(), value.getLabel(), adminLevel));
            }
        }
        return clusters;
    }
    
    private boolean hasMapBoundaries(AmpCategoryValue value, List<Boundary> boundaries) {
        AdmLevel admLevel = AdmLevel.fromString("adm-" + value.getIndex());
        for (Boundary boundary : boundaries) {
            if (admLevel.equals(boundary.getId())) {
                return true;
            }           
        }
        return false;
    }

    @POST
    @Path("/last-updated")
    @Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
    @ApiMethod(ui = false, id = "LastUpdatedActivities")
    @ApiOperation("List of recently updated activities")
    public RecentlyUpdatedActivities getLastUpdated(
            @ApiParam("the number of activities to include") @DefaultValue("10") @QueryParam("limit") Integer limit,
            @ApiParam("the name of extra columns to include on the report. The report already includes: "
                    + "project title, date of update and donor names")
            @QueryParam("columns") String columns, SettingsAndFiltersParameters config) {
        List<String> extraColumns = new ArrayList<String>();
        if (columns != null) {
            StringTokenizer tokenizer = new StringTokenizer(columns, ",");
            while (tokenizer.hasMoreTokens()) {
                extraColumns.add(tokenizer.nextToken());
            }
        }
        return ActivityService.getLastUpdatedActivities(extraColumns, limit,config);
    }
    

    @GET
    @Path("/boundaries")
    @Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
    @ApiOperation("List boundaries")
    public List<Boundary> getBoundaries() {
        return BoundariesService.getBoundaries();
    }
    
    @GET
    @Path("/report/{report_config_id}")
    @Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
    @ApiMethod(ui = false, id = "reportExport")
    @ApiOperation("Load report configuration from current session")
    public JsonBean getLastUpdated(@PathParam("report_config_id") String reportConfigId) {
        return ReportsUtil.getApiState(reportConfigId);
    }
    
    @GET
    @Path("/has-enabled-performance-rules")
    @Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
    @ApiMethod(ui = false, id = "hasEnabledPerformanceRules")
    @ApiOperation(
            value = "Provides information about the availability or not of enabled performance rules.",
            notes = "This information is used for configuring the GIS UI.\n\n"
                    + "The performance rule toggle on GIS UI is only displayed if enabled performance rules are "
                    + "available.")
    public boolean hasEnabledPerformanceRules() {
        return !PerformanceRuleManager.getInstance().getPerformanceRuleMatchers().isEmpty();
    }
    
    @GET
    @Path("/map-tiles")
    @Produces(MediaType.APPLICATION_OCTET_STREAM)
    @ApiMethod(authTypes = {AuthRule.AUTHENTICATED, AuthRule.AMP_OFFLINE}, id = "mapTiles", ui = false)
    @ApiOperation(value = "Gets the map-tiles file from content repository.",
    notes = "Return archived file with map-tiles.")
    public Response getMapTiles() {
        return MapTilesService.getInstance().getArchivedMapTiles();
    }
    
    @GET
    @Path("/locators")
    @Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
    @ApiMethod(authTypes = {AuthRule.AUTHENTICATED, AuthRule.AMP_OFFLINE}, id = "locators", ui = false)
    @ApiOperation("Gets the amp locator objects.")
    public List<AmpLocator> getLocators() {
        return GisUtils.getLocators();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Class getErrorsClass() {
        return GisErrors.class;
    }
}
