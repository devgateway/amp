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
	private String lat;
	private String lon;
	private Boolean islocated = false;
	private String implementation_location;

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
		if (this.lat != null || this.lon != null) {
			this.islocated = true;
			return islocated;
		}
		return islocated;
	}

	public void setIslocated(Boolean islocated) {
		this.islocated = islocated;
	}

	public void setImplementation_location(String implementation_location) {
		this.implementation_location = implementation_location;
	}

	public String getImplementation_location() {
		return implementation_location;
	}
}
