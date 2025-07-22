package org.digijava.kernel.ampapi.endpoints.gis.services;

import org.dgfoundation.amp.ar.ArConstants;
import org.dgfoundation.amp.ar.ColumnConstants;
import org.dgfoundation.amp.ar.viewfetcher.RsInfo;
import org.dgfoundation.amp.ar.viewfetcher.SQLUtils;
import org.dgfoundation.amp.newreports.*;
import org.digijava.kernel.ampapi.endpoints.common.EndpointUtils;
import org.digijava.kernel.ampapi.endpoints.dto.Activity;
import org.digijava.kernel.persistence.PersistenceManager;
import org.hibernate.jdbc.ReturningWork;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

public class ActivityLocationExporter extends ActivityExporter {

    @Override
    protected List<String> getOriginalNames() {
        return Arrays.asList("Time Stamp", "Activity Id", "Project Title", "Type", "Location Name", 
                "Latitude", "Longitude", "GeoId", "Sectors", "Donors", "Total Project Commitments", 
                "Total Project Disbursements");
    }
    
    public ActivityLocationExporter(Map<String, Object> filters) {
        super(filters);
    }
    
    private Set<ReportColumn> bundleColumns(List<String> columnNames) {
        Set<ReportColumn> cols = new LinkedHashSet<>();
        for (String name : columnNames)
            cols.add(new ReportColumn(name));
        return cols;
    }
    
    @Override 
    protected ReportSpecificationImpl generateCustomSpec() {
        ReportSpecificationImpl res = new ReportSpecificationImpl("MapExport", ArConstants.DONOR_TYPE);
        Set<ReportColumn> cols = bundleColumns(Arrays.asList(ColumnConstants.AMP_ID, ColumnConstants.GEOCODE, ColumnConstants.IMPLEMENTATION_LEVEL));
        for (ReportColumn col : cols)
            res.addColumn(col);
        res.setHierarchies(new HashSet<>(Arrays.asList(new ReportColumn(ColumnConstants.GEOCODE))));
        return res;
    };
    
    
    /*because Mondrian, the first row of a hierarchy will contain the hierarchy value;
     * all subsequent ones of the same group will be null.
     * Since the hierarchy we're using is by GeoID (don't need others), 
     * geoId is the only one affected by it.
     * example: 
     * ampId: [872261299791], geocode: [924591], impl. level: [Malawi Subnational], name: [Integrated water supply and sanitation for central and northern region] 
       ampId: [872261239867], geocode: [], impl. level: [], name: [Norwegian Church Aid-health training]
       adding activity: ampId: [872261239867], geocode: [], impl. level: [], name: [Norwegian Church Aid-health training]
       
     * all three above have the same geocode
        
        
     * */
    /**
     * logic is: extract info from this row and pass it down
     * it will get overwritten if it's meaningful in children
     * if this is the bottom layer, add to list
     * 
     * 
     * @param reportArea
     * @param parentActivity
     * @return false if this set of siblings should not be parsed
     */
    protected boolean extractActivitiesFromReport(ReportArea reportArea, Activity parentActivity){

        Activity a = new Activity(parentActivity);
        boolean goodAct = true;
        for (Map.Entry<ReportOutputColumn, ReportCell> entry : reportArea.getContents().entrySet()) {
            String columnValue = entry.getValue().displayedValue.toString();
            if (setActivityField(entry.getKey().originalColumnName, columnValue, a) == null) {
                //this is a hierarchy of undefined geocodes; this is a bad line, we'll drop it
                //we also need to notify the system that all siblings are also broken
                goodAct = false;
                break;
            }
        }
        if (reportArea.getChildren() == null) {
            //do for oneself
            if (goodAct) {
                activities.add(a);
            } else {
                return false;
            }
        } else {
            //do for children
            boolean goAhead = true;
            for (ReportArea child : reportArea.getChildren()) {
                goAhead = extractActivitiesFromReport(child, a);
                if (!goAhead)
                    break;//this is a spoiled batch of children
            }
        }
        return true;
    }
    
    /**
     * Extracts a mapping of geoCode -> {geocode, latitude, longitude, location name} from DB, 
     * filtered by a list of geocodes (already joined via toCSString())
     * @param geoCodesJoined
     * @return
     */
    private Map<String, GeoDataSkeleton> getGeoInfoFromDb(final String geoCodesJoined) {
        return PersistenceManager.getSession().doReturningWork(new ReturningWork<Map<String, GeoDataSkeleton>>() {
            public Map<String, GeoDataSkeleton> execute(Connection conn) throws SQLException {
                Map<String, GeoDataSkeleton> res = new HashMap<>();
                String query = "select geo_code,location_name,gs_lat,gs_long from amp_category_value_location "
                    + String.format("where  geo_code in (%s)", geoCodesJoined);
                try(RsInfo rsi = SQLUtils.rawRunQuery(conn, query, null)) {
                    ResultSet rs = rsi.rs;
                    while (rs.next()) {
                        String geoCode = rs.getString("geo_code");
                        GeoDataSkeleton gds = new GeoDataSkeleton(geoCode, rs.getString("location_name"), 
                                rs.getString("gs_lat"), rs.getString("gs_long"));
                        res.put(geoCode, gds);
                    }
                }
                return res;
            }
        });
    }
    
    @Override
    protected List<Activity> generateActivities() {
        ReportSpecificationImpl spec = generateSpec();
        GeneratedReport report = EndpointUtils.runReport(spec);
        
        if (report != null && report.reportContents != null
                && report.reportContents.getChildren() != null) {
            extractActivitiesFromReport(report.reportContents, new Activity());
            //postprocess: expand geocodes into latitude/longitude
            //has to be done outside of the recursive part since it implies a DB request
            if (getGeoCodes().size() > 0) {
                String preparedGeoCodes = org.dgfoundation.amp.Util.toCSString(getGeoCodes());
                Map<String, GeoDataSkeleton> geoDataExpanded = getGeoInfoFromDb(preparedGeoCodes);
                for (Activity act : activities) {
                    if (geoDataExpanded.containsKey(act.getGeoCode())) {
                        GeoDataSkeleton gds = geoDataExpanded.get(act.getGeoCode());
                        act.setLocationName(gds.locationName);
                        act.setLatitude(gds.latitude);
                        act.setLongitude(gds.longitude);
                    }
                }
            }
        }
        Collections.sort(activities, new Comparator<Activity>() {
                @Override
                public int compare(Activity a, Activity b) {
                    return a.getAmpId().compareTo(b.getAmpId());
                }
            });
        return activities;
    }

    @Override
    protected List<List<String>> generateRowValues() {
        java.util.Date date = new java.util.Date();
        List<List<String>> rows=new ArrayList<>();
        
        for (Activity a : this.activities) {
            List<String> row = new ArrayList<String>();
            row.add(date.toString());
            row.add(a.getAmpId());
            row.add(a.getName());
            row.add(a.getImplementationLevel());
            row.add(a.getLocationName());
            row.add(a.getLatitude());
            row.add(a.getLongitude());
            row.add(a.getGeoCode());
            row.add(a.getPrimarySector());
            row.add(a.getDonorAgency());
            row.add(a.getTotalCommitments());
            row.add(a.getTotalDisbursments());
            rows.add(row);
        }
        return rows;
    }
}
