package org.digijava.kernel.ampapi.endpoints.reports.designer;

/**
 * @author Viorel Chihai
 */
public class ReportMeasure {

    private Long id;

    private String name;

    private String label;

    private String description;

    private ReportMeasureType type;

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
