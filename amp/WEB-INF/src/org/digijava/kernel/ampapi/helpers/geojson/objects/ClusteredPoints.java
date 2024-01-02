package org.digijava.kernel.ampapi.helpers.geojson.objects;

import java.util.ArrayList;
import java.util.List;

public class ClusteredPoints {

    private List<Long> activityids= new ArrayList<>();
    private String lat;
    private String lon;
    private String admin;
    private Long admId;
    
    public ClusteredPoints(){
        activityids= new ArrayList<Long>();
    }

    
    public List<Long> getActivityids() {
        return activityids;
    }

    public void setActivityids(List<Long> activityids) {
        this.activityids = activityids;
    }

    public String getLat() {
        return lat;
    }

    public void setLat(String lat) {
        this.lat = lat;
    }

    public String getLon() {
        return lon;
    }

    public void setLon(String lon) {
        this.lon = lon;
    }

    public String getAdmin() {
        return admin;
    }

    public void setAdmin(String admin) {
        this.admin = admin;
    }

    public Long getAdmId() {
        return admId;
    }

    public void setAdmId(Long admId) {
        this.admId = admId;
    }

    @Override
    public String toString() {
        return "ClusteredPoints{" +
                "activityids=" + activityids +
                ", lat='" + lat + '\'' +
                ", lon='" + lon + '\'' +
                ", admin='" + admin + '\'' +
                ", admId=" + admId +
                '}';
    }
}
