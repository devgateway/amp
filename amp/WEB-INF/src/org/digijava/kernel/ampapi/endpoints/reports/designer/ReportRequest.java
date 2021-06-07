package org.digijava.kernel.ampapi.endpoints.reports.designer;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIdentityReference;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import io.swagger.annotations.ApiModelProperty;
import org.digijava.kernel.ampapi.endpoints.common.EPConstants;
import org.digijava.kernel.ampapi.endpoints.dto.MultilingualContent;
import org.digijava.module.aim.dbentity.AmpTeamMember;
import org.digijava.module.aim.dbentity.EntityResolver;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static java.lang.Boolean.TRUE;

/**
 * @author Viorel Chihai
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class ReportRequest {

    @ApiModelProperty("Mandatory to be provided for reports.")
    private MultilingualContent name;

    private String description;

    @ApiModelProperty(value = "Can be on of \"D\" (Donor), \"C\" (Component), \"P\" (Pledge), \"R\" (Regional),"
            + " \"G\" (GPI). Default is \"D\" if not provided.", allowableValues = "D,C,P,R,G")
    private String type;

    @ApiModelProperty(value = "Time frame by which to group funding data in the report. If not specified only totals "
            + "will be computed.\n* A - Anually\n* Q - Quarterly\n* M - Monthly\n* N - Totals Only",
            example = "A", allowableValues = "A, Q, M, N")
    private String groupingOption;

    @ApiModelProperty(value = "To hide or not the activities in the report.")
    private Boolean summary;

    @ApiModelProperty(value = "If the report is a tab or not.")
    private Boolean tab;

    @ApiModelProperty(value = "If the report is visible to public view.")
    private Boolean publicView;

    @ApiModelProperty(value = "Link public view with creating workspace.")
    private Boolean workspaceLinked;

    @ApiModelProperty(value = "Also show pledges.")
    private Boolean alsoShowPledges;

    @ApiModelProperty(value = "Show Original reporting currencies.")
    private Boolean showOriginalCurrency;

    @ApiModelProperty(value = "Allow empty funding columns for year, quarter and month.")
    private Boolean allowEmptyFundingColumns;

    @ApiModelProperty(value = "Split by funding.")
    private Boolean splitByFunding;

    @ApiModelProperty(value = "Report category id.")
    private Long reportCategory;

    @JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "ampTeamMemId",
            resolver = EntityResolver.class, scope = AmpTeamMember.class)
    @JsonIdentityReference(alwaysAsId = true)
    @ApiModelProperty(value = "The member that created the report.")
    private AmpTeamMember ownerId;

    @ApiModelProperty(value = "Include location children.")
    private Boolean includeLocationChildren;

    @ApiModelProperty(value = "Report column ids.")
    private List<Long> columns;

    @ApiModelProperty(value = "Report hierarchies.")
    private List<Long> hierarchies;

    @ApiModelProperty(value = "Report measures.")
    private List<Long> measures;

    @JsonProperty(EPConstants.FILTERS)
    @ApiModelProperty(dataType = "org.digijava.kernel.ampapi.swagger.types.FiltersPH")
    private Map<String, Object> filters = new HashMap<>();

    @JsonProperty(EPConstants.SETTINGS)
    @ApiModelProperty(dataType = "org.digijava.kernel.ampapi.swagger.types.SettingsPH")
    private Map<String, Object> settings;

    private List<Map<String, String>> reportData;

    public MultilingualContent getName() {
        return name;
    }

    public void setName(final MultilingualContent name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(final String description) {
        this.description = description;
    }

    public String getType() {
        return type;
    }

    public void setType(final String type) {
        this.type = type;
    }

    public String getGroupingOption() {
        return groupingOption;
    }

    public void setGroupingOption(final String groupingOption) {
        this.groupingOption = groupingOption;
    }

    public Boolean getSummary() {
        return summary;
    }

    public Boolean isSummary() {
        return TRUE.equals(summary);
    }

    public void setSummary(final Boolean summary) {
        this.summary = summary;
    }

    public Boolean getTab() {
        return tab;
    }

    public Boolean isTab() {
        return TRUE.equals(tab);
    }

    public void setTab(final Boolean tab) {
        this.tab = tab;
    }

    public Boolean getPublicView() {
        return publicView;
    }

    public void setPublicView(final Boolean publicView) {
        this.publicView = publicView;
    }

    public Boolean getWorkspaceLinked() {
        return workspaceLinked;
    }

    public void setWorkspaceLinked(final Boolean workspaceLinked) {
        this.workspaceLinked = workspaceLinked;
    }

    public Boolean getAlsoShowPledges() {
        return alsoShowPledges;
    }

    public void setAlsoShowPledges(final Boolean alsoShowPledges) {
        this.alsoShowPledges = alsoShowPledges;
    }

    public Boolean getShowOriginalCurrency() {
        return showOriginalCurrency;
    }

    public void setShowOriginalCurrency(final Boolean showOriginalCurrency) {
        this.showOriginalCurrency = showOriginalCurrency;
    }

    public Boolean getAllowEmptyFundingColumns() {
        return allowEmptyFundingColumns;
    }

    public void setAllowEmptyFundingColumns(final Boolean allowEmptyFundingColumns) {
        this.allowEmptyFundingColumns = allowEmptyFundingColumns;
    }

    public Boolean getSplitByFunding() {
        return splitByFunding;
    }

    public void setSplitByFunding(final Boolean splitByFunding) {
        this.splitByFunding = splitByFunding;
    }

    public Long getReportCategory() {
        return reportCategory;
    }

    public void setReportCategory(final Long reportCategory) {
        this.reportCategory = reportCategory;
    }

    public AmpTeamMember getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(final AmpTeamMember ownerId) {
        this.ownerId = ownerId;
    }

    public List<Long> getColumns() {
        return columns;
    }

    public void setColumns(final List<Long> columns) {
        this.columns = columns;
    }

    public List<Long> getHierarchies() {
        return hierarchies;
    }

    public void setHierarchies(final List<Long> hierarchies) {
        this.hierarchies = hierarchies;
    }

    public List<Long> getMeasures() {
        return measures;
    }

    public void setMeasures(final List<Long> measures) {
        this.measures = measures;
    }

    public Map<String, Object> getFilters() {
        return filters;
    }

    public void setFilters(final Map<String, Object> filters) {
        this.filters = filters;
    }

    public Map<String, Object> getSettings() {
        return settings;
    }

    public void setSettings(final Map<String, Object> settings) {
        this.settings = settings;
    }

    public List<Map<String, String>> getReportData() {
        return reportData;
    }

    public void setReportData(final List<Map<String, String>> reportData) {
        this.reportData = reportData;
    }

    public Boolean isIncludeLocationChildren() {
        return includeLocationChildren;
    }

    public void setIncludeLocationChildren(final Boolean includeLocationChildren) {
        this.includeLocationChildren = includeLocationChildren;
    }
}
