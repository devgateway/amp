package org.digijava.kernel.ampapi.endpoints.publicportal;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModelProperty;
import org.digijava.kernel.ampapi.endpoints.common.EPConstants;

import java.util.List;
import java.util.Map;

/**
 * @author Octavian Ciubotaru
 */
public class PublicReportFormParameters {
    @ApiModelProperty("Page number, starting from 1. Default to 1")
    private Integer page;

    @ApiModelProperty("The number of records per page to return. Default will be set to the number configured in AMP. "
            + "Set it to -1 to get the unlimited records, that will provide all records.")
    private Integer recordsPerPage;

    @JsonProperty(EPConstants.REPORT_TYPE)
    @ApiModelProperty(value = "Can be on of \"D\" (Donor), \"C\" (Component), \"P\" (Pledge). "
            + "Default is \"D\" if not provided.", allowableValues = "D,C,P")
    private String reportType;

    @JsonProperty(EPConstants.PROJECT_TYPE)
    @ApiModelProperty(value = "An optional list of projects, mainly used to change usual default project types "
            + "included in the report. Allowed options per report type:\n"
            + " * \"D\" : [\"A\", \"S\", \"P\"], where default is [\"A\", \"S\"] if both are reachable\n"
            + " * \"C\" : [\"A\", \"S\"], where default is [\"A\", \"S\"]\n"
            + " * \"P\" : [\"P\"], where default is [\"P\"]\n\n"
            + "Where \"A\" - standard activity, \"S\" - SSC Activity, \"P\" - pledge</dd>")
    private List<String> projectType;

    @JsonProperty(EPConstants.SETTINGS)
    @ApiModelProperty(dataType = "org.digijava.kernel.ampapi.swagger.types.SettingsPH")
    private Map<String, Object> settings;

    @JsonProperty(EPConstants.FILTERS)
    @ApiModelProperty(dataType = "org.digijava.kernel.ampapi.swagger.types.FiltersPH")
    private Map<String, Object> filters;

    public String getReportType() {
        return reportType;
    }

    public void setReportType(String reportType) {
        this.reportType = reportType;
    }

    public List<String> getProjectType() {
        return projectType;
    }

    public void setProjectType(List<String> projectType) {
        this.projectType = projectType;
    }

    public Map<String, Object> getSettings() {
        return settings;
    }

    public void setSettings(Map<String, Object> settings) {
        this.settings = settings;
    }

    public Map<String, Object> getFilters() {
        return filters;
    }

    public void setFilters(Map<String, Object> filters) {
        this.filters = filters;
    }

    public Integer getPage() {
        return page;
    }

    public void setPage(Integer page) {
        this.page = page;
    }

    public Integer getRecordsPerPage() {
        return recordsPerPage;
    }

    public void setRecordsPerPage(Integer recordsPerPage) {
        this.recordsPerPage = recordsPerPage;
    }
}
