package org.digijava.kernel.ampapi.endpoints.gis;

import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
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

import org.apache.log4j.Logger;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.codehaus.jackson.node.POJONode;
import org.codehaus.jackson.node.TextNode;
import org.dgfoundation.amp.newreports.AmountsUnits;
import org.digijava.kernel.ampapi.endpoints.common.EndpointUtils;
import org.digijava.kernel.ampapi.endpoints.common.TranslationUtil;
import org.digijava.kernel.ampapi.endpoints.dto.gis.IndicatorLayers;
import org.digijava.kernel.ampapi.endpoints.errors.ErrorReportingEndpoint;
import org.digijava.kernel.ampapi.endpoints.gis.services.ActivityLocationExporter;
import org.digijava.kernel.ampapi.endpoints.gis.services.ActivityService;
import org.digijava.kernel.ampapi.endpoints.gis.services.ActivityStructuresExporter;
import org.digijava.kernel.ampapi.endpoints.gis.services.BoundariesService;
import org.digijava.kernel.ampapi.endpoints.gis.services.GapAnalysis;
import org.digijava.kernel.ampapi.endpoints.gis.services.GisUtils;
import org.digijava.kernel.ampapi.endpoints.gis.services.LocationService;
import org.digijava.kernel.ampapi.endpoints.gis.services.MapTilesService;
import org.digijava.kernel.ampapi.endpoints.gis.services.PublicGapAnalysis;
import org.digijava.kernel.ampapi.endpoints.indicator.IndicatorEPConstants;
import org.digijava.kernel.ampapi.endpoints.indicator.IndicatorUtils;
import org.digijava.kernel.ampapi.endpoints.performance.PerformanceRuleConstants;
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
import org.digijava.module.aim.dbentity.AmpIndicatorColor;
import org.digijava.module.aim.dbentity.AmpIndicatorLayer;
import org.digijava.module.aim.dbentity.AmpStructure;
import org.digijava.module.aim.helper.FormatHelper;
import org.digijava.module.aim.util.ColorRampUtil;
import org.digijava.module.categorymanager.dbentity.AmpCategoryValue;
import org.digijava.module.esrigis.dbentity.AmpApiState;
import org.digijava.module.esrigis.dbentity.AmpMapConfig;
import org.digijava.module.esrigis.helpers.DbHelper;
import org.digijava.module.esrigis.helpers.MapConstants;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;


/**
 * Class that holds entrypoing for GIS api methods
 * 
 * @author ddimunzio@developmentgateway.org jdeanquin@developmentgateway.org
 * 
 */
@Path("gis")
public class GisEndPoints implements ErrorReportingEndpoint {
    private static final Logger logger = Logger.getLogger(GisEndPoints.class);
    
    @GET
    @Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
    public List<AvailableMethod> getAvailableFilters() {
        return EndpointUtils.getAvailableMethods(GisEndPoints.class.getName());
    }   

    /**
     * Returns Aggregate ADM info by ADM Level
     * 
     * @param filter
     *            adminLevel to filter, the json should look like {
     *            "FiltersParams":
     *            {"params":[{"filterName":"adminLevel","filterValue"
     *            :["Region"]}] } }
     * 
     *            Available regions
     * @return
     * @throws AmpApiException 
     */
    @POST
    @Path("/cluster")
    @Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
    @ApiMethod(ui = false, id = "ClusterPointsByAdmin")
    public final FeatureCollectionGeoJSON getClusteredPointsByAdm(
            final JsonBean config) throws AmpApiException {

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

    /**
     * Returns Aggregate ADM info by ADM Level
     * 
     * @param filter
     *            adminLevel to filter, the json should look like {
     *            "FiltersParams":
     *            {"params":[{"filterName":"adminLevel","filterValue"
     *            :["Region"]}] } }
     * 
     *            Available regions
     * @return
     * @throws AmpApiException 
     */
    @SuppressWarnings("unchecked")
    @POST
    @Path("/structures")
    @Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
    @ApiMethod(ui = false, id = "Structures")
    public final FeatureCollectionGeoJSON getProjectSites(final JsonBean config,@QueryParam("startFrom") Integer startFrom,
            @QueryParam("size")Integer size) throws AmpApiException {
        FeatureCollectionGeoJSON f = new FeatureCollectionGeoJSON();
        List<AmpStructure> al = LocationService.getStructures( config);
        int start = 0;
        int end = al.size() -1;
        if (startFrom!=null && size!=null && startFrom < al.size()) {
            start = startFrom.intValue();
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
    public JsonBean savedMaps(final JsonBean pMap) {
        return EndpointUtils.saveApiState(pMap,"G");
    }



    @GET
    @Path("/saved-maps/{mapId}")
    @Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
    @ApiMethod(ui = false, id = "MapById")
    public JsonBean savedMaps(@PathParam("mapId") Long mapId) {
        return EndpointUtils.getApiState(mapId);

    }




    @GET
    @Path("/saved-maps")
    @Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
    @ApiMethod(ui = false, id = "MapList")
    public List<JsonBean> savedMaps() {
        String type="G";
        return EndpointUtils.getApiStateList(type);
    }



    @GET
    @Path("/indicator-layers")
    @Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
    @ApiMethod(ui = false, id = "IndicatorLayers")
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

    /**
    * Config sample <br/>
    * {                                                 <br/>
    *  "columnFilters":{                                <br/>  
    *   "National Planning Objectives Level 1 Id":[     <br/>  
    *     1,2,3,4                                       <br/>
    *   ],                                              <br/>
    *  "Primary Sector Sub-Sector Id":[                 <br/>  
    *     11,22,32,43                                   <br/>
    *   ]                                               <br/>
    * },                                                <br/>
    * "filters":{                                       <br/>
    *   "date":{                                        <br/>
    *      "start":"1967-01-01",                        <br/>
    *      "end":"2015-12-31"                           <br/>
    *  },                                               <br/>
    *  "keyWord":"some activity"                        <br/>
    * },                                                <br/>
    * “settings” :  {                                   <br/>
    *       "0" : [“Actual Commitments”, “Actual Disbursements”],   <br/>
    *       "1" : “USD”,                                <br/>
    *       "2" : “123”                                 <br/>
    *   }                                               <br/>
    * }                                                 <br/>
    */
    @POST
    @Path("/activities")
    @Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
    @ApiMethod(ui = false, id = "ActivitiesNewLists")
    public JsonBean getActivitiesNew(JsonBean config, @QueryParam("start") Integer page,@QueryParam("size") Integer pageSize) {
        logger.error(String.format("Requesting %s pagesize from %s page", pageSize, page));
        try {
            Integer reqNumber = (pageSize == null) || (page == null) ? null : pageSize * page; 
            return ActivityService.getActivities(config, null, reqNumber, pageSize);
        }catch(AmpApiException ex){
            throw new WebApplicationException(ex);
        }   
    }

    /**
     * return activity by id in the format /activities/12,15,16
     * @param config {@link #getActivitiesNew  Json config}
     * @param activityIds comma separated list of ids
     * @return
     * @see #getActivitiesNew
     */
    @POST
    @Path("/activities/{activityId}") //once its done remove the New
    @Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
    @ApiMethod(ui = false, id = "ActivitiesById")
    public JsonBean getActivities(JsonBean config, @PathParam("activityId") PathSegment activityIds) {
        try {
            return ActivityService.getActivities(config,
                    Arrays.asList(activityIds.getPath().split("\\s*,\\s*")),
                    null, null);
        } catch (AmpApiException ex) {
            throw new WebApplicationException(ex); 
        }
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


    
    @POST
    @Path("/locationstotals/{admlevel}")
    @Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
    @ApiMethod(ui = false, id = "locationstotals")  
    public JsonBean getAdminLevelsTotals(JsonBean filters, @PathParam ("admlevel") String admlevel){
        LocationService ls = new LocationService();
        // this Service was resetting the amount units so far (used by this EP only), now changed its interface to allow other "users" to not reset it
        return ls.getTotals(admlevel, filters, AmountsUnits.AMOUNTS_OPTION_UNITS);
    }
    
    
    @GET
    @Path("/indicators")
    @Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
    @ApiMethod(ui = false, id = "IndicatorsList")
    public List<JsonBean> getIndicators(@QueryParam("admLevel") String admLevel) {
        List<AmpIndicatorLayer> indicators;
        if (admLevel !=null) {
            indicators = QueryUtil.getIndicatorByCategoryValue(admLevel);
            return generateIndicatorJson(indicators, false);
        }
        else {
            indicators = QueryUtil.getIndicatorLayers();
            return generateIndicatorJson(indicators, true);

        }
    }
    
    @POST
    @Path("/indicators/{indicatorId}")
    @Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
    @ApiMethod(ui = false, id = "IndicatorById")
    public JsonBean getIndicatorsById(JsonBean input, @PathParam ("indicatorId") Long indicatorId) {
        boolean isGapAnalysis = EndpointUtils.getSingleValue(input, IndicatorEPConstants.DO_GAP_ANALYSIS, Boolean.FALSE);
        return IndicatorUtils.getIndicatorsAndLocationValues(indicatorId, input, isGapAnalysis);
    }
    
    private List<JsonBean> generateIndicatorJson (List<AmpIndicatorLayer> indicators,boolean includeAdmLevel) {
        List<JsonBean> indicatorsJson = new ArrayList<JsonBean>();
        GapAnalysis gapAnalysis = new GapAnalysis();

        for (AmpIndicatorLayer indicator : indicators) {
            JsonBean json = new JsonBean();
            json.set(IndicatorEPConstants.ID, indicator.getId());
            json.set(IndicatorEPConstants.NAME, TranslationUtil.getTranslatableFieldValue(IndicatorEPConstants.NAME, indicator.getName(), indicator.getId()));
            json.set(IndicatorEPConstants.DESCRIPTION, TranslationUtil.getTranslatableFieldValue(IndicatorEPConstants.DESCRIPTION, indicator.getDescription(), indicator.getId()));
            json.set(IndicatorEPConstants.UNIT, TranslationUtil.getTranslatableFieldValue(IndicatorEPConstants.UNIT, indicator.getUnit(), indicator.getId()));
            json.set(IndicatorEPConstants.ID, indicator.getId());
            if (includeAdmLevel) {
                json.set(IndicatorEPConstants.ADM_LEVEL_ID, indicator.getAdmLevel().getId());
                json.set(IndicatorEPConstants.ADM_LEVEL_NAME, indicator.getAdmLevel().getLabel());
                json.set(IndicatorEPConstants.ADMIN_LEVEL, IndicatorEPConstants.ADM_PREFIX + indicator.getAdmLevel().getIndex());
            }
            json.set(IndicatorEPConstants.NUMBER_OF_CLASSES, indicator.getNumberOfClasses());
            json.set(IndicatorEPConstants.ACCESS_TYPE_ID, indicator.getAccessType().getValue());
            json.set(IndicatorEPConstants.INDICATOR_TYPE_ID, indicator.getIndicatorType() == null ? null : indicator.getIndicatorType().getId());
            json.set(IndicatorEPConstants.CAN_DO_GAP_ANALYSIS, gapAnalysis.canDoGapAnalysis(indicator));
            json.set(IndicatorEPConstants.FIELD_ZERO_CATEGORY_ENABLED, indicator.getZeroCategoryEnabled());
            
            json.set(IndicatorEPConstants.CREATED_ON, FormatHelper.formatDate(indicator.getCreatedOn()));
            json.set(IndicatorEPConstants.UPDATED_ON, FormatHelper.formatDate(indicator.getUpdatedOn()));

            if (indicator.getCreatedBy() != null) {
                json.set(IndicatorEPConstants.CREATE_BY, indicator.getCreatedBy().getUser().getEmail());
            }
            List<JsonBean> colors = new ArrayList<JsonBean>();
            List<AmpIndicatorColor> colorList = new ArrayList<AmpIndicatorColor>(indicator.getColorRamp());
            Collections.sort(colorList, new Comparator<AmpIndicatorColor>() {
                @Override
                public int compare(AmpIndicatorColor o1, AmpIndicatorColor o2) {
                    return o1.getPayload().compareTo(o2.getPayload());
                }
            });
            for (AmpIndicatorColor color : colorList) {
                JsonBean colorJson = new JsonBean();
                colorJson.set("color", color.getColor());
                colorJson.set("order", color.getPayload());
                colors.add(colorJson);
                 if (color.getPayload() == IndicatorEPConstants.PAYLOAD_INDEX) {
                        long colorId = ColorRampUtil.getColorId(color.getColor());
                        json.set(IndicatorEPConstants.IS_MULTI_COLOR, IndicatorEPConstants.MULTI_COLOR_PALETTES.contains(colorId));
                 }
            }
            json.set("colorRamp", colors);
            indicatorsJson.add(json);
        }
        return indicatorsJson;
    }
    
    /**
     * Clarifies if Gap Analysis can be done over for the indicator layer based on its indicator Type and ADM level
     * @param indicatorTypeId the indicator type id
     * @param admLevel administrative level id
     * @return <pre>
     * {
     *     "canDoGapAnalysis": true/false
     *     "error" : {...} // OPTIONAL, on invalid input/other errors   
     * }
     * </pre>
     * 
     */
    @GET
    @Path("/can-do-gap-analysis")
    @Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
    @ApiMethod(ui = false, id = "canDoGapAnalysis")
    public JsonBean canDoGapAnalysis(@QueryParam("indicatorTypeId") Long indicatorTypeId, 
            @QueryParam("admLevelId") Long admLevelId) {
        return new PublicGapAnalysis().canDoGapAnalysis(indicatorTypeId, admLevelId);
    }
    
    /**
     * Runs Gap Analysis directly over external indicator data, not from DB. 
     * For saved indicators Gap Analysis see {@link #getIndicatorsById(JsonBean, Long)} 
     * @param input
     * <pre>
     * {
     *   "indicator" : {...}, // full indicator data, like the one that is used for saving
     *   "filters" : {...}, // current filters
     *   "settings" : {...} // current settings if any
     * }
     * </pre>
     * @return
     */
    @POST
    @Path("/do-gap-analysis")
    @Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
    @ApiMethod(ui = false, id = "doGapAnalysis")
    public JsonBean doGapAnalysis(JsonBean input) {
        return new PublicGapAnalysis().doPublicGapAnalysis(input);
    }
    
    /**
     * Just returns the same data so the GIS code doesnt break with public layers. 
     * @param input
     * <pre>
     * {
     *   "indicator" : {...}, // full indicator data, like the one that is used for saving
     * }
     * </pre>
     * @return
     */
    @POST
    @Path("/process-public-layer")
    @Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
    @ApiMethod(ui = false, id = "processPublicLayer")
    public JsonBean processPublicLayer(JsonBean input) {
        // Due to problems on the frontend for now we receive the public layer data and if there is no gap analysis then we return it without changes.
        return input;
    }    
    
    /**
     * Export map id from current filters
     * 
     * @param webResponse
     * @param mapId
     * @param exportType type 1 to export activity locations
     *                   type 2 to export activity Structures
     * @return
     * @throws AmpApiException 
     */
    @GET
    @Path("/export-map/")
    @Produces("application/vnd.ms-excel")
    @ApiMethod(ui = false, id = "MapExport")
     public StreamingOutput getExportMap(@Context HttpServletResponse webResponse,@QueryParam("mapId") Long mapId,@DefaultValue("1") @QueryParam("exportType") Long exportType) throws AmpApiException
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
    public List<JsonBean> getClusterLevels() {
        List<AmpCategoryValue> values = QueryUtil.getClusterLevels();
        List<JsonBean> levelsJson = new ArrayList<JsonBean>();
        JSONArray boundaries = getBoundaries();
        for (AmpCategoryValue value : values) {
            if(hasMapBounderies(value, boundaries)) {
                JsonBean json = new JsonBean();
                json.set("id", value.getId());
                json.set("title", value.getLabel());
                json.set("adminLevel", "adm-" + value.getIndex());
                levelsJson.add(json);
            }
        }
        return levelsJson;
    }
    
    private boolean hasMapBounderies(AmpCategoryValue value, JSONArray boundaries) {
        for (Object object : boundaries) {
            if (object instanceof JSONObject && ("adm-"+value.getIndex()).equals(((JSONObject) object).get("id"))) {
                return true;
            }           
        }
        return false;
    }
    
    /**
     * Return the last updated activities
     * 
     * @param limit
     *             the number of activities to include
     * @param columns
     *            the name of extra columns to include on the report. The
     *            report already includes: project title, date of update and
     *            donor names
     * @return JSONObject, with the last updated activities
     */
    @POST
    @Path("/last-updated")
    @Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
    @ApiMethod(ui = false, id = "LastUpdatedActivities")
    public JSONObject getLastUpdated(@DefaultValue("10") @QueryParam("limit") Integer limit,
            @QueryParam("columns") String columns, JsonBean config) {
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
    public JSONArray getBoundaries() {
        return BoundariesService.getBoundaries();
    }
    
    @GET
    @Path("/report/{report_config_id}")
    @Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
    @ApiMethod(ui = false, id = "reportExport")
    public JsonBean getLastUpdated(@PathParam("report_config_id") String reportConfigId) {
        return ReportsUtil.getApiState(reportConfigId);
    }
    
    /**
     * Provides information about the availability or not of enabled performance rules. 
     * This information is used for configuring the GIS UI.
     * The performance rule toggle on GIS UI is only displayed if enabled performance rules are available.
     * @return <pre>
     * {
     *     "hasEnabledPerformanceRules": true/false  
     * }
     * </pre>
     * 
     */
    @GET
    @Path("/has-enabled-performance-rules")
    @Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
    @ApiMethod(ui = false, id = "hasEnabledPerformanceRules")
    public JsonBean hasEnabledPerformanceRules() {
        JsonBean result = new JsonBean();
        result.set(PerformanceRuleConstants.HAS_ENABLED_PERFORMANCE_RULES,
                !PerformanceRuleManager.getInstance().getPerformanceRuleMatchers().isEmpty());
        
        return result;
    }
    
    
    /**
     * Gets the map-tiles file from content repository.
     * 
     * @return archived file with map-tiles
     */
    @GET
    @Path("/map-tiles")
    @Produces(MediaType.APPLICATION_OCTET_STREAM)
    @ApiMethod(authTypes = {AuthRule.AUTHENTICATED, AuthRule.AMP_OFFLINE}, id = "mapTiles", ui = false)
    public Response getMapTiles() {
        return MapTilesService.getInstance().getArchivedMapTiles();
    }
    
    /**
     * Gets the amp locator objects.
     * 
     * @return locators
     */
    @GET
    @Path("/locators")
    @Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
    @ApiMethod(authTypes = {AuthRule.AUTHENTICATED, AuthRule.AMP_OFFLINE}, id = "locators", ui = false)
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
