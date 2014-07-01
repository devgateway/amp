package org.digijava.kernel.ampapi.postgis.util;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.log4j.Logger;
import org.dgfoundation.amp.ar.viewfetcher.SQLUtils;
import org.digijava.kernel.ampapi.helpers.geojson.objects.ClusteredPoints;
import org.digijava.kernel.persistence.PersistenceManager;

public class QueryUtil {
    protected static Logger logger = Logger.getLogger(QueryUtil.class);
    public static List<ClusteredPoints>getClusteredPoints(String adminLevel){
        List<ClusteredPoints> l= new ArrayList<ClusteredPoints>();
        ClusteredPoints cp=null;
        String qry=" WITH RECURSIVE rt_amp_category_value_location(id, parent_id, gs_lat, gs_long, acvl_parent_category_value, level, root_location_id,root_location_description) AS ( " +
                    " select acvl.id, acvl.parent_location, acvl.gs_lat, acvl.gs_long, acvl.parent_category_value, 1, acvl.id,acvl.location_name  " +
                    " from amp_category_value_location acvl  " +
                    " join amp_category_value amcv on acvl.parent_category_value =amcv.id  " +
                    " where amcv.category_value ='"+adminLevel+ "'  " +
                    " and acvl.gs_lat is not null and acvl.gs_long is not null  " +
                    " UNION ALL  " +
                    " SELECT acvl.id, acvl.parent_location, rt.gs_lat, rt.gs_long, acvl.parent_category_value, rt.LEVEL + 1, rt.root_location_id, rt.root_location_description  " +
                    " FROM rt_amp_category_value_location rt, amp_category_value_location acvl  " +
                    " WHERE acvl.parent_location =rt.id  " +
                    " )  " +
                    " SELECT al.amp_activity_id, acvl.root_location_id, acvl.root_location_description, acvl.gs_lat, acvl.gs_long  " +
                    " FROM amp_activity_location al  " +
                    " join amp_location loc on al.amp_location_id = loc.amp_location_id  " + 
                    " join rt_amp_category_value_location acvl on loc.location_id = acvl.id  " + 
                    " order by acvl.root_location_id,al.amp_activity_id"; 
        Connection conn=null;
        try {
            conn=PersistenceManager.getJdbcConnection();
            java.sql.ResultSet rs=
            SQLUtils.rawRunQuery(PersistenceManager.getJdbcConnection(), qry, null);
            Long rootLocationId=0L;
            while(rs.next()){
                if(!rootLocationId.equals(rs.getLong("root_location_id"))){
                    if(cp!=null){
                        l.add(cp);
                    }
                    rootLocationId=rs.getLong("root_location_id");
                    cp=new ClusteredPoints();
                    cp.setAdmin(rs.getString("root_location_description"));
                    cp.setLat(rs.getString("gs_lat"));
                    cp.setLon(rs.getString("gs_long"));
                }
                cp.getActivityids().add(rs.getLong("amp_activity_id"));
            }
            if(cp!=null){
                l.add(cp);
            }
            rs.close();
            
        } catch (SQLException e) {
            
            e.printStackTrace();
        }finally{
            try {
                conn.close();
            } catch (SQLException e) {
                logger.debug("cannot close connection",e);
            }
        }
        return l;
    }
    public static List<String>getAdminLevels(){
        return new ArrayList<String>(
                Arrays.asList("Country","Region","Zone","District"));
    }
}
