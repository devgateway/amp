package org.digijava.kernel.ampapi.endpoints.reports.designer;

import io.swagger.annotations.ApiModelProperty;

/**
 * @author Viorel Chihai
 */
public class ReportColumn {

    @ApiModelProperty(value = "The id of the column")
    private Long id;

    @ApiModelProperty(value = "The name of the column", example = "Approval Status")
    private String name;

    @ApiModelProperty(value = "The translated name of the column", example = "Status")
    private String label;

    @ApiModelProperty(value = "The description of the column", example = "Column Description")
    private String description;

    @ApiModelProperty(value = "The category group where the column is part of", example = "Identification")
    private String category;

    @ApiModelProperty(value = "If the column can be used as hierarchy")
    private boolean hierarchy;

    @ApiModelProperty(value = "If the column values contains amounts")
    private boolean amount;

    public ReportColumn(final Long id, final String name, final String label, final String description,
                        final String category, final boolean hierarchy, final boolean amount) {
        this.id = id;
        this.name = name;
        this.label = label;
        this.description = description;
        this.category = category;
        this.hierarchy = hierarchy;
        this.amount = amount;
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

    public String getCategory() {
        return category;
    }

    public void setCategory(final String category) {
        this.category = category;
    }

    public boolean isHierarchy() {
        return hierarchy;
    }

    public void setHierarchy(final boolean hierarchy) {
        this.hierarchy = hierarchy;
    }

    public boolean isAmount() {
        return amount;
    }

    public void setAmount(final boolean amount) {
        this.amount = amount;
    }
}
