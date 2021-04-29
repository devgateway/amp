package org.digijava.kernel.ampapi.endpoints.reports.designer;

/**
 * @author Viorel Chihai
 */
public class ReportColumn {

    private Long id;

    private String name;

    private String label;

    private String description;

    private String category;

    private boolean hierarchy;

    private boolean amount;

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
