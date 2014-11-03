package org.digijava.kernel.ampapi.endpoints.dto;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.codehaus.jackson.map.annotate.JsonSerialize;
import org.digijava.kernel.ampapi.endpoints.util.JsonBean;

/**
 * Class that holds the activitiy data to be serialized as a JsonObject we ignore an object if its null
 * 
 * @author jdeanquin
 *
 */
/**
 * @author Fernando Ferreyra
 *
 */
@JsonSerialize(include = JsonSerialize.Inclusion.NON_NULL)
public class Activity {
	private Long id;
	private String name;
	private String ampId;
	private String description;
	private String ampUrl;
	private String donorAgency;
	private String implementationLevel;
	private String totalCommitments;
	private String totalDisbursments;
	private String totalCommitmentsForActivity;
	private String totalDisbursmentsForActivity;
	private String geoCode;
	private String locationName;
	private String structureName;
	private String latitude;
	private String longitude;
	private String primarySector;

	private JsonBean matchesFilters;

	public Activity() {

	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getAmpUrl() {
		return ampUrl;
	}

	public void setAmpUrl(String ampUrl) {
		this.ampUrl = ampUrl;
	}

	public String getTotalCommitments() {
		return totalCommitments;
	}

	public void setTotalCommitments(String totalCommitments) {
		this.totalCommitments = totalCommitments;
	}

	public String getTotalDisbursments() {
		return totalDisbursments;
	}

	public void setTotalDisbursments(String totalDisbursments) {
		this.totalDisbursments = totalDisbursments;
	}

	public String getTotalCommitmentsForActivity() {
		return totalCommitmentsForActivity;
	}

	public void setTotalCommitmentsForActivity(
			String totalCommitmentsForActivity) {
		this.totalCommitmentsForActivity = totalCommitmentsForActivity;
	}

	public String getTotalDisbursmentsForActivity() {
		return totalDisbursmentsForActivity;
	}

	public void setTotalDisbursmentsForActivity(
			String totalDisbursmentsForActivity) {
		this.totalDisbursmentsForActivity = totalDisbursmentsForActivity;
	}

	public JsonBean getMatchesFilters() {
		return matchesFilters;
	}

	public void setMatchesFilters(JsonBean matchesFilters) {
		this.matchesFilters = matchesFilters;
	}

	public String getAmpId() {
		return ampId;
	}

	public void setAmpId(String ampId) {
		this.ampId = ampId;
	}

	public String getStructureName() {
		return structureName;
	}

	public void setStructureName(String structureName) {
		this.structureName = structureName;
	}

	public String getLatitude() {
		return latitude;
	}

	public void setLatitude(String latitude) {
		this.latitude = latitude;
	}

	public String getLongitude() {
		return longitude;
	}

	public void setLongitude(String longitude) {
		this.longitude = longitude;
	}

	public String getDonorAgency() {
		return donorAgency;
	}

	public void setDonorAgency(String donorAgency) {
		this.donorAgency = donorAgency;
	}

	public String getImplementationLevel() {
		return implementationLevel;
	}

	public void setImplementationLevel(String implementationLevel) {
		this.implementationLevel = implementationLevel;
	}

	public String getPrimarySector() {
		return primarySector;
	}

	public void setPrimarySector(String primarySector) {
		this.primarySector = primarySector;
	}

	public String getGeoCode() {
		return geoCode;
	}

	public void setGeoCode(String geoCode) {
		this.geoCode = geoCode;
	}

	public String getLocationName() {
		return locationName;
	}

	public void setLocationName(String locationName) {
		this.locationName = locationName;
	}

}
