package org.digijava.kernel.ampapi.endpoints.gis.services;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.codehaus.jackson.node.POJONode;
import org.codehaus.jackson.node.TextNode;
import org.dgfoundation.amp.Util;
import org.dgfoundation.amp.algo.ValueWrapper;
import org.dgfoundation.amp.ar.ArConstants;
import org.dgfoundation.amp.ar.ColumnConstants;
import org.dgfoundation.amp.ar.MeasureConstants;
import org.dgfoundation.amp.ar.viewfetcher.RsInfo;
import org.dgfoundation.amp.ar.viewfetcher.SQLUtils;
import org.dgfoundation.amp.newreports.AmountsUnits;
import org.dgfoundation.amp.newreports.AmpReportFilters;
import org.dgfoundation.amp.newreports.FilterRule;
import org.dgfoundation.amp.newreports.GeneratedReport;
import org.dgfoundation.amp.newreports.IdentifiedReportCell;
import org.dgfoundation.amp.newreports.ReportArea;
import org.dgfoundation.amp.newreports.ReportAreaImpl;
import org.dgfoundation.amp.newreports.ReportCell;
import org.dgfoundation.amp.newreports.ReportColumn;
import org.dgfoundation.amp.newreports.ReportMeasure;
import org.dgfoundation.amp.newreports.ReportOutputColumn;
import org.dgfoundation.amp.newreports.ReportSettingsImpl;
import org.dgfoundation.amp.newreports.ReportSpecification;
import org.dgfoundation.amp.newreports.ReportSpecificationImpl;
import org.dgfoundation.amp.nireports.amp.OutputSettings;
import org.digijava.kernel.ampapi.endpoints.common.EPConstants;
import org.digijava.kernel.ampapi.endpoints.common.EndpointUtils;
import org.digijava.kernel.ampapi.endpoints.settings.SettingsConstants;
import org.digijava.kernel.ampapi.endpoints.settings.SettingsUtils;
import org.digijava.kernel.ampapi.endpoints.util.FilterUtils;
import org.digijava.kernel.ampapi.endpoints.util.GisConstants;
import org.digijava.kernel.ampapi.endpoints.util.JsonBean;
import org.digijava.kernel.ampapi.exception.AmpApiException;
import org.digijava.kernel.ampapi.helpers.geojson.FeatureGeoJSON;
import org.digijava.kernel.ampapi.helpers.geojson.GeoJSON;
import org.digijava.kernel.ampapi.helpers.geojson.LineStringGeoJSON;
import org.digijava.kernel.ampapi.helpers.geojson.PointGeoJSON;
import org.digijava.kernel.ampapi.helpers.geojson.PolygonGeoJSON;
import org.digijava.kernel.ampapi.helpers.geojson.objects.ClusteredPoints;
import org.digijava.kernel.ampapi.mondrian.util.MoConstants;
import org.digijava.kernel.persistence.PersistenceManager;
import org.digijava.module.aim.dbentity.AmpActivityVersion;
import org.digijava.module.aim.dbentity.AmpCategoryValueLocations;
import org.digijava.module.aim.dbentity.AmpFiscalCalendar;
import org.digijava.module.aim.dbentity.AmpStructure;
import org.digijava.module.aim.dbentity.AmpStructureCoordinate;
import org.digijava.module.aim.helper.GlobalSettingsConstants;
import org.digijava.module.aim.util.DynLocationManagerUtil;
import org.digijava.module.aim.util.FeaturesUtil;
import org.digijava.module.categorymanager.util.CategoryConstants;
import org.digijava.module.categorymanager.util.CategoryConstants.HardCodedCategoryValue;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.jdbc.Work;

/**
 * 
 * @author Diego Dimunzio
 *
 */
public class LocationService {
    protected static Logger logger = Logger.getLogger(LocationService.class);
    
    private ReportSpecification spec = null;
    
    public ReportSpecification getLastReportSpec() {
        return spec;
    }
    
    /**
     * Get totals (actual commitments/ actual disbursements) by administrative level
     * @param admlevel
     * @param config json configuration
     * @return
     */
    public JsonBean getTotals(String admlevel, JsonBean config, AmountsUnits amountUnits) {
        JsonBean retlist = new JsonBean();
        HardCodedCategoryValue admLevelCV = GisConstants.ADM_TO_IMPL_CATEGORY_VALUE.getOrDefault(admlevel,
                CategoryConstants.IMPLEMENTATION_LOCATION_REGION);
        admlevel = admLevelCV.getValueKey();
        
        String numberformat = FeaturesUtil.getGlobalSettingValue(GlobalSettingsConstants.NUMBER_FORMAT);
        ReportSpecificationImpl spec = new ReportSpecificationImpl("LocationsTotals", ArConstants.DONOR_TYPE);
        this.spec = spec;
        spec.addColumn(new ReportColumn(admlevel));
        spec.getHierarchies().addAll(spec.getColumns());
        // also configures the measure(s) from funding type settings request
        SettingsUtils.applyExtendedSettings(spec, config);
        ReportSettingsImpl mrs = (ReportSettingsImpl) spec.getSettings();
        // THIS IS OLD, just allowing now to not reset the units when used by other services 
        if (amountUnits != null)
            mrs.setUnitsOption(AmountsUnits.AMOUNTS_OPTION_UNITS);
        
        AmpReportFilters filterRules = new AmpReportFilters((AmpFiscalCalendar) spec.getSettings().getCalendar());
        
        if(config != null){
            Map<String, Object> filters = (Map<String, Object>) config.get(EPConstants.FILTERS);
            if (filters != null) {
                filterRules = FilterUtils.getFilterRules(filters, null, filterRules);
            }

            GisUtils.configurePerformanceFilter(config, filterRules);
        }
        Map<Long, String> admLevelToGeoCode;
        if (admlevel.equals(ColumnConstants.COUNTRY)) {
            // If the admin level is country we filter only to show projects at
            // the country of the current installation
            final ValueWrapper<String> countryId = new ValueWrapper<String>("");
            final ValueWrapper<String> countryGeoCode = new ValueWrapper<String>("");
            final ValueWrapper<String> countryName = new ValueWrapper<String>("");
            PersistenceManager.getSession().doWork(new Work() {
                public void execute(Connection conn) throws SQLException {
                    String countryIdQuery = "select acvl.id,acvl.location_name from amp_category_value_location acvl,amp_global_settings gs "
                            + " where acvl.iso=gs.settingsvalue  " + " and gs.settingsname ='Default Country'";
                    RsInfo rsi = SQLUtils.rawRunQuery(conn, countryIdQuery, null);
                    if (rsi.rs.next()) {
                        countryId.value = rsi.rs.getString(1);
                        countryName.value = rsi.rs.getString(2);
                    }
                    rsi.close();
                }
            });

            filterRules.addFilterRule(new ReportColumn(ColumnConstants.COUNTRY), new FilterRule(countryId.value, true));
            
        } 
        
        admLevelToGeoCode = getAdmLevelGeoCodeMap(admlevel, admLevelCV);
        spec.setFilters(filterRules);
        
        String currcode = FilterUtils.getSettingbyName(config, SettingsConstants.CURRENCY_ID);
        retlist.set("currency", currcode);
        retlist.set("numberformat", numberformat);
        GeneratedReport report = EndpointUtils.runReport(spec);
        List<JsonBean> values = new ArrayList<JsonBean>();
        
        if (report != null && report.reportContents != null && report.reportContents.getChildren() != null) {
            // find the admID (geocode) for each implementation location name
            
            for (ReportArea reportArea : report.reportContents.getChildren()) {
                JsonBean item = new JsonBean();
                Iterator<ReportCell> iter = reportArea.getContents().values().iterator();
                BigDecimal value=(BigDecimal) iter.next().value;                
                ReportCell reportcell = (ReportCell) iter.next();
                //we fetch the entity id so we can Univocally search the GeoId
                Long entityId=((IdentifiedReportCell)reportcell).entityId;
                String admid = admLevelToGeoCode.get(entityId);
                item.set("admID", admid);
                item.set("amount", value);
                if (admid!=null){
                    values.add(item);
                }
            }
        }
        retlist.set("values", values);
        return retlist;
    }
    
    /**
     * Provides admLevel name to geo code map
     * @param admLevel
     * @param admLevelCV
     * @return
     */
    public Map<Long, String> getAdmLevelGeoCodeMap(String admLevel, HardCodedCategoryValue admLevelCV) {
        Set<AmpCategoryValueLocations> acvlData = DynLocationManagerUtil.getLocationsByLayer(admLevelCV);
        Map<Long, String> levelToGeoCodeMap = new HashMap<Long, String>();
        if (acvlData != null) {
            for (AmpCategoryValueLocations acvl : acvlData) {
                levelToGeoCodeMap.put(acvl.getId(), acvl.getGeoCode());
            }
        }
        return levelToGeoCodeMap;
    }
    
    public static List<ClusteredPoints> getClusteredPoints(JsonBean config) throws AmpApiException {
        String adminLevel = "";
        final List<ClusteredPoints> l = new ArrayList<ClusteredPoints>();

        if (config != null) {
            Map filters = (Map) config.get(EPConstants.FILTERS);
            if (filters != null && filters.get("adminLevel") != null) {
                adminLevel = filters.get("adminLevel").toString();
            }
        }
        
        final String usedAdminLevel = adminLevel;
        Set<Long> activitiesId = getActivitiesForFiltering(config, adminLevel);
        
        final Double countryLatitude=FeaturesUtil.getGlobalSettingDouble(GlobalSettingsConstants.COUNTRY_LATITUDE);
        final Double countryLongitude=FeaturesUtil.getGlobalSettingDouble(GlobalSettingsConstants.COUNTRY_LONGITUDE);
        final ValueWrapper<String> qry = new ValueWrapper<String>(null);
        if (adminLevel.equals("Country")) {
            qry.value = " SELECT al.amp_activity_id, acvl.id root_location_id,acvl.location_name "
                    + "root_location_description,acvl.gs_lat, acvl.gs_long "
                    + " FROM amp_activity_location al   "
                    + " join amp_location loc on al.amp_location_id = loc.amp_location_id  "
                    + " join amp_category_value_location acvl on loc.location_id = acvl.id  "
                    + " join amp_category_value amcv on acvl.parent_category_value =amcv.id "
                    + " where amcv.category_value ='Country'"
                    + " and (acvl.deleted is null or acvl.deleted = false) "
                    + " and al.amp_activity_id in(" + Util.toCSStringForIN(activitiesId) + " ) "
                    + " and location_name=(select country_name "
                    + " from DG_COUNTRIES where iso='" + FeaturesUtil
                    .getGlobalSettingValue(GlobalSettingsConstants.DEFAULT_COUNTRY) + "')";

            
        }else{
        qry.value = " WITH RECURSIVE rt_amp_category_value_location(id, parent_id, gs_lat, gs_long, acvl_parent_category_value, level, root_location_id,root_location_description) AS ( "
                + " select acvl.id, acvl.parent_location, acvl.gs_lat, acvl.gs_long, acvl.parent_category_value, 1, acvl.id,acvl.location_name  "
                + " from amp_category_value_location acvl  "
                + " join amp_category_value amcv on acvl.parent_category_value =amcv.id  "
                + " where amcv.category_value ='"
                + adminLevel
                + "'  "
                + " and acvl.gs_lat is not null and acvl.gs_long is not null  "
                + " and (acvl.deleted is null or acvl.deleted = false) "
                + " UNION ALL  "
                + " SELECT acvl.id, acvl.parent_location, rt.gs_lat, rt.gs_long, acvl.parent_category_value, rt.LEVEL + 1, rt.root_location_id, rt.root_location_description  "
                + " FROM rt_amp_category_value_location rt, amp_category_value_location acvl  "
                + " WHERE acvl.parent_location =rt.id  "
                + " )  "
                + " SELECT distinct al.amp_activity_id, acvl.root_location_id, acvl.root_location_description, acvl.gs_lat, acvl.gs_long  "
                + " FROM amp_activity_location al  "
                + " join amp_location loc on al.amp_location_id = loc.amp_location_id  "
                + " join rt_amp_category_value_location acvl on loc.location_id = acvl.id  "
                + " where al.amp_activity_id in(" + Util.toCSStringForIN(activitiesId) + " ) "
                + " order by acvl.root_location_id,al.amp_activity_id";
        }

        try {
            PersistenceManager.getSession().doWork(new Work() {

                @Override
                public void execute(Connection connection) throws SQLException {
                    try(RsInfo rsi = SQLUtils.rawRunQuery(connection, qry.value, null)) {
                        ResultSet rs = rsi.rs;
                        ClusteredPoints cp = null;
                        Long rootLocationId = 0L;
                        while (rs.next()) {
                            if (!rootLocationId.equals(rs.getLong("root_location_id"))) {
                                if (cp != null) {
                                    l.add(cp);
                                }
                                rootLocationId = rs.getLong("root_location_id");
                                cp = new ClusteredPoints();
                                cp.setAdmId(rootLocationId);
                                cp.setAdmin(rs.getString("root_location_description"));
                                if (usedAdminLevel.equals("Country")){
                                    cp.setLat(countryLatitude.toString());
                                    cp.setLon(countryLongitude.toString());                         
                                }else{
                                    cp.setLat(rs.getString("gs_lat"));
                                    cp.setLon(rs.getString("gs_long"));
                                }
                            }
                            cp.getActivityids().add(rs.getLong("amp_activity_id"));
                        }
                        if (cp != null) {
                            l.add(cp);
                        }
                    }
                }});
        }
        catch(HibernateException e){
            throw new RuntimeException(e);
        }
    
        return l;
    }
    private static Set<Long> getActivitiesForFiltering(JsonBean config, String adminLevel)
            throws AmpApiException {
        Set<Long> activitiesId = new HashSet<Long>();
         
        ReportSpecificationImpl spec = new ReportSpecificationImpl("ActivityIdsForCluster", ArConstants.DONOR_TYPE);

        spec.addColumn(new ReportColumn(ColumnConstants.AMP_ID));
        // AMP-20903 - In order to not have inconsistency with data used in gis map, DONOR_ID was added 
        spec.addColumn(new ReportColumn(ColumnConstants.DONOR_AGENCY));

        OutputSettings outSettings = new OutputSettings(new HashSet<String>() {{
            add(ColumnConstants.AMP_ID);
        }});
        
        SettingsUtils.configureMeasures(spec, config);

        ReportColumn implementationLevelColumn = null;
        if (adminLevel != null) {
            switch (adminLevel) {
                case ColumnConstants.COUNTRY:
                    implementationLevelColumn = new ReportColumn(MoConstants.H_COUNTRIES);
                    break;
                case ColumnConstants.REGION:
                    implementationLevelColumn = new ReportColumn(MoConstants.H_REGIONS);
                    break;
                case ColumnConstants.ZONE:
                    implementationLevelColumn = new ReportColumn(MoConstants.H_ZONES);
                    break;
                case ColumnConstants.DISTRICT:
                    implementationLevelColumn = new ReportColumn(MoConstants.H_DISTRICTS);
                    break;
            }
        }

        if(implementationLevelColumn != null){
            spec.addColumn(implementationLevelColumn);
            Set<ReportColumn>implementationLevelHierarchy=new HashSet<ReportColumn>();
            implementationLevelHierarchy.add(implementationLevelColumn);
            spec.setHierarchies(implementationLevelHierarchy);
        }
        spec.setDisplayEmptyFundingRows(true);
        
        SettingsUtils.applyExtendedSettings(spec, config);
        ReportSettingsImpl mrs = (ReportSettingsImpl) spec.getSettings();
        mrs.setUnitsOption(AmountsUnits.AMOUNTS_OPTION_UNITS);

        if (config != null) {
            Map<String, Object> filterMap = (Map<String, Object>) config.get(EPConstants.FILTERS);
            AmpReportFilters filterRules = FilterUtils.getFilters(filterMap, new AmpReportFilters(mrs.getCalendar()));

            GisUtils.configurePerformanceFilter(config, filterRules);

            if (filterRules != null) {
                spec.setFilters(filterRules);
            }
        }

        GeneratedReport report = EndpointUtils.runReport(spec, ReportAreaImpl.class, outSettings);
        
        List<ReportArea> ll = null;
        ll = report.reportContents.getChildren();
        if (ll != null) {
            for (ReportArea reportArea : ll) {
                if (implementationLevelColumn != null) {
                    List<ReportArea> childrenHierarchy = reportArea.getChildren();

                    for (ReportArea reportAreachi : childrenHierarchy) {
                        Map<ReportOutputColumn, ReportCell> row = reportAreachi.getContents();
                        Set<ReportOutputColumn> col = row.keySet();
                        for (ReportOutputColumn reportOutputColumn : col) {
                            if (reportOutputColumn.originalColumnName.equals(ColumnConstants.AMP_ID)) {
                                activitiesId.add(((IdentifiedReportCell) row.get(reportOutputColumn)).entityId);
                            }
                        }
                    }

                } else {
                    // we don't have hierarchy
                    Map<ReportOutputColumn, ReportCell> row = reportArea.getContents();
                    Set<ReportOutputColumn> col = row.keySet();
                    for (ReportOutputColumn reportOutputColumn : col) {
                        if (reportOutputColumn.originalColumnName.equals(ColumnConstants.AMP_ID)) {
                            activitiesId.add(((IdentifiedReportCell) row.get(reportOutputColumn)).entityId);
                        }
                    }
                }
            }
        }
        return activitiesId;
    }

    @SuppressWarnings("unchecked")
    public static List<AmpStructure> getStructures(JsonBean config) throws AmpApiException{
        List<AmpStructure> al = null;
        Set<Long> activitiesId = getActivitiesForFiltering( config,null);
        String queryString = "select s from " + AmpStructure.class.getName() + " s inner join s.activities a where"
                    + " a.ampActivityId in (" + Util.toCSStringForIN(activitiesId) + " )";
        Query q = PersistenceManager.getSession().createQuery(queryString);
        al = q.list();
        return al;

    }

    public static FeatureGeoJSON buildFeatureGeoJSON(AmpStructure structure) {
        FeatureGeoJSON fgj = new FeatureGeoJSON();
        try {
            fgj.geometry = getGeometry(structure);
            fgj.id = structure.getAmpStructureId().toString();
            fgj.properties.put("title", new TextNode(structure.getTitle()));
            if (structure.getDescription() != null && !structure.getDescription().trim().equals("")) {
                fgj.properties.put("description", new TextNode(structure.getDescription()));
            }
            Set<AmpActivityVersion> av = structure.getActivities();
            List<Long> actIds = new ArrayList<Long>();

            for (AmpActivityVersion ampActivity : av) {
                actIds.add(ampActivity.getAmpActivityId());
            }

            fgj.properties.put("activity", new POJONode(actIds));
        } catch (NumberFormatException e) {
            logger.warn("Couldn't get parse latitude/longitude for structure with latitude: "
                    + structure.getLatitude() + " longitude: " + structure.getLongitude() + " and title: "
                    + structure.getTitle());
        }

        return fgj;

    }

    private static GeoJSON getGeometry(AmpStructure structure) {
        String shape = StringUtils.isEmpty(structure.getShape()) ? GisConstants.GIS_STRUCTURE_POINT
                : structure.getShape();
        switch (shape) {
        case GisConstants.GIS_STRUCTURE_POLYGON:
            return buildPolygon(structure);
        case GisConstants.GIS_STRUCTURE_POLYLINE:
            return buildPolyLine(structure);
        case GisConstants.GIS_STRUCTURE_POINT:
            return buildPoint(structure);
        default:
            return null;
        }
    }

    private static PointGeoJSON buildPoint(AmpStructure structure) {
        PointGeoJSON pg = new PointGeoJSON();
        pg.coordinates.add(parseDouble(structure.getLatitude()));
        pg.coordinates.add(parseDouble(structure.getLongitude()));
        return pg;
    }

    private static LineStringGeoJSON buildPolyLine(AmpStructure structure) {
        LineStringGeoJSON line = new LineStringGeoJSON();
        line.coordinates = new ArrayList<>();
        if (structure.getCoordinates() != null) {
          for (AmpStructureCoordinate coord : structure.getCoordinates()) {
              List<Double> lngLat =  new ArrayList<>();
              lngLat.add(parseDouble(coord.getLatitude()));
              lngLat.add(parseDouble(coord.getLongitude()));
              line.coordinates.add(lngLat);
          }
        }

        return line;
    }

    private static PolygonGeoJSON buildPolygon(AmpStructure structure) {
        PolygonGeoJSON polygon = new PolygonGeoJSON();
        polygon.coordinates = new ArrayList<>();
        if (structure.getCoordinates() != null) {
            List<List<Double>> ring = new ArrayList<>();
            for (AmpStructureCoordinate coord : structure.getCoordinates()) {
                List<Double> lngLat = new ArrayList<>();
                lngLat.add(parseDouble(coord.getLatitude()));
                lngLat.add(parseDouble(coord.getLongitude()));
                ring.add(lngLat);
            }

            polygon.coordinates.add(ring);
        }

        return polygon;
    }

    private static Double parseDouble(String value) {
       return Double.parseDouble(value == null ? "0" : value);
    }

}
