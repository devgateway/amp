package org.digijava.kernel.ampapi.endpoints.gis.services;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.dgfoundation.amp.ar.ArConstants;
import org.dgfoundation.amp.ar.ColumnConstants;
import org.dgfoundation.amp.ar.viewfetcher.RsInfo;
import org.dgfoundation.amp.ar.viewfetcher.SQLUtils;
import org.dgfoundation.amp.newreports.GeneratedReport;
import org.dgfoundation.amp.newreports.ReportArea;
import org.dgfoundation.amp.newreports.ReportCell;
import org.dgfoundation.amp.newreports.ReportColumn;
import org.dgfoundation.amp.newreports.ReportOutputColumn;
import org.dgfoundation.amp.newreports.ReportSpecificationImpl;
import org.digijava.kernel.ampapi.endpoints.common.EndpointUtils;
import org.digijava.kernel.ampapi.endpoints.dto.Activity;
import org.digijava.kernel.persistence.PersistenceManager;
import org.hibernate.jdbc.ReturningWork;
import org.hibernate.jdbc.Work;

/**
 * Exports a list of tuples {activity, structure} 
 * only for activities that contain structures
 * @author acartaleanu
 *
 */
public class ActivityStructuresExporter extends ActivityExporter {

    public ActivityStructuresExporter(Map<String, Object> filters) {
        super(filters);
    }

    @Override
    protected List<String> getOriginalNames() {
        return Arrays.asList("Time Stamp", "Activity Id", "Project Title", "Project Site Description", 
                "Project Site", "Latitude", "Longitude", "Sectors", "Donors", "Total Project Commitments", 
                "Total Project Disbursements");
    }

    @Override
    protected ReportSpecificationImpl generateCustomSpec() {
        ReportSpecificationImpl res = new ReportSpecificationImpl("MapExport", ArConstants.DONOR_TYPE);
        res.addColumn(new ReportColumn(ColumnConstants.ACTIVITY_ID));
        res.addColumn(new ReportColumn(ColumnConstants.AMP_ID));
        return res;
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
            row.add(a.getDescription());
            row.add(a.getStructureName());
            row.add(a.getLatitude());
            row.add(a.getLongitude());
            row.add(a.getPrimarySector());
            row.add(a.getDonorAgency());
            row.add(a.getTotalCommitments());
            row.add(a.getTotalDisbursments());
            rows.add(row);
        }
        return rows;
    }       

    
    /**
     * Extracts a mapping of -> {geocode, latitude, longitude, location name} from DB, 
     * filtered by a list of geocodes (already joined via toCSString())
     * @param geoCodesJoined
     * @return
     */
    private List<StructuresDataSkeleton> getStructuresInfoFromDb(Set<Long> activityIds) {
//      return PersistenceManager.getSession().doReturningWork( conn -> {
        return PersistenceManager.getSession().doReturningWork(new ReturningWork<List<StructuresDataSkeleton>>() {
            public List<StructuresDataSkeleton> execute(Connection conn) throws SQLException {
                List <StructuresDataSkeleton> res = new ArrayList<>();
                String query = "select amp_activity_id, title, description, latitude, longitude"
                        + " from amp_structure"
                        + " where amp_activity_id in(" + org.dgfoundation.amp.Util.toCSString(activityIds) + ")";
                try(RsInfo rsi = SQLUtils.rawRunQuery(conn, query, null)) {
                    ResultSet rs = rsi.rs;
                    while (rs.next()) {
                        res.add(new StructuresDataSkeleton(rs.getLong(1), rs.getString(2), rs.getString(3), rs.getString(4), rs.getString(5)));
                    }
                }
                return res;
            }
        });
    }
    
    /**
     * Report is flat for this implementation, so no need for recursive methods 
     */
    @Override
    protected List<Activity> generateActivities() {
        GeneratedReport report = EndpointUtils.runReport(generateSpec());
        Map<Long, Activity> activitiesMap = new HashMap<>();
        if (report != null && report.reportContents != null && report.reportContents.getChildren() != null) {
            for (ReportArea reportArea : report.reportContents.getChildren()) {
                Activity a = new Activity();
                Map<ReportOutputColumn, ReportCell> row = reportArea.getContents();
                for (Map.Entry<ReportOutputColumn, ReportCell> entry : reportArea.getContents().entrySet()) {
                    String columnValue;
                    if (entry.getKey().originalColumnName.equals(ColumnConstants.ACTIVITY_ID)) {
                        columnValue = entry.getValue().value.toString();
                    }
                    else {
                        columnValue = entry.getValue().displayedValue.toString();
                    }
                    
                    setActivityField(entry.getKey().originalColumnName, columnValue, a);
                }
                activitiesMap.put(a.getId(), a);
            }
        }
        List<StructuresDataSkeleton> structs = getStructuresInfoFromDb(activitiesMap.keySet());
        List<Activity> res = new ArrayList<>();
        for (StructuresDataSkeleton sk : structs) {
            Activity a = new Activity(activitiesMap.get(sk.act_id));
            a.setDescription(sk.description);
            a.setLatitude(sk.latitude);
            a.setLongitude(sk.longitude);
            a.setStructureName(sk.title);
            res.add(a);
        }
        return new ArrayList<>(res);
    }
}
