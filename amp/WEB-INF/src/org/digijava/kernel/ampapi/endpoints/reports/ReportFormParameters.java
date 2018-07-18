package org.digijava.kernel.ampapi.endpoints.reports;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.swagger.annotations.ApiModelProperty;
import org.digijava.kernel.ampapi.endpoints.common.EPConstants;
import org.digijava.kernel.ampapi.endpoints.reports.saiku.SortParam;

/**
 * @author Octavian Ciubotaru
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class ReportFormParameters {

    @ApiModelProperty("Page number, starting from 1. Use 0 to retrieve only pagination information, "
            + "without any records. Default to 0")
    private Integer page;

    @ApiModelProperty("The number of records per page to return. Default will be set to the number configured in AMP. "
            + "Set it to -1 to get the unlimited records, that will provide all records.")
    private Integer recordsPerPage;

    @JsonProperty(EPConstants.RAW_VALUES)
    @ApiModelProperty("Which elements must provide raw (unformatted) values, with possible options (default []):\n"
            + "* \"M\": measures")
    private List<String> rawValues;

    @JsonProperty(EPConstants.MD5_TOKEN)
    @ApiModelProperty("Pagination token. Use the same token if only page or recordsPerPage changed.")
    private String md5;

    @JsonProperty(EPConstants.IS_CUSTOM)
    private Boolean custom;

    /**
     * Used internally to designate session reports.
     */
    @JsonIgnore
    private Boolean dynamic;

    @JsonProperty(EPConstants.REPORT_NAME)
    @ApiModelProperty("Mandatory to be provided for custom reports, otherwise it is skipped.")
    private String reportName;

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

    @JsonProperty(EPConstants.ADD_COLUMNS)
    @ApiModelProperty("Columns names")
    private List<String> additionalColumns;

    @JsonProperty(EPConstants.ADD_HIERARCHIES)
    @ApiModelProperty("Column names to be used as hierarchy. If a column is selected as hierarchy, then it "
            + "must be included in " + EPConstants.ADD_COLUMNS + " as well.")
    private List<String> additionalHierarchies;

    @JsonProperty(EPConstants.ADD_MEASURES)
    @ApiModelProperty("Measure names")
    private List<String> additionalMeasures;

    @JsonProperty(EPConstants.SETTINGS)
    @ApiModelProperty(dataType = "org.digijava.kernel.ampapi.swagger.types.SettingsPH")
    private Map<String, Object> settings;

    @JsonProperty(EPConstants.FILTERS)
    @ApiModelProperty(dataType = "org.digijava.kernel.ampapi.swagger.types.FiltersPH")
    private Map<String, Object> filters;

    @JsonProperty(EPConstants.SORTING)
    private List<SortParam> sorting;

    @JsonProperty(EPConstants.SHOW_EMPTY_ROWS)
    @ApiModelProperty("To show rows with empty measures amounts. Default false.")
    private Boolean showEmptyRows;

    @JsonProperty(EPConstants.SHOW_EMPTY_COLUMNS)
    @ApiModelProperty("To show full column groups (by quarter, year) with empty measures amounts. Default false.")
    private Boolean showEmptyColumnGroups;

    @JsonProperty(EPConstants.FORCE_HEADERS)
    @ApiModelProperty("If the report query returns empty response the list of column headers is populated "
            + "from the request. Default is false.")
    private Boolean forceHeaders;

    @JsonProperty(EPConstants.GROUPING_OPTION)
    @ApiModelProperty(value = "Time frame by which to group funding data in the report. If not specified only totals "
            + "will be computed.\n* A - Anually\n* Q - Quarterly\n* M - Monthly",
            example = "A", allowableValues = "A, Q, M")
    private String groupingOption;

    @JsonProperty(EPConstants.SUMMARY)
    private Boolean summary;

    @JsonProperty(EPConstants.SHOW_ORIGINAL_CURRENCY)
    private Boolean showOriginalCurrency;

    @JsonProperty(EPConstants.COLUMNS_WITH_IDS)
    @ApiModelProperty("Columns names that should also provide ids.")
    private List<String> columnsWithIds;

    @JsonProperty(EPConstants.INFO)
    @ApiModelProperty("Additional information to include [\"stats\", \"warnings\", \"generatedHeaders\"]. "
            + "Default is [] (none).")
    private List<String> additionalInfo;

    @ApiModelProperty(
            value = "JQGrid style sort param. Comma separated list of columns and optionally the diresction.",
            example = "Project Title desc, Donor Agency")
    private String sidx;

    @ApiModelProperty(
            value = "JQGrid style sort param. Default order when not specified at column level.",
            allowableValues = "asc, desc")
    private String sord;

    public static ReportFormParameters fromString(String value) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            return objectMapper.readValue(value, ReportFormParameters.class);
        } catch (IOException e) {
            throw new IllegalArgumentException("Cannot deserialize: " + value, e);
        }
    }

    public String getSidx() {
        return sidx;
    }

    public void setSidx(String sidx) {
        this.sidx = sidx;
    }

    public String getSord() {
        return sord;
    }

    public void setSord(String sord) {
        this.sord = sord;
    }

    public List<String> getAdditionalInfo() {
        return additionalInfo;
    }

    public void setAdditionalInfo(List<String> additionalInfo) {
        this.additionalInfo = additionalInfo;
    }

    public List<String> getColumnsWithIds() {
        return columnsWithIds;
    }

    public void setColumnsWithIds(List<String> columnsWithIds) {
        this.columnsWithIds = columnsWithIds;
    }

    public Boolean getShowOriginalCurrency() {
        return showOriginalCurrency;
    }

    public void setShowOriginalCurrency(Boolean showOriginalCurrency) {
        this.showOriginalCurrency = showOriginalCurrency;
    }

    public Boolean getSummary() {
        return summary;
    }

    public void setSummary(Boolean summary) {
        this.summary = summary;
    }

    public String getGroupingOption() {
        return groupingOption;
    }

    public void setGroupingOption(String groupingOption) {
        this.groupingOption = groupingOption;
    }

    public Boolean getForceHeaders() {
        return forceHeaders;
    }

    public void setForceHeaders(Boolean forceHeaders) {
        this.forceHeaders = forceHeaders;
    }

    public Boolean getShowEmptyColumnGroups() {
        return showEmptyColumnGroups;
    }

    public void setShowEmptyColumnGroups(Boolean showEmptyColumnGroups) {
        this.showEmptyColumnGroups = showEmptyColumnGroups;
    }

    public Boolean getShowEmptyRows() {
        return showEmptyRows;
    }

    public void setShowEmptyRows(Boolean showEmptyRows) {
        this.showEmptyRows = showEmptyRows;
    }

    public List<String> getProjectType() {
        return projectType;
    }

    public void setProjectType(List<String> projectType) {
        this.projectType = projectType;
    }

    public List<SortParam> getSorting() {
        return sorting;
    }

    public void setSorting(List<SortParam> sorting) {
        this.sorting = sorting;
    }

    public Map<String, Object> getFilters() {
        return filters;
    }

    public void setFilters(Map<String, Object> filters) {
        this.filters = filters;
    }

    public Map<String, Object> getSettings() {
        return settings;
    }

    public void setSettings(Map<String, Object> settings) {
        this.settings = settings;
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

    public List<String> getRawValues() {
        return rawValues;
    }

    public void setRawValues(List<String> rawValues) {
        this.rawValues = rawValues;
    }

    public String getMd5() {
        return md5;
    }

    public void setMd5(String md5) {
        this.md5 = md5;
    }

    public Boolean getCustom() {
        return custom;
    }

    public void setCustom(Boolean custom) {
        this.custom = custom;
    }

    public Boolean getDynamic() {
        return dynamic;
    }

    public void setDynamic(Boolean dynamic) {
        this.dynamic = dynamic;
    }

    public String getReportName() {
        return reportName;
    }

    public void setReportName(String reportName) {
        this.reportName = reportName;
    }

    public String getReportType() {
        return reportType;
    }

    public void setReportType(String reportType) {
        this.reportType = reportType;
    }

    public List<String> getAdditionalColumns() {
        return additionalColumns;
    }

    public void setAdditionalColumns(List<String> additionalColumns) {
        this.additionalColumns = additionalColumns;
    }

    public List<String> getAdditionalHierarchies() {
        return additionalHierarchies;
    }

    public void setAdditionalHierarchies(List<String> additionalHierarchies) {
        this.additionalHierarchies = additionalHierarchies;
    }

    public List<String> getAdditionalMeasures() {
        return additionalMeasures;
    }

    public void setAdditionalMeasures(List<String> additionalMeasures) {
        this.additionalMeasures = additionalMeasures;
    }
}
