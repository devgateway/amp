package org.digijava.kernel.ampapi.endpoints.dto;

import java.util.Map;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModelProperty;
import org.dgfoundation.amp.ar.ColumnConstants;
import org.dgfoundation.amp.ar.MeasureConstants;

/**
 * DTO object that holds information about activity. It is used in GIS (/gis/activities)
 *
 * @author Viorel Chihai
 *
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class GisActivity {
    
    @JsonProperty(ColumnConstants.ACTIVITY_ID)
    private Long id;
    
    @JsonProperty(ColumnConstants.PROJECT_TITLE)
    private String projectTitle;
    
    @JsonProperty(ColumnConstants.AMP_ID)
    private String ampId;
    
    private String description;
    
    private String ampUrl;
    
    @JsonProperty(ColumnConstants.DONOR_AGENCY)
    private String donorAgency;
    
    @JsonProperty(ColumnConstants.EXECUTING_AGENCY)
    private String executingAgency;
    
    @JsonProperty(ColumnConstants.PRIMARY_SECTOR)
    private String primarySector;
    
    @JsonProperty(MeasureConstants.ACTUAL_COMMITMENTS)
    private Double actualCommitments;
    
    @JsonProperty(MeasureConstants.ACTUAL_DISBURSEMENTS)
    private Double actualDisbursements;
    
    @JsonProperty(MeasureConstants.PLANNED_COMMITMENTS)
    private Double plannedCommitments;
    
    @JsonProperty(MeasureConstants.PLANNED_DISBURSEMENTS)
    private Double plannedDisbursements;
    
    @JsonProperty(MeasureConstants.BILATERAL_SSC_COMMITMENTS)
    private Double bilateralSSCCommitments;
    
    @JsonProperty(MeasureConstants.TRIANGULAR_SSC_COMMITMENTS)
    private Double triangularSSCCommitments;

    @ApiModelProperty("explains why activity was included")
    private Map<String, Object> matchesFilters;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getProjectTitle() {
        return projectTitle;
    }

    public void setProjectTitle(String projectTitle) {
        this.projectTitle = projectTitle;
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

    public Map<String, Object> getMatchesFilters() {
        return matchesFilters;
    }

    public void setMatchesFilters(Map<String, Object> matchesFilters) {
        this.matchesFilters = matchesFilters;
    }

    public String getAmpId() {
        return ampId;
    }

    public void setAmpId(String ampId) {
        this.ampId = ampId;
    }

    public String getDonorAgency() {
        return donorAgency;
    }

    public void setDonorAgency(String donorAgency) {
        this.donorAgency = donorAgency;
    }
    
    public String getExecutingAgency() {
        return executingAgency;
    }
    
    public void setExecutingAgency(String executingAgency) {
        this.executingAgency = executingAgency;
    }
    
    public String getPrimarySector() {
        return primarySector;
    }

    public void setPrimarySector(String primarySector) {
        this.primarySector = primarySector;
    }

    public Double getActualCommitments() {
        return actualCommitments;
    }
    
    public void setActualCommitments(Double actualCommitments) {
        this.actualCommitments = actualCommitments;
    }
    
    public Double getActualDisbursements() {
        return actualDisbursements;
    }
    
    public void setActualDisbursements(Double actualDisbursements) {
        this.actualDisbursements = actualDisbursements;
    }
    
    public Double getPlannedCommitments() {
        return plannedCommitments;
    }
    
    public void setPlannedCommitments(Double plannedCommitments) {
        this.plannedCommitments = plannedCommitments;
    }
    
    public Double getPlannedDisbursements() {
        return plannedDisbursements;
    }
    
    public void setPlannedDisbursements(Double plannedDisbursements) {
        this.plannedDisbursements = plannedDisbursements;
    }
    
    public Double getBilateralSSCCommitments() {
        return bilateralSSCCommitments;
    }
    
    public void setBilateralSSCCommitments(Double bilateralSSCCommitments) {
        this.bilateralSSCCommitments = bilateralSSCCommitments;
    }
    
    public Double getTriangularSSCCommitments() {
        return triangularSSCCommitments;
    }
    
    public void setTriangularSSCCommitments(Double triangularSSCCommitments) {
        this.triangularSSCCommitments = triangularSSCCommitments;
    }
}
