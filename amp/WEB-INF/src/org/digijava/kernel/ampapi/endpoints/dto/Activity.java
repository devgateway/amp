package org.digijava.kernel.ampapi.endpoints.dto;

import java.util.Map;

import com.fasterxml.jackson.annotation.JsonInclude;

/**
 * Class that holds the activitiy data to be serialized as a JsonObject.
 * We ignore an object if it's null
 * 
 * @author jdeanquin
 *
 */
/**
 * @author Fernando Ferreyra
 *
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
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

    private Map<String, Object>  matchesFilters;

    public Activity() {

    }
    
    /**
     * copy constructor
     * @param o The other activity
     */
    public Activity(Activity o) {
        this.id = o.id;
        this.name = o.name;
        this.ampId = o.ampId;
        this.description = o.description;
        this.ampUrl = o.ampUrl;
        this.donorAgency = o.donorAgency;
        this.implementationLevel = o.implementationLevel;
        this.totalCommitments = o.totalCommitments;
        this.totalDisbursments = o.totalDisbursments;
        this.totalCommitmentsForActivity = o.totalCommitmentsForActivity;
        this.totalDisbursmentsForActivity = o.totalDisbursmentsForActivity;
        this.geoCode = o.geoCode;
        this.locationName = o.locationName;
        this.structureName = o.structureName;
        this.latitude = o.latitude;
        this.longitude = o.longitude;
        this.primarySector = o.primarySector;
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

    public Map<String, Object> getMatchesFilters() {
        return matchesFilters;
    }

    public void setMatchesFilters(Map<String, Object>  matchesFilters) {
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
    
    @Override
    public String toString() {
        StringBuilder bld = new StringBuilder();
        if (ampId != null) bld.append("ampId: [" + ampId + "], ");
        if (geoCode != null) bld.append("geocode: [" + geoCode + "], ");
        if (implementationLevel != null) bld.append("impl. level: [" + implementationLevel + "], ");
        if (name != null) bld.append("name: [" + name + "], ");
        if (id != null) bld.append("id: ["+ id + "], ");
        if (description != null) bld.append("description: [" + description + "], ");
        if (ampUrl != null) bld.append("ampUrl: [" + ampUrl + "], ");
        if (donorAgency != null) bld.append("donorAgency: [" + donorAgency + "], ");

        if (totalCommitments != null) bld.append("totalCommitments: [" + totalCommitments + "], ");
        if (totalDisbursments != null) bld.append("totalDisbursements: [" + totalDisbursments + "], ");
        if (totalCommitmentsForActivity != null) bld.append("totalcommforact: [" + totalCommitmentsForActivity + "], ");
        if (totalDisbursmentsForActivity != null) bld.append("totaldisbforact: [" + totalDisbursmentsForActivity + "], ");
        
        if (locationName != null) bld.append("locname: [" + locationName + "], ");
        if (structureName != null) bld.append("structname: [" + structureName + "], ");
        if (latitude != null) bld.append("lat: [" + latitude + "], ");
        if (longitude != null) bld.append("long: [" + longitude + "], ");
        if (primarySector != null) bld.append("prisect: [" + primarySector + "], ");
        return bld.toString();
    }
}
