package org.digijava.module.esrigis.helpers;

/**
 * Simplified location object to manage locations in JSON 
 *
 */

public class SimpleLocation {
	private String name;
	private String GeoId;
	private String commitments;
	private String disbursements;
	private String expenditures;
	private String pledges;
	private String lat;
	private String lon;
	private Boolean islocated = false;
	private String percentage;
    //This field is used only in the view to mark the location. 
    private String isdisplayed;

	public String getName() {
		return name;
	}

	public SimpleLocation() {
		super();
		// TODO Auto-generated constructor stub
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getGeoId() {
		return GeoId;
	}

	public void setGeoId(String geoId) {
		GeoId = geoId;
	}

	public String getCommitments() {
		return commitments;
	}

	public void setCommitments(String commitments) {
		this.commitments = commitments;
	}

	public String getDisbursements() {
		return disbursements;
	}

	public void setDisbursements(String disbursements) {
		this.disbursements = disbursements;
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

	public Boolean getIslocated() {
		return this.islocated;
	}

	public void setIslocated(Boolean islocated) {
		this.islocated = islocated;
	}

	
	public String getPercentage() {
		return percentage;
	}

	public void setPercentage(String percentage) {
		this.percentage = percentage;
	}

	public String getExpenditures() {
		return expenditures;
	}

	public void setExpenditures(String expenditures) {
		this.expenditures = expenditures;
	}

	public String getPledges() {
		return pledges;
	}

	public void setPledges(String pledges) {
		this.pledges = pledges;
	}

	public void setIsdisplayed(String isdisplayed) {
		this.isdisplayed = isdisplayed;
	}

	public String getIsdisplayed() {
		return isdisplayed;
	}
}
