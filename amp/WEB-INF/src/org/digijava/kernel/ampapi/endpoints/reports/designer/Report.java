package org.digijava.kernel.ampapi.endpoints.reports.designer;

import com.fasterxml.jackson.annotation.*;
import io.swagger.annotations.ApiModelProperty;
import org.dgfoundation.amp.ar.dbentity.AmpFilterData;
import org.dgfoundation.amp.newreports.ReportFilters;
import org.digijava.kernel.ampapi.endpoints.common.EPConstants;
import org.digijava.kernel.ampapi.endpoints.settings.Settings;
import org.digijava.module.aim.dbentity.*;

import java.util.Set;

/**
 * @author Viorel Chihai
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class Report {

    private Long id;

    @ApiModelProperty("Mandatory to be provided for reports.")
    private String name;

    private String description;

    @ApiModelProperty(value = "Can be on of \"D\" (Donor), \"C\" (Component), \"P\" (Pledge). "
            + "Default is \"D\" if not provided.", allowableValues = "D,C,P,R,G")
    private ReportType type;

    @ApiModelProperty(value = "Time frame by which to group funding data in the report. If not specified only totals "
            + "will be computed.\n* A - Anually\n* Q - Quarterly\n* M - Monthly",
            example = "A", allowableValues = "A, Q, M")
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

    @ApiModelProperty(value = "Allow empty funding columns for year, quarter and month.")
    private Boolean splitByFunding;

    @ApiModelProperty(value = "Report category id.")
    private Long reportCategory;

    @JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "ampTeamMemId",
            resolver = EntityResolver.class, scope = AmpTeamMember.class)
    @JsonIdentityReference(alwaysAsId = true)
    @ApiModelProperty(value = "The member that created the report.")
    private AmpTeamMember ownerId;

    @ApiModelProperty(value = "Report columns.")
    private Set<AmpReportColumn> columns;

    @ApiModelProperty(value = "Report hierarchies.")
    private Set<AmpReportHierarchy> hierarchies;

    @ApiModelProperty(value = "Report measures.")
    private Set<AmpReportMeasures> measures;

    @JsonProperty(EPConstants.SETTINGS)
    @ApiModelProperty(value = "Report settings.")
    private Settings settings;

    @JsonProperty(EPConstants.FILTERS)
    @ApiModelProperty(dataType = "org.digijava.kernel.ampapi.swagger.types.FiltersPH")
    private ReportFilters filters;

    private Set<AmpFilterData> filterDataSet;

    public Long getId() {
        return id;
    }

    public void setId(final Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(final String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(final String description) {
        this.description = description;
    }

    public ReportType getType() {
        return type;
    }

    public void setType(final ReportType type) {
        this.type = type;
    }

    public Boolean isSummary() {
        return summary;
    }

    public void setSummary(Boolean summary) {
        this.summary = summary;
    }

    public Boolean isTab() {
        return tab;
    }

    public void setTab(final Boolean tab) {
        this.tab = tab;
    }

    public Boolean isPublicView() {
        return publicView;
    }

    public void setPublicView(final Boolean publicView) {
        this.publicView = publicView;
    }

    public Boolean isWorkspaceLinked() {
        return workspaceLinked;
    }

    public void setWorkspaceLinked(final Boolean workspaceLinked) {
        this.workspaceLinked = workspaceLinked;
    }

    public Boolean isAlsoShowPledges() {
        return alsoShowPledges;
    }

    public void setAlsoShowPledges(final Boolean alsoShowPledges) {
        this.alsoShowPledges = alsoShowPledges;
    }

    public Boolean isShowOriginalCurrency() {
        return showOriginalCurrency;
    }

    public void setShowOriginalCurrency(Boolean showOriginalCurrency) {
        this.showOriginalCurrency = showOriginalCurrency;
    }

    public Boolean isAllowEmptyFundingColumns() {
        return allowEmptyFundingColumns;
    }

    public void setAllowEmptyFundingColumns(final Boolean allowEmptyFundingColumns) {
        this.allowEmptyFundingColumns = allowEmptyFundingColumns;
    }

    public Boolean isSplitByFunding() {
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

    public String getGroupingOption() {
        return groupingOption;
    }

    public void setGroupingOption(String groupingOption) {
        this.groupingOption = groupingOption;
    }

    public ReportFilters getFilters() {
        return filters;
    }

    public void setFilters(ReportFilters filters) {
        this.filters = filters;
    }

    public Settings getSettings() {
        return settings;
    }

    public void setSettings(final Settings settings) {
        this.settings = settings;
    }

    public Set<AmpReportColumn> getColumns() {
        return columns;
    }

    public void setColumns(final Set<AmpReportColumn> columns) {
        this.columns = columns;
    }

    public Set<AmpReportHierarchy> getHierarchies() {
        return hierarchies;
    }

    public void setHierarchies(final Set<AmpReportHierarchy> hierarchies) {
        this.hierarchies = hierarchies;
    }

    public Set<AmpReportMeasures> getMeasures() {
        return measures;
    }

    public void setMeasures(final Set<AmpReportMeasures> measures) {
        this.measures = measures;
    }
}
