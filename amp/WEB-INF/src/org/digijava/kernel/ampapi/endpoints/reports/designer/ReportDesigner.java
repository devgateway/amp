package org.digijava.kernel.ampapi.endpoints.reports.designer;

import io.swagger.annotations.ApiModelProperty;

import java.util.List;

/**
 * @author Viorel Chihai
 */
public class ReportDesigner {

    @ApiModelProperty(value = "The profile of the report designer", example = "report")
    private ReportProfile profile;

    @ApiModelProperty(value = "The report type", example = "donor")
    private ReportType type;

    @ApiModelProperty(value = "The list contains all the available columns for the selected profile and report type.")
    private List<ReportColumn> columns;

    @ApiModelProperty(value = "The list contains all the available measures for the selected profile and report type.")
    private List<ReportMeasure> measures;

    @ApiModelProperty(value = "The list contains all the available options for the selected profile and report type.")
    private List<ReportOption> options;

    public ReportProfile getProfile() {
        return profile;
    }

    public void setProfile(final ReportProfile profile) {
        this.profile = profile;
    }

    public ReportType getType() {
        return type;
    }

    public void setType(final ReportType type) {
        this.type = type;
    }

    public List<ReportColumn> getColumns() {
        return columns;
    }

    public void setColumns(final List<ReportColumn> columns) {
        this.columns = columns;
    }

    public List<ReportMeasure> getMeasures() {
        return measures;
    }

    public void setMeasures(final List<ReportMeasure> measures) {
        this.measures = measures;
    }

    public List<ReportOption> getOptions() {
        return options;
    }

    public void setOptions(final List<ReportOption> options) {
        this.options = options;
    }
}
