package org.digijava.kernel.ampapi.endpoints.reports.designer;

import io.swagger.annotations.ApiModelProperty;

/**
 * @author Viorel Chihai
 */
public class ReportMeasure {

    @ApiModelProperty(value = "The id of the measure")
    private Long id;

    @ApiModelProperty(value = "The name of the measure", example = "Actual Commitments")
    private String name;

    @ApiModelProperty(value = "The label of the measure", example = "Actual Commitments")
    private String label;

    @ApiModelProperty(value = "The description of the measure", example = "Measure Description")
    private String description;

    @ApiModelProperty(value = "The measure type", example = "A")
    private ReportMeasureType type;

    public ReportMeasure(final Long id, final String name, final String label, final String description,
                         final ReportMeasureType type) {
        this.id = id;
        this.name = name;
        this.label = label;
        this.description = description;
        this.type = type;
    }

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

    public String getLabel() {
        return label;
    }

    public void setLabel(final String label) {
        this.label = label;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(final String description) {
        this.description = description;
    }

    public ReportMeasureType getType() {
        return type;
    }

    public void setType(final ReportMeasureType type) {
        this.type = type;
    }
}
