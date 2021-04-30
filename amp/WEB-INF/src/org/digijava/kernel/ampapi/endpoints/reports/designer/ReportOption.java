package org.digijava.kernel.ampapi.endpoints.reports.designer;

import io.swagger.annotations.ApiModelProperty;

/**
 * @author Viorel Chihai
 */
public class ReportOption {

    @ApiModelProperty(value = "The name of the option", example = "totals-only")
    private String name;

    @ApiModelProperty(value = "The displayed label of the option", example = "Totals Only")
    private String label;

    @ApiModelProperty(value = "The description of the option", example = "Sample Description")
    private String description;

    @ApiModelProperty(value = "If the option is visible in the report designer", example = "true")
    private boolean visible = true;

    public ReportOption(final String name, final String label, final String description) {
        this.name = name;
        this.label = label;
        this.description = description;
    }

    public String getName() {
        return name;
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

    public boolean isVisible() {
        return visible;
    }

    public void setVisible(final boolean visible) {
        this.visible = visible;
    }
}
