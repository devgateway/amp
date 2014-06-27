package org.digijava.kernel.ampapi.helpers.geojson.objects;

import java.util.List;

public class ClusteredPoints {

	private List<String> activityids;
	private String lat;
	private String lon;
	
	
	public List<String> getActivityids() {
		return activityids;
	}

	public void setActivityids(List<String> activityids) {
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
}
