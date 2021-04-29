package org.digijava.kernel.ampapi.endpoints.reports.designer;

/**
 * @author Viorel Chihai
 */
public class ReportOption {

    private String name;

    private String label;

    private String description;

    private boolean visible;

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

    public boolean isVisible() {
        return visible;
    }

    public void setVisible(final boolean visible) {
        this.visible = visible;
    }
}
